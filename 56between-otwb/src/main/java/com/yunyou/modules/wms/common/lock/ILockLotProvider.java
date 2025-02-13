package com.yunyou.modules.wms.common.lock;

/**
 * 获取需要加锁的货物LOT 批次信息
 * @author WMJ
 * @version 2019/10/16
 */
public interface ILockLotProvider {

	/**
	 * 获取需要加锁的LOT 批次信息
	 * @return 需要加锁的货物批次信息
	 */
	LockLotInfo getNeedLockLotInfo();
}
