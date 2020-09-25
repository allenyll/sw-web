package com.sw.admin.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.customer.Customer;
import com.sw.common.util.Result;
import net.sf.json.JSONObject;

import java.util.Map;

public interface ICustomerService extends IService<Customer> {

    /**
     * 根据客户名称获取客户
     * @param userName
     * @return
     */
    Customer selectUserByName(String userName);

    /**
     * 更新用户
     * @param customer
     * @return
     */
    Result<Customer> updateCustomer(Customer customer);

    /**
     * 根据openid查询一次用户
     * @param openid
     * @return
     */
    Result<Customer> queryUserByOpenId(String openid);

    /**
     * 获取微信用户手机号，并更新到数据库
     * @param params
     * @return
     */
    Result<Customer> getPhoneNumber(Map<String, Object> params);

    /**
     * 用户更新账户信息
     * @param params
     * @return
     */
    Result<Customer> updateCustomerAccount(Map<String, Object> params);
}
