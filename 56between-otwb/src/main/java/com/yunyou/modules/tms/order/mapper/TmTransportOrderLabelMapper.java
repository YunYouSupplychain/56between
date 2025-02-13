package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import org.apache.ibatis.annotations.Param;

/**
 * 运输订单标签信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmTransportOrderLabelMapper extends BaseMapper<TmTransportOrderLabel> {

    String getMaxNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    TmTransportOrderLabel getByTransportNoAndLabelNo(@Param("transportNo") String transportNo, @Param("labelNo") String labelNo, @Param("orgId") String orgId);
}