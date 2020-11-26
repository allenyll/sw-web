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
 * 商家地址
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-20 16:06:05
 */
@ToString
@Data
@TableName("snu_shop_address")
public class ShopAddress extends BaseEntity<ShopAddress>  {

	private static final long serialVersionUID = 1L;

    /**
    * 地址主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 会员主键
    */
    private Long shopId;

    /**
    * 发货点
    */
    private String deliveryPoint;

    /**
    * 名称
    */
    private String name;

    /**
    * 手机号
    */
    private String phone;

    /**
    * 状态
    */
    private String status;

    /**
    * 邮编
    */
    private String postCode;

    /**
    * 省
    */
    private String province;

    /**
    * 市
    */
    private String city;

    /**
    * 区/县
    */
    private String region;

    /**
    * 详细地址
    */
    private String detailAddress;

    /**
    * 是否默认地址
    */
    private String isDefault;

	@Override
    protected Serializable pkVal() {
		return id;
	}

}
