package com.sw.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.system.Depot;
import com.sw.system.mapper.DepotMapper;
import com.sw.system.service.IDepotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2020/8/31 2:59 下午
 * @Version:      1.0
 */
@Service("depotService")
@Transactional(rollbackFor = Exception.class)
public class DepotServiceImpl extends ServiceImpl<DepotMapper, Depot> implements IDepotService {

}
