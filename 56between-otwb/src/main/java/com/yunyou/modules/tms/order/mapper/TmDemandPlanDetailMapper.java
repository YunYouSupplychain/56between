package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDemandPlanDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 需求计划明细MAPPER接口
 * @author WMJ
 * @version 2020-07-07
 */
@MyBatisMapper
public interface TmDemandPlanDetailMapper extends BaseMapper<TmDemandPlanDetail> {

    void deleteDetail(@Param("planOrderNo")String planOrderNo, @Param("orgId")String orgId, @Param("skuCode")String skuCode);

}