package com.sw.admin.pay.service;

import com.sw.common.dto.TransactionRefundDto;
import com.sw.common.util.Result;

import java.math.BigDecimal;

/**
 * @Description:  小程序退款接口
 * @Author:       allenyll
 * @Date:         2020/11/23 上午11:54
 * @Version:      1.0
 */
public interface IWxRefundService {

    /**
     * 退款
     * @param transactionNo 商户订单号
     * @param amount 订单金额
     * @param refundAmount 退款金额
     * @param aftersaleReason 退款原因
     * @return 结果
     */
    Result<TransactionRefundDto> payRefund(String transactionNo, BigDecimal amount, BigDecimal refundAmount, String aftersaleReason);
}
