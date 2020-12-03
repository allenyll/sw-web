package com.sw.admin.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.entity.order.OrderAftersale;
import com.sw.common.entity.system.User;
import com.sw.common.util.Result;

import java.util.List;

/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
public interface IOrderAftersaleService extends IService<OrderAftersale> {

    /**
     * 查询售后服务单
     * @param queryDto 查询参数
     * @return List
     */
    List<OrderAftersaleDto> getOrderRefundList(OrderQueryDto queryDto);

    /**
     * 提交售后申请单
     * @param orderAftersaleDto 售后实体
     * @return Result
     */
    Result<OrderAftersaleDto> submitOrderAftersale(OrderAftersaleDto orderAftersaleDto);

    /**
     * 查询售后申请单总量
     * @param orderQueryDto 查询条件
     * @return 数量
     */
    int selectCount(OrderQueryDto orderQueryDto);

    /**
     * 分页查询售后申请单
     * @param orderQueryDto 查询条件
     * @return 售后申请单集合
     */
    List<OrderAftersaleDto> getOrderAftersalePage(OrderQueryDto orderQueryDto);

    /**
     * 获取申请单详情
     *
     * @param user 处理人
     * @param id 申请单ID
     * @return 售后申请单集合
     */
    Result<OrderAftersaleDto> getDetail(User user, Long id);

    /**
     * 更新申请单详情
     * @param user 操作人信息
     * @param aftersaleDto 参数
     * @return 返回值
     */
    Result updateAftersaleStatus(User user, OrderAftersaleDto aftersaleDto);

    /**
     * 取消售后申请单
     * @param user 操作人
     * @param orderAftersaleDto 参数
     * @return 操作结果
     */
    Result<OrderAftersaleDto> cancelOrderAftersale(User user, OrderAftersaleDto orderAftersaleDto);

    /**
     * 删除售后申请单
     * @param user 操作人
     * @param orderAftersaleDto 参数
     * @return 操作结果
     */
    Result<OrderAftersaleDto> deleteOrderAftersale(User user, OrderAftersaleDto orderAftersaleDto);

    /**
     * 更新发货信息
     * @param orderAftersaleDto 参数
     * @return 操作结果
     */
    Result<OrderAftersaleDto> saveDeliveryInfo(OrderAftersaleDto orderAftersaleDto);
}
