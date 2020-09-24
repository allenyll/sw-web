package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.customer.CustomerBalance;
import com.sw.admin.member.mapper.CustomerBalanceMapper;
import com.sw.admin.member.service.ICustomerBalanceService;
import org.springframework.stereotype.Service;

/**
 * 会员余额表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-04-10 16:16:16
 */
@Service("customerBalanceService")
public class CustomerBalanceServiceImpl extends ServiceImpl<CustomerBalanceMapper, CustomerBalance> implements ICustomerBalanceService {

}
