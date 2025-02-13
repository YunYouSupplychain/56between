package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmBusinessOrderTypeRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 业务类型-订单类型关联关系MAPPER接口
 *
 * @author zyf
 * @version 2019-07-03
 */
@MyBatisMapper
public interface OmBusinessOrderTypeRelationMapper extends BaseMapper<OmBusinessOrderTypeRelation> {

    void remove(@Param("businessOrderType") String businessOrderType, @Param("taskType") String taskType, @Param("pushSystem") String pushSystem, @Param("orgId") String orgId);
}