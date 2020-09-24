package com.sw.admin.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.GoodsFullReduce;
import com.sw.admin.product.mapper.GoodsFullReduceMapper;
import com.sw.admin.product.service.IGoodsFullReduceService;
import org.springframework.stereotype.Service;

/**
 * 商品满减
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-28 17:24:36
 */
@Service("goodsFullReduceService")
public class GoodsFullReduceServiceImpl extends ServiceImpl<GoodsFullReduceMapper, GoodsFullReduce> implements IGoodsFullReduceService {

}
