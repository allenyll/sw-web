package com.sw.admin.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.SpecOption;
import com.sw.admin.product.mapper.SpecOptionMapper;
import com.sw.admin.product.service.ISpecsOptionService;
import org.springframework.stereotype.Service;

/**
 * 规格选项
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-13 16:09:07
 */
@Service("specOptionService")
public class SpecOptionServiceImpl extends ServiceImpl<SpecOptionMapper, SpecOption> implements ISpecsOptionService {

}
