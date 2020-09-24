package com.sw.admin.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.customer.CustomerBalanceDetail;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.admin.member.mapper.CustomerBalanceDetailMapper;
import com.sw.admin.member.service.impl.CustomerBalanceDetailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:  月明细管理
 * @Author:       allenyll
 * @Date:         2020/5/26 10:32 下午
 * @Version:      1.0
 */
@Slf4j
@Api("余额明细接口")
@RestController
@RequestMapping("customerBalanceDetail")
public class CustomerBalanceDetailController extends BaseController<CustomerBalanceDetailServiceImpl, CustomerBalanceDetail> {


    @Autowired
    CustomerBalanceDetailMapper customerBalanceDetailMapper;

    /**
     * 获取积分详情
     * @param param
     * @return
     */
    @ApiOperation("微信余额详细")
    @ResponseBody
    @RequestMapping(value = "/getBalanceDetail", method = RequestMethod.POST)
    public DataResponse getDetail(@RequestBody Map<String, Object> param){
        log.info("==============开始调用 getBalanceDetail ================");

        Map<String, Object> result = new HashMap<>();

        Long customerId = MapUtil.getLong(param, "customerId");
        String action = MapUtil.getMapValue(param, "action");
        String pageStr = MapUtil.getMapValue(param, "page");

        Integer page = Integer.parseInt(pageStr);

        QueryWrapper<CustomerBalanceDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("CUSTOMER_ID", customerId);
        if(!action.equals("SW0500")){
            wrapper.eq("TYPE", action);
        }
        wrapper.eq("IS_DELETE", 0);
        Page<CustomerBalanceDetail> pageList = new Page<>(page, 10);
        Page<CustomerBalanceDetail> list = customerBalanceDetailMapper.selectPage(pageList, wrapper);

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

}
