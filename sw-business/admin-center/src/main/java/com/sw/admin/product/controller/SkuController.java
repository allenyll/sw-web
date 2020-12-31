package com.sw.admin.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.product.service.IGoodsService;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.product.Goods;
import com.sw.common.entity.product.Sku;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.common.util.StringUtil;
import com.sw.admin.product.service.impl.SkuServiceImpl;
import com.sw.admin.product.service.impl.SpecsServiceImpl;
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

@Api("商品SKU接口")
@Controller
@RequestMapping("sku")
public class SkuController extends BaseController<SkuServiceImpl, Sku> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkuController.class);

    @Autowired
    SkuServiceImpl skuService;

    @Autowired
    SpecsServiceImpl specsService;

    @Autowired
    IGoodsService goodsService;

    @ApiOperation("获取SKU库存列表信息")
    @ResponseBody
    @RequestMapping(value = "/getSkuStockList/{id}", method = RequestMethod.POST)
    private DataResponse getSkuStockList(@PathVariable String id, @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        String keyword = MapUtil.getString(params, "keyword");

        QueryWrapper<Sku> wrapper = new QueryWrapper<>();
        wrapper.eq("GOODS_ID", id);
        wrapper.eq("IS_DELETE", 0);
        if(StringUtil.isNotEmpty(keyword)){
            wrapper.like("SKU_CODE", keyword);
        }

        List<Sku> list = skuService.list(wrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            result = dealSkuSpec(list);
        }

        return DataResponse.success(result);
    }

    @ApiOperation("更新SKU库存列表信息")
    @ResponseBody
    @RequestMapping(value = "/updateSkuStock/{id}", method = RequestMethod.POST)
    private DataResponse updateSkuStock(@PathVariable String id, @RequestBody List<Sku> stockList) {
        Map<String, Object> result = new HashMap<>();
        int stock = 0;
        if(CollectionUtil.isNotEmpty(stockList)) {
            for(Sku sku:stockList){
                stock += sku.getSkuStock();
                skuService.updateById(sku);
            }
        }
        Goods goods = goodsService.getById(id);
        if (goods != null) {
            goods.setStock(stock);
            goodsService.updateById(goods);
        }
        return DataResponse.success(result);
    }

    /**
     * 处理SKU规格
     * @param list
     */
    private Map<String, Object> dealSkuSpec(List<Sku> list) {
        Map<String, Object> result = new HashMap<>();
        List<String> specList = new ArrayList<>();
        List<Map<String, Object>> specValueList = new ArrayList<>();
        for(Sku sku:list){
            Map<String, Object> map;
            map = sku.toMap();
            String specValue = sku.getSpecValue();
            String[] specValues = specValue.split(";");
            if(specValues.length > 1) {
                for(int i=0; i<specValues.length;i++){
                    String[] split = specValues[i].substring(1, specValues[i].length() - 1).split(",");
                    String _id = split[0];
                    Map<String, Object> spec = specsService.getSpecs(_id);
                    if(spec == null && spec.isEmpty()) {
                        return null;
                    }
                    String name = MapUtil.getString(spec, "specName");
                    if(!specList.contains(name)){
                        specList.add(name);
                    }
                    String _val = split[1];
                    map.put("value"+i, _val);
                }
            }
            specValueList.add(map);
        }
        result.put("specList", specList);
        result.put("stockList", specValueList);
        return result;
    }

}
