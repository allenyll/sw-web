package com.sw.admin.product.controller;

import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.product.SpecsGroup;
import com.sw.common.entity.system.User;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.admin.product.service.impl.SpecsGroupServiceImpl;
import com.sw.common.util.SnowflakeIdWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("specsGroup")
public class SpecsGroupController extends BaseController<SpecsGroupServiceImpl, SpecsGroup> {

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody SpecsGroup entity) {
        entity.setId(SnowflakeIdWorker.generateId());
        return super.add(user, entity);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<SpecsGroup> list = (List<SpecsGroup>) data.get("list");
        Map<Long, String> map = new HashMap<>();
        List<Map<String, Object>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(SpecsGroup specsGroup:list){
                Map<String, Object> _map = new HashMap<>();
                map.put(specsGroup.getId(), specsGroup.getName());
                _map.put("label", specsGroup.getName());
                _map.put("value", specsGroup.getId());
                newList.add(_map);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

}
