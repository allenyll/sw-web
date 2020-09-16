package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 属性选项表,也就是属性的详情
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-12 17:55:00
 */
@Data
@TableName("snu_attr_option")
public class AttrOption extends BaseEntity<AttrOption> {

	private static final long serialVersionUID = 1L;

	//
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkAttrOptionId;

	// 属性Id
    private String fkAttributeId;

	// 名称
    private String optionName;

	@Override
    protected Serializable pkVal() {
		return pkAttrOptionId;
	}



}
