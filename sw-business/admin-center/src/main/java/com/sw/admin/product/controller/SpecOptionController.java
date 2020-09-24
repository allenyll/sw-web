package com.sw.admin.product.controller;

import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.product.SpecOption;
import com.sw.common.entity.system.User;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.admin.product.service.impl.SpecOptionServiceImpl;
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
@RequestMapping("specOption")
public class SpecOptionController extends BaseController<SpecOptionServiceImpl, SpecOption> {

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody SpecOption entity) {
        entity.setId(SnowflakeIdWorker.generateId());
        return super.add(user, entity);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<SpecOption> list = (List<SpecOption>) data.get("list");
        Map<Long, String> map = new HashMap<>();
        List<Map<String, Object>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(SpecOption specOption:list){
                Map<String, Object> _map = new HashMap<>();
                map.put(specOption.getId(), specOption.getName());
                _map.put("label", specOption.getName());
                _map.put("value", specOption.getId());
                newList.add(_map);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

}
