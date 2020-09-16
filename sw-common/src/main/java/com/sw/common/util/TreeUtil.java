package com.sw.common.util;

import com.sw.common.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:  树结构工具类
 * @Author:       allenyll
 * @Date:         ' 2:01 AM
 * @Version:      1.0
 */
public class TreeUtil {

    /**
     * 两层循环实现建树
     *
     * @param nodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeNode> List<T> build(List<T> nodes, Object root){
        if(nodes == null){
            return null;
        }
        List<T> trees = new ArrayList<T>();
        if (nodes.size() == 1) {
            trees.add(nodes.get(0));
        } else {
            for(T node:nodes){
                // 如果parentId是根结点id，该节点是其他节点的根结点
                if(root.equals(node.getParentId())){
                    trees.add(node);
                }

                // 获取该节点的所有子节点
                for(T nodeChild:nodes){
                    if(nodeChild.getParentId().equals(node.getId())){
                        if(node.getChildren() == null){
                            node.setChildren(new ArrayList<>());
                        }
                        node.add(nodeChild);
                    }
                }
            }
        }
        return trees;
    }

}
