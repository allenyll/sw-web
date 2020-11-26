package com.sw.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.file.mapper.FileMapper;
import com.sw.admin.member.service.ICustomerService;
import com.sw.admin.order.service.IOrderAftersaleService;
import com.sw.admin.order.service.IOrderDetailService;
import com.sw.admin.order.service.IOrderService;
import com.sw.admin.pay.service.ITransactionRefundService;
import com.sw.admin.pay.service.ITransactionService;
import com.sw.admin.pay.service.IWxRefundService;
import com.sw.admin.product.service.IBrandService;
import com.sw.admin.product.service.IGoodsService;
import com.sw.common.constants.dict.*;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.dto.TransactionRefundDto;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderDetail;
import com.sw.common.entity.pay.Transaction;
import com.sw.common.entity.pay.TransactionRefund;
import com.sw.common.entity.product.Brand;
import com.sw.common.entity.product.Goods;
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
import java.text.SimpleDateFormat;
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

    @Resource
    private OrderAftersaleMapper orderAftersaleMapper;

    @Resource
    private FileMapper fileMapper;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private IWxRefundService wxRefundService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private ITransactionRefundService transactionRefundService;

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

    @Override
    public Result updateAftersaleStatus(User user, OrderAftersaleDto aftersaleDto) {
        Result result = new Result();
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
            } else if(aftersaleDto.getAftersaleStatus().equals(OrderSaleDict.DEAL.getCode())) {
                aftersale.setDealUser(user.getId());
                aftersale.setDealNote(aftersaleDto.getDealNote());
                aftersale.setDealUserName(user.getUserName());
                aftersale.setDealTime(time);
            } else if(aftersaleDto.getAftersaleStatus().equals(OrderSaleDict.COMPLETE.getCode())) {
                aftersale.setDealUser(user.getId());
                aftersale.setDealNote(aftersaleDto.getDealNote());
                aftersale.setDealUserName(user.getUserName());
                aftersale.setDealTime(time);
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
               if (order != null && !OrderStatusDict.CLOSE.getCode().equals(order.getOrderStatus())) {
                   order.setOrderStatus(OrderStatusDict.CLOSE.getCode());
                   order.setNote("该订单下所有商品均已申请售后，关闭该订单");
                   order.setUpdateUser(user.getId());
                   order.setUpdateTime(time);
                   orderService.updateById(order);
                   orderService.dealOperateLog(user.getId(), time, order, "该订单下所有商品均已申请售后，关闭该订单", "close", "");
               }
            }
            orderAftersaleMapper.updateById(aftersale);
        }
        return result;
    }

    @Override
    public List<OrderAftersaleDto> getOrderRefundList(OrderQueryDto queryDto) {
        return orderAftersaleMapper.selectAftersaleList(queryDto);
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
        orderAftersale.setRefundAmount(orderAftersale.getRefundAmount().multiply(new BigDecimal(100)));
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
