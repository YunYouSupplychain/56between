package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmElectronicFence;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmElectronicFenceMapper extends BaseMapper<TmElectronicFence> {

    TmElectronicFence getByCode(@Param("fenceCode") String fenceCode, @Param("orgId") String orgId);

    List<TmElectronicFence> findPage(TmElectronicFence entity);

    List<TmElectronicFence> findGrid(TmElectronicFence entity);
}
