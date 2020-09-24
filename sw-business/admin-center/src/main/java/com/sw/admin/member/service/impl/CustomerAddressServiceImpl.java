package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.customer.CustomerAddress;
import com.sw.admin.member.mapper.CustomerAddressMapper;
import com.sw.admin.member.service.ICustomerAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员收货地址
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-25 15:58:58
 */
@Slf4j
@Service("customerAddressService")
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress> implements ICustomerAddressService {

}
