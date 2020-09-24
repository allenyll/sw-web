package com.sw.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.entity.system.*;
import com.sw.common.util.DataResponse;
import com.sw.common.util.SnowflakeIdWorker;
import com.sw.system.service.impl.RoleMenuServiceImpl;
import com.sw.system.service.impl.RoleServiceImpl;
import com.sw.system.service.impl.UserRoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表
 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-29
 */
@Api(value = "角色管理相关接口", tags = "角色管理")
@Controller
@RequestMapping("role")
public class RoleController  extends BaseController<RoleServiceImpl, Role> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    RoleMenuServiceImpl roleMenuService;

    @Autowired
    UserRoleServiceImpl userRoleService;

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Role role) {
        role.setId(SnowflakeIdWorker.generateId());
        return super.add(user, role);
    }

    @ApiOperation("获取角色列表")
    @ResponseBody
    @RequestMapping(value = "getRoleList/{id}", method = RequestMethod.GET)
    public DataResponse getAllRole(@PathVariable String id){
        LOGGER.info("============= {开始调用方法：getAllRole(} =============");
        Map<String, Object> result = new HashMap<>();

        // 获取用户拥有的角色
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);

        SysUserRole user = userRoleService.getOne(wrapper);
        Role role;
        if(user != null){
            Long roleId = user.getRoleId();
            QueryWrapper<Role> roleEntityWrapper = new QueryWrapper<>();
            roleEntityWrapper.eq("is_delete", 0);
            roleEntityWrapper.eq("id", roleId);

            role = service.getOne(roleEntityWrapper);
            result.put("userRole", role);
        }else{
            result.put("userRole", "");
        }

        QueryWrapper<Role> userWrapper = new QueryWrapper<>();
        userWrapper.eq("is_delete", 0);
        List<Role> roles = service.list(userWrapper);

        result.put("roleList", roles);

        LOGGER.info("============= {结束调用方法：getAllRole(} =============");
        return DataResponse.success(result);
    }

    @ApiOperation("配置角色菜单")
    @RequestMapping(value = "/setMenus",method = RequestMethod.POST)
    @ResponseBody
    public DataResponse setMenus(@RequestBody Map<String, Object> params){

        LOGGER.info("==================开始调用 setMenus ================");
        LOGGER.info("params"+params);
        // 全删全插配置角色菜单
        // 1、先删除所有该角色拥有的菜单权限
        Long roleId = Long.parseLong((String) params.get("id"));
        QueryWrapper<SysRoleMenu> roleMenuEntityWrapper = new QueryWrapper<>();
        roleMenuEntityWrapper.eq("role_id", roleId);
        roleMenuService.remove(roleMenuEntityWrapper);

        // 2、重新插入选择的菜单权限
        List<SysRoleMenu> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.fromObject(params.get("ids"));
        if(jsonArray.size() > 0){
            for(int i=0; i<jsonArray.size(); i++){
                Long menuId = Long.parseLong(jsonArray.getString(i));
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setId(SnowflakeIdWorker.generateId());
                list.add(sysRoleMenu);
            }
        }
        roleMenuService.saveBatch(list);

        LOGGER.info("==================结束调用 setMenus ================");
        return DataResponse.success();
    }

    @ApiOperation("获取角色授权菜单")
    @ResponseBody
    @RequestMapping(value = "getRoleMenus/{id}", method = RequestMethod.GET)
    public DataResponse getRoleMenus(@PathVariable String id){
        LOGGER.info("获取角色已选择菜单 id = " + id);
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);

        List<SysRoleMenu> sysRoleMenus = roleMenuService.list(wrapper);

        List<Long> list = new ArrayList<>();

        if(!CollectionUtils.isEmpty(sysRoleMenus)){
            for(SysRoleMenu roleMenu:sysRoleMenus){
                list.add(roleMenu.getMenuId());
            }
        }

        result.put("list", list);
        return DataResponse.success(result);
    }

}
