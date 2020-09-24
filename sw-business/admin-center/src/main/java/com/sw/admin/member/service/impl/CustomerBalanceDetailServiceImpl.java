package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.customer.CustomerBalanceDetail;
import com.sw.admin.member.mapper.CustomerBalanceDetailMapper;
import com.sw.admin.member.service.ICustomerBalanceDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员余额明细表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-04-10 16:24:29
 */
@Slf4j
@Service("customerBalanceDetailService")
public class CustomerBalanceDetailServiceImpl extends ServiceImpl<CustomerBalanceDetailMapper, CustomerBalanceDetail> implements ICustomerBalanceDetailService {

}
