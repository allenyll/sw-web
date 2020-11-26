package com.sw.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;


/**
 * 店铺表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-20 16:05:54
 */
@ToString
@Data
@TableName("snu_shop")
public class Shop extends BaseEntity<Shop>  {

	private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 店铺名称
    */
    private String shopName;

    /**
    * 店铺编号
    */
    private String shopCode;

    /**
    * 店铺状态
    */
    private String status;

    /**
    * 店主姓名
    */
    private String shopKeeper;

    /**
    * 店主手机号
    */
    private String shopPhone;

    /**
    * QQ号
    */
    private String shopQq;

    /**
    * 微信号
    */
    private String shopWechat;

    /**
    * 店铺地址
    */
    private String shopAddress;

    /**
    * 店铺图标
    */
    private String shopImg;

    /**
    * 开始营业时间
    */
    private String startTime;

    /**
    * 结束营业时间
    */
    private String endTime;

    /**
    * 店铺营业状态
    */
    private String shopActive;

    /**
    * 能否开发票
    */
    private String isInvoice;

    /**
    * 发票说明
    */
    private String invoiceRemark;

	@Override
    protected Serializable pkVal() {
		return id;
	}

}
