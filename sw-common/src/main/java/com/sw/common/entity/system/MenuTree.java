package com.sw.common.entity.system;

import com.sw.common.entity.TreeNode;
import lombok.Data;

/**
 * @Description:  菜单树
 * @Author:       allenyll
 * @Date:         2018/11/18 12:56 PM
 * @Version:      1.0
 */
@Data
public class MenuTree extends TreeNode {

    private String code;

    private String name;

    private String title;

    private String label;

    private String href;

    private String icon;

    private boolean spread = false;

}
