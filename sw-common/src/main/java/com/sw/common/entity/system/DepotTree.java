package com.sw.common.entity.system;

import com.sw.common.entity.TreeNode;
import lombok.Data;

/**
 * @Description:  组织树
 * @Author:       allenyll
 * @Date:         2018/11/18 12:56 PM
 * @Version:      1.0
 */
@Data
public class DepotTree extends TreeNode {

    private String code;

    private String name;

    private String title;

    private String label;

    private boolean spread = false;

}
