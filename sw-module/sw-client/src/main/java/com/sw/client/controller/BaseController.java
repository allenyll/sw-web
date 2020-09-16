package com.sw.client.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.client.annotion.CurrentUser;
import com.sw.common.constants.WrapperConstants;
import com.sw.common.entity.BaseEntity;
import com.sw.common.entity.system.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.DateUtil;
import com.sw.common.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description:  基础控制器
 * @Author:       allenyll
 * @Date:         2020/8/31 5:18 下午
 * @Version:      1.0
 */
public class BaseController<Service extends ServiceImpl, T extends BaseEntity> {

    @Autowired
    protected Service service;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected static final String DEFAULT_URL = "https://system-web-1257935390.cos.ap-chengdu.myqcloud.com/images/no.jpeg";

    /**
     * 当前页数
     */
    protected int page = 1;

    /**
     * 每页数量
     */
    protected int limit = 10;

    /**
     * 总页数
     */
    protected int totalPage = 0;

    @ApiOperation("公用获取数据列表")
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list(){
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);

        List<T> list = service.list(wrapper);

        result.put("list", list);

        return DataResponse.success(result);
    }

    @ApiOperation("公用分页查询数据")
    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        LOGGER.info("传入参数=============" + params);

        page = Integer.parseInt(params.get("page").toString());
        limit = Integer.parseInt(params.get("limit").toString());

        QueryWrapper<T> wrapper = mapToWrapper(params);

        int total = service.count(wrapper);
        IPage<T> list = service.page(new Page<>(page, limit), wrapper);

        result.put("total", total);
        result.put("list", list.getRecords());

        return DataResponse.success(result);
    }

    @ApiOperation("公用根据ID获取数据")
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id){
        Map<String, Object> result = new HashMap<>();

        T obj = (T) service.getById(id);

        result.put("obj", obj);

        return DataResponse.success(result);
    }

    @ApiOperation("公用添加数据")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody T t){
        LOGGER.info("开始添加 "+t.getClass());
        Long userId = user.getId();
        t.setIsDelete(0);
        t.setAddTime(DateUtil.getCurrentDateTime());
        t.setAddUser(userId);
        t.setUpdateTime(DateUtil.getCurrentDateTime());
        t.setUpdateUser(userId);
        try {
                service.save(t);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(t.getClass() + " 添加失败");
            return DataResponse.fail(t.getClass()+" 添加失败");
        }
        LOGGER.info("结束添加 "+ t.getClass());
        return DataResponse.success();
    }

    @ApiOperation("公用更新数据")
    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    @ResponseBody
    public DataResponse update(@CurrentUser(isFull = true) User user, @RequestBody T t){
        Long userId = user.getId();

        t.setUpdateTime(DateUtil.getCurrentDateTime());
        t.setUpdateUser(userId);
        service.updateById(t);

        return DataResponse.success();
    }

    @ApiOperation("公用删除数据")
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public DataResponse delete(@CurrentUser(isFull = true) User user, @PathVariable String id, @RequestParam Map<String, Object> params){

        Long userId = user.getId();
        LOGGER.info("userId" + userId);

        T obj = (T) service.getById(id);

        QueryWrapper<T> delWrapper = mapToWrapper(params);
        obj.setIsDelete(1);
        obj.setUpdateTime(DateUtil.getCurrentDateTime());
        obj.setUpdateUser(userId);
        boolean flag = service.update(obj, delWrapper);
        if(!flag){
            return DataResponse.fail("删除失败");
        }

        return DataResponse.success();
    }

    /**
     * 将参数封装成wrapper查询条件
     * @param params
     * @return
     */
    protected QueryWrapper<T> mapToWrapper(Map<String, Object> params) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        Set<Map.Entry<String, Object>> set = params.entrySet();
        if(!CollectionUtils.isEmpty(set)){
            for(Map.Entry<String, Object> entry : set){
                String key = entry.getKey();
                if(StringUtil.isNotEmpty(key)){
                    String condition = key.split("_")[0];
                    String val = entry.getValue().toString();
                    if(StringUtil.isNotEmpty(val)){
                        if(WrapperConstants.LIKE.equals(condition)){
                            wrapper.like(key.substring(key.indexOf("_")+1), entry.getValue().toString());
                        }else if(WrapperConstants.EQ.equals(condition)){
                            wrapper.eq(key.substring(key.indexOf("_")+1), entry.getValue().toString());
                        }else if(WrapperConstants.GT.equals(condition)){
                            wrapper.gt(key.substring(key.indexOf("_")+1), entry.getValue().toString());
                        }else if(WrapperConstants.GE.equals(condition)){
                            wrapper.ge(key.substring(key.indexOf("_")+1), entry.getValue().toString());
                        }else if(WrapperConstants.LT.equals(condition)){
                            wrapper.lt(key.substring(key.indexOf("_")+1), entry.getValue().toString());
                        }else if(WrapperConstants.LE.equals(condition)){
                            wrapper.le(key.substring(key.indexOf("_")+1), entry.getValue().toString());
                        }
                    }
                    if(WrapperConstants.ORDER.equals(condition)){
                        wrapper.orderBy(true, Boolean.parseBoolean(entry.getValue().toString()), new String[]{key.substring(key.indexOf("_")+1)});
                    }
                }
            }
        }
        return wrapper;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
