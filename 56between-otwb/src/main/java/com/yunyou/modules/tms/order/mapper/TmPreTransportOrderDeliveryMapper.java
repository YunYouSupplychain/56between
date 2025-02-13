package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderDeliveryEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 运输订单配送信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmPreTransportOrderDeliveryMapper extends BaseMapper<TmPreTransportOrderDelivery> {

    TmPreTransportOrderDelivery getByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    TmPreTransportOrderDeliveryEntity getEntityByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);
}