package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmPackageRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface OmPackageRelationMapper extends BaseMapper<OmPackageRelation> {

    List<OmPackageRelation> findPage(OmPackageRelation entity);

    OmPackageRelation findByPackageUom(@Param("packageCode") String packageCode, @Param("uom") String uom, @Param("orgId") String orgId);

    void deleteByPmCode(String cdpaPmCode);

    void remove(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void batchInsert(List<OmPackageRelation> list);
}