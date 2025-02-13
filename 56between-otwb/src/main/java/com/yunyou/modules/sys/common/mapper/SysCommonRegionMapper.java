package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonRegion;
import com.yunyou.modules.sys.common.entity.extend.SysCommonRegionEntity;
import com.yunyou.modules.sys.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域MAPPER接口
 */
@MyBatisMapper
public interface SysCommonRegionMapper extends BaseMapper<SysCommonRegion> {
    List<SysCommonRegionEntity> findPage(SysCommonRegionEntity entity);

    List<SysCommonRegion> findGrid(SysCommonRegion entity);

    SysCommonRegionEntity getEntity(String id);

    SysCommonRegion getByCode(@Param("code") String code, @Param("dataSet") String dataSet);

    void insertArea(SysCommonRegion entity);

    void deleteArea(String id);

    List<Area> findPlace(String id);
}