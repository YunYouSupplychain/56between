package com.yunyou.modules.wms.common.lock;

import com.yunyou.core.transaction.lock.ILockHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 库存表更新操作拦截器 备注：用于库存操作加锁，以保证库存一致
 * @author WMJ
 * @version 2019/10/16
 */
@Aspect
@Component
public class InventoryServiceInterceptor {
	@Resource(name = "iLockHandler")
	protected ILockHandler locker = null;

	/**
	 * 获取库存操作锁
	 */
	@Before("execution(* com.yunyou.modules.wms.inventory.service.BanQinInventoryService.*(..))")
	public void before(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof ILockLotProvider) {
				ILockLotProvider param = (ILockLotProvider) arg;
				LockLotInfo lotInfo = param.getNeedLockLotInfo();
				// 校验
				if (lotInfo.getLotNum() == null) {
					continue;
				}
				// 加锁
				locker.lock(lotInfo);
			}
		}
	}
}
