package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 属性值
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-12 17:47:06
 */
@Data
@TableName("snu_attributes")
public class Attributes extends BaseEntity<Attributes> {

	private static final long serialVersionUID = 1L;

	// 属性Id
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkAttributeId;

	// 分类主键
    private String fkCategoryId;

	// 属性名称
    private String attrName;

	// 属性类型
    private String attType;

	// 属性值
    private String attrVal;

	// 排序
    private Integer attrSeq;

	// 是否显示
    private String isShow;

	//
    private String status;

    @TableField(exist = false)
    private String[] categoryIds;

	@Override
    protected Serializable pkVal() {
		return pkAttributeId;
	}



}
