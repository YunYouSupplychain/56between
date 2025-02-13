package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderDeliveryEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 运输订单配送信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmTransportOrderDeliveryMapper extends BaseMapper<TmTransportOrderDelivery> {

    TmTransportOrderDelivery getByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    TmTransportOrderDeliveryEntity getEntityByNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);
}