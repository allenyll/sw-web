package com.sw.common.entity.market;

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
 * @date 2019-12-19 20:14:24
 */
@Data
@TableName("snu_ad")
public class Ad extends BaseEntity<Ad> {

	private static final long serialVersionUID = 1L;

	// 广告主键ID
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkAdId;

	// 主键ID
    private String fkAdPositionId;

	// 广告名称
    private String adName;

	// 广告类型
    private String adType;

	// 页面链接
    private String link;

	// 图片地址
    private String imageUrl;

	// 是否启用
    private String isUsed;

	// 内容
    private String connent;

	// 开始时间
    private String startTime;

	// 结束时间
    private String endTime;

	@Override
    protected Serializable pkVal() {
		return pkAdId;
	}



}
