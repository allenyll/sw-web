package com.sw.admin.cms.controller;

import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.admin.cms.service.impl.FeedbackServiceImpl;
import com.sw.common.entity.cms.Feedback;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description:  意见反馈
 * @Author:       allenyll
 * @Date:         2020/11/4 9:50 上午
 * @Version:      1.0
 */
@Slf4j
@Api(value = "意见反馈", tags = "意见反馈")
@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController<FeedbackServiceImpl,Feedback> {

    @ApiOperation("保存意见")
    @ResponseBody
    @RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)
    public Result saveFeedback(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params){
        String type = MapUtil.getString(params, "type");
        String content = MapUtil.getString(params, "content");
        String phone = MapUtil.getString(params, "phone");
        Feedback feedback = new Feedback();
        feedback.setId(SnowflakeIdWorker.generateId());
        feedback.setType(type);
        feedback.setContent(content);
        feedback.setPhone(phone);
        feedback.setIsDelete(0);
        feedback.setAddTime(DateUtil.getCurrentDateTime());
        feedback.setUpdateTime(DateUtil.getCurrentDateTime());
        feedback.setAddUser(user.getId());
        feedback.setUpdateUser(user.getId());
        service.save(feedback);
        return new Result();
    }

}
