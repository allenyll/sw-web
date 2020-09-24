package com.sw.admin.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.BaseConstants;
import com.sw.common.entity.customer.Customer;
import com.sw.common.util.MapUtil;
import com.sw.admin.member.service.impl.CustomerServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2018-10-22
 */
@Api(value = "用户接口", tags = "用户接口")
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController<CustomerServiceImpl, Customer> {

    @ResponseBody
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public Customer selectOne(@RequestBody Map<String, Object> map) {
        String mark = MapUtil.getString(map, "MARK");
        QueryWrapper<Customer> customerEntityWrapper = new QueryWrapper<>();
        customerEntityWrapper.eq("IS_DELETE", 0);
        if (BaseConstants.SW_WECHAT.equals(mark)) {
            customerEntityWrapper.eq("OPENID", MapUtil.getString(map, "OPENID"));
        } else {
            customerEntityWrapper.eq("id", MapUtil.getLong(map, "CUSTOMER_ID"));
        }
        return service.getOne(customerEntityWrapper);
    }

    @ResponseBody
    @RequestMapping(value = "/selectCustomerById", method = RequestMethod.POST)
    public Customer selectCustomerById(@RequestParam Long fkCustomerId) {
        return service.getById(fkCustomerId);
    }

    @ApiOperation("更新用户信息")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public void updateById(@RequestBody Customer customer) {
        service.updateById(customer);
    }

    @RequestMapping(value = "/loginOrRegisterConsumer", method = RequestMethod.POST)
    public void loginOrRegisterConsumer(@RequestBody Customer customer) {
        service.loginOrRegisterConsumer(customer);
    }

    @RequestMapping(value = "selectUserByName", method = RequestMethod.POST)
    public Customer selectUserByName(@RequestParam String userName) {
        return service.selectUserByName(userName);
    }

}
