package com.sw.common.dto;

import com.sw.common.entity.customer.Customer;
import lombok.Data;

import java.util.List;

/**
 * 客户接口返回值统一
 * @ClassName: com.sw.common.dto.GoodsResult.java
 * @Description:
 * @author: 20012055 yuleilei
 * @date:  2020/12/18 14:28
 * @version V1.0
 */
@Data
public class CustomerResult {

    /**
     * 分页--当前页
     */
    private Integer currentPage;

    /**
     * 分页--总页码
     */
    private Integer totalPage;

    /**
     * 商品集合
     */
    private List<Customer> customerList;

}
