package com.sw.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.system.Depot;
import com.sw.common.entity.system.SysUserRole;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.system.service.impl.DepotServiceImpl;
import com.sw.system.service.impl.UserRoleServiceImpl;
import com.sw.system.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description:  用户管理控制器
 * @Author:       allenyll
 * @Date:         2020/5/10 11:13 下午
 * @Version:      1.0
 */
@Slf4j
@Api(value = "用户管理相关操作", tags = "用户管理")
@RefreshScope
@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserServiceImpl, User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    UserServiceImpl userService;

    @Autowired
    UserRoleServiceImpl userRoleService;

    @Autowired
    DepotServiceImpl depotService;

    @ResponseBody
    @ApiOperation(value = "根据token获取当前用户所属角色拥有的菜单", notes = "根据token获取当前用户所属角色拥有的菜单")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public Result getUserInfo(@RequestParam String token){
        return userService.getUserInfo(token);
    }

    @ApiOperation("测试获取当前用户")
    // @Log(value = "测试获取当前用户")
    @RequestMapping(value = "getCurrentUser", method = RequestMethod.POST)
    public DataResponse getCurrentUser(@CurrentUser(isFull = true) User user) {
        log.info("当前用户：{}", user.getAccount());
        return DataResponse.success();
    }

    /**
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "根据账户获取用户")
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public User selectOne(@RequestBody Map<String, Object> params) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", MapUtil.getString(params, "account"));
        wrapper.eq("status", MapUtil.getString(params, "status"));
        wrapper.eq("is_delete", MapUtil.getString(params, "is_delete"));
        return userService.getOne(wrapper);
    }

    /**
     * 获取用户权限
     * @param params
     * @return
     */
    @ApiOperation(value = "获取用户权限")
    @RequestMapping(value = "/selectOneSysUserRole", method = RequestMethod.POST)
    public SysUserRole selectOneSysUserRole(@RequestBody Map<String, Object> params) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        String userId = MapUtil.getString(params, "user_id");
        wrapper.eq("user_id", userId);
        return userRoleService.getOne(wrapper);
    }

    /**
     * 根据用户信息获取菜单
     * @param params
     * @return
     */
    @ApiOperation(value = "根据用户信息获取菜单")
    @RequestMapping(value = "/getUserRoleMenuList", method = RequestMethod.POST)
    public List<Map<String, Object>> getUserRoleMenuList(@RequestBody Map<String, Object> params) {
        return userService.getUserRoleMenuList(params);
    }

    @Override
    @ApiOperation(value = "根据ID获取用户")
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id){
        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        User user = (User) data.get("obj");
        if(user != null){
            setDepotName(user);
        }
        data.put("obj", user);
        dataResponse.put("data", data);
        return dataResponse;
    }

    private List<User>  buildUserList(List<User> list) {
        if(!CollectionUtils.isEmpty(list)){
            for(User user:list){
                setDepotName(user);
            }
        }
        return list;
    }

    @Override
    @ApiOperation(value = "分页查询用户")
    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){

        DataResponse dataResponse = super.page(params);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<User> list = buildUserList((List<User>) data.get("list"));
        data.put("list", list);
        dataResponse.put("data", data);
        return dataResponse;
    }

    public void setDepotName(User user){
        Long depotId = user.getDepotId();
        QueryWrapper<Depot> depotEntityWrapper = new QueryWrapper<>();
        depotEntityWrapper.eq("is_delete", 0);
        depotEntityWrapper.eq("id", depotId);

        Depot depot = depotService.getOne(depotEntityWrapper);

        if(depot != null){
            user.setDepotName(depot.getDepotName());
        }
    }

    @Override
    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public DataResponse add(@CurrentUser(isFull = true) User user,@RequestBody User sysUser){
        LOGGER.info("==================开始调用 addUser ================");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUser.setLastPasswordResetDate(new Date());
        sysUser.setId(SnowflakeIdWorker.generateId());
        LOGGER.info("==================结束调用 addUser ================");
        return super.add(user, sysUser);
    }


    @ApiOperation(value = "配置权限")
    @RequestMapping(value = "/setRoles",method = RequestMethod.POST)
    @ResponseBody
    public DataResponse setRoles(@RequestBody Map<String, Object> params){

        LOGGER.info("==================开始调用 setRoles ================");
        LOGGER.info("params"+params);
        // 全删全插配置用户角色
        // 1、先删除所有该用户拥有的角色
        Long userId = Long.parseLong(params.get("id").toString());
        QueryWrapper<SysUserRole> userRoleEntityWrapper = new QueryWrapper<>();
        userRoleEntityWrapper.eq("user_id", userId);
        userRoleService.remove(userRoleEntityWrapper);

        // 2、重新插入选择的角色权限
        List<SysUserRole> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.fromObject(params.get("ids"));
        if(jsonArray.size() > 0){
            for(int i=0; i<jsonArray.size(); i++){
                Long roleId = Long.parseLong(jsonArray.getString(i));
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userId);
                sysUserRole.setId(SnowflakeIdWorker.generateId());
                list.add(sysUserRole);
            }
        }
        userRoleService.saveBatch(list);

        LOGGER.info("==================结束调用 setRoles ================");
        return DataResponse.success();
    }

    @ResponseBody
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public DataResponse updateStatus(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params) {
        LOGGER.debug("保存参数：{}", params);
        Map<String, Object> result = new HashMap<>();
        Long id = MapUtil.getLong(params, "id");
        String status = MapUtil.getString(params, "status");

        DataResponse dataResponse = super.get(id);

        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        User sysUser = (User) data.get("obj");

        if(sysUser == null){
            return DataResponse.fail("更新失败, 用户不存在");
        }

        sysUser.setStatus(status);

        Long userId = user.getId();

        sysUser.setUpdateTime(DateUtil.getCurrentDateTime());
        sysUser.setUpdateUser(userId);
        boolean flag = userService.updateById(sysUser);

        if(!flag){
            return DataResponse.fail("更新用户状态失败");
        }

        return DataResponse.success(result);
    }

    @RequestMapping(value = "selectById", method = RequestMethod.POST)
    public User selectById(@RequestParam String userId) {
        return service.getById(userId);
    }

    @RequestMapping(value = "selectUserByName", method = RequestMethod.POST)
    public User selectUserByName(@RequestParam String userName) {
        return userService.selectUserByName(userName);
    }

}
