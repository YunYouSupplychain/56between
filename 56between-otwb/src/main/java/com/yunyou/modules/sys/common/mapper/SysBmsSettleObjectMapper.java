package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsSettleObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算对象MAPPER接口
 */
@MyBatisMapper
public interface SysBmsSettleObjectMapper extends BaseMapper<SysBmsSettleObject> {

    List<SysBmsSettleObject> findPage(SysBmsSettleObject entity);

    List<SysBmsSettleObject> findGrid(SysBmsSettleObject entity);

    SysBmsSettleObject getByCode(@Param("code") String code, @Param("dataSet") String dataSet);
}