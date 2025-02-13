package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;

import java.util.List;

/**
 * 批次库存表MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmInvLotMapper extends BaseMapper<BanQinWmInvLot> {
    
	List<String> checkSkuIsToQcQuery(BanQinWmInvLot banQinWmInvLot);
}