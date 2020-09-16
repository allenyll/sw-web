package com.sw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.system.Dict;
import com.sw.common.util.CollectionUtil;
import com.sw.system.mapper.DictMapper;
import com.sw.system.service.IDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 字典表
 服务实现类
 * </p>
 *
 * @author yu.leilei
 * @since 2019-02-15
 */
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

   @Resource
    DictMapper dictMapper;

    public List<Dict> selectList(QueryWrapper<Dict> wrapper) {
        return dictMapper.selectList(wrapper);
    }

    public Dict getDictByCode(String code){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("CODE", code);
        List<Dict> dictList = dictMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(dictList)){
            return dictList.get(0);
        }
        return null;
    }
}
