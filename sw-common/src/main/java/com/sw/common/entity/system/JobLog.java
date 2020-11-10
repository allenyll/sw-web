package com.sw.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;


/**
 * 调度任务日志记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-09 17:34:28
 */
@ToString
@Data
@TableName("sys_job_log")
public class JobLog extends BaseEntity<JobLog>  {

	private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 调度任务主键
    */
    private Long jobId;

    /**
    * 任务名称
    */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
    * 执行状态
    */
    private String status;

    /**
    * 执行时间
    */
    private Long time;

    /**
    * 消息
    */
    private String message;

	@Override
    protected Serializable pkVal() {
		return id;
	}

}
