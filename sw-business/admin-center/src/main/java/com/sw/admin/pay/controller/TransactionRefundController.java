package com.sw.admin.pay.controller;

import com.sw.client.controller.BaseController;
import com.sw.admin.pay.service.impl.TransactionRefundServiceImpl;
import com.sw.common.entity.pay.TransactionRefund;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易退款
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-23 17:32:11
 */
@Slf4j
@Api(value = "交易退款", tags = "交易退款")
@RestController
@RequestMapping("/transactionRefund")
public class TransactionRefundController extends BaseController<TransactionRefundServiceImpl,TransactionRefund> {


}
