package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonPackageRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-05-30
 */
@MyBatisMapper
public interface SysCommonPackageRelationMapper extends BaseMapper<SysCommonPackageRelation> {

    void deleteByPmCode(String cdpaPmCode);

    List<SysCommonPackageRelation> findByPackageCode(@Param("packageCode") String packageCode, @Param("dataSet") String dataSet);

    SysCommonPackageRelation findByPackageUom(@Param("packageCode") String packageCode, @Param("uom") String uom, @Param("dataSet") String dataSet);

    void batchInsert(List<SysCommonPackageRelation> list);
}