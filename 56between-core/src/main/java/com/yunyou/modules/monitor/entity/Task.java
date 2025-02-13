package com.yunyou.modules.monitor.entity;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.time.DateFormatUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 定时任务工作类
 *
 * @author liujianhua
 */
@DisallowConcurrentExecution
public abstract class Task implements Job {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        String startTime = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_CN, new Date());
        logger.info("定时任务 = [" + scheduleJob.getName() + "]" + " 在 " + startTime + " 时开始运行");
        try {
            SecurityUtils.getSecurityManager();
        } catch (UnavailableSecurityManagerException e) {
            // 将SecurityManager绑定给SecurityUtils
            SecurityUtils.setSecurityManager(SpringContextHolder.getBean(SecurityManager.class));
        }
        try {
            run();
            logger.info("定时任务 = [" + scheduleJob.getName() + "]" + " 在 " + DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_CN, new Date()) + " 时结束运行");
        } catch (Throwable e) {
            logger.error("定时任务 = [" + scheduleJob.getName() + "]运行异常", e);
        }
    }

    public abstract void run() throws InterruptedException;
}
