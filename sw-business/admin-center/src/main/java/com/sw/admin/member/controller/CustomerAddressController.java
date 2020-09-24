package com.sw.admin.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.dict.IsOrNoDict;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.customer.CustomerAddress;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.member.service.impl.CustomerAddressServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:  地址管理接口
 * @Author:       allenyll
 * @Date:         2020/5/26 10:21 下午
 * @Version:      1.0
 */
@Api("地址管理通用接口")
@Slf4j
@RestController
@RequestMapping("/customerAddress")
public class CustomerAddressController extends BaseController<CustomerAddressServiceImpl, CustomerAddress> {

    @ApiOperation("设置地址")
    @ResponseBody
    @RequestMapping(value = "/setAddress", method = RequestMethod.POST)
    public DataResponse setAddress(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params){
        String addOrUpdate = MapUtil.getString(params, "addOrUpdate");
        String isDefault = MapUtil.getString(params, "isDefault");
        if("add".equals(addOrUpdate)){
            CustomerAddress customerAddress = new CustomerAddress();
            customerAddress.setId(SnowflakeIdWorker.generateId());
            setData(customerAddress, params);
            if(IsOrNoDict.YES.getCode().equals(isDefault)){
                setDefault(customerAddress);
            }
            super.add(user, customerAddress);
        }else if("update".equals(addOrUpdate)){
            String id = MapUtil.getString(params, "id");
            QueryWrapper<CustomerAddress> wrapper = new QueryWrapper<>();
            wrapper.eq("PK_ADDRESS_ID", id);
            wrapper.eq("IS_DELETE", 0);
            CustomerAddress customerAddress = service.getOne(wrapper);
            if(customerAddress == null) {
                return  DataResponse.fail("更新失败, 未找到对应地址");
            }
            String _isDefault = customerAddress.getIsDefault();
            if(!_isDefault.equals(isDefault) && IsOrNoDict.YES.getCode().equals(isDefault)){
                setDefault(customerAddress);
            }
            setData(customerAddress, params);
            super.update(user, customerAddress);

        }
        return DataResponse.success();
    }

    private void setDefault(CustomerAddress customerAddress) {
        List<CustomerAddress> list = getList(customerAddress);
        if(CollectionUtil.isNotEmpty(list)){
            for(CustomerAddress _customerAddress:list){
                _customerAddress.setIsDefault(IsOrNoDict.NO.getCode());
                _customerAddress.setIsSelect(IsOrNoDict.NO.getCode());
                service.updateById(_customerAddress);
            }
        }
    }

    public void setData(CustomerAddress customerAddress, Map<String, Object> params){
        customerAddress.setName(MapUtil.getString(params, "name"));
        customerAddress.setPhone(MapUtil.getString(params, "phone"));
        customerAddress.setCustomerId(MapUtil.getLong(params, "customerId"));
        customerAddress.setPostCode(MapUtil.getString(params, "postCode"));
        customerAddress.setStatus(MapUtil.getString(params, "status"));
        customerAddress.setProvince(MapUtil.getString(params, "province"));
        customerAddress.setCity(MapUtil.getString(params, "city"));
        customerAddress.setRegion(MapUtil.getString(params, "region"));
        customerAddress.setDetailAddress(MapUtil.getString(params, "detailAddress"));
        customerAddress.setIsDefault(MapUtil.getString(params, "isDefault"));
        customerAddress.setIsSelect(MapUtil.getString(params, "isSelect"));
    }

    @ApiOperation("获取地址列表")
    @ResponseBody
    @RequestMapping(value = "/getAddressList", method = RequestMethod.POST)
    public DataResponse getAddressList(@RequestBody Map<String, Object> params){
        Long customerId = MapUtil.getLong(params, "customerId");
        if(StringUtil.isEmpty(customerId)){
            return DataResponse.fail("用户不能为空");
        }
        QueryWrapper<CustomerAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("CUSTOMER_ID", customerId);
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("STATUS", StatusDict.START.getCode());
        List<CustomerAddress> list = service.list(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        return DataResponse.success(result);
    }

    @ApiOperation("根据id删除地址")
    @Override
    @ResponseBody
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    public DataResponse delete(@CurrentUser(isFull = true) User user, @PathVariable String id, @RequestBody Map<String, Object> params){

        Long userId = user.getId();
        log.info("userId" + userId);

        CustomerAddress obj = service.getById(id);

        QueryWrapper<CustomerAddress> delWrapper = super.mapToWrapper(params);
        obj.setIsDelete(1);
        obj.setUpdateUser(userId);
        obj.setUpdateTime(DateUtil.getCurrentDateTime());
        boolean flag = service.update(obj, delWrapper);
        if(!flag){
            return DataResponse.fail("删除失败");
        }

        return DataResponse.success();
    }

    @ApiOperation("根据ID更新地址")
    @ResponseBody
    @RequestMapping(value = "/updateAddress/{id}", method = RequestMethod.POST)
    public DataResponse updateAddress(@PathVariable String id){
        if(StringUtil.isEmpty(id)){
            return DataResponse.fail("收货地址不能为空");
        }
        QueryWrapper<CustomerAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("PK_ADDRESS_ID", id);
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("STATUS", StatusDict.START.getCode());
        CustomerAddress customerAddress = service.getOne(wrapper);
        if(customerAddress == null){
            return DataResponse.fail("获取收货地址异常");
        }

        List<CustomerAddress> list = getList(customerAddress);

        if(CollectionUtil.isNotEmpty(list)){
            for(CustomerAddress _customerAddress:list){
                _customerAddress.setIsSelect(IsOrNoDict.NO.getCode());
                service.updateById(_customerAddress);
            }
            customerAddress.setIsSelect(IsOrNoDict.YES.getCode());
            service.updateById(customerAddress);
        }

        return DataResponse.success();
    }

    public List<CustomerAddress> getList(CustomerAddress customerAddress){
        QueryWrapper<CustomerAddress> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("CUSTOMER_ID", customerAddress.getCustomerId());
        entityWrapper.eq("STATUS", StatusDict.START.getCode());
        entityWrapper.eq("IS_DELETE", 0);
        List<CustomerAddress> list = service.list(entityWrapper);
        return list;
    }

    @RequestMapping(value = "/selectAddressById", method = RequestMethod.POST)
    public CustomerAddress selectAddressById(@RequestParam String fkAddressId) {
        return service.getById(fkAddressId);
    }

}
