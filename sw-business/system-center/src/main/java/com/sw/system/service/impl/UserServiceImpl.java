package com.sw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.constants.dict.UserStatus;
import com.sw.common.entity.system.SysUserRole;
import com.sw.common.entity.system.User;
import com.sw.common.util.JwtUtil;
import com.sw.common.util.Result;
import com.sw.system.mapper.UserMapper;
import com.sw.system.service.IUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:  用户<user>服务实现
 * @Author:       allenyll
 * @Date:         2020/5/4 8:50 下午
 * @Version:      1.0
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    UserMapper userMapper;

    @Autowired
    UserRoleServiceImpl userRoleService;

    @Override
    public List<Map<String, Object>> getUserRoleMenuList(Map<String, Object> params) {
        return userMapper.getUserRoleMenuList(params);
    }

    @Override
    public User selectUserByName(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public Result getUserInfo(String token) {
        Result result = new Result();
        Map<String, Object> resultMap = new HashMap<>(5);
        try {
            System.out.println(token);
            Claims claims = JwtUtil.verifyToken(token);
            String username = (String) claims.get("username");
            String clientId = (String) claims.get("client_id");
            String jti = (String) claims.get("jti");
            // 获取用户
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("ACCOUNT", username);
            wrapper.eq("STATUS", UserStatus.OK.getCode());
            wrapper.eq("IS_DELETE", 0);
            User user = userMapper.selectOne(wrapper);
            if(user != null){
                Long userId = user.getId();
                Map<String, Object> param = new HashMap<>(1);
                param.put("user_id", userId);
                // 根据userId查询用户角色
                List<Map<String, Object>> menuList = userMapper.getUserRoleMenuList(param);
                QueryWrapper<SysUserRole> roleQueryWrapper = new QueryWrapper<>();
                roleQueryWrapper.eq("user_id", userId);
                SysUserRole sysUserRole = userRoleService.getOne(roleQueryWrapper);
                resultMap.put("user", user);
                resultMap.put("name", user.getUserName());
                resultMap.put("menus", menuList);
                resultMap.put("roles", sysUserRole);
                result.setObject(resultMap);
            }else{
                log.info("没有查询到用户!");
                result.fail("查询用户失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.fail("认证用户失败！");
            return result;
        }
        return result;
    }
}
