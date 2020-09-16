package com.sw.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * <p>
 * 管理部门
 * </p>
 *
 * @author yu.leilei
 * @since 2018-11-20
 */
@TableName("sys_depot")
@Data
@ToString
public class Depot extends BaseEntity<Depot> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 部门主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 父级部门ID
	 */
	private Long pid;

	@TableField(exist = false)
	private String parentDepotName;
	/**
	 * 部门名称
	 */
	private String depotName;
	/**
	 * 部门编码
	 */
	private String depotCode;
	/**
	 * 排序
	 */
	private Integer sort;

}
