package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;
import com.yunyou.modules.bms.basic.entity.extend.BmsCarrierRouteEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 承运商路由MAPPER接口
 *
 * @author zqs
 * @version 2018-06-15
 */
@MyBatisMapper
public interface BmsCarrierRouteMapper extends BaseMapper<BmsCarrierRoute> {

    List<BmsCarrierRoute> findPage(BmsCarrierRouteEntity entity);

    BmsCarrierRouteEntity getEntity(String id);

    BmsCarrierRoute getByCode(@Param("carrierCode") String carrierCode, @Param("routeCode") String routeCode, @Param("orgId") String orgId);

    BmsCarrierRoute getByStartAndEndAreaId(@Param("carrierCode") String carrierCode, @Param("startAreaId") String startAreaId, @Param("endAreaId") String endAreaId, @Param("orgId") String orgId);

    void remove(@Param("carrierCode") String carrierCode, @Param("routeCode") String routeCode, @Param("orgId") String orgId);

    List<BmsCarrierRoute> findGrid(BmsCarrierRouteEntity entity);
}