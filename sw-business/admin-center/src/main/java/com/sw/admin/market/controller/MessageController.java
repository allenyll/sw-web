package com.sw.admin.market.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.market.Message;
import com.sw.common.entity.system.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.common.util.SnowflakeIdWorker;
import com.sw.common.util.StringUtil;
import com.sw.admin.market.service.impl.MessageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("消息接口")
@Controller
@RequestMapping("message")
public class MessageController extends BaseController<MessageServiceImpl, Message> {

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Message entity) {
        entity.setId(SnowflakeIdWorker.generateId());
        return super.add(user, entity);
    }

    @ApiOperation("根据类型获取消息")
    @ResponseBody
    @RequestMapping(value = "getMessageListByType", method = RequestMethod.POST)
    public DataResponse getMessageListByType(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();
        String msgType = MapUtil.getString(params, "msgType");
        if (StringUtil.isEmpty(msgType)) {
            msgType = "SW2701";
        }
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("TYPE", msgType);
        wrapper.eq("IS_DELETE", 0);
        List<Message> list = service.list(wrapper);
        result.put("messageList", list);
        return DataResponse.success(result);
    }

    @ApiOperation("根据ID获取消息")
    @ResponseBody
    @RequestMapping(value = "getMessageById", method = RequestMethod.POST)
    public DataResponse getMessageById(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();
        Long id = MapUtil.getLong(params, "id");
        if (StringUtil.isEmpty(id)) {
            return DataResponse.fail("查询通知的ID不能为空");
        }
        Message message = service.getById(id);
        result.put("message", message);
        return DataResponse.success(result);
    }
}
