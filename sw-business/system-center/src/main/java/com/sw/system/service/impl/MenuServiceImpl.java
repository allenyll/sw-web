package com.sw.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.system.Menu;
import com.sw.system.mapper.MenuMapper;
import com.sw.system.service.IMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2020/8/31 3:00 下午
 * @Version:      1.0
 */
@Service("menuService")
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
