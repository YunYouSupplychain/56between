package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmCarrierRouteRelation;
import com.yunyou.modules.tms.basic.entity.extend.TmCarrierRouteRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 承运商路由信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmCarrierRouteRelationMapper extends BaseMapper<TmCarrierRouteRelation> {

    TmCarrierRouteRelationEntity getEntity(String id);

    List<TmCarrierRouteRelationEntity> findPage(TmCarrierRouteRelation tmCarrierRouteRelation);

    List<TmCarrierRouteRelationEntity> findGrid(TmCarrierRouteRelationEntity entity);

    TmCarrierRouteRelation getByCode(@Param("carrierCode") String carrierCode, @Param("code") String code, @Param("orgId") String orgId);

    void remove(@Param("carrierCode") String carrierCode, @Param("code") String code, @Param("orgId") String orgId);
}