package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsPackageRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装明细MAPPER接口
 */
@MyBatisMapper
public interface SysOmsPackageRelationMapper extends BaseMapper<SysOmsPackageRelation> {
    void deleteByPmCode(String cdpaPmCode);

    List<SysOmsPackageRelation> findPage(SysOmsPackageRelation entity);

    SysOmsPackageRelation findByPackageUom(@Param("packageCode") String packageCode, @Param("uom") String uom, @Param("dataSet") String dataSet);

    void batchInsert(List<SysOmsPackageRelation> list);

    void remove(@Param("packageCode") String packageCode, @Param("dataSet") String dataSet);
}