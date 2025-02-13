package com.yunyou.core.transaction.lock;

import java.util.*;

/**
 * @author WMJ
 * @version 2019/10/16
 */
public class LockerContext {
	protected static Object object = new Object();
	protected static ThreadLocal<Map<Object, Set<AbstractLockInfo>>> transactionLockInfo = new ThreadLocal<>();

	/**
	 * 当前线程中已取得的锁信息
	 */
	public static void addTransactionLock(Object transactionId, AbstractLockInfo lotInfo) {
		Map<Object, Set<AbstractLockInfo>> transactionLockLotsMap = getTransactionLockMap();
		Set<AbstractLockInfo> lots = transactionLockLotsMap.computeIfAbsent(transactionId, k -> Collections.synchronizedSet(new HashSet<>()));
		if (!lots.contains(lotInfo)) {
			lots.add(lotInfo);
		}
	}

	/**
	 * 锁事务队列Map
	 */
	public static Map<Object, Set<AbstractLockInfo>> getTransactionLockMap() {
		Map<Object, Set<AbstractLockInfo>> transactionLockLotsMap = transactionLockInfo.get();
		if (transactionLockLotsMap == null) {
			synchronized (object) {
				transactionLockLotsMap = Collections.synchronizedMap(new HashMap<>());
				transactionLockInfo.set(transactionLockLotsMap);
			}
		}
		return transactionLockLotsMap;
	}
}
