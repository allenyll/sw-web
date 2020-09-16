package com.sw.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;


/**
 * <p>
 * 用户表
 * 设置用户权限，所以要放在公用模块
 * </p>
 *
 * @author yu.leilei
 * @since 2018-06-12
 */
@TableName("sys_user")
@Data
@ToString
public class User extends BaseEntity<User> {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 组织ID
	 */
	private Long depotId;

	@TableField(exist = false)
	private String depotName;

	/**
	 * 名称
	 */
	private String userName;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 密码盐
	 */
	private String salt;
	/**
	 * 会员状态
	 */
	private String status;

	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 头像
	 */
	private String picId;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 区划
	 */
	private String area;
	/**
	 * 最后更新密码时间
	 */
	private Date lastPasswordResetDate;

}



