package com.yunyou.modules.tms.order.manager.mapper;

import java.util.List;

import com.yunyou.modules.tms.order.entity.extend.*;
import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmCarrierFreight;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;

@MyBatisMapper
public interface TmDispatchOrderMapper extends BaseMapper<TmDispatchOrderHeader> {

    TmDispatchOrderEntity getEntity(@Param("id") String id);

    List<TmDispatchOrderEntity> findPage(TmDispatchOrderEntity qEntity);

    List<TmDispatchOrderEntity> findOrderList(TmDispatchOrderEntity qEntity);

    List<TmDispatchOrderSiteEntity> findSiteList(TmDispatchOrderSiteEntity qEntity);

    List<TmDispatchOrderLabelEntity> findLabelPage(TmDispatchOrderLabelEntity qEntity);

    List<TmTransportOrderEntity> findTransportPage(TmDispatchOrderLabelEntity qEntity);

    List<TmDispatchSiteSelectLabelEntity> selectLabel(TmDispatchSiteSelectLabelEntity qEntity);

    List<TmDispatchSiteSelectLabelEntity> selectLabelForLeft3(TmDispatchSiteSelectLabelEntity qEntity);

    List<TmDispatchSiteSelectLabelEntity> selectedLabel(TmDispatchSiteSelectLabelEntity qEntity);

    List<TmDispatchOrderLabelEntity> findLabelList(TmDispatchOrderLabelEntity qEntity);

    List<TmCarrierFreight> findCarrierFreight(TmCarrierFreight qEntity);

    List<TmDispatchVehicleEntity> findDispatchVehicles(@Param("transportNo") String transportNo, @Param("baseOrgId") String baseOrgId, @Param("orgId") String orgId);

    List<TmNoReturnVehicleInfo> findNoReturnVehicle();

    List<TmDispatchVehicleEntity> findRunningVehicle(@Param("orgId") String orgId);
}
