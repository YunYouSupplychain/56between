package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmOrderAppendix;
import org.apache.ibatis.annotations.Param;

/**
 * 订单补充数据MAPPER接口
 * @author zyf
 * @version 2021-05-28
 */
@MyBatisMapper
public interface OmOrderAppendixMapper extends BaseMapper<OmOrderAppendix> {

    void deleteByOrderNo(@Param("orderNo")String orderNo, @Param("orgId")String orgId);

}