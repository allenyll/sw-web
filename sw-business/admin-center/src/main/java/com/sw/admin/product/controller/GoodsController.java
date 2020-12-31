package com.sw.admin.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.admin.cms.service.impl.SearchHistoryServiceImpl;
import com.sw.admin.file.service.impl.FileServiceImpl;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.dict.FileDict;
import com.sw.common.constants.dict.IsOrNoDict;
import com.sw.common.dto.GoodsQueryDto;
import com.sw.common.dto.GoodsResult;
import com.sw.common.entity.cms.SearchHistory;
import com.sw.common.entity.product.Goods;
import com.sw.common.entity.product.GoodsParam;
import com.sw.common.entity.system.File;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.product.service.impl.GoodsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("商品管理相关接口")
@Controller
@RequestMapping("goods")
public class GoodsController extends BaseController<GoodsServiceImpl, Goods> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    FileServiceImpl fileService;

    @Autowired
    GoodsServiceImpl goodsService;

    @Autowired
    SearchHistoryServiceImpl searchHistoryService;

    @ApiOperation("获取ID")
    @ResponseBody
    @RequestMapping(value = "getSnowFlakeId", method = RequestMethod.POST)
    public DataResponse getSnowFlakeId() {
        Long id = SnowflakeIdWorker.generateId();
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        return DataResponse.success(result);
    }

    @ApiOperation("获取商品列表（前端展示使用）")
    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<Goods> list = (List<Goods>) data.get("list");
        Map<Long, String> map = new HashMap<>();
        List<Map<String, Object>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(Goods goods:list){
                Map<String, Object> _map = new HashMap<>();
                map.put(goods.getId(), goods.getGoodsName());
                _map.put("label", goods.getGoodsName());
                _map.put("value", goods.getId());
                newList.add(_map);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation("获取商品列表")
    @ResponseBody
    @RequestMapping(value = "getGoodsList", method = RequestMethod.POST)
    public DataResponse getGoodsList(@RequestBody Map<String, Object> params) {
        String keyword = MapUtil.getString(params, "keyword");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        if(StringUtil.isNotEmpty(keyword)){
            wrapper.and(_wrapper -> wrapper.like("GOODS_NAME", keyword).or().like("GOODS_CODE", keyword));
        }
        List<Goods> list = goodsService.list(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return DataResponse.success(result);
    }

    @ApiOperation("根据商品类型获取商品列表")
    @ResponseBody
    @RequestMapping(value = "getGoodsListByType", method = RequestMethod.POST)
    public DataResponse getGoodsListByType(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String goodsType = MapUtil.getString(params, "goodsType");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("IS_USED", "SW1302");
        if (StringUtil.isEmpty(goodsType)) {
        result.put("goodsList", new ArrayList<>());
         return DataResponse.success(result);
        }
        if ("new".equals(goodsType)) {
            wrapper.eq("IS_NEW", IsOrNoDict.YES.getCode());
        } else if ("hot".equals(goodsType)) {
            wrapper.eq("IS_HOT", IsOrNoDict.YES.getCode());
        } else if ("recommend".equals(goodsType)) {
            wrapper.eq("IS_RECOM", IsOrNoDict.YES.getCode());
        } else if ("best".equals(goodsType)) {
            wrapper.eq("IS_BEST", IsOrNoDict.YES.getCode());
        }
        List<Goods> list = goodsService.list(wrapper);
        if(CollectionUtil.isNotEmpty(list)){
            for(Goods goods:list){
                goodsService.setFile(goods);
            }
        }
        result.put("goodsList", list);
        return DataResponse.success(result);
    }

    @ApiOperation("分页查询商品")
    @Override
    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        LOGGER.info("传入参数=============" + params);
        DataResponse dataResponse = super.page(params);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<Goods> goodsList = (List<Goods>) data.get("list");
        if(CollectionUtil.isNotEmpty(goodsList)){
            for(Goods goods:goodsList){
                goodsService.setFile(goods);
            }
        }
        result.put("total", dataResponse.get("total"));
        result.put("list", goodsList);
        return DataResponse.success(result);
    }

    @ApiOperation("创建商品")
    @ResponseBody
    @RequestMapping(value = "/createGoods", method = RequestMethod.POST)
    public DataResponse createGoods(@CurrentUser(isFull = true) User user, @RequestBody GoodsParam goodsParam) {
        LOGGER.debug("保存参数：{}", goodsParam);
        Map<String, Object> result = new HashMap<>();
        try {
            int count = goodsService.createGoods(goodsParam, user);
            result.put("count", count);
        } catch (Exception e) {
            LOGGER.error("创建商品失败");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("更新商品")
    @ResponseBody
    @RequestMapping(value = "/updateGoods/{id}", method = RequestMethod.POST)
    public DataResponse updateGoods(@CurrentUser(isFull = true) User user, @PathVariable String id, @RequestBody GoodsParam goodsParam) {
        LOGGER.debug("更新参数：{}", goodsParam);
        Map<String, Object> result = new HashMap<>();

        try {
            int count = goodsService.updateGoods(goodsParam, user);
            result.put("count", count);
        } catch (Exception e) {
            LOGGER.error("创建商品失败");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("删除商品")
    @ResponseBody
    @RequestMapping(value = "/deleteGoods/{id}", method = RequestMethod.POST)
    public DataResponse deleteGoods(@CurrentUser(isFull = true) User user, @PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        try {
            int count = goodsService.deleteGoods(user, id);
            result.put("count", count);
        } catch (Exception e) {
            LOGGER.error("创建商品失败");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    /**
     * 更新商品状态
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateLabel", method = RequestMethod.POST)
    public DataResponse updateLabel(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params) {
        LOGGER.debug("保存参数：{}", params);
        Map<String, Object> result = new HashMap<>();
        Long goodsId = MapUtil.getLong(params, "id");
        String label = MapUtil.getString(params, "label");
        String status = MapUtil.getString(params, "status");

        DataResponse dataResponse = super.get(goodsId);

        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Goods goods = (Goods) data.get("obj");

        if(goods == null){
            return DataResponse.fail("更新失败, 商品不存在");
        }
        if("isUsed".equals(label)){
            goods.setIsUsed(status);
        }else if("isRecom".equals(label)){
            goods.setIsRecom(status);
        }else if("isSpec".equals(label)){
            goods.setIsSpec(status);
        }else if("isBest".equals(label)){
            goods.setIsBest(status);
        }else if("isHot".equals(label)){
            goods.setIsHot(status);
        }else if("isNew".equals(label)){
            goods.setIsNew(status);
        }

        Long userId = user.getId();

        goods.setUpdateTime(DateUtil.getCurrentDateTime());
        goods.setUpdateUser(userId);
        boolean flag = goodsService.updateById(goods);

        if(!flag){
            return DataResponse.fail("更新商品状态失败");
        }

        return DataResponse.success(result);
    }

    @Override
    @ApiOperation("根据ID获取商品")
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id) {

        Map<String, Object> result = null;

        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Goods goods = (Goods) data.get("obj");

        if(goods == null) {
            return  DataResponse.fail("获取商品失败");
        }
        goodsService.setFile(goods);
        try {
            result = goodsService.getGoodsInfo(goods);
        } catch (Exception e) {
            LOGGER.error("赋值异常");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("根据分类获取商品")
    @ResponseBody
    @RequestMapping(value = "/getGoods", method = RequestMethod.POST)
    public DataResponse getGoods(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        page = MapUtil.getIntValue(params, "page");
        limit = MapUtil.getIntValue(params, "limit");
        String id = MapUtil.getMapValue(params, "categoryId");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("CATEGORY_ID", id);

        int total = goodsService.count(wrapper);
        Page<Goods> pages = service.page(new Page<>(page, limit), wrapper);
        List<Goods> list = pages.getRecords();
        if(CollectionUtil.isNotEmpty(list)){
            for (Goods goods: list){
                goodsService.setFile(goods);
            }
        }

        if(total%limit == 0){
            totalPage = total/limit;
        }else{
            totalPage = total/limit + 1;
        }

        result.put("currentPage", page);
        result.put("totalPage", totalPage);
        result.put("goods", list);

        return DataResponse.success(result);
    }

    @ApiOperation("小程序获取商品详情")
    @ResponseBody
    @RequestMapping(value = "/getGoodsInfo/{id}", method = RequestMethod.POST)
    public DataResponse getGoodsInfo(@PathVariable Long id){
        Map<String, Object> result = new HashMap<>();
        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Goods goods = (Goods) data.get("obj");
        if(goods == null){
            return DataResponse.fail("商品不存在");
        }

        goodsService.setFile(goods);

        try {
            result = goodsService.getGoodsInfo(goods);
        } catch (Exception e) {
            LOGGER.error("赋值异常");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("查询商品")
    @ResponseBody
    @RequestMapping(value = "/searchGoods", method = RequestMethod.POST)
    public DataResponse searchGoods(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        page = MapUtil.getIntValue(params, "page");
        limit = MapUtil.getIntValue(params, "limit");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        String sort = MapUtil.getString(params, "sort");
        if ("default".equals(sort)) {
            // 综合排序处理
            sort = "goods_seq";
        }
        String order = MapUtil.getString(params, "order");
        boolean isAsc = true;
        if ("asc".endsWith(order)) {
            isAsc = true;
        } else {
            isAsc = false;
        }
        wrapper.orderBy(true, isAsc, sort);
        String keyword = MapUtil.getString(params, "keyword");
        if (StringUtil.isNotEmpty(keyword)) {
            String time = DateUtil.getCurrentDateTime();
            // 新增搜索记录
            Long customerId = MapUtil.getLong(params, "userId");
            if (StringUtil.isEmpty(customerId)) {
                return DataResponse.fail("关联用户为空，无法查询");
            }
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setId(SnowflakeIdWorker.generateId());
            searchHistory.setDataSource("小程序");
            searchHistory.setKeyword(keyword);
            searchHistory.setUserId(customerId);
            searchHistory.setIsDelete(0);
            searchHistory.setAddTime(time);
            searchHistory.setAddUser(customerId);
            searchHistory.setUpdateUser(customerId);
            searchHistory.setUpdateTime(time);
            searchHistoryService.save(searchHistory);
        }
        wrapper.like("KEYWORDS", keyword);
        String categoryId = MapUtil.getMapValue(params, "categoryId");
        if (StringUtil.isNotEmpty(categoryId)) {
            wrapper.eq("CATEGORY_ID", categoryId);
        }

        int total = goodsService.count(wrapper);
        Page<Goods> pages = service.page(new Page<>(page, limit), wrapper);
        List<Goods> list = pages.getRecords();
        if(CollectionUtil.isNotEmpty(list)){
            for (Goods goods: list){
                goodsService.setFile(goods);
            }
        }

        int num = total%limit;
        if(num == 0){
            totalPage = total/limit;
        }else{
            totalPage = total/limit + 1;
        }

        result.put("currentPage", page);
        result.put("totalPage", totalPage);
        result.put("goods", list);

        return DataResponse.success(result);
    }

    @ApiOperation("获取商品列表")
    @ResponseBody
    @RequestMapping(value = "/getGoodsListByCondition", method = RequestMethod.POST)
    public Result<GoodsResult> getGoodsListByCondition(@RequestBody GoodsQueryDto goodsQueryDto){
        return service.getGoodsListByCondition(goodsQueryDto);
    }


    @ApiOperation("获取商品库存信息")
    @ResponseBody
    @RequestMapping(value = "/getStock", method = RequestMethod.POST)
    public Result<GoodsResult> getStock(@RequestBody GoodsQueryDto goodsQueryDto){
        return service.getStock(goodsQueryDto);
    }



    public static void main(String[] args) {
        int count = 0;
        int i = count%10;
        System.out.println(i);
    }

}
