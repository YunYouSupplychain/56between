package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.report.entity.WmShipDataEntity;
import com.yunyou.modules.wms.report.entity.WmSoSkuLabel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分配拣货明细MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSoAllocMapper extends BaseMapper<BanQinWmSoAlloc> {

    BanQinWmSoAllocEntity getEntity(String id);
    
    List<BanQinWmSoAllocEntity> findPage(BanQinWmSoAllocEntity banQinWmSoAllocEntity);

    List<WmShipDataEntity> findShipData(WmShipDataEntity entity);

    List<BanQinWmSoAllocEntity> findEntity(BanQinWmSoAllocEntity banQinWmSoAllocEntity);
    
    List<BanQinWmSoAllocEntity> findByNo(BanQinWmSoAllocEntity banQinWmSoAllocEntity);
    
    Map<String, Double> checkPackingTotalInfo(@Param("soNo") String soNo, @Param("traceId") String traceId, @Param("orgId") String orgId);
    
    List<BanQinWmSoAllocEntity> findByWaveNos(@Param("waveNos") List<String> waveNos, @Param("uom") String uom, @Param("orgId") String orgId);
    
    List<BanQinWmSoAllocEntity> getWmSoAllocToTraceId(BanQinWmSoAllocEntity banQinWmSoAllocEntity);
    
    List<BanQinWmSoAllocEntity> getWmInterceptSoAlloc(@Param("allocIds") List<String> allocIds, @Param("soNos") List<String> soNos, @Param("lineNos") String lineNos,
                                                @Param("waveNos") List<String> waveNos, @Param("orgId") String orgId, @Param("status") String status);

    List<String> checkNoFullCheckBySoNos(@Param("soNos") List<String> soNos, @Param("orgId") String orgId);

    List<BanQinWmSoAllocEntity> getWmSoAllocByCd(BanQinWmSoAllocEntity banQinWmSoAllocEntity);

    List<BanQinWmSoAllocEntity> getWmCheck(BanQinWmSoAllocEntity banQinWmSoAllocEntity);

    List<BanQinWmSoAllocEntity> getWmPack(BanQinWmSoAllocEntity banQinWmSoAllocEntity);

    Map<String, Long> rfPKGetPickNumQuery(@Param("soNo") String soNo, @Param("pickNo") String pickNo, @Param("allocId") String allocId, @Param("orgId") String orgId, @Param("waveNo") String waveNo);

    List<BanQinWmSoAllocEntity> rfPKGetPickDetailQuery(@Param("soNo") String soNo, @Param("pickNo") String pickNo, @Param("allocId") String allocId, @Param("orgId") String orgId, @Param("waveNo") String waveNo);

    List<BanQinWmSoAllocEntity> rfInvGetPickBoxDetailQuery(@Param("soNo") String soNo, @Param("toId") String toId, @Param("orgId") String orgId);

    void updatePackInfo(@Param("id") String id, @Param("trackingNo") String trackingNo, @Param("consigneeAddress") String consigneeAddress, @Param("caseNo") String caseNo);

    void updatePackScanCount(@Param("id") String id, @Param("packScanCount") Integer packScanCount);

    void cancelPack(@Param("id") String id, @Param("checkStatus") String checkStatus, @Param("checkOp") String checkOp, @Param("checkTime") Date checkTime, @Param("updateBy") String updateBy);


    List<BanQinWmSoAllocEntity> findSkuDataPage(BanQinWmSoAllocEntity banQinWmSoAllocEntity);

    List<WmSoSkuLabel> getWmSoSkuLabel(BanQinWmSoAllocEntity banQinWmSoAllocEntity);

    List<BanQinWmSoAllocEntity> findEntityByIds(@Param("ids") List<String> ids);
}