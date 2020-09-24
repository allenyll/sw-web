package com.sw.admin.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.market.Ad;
import com.sw.admin.market.mapper.AdMapper;
import com.sw.admin.market.service.IAdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-19 20:14:24
 */
@Service("adService")
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdServiceImpl.class);
}
