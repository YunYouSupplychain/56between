package com.yunyou.modules.sys.service;

import java.util.List;

import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.config.CacheNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.DictType;
import com.yunyou.modules.sys.entity.DictValue;
import com.yunyou.modules.sys.mapper.DictTypeMapper;
import com.yunyou.modules.sys.mapper.DictValueMapper;

/**
 * 数据字典Service
 *
 * @author lgf
 * @version 2017-01-16
 */
@Service
@Transactional(readOnly = true)
public class DictTypeService extends CrudService<DictTypeMapper, DictType> {

    @Autowired
    private DictValueMapper dictValueMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public DictType get(String id) {
        DictType dictType = super.get(id);
        if (dictType != null) {
            dictType.setDictValueList(dictValueMapper.findList(new DictValue(dictType)));
        }
        return dictType;
    }

    public DictValue getDictValue(String id) {
        return dictValueMapper.get(id);
    }

    @Override
    @Transactional
    public void save(DictType dictType) {
        super.save(dictType);
        redisUtils.delete(CacheNames.SYS_CACHE_DICT_MAP);
    }

    @Transactional
    public void saveDictValue(DictValue dictValue) {
        if (StringUtils.isBlank(dictValue.getId())) {
            dictValue.preInsert();
            dictValueMapper.insert(dictValue);
        } else {
            dictValue.preUpdate();
            dictValueMapper.update(dictValue);
        }
        redisUtils.delete(CacheNames.SYS_CACHE_DICT_MAP);
    }

    @Transactional
    public void deleteDictValue(DictValue dictValue) {
        dictValueMapper.delete(dictValue);
        redisUtils.delete(CacheNames.SYS_CACHE_DICT_MAP);
    }

    @Override
    @Transactional
    public void delete(DictType dictType) {
        super.delete(dictType);
        dictValueMapper.delete(new DictValue(dictType));
        redisUtils.delete(CacheNames.SYS_CACHE_DICT_MAP);
    }

    public List<DictValue> getDictListByType(String type) {
        return dictValueMapper.findDictListByType(type);
    }

}