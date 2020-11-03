package com.sw.admin.cms.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.cms.Feedback;
import com.sw.admin.cms.mapper.FeedbackMapper;
import com.sw.admin.cms.service.IFeedbackService;

/**
 * 意见反馈表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-03 14:44:18
 */
@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper,Feedback> implements IFeedbackService{

}
