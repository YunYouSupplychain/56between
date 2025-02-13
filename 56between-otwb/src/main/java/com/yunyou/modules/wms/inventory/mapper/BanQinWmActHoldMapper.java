package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActHold;

import java.util.List;

/**
 * 冻结记录MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmActHoldMapper extends BaseMapper<BanQinWmActHold> {
	List<BanQinWmActHold> findPage(BanQinWmActHold banQinWmActHold);
}