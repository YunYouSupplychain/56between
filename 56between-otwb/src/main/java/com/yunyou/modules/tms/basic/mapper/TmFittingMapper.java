package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmFitting;
import com.yunyou.modules.tms.basic.entity.extend.TmFittingEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmFittingMapper extends BaseMapper<TmFitting> {

    List<TmFittingEntity> findPage(TmFittingEntity qEntity);

    List<TmFittingEntity> findGrid(TmFittingEntity qEntity);

    TmFittingEntity getEntity(String id);

    TmFitting getByCode(@Param("fittingCode") String fittingCode, @Param("orgId") String orgId);

    void remove(@Param("fittingCode") String fittingCode, @Param("orgId") String orgId);

}
