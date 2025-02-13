package com.yunyou.modules.tms.applet.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.applet.entity.extend.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmAppMapper {

    List<TmItemEntity> querySkuList(@Param("ownerCode") String ownerCode, @Param("orgId") String orgId, @Param("skuCode") String skuCode, @Param("skuName") String skuName);

    void saveTmDemandPlanHeader(TmDemandPlanHeaderEntity entity);

    void saveTmDemandPlanDetail(TmDemandPlanDetailEntity entity);
    // 需求订单表头查询
    List<TmDemandPlanHeaderEntity> findTmDemandPlanList(TmDemandPlanHeaderEntity entity);
    // 需求订单明细查询
    List<TmDemandPlanDetailEntity> findTmDemandPlanDetailList(TmDemandPlanDetailEntity entity);

    void deleteTmDemandPlanHeader(@Param("planOrderNo") String planOrderNo, @Param("orgId") String orgId);

    void deleteTmDemandPlanDetail(@Param("planOrderNo") String planOrderNo, @Param("orgId") String orgId, @Param("skuCode") String skuCode);
    // 派车单查询
    List<TmDispatchOrderEntity> findTmDispatchOrderList(TmDispatchOrderEntity entity);
    // 派车单网点查询
    List<TmDispatchOrderSiteEntity> findTmDispatchOrderSiteList(TmDispatchOrderSiteEntity entity);
    // 派车单标签查询
    List<TmDispatchOrderLabelEntity> findTmDispatchOrderLabelList(TmDispatchOrderLabelEntity entity);
    // 出车安全检查表查询
    List<TmVehicleSafetyCheckEntity> findTmVehicleSafetyCheckList(TmVehicleSafetyCheckEntity entity);
    // get出车安全检查表
    TmVehicleSafetyCheckEntity getTmVehicleSafetyCheck(String id);
    // 派车关联运输订单查询
    List<TmDispatchTransportOrderEntity> findDispatchTransportOrderList(TmDispatchTransportOrderEntity entity);

    List<TmDispatchTransportOrderDetailEntity> findDispatchTransportOrderDetailList(TmDispatchTransportOrderDetailEntity entity);
    // 交接单查询
    List<TmHandoverOrderEntity> findTmHandoverOrderList(TmHandoverOrderEntity entity);
    // 交接单标签查询
    List<TmHandoverOrderLabelEntity> findTmHandoverOrderLabelList(TmHandoverOrderLabelEntity entity);
    // 交接单商品查询
    List<TmHandoverOrderSkuEntity> findTmHandoverOrderSkuList(TmHandoverOrderSkuEntity entity);
    // 车辆查询
    List<TmVehicleEntity> findTmVehicleList(TmVehicleEntity entity);
    // 维修工单查询
    List<TmRepairOrderEntity> findTmRepairOrderList(TmRepairOrderEntity entity);
    // 派车装罐信息查询
    List<TmDispatchTankInfoEntity> findTmDispatchTankInfoList(TmDispatchTankInfoEntity entity);
    // 异常处理单查询
    List<TmExceptionHandleBillEntity> findTmExceptionHandleBillList(TmExceptionHandleBillEntity entity);
    // 司机工资中间表查询
    List<TmMiddleSalaryEntity> findTmMiddleSalaryList(TmMiddleSalaryEntity entity);

    TmDispatchOrderEntity getDispatchOrder(String id);

    List<TmTransportObjEntity> findSiteList(TmTransportObjEntity entity);

    List<TmWaitingTimeApplyEntity> findWaitingTimeApplyList(TmWaitingTimeApplyEntity entity);
}
