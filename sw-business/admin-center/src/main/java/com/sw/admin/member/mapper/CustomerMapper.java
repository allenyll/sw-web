package com.sw.admin.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.common.entity.customer.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 会员表 Mapper 接口
 * </p>
 *
 * @author yu.leilei
 * @since 2018-10-22
 */
@Repository("customerMapper")
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 统计用户数量
     * @param params
     * @return
     */
    int count(Map<String, Object> params);

    /**
     * 分页查询用户
     * @param params
     * @return
     */
    List<Customer> page(Map<String, Object> params);
}
