package com.sw.admin.pay.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.pay.TransactionRefund;
import com.sw.admin.pay.mapper.TransactionRefundMapper;
import com.sw.admin.pay.service.ITransactionRefundService;

/**
 * 交易退款
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-23 17:32:11
 */
@Service("transactionRefundService")
public class TransactionRefundServiceImpl extends ServiceImpl<TransactionRefundMapper,TransactionRefund> implements ITransactionRefundService{

}
