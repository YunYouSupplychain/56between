package com.yunyou.modules.tms.spare.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoScanInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmSparePoMapper {

    TmSparePoEntity getEntity(String id);

    TmSparePoDetailEntity getDetailEntity(String id);

    TmSparePoScanInfoEntity getScanInfoEntity(String id);

    List<TmSparePoDetailEntity> findDetailList(TmSparePoDetailEntity qEntity);

    List<TmSparePoScanInfoEntity> findScanInfoList(TmSparePoScanInfoEntity qEntity);

    List<TmSparePoEntity> findPoPage(TmSparePoEntity qEntity);

    List<TmSparePoDetailEntity> findDetailPage(TmSparePoDetailEntity qEntity);

    List<TmSparePoScanInfoEntity> findScanInfoPage(TmSparePoScanInfoEntity qEntity);

    void removeDetailByNo(@Param("sparePoNo") String sparePoNo, @Param("orgId") String orgId);

    TmSparePoEntity getByNo(@Param("sparePoNo") String sparePoNo, @Param("orgId") String orgId);

    TmSparePoDetailEntity getDetailByNoAndFitting(@Param("sparePoNo") String sparePoNo, @Param("fittingCode") String fittingCode, @Param("orgId") String orgId);
}
