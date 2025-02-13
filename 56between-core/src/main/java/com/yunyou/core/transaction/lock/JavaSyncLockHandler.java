package com.yunyou.core.transaction.lock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 操作同步锁接口
 *
 * @version 2019/10/16
 * @author WMJ
 */
@Service("iLockHandler")
public class JavaSyncLockHandler implements ILockHandler {
    protected Map<String, Lock> lockMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 获取锁 备注：该方法是同步的，如果未获取锁，线程则等待，直至获取锁再继续执行
     * 线程等待有设置最大等待时间，如果最大等待时间已过还未获取锁，则强制唤醒继续
     *
     * @param lotInfo 需要加锁的信息
     */
    @Override
    public void lock(AbstractLockInfo lotInfo) {
        // 当前事务名称
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        if (transactionName == null) {
            if (TransactionSynchronizationManager.isActualTransactionActive() || TransactionSynchronizationManager.isSynchronizationActive()) {
                // 暂未解决通过new NewTransactionTemplate().execute创建的事务锁问题。现采取该情形放行不锁
                return;
            } else {
                throw new RuntimeException("操作悲观锁的lock方法，必须在事务内部调用。");
            }
        }
        // 如果线程中已存在该批次，则代表是嵌套调用
        Set<AbstractLockInfo> lockLots = LockerContext.getTransactionLockMap().get(transactionName);
        if (lockLots != null && lockLots.contains(lotInfo)) {
            return;
        }

        String key = lotInfo.toString();
        Lock lock;
        boolean isCurThreadHasLock = false;
        // 同步锁
        synchronized (lockMap) {
            lock = lockMap.get(key);
            // 为空说明该货物无人操作，则往lockMap中放入锁，当前线程获得继续操作的锁权限
            if (lock == null) {
                lock = new Lock();
                lockMap.put(key, lock);
                isCurThreadHasLock = true;
            }
            // 注意需放置在同步块中，对应解锁时获取RefNum也是在lockMap的同步块中
            lock.addRefNumber();
        }
        // 如果批次号已正在使用，那么等待
        if (!isCurThreadHasLock) {
            /*系统控制参数：WM_LOCK_MAX_WAIT(库存操作锁最大等待时间，单位：秒)
            1、该参数设置需为正整数
            2、不设置该参数，或设置值不合法，系统将采用默认值20秒*/
            String maxWaitSecondsStr = "20";
            long maxWaitSeconds = 20;
            // 如果不配置该系统控制参数，则默认20秒
            if (maxWaitSecondsStr.matches("\\d{1,7}")) {
                maxWaitSeconds = Long.parseLong(maxWaitSecondsStr);
            }
            try {
                // 同步锁，队列等待
                synchronized (lock) {
                    lock.wait(maxWaitSeconds * 1000L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 当前线程中已取得锁的信息
        LockerContext.addTransactionLock(transactionName, lotInfo);
    }

    /**
     * 释放锁
     *
     * @param lotInfo 需要释放锁的信息
     */
    @Override
    public void releaseLock(AbstractLockInfo lotInfo) {
        String key = lotInfo.toString();
        // 同步锁
        synchronized (lockMap) {
            Lock lock = lockMap.get(key);
            if (lock == null) {
                throw new RuntimeException("系统异常，错误信息：操作结束释放同步锁时，同步锁对象为空。");
            }
            lock.minusRefNumber();
            if (!lock.hashRef()) {
                lockMap.remove(key);
            } else {
                // 释放锁
                synchronized (lock) {
                    boolean success = false;
                    while (!success) {
                        // 唤醒一个线程
                        lock.notify();
                        success = true;
                    }
                }
            }
        }
    }

    /**
     * 锁对象，记录了当前请求该锁的线程个数
     */
    protected class Lock {
        protected int refNum = 0; // 当前请求该锁的线程个数

        public void addRefNumber() {
            this.refNum++;
        }

        public void minusRefNumber() {
            this.refNum--;
        }

        public boolean hashRef() {
            return this.refNum >= 1;
        }
    }
}
