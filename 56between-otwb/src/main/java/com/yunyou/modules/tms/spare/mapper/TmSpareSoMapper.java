package com.yunyou.modules.tms.spare.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoScanInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmSpareSoMapper {

    TmSpareSoEntity getEntity(String id);

    TmSpareSoDetailEntity getDetailEntity(String id);

    TmSpareSoScanInfoEntity getScanInfoEntity(String id);

    List<TmSpareSoDetailEntity> findDetailList(TmSpareSoDetailEntity qEntity);

    List<TmSpareSoScanInfoEntity> findScanInfoList(TmSpareSoScanInfoEntity qEntity);

    List<TmSpareSoEntity> findSoPage(TmSpareSoEntity qEntity);

    List<TmSpareSoDetailEntity> findDetailPage(TmSpareSoDetailEntity qEntity);

    List<TmSpareSoScanInfoEntity> findScanInfoPage(TmSpareSoScanInfoEntity qEntity);

    void removeDetailByNo(@Param("spareSoNo") String spareSoNo, @Param("orgId") String orgId);

    TmSpareSoEntity getByNo(@Param("spareSoNo") String spareSoNo, @Param("orgId") String orgId);

    TmSpareSoDetailEntity getDetailByNoAndFitting(@Param("spareSoNo") String spareSoNo, @Param("fittingCode") String fittingCode, @Param("orgId") String orgId);
}
