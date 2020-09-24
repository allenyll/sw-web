package com.sw.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 字典表

 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-29
 */
@TableName("sys_dict")
@Data
@ToString
public class Dict extends BaseEntity<Dict> {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(type = IdType.ASSIGN_ID)
	private Long id;
    /**
     * 名称
     */
	private String name;
    /**
     * 编码
     */
	private String code;
    /**
     * 父字典
     */
	private String parentId;
    /**
     * 说明
     */
	private String description;
    /**
     * 是否有效
     */
	private String status;

}
