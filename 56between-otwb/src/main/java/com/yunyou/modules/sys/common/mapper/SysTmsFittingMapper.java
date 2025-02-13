package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsFitting;
import com.yunyou.modules.sys.common.entity.extend.SysTmsFittingEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface SysTmsFittingMapper extends BaseMapper<SysTmsFitting> {

    List<SysTmsFittingEntity> findPage(SysTmsFittingEntity qEntity);

    List<SysTmsFittingEntity> findGrid(SysTmsFittingEntity qEntity);

    SysTmsFittingEntity getEntity(String id);

    SysTmsFitting getByCode(@Param("fittingCode") String fittingCode, @Param("dataSet") String dataSet);
}
