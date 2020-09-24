package com.sw.admin.order.controller;

import com.sw.client.controller.BaseController;
import com.sw.common.entity.order.OrderOperateLog;
import com.sw.admin.order.service.impl.OrderOperateLogServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orderOperateLog")
public class OrderOperateLogController extends BaseController<OrderOperateLogServiceImpl, OrderOperateLog> {


}
