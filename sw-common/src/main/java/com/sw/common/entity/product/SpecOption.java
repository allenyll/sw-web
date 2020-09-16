package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 规格选项
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-13 16:09:07
 */
@Data
@TableName("snu_spec_option")
public class SpecOption extends BaseEntity<SpecOption> {

	private static final long serialVersionUID = 1L;

	// 规格选项
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkSpecOptionId;

	//
    private String fkSpecsId;

	// 名称
    private String name;

	// 编码
    private String code;

	@Override
    protected Serializable pkVal() {
		return pkSpecOptionId;
	}



}
