package com.sw.admin.shop.service.impl;

import com.sw.admin.shop.service.IShopService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.shop.Shop;
import com.sw.admin.shop.mapper.ShopMapper;

/**
 * 店铺表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-20 16:05:54
 */
@Service("shopService")
public class ShopServiceImpl extends ServiceImpl<ShopMapper,Shop> implements IShopService {

}
