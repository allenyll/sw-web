package com.sw.admin.pay.controller;

import com.sw.client.controller.BaseController;
import com.sw.common.entity.pay.Transaction;
import com.sw.admin.pay.service.impl.TransactionServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transaction")
public class TransactionController extends BaseController<TransactionServiceImpl, Transaction> {


}
