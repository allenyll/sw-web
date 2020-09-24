package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.customer.CustomerLevel;
import com.sw.admin.member.mapper.CustomerLevelMapper;
import com.sw.admin.member.service.ICustomerLevelService;
import org.springframework.stereotype.Service;

/**
 * 会员等级表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-18 16:03:02
 */
@Service("customerLevelService")
public class CustomerLevelServiceImpl extends ServiceImpl<CustomerLevelMapper, CustomerLevel> implements ICustomerLevelService {

}
