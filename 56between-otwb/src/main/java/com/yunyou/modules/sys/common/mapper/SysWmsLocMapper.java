package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsLoc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库位MAPPER接口
 */
@MyBatisMapper
public interface SysWmsLocMapper extends BaseMapper<SysWmsLoc> {
    List<SysWmsLoc> findPage(SysWmsLoc entity);

    List<SysWmsLoc> findGrid(SysWmsLoc entity);

    SysWmsLoc getByCode(@Param("locCode") String locCode, @Param("dataSet") String dataSet);

    List<SysWmsLoc> getExistLoc(List<String> locList, String dataSet);
}