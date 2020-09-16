package com.sw.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.common.entity.system.User;

import java.util.List;
import java.util.Map;

/**
 * @Description:  用户<user>持久层接口
 * @Author:       allenyll
 * @Date:         2020/5/4 8:49 下午
 * @Version:      1.0
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户id获取该用户所在角色下的菜单
     * @param param
     * @return
     */
    List<Map<String, Object>> getUserRoleMenuList(Map<String, Object> param);

}
