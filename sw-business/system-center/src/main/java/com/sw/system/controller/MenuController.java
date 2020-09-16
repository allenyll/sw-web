package com.sw.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.BaseConstants;
import com.sw.common.entity.system.Menu;
import com.sw.common.entity.system.MenuTree;
import com.sw.common.entity.system.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.SnowflakeIdWorker;
import com.sw.common.util.TreeUtil;
import com.sw.system.service.impl.MenuServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2018-06-12
 */
@Api(value = "菜单相关接口", tags = "菜单管理")
@Controller
@RequestMapping("menu")
public class MenuController extends BaseController<MenuServiceImpl, Menu> {

    private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Menu menu) {
        menu.setId(SnowflakeIdWorker.generateId());
        return super.add(user, menu);
    }

    /**
     * 获取菜单
     * @param params
     * @return
     */
    @ApiOperation(value = "获取全部的菜单信息")
    @ResponseBody
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET)
    public DataResponse getAllMenu(@RequestParam Map<String, Object> params){
        LOG.info("==============开始调用getAllMenu================");

        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Menu> wrapper = super.mapToWrapper(params);
        List<Menu> menuList = service.list(wrapper);
        List<MenuTree> list = getMenuTree(menuList, BaseConstants.MENU_ROOT);
        result.put("menus", list);

        return DataResponse.success(result);
    }

    @ApiOperation(value = "根据菜单ID获取菜单")
    @Override
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id){
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.eq("id", id);

        Menu sysMenu = service.getOne(wrapper);

        if(id.equals(BaseConstants.MENU_ROOT)){
            sysMenu = new Menu();
            sysMenu.setId(id);
            sysMenu.setMenuName("顶级节点");
        }else{
            setParentMenu(sysMenu);
        }
        result.put("obj", sysMenu);

        return DataResponse.success(result);
    }

    @ApiOperation(value = "组装菜单树")
    @ResponseBody
    @RequestMapping(value = "getMenuTree", method = RequestMethod.GET)
    public DataResponse getMenuTree(String type){
        LOG.info("==================开始调用getMenuTree================");
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        if ("menu".equals(type)) {
            wrapper.eq("menu_type", "SW0101");
        }

        List<Menu> menuList = service.list(wrapper);
        if(!CollectionUtils.isEmpty(menuList)){
            for(Menu menu:menuList){
                setParentMenu(menu);
            }
        }
        Menu topMenu = new Menu();
        topMenu.setId(0L);
        topMenu.setIsDelete(0);
        topMenu.setMenuName("顶级节点");
        topMenu.setMenuCode("top");
        topMenu.setPid(1000000L);
        topMenu.setMenuIcon("sw-top");
        menuList.add(topMenu);
        List<MenuTree> menuTrees = getMenuTree(menuList, 1000000L);
        result.put("menuTree", menuTrees);
        LOG.info("==================结束调用getMenuTree================");
        return DataResponse.success(result);
    }

    private List<MenuTree> getMenuTree(List<Menu> menuList, Long menuRootId) {
        List<MenuTree> menuTrees = new ArrayList<>();
        MenuTree menuTree;
        if(!CollectionUtils.isEmpty(menuList)){
            for(Menu menu:menuList){
                menuTree = new MenuTree();
                menuTree.setId(menu.getId());
                menuTree.setParentId(menu.getPid());
                menuTree.setCode(menu.getMenuCode());
                menuTree.setName(menu.getMenuName());
                menuTree.setHref(menu.getMenuUrl());
                menuTree.setTitle(menu.getMenuName());
                menuTree.setLabel(menu.getMenuName());
                menuTree.setIcon(menu.getMenuIcon());
                menuTrees.add(menuTree);
            }
        }
        return TreeUtil.build(menuTrees, menuRootId);
    }

    private void setParentMenu(Menu sysMenu) {
        Long parentId = sysMenu.getPid();
        if(parentId.equals(BaseConstants.MENU_ROOT)){
            sysMenu.setParentMenuName("顶级节点");
        }else{
            QueryWrapper<Menu> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("is_delete", 0);
            entityWrapper.eq("id", parentId);
            Menu menu = service.getOne(entityWrapper);
            if(menu != null){
                sysMenu.setParentMenuName(menu.getMenuName());
            }
        }
    }

}
