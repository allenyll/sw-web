package com.sw.admin.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.market.AdPosition;
import com.sw.admin.market.mapper.AdPositionMapper;
import com.sw.admin.market.service.IAdPositionService;
import org.springframework.stereotype.Service;

/**
 * 广告位表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-19 20:12:58
 */
@Service("adPositionService")
public class AdPositionServiceImpl extends ServiceImpl<AdPositionMapper, AdPosition> implements IAdPositionService {

}
