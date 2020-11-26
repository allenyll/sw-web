package com.sw.admin.shop.service.impl;

import com.sw.admin.shop.service.IShopAddressService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.shop.ShopAddress;
import com.sw.admin.shop.mapper.ShopAddressMapper;

/**
 * 商家地址
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-20 16:06:05
 */
@Service("shopAddressService")
public class ShopAddressServiceImpl extends ServiceImpl<ShopAddressMapper,ShopAddress> implements IShopAddressService {

}
