package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装MAPPER接口
 */
@MyBatisMapper
public interface SysWmsPackageMapper extends BaseMapper<SysWmsPackage> {

    SysWmsPackage findByPackageCode(@Param("packCode") String packCode, @Param("dataSet") String dataSet);

    List<SysWmsPackage> findPage(SysWmsPackage sysWmsPackage);

    List<SysWmsPackage> findGrid(SysWmsPackage sysWmsPackage);

    void batchInsert(List<SysWmsPackage> list);

    void remove(@Param("packCode") String packCode, @Param("dataSet") String dataSet);

}