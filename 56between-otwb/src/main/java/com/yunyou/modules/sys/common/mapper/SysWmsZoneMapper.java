package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsZone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库区MAPPER接口
 */
@MyBatisMapper
public interface SysWmsZoneMapper extends BaseMapper<SysWmsZone> {
    List<SysWmsZone> findPage(SysWmsZone cdWhZone);

    List<SysWmsZone> findGrid(SysWmsZone cdWhZone);

    SysWmsZone getByCode(@Param("zoneCode") String zoneCode, @Param("dataSet") String dataSet);
}