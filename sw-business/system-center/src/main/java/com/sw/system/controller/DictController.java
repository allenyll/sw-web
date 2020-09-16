package com.sw.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.system.Dict;
import com.sw.common.util.DataResponse;
import com.sw.system.service.impl.DictServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表
 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-29
 */
@Api(value = "字典管理相关接口", tags = "菜单管理")
@RestController
@RequestMapping("dict")
public class DictController extends BaseController<DictServiceImpl, Dict> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictController.class);

    @Autowired
    DictServiceImpl dictService;

    @ApiOperation("字典列表")
    @RequestMapping(value = "list/{code}", method = RequestMethod.POST)
    public DataResponse list(@PathVariable String code) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.eq("status", StatusDict.START.getCode());
        wrapper.like("code", code);
        wrapper.ne("parent_id","0");
        List<Dict> list = service.selectList(wrapper);

        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(Dict dict:list){
                Map<String, String> _map = new HashMap<>();
                map.put(dict.getCode(), dict.getName());
                _map.put("label", dict.getName());
                _map.put("value", dict.getCode());
                newList.add(_map);
            }
        }
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation(value = "获取 parentId = 0 的字典集合")
    @GetMapping("/getParent")
    public DataResponse getParent(){
        LOGGER.info("============= {开始调用方法：getParentDict(} =============");
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.orderBy( true, false, "CODE");
        wrapper.eq("parent_id", "0");
        List<Dict> list = service.selectList(wrapper);
        result.put("list", list);
        return DataResponse.success(result);
    }


    @ApiOperation(value = "获取子字典集合")
    @RequestMapping(value = "/getChild/{id}", method = RequestMethod.GET)
    public DataResponse getChild(@PathVariable String id){
        LOGGER.info("============= {开始调用方法：DICT getChild(} =============");
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.orderBy(true, false, "CODE");
        wrapper.eq("parent_id", id);
        List<Dict> list = service.selectList(wrapper);

        result.put("list", list);
        return DataResponse.success(result);
    }

    @ApiOperation("根据字典code获取具体描述")
    @RequestMapping(value = "getDictByCode", method = RequestMethod.POST)
    public Dict getDictByCode(@RequestParam String orderStatus) {
        return service.getDictByCode(orderStatus);
    }

}
