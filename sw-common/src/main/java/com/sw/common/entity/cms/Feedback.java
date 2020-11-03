package com.sw.common.entity.cms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;


/**
 * 意见反馈表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-03 14:44:18
 */
@ToString
@Data
@TableName("snu_feedback")
public class Feedback extends BaseEntity<Feedback>  {

	private static final long serialVersionUID = 1L;

	// 主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 意见类型
    private String type;

	// 意见内容
    private String content;

	// 联系方式
    private String phone;

	@Override
    protected Serializable pkVal() {
		return id;
	}

}
