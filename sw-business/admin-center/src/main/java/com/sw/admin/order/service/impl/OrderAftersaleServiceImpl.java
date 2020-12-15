package com.sw.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.file.mapper.FileMapper;
import com.sw.admin.order.producer.OrderAftersaleProducer;
import com.sw.admin.order.service.IOrderAftersaleService;
import com.sw.admin.order.service.IOrderDetailService;
import com.sw.admin.pay.service.ITransactionRefundService;
import com.sw.admin.pay.service.ITransactionService;
import com.sw.admin.pay.service.IWxRefundService;
import com.sw.common.constants.dict.*;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.dto.TransactionRefundDto;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderDetail;
import com.sw.common.entity.pay.Transaction;
import com.sw.common.entity.pay.TransactionRefund;
import com.sw.common.entity.system.File;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.order.OrderAftersale;
import com.sw.admin.order.mapper.OrderAftersaleMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
@Service("orderAftersaleService")
public class OrderAftersaleServiceImpl extends ServiceImpl<OrderAftersaleMapper,OrderAftersale> implements IOrderAftersaleService {

    /**
     * 最大可退金额=需退款商品原价(30元)-订单中优惠的金额（20元）*（需退款商品原价/订单原价）（30元/100元）= 24元
     */
    /**
     * 未发货自动取消时间
     */
    private static final long DELAY_TIMES = 7 * 24 * 60 * 60 * 1000;

    @Resource
    private OrderAftersaleMapper orderAftersaleMapper;

    @Resource
    private FileMapper fileMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IWxRefundService wxRefundService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private ITransactionRefundService transactionRefundService;

    @Autowired
    private OrderAftersaleProducer orderAftersaleProducer;

    @Override
    public int selectCount(OrderQueryDto orderQueryDto) {
        return orderAftersaleMapper.selectCount(orderQueryDto);
    }

    @Override
    public List<OrderAftersaleDto> getOrderAftersalePage(OrderQueryDto orderQueryDto) {
        int start = (orderQueryDto.getPage() - 1 ) * orderQueryDto.getLimit();
        orderQueryDto.setStart(start);
        return orderAftersaleMapper.getOrderAftersalePage(orderQueryDto);
    }

    @Override
    public Result<OrderAftersaleDto> getDetail(User user, Long id) {
        Result<OrderAftersaleDto> result = new Result<>();
        OrderAftersaleDto orderAftersale = orderAftersaleMapper.getApplyById(id);
        setUnDeliveryTime(orderAftersale);

        Order order = orderService.getById(orderAftersale.getOrderId());
        orderAftersale.setOrder(order);

        OrderDetail orderDetail = orderDetailService.getById(orderAftersale.getOrderDetailId());
        orderAftersale.setOrderDetail(orderDetail);

        orderAftersale.setDealUser(user.getId());
        orderAftersale.setDealUserName(user.getUserName());

        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("FK_ID", orderAftersale.getId());
        fileQueryWrapper.eq("IS_DELETE", 0);
        List<File> files = fileMapper.selectList(fileQueryWrapper);
        orderAftersale.setApplyFiles(files);

        result.setObject(orderAftersale);
        return result;
    }

    private void setUnDeliveryTime(OrderAftersaleDto orderAftersale) {
        if (OrderSaleDict.DEAL.getCode().equals(orderAftersale.getAftersaleStatus()) && StringUtil.isEmpty(orderAftersale.getDeliveryTime())) {
            Date dealDate = DateUtil.stringToDate(orderAftersale.getDealTime(), "yyyy-MM-dd HH:mm:ss");
            Date nowDate = new Date();
            long unDeliveryTime = dealDate.getTime() + DELAY_TIMES - nowDate.getTime() ;
            orderAftersale.setUnDeliveryTime(unDeliveryTime);
        }
    }

    @Override
    public Result updateAftersaleStatus(User user, OrderAftersaleDto aftersaleDto) {
        Result result = new Result();
        OrderAftersaleDto paramDto = new OrderAftersaleDto();
        OrderAftersale aftersale = orderAftersaleMapper.selectById(aftersaleDto.getId());
        String aftersaleType = aftersaleDto.getAftersaleType();
        String time = DateUtil.getCurrentDateTime();
        if (aftersale != null) {
            aftersale.setUpdateTime(DateUtil.getCurrentDateTime());
            aftersale.setUpdateUser(user.getId());
            aftersale.setAftersaleStatus(aftersaleDto.getAftersaleStatus());

            // 不是退款，更新收货人信息
            if (!OrderAftersaleTypeDict.REFUND.getCode().equals(aftersaleType)) {
                // 收货人信息
                aftersale.setReceiverName(aftersaleDto.getReceiverName());
                aftersale.setReceiverPhone(aftersaleDto.getReceiverPhone());
                aftersale.setReceiverPostCode(aftersaleDto.getReceiverPostCode());
                aftersale.setReceiverProvince(aftersaleDto.getReceiverProvince());
                aftersale.setReceiverCity(aftersaleDto.getReceiverCity());
                aftersale.setReceiverRegion(aftersaleDto.getReceiverRegion());
                aftersale.setReceiverDetailAddress(aftersaleDto.getReceiverDetailAddress());
            }

            if (aftersaleDto.getAftersaleStatus().equals(OrderSaleDict.CANCEL.getCode())) {
                aftersale.setAftersaleStatus(aftersaleDto.getAftersaleStatus());
                aftersale.setCancelTime(time);
                orderAftersaleMapper.updateById(aftersale);
                return result;
            } else if(aftersaleDto.getAftersaleStatus().equals(OrderSaleDict.REFUSE.getCode())) {
                aftersale.setRefuseReason(aftersaleDto.getRefuseReason());
                aftersale.setDealUser(user.getId());
                aftersale.setDealNote(aftersaleDto.getRefuseReason());
                aftersale.setDealUserName(user.getUserName());
                aftersale.setDealTime(time);
            } else if(aftersaleDto.getAftersaleStatus().equals(OrderSaleDict.DEAL.getCode())) {
                aftersale.setRefundAmount(aftersaleDto.getRefundAmount().multiply(new BigDecimal(100)));
                aftersale.setDealUser(user.getId());
                aftersale.setDealNote(aftersaleDto.getDealNote());
                aftersale.setDealUserName(user.getUserName());
                aftersale.setDealTime(time);
                // 将消息放入取消队列
                BeanUtils.copyProperties(aftersale, paramDto);
                sendMessage(paramDto);
            } else if(aftersaleDto.getAftersaleStatus().equals(OrderSaleDict.COMPLETE.getCode())) {
                aftersale.setDealUser(user.getId());
                aftersale.setDealNote(aftersaleDto.getDealNote());
                aftersale.setDealUserName(user.getUserName());
                aftersale.setDealTime(time);
                if (OrderAftersaleTypeDict.REFUND_CHANGE.getCode().equals(aftersaleType)) {
                    aftersale.setReceiverNote(aftersaleDto.getReceiverNote());
                    aftersale.setReceiverTime(time);
                }
                if (!OrderAftersaleTypeDict.CHANGE.getCode().equals(aftersaleType)) {
                    // 发起退款
                    aftersale.setRefundAmount(aftersaleDto.getRefundAmount().multiply(new BigDecimal(100)));
                    // 查询商户订单号
                    QueryWrapper<Transaction> transactionQueryWrapper = new QueryWrapper<>();
                    transactionQueryWrapper.eq("ORDER_ID", aftersale.getOrderId());
                    transactionQueryWrapper.eq("STATUS", OrderTradeDict.COMPLETE.getCode());
                    Transaction transaction = transactionService.getOne(transactionQueryWrapper);
                    if (transaction != null) {
                        Result<TransactionRefundDto> refundResult = wxRefundService.payRefund(transaction.getTransactionNo(), transaction.getAmount(), aftersaleDto.getRefundAmount(), aftersaleDto.getAftersaleReason());
                        if (!refundResult.isSuccess()) {
                            result.fail("发起退款失败!");
                            return result;
                        } else {
                            // 记录退款日志
                            TransactionRefundDto refundDto = refundResult.getObject();
                            TransactionRefund transactionRefund = new TransactionRefund();
                            BeanUtils.copyProperties(refundDto, transactionRefund);
                            transactionRefund.setId(SnowflakeIdWorker.generateId());
                            transactionRefund.setAftersaleId(aftersale.getId());
                            transactionRefund.setOrderId(aftersale.getOrderId());
                            transactionRefund.setOrderDetailId(aftersale.getOrderDetailId());
                            transactionRefund.setCustomerId(aftersale.getCustomerId());
                            transactionRefund.setReturnType(aftersale.getRefundType());
                            transactionRefund.setStatus(OrderTradeDict.COMPLETE.getCode());
                            transactionRefund.setRefundTime(time);
                            transactionRefund.setIsDelete(0);
                            transactionRefund.setAddUser(aftersale.getCustomerId());
                            transactionRefund.setAddTime(time);
                            transactionRefund.setUpdateUser(aftersale.getCustomerId());
                            transactionRefund.setUpdateTime(time);
                            transactionRefundService.save(transactionRefund);
                        }
                    }
                }
                if (!OrderAftersaleTypeDict.REFUND.getCode().equals(aftersaleType)) {
                    aftersale.setReceiverNote(aftersaleDto.getReceiverNote());
                    aftersale.setReceiverTime(time);
                }
            }

            // 更新订单明细售后申请状态信息
            OrderDetail orderDetail = orderDetailService.getById(aftersale.getOrderDetailId());
            if (orderDetail != null) {
                orderDetail.setStatus(aftersaleDto.getAftersaleStatus());
                orderDetail.setUpdateUser(user.getId());
                orderDetail.setUpdateTime(time);
                orderDetailService.updateById(orderDetail);
            }
            // 判断订单是否全部已申请售后，更新订单状态为关闭
            QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("STATUS", OrderSaleDict.START.getCode());
            wrapper.eq("ORDER_ID", aftersale.getOrderId());
            wrapper.eq("IS_DELETE", 0);
            List<OrderDetail> orderDetailList = orderDetailService.list(wrapper);
            if (CollectionUtil.isEmpty(orderDetailList)) {
               Order order = orderService.getById(aftersale.getOrderId());
               if (order != null && !OrderStatusDict.COMPLETE.getCode().equals(order.getOrderStatus())) {
                   order.setOrderStatusRecord(order.getOrderStatus());
                   order.setOrderStatus(OrderStatusDict.COMPLETE.getCode());
                   order.setNote("该订单下所有商品均已申请售后，订单自动完成");
                   order.setUpdateUser(user.getId());
                   order.setUpdateTime(time);
                   orderService.updateById(order);
                   orderService.dealOperateLog(user.getId(), time, order, "该订单下所有商品均已申请售后，订单自动完成", "cancel", "");
               }
            }
            orderAftersaleMapper.updateById(aftersale);
        }
        return result;
    }

    /**
     * 发放消息到队列
     * @param aftersaleDto 参数
     * @return 是否发放成功
     */
    public DataResponse sendMessage(OrderAftersaleDto aftersaleDto) {
        //放入消息队列
        try {
            orderAftersaleProducer.sendMessage(aftersaleDto, DELAY_TIMES);
            return DataResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            DataResponse.fail("放置消息队列失败");
        }
        return null;
    }

    @Override
    public Result<OrderAftersaleDto> cancelOrderAftersale(User user, OrderAftersaleDto orderAftersaleDto) {
        Result<OrderAftersaleDto> result = new Result<>();
        String time = DateUtil.getCurrentDateTime();
        OrderAftersale orderAftersale = orderAftersaleMapper.selectById(orderAftersaleDto.getId());
        if (orderAftersale != null) {
            orderAftersale.setAftersaleStatus(OrderSaleDict.CANCEL.getCode());
            orderAftersale.setCancelTime(time);
            orderAftersaleMapper.updateById(orderAftersale);

            // 更新订单明细售后申请状态信息
            OrderDetail orderDetail = orderDetailService.getById(orderAftersaleDto.getOrderDetailId());
            if (orderDetail != null) {
                orderDetail.setStatus(OrderSaleDict.START.getCode());
                orderDetail.setUpdateUser(user.getId());
                orderDetail.setUpdateTime(time);
                orderDetailService.updateById(orderDetail);
            }
            // 如果订单为完成状态，更新订单状态为之前的状态
            Order order = orderService.getById(orderAftersaleDto.getOrderId());
            if (order != null && OrderStatusDict.COMPLETE.getCode().equals(order.getOrderStatus())) {
                order.setOrderStatus(order.getOrderStatusRecord());
                order.setNote("售后申请单取消，重新打开订单，更新为完成之前的状态");
                order.setUpdateUser(user.getId());
                order.setUpdateTime(time);
                orderService.updateById(order);
                orderService.dealOperateLog(user.getId(), time, order, "重新打开订单，更新为完成之前的状态", "cancel", "");
            }
        }
        return result;
    }

    @Override
    public Result<OrderAftersaleDto> deleteOrderAftersale(User user, OrderAftersaleDto orderAftersaleDto) {
        Result<OrderAftersaleDto> result = new Result<>();
        OrderAftersale orderAftersale = orderAftersaleMapper.selectById(orderAftersaleDto.getId());
        if (orderAftersale != null) {
            orderAftersale.setIsDelete(1);
            orderAftersale.setUpdateUser(user.getId());
            orderAftersale.setUpdateTime(DateUtil.getCurrentDateTime());
            orderAftersaleMapper.updateById(orderAftersale);
        }
        return result;
    }

    @Override
    public Result<OrderAftersaleDto> saveDeliveryInfo(OrderAftersaleDto orderAftersaleDto) {
        OrderAftersale orderAftersale = orderAftersaleMapper.selectById(orderAftersaleDto.getId());
        if (orderAftersale != null) {
            orderAftersale.setDeliveryCompany(orderAftersaleDto.getDeliveryCompany());
            orderAftersale.setDeliveryNo(orderAftersaleDto.getDeliveryNo());
            orderAftersale.setDeliveryTime(DateUtil.getCurrentDateTime());
            orderAftersaleMapper.updateById(orderAftersale);
        }
        return new Result<>();
    }

    @Override
    public List<OrderAftersaleDto> getOrderRefundList(OrderQueryDto queryDto) {
        List<OrderAftersaleDto> list = orderAftersaleMapper.selectAftersaleList(queryDto);
        if (CollectionUtil.isNotEmpty(list)) {
            for (OrderAftersaleDto aftersaleDto:list) {
                setUnDeliveryTime(aftersaleDto);
            }
        }
        return list;
    }

    @Override
    public Result<OrderAftersaleDto> submitOrderAftersale(OrderAftersaleDto orderAftersaleDto) {
        Result<OrderAftersaleDto> result = new Result<>();
        OrderAftersale orderAftersale = new OrderAftersale();
        BeanUtils.copyProperties(orderAftersaleDto, orderAftersale);

        orderAftersale.setApplyTime(DateUtil.getCurrentDateTime());
        Long id = SnowflakeIdWorker.generateId();
        orderAftersale.setId(id);
        orderAftersale.setIsDelete(0);
        orderAftersale.setAftersaleNo(StringUtil.getOrderAfterSaleNo());
        if (OrderAftersaleTypeDict.CHANGE.getCode().equals(orderAftersale.getAftersaleType())) {
            orderAftersale.setRefundAmount(new BigDecimal(0));
        } else {
            orderAftersale.setRefundAmount(orderAftersale.getRefundAmount().multiply(new BigDecimal(100)));
        }

        orderAftersaleMapper.insert(orderAftersale);

        // 关联售后图片
        if (StringUtil.isNotEmpty(orderAftersale.getApplyFile())) {
            String[] fileIds = orderAftersale.getApplyFile().split(",");
            for (String fileId : fileIds) {
                File file = fileMapper.selectById(Long.parseLong(fileId));
                if (file != null) {
                    file.setFkId(id);
                    file.setUpdateTime(DateUtil.getCurrentDateTime());
                    fileMapper.updateById(file);
                }
            }
        }

        // 更新订单明细售后申请状态信息
        OrderDetail orderDetail = orderDetailService.getById(orderAftersale.getOrderDetailId());
        orderDetail.setStatus(OrderSaleDict.APPLY.getCode());
        orderDetail.setUpdateTime(DateUtil.getCurrentDateTime());
        orderDetailService.updateById(orderDetail);

        result.setObject(orderAftersaleDto);
        return result;
    }

}
