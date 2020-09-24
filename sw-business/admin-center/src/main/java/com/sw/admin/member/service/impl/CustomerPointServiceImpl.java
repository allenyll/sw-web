package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.admin.member.mapper.CustomerPointMapper;
import com.sw.admin.member.service.ICustomerPointService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-09
 */
@Service("customerPointService")
public class CustomerPointServiceImpl extends ServiceImpl<CustomerPointMapper, CustomerPoint> implements ICustomerPointService {

}
