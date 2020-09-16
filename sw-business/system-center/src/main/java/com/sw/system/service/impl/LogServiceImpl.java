package com.sw.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.system.Log;
import com.sw.system.mapper.LogMapper;
import com.sw.system.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2020-04-28 23:40
 * @Version:      1.0
 */
@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);

}
