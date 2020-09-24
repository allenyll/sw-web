package com.sw.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 文件相关信息
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-26 21:28:23
 */
@Data
@TableName("sys_file")
public class File extends BaseEntity<File> {

	private static final long serialVersionUID = 1L;

	// 文件主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 外键Id
    private Long fkId;

	// 文件类型
    private String fileType;

	// 文件路径
    private String fileUrl;

    // 文件下载路径
    private String downloadUrl;

	// 备注
    private String remark;

}
