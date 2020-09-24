package com.sw.admin.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.customer.Customer;

public interface ICustomerService extends IService<Customer> {

    /**
     * 根据客户名称获取客户
     * @param userName
     * @return
     */
    Customer selectUserByName(String userName);
}
