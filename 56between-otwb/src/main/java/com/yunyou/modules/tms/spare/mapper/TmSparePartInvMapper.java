package com.yunyou.modules.tms.spare.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.spare.entity.TmSparePartInv;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePartInvEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmSparePartInvMapper extends BaseMapper<TmSparePartInv> {

    TmSparePartInvEntity getEntity(String id);

    TmSparePartInv getOnly(@Param("barcode") String barcode, @Param("baseOrgId") String baseOrgId);

    TmSparePartInvEntity getOnlyEntity(@Param("barcode") String barcode, @Param("baseOrgId") String baseOrgId);

    List<TmSparePartInvEntity> findPage(TmSparePartInvEntity qEntity);

    void deleteByNo(@Param("barcode") String barcode, @Param("baseOrgId") String baseOrgId);
}
