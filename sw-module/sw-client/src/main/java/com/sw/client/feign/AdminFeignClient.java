package com.sw.client.feign;

import com.sw.client.fallback.AdminFallbackFactory;
import com.sw.client.fallback.CustomerFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerAddress;
import com.sw.common.entity.customer.CustomerBalance;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.market.CouponDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:  feign 会员服务接口
 * @Author:       allenyll
 * @Date:         2020/5/4 5:54 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.ADMIN_SERVICE, fallbackFactory = AdminFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface AdminFeignClient {

    @RequestMapping(value = "customer/selectOne", method = RequestMethod.POST)
    Customer selectOne(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "customer/selectCustomerById", method = RequestMethod.POST)
    Customer selectCustomerById(@RequestParam String fkCustomerId);

    @RequestMapping(value = "customerAddress/selectAddressById", method = RequestMethod.POST)
    CustomerAddress selectAddressById(@RequestParam String fkAddressId);

    @RequestMapping(value = "customer/updateById", method = RequestMethod.POST)
    void updateById(@RequestBody Customer customer);

    @RequestMapping(value = "customer/loginOrRegisterConsumer", method = RequestMethod.POST)
    void loginOrRegisterConsumer(@RequestBody Customer customer);

    @RequestMapping(value = "customerPoint/selectOne", method = RequestMethod.POST)
    CustomerPoint selectCustomerPointOne(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "customerBalance/selectOne", method = RequestMethod.POST)
    CustomerBalance selectCustomerBalanceOne(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "customer/selectUserByName", method = RequestMethod.POST)
    Customer selectUserByName(@RequestParam String userName);

    /**
     * 促销相关开始
     */
    @RequestMapping(value = "coupon/getCoupons", method = RequestMethod.POST)
    List<Coupon> getCouponList(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "couponDetail/getCouponDetailList", method = RequestMethod.POST)
    List<CouponDetail> getCouponDetailList(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "couponDetail/updateById", method = RequestMethod.POST)
    void updateById(@RequestBody CouponDetail couponDetail);

}
