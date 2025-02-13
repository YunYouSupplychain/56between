package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运输订单基本信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@MyBatisMapper
public interface TmTransportOrderHeaderMapper extends BaseMapper<TmTransportOrderHeader> {

    TmTransportOrderHeader getByNo(@Param("transportNo") String transportNo);

    TmTransportOrderHeader getByCustomerNo(@Param("customerNo") String customerNo, @Param("orgId") String orgId);

    Integer findUnSignCount();

    List<TmTransportOrderHeader> findUnSignMailNo(@Param("offset") int offset, @Param("num") int num);
}