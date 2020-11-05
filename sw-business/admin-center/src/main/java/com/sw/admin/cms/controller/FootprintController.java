package com.sw.admin.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.product.service.IGoodsService;
import com.sw.client.controller.BaseController;
import com.sw.admin.cms.service.impl.FootprintServiceImpl;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.cms.Footprint;
import com.sw.common.entity.product.Goods;
import com.sw.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品浏览记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-04 09:48:58
 */
@Slf4j
@Api(value = "商品浏览记录", tags = "商品浏览记录")
@RestController
@RequestMapping("/footprint")
public class FootprintController extends BaseController<FootprintServiceImpl,Footprint> {

    @Autowired
    IGoodsService goodsService;

//    @Override
//    @ResponseBody
//    @RequestMapping(value = "page", method = RequestMethod.GET)
//    public DataResponse page(@RequestParam Map<String, Object> params) {
//        DataResponse dataResponse =  super.page(params);
//        Map<String, Object> map = (Map<String, Object>) dataResponse.get("data");
//        List<Footprint> list = (List<Footprint>) map.get("list");
//        if (CollectionUtil.isNotEmpty(list)) {
//            for (Footprint footprint:list) {
//                Long goodsId = footprint.getGoodsId();
//                Goods goods = goodsService.getById(goodsId);
//                if (goods == null) {
//                    continue;
//                }
//                goodsService.setFile(goods);
//                footprint.setGoods(goods);
//                footprint.setGoodsName(goods.getGoodsName());
//                footprint.setGoodsFileUrl(goods.getFileUrl());
//            }
//        }
//        map.put("list", list);
//        dataResponse.put("data", map);
//        return dataResponse;
//    }

    @Override
    @ApiOperation("分页查询浏览")
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();
        int total = service.selectCount(params);
        List<Footprint>  list = service.getFootprintPage(params);
        result.put("total", total);
        result.put("list", list);
        return DataResponse.success(result);
    }

    @ApiOperation("商品浏览记录")
    @ResponseBody
    @RequestMapping(value = "/saveFootprint", method = RequestMethod.POST)
    public Result saveFootprint(@RequestBody Map<String, Object> params){
        String type = MapUtil.getString(params, "type");
        Long goodsId = MapUtil.getLong(params, "goodsId");
        Long customerId = MapUtil.getLong(params, "customerId");
        QueryWrapper<Footprint> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId);
        wrapper.eq("add_user", customerId);
        wrapper.eq("is_delete", 0);
        wrapper.eq("type", type);
        Footprint isFootPrint = service.getOne(wrapper);
        if (isFootPrint != null) {
            isFootPrint.setUpdateUser(customerId);
            isFootPrint.setTimes(isFootPrint.getTimes()+1);
            isFootPrint.setUpdateTime(DateUtil.getCurrentDateTime());
            service.updateById(isFootPrint);
        } else {
            Footprint footprint = new Footprint();
            footprint.setId(SnowflakeIdWorker.generateId());
            footprint.setType(type);
            footprint.setGoodsId(goodsId);
            footprint.setStatus(StatusDict.START.getCode());
            footprint.setTimes(1);
            footprint.setIsDelete(0);
            footprint.setAddUser(customerId);
            footprint.setAddTime(DateUtil.getCurrentDateTime());
            footprint.setUpdateUser(customerId);
            footprint.setUpdateTime(DateUtil.getCurrentDateTime());
            service.save(footprint);
        }
        return new Result();
    }

    @ApiOperation("小程序获取浏览记录")
    @ResponseBody
    @RequestMapping(value = "/getFootprint/{customerId}", method = RequestMethod.POST)
    public Result<List<Footprint>>  getFootprint(@PathVariable Long customerId){
        Result<List<Footprint>>  result = service.getFootprint(customerId);
        return result;
    }

    @ApiOperation("删除商品浏览记录")
    @ResponseBody
    @RequestMapping(value = "/deleteFootprint", method = RequestMethod.POST)
    public Result deleteFootprint(@RequestBody Map<String, Object> params){
        Long goodsId = MapUtil.getLong(params, "goodsId");
        Long customerId = MapUtil.getLong(params, "customerId");
        String type = MapUtil.getString(params, "type");
        QueryWrapper<Footprint> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId);
        wrapper.eq("add_user", customerId);
        wrapper.eq("is_delete", 0);
        wrapper.eq("type", type);
        Footprint isFootPrint = service.getOne(wrapper);
        if (isFootPrint != null) {
            isFootPrint.setUpdateUser(customerId);
            isFootPrint.setIsDelete(1);
            isFootPrint.setUpdateTime(DateUtil.getCurrentDateTime());
            service.updateById(isFootPrint);
        }
        return new Result();
    }

}
