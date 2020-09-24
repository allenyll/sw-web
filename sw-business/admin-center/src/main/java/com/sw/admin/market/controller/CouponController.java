package com.sw.admin.market.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.dict.CouponDict;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.system.User;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.common.util.SnowflakeIdWorker;
import com.sw.common.util.StringUtil;
import com.sw.admin.market.service.impl.CouponServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("优惠券接口")
@RestController
@RequestMapping("coupon")
public class CouponController extends BaseController<CouponServiceImpl, Coupon> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    CouponServiceImpl couponService;

    @ApiOperation("添加优惠券")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Coupon coupon){
        LOGGER.info("coupon: " + coupon);
        Long id = SnowflakeIdWorker.generateId();
        coupon.setId(id);
        coupon.setReceiveCount(0);
        coupon.setUseCount(0);
        super.add(user, coupon);
        List<Map<String, Object>> list = coupon.getCouponGoodsList();
        if(CollectionUtil.isNotEmpty(list)){
            for(Map<String, Object> map:list){
                map.put("id", StringUtil.getUUID32());
                map.put("couponId", id);
                couponService.addCouponGoods(map);
            }
        }
        return DataResponse.success();
    }

    @ApiOperation("根据ID获取优惠券")
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id){
        Map<String, Object> result = new HashMap<>();

        Coupon obj =  couponService.getById(id);

        if(CouponDict.GOODS.getCode().equals(obj.getUseType())){
            List<Map<String, Object>> list = couponService.selectCouponGoods(obj.getId());
            obj.setCouponGoodsList(list);
        }
        result.put("obj", obj);
        return DataResponse.success(result);
    }

    @ApiOperation("发放优惠券")
    @RequestMapping(value = "publishCoupon", method = RequestMethod.POST)
    public DataResponse publishCoupon(@RequestBody Map<String, Object> params){
        DataResponse dataResponse = couponService.publishCoupon(params);
        return dataResponse;
    }

    @ApiOperation("获取优惠券列表")
    @RequestMapping(value = "getCouponList", method = RequestMethod.POST)
    public DataResponse getCouponList(@RequestBody Map<String, Object> params){
        DataResponse dataResponse = couponService.getCouponList(params);
        return dataResponse;
    }

    @ApiOperation("调度任务获取所有未被删除的优惠券")
    @RequestMapping(value = "getCoupons", method = RequestMethod.POST)
    public List<Coupon> getCoupons(@RequestBody Map<String, Object> param) {
        QueryWrapper<Coupon> couponEntityWrapper = new QueryWrapper<>();
        couponEntityWrapper.eq("is_delete", 0);
        return service.list(couponEntityWrapper);
    }

}
