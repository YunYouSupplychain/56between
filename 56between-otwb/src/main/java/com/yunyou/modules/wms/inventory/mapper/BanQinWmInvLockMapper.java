package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLock;

/**
 * 库存操作悲观锁记录表MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmInvLockMapper extends BaseMapper<BanQinWmInvLock> {
	
}