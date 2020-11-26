package com.sw.admin.shop.controller;

import com.sw.client.controller.BaseController;
import com.sw.admin.shop.service.impl.ShopServiceImpl;
import com.sw.common.entity.shop.Shop;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 店铺表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-20 16:05:54
 */
@Slf4j
@Api(value = "店铺表", tags = "店铺表")
@RestController
@RequestMapping("/shop")
public class ShopController extends BaseController<ShopServiceImpl,Shop> {


}
