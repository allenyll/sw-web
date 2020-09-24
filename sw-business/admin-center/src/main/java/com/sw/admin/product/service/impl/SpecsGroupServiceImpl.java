package com.sw.admin.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.SpecsGroup;
import com.sw.admin.product.mapper.SpecsGroupMapper;
import com.sw.admin.product.service.ISpecsGroupService;
import org.springframework.stereotype.Service;

/**
 * 规格组
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-13 16:05:43
 */
@Service("specsGroupService")
public class SpecsGroupServiceImpl extends ServiceImpl<SpecsGroupMapper, SpecsGroup> implements ISpecsGroupService {

}
