package com.sw.admin.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.market.CouponDetail;
import com.sw.admin.market.mapper.CouponDetailMapper;
import com.sw.admin.market.service.ICouponDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 优惠券领取详情表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-29 22:31:34
 */
@Service("couponDetailService")
public class CouponDetailServiceImpl extends ServiceImpl<CouponDetailMapper, CouponDetail> implements ICouponDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponDetailServiceImpl.class);
}
