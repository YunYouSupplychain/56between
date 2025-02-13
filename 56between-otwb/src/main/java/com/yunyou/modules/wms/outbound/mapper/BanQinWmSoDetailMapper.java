package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCrossDockSoDetail1QueryEntity;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCrossDockSoDetail2QueryEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库单明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSoDetailMapper extends BaseMapper<BanQinWmSoDetail> {
    
    List<BanQinWmSoDetailEntity> findPage(BanQinWmSoDetailEntity banQinWmSoDetailEntity);

    List<BanQinWmSoDetailEntity> findEntityByNos(BanQinWmSoDetailEntity example);

    List<BanQinWmSoDetail> findInterceptBySoNo(@Param("soNos") String[] soNos, @Param("orgId") String orgId);

    List<BanQinWmSoDetail> findByWaveNo(@Param("waveNo") String waveNo, @Param("orgId") String orgId);

    List<String> findLdStatusBySoNoAndLineNo(@Param("soNo") String soNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);
    
    List<String> checkSoExistCd(@Param("soNos") List<String> soNos, @Param("orgId") String orgId);

    List<BanQinWmSoDetailByCdQuery> getEntityByCdAndSku(BanQinWmSoDetailByCdQuery entity);

    List<BanQinGetCrossDockSoDetail2QueryEntity> getCrossDockSoDetail2Query(BanQinGetCrossDockSoDetail2QueryEntity entity);

    List<BanQinGetCrossDockSoDetail1QueryEntity> getCrossDockSoDetail1Query(BanQinGetCrossDockSoDetail1QueryEntity entity);

    List<BanQinWmSoDetailEntity> findEntityList(BanQinWmSoDetailEntity entity);

    List<BanQinWmSoDetailEntity> findOrderCheckPage(BanQinWmSoDetailEntity banQinWmSoDetailEntity);

}