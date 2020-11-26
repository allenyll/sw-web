package com.sw.admin.shop.controller;

import com.sw.client.controller.BaseController;
import com.sw.admin.shop.service.impl.ShopAddressServiceImpl;
import com.sw.common.entity.shop.ShopAddress;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家地址
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-20 16:06:05
 */
@Slf4j
@Api(value = "商家地址", tags = "商家地址")
@RestController
@RequestMapping("/shopAddress")
public class ShopAddressController extends BaseController<ShopAddressServiceImpl,ShopAddress> {


}
