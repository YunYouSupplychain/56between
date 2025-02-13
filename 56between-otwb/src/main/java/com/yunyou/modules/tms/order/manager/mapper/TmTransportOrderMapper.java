package com.yunyou.modules.tms.order.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderCostEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderRouteEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderSkuEntity;

@MyBatisMapper
public interface TmTransportOrderMapper extends BaseMapper<TmTransportOrderHeader> {

    List<TmTransportOrderEntity> findOrderPage(TmTransportOrderEntity entity);

    List<TmTransportOrderSkuEntity> findSkuPage(TmTransportOrderSkuEntity qEntity);

    List<TmTransportOrderCostEntity> findCostPage(TmTransportOrderCostEntity qEntity);

    List<TmTransportOrderLabelEntity> findLabelPage(TmTransportOrderLabelEntity qEntity);

    List<TmTransportOrderRouteEntity> findRoutePage(TmTransportOrderRouteEntity qEntity);
    /*-------------------------------page---------------------------------------------*/

    List<TmTransportOrderHeader> findHeader(TmTransportOrderEntity entity);

    List<TmTransportOrderSkuEntity> findSkuList(TmTransportOrderSkuEntity qEntity);

    List<TmTransportOrderCostEntity> findCostList(TmTransportOrderCostEntity qEntity);

    List<TmTransportOrderLabel> findLabelList(TmTransportOrderLabel qEntity);
    /*-------------------------------list---------------------------------------------*/


    TmTransportOrderEntity getEntity(String id);

    /*查找可揽收标签（状态为新建00）*/
    List<TmTransportOrderLabel> findCanReceiveLabel(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    /*查找可签收标签（状态为已收货10）*/
    List<TmTransportOrderLabel> findCanSignLabel(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    /*查找可回单标签（状态为已签收20）*/
    List<TmTransportOrderLabel> findCanReceiptLabel(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    List<String> findOutletByAreaId(@Param("areaId") String areaId, @Param("baseOrgId") String baseOrgId);

    /*查找未调度标签*/
    List<TmTransportOrderLabelEntity> findCanDispatchLabel(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    /*校验是否曾配载过*/
    List<String> checkHasDispatch(@Param("transportNo") String transportNo, @Param("baseOrgId") String baseOrgId);

    void deleteSkuByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    void deleteDeliveryByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    TmTransportOrderSkuEntity getByNoAndLineNo(@Param("transportNo") String transportNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    List<TmTransportOrderLabelEntity> findDirectLabelPage(TmTransportOrderLabelEntity qEntity);

    /*校验是否已配载未发车*/
    List<String> checkHasDispatchNonDepart(@Param("transportNo") String transportNo, @Param("baseOrgId") String baseOrgId);

    TmTransportOrderLabelEntity getLabelEntity(String id);
}
