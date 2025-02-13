package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库区域MAPPER接口
 */
@MyBatisMapper
public interface SysWmsAreaMapper extends BaseMapper<SysWmsArea> {
    List<SysWmsArea> findPage(SysWmsArea entity);

    List<SysWmsArea> findGrid(SysWmsArea entity);

    SysWmsArea getByCode(@Param("areaCode") String areaCode, @Param("dataSet") String dataSet);
}