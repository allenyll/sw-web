package com.sw.admin.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.pay.Transaction;
import com.sw.admin.pay.mapper.TransactionMapper;
import com.sw.admin.pay.service.ITransactionService;
import org.springframework.stereotype.Service;

/**
 * 交易表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-04-04 16:37:41
 */
@Service("transactionService")
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {

}
