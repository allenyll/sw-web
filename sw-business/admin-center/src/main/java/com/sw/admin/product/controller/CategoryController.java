package com.sw.admin.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.file.service.impl.FileServiceImpl;
import com.sw.cache.util.CacheUtil;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.dict.FileDict;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.product.Category;
import com.sw.common.entity.product.CategoryTree;
import com.sw.common.entity.system.File;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.product.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "商品分类接口")
@Controller
@RequestMapping("category")
public class CategoryController extends BaseController<CategoryServiceImpl, Category> {


    @Autowired
    FileServiceImpl fileService;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    CategoryServiceImpl categoryService;

    @Override
    @ApiOperation("组装分类前台展示")
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<Category> list = (List<Category>) data.get("list");
        Map<Long, String> map = new HashMap<>();
        List<Map<String, Object>> newList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(Category category:list){
              /*  if("0".equals(category.getParentId())){
                    continue;
                }*/
                Map<String, Object> _map = new HashMap<>();
                map.put(category.getId(), category.getCategoryName());
                _map.put("label", category.getCategoryName());
                _map.put("value", category.getId());
                _map.put("parentId", category.getParentId());
                newList.add(_map);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation("获取分类树")
    @ResponseBody
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public DataResponse tree(String name){
        log.info("============= {开始调用方法：tree(} =============");
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.orderBy(true, true, "CATEGORY_SEQ");
        if(StringUtil.isNotEmpty(name)){
            wrapper.like("CATEGORY_NAME", name);
        }
        List<Category> categories = service.list(wrapper);

        if(CollectionUtil.isNotEmpty(categories)){
            for(Category category:categories){
                QueryWrapper<File> fileEntityWrapper = new QueryWrapper<>();
                fileEntityWrapper.eq("FILE_TYPE", FileDict.CATEGORY.getCode());
                fileEntityWrapper.eq("IS_DELETE", 0);
                fileEntityWrapper.eq("FK_ID", category.getId());
                List<File> sysFiles = fileService.list(fileEntityWrapper);
                if(CollectionUtil.isNotEmpty(sysFiles)){
                    category.setFileUrl(sysFiles.get(0).getFileUrl());
                }else{
                    category.setFileUrl(DEFAULT_URL);
                }
            }
        }

        List<CategoryTree> list = getCategoryTree(categories, BaseConstants.MENU_ROOT);

        result.put("list", list);
        log.info("============= {结束调用方法：tree(} =============");
        return DataResponse.success(result);
    }

    @ApiOperation("获取分类树")
    @ResponseBody
    @RequestMapping(value = "categoryTree", method = RequestMethod.GET)
    public DataResponse categoryTree(){
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<Category> list = (List<Category>) data.get("list");
        if(!CollectionUtils.isEmpty(list)){
            for(Category _category:list){
                setParentCategory(_category);
            }
        }

        Category category = new Category();
        category.setId(0L);
        category.setIsDelete(0);
        category.setCategoryName("顶级节点");
        category.setCategoryNo("top");
        category.setParentId(1000000L);
        list.add(category);

        List<CategoryTree> trees = getCategoryTree(list, 1000000L);
        Map<String, Object> result = new HashMap<>();
        result.put("categoryTree", trees);
        return DataResponse.success(result);
    }

    @ApiOperation("小程序获取分类详情")
    @ResponseBody
    @RequestMapping(value = "getCategoryInfo/{id}", method = RequestMethod.GET)
    public DataResponse getCategoryInfo(@PathVariable Long id){

        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Category category = (Category) data.get("obj");

        // 获取同级分类
        if(category == null){
            return DataResponse.fail("没有获取到对应的分类");
        }

        // 获取子分类
        QueryWrapper<Category> childWrapper = new QueryWrapper<>();
        childWrapper.eq("PARENT_ID", category.getParentId());
        childWrapper.eq("IS_USED", "SW1302");
        childWrapper.eq("IS_DELETE", 0);
        childWrapper.orderBy(true, false, "CATEGORY_NO");

        List<Category> brotherCategoryList = categoryService.list(childWrapper);

        List<CategoryTree> trees = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(brotherCategoryList)){
            for (Category _category:brotherCategoryList){
                CategoryTree categoryTree = setCategoryTree(_category);
                trees.add(categoryTree);
            }
        }

        data.put("list", trees);
        data.put("obj", setCategoryTree(category));
        dataResponse.put("data", data);
        log.info("==================结束调用 get================");

        return dataResponse;
    }

    @Override
    @ApiOperation("小程序根据ID获取分类")
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable Long id){

        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Category category = (Category) data.get("obj");

        if(BaseConstants.MENU_ROOT.equals(id)){
            category = new Category();
            category.setId(id);
            category.setCategoryName("顶级节点");
        }else{
            setParentCategory(category);
        }

        File file = getFile(category);
        if(file != null){
            category.setFileUrl(file.getFileUrl());
        }else{
            file = new File();
        }

        // 获取子分类
        QueryWrapper<Category> childWrapper = new QueryWrapper<>();
        childWrapper.eq("PARENT_ID", id);
        childWrapper.eq("IS_DELETE", 0);
        childWrapper.eq("IS_USED", "SW1302");
        childWrapper.orderBy(true, true, "CATEGORY_SEQ");
        List<Category> childCategoryList = categoryService.list(childWrapper);
        childCategoryList.add(category);
        for(Category child:childCategoryList){
            File _file = getFile(child);
            if(_file != null){
                child.setFileUrl(_file.getFileUrl());
            } else {
                child.setFileUrl(DEFAULT_URL);
            }
        }

        if(!(0L == id)){
            List<CategoryTree> trees = getCategoryTree(childCategoryList, 0L);
            data.put("tree", trees);
        }

        data.put("file", file);
        data.put("obj", category);
        dataResponse.put("data", data);
        log.info("==================结束调用 get================");

        return dataResponse;
    }

    private File getFile(Category category) {
        QueryWrapper<File> fileEntityWrapper = new QueryWrapper<>();
        fileEntityWrapper.eq("FILE_TYPE", FileDict.CATEGORY.getCode());
        fileEntityWrapper.eq("IS_DELETE", 0);
        fileEntityWrapper.eq("FK_ID", category.getId());
        List<File> _sysFiles = fileService.list(fileEntityWrapper);
        File _file = null;
        if(CollectionUtil.isNotEmpty(_sysFiles)){
            _file = _sysFiles.get(0);
        }
        return _file;
    }

    private CategoryTree setCategoryTree(Category category){
        CategoryTree categoryTree = new CategoryTree();
        categoryTree.setId(category.getId());
        categoryTree.setParentId(category.getParentId());
        categoryTree.setName(category.getCategoryName());
        categoryTree.setCode(category.getCategoryNo());
        categoryTree.setLabel(category.getCategoryName());
        categoryTree.setTitle(category.getCategoryName());
        categoryTree.setLevel(category.getCategoryLevel());
        categoryTree.setIsUsed(StatusDict.codeToMessage(category.getIsUsed()));
        return categoryTree;
    }

    @ApiOperation("添加分类")
    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Category category) {
        Long id = SnowflakeIdWorker.generateId();
        category.setId(id);
        super.add(user, category);
        List<Map<String, String>> fileList = category.getFileList();
        if(CollectionUtil.isNotEmpty(fileList)){
            Map<String, String> map = fileList.get(0);
            String url = MapUtil.getMapValue(map, "url");
            Long userId = user.getId();
            // 存入数据库
            File sysFile = new File();
            sysFile.setId(SnowflakeIdWorker.generateId());
            sysFile.setFileType(FileDict.CATEGORY.getCode());
            sysFile.setFkId(id);
            sysFile.setFileUrl(url);
            sysFile.setAddTime(DateUtil.getCurrentDateTime());
            sysFile.setIsDelete(0);
            sysFile.setAddUser(userId);
            sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
            sysFile.setUpdateUser(userId);
            fileService.save(sysFile);
        }
        return DataResponse.success();
    }

    @ApiOperation("更新分类")
    @Override
    @ResponseBody
    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    public DataResponse update(@CurrentUser(isFull = true) User user, @RequestBody Category category) {
        List<Map<String, String>> fileList = category.getFileList();
        if(CollectionUtil.isEmpty(fileList)){
            // 删除对应的图片
            fileService.deleteFile(category.getId());
        }
        return super.update(user, category);
    }

    private void setParentCategory(Category category) {
        Long parentId = category.getParentId();
        if(parentId.equals(BaseConstants.MENU_ROOT)){
            category.setParentCategoryName("顶级节点");
        }else{
            QueryWrapper<Category> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("IS_DELETE", 0);
            entityWrapper.eq("ID", parentId);
            Category _category = service.getOne(entityWrapper);
            if(_category != null){
                category.setParentCategoryName(_category.getCategoryName());
            }
        }
    }

    private List<CategoryTree> getCategoryTree(List<Category> list, Long rootId) {
        List<CategoryTree> trees = new ArrayList<>();
        CategoryTree tree;
        if(CollectionUtil.isNotEmpty(list)){
            for(Category obj:list){
                tree = new CategoryTree();
                tree.setId(obj.getId());
                tree.setParentId(obj.getParentId());
                tree.setName(obj.getCategoryName());
                tree.setCode(obj.getCategoryNo());
                tree.setTitle(obj.getCategoryName());
                tree.setLabel(obj.getCategoryName());
                tree.setLevel(obj.getCategoryLevel());
                tree.setIsUsed(StatusDict.codeToMessage(obj.getIsUsed()));
                tree.setUrl(obj.getFileUrl());
                trees.add(tree);
            }
        }
        return TreeUtil.build(trees, rootId);
    }

}
