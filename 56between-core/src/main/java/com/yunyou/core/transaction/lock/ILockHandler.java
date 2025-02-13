package com.yunyou.core.transaction.lock;

/**
 * 同步锁接口 -- Java对象同步锁的实现
 * @author WMJ
 * @version 2019/10/16
 */
public interface ILockHandler {
	/**
	 * 获取锁 备注：该方法是同步的，如果未获取锁，线程则等待，直至获取锁再继续执行
	 * 线程等待有设置最大等待时间，如果最大等待时间已过还未获取锁，则强制唤醒继续
	 * @param lotInfo 需要加锁的信息
	 */
	void lock(AbstractLockInfo lotInfo);

	/**
	 * 释放锁
	 * @param lotInfo 需要加锁的信息
	 */
	void releaseLock(AbstractLockInfo lotInfo);
}
