package com.sw.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cache.util.CacheUtil;
import com.sw.client.feign.CustomerFeignClient;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.CacheKeys;
import com.sw.common.constants.dict.UserStatus;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerBalance;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.entity.wechat.WxCodeResponse;
import com.sw.common.util.*;
import com.sw.wechat.properties.WeChatProperties;
import com.sw.wechat.service.IWeChatService;
import com.sw.wechat.util.AESUtil;
import feign.QueryMap;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
* @Title: WeChatServiceImpl
* @Package com.sw.wechat.service.impl
* @Description: 微信业务实现
* @author yu.leilei
* @date 2018/10/19 17:40
* @version V1.0
*/
@Service("weChatService")
public class WeChatServiceImpl implements IWeChatService {

    private static final Logger LOG = LoggerFactory.getLogger(WeChatServiceImpl.class);

    /**
     * 服务器第三方session有效时间，单位秒, 默认1天
     */
    private static final Long EXPIRES = 86400L;

    private RestTemplate restTemplate = new RestTemplate();

    @Resource
    WeChatProperties weChatProperties;

    @Autowired
    CustomerFeignClient customerFeignClient;

    @Autowired
    CacheUtil cacheUtil;

    @Override
    public String auth(String code) {

        WxCodeResponse wxCodeResponse = getWxCodeSession(code);
        String openid = wxCodeResponse.getOpenid();
        String sessionKey = wxCodeResponse.getSessionKey();

        String token = createToken(sessionKey, openid);
        //redisService.set(WxConstants.WX_JWT_MARK, WxConstants.WX_JWT);
        //redisService.expire(WxConstants.WX_JWT_MARK, EXPIRES);
        return token;
    }

    @Override
    public String login(Customer customer){
        customerFeignClient.loginOrRegisterConsumer(customer);
        return null;
    }

    /**
     * 创建token
     * @param sessionKey
     * @param openid
     * @return
     */
    private String createToken(String sessionKey, String openid) {
        String cacheKey = RandomStringUtils.randomAlphanumeric(64);
        StringBuffer sb = new StringBuffer();
        sb.append(sessionKey + "#" + openid);
        //redisService.set(cacheKey + "#" + openid, sb.toString());
        //redisService.expire(cacheKey + "#" + openid, EXPIRES);
        return cacheKey + "#" + openid;
    }

    /**
     * code2session
     * @param code
     * @return
     */
    private WxCodeResponse getWxCodeSession(String code) {

        LOG.info("获取session_key的code:" + code);
        String urlString = "?appid={appid}&secret={secret}&js_code={code}&grant_type={grantType}";
        Map<String, Object> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppId());
        map.put("secret", weChatProperties.getAppSecret());
        map.put("code", code);
        map.put("grantType", weChatProperties.getGrantType());

        String response = restTemplate.getForObject(weChatProperties.getSessionHost() + urlString, String.class, map);

        ObjectMapper objectMapper = new ObjectMapper();
        WxCodeResponse wxCodeResponse;
        try {
            wxCodeResponse = objectMapper.readValue(response, WxCodeResponse.class);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            wxCodeResponse = null;
            e.printStackTrace();
        }

        LOG.info(wxCodeResponse.toString());
        if (null == wxCodeResponse) {
            throw new RuntimeException("调用微信接口失败");
        }
        if (wxCodeResponse.getErrcode() != null) {
            throw new RuntimeException(wxCodeResponse.getErrMsg());
        }

        return wxCodeResponse;
    }

    @Override
    public void updateCustomer(Customer customer) {
        Map<String, Object> map = new HashMap<>();
        map.put("MARK", BaseConstants.SW_WECHAT);
        map.put("OPENID", AppContext.getCurrentUserWechatOpenId());
        Customer customerExist = customerFeignClient.selectOne(map);
        customerExist.setUpdateTime(DateUtil.getCurrentDateTime());
        customerExist.setGender(customer.getGender());
        customerExist.setOpenid(customer.getOpenid());
        customerExist.setEmail(customer.getEmail());
        customerExist.setPhone(customer.getPhone());
        customerExist.setCountry(customer.getCountry());
        customerExist.setProvince(customer.getProvince());
        customerExist.setCity(customer.getCity());
        customerExist.setAvatarUrl(customer.getAvatarUrl());
        customer.setStatus(UserStatus.OK.getCode());
        customerFeignClient.updateById(customerExist);
    }

    @Override
    public DataResponse queryUserByOpenId(String openid) {
        String currentOpenId = cacheUtil.get(CacheKeys.WX_CURRENT_OPENID + "_" + openid);
        Map<String, Object> map = new HashMap<>();
        if(openid.equals(currentOpenId)){
            Map<String, Object> _map = new HashMap<>();
            _map.put("MARK", BaseConstants.SW_WECHAT);
            _map.put("OPENID", currentOpenId);
            Customer customer = customerFeignClient.selectOne(_map);
            if(customer != null){
                map.put("customer", customer);
                _map.put("FK_CUSTOMER_ID", customer.getPkCustomerId());
                CustomerPoint customerPoint = customerFeignClient.selectCustomerPointOne(_map);
                map.put("customerPoint", customerPoint);
                CustomerBalance customerBalance = customerFeignClient.selectCustomerBalanceOne(_map);
                map.put("customerBalance", customerBalance);
            }else{
                return DataResponse.fail("没有查询到用户！");
            }
        }
        return DataResponse.success(map);
    }

    @Override
    public JSONObject getPhoneNumber(Map<String, Object> params) {
        String code = MapUtil.getMapValue(params, "code");
        String encryptedData = MapUtil.getMapValue(params, "encryptedData");
        String iv = MapUtil.getMapValue(params, "iv");
        WxCodeResponse response = getWxCodeSession(code);
        String str = AESUtil.wxDecrypt(encryptedData, response.getSessionKey(), iv);
        JSONObject json = JSONObject.fromObject(str);
        String phoneNumber = json.getString("phoneNumber");
        String currentOpenId = cacheUtil.get(CacheKeys.WX_CURRENT_OPENID + "_" + response.getOpenid());
        if(StringUtil.isNotEmpty(phoneNumber)){
            if(response.getOpenid().equals(currentOpenId)){
                Map<String, Object> _map = new HashMap<>();
                _map.put("MARK", BaseConstants.SW_WECHAT);
                _map.put("OPENID", currentOpenId);
                Customer customer = customerFeignClient.selectOne(_map);
                if(customer != null ){
                    customer.setPhone(phoneNumber);
                    customerFeignClient.updateById(customer);
                }
            }
        }
        return json;
    }
}
