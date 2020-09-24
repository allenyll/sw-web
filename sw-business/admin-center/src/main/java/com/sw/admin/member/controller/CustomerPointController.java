package com.sw.admin.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.entity.customer.CustomerPointDetail;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.common.util.StringUtil;
import com.sw.admin.member.mapper.CustomerPointDetailMapper;
import com.sw.admin.member.service.impl.CustomerPointDetailServiceImpl;
import com.sw.admin.member.service.impl.CustomerPointServiceImpl;
import com.sw.admin.member.service.impl.CustomerServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-09
 */
@Slf4j
@RestController
@RequestMapping("customerPoint")
@Api(value = "用户积分相关接口")
public class CustomerPointController extends BaseController<CustomerPointServiceImpl, CustomerPoint> {

    @Autowired
    CustomerPointServiceImpl customerPointService;

    @Autowired
    CustomerPointDetailServiceImpl customerPointDetailService;

    @Autowired
    CustomerPointDetailMapper customerPointDetailMapper;

    @Autowired
    CustomerServiceImpl customerService;

    @ApiOperation("分页查询积分")
    @Override
    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params) {
        String customerAccount = MapUtil.getMapValue(params, "customerAccount");
        if(StringUtil.isNotEmpty(customerAccount)){
            QueryWrapper<Customer> wrapper = new QueryWrapper<>();
            wrapper.eq("CUSTOMER_ACCOUNT", customerAccount);
            Customer customer = customerService.getOne(wrapper);
            if(customer != null){
                params.put("eq_customer_id", customer.getId());
            }else{
                params.put("eq_customer_id", "undefined");
            }
        }

        DataResponse response = super.page(params);
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        List<CustomerPoint> list = (List<CustomerPoint>) data.get("list");
        if(!CollectionUtils.isEmpty(list)){
            for(CustomerPoint customerPoint:list){
                Customer customer = customerService.getById(customerPoint.getCustomerId());
                if(customer != null){
                    customerPoint.setCustomerName(customer.getCustomerName());
                    customerPoint.setCustomerAccount(customer.getCustomerAccount());
                }
            }
        }

        response.put("list", list);

        return response;
    }

    /**
     * 获取积分
     * @param param
     * @return
     */
    @ApiOperation("获取积分")
    @ResponseBody
    @RequestMapping(value = "/getPoint", method = RequestMethod.POST)
    public DataResponse getPoint(@RequestBody Map<String, Object> param){
        log.info("==============开始调用getPoint================");

        Map<String, Object> result = new HashMap<>();

        String customerId = MapUtil.getMapValue(param, "customerId");

        QueryWrapper<CustomerPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("FK_CUSTOMER_ID", customerId);

        CustomerPoint customerPoint = customerPointService.getOne(wrapper);
        if (customerPoint == null) {
            customerPoint = new CustomerPoint();
            customerPoint.setPoint(0);
        }
        result.put("customerPoint", customerPoint);

        return DataResponse.success(result);
    }

    /**
     * 获取积分详情
     * @param param
     * @return
     */
    @ApiOperation("获取积分详情")
    @ResponseBody
    @RequestMapping(value = "/getPointDetail", method = RequestMethod.POST)
    public DataResponse getPointDetail(@RequestBody Map<String, Object> param){
        log.info("==============开始调用 getPointDetail ================");

        Map<String, Object> result = new HashMap<>();

        Long customerId = MapUtil.getLong(param, "customerId");
        String action = MapUtil.getMapValue(param, "action");
        String pageStr = MapUtil.getMapValue(param, "page");

        Integer page = Integer.parseInt(pageStr);

        QueryWrapper<CustomerPointDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("CUSTOMER_ID", customerId);
        wrapper.eq("TYPE", action);
        wrapper.eq("IS_DELETE", 0);
        Page<CustomerPointDetail> pageList = new Page<>(page, 10);
        Page<CustomerPointDetail> list = customerPointDetailMapper.selectPage(pageList, wrapper);

        long isMore = list.getSize();

        if(isMore < 10){
            result.put("is_more", 0);
        }else {
            result.put("is_more", 1);
        }
        result.put("current_page", page);

        result.put("list", list);

        return DataResponse.success(result);
    }

    @ResponseBody
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public CustomerPoint selectOne(@RequestBody Map<String, Object> map) {
        QueryWrapper<CustomerPoint> customerPointEntityWrapper = new QueryWrapper<>();
        customerPointEntityWrapper.eq("IS_DELETE", 0);
        customerPointEntityWrapper.eq("CUSTOMER_ID", MapUtil.getLong(map, "CUSTOMER_ID"));
        return service.getOne(customerPointEntityWrapper);
    }

}
