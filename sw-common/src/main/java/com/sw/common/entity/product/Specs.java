package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 规格表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-13 16:08:41
 */
@Data
@TableName("snu_specs")
public class Specs extends BaseEntity<Specs> {

	private static final long serialVersionUID = 1L;

	//
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 规格主键
    private Long specsGroupId;

	// 分类主键
    private String categoryId;

	// 名称
    private String specsName;

	// 类型
    private String specsType;

	// 值
    private String specsVal;

	// 排序
    private Integer specsSeq;

	// 是否显示
    private String isShow;

	//
    private String status;

	@TableField(exist = false)
	private Long[] categoryIds;

}
