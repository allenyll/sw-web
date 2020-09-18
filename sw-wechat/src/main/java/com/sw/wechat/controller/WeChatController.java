package com.sw.wechat.controller;

import com.sw.client.feign.CustomerFeignClient;
import com.sw.common.constants.BaseConstants;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.wechat.WxUserInfo;
import com.sw.common.util.DataResponse;
import com.sw.common.util.DateUtil;
import com.sw.common.util.MapUtil;
import com.sw.common.util.StringUtil;
import com.sw.wechat.service.IWeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "微信API", tags = "微信接口")
@RestController
@RequestMapping(value = "wx")
@Slf4j
public class WeChatController {

    @Autowired
    IWeChatService weChatService;

    @Autowired
    CustomerFeignClient customerFeignClient;

    @RequestMapping("/test")
    public void test() {
        System.out.println("test");
    }

    @ApiOperation(value = "微信授权" ,  notes="微信授权")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public DataResponse auth(@RequestParam String code) {
        final String token = weChatService.auth(code);
        DataResponse dataResponse = new DataResponse();
        dataResponse.put("token", token);
        return dataResponse;
    }

    @ApiOperation(value = "微信登录", notes="微信登录")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.POST)
    public void wxLogin(@RequestBody Customer customer){
        weChatService.login(customer);
    }


    @ApiOperation(value = "根据openid查询用户")
    @ResponseBody
    @RequestMapping(value = "queryUserByOpenId", method = RequestMethod.POST)
    public DataResponse queryUserByOpenId(@RequestParam String openid){
        log.info("开始调用查询微信用户openid:" + openid);
        DataResponse dataResponse;
        try {
            dataResponse = weChatService.queryUserByOpenId(openid);
        } catch (Exception e) {
            log.error("查询微信用户异常");
            return DataResponse.fail("查询微信用户异常");
        }
        return dataResponse;
    }


    @ApiOperation(value = "更新用户")
    @RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
    public void updateCustomer(@RequestBody Map<String, Object> params){
        String openid = MapUtil.getMapValue(params, "openid");
        String customerAccount = MapUtil.getMapValue(params, "customerAccount");
        String sex = MapUtil.getMapValue(params, "sex");
        String email = MapUtil.getMapValue(params, "email");

        DataResponse dataResponse = weChatService.queryUserByOpenId(openid);

        String code = MapUtil.getMapValue(dataResponse, "code");

        if(BaseConstants.SUCCESS.equals(code)){
            Customer customer = (Customer) dataResponse.get("customer");
            if(StringUtil.isNotEmpty(customerAccount)){
                customer.setCustomerAccount(customerAccount);
            }
            if(StringUtil.isNotEmpty(sex)){
                customer.setGender(sex);
            }
            if(StringUtil.isNotEmpty(email)){
                customer.setEmail(email);
            }
            customer.setUpdateTime(DateUtil.getCurrentDateTime());
            customerFeignClient.updateById(customer);
        }

    }

    @ApiOperation(value = "获取微信用户手机号，并更新到数据库")
    @ResponseBody
    @RequestMapping(value = "/getPhoneNumber", method = RequestMethod.POST)
    public DataResponse getPhoneNumber(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();
        JSONObject json = weChatService.getPhoneNumber(params);
        result.put("json", json);
        return DataResponse.success(result);
    }

}
