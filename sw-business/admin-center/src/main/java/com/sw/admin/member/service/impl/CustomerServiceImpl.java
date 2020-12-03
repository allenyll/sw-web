package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.admin.member.util.AESUtil;
import com.sw.admin.pay.properties.WxProperties;
import com.sw.cache.util.CacheUtil;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.CacheKeys;
import com.sw.common.constants.dict.UserStatus;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerBalance;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.entity.customer.CustomerPointDetail;
import com.sw.common.entity.wechat.WxCodeResponse;
import com.sw.common.util.*;
import com.sw.admin.member.mapper.CustomerMapper;
import com.sw.admin.member.mapper.CustomerPointDetailMapper;
import com.sw.admin.member.mapper.CustomerPointMapper;
import com.sw.admin.member.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author yu.leilei
 * @since 2018-10-22
 */
@Slf4j
@Service("customerService")
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CustomerPointMapper customerPointMapper;

    @Autowired
    CustomerPointDetailMapper customerPointDetailMapper;

    @Autowired
    CustomerBalanceServiceImpl customerBalanceService;

    @Autowired
    WxProperties wxProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheUtil cacheUtil;


    @Transactional(rollbackFor=Exception.class)
    public void loginOrRegisterConsumer(Customer customer) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("OPENID", customer.getOpenid());
        wrapper.eq("STATUS", UserStatus.OK.getCode());
        wrapper.eq("IS_DELETE", 0);
        List<Customer> customerList = customerMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(customerList)){
            Long id = SnowflakeIdWorker.generateId();
            String date = DateUtil.getCurrentDateTime();
            customer.setId(id);
            customer.setIsDelete(0);
            customer.setStatus(UserStatus.OK.getCode());
            customer.setAddTime(date);
            customer.setUpdateTime(date);
            String gender = "";
            if("1".equals(customer.getGender())){
                gender = "SW0201";
            } else if("0".equals(customer.getGender())){
                gender = "SW0202";
            }
            customer.setGender(gender);
            customer.setOpenid(customer.getOpenid());
            customer.setEmail(customer.getEmail());
            customer.setPhone(customer.getPhone());
            customer.setCountry(customer.getCountry());
            customer.setProvince(customer.getProvince());
            customer.setCity(customer.getCity());
            customer.setAvatarUrl(customer.getAvatarUrl());
            customer.setAddUser(id);
            customer.setUpdateTime(DateUtil.getCurrentDateTime());
            customer.setCustomerAccount(customer.getNickName());
            customer.setCustomerName(customer.getNickName());

            // 注册送积分

            try {
                customerMapper.insert(customer);
                sendPoint(customer);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

    @Override
    public Customer selectUserByName(String userName) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("CUSTOMER_NAME", userName);
        return customerMapper.selectOne(wrapper);
    }

    @Override
    public Result<Customer> updateCustomer(Customer customer) {
        Result<Customer> result = new Result<>();
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("OPENID", customer.getOpenid());
        wrapper.eq("STATUS", UserStatus.OK.getCode());
        wrapper.eq("IS_DELETE", 0);
        Customer hasCustomer = customerMapper.selectOne(wrapper);
        if (hasCustomer != null) {
            String gender = "";
            if("1".equals(customer.getGender())){
                gender = "SW0201";
            } else if("0".equals(customer.getGender())){
                gender = "SW0202";
            }
            hasCustomer.setGender(gender);
            hasCustomer.setEmail(customer.getEmail());
            hasCustomer.setPhone(customer.getPhone());
            hasCustomer.setCountry(customer.getCountry());
            hasCustomer.setProvince(customer.getProvince());
            hasCustomer.setCity(customer.getCity());
            hasCustomer.setAvatarUrl(customer.getAvatarUrl());
            hasCustomer.setAddUser(customer.getId());
            hasCustomer.setUpdateTime(DateUtil.getCurrentDateTime());
            hasCustomer.setCustomerAccount(customer.getNickName());
            hasCustomer.setCustomerName(customer.getNickName());
            hasCustomer.setNickName(customer.getNickName());
            try {
                customerMapper.updateById(hasCustomer);
                // sendPoint(hasCustomer);
            } catch (Exception e) {
                e.printStackTrace();
                result.fail("授权失败，请联系管理员！");
                return result;
            }
        } else {
            try {
                loginOrRegisterConsumer(customer);
            } catch (Exception e) {
                e.printStackTrace();
                result.fail("注册发生失败，请联系管理员！");
                return result;
            }
        }
        result.setObject(customer);
        return result;
    }

    @Override
    public Result<Customer> queryUserByOpenId(String openid) {
        Result<Customer> result = new Result<>();
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("OPENID", openid);
        wrapper.eq("STATUS", UserStatus.OK.getCode());
        wrapper.eq("IS_DELETE", 0);
        Customer customer = customerMapper.selectOne(wrapper);

        if (customer != null) {
            QueryWrapper<CustomerBalance> balanceQueryWrapper = new QueryWrapper<>();
            wrapper.eq("IS_DELETE", 0);
            wrapper.eq("CUSTOMER_ID", customer.getId());
            CustomerBalance customerBalance = customerBalanceService.getOne(balanceQueryWrapper);
            customer.setCustomerBalance(customerBalance);

            QueryWrapper<CustomerPoint> customerPointEntityWrapper = new QueryWrapper<>();
            customerPointEntityWrapper.eq("IS_DELETE", 0);
            customerPointEntityWrapper.eq("CUSTOMER_ID", customer.getId());
            CustomerPoint customerPoint = customerPointMapper.selectOne(customerPointEntityWrapper);
            customer.setCustomerPoint(customerPoint);
            result.setObject(customer);
            return result;
        } else {
            result.fail("没有找到用户，登陆失败!");
            return result;
        }
    }

    @Override
    public Result<Customer> getPhoneNumber(Map<String, Object> params) {
        Result<Customer> result = new Result<>();
        String code = MapUtil.getMapValue(params, "code");
        String encryptedData = MapUtil.getMapValue(params, "encryptedData");
        String iv = MapUtil.getMapValue(params, "iv");
        WxCodeResponse response = getWxCodeSession(code);
        String str = AESUtil.wxDecrypt(encryptedData, response.getSessionKey(), iv);
        JSONObject json = JSONObject.fromObject(str);
        String phoneNumber = json.getString("phoneNumber");
        String currentOpenId = cacheUtil.get(CacheKeys.WX_CURRENT_OPENID + "_" + response.getOpenid());
        Customer customer = null;
        if(StringUtil.isNotEmpty(phoneNumber)){
            if(response.getOpenid().equals(currentOpenId)){
                QueryWrapper<Customer> wrapper = new QueryWrapper<>();
                wrapper.eq("OPENID", currentOpenId);
                wrapper.eq("STATUS", UserStatus.OK.getCode());
                wrapper.eq("IS_DELETE", 0);
                customer = customerMapper.selectOne(wrapper);
                if(customer != null ){
                    customer.setPhone(phoneNumber);
                    customerMapper.updateById(customer);
                }
            }
        }
        result.setObject(customer);
        return result;
    }

    @Override
    public Result<Customer> updateCustomerAccount(Map<String, Object> params) {
        String openid = MapUtil.getMapValue(params, "openid");
        String customerAccount = MapUtil.getMapValue(params, "customerAccount");
        String sex = MapUtil.getMapValue(params, "sex");
        String email = MapUtil.getMapValue(params, "email");

        Result<Customer> result = queryUserByOpenId(openid);

        if(result.isSuccess()){
            Customer customer = result.getObject();
            if(StringUtil.isNotEmpty(customerAccount)){
                customer.setCustomerAccount(customerAccount);
            }
            if(StringUtil.isNotEmpty(sex)){
                String gender = "";
                if(sex.equals("1")){
                    gender = "SW0201";
                } else if(sex.equals("0")){
                    gender = "SW0202";
                }
                customer.setGender(gender);
            }
            if(StringUtil.isNotEmpty(email)){
                customer.setEmail(email);
            }
            customer.setUpdateTime(DateUtil.getCurrentDateTime());
            customerMapper.updateById(customer);
            result.setObject(customer);
            return result;
        }
        return result;
    }

    /**
     * 注册赠送积分
     * @param customer
     */
    private void sendPoint(Customer customer) {
        // 注册送积分
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.setId(SnowflakeIdWorker.generateId());
        customerPoint.setCustomerId(customer.getId());
        customerPoint.setPoint(5);
        customerPoint.setUsed(0);

        CustomerPointDetail customerPointDetail = new CustomerPointDetail();
        customerPointDetail.setId(SnowflakeIdWorker.generateId());
        customerPointDetail.setCustomerId(customer.getId());
        customerPointDetail.setPoint(5);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);
        Date time = calendar.getTime();
        customerPointDetail.setExpiredTime(FORMAT.format(time));
        customerPointDetail.setIsDelete(0);
        customerPointDetail.setType("SW0501");
        customerPointDetail.setGetTime(FORMAT.format(time));
        customerPointDetail.setRemark("用户" + customer.getNickName() + "注册赠送5个积分!");

        try {
            customerPointMapper.insert(customerPoint);
            customerPointDetailMapper.insert(customerPointDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * code2session
     * @param code
     * @return
     */
    private WxCodeResponse getWxCodeSession(String code) {

        String urlString = "?appid={appid}&secret={secret}&js_code={code}&grant_type={grantType}";
        Map<String, Object> map = new HashMap<>();
        map.put("appid", wxProperties.getAppId());
        map.put("secret", wxProperties.getAppSecret());
        map.put("code", code);
        map.put("grantType", wxProperties.getGrantType());

        String response = restTemplate.getForObject(wxProperties.getSessionHost() + urlString, String.class, map);

        ObjectMapper objectMapper = new ObjectMapper();
        WxCodeResponse wxCodeResponse;
        try {
            wxCodeResponse = objectMapper.readValue(response, WxCodeResponse.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            wxCodeResponse = null;
            e.printStackTrace();
        }

        log.info(wxCodeResponse.toString());
        if (null == wxCodeResponse) {
            throw new RuntimeException("调用微信接口失败");
        }
        if (wxCodeResponse.getErrcode() != null) {
            throw new RuntimeException(wxCodeResponse.getErrMsg());
        }

        return wxCodeResponse;
    }
}
