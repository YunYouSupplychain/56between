package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装MAPPER接口
 */
@MyBatisMapper
public interface SysOmsPackageMapper extends BaseMapper<SysOmsPackage> {

    SysOmsPackage findByPackageCode(@Param("packCode") String packCode, @Param("dataSet") String dataSet);

    List<SysOmsPackage> findPage(SysOmsPackage entity);

    void batchInsert(List<SysOmsPackage> list);

    void remove(@Param("packageCode") String packageCode, @Param("dataSet") String dataSet);
}