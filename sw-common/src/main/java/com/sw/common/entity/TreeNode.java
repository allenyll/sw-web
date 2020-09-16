package com.sw.common.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description:
 * @Author:         allenyll
 * @Date:           2018/11/18 1:12 AM
 * @Version:        1.0
 */
@Data
public class TreeNode {

    protected Long id;

    protected Long parentId;

    List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode node){
        children.add(node);
    }
}
