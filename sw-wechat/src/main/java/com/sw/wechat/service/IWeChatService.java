package com.sw.wechat.service;

import com.sw.common.entity.customer.Customer;
import com.sw.common.util.DataResponse;
import net.sf.json.JSONObject;

import java.util.Map;

/**
* @Title: IWeChatService
* @Package com.sw.wechat.service
* @Description: 微信服务接口
* @author yu.leilei
* @date 2018/10/19 17:46
* @version V1.0
*/
public interface IWeChatService {

    /**
     * 授权，返回openid
     * @param code
     * @return
     */
    String auth(String code);

    /**
     * 登录
     * @param customer
     * @return
     */
    String login(Customer customer);

    /**
     * 根据微信用户更新用户信息
     * @param customer
     */
    void updateCustomer(Customer customer);

    /**
     * 根据openid查询用户
     * @param openid
     */
    DataResponse queryUserByOpenId(String openid);

    /**
     * 获取用户手机号
     * @return
     */
    JSONObject getPhoneNumber(Map<String, Object> params);
}
