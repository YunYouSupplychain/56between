package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsPackageRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装明细MAPPER接口
 */
@MyBatisMapper
public interface SysWmsPackageRelationMapper extends BaseMapper<SysWmsPackageRelation> {

    void deleteByPmCode(String cdpaPmCode);

    List<SysWmsPackageRelation> findPage(SysWmsPackageRelation sysWmsPackageRelation);

    List<SysWmsPackageRelation> findByPackCode(@Param("packCode") String packCode, @Param("dataSet") String dataSet);

    void batchInsert(List<SysWmsPackageRelation> list);

    void remove(@Param("packCode") String packCode, @Param("dataSet") String dataSet);
}