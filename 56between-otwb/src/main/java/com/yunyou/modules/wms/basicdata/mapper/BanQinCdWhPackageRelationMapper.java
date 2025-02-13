package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装明细MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhPackageRelationMapper extends BaseMapper<BanQinCdWhPackageRelation> {

    BanQinCdWhPackageRelation findByPackageUom(@Param("packageCode") String packageCode, @Param("uom") String uom, @Param("orgId") String orgId);

    void deleteByPmCode(String cdpaPmCode);

    List<BanQinCdWhPackageRelation> findPage(BanQinCdWhPackageRelation cdWhPackageRelation);

    List<BanQinCdWhPackageRelation> findByPackCode(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void remove(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void batchInsert(List<BanQinCdWhPackageRelation> list);
}