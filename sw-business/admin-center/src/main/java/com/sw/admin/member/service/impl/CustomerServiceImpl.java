package com.sw.admin.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.constants.dict.UserStatus;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.entity.customer.CustomerPointDetail;
import com.sw.common.util.DateUtil;
import com.sw.common.util.SnowflakeIdWorker;
import com.sw.common.util.StringUtil;
import com.sw.admin.member.mapper.CustomerMapper;
import com.sw.admin.member.mapper.CustomerPointDetailMapper;
import com.sw.admin.member.mapper.CustomerPointMapper;
import com.sw.admin.member.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author yu.leilei
 * @since 2018-10-22
 */
@Service("customerService")
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CustomerPointMapper customerPointMapper;

    @Autowired
    CustomerPointDetailMapper customerPointDetailMapper;

    @Transactional(rollbackFor=Exception.class)
    public void loginOrRegisterConsumer(Customer customer) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("OPENID", customer.getOpenid());
        wrapper.eq("STATUS", UserStatus.OK.getCode());
        wrapper.eq("IS_DELETE", 0);
        List<Customer> customerList = customerMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(customerList)){
            Long id = SnowflakeIdWorker.generateId();
            String date = DateUtil.getCurrentDateTime();
            customer.setId(id);
            customer.setIsDelete(0);
            customer.setStatus(UserStatus.OK.getCode());
            customer.setAddTime(date);
            customer.setUpdateTime(date);
            String gender = "";
            if("1".equals(customer.getGender())){
                gender = "SW0201";
            } else if("0".equals(customer.getGender())){
                gender = "SW0202";
            }
            customer.setGender(gender);
            customer.setOpenid(customer.getOpenid());
            customer.setEmail(customer.getEmail());
            customer.setPhone(customer.getPhone());
            customer.setCountry(customer.getCountry());
            customer.setProvince(customer.getProvince());
            customer.setCity(customer.getCity());
            customer.setAvatarUrl(customer.getAvatarUrl());
            customer.setAddUser(id);
            customer.setUpdateTime(DateUtil.getCurrentDateTime());
            customer.setCustomerAccount(customer.getNickName());
            customer.setCustomerName(customer.getNickName());

            // 注册送积分
            CustomerPoint customerPoint = new CustomerPoint();
            customerPoint.setCustomerId(id);
            customerPoint.setPoint(5);
            customerPoint.setUsed(0);

            CustomerPointDetail customerPointDetail = new CustomerPointDetail();
            customerPointDetail.setCustomerId(id);
            customerPointDetail.setPoint(5);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 6);
            Date time = calendar.getTime();
            customerPointDetail.setExpiredTime(FORMAT.format(time));
            customerPointDetail.setIsDelete(0);
            customerPointDetail.setType("SW0501");
            customerPointDetail.setGetTime(FORMAT.format(time));
            customerPointDetail.setRemark("用户" + customer.getNickName() + "注册赠送5个积分!");

            try {
                customerMapper.insert(customer);
                customerPointMapper.insert(customerPoint);
                customerPointDetailMapper.insert(customerPointDetail);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

    @Override
    public Customer selectUserByName(String userName) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("CUSTOMER_NAME", userName);
        return customerMapper.selectOne(wrapper);
    }
}
