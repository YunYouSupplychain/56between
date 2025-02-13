package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeaderEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmRepOutDailyQueryEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import com.yunyou.modules.wms.report.entity.PickingOrderLabel;
import com.yunyou.modules.wms.report.entity.ShipHandoverOrder;
import com.yunyou.modules.wms.report.entity.ShipOrderLabel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出库单MAPPER接口
 *
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSoHeaderMapper extends BaseMapper<BanQinWmSoHeader> {

    BanQinWmSoEntity getEntity(String id);

    List<BanQinWmSoEntity> findPage(BanQinWmSoEntity banQinWmSoEntity);

    List<BanQinWmSoEntity> findGrid(BanQinWmSoEntity banQinWmSoEntity);

    List<BanQinWmSoEntity> findEntity(BanQinWmSoEntity entity);

    List<String> findSoNoInterceptOrHoldStatus(@Param("isInterceptStatus") String isInterceptStatus, @Param("isHoldStatus") String isHoldStatus, @Param("soNos") List<String> soNos, @Param("lineNos") List<String> lineNos, @Param("preallocIds") List<String> preallocIds, @Param("allocIds") List<String> allocIds, @Param("waveNos") List<String> waveNos, @Param("orgId") String orgId);

    void updateSoHeaderStatusByWave(@Param("waveNos") List<String> waveNos, @Param("userId") String userId, @Param("orgId") String orgId);

    void updateSoHeaderStatusBySo(@Param("soNos") List<String> soNos, @Param("userId") String userId, @Param("orgId") String orgId);

    List<String> checkSoIsCdByCreateWave(@Param("soNos") List<String> soNos, @Param("orgId") String orgId);

    List<BanQinWmSoHeader> checkSoIsClose(@Param("ldNo") String ldNo, @Param("orgId") String orgId);

    List<PickingOrderLabel> getPickingOrder(List<String> soIds);

    List<ShipOrderLabel> getShipOrder(List<String> soIds);

    List<BanQinWmRepOutDailyQueryEntity> wmRepOutDailyQuery(BanQinWmRepOutDailyQueryEntity entity);

    List<BanQinWmRepOutDailyQueryEntity> countTotalQuery(BanQinWmRepOutDailyQueryEntity entity);

    List<BanQinWmSoEntity> rfInvGetPickBoxHeaderQuery(@Param("toId") String toId, @Param("orgId") String orgId);

    void updateConsigneeInfo(BanQinWmSoEntity entity);

    void updateCarrierInfo(BanQinWmSoEntity entity);

    List<Map<String, Object>> countBySoQuery(BanQinWmSoEntity entity);

    List<String> findSoNosByWaveGroup(BanQinCdRuleWvGroupHeaderEntity entity);

    int updateInterceptStatus(@Param("soNo") String soNo, @Param("interceptStatus") String interceptStatus, @Param("interceptTime") Date interceptTime, @Param("orgId") String orgId, @Param("recVer") Integer recVer);

    List<BanQinWmSoHeader> findSoNosForIntercept();

    List<ShipHandoverOrder> getShipHandoverOrder(List<String> soIds);

    void pushedToTms(@Param("id") String id, @Param("isPushed") String isPushed);
}