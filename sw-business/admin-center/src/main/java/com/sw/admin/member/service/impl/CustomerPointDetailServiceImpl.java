package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.customer.CustomerPointDetail;
import com.sw.admin.member.mapper.CustomerPointDetailMapper;
import com.sw.admin.member.service.ICustomerPointDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-09
 */
@Service
public class CustomerPointDetailServiceImpl extends ServiceImpl<CustomerPointDetailMapper, CustomerPointDetail> implements ICustomerPointDetailService {

}
