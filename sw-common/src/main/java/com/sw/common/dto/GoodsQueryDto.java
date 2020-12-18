package com.sw.common.dto;


import lombok.Data;

/**
 * 商品相关查询条件参数DTO
 * @ClassName: com.sw.common.dto.GoodsQueryDto.java
 * @Description:
 * @author: 20012055 yuleilei
 * @date:  2020/12/18 14:18
 * @version V1.0
 */
@Data
public class GoodsQueryDto extends BaseQueryDto{

    /**
     * 排序类型
     */
    private String sort;

    /**
     * 排序方式
     */
    private String order;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 商品分类ID
     */
    private Long categoryId;
}
