package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAttEntity;

import java.util.List;

/**
 * 批次号库存表MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmInvLotAttMapper extends BaseMapper<BanQinWmInvLotAtt> {
	List<BanQinWmInvLotAtt> findByAllLot(BanQinWmInvLotAtt banQinWmInvLotAtt);
	List<BanQinWmInvLotAttEntity> findPage(BanQinWmInvLotAttEntity entity);
}