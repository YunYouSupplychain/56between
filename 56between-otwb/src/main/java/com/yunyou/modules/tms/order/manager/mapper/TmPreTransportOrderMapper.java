package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.extend.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmPreTransportOrderMapper extends BaseMapper<TmPreTransportOrderHeader> {

    List<TmPreTransportOrderEntity> findOrderPage(TmPreTransportOrderEntity entity);

    List<TmPreTransportOrderSkuEntity> findSkuPage(TmPreTransportOrderSkuEntity qEntity);

    List<TmPreTransportOrderCostEntity> findCostPage(TmPreTransportOrderCostEntity qEntity);

    /*-------------------------------page---------------------------------------------*/

    List<TmPreTransportOrderHeader> findHeader(TmPreTransportOrderEntity entity);

    List<TmPreTransportOrderSkuEntity> findSkuList(TmPreTransportOrderSkuEntity qEntity);

    List<TmPreTransportOrderCostEntity> findCostList(TmPreTransportOrderCostEntity qEntity);
    /*-------------------------------list---------------------------------------------*/

    TmPreTransportOrderEntity getEntity(String id);

    List<String> findOutletByAreaId(@Param("areaId") String areaId, @Param("baseOrgId") String baseOrgId);

    void deleteSkuByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    void deleteDeliveryByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);
}
