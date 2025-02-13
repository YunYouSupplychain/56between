package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderHeader;
import org.apache.ibatis.annotations.Param;

/**
 * 运输订单基本信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmPreTransportOrderHeaderMapper extends BaseMapper<TmPreTransportOrderHeader> {

    TmPreTransportOrderHeader getByNo(@Param("transportNo") String transportNo);
}