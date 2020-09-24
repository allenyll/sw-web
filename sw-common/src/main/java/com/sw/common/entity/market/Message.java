package com.sw.common.entity.market;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 消息表，包含通知，推送，私信等
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-25 18:51:28
 */
@Data
@TableName("snu_message")
public class Message extends BaseEntity<Message> {

	private static final long serialVersionUID = 1L;

	// 主键ID
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 消息名称
    private String messageName;

	// 消息内容
    private String content;

	// 消息类型
    private String type;

	// 消息摘要
    private String abstractMsg;

	// 消息时间
    private String messageTime;

	// 消息创建人
    private String messageSender;

}
