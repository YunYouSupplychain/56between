package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inbound.entity.BanQinWmRepInDailyQueryEntity;
import com.yunyou.modules.wms.report.entity.ReceivingOrderLabel;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import com.yunyou.modules.wms.report.entity.WmCheckReceiveOrderLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 入库单MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmAsnHeaderMapper extends BaseMapper<BanQinWmAsnHeader> {
    List<BanQinWmAsnEntity> findPage(BanQinWmAsnEntity banQinWmAsnEntity);

    BanQinWmAsnEntity getEntity(String id);

    BanQinWmAsnHeader getByAsnNo(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    List<String> findNotQCById(@Param("orderIds") List<String> orderIds);

    List<BanQinWmAsnHeader> getByIds(@Param("ids") String[] ids);

    void updateAuditStatusById(@Param("orderIds") List<String> orderIds, @Param("auditStatus") String auditStatus, @Param("auditOp") String auditOp);

    void updateHoldStatusById(@Param("orderIds") List<String> orderIds, @Param("holdStatus") String holdStatus, @Param("holdOp") String holdOp);

    void updateStatus(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    void removeByAsnNo(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    List<BanQinWmAsnHeader> checkAsnStatusQuery(@Param("asnIds") String[] asnIds, @Param("status") String[] status, @Param("auditStatus") String[] auditStatus, @Param("holdStatus") String[] holdStatus,
                                                @Param("isPalletize") String isPalletize, @Param("isVoucher") String isVoucher, @Param("isArrangeLoc") String isArrangeLoc, @Param("isCrossDock") String isCrossDock);

    List<ReceivingOrderLabel> getReceivingOrder(List<String> asnHeaderIds);

    List<TraceLabel> getTraceLabel(List<String> asnHeaderIds);

    List<BanQinWmRepInDailyQueryEntity> wmRepInDailyQuery(BanQinWmRepInDailyQueryEntity entity);

    List<BanQinWmRepInDailyQueryEntity> countQtyQuery(BanQinWmRepInDailyQueryEntity entity);

    List<Map<String, Long>> rfRcCheckAsnIsPalletizeQuery(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    List<BanQinWmAsnDetailReceiveEntity> rfRcGetAsnDetailByTraceIDOrSkuQuery(@Param("asnNo") String asnNo, @Param("planId") String planId, @Param("skuCode") String skuCode, @Param("funType") String funType, @Param("orgId") String orgId);

    List<WmCheckReceiveOrderLabel> getCheckReceiveOrder(List<String> asnHeaderIds);
}