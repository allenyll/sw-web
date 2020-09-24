package com.sw.admin.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.GoodsAppraises;
import com.sw.admin.product.mapper.GoodsAppraisesMapper;
import com.sw.admin.product.service.IGoodsAppraisesService;
import org.springframework.stereotype.Service;

/**
 * 商品评价表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-09 15:16:31
 */
@Service("goodsAppraisesService")
public class GoodsAppraisesServiceImpl extends ServiceImpl<GoodsAppraisesMapper, GoodsAppraises> implements IGoodsAppraisesService {

}
