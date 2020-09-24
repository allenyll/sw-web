package com.sw.common.entity.cms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 *
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-27 14:46:25
 */
@Data
@TableName("snu_search_history")
public class SearchHistory extends BaseEntity<SearchHistory> {

	private static final long serialVersionUID = 1L;

	// 主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 关键字
    private String keyword;

	// 搜索来源，如PC、小程序、APP等
    private String dataSource;

	// 会员Id
    private Long userId;

}
