package com.sw.client.fallback;

import com.sw.client.feign.AdminFeignClient;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerAddress;
import com.sw.common.entity.customer.CustomerBalance;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.market.CouponDetail;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @Description:  sw-uac 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class AdminFallbackFactory implements FallbackFactory<AdminFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminFallbackFactory.class);

    @Override
    public AdminFeignClient create(Throwable throwable) {
        return new AdminFeignClient() {
            @Override
            public Customer selectOne(Map<String, Object> map) {
                LOGGER.error("FEIGN调用：查询用户失败");
                return null;
            }

            @Override
            public Customer selectCustomerById(String fkCustomerId) {
                LOGGER.error("FEIGN调用：根据ID获取用户失败");
                return null;
            }

            @Override
            public CustomerAddress selectAddressById(String fkAddressId) {
                LOGGER.error("FEIGN调用：根据ID获取地址失败");
                return null;
            }

            @Override
            public void updateById(Customer customer) {
                LOGGER.error("FEIGN调用：更新用户失败!");
            }

            @Override
            public void loginOrRegisterConsumer(Customer customer) {
                LOGGER.error("FEIGN调用：注册失败");
            }

            @Override
            public CustomerPoint selectCustomerPointOne(Map<String, Object> map) {
                LOGGER.error("FEIGN调用：获取用户积分失败");
                return null;
            }

            @Override
            public CustomerBalance selectCustomerBalanceOne(Map<String, Object> map) {
                LOGGER.error("FEIGN调用：获取用户余额失败");
                return null;
            }

            @Override
            public Customer selectUserByName(String userName) {
                LOGGER.error("FEIGN调用：根据用户名称获取用户失败");
                return null;
            }

            @Override
            public List<Coupon> getCouponList(Map<String, Object> param) {
                LOGGER.error("FEIGN调用：获取优惠券列表失败");
                return null;
            }

            @Override
            public List<CouponDetail> getCouponDetailList(Map<String, Object> param) {
                LOGGER.error("FEIGN调用：获取优惠券明细失败");
                return null;
            }

            @Override
            public void updateById(CouponDetail couponDetail) {
                LOGGER.error("FEIGN调用：发放优惠券失败");
            }
        };
    }
}
