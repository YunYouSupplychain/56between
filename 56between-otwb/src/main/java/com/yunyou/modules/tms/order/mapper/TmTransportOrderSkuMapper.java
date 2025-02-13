package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运输订单明细信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmTransportOrderSkuMapper extends BaseMapper<TmTransportOrderSku> {

    void saveAll(List<TmTransportOrderSku> list);

    Integer getMaxLineNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);
}