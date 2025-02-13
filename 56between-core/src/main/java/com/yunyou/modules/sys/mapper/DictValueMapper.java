package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.DictValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字典MAPPER接口
 *
 * @author lgf
 * @version 2017-01-16
 */
@MyBatisMapper
public interface DictValueMapper extends BaseMapper<DictValue> {

    List<DictValue> findInitList(@Param(value = "type") String type, @Param(value = "value") String value, @Param(value = "label") String label);

    List<DictValue> findInitFeeList(@Param(value = "type") String type);

    List<DictValue> findDictListByType(@Param(value = "type") String type);

    List<DictValue> findDictListByTypeLabel(@Param(value = "type") String type, @Param(value = "label") String label);
}