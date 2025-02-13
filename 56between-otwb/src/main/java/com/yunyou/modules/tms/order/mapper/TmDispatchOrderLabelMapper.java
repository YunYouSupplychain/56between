package com.yunyou.modules.tms.order.mapper;

import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderLabel;

/**
 * 派车单标签MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@MyBatisMapper
public interface TmDispatchOrderLabelMapper extends BaseMapper<TmDispatchOrderLabel> {

    TmDispatchOrderLabel getByNoAndLabelAndRS(@Param("dispatchNo") String dispatchNo, @Param("labelNo") String labelNo, @Param("receiveShip") String receiveShip, @Param("orgId") String orgId);

    TmDispatchOrderLabel getByNoAndOutletAndLabel(@Param("dispatchNo") String dispatchNo, @Param("dispatchOutletCode") String dispatchOutletCode, @Param("labelNo") String labelNo, @Param("orgId") String orgId);

    boolean existTransportLabel(@Param("transportNo") String transportNo, @Param("labelNo") String labelNo, @Param("baseOrgId") String baseOrgId);
}