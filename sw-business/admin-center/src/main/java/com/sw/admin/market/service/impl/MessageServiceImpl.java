package com.sw.admin.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.market.Message;
import com.sw.admin.market.mapper.MessageMapper;
import com.sw.admin.market.service.IMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 消息表，包含通知，推送，私信等
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-25 18:51:28
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
}
