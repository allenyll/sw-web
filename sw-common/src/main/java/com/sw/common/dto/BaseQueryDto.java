package com.sw.common.dto;

import lombok.Data;

/**
 * @Description:  通用查询条件
 * @Author:       allenyll
 * @Date:         2020/11/11 11:10 上午
 * @Version:      1.0
 */
@Data
public class BaseQueryDto {

    /**
     * 用户ID
     */
    private Long customerId;

    /**
     * 一页显示数量
     */
    private Integer limit;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 开始序号
     */
    private Integer start;
}
