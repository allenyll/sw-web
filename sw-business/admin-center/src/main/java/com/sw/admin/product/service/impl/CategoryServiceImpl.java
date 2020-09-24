package com.sw.admin.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.Category;
import com.sw.admin.product.mapper.CategoryMapper;
import com.sw.admin.product.service.ICategoryService;
import org.springframework.stereotype.Service;

/**
 * 商品分类
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-21 10:51:04
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
