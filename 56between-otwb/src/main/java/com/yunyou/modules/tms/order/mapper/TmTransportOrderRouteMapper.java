package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import org.apache.ibatis.annotations.Param;

/**
 * 运输订单路由信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmTransportOrderRouteMapper extends BaseMapper<TmTransportOrderRoute> {

    TmTransportOrderRoute getByTransportNoAndLabel(@Param("transportNo") String transportNo, @Param("labelNo") String labelNo, @Param("baseOrgId") String baseOrgId);
}