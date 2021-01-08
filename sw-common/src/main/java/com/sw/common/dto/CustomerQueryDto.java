package com.sw.common.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户查询DTO
 * @ClassName: com.sw.common.dto.CustomerQueryDto.java
 * @Description:
 * @author: 20012055 yuleilei
 * @date:  2021/1/5 11:30
 * @version V1.0
 */
@Data
public class CustomerQueryDto  extends BaseQueryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    private String customerName;
}
