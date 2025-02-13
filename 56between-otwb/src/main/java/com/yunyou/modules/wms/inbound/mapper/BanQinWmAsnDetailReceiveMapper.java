package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnReceiveByCdQuery;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收货明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmAsnDetailReceiveMapper extends BaseMapper<BanQinWmAsnDetailReceive> {
    List<BanQinWmAsnDetailReceiveEntity> findPage(BanQinWmAsnDetailReceiveEntity banQinWmAsnDetailReceiveEntity);

    List<BanQinWmAsnDetailReceiveEntity> findGrid(BanQinWmAsnDetailReceiveEntity banQinWmAsnDetailReceiveEntity);

    Integer getMaxLineNo(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceive> getVoucherDetail(@Param("headId") String headId, @Param("lienNo") String lienNo, @Param("isVoucher") String isVoucher);

    List<BanQinWmAsnDetailReceive> getArrangeLocDetail(@Param("headId") String headId, @Param("lienNo") String lienNo, @Param("isArrangeLoc") String isArrangeLoc);

    List<BanQinWmAsnDetailReceive> getCrossDockDetail(@Param("headId") String headId, @Param("lienNo") String lienNo, @Param("isCrossDock") String isCrossDock);

    List<BanQinWmAsnDetailReceive> findCanPalletizeDetail(@Param("asnNo") String asnNo, @Param("asnLineNo") String asnLineNo, @Param("status") String status, @Param("isCrossDock") String isCrossDock, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceive> findCancelPalletizeDetail(@Param("asnNo") String asnNo, @Param("asnLineNo") String asnLineNo, @Param("status") String status, @Param("isCrossDock") String isCrossDock, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceive> getRepeatPlanId(@Param("asnNo") String asnNo, @Param("asnLineNos") String[] asnLineNos, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceive> findCancelReceiveDetail(@Param("asnNo") String asnNo, @Param("asnLineNo") String asnLineNo, @Param("planId") String planId, @Param("status") String status, @Param("cdRcvId") String cdRcvId, @Param("qcRcvId") String qcRcvId, @Param("orgId") String orgId);

    List<String> getLineNoByCheckStatus(@Param("asnNo") String asnNo, @Param("lineNos") String[] lineNos, @Param("lineStatus") String[] lineStatus, @Param("isArrangeLoc") String isArrangeLoc, @Param("isVoucher") String isVoucher, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceive> findUnQcDetail(@Param("asnNo") String asnNo, @Param("asnLineNo") String asnLineNo, @Param("orgId") String orgId);

    void updateStatus(@Param("asnNo") String asnNo, @Param("asnLineNo") String asnLineNo, @Param("status") String status, @Param("orgId") String orgId);

    void updateLogisticNo(@Param("asnNo") String asnNo, @Param("logisticNo") String logisticNo, @Param("orgId") String orgId);

    void removeByAsnNoAndLineNo(@Param("asnNo") String asnNo, @Param("lineNos") String[] lineNos, @Param("orgId") String orgId);

    void createVoucherNoByAsn(@Param("asnId") String asnId, @Param("lineNos") String[] lineNos, @Param("voucherNo") String voucherNo);

    void cancelVoucherNoByAsn(@Param("asnId") String asnId, @Param("lineNos") String[] lineNos);

    void updatePlanPaLoc(@Param("asnNo") String asnNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    void updatePaIdNull(@Param("paId") String paId, @Param("orgId") String orgId);

    void removeByAsnNo(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceive> rfPaCheckCreatePATaskByToIDQuery(@Param("toId") String toId, @Param("orgId") String orgId);

    List<TraceLabel> getTraceLabel(List<String> asnDetailReceiveIds);

    List<BanQinWmAsnReceiveByCdQuery> getEntityByCdAndSku(BanQinWmAsnReceiveByCdQuery entity);

    List<BanQinWmAsnDetailReceiveEntity> findEntityList(BanQinWmAsnDetailReceiveEntity entity);

    List<BanQinWmAsnDetailReceiveEntity> findEntityByIds(@Param("ids") List<String> ids);
}