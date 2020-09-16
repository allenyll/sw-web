package com.sw.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author yu.leilei
 * @since 2018-06-12
 */
@TableName("sys_menu")
@Data
@ToString
public class Menu extends BaseEntity<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键
     */
    @TableId(type = IdType.ASSIGN_ID)
	private Long id;
    /**
     * 菜单父ID
     */
	private Long pid;

	@TableField(exist = false)
	private String parentMenuName;
    /**
     * 菜单名称
     */
	private String menuName;
    /**
     * 菜单跳转地址
     */
	private String menuUrl;
    /**
     * 菜单权限
     */
	private String menuPerms;
    /**
     * 菜单类型
     */
	private String menuType;
	/**
	 * 菜单编码
	 */
	private String menuCode;
    /**
     * 菜单图标
     */
	private String menuIcon;
    /**
     * 排序
     */
	private Integer sortNum;

}
