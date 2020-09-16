package com.sw.client.feign;

import com.sw.client.fallback.SystemFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.system.Dict;
import com.sw.common.entity.system.Log;
import com.sw.common.entity.system.SysUserRole;
import com.sw.common.entity.system.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:  feign 用户服务接口
 * @Author:       allenyll
 * @Date:         2020/5/4 5:54 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.SYSTEM_SERVICE, fallbackFactory = SystemFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface SystemFeignClient {

    /**
     * 保存用户
     * @param userToAdd
     */
    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    void save(@RequestBody User userToAdd);

    /**
     * 查询用户
     * @param params
     * @return
     */
    @RequestMapping(value = "user/selectOne", method = RequestMethod.POST)
    User selectOne(@RequestBody Map<String, Object> params);

    /**
     * 根据角色查询用户
     * @param params
     * @return
     */
    @RequestMapping(value = "user/selectOneSysUserRole", method = RequestMethod.POST)
    SysUserRole selectOneSysUserRole(@RequestBody Map<String, String> params);

    /**
     * 查询角色拥有权限
     * @param param
     * @return
     */
    @RequestMapping(value = "user/getUserRoleMenuList", method = RequestMethod.POST)
    List<Map<String, Object>> getUserRoleMenuList(@RequestBody Map<String, String> param);

    /**
     * 保存日志
     * @param sysLog
     */
    @RequestMapping(value = "log/saveLog", method = RequestMethod.POST)
    void saveLog(@RequestBody Log sysLog);

    /**
     * 根据ID查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "user/selectById", method = RequestMethod.POST)
    User selectById(@RequestParam String userId);

    /**
     * 根据字典码查询字典项
     * @param orderStatus
     * @return
     */
    @RequestMapping(value = "dict/getDictByCode", method = RequestMethod.POST)
    Dict getDictByCode(@RequestParam String orderStatus);

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    @RequestMapping(value = "user/selectUserByName", method = RequestMethod.POST)
    User selectUserByName(@RequestParam String userName);
}
