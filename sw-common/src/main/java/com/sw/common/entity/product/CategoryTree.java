package com.sw.common.entity.product;

import com.sw.common.entity.TreeNode;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description:  商品分类树
 * @Author:       allenyll
 * @Date:         2019/3/21 5:16 PM
 * @Version:      1.0
 */
@Data
@ToString
public class CategoryTree extends TreeNode {

    private String code;

    private String name;

    private String title;

    private String label;

    private Integer level;

    private String isUsed;

    private boolean spread = false;

    private String url;

    private List<CategoryTree> child;
}
