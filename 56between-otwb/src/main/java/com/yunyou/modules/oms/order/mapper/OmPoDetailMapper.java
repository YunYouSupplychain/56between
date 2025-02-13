package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmPoDetail;

import java.util.List;

/**
 * 采购订单明细表MAPPER接口
 * @author WMJ
 * @version 2019-04-16
 */
@MyBatisMapper
public interface OmPoDetailMapper extends BaseMapper<OmPoDetail> {

    List<OmPoDetail> findDetailList(OmPoDetail entity);

}