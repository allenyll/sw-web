package com.sw.admin.member.controller;

import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.customer.CustomerLevel;
import com.sw.admin.member.service.impl.CustomerLevelServiceImpl;
import com.sw.common.entity.system.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.SnowflakeIdWorker;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customerLevel")
public class CustomerLevelController extends BaseController<CustomerLevelServiceImpl, CustomerLevel> {

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody CustomerLevel entity) {
        entity.setId(SnowflakeIdWorker.generateId());
        return super.add(user, entity);
    }

}
