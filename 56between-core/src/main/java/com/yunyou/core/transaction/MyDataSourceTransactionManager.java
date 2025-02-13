package com.yunyou.core.transaction;

import com.yunyou.core.transaction.lock.AbstractLockInfo;
import com.yunyou.core.transaction.lock.ILockHandler;
import com.yunyou.core.transaction.lock.LockerContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * 通过事务管理器的截面，释放库存操作锁
 * @author WMJ
 * @version 2019/10/17
 */
public class MyDataSourceTransactionManager extends DataSourceTransactionManager {
    @Resource(name = "iLockHandler")
    protected ILockHandler locker = null;

    public MyDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        this.releaseLock();
        super.doCommit(status);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        try {
            this.releaseLock();
        } catch (Throwable ignored) {

        }
        super.doRollback(status);
    }

    /**
     * 释放事务内存放在线程变量中的锁
     */
    protected void releaseLock() {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        Set<AbstractLockInfo> lockLots = LockerContext.getTransactionLockMap().get(transactionName);
        if (lockLots != null && !lockLots.isEmpty()) {
            if (logger.isInfoEnabled()) {
                int size = lockLots.size();
                logger.info("当前事务内锁的LOT集合[" + size + "]：" + Arrays.toString(lockLots.toArray(new AbstractLockInfo[0])));
            }
            for (Iterator<AbstractLockInfo> it = lockLots.iterator(); it.hasNext();) {
                locker.releaseLock(it.next());
                it.remove();
            }
        }
    }
}
