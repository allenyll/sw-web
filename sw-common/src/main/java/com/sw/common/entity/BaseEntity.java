package com.sw.common.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @Description:  共有实体
 * @Author:       allenyll
 * @Date:         2020/8/31 10:37 上午
 * @Version:      1.0
 */
@Data
public abstract class BaseEntity<T extends BaseEntity> extends Model {

    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 创建人
     */
    private Long addUser;
    /**
     * 创建时间
     */
    private String addTime;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 更新时间
     */
    private String updateTime;


}
