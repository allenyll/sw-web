package com.sw.admin.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.dict.IsOrNoDict;
import com.sw.common.entity.product.SpecOption;
import com.sw.common.entity.product.Specs;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.product.service.impl.SpecOptionServiceImpl;
import com.sw.admin.product.service.impl.SpecsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(value = "规格管理接口", tags = "规格管理接口")
@RequestMapping("specs")
public class SpecsController extends BaseController<SpecsServiceImpl, Specs> {

    @Autowired
    SpecsServiceImpl specsService;

    @Autowired
    SpecOptionServiceImpl specOptionService;

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Specs entity) {
        entity.setId(SnowflakeIdWorker.generateId());
        return super.add(user, entity);
    }

    @ApiOperation("封装前端下拉列表")
    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<Specs> list = (List<Specs>) data.get("list");
        Map<Long, String> map = new HashMap<>();
        List<Map<String, Object>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(Specs specs:list){
                if (IsOrNoDict.YES.getCode().equals(specs.getStatus())) {
                    Map<String, Object> _map = new HashMap<>();
                    map.put(specs.getId(), specs.getSpecsName());
                    _map.put("label", specs.getSpecsName());
                    _map.put("value", specs.getId());
                    newList.add(_map);
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation("根据所属菜单获取下挂的规格和规格属性")
    @ResponseBody
    @RequestMapping(value = "getSpecsListCondition", method = RequestMethod.POST)
    public DataResponse getSpecsListCondition(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();

        QueryWrapper<Specs> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.like("CATEGORY_ID", MapUtil.getLong(params, "categoryId"));
        wrapper.eq("STATUS", IsOrNoDict.YES.getCode());
        wrapper.orderBy(true, true, "SPECS_SEQ");

        List<Specs> specsList = specsService.list(wrapper);File
        if(CollectionUtil.isNotEmpty(specsList)) {
            for(Specs specs:specsList){
                QueryWrapper<SpecOption> entityWrapper = new QueryWrapper<>();
                entityWrapper.eq("IS_DELETE", 0);
                entityWrapper.eq("SPECS_ID", specs.getId());
                List<SpecOption> specOptions = specOptionService.list(entityWrapper);
                if(CollectionUtil.isNotEmpty(specOptions)){
                    Map<String, Object> specOptionMap = new HashMap();
                    specOptionMap.put("specName", specs.getSpecsName());
                    specOptionMap.put("specId", specs.getId());
                    specOptionMap.put("specOptions", specOptions);
                    list.add(specOptionMap);
                }
            }
        }

        result.put("list", list);

        return DataResponse.success(result);
    }

    @ApiOperation("根据ID获取规格")
    @Override
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id){
        Map<String, Object> result = new HashMap<>();

        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Specs obj = (Specs) data.get("obj");
        if(obj != null){
            String categoryId = obj.getCategoryId();
            if (StringUtil.isNotEmpty(categoryId)) {
                // categoryId = categoryId.substring(1, categoryId.length() - 1).replace("\"", "");
                String[] categoryIdArr = categoryId.split(",");
                Long[] categoryIds = (Long[]) ConvertUtils.convert(categoryIdArr,Long.class);
                obj.setCategoryIds(categoryIds);
            }
        }
        result.put("obj", obj);
        return DataResponse.success(result);
    }

}

