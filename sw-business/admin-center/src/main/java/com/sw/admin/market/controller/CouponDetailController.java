package com.sw.admin.market.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.market.CouponDetail;
import com.sw.common.util.MapUtil;
import com.sw.admin.market.service.impl.CouponDetailServiceImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("couponDetail")
public class CouponDetailController extends BaseController<CouponDetailServiceImpl, CouponDetail> {

    @RequestMapping(value = "getCouponDetailList", method = RequestMethod.POST)
    public List<CouponDetail> getCouponDetailList(@RequestBody Map<String, Object> param) {
        QueryWrapper<CouponDetail> couponDetailEntityWrapper = new QueryWrapper<>();
        couponDetailEntityWrapper.eq("IS_DELETE", 0);
        couponDetailEntityWrapper.eq("COUPON_ID", MapUtil.getLong(param, "COUPON_ID"));
        return service.list(couponDetailEntityWrapper);
    }

    @RequestMapping(value = "updateById", method = RequestMethod.POST)
    public void updateById(@RequestBody CouponDetail couponDetail) {
        service.updateById(couponDetail);
    }

}
