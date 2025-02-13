package com.yunyou.modules.monitor.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.monitor.entity.ScheduleJob;
import com.yunyou.modules.monitor.mapper.ScheduleJobMapper;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 定时任务Service
 *
 * @author lgf
 * @version 2017-02-04
 */
@Service
@Transactional(readOnly = true)
public class ScheduleJobService extends CrudService<ScheduleJobMapper, ScheduleJob> {

    @Autowired
    private Scheduler scheduler;

    public ScheduleJob get(String id) {
        return super.get(id);
    }

    public List<ScheduleJob> findList(ScheduleJob scheduleJob) {
        return super.findList(scheduleJob);
    }

    public Page<ScheduleJob> findPage(Page<ScheduleJob> page, ScheduleJob scheduleJob) {
        return super.findPage(page, scheduleJob);
    }

    @Transactional
    public void save(ScheduleJob scheduleJob) {
        if (!scheduleJob.getIsNewRecord()) {
            ScheduleJob t = this.get(scheduleJob.getId());
            JobKey key = new JobKey(t.getName(), t.getGroup());
            try {
                scheduler.deleteJob(key);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        this.add(scheduleJob);
        super.save(scheduleJob);
    }

    @Transactional
    public void add(ScheduleJob scheduleJob) {
        Class job = null;
        try {
            job = Class.forName(scheduleJob.getClassName());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(scheduleJob.getName(), scheduleJob.getGroup())
            .build();
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

        // 表达式调度构建器（可判断创建SimpleScheduleBuilder）
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(scheduleJob.getName(), scheduleJob.getGroup()).withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            JobKey key = new JobKey(scheduleJob.getName(), scheduleJob.getGroup());
            if (scheduleJob.getStatus().equals("0")) {
                scheduler.pauseJob(key);
            } else {
                scheduler.resumeJob(key);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void delete(ScheduleJob scheduleJob) {
        JobKey key = new JobKey(scheduleJob.getName(), scheduleJob.getGroup());
        try {
            scheduler.deleteJob(key);
            super.delete(scheduleJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有JobDetail
     *
     * @return 结果集合
     */
    public List<JobDetail> getJobs() {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            List<JobDetail> jobDetails = new ArrayList<>();
            for (JobKey key : jobKeys) {
                jobDetails.add(scheduler.getJobDetail(key));
            }
            return jobDetails;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有计划中的任务
     *
     * @return 结果集合
     */
    public List<ScheduleJob> getAllScheduleJob() {
        List<ScheduleJob> scheduleJobList = new ArrayList<>();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    ScheduleJob scheduleJob = new ScheduleJob();
                    scheduleJob.setName(jobKey.getName());
                    scheduleJob.setGroup(jobKey.getGroup());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJob.setStatus(triggerState.name());
                    //获取要执行的定时任务类名
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    scheduleJob.setClassName(jobDetail.getJobClass().getName());
                    //判断trigger
                    if (trigger instanceof SimpleTrigger) {
                        SimpleTrigger simple = (SimpleTrigger) trigger;
                        scheduleJob.setCronExpression("重复次数:" + (simple.getRepeatCount() == -1 ?
                            "无限" : simple.getRepeatCount()) + ",重复间隔:" + (simple.getRepeatInterval() / 1000L));
                        scheduleJob.setDescription(simple.getDescription());
                    }
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cron = (CronTrigger) trigger;
                        scheduleJob.setCronExpression(cron.getCronExpression());
                        scheduleJob.setDescription(cron.getDescription() == null ? ("触发器:" + trigger.getKey()) : cron.getDescription());
                    }
                    scheduleJobList.add(scheduleJob);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleJobList;
    }

    /**
     * 获取所有运行中的任务
     *
     * @return 结果集合
     */
    public List<ScheduleJob> getAllRuningScheduleJob() {
        List<ScheduleJob> scheduleJobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            scheduleJobList = new ArrayList<ScheduleJob>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                ScheduleJob scheduleJob = new ScheduleJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                scheduleJob.setName(jobKey.getName());
                scheduleJob.setGroup(jobKey.getGroup());
                //scheduleJob.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                scheduleJob.setStatus(triggerState.name());
                //获取要执行的定时任务类名
                scheduleJob.setClassName(jobDetail.getJobClass().getName());
                //判断trigger
                if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simple = (SimpleTrigger) trigger;
                    scheduleJob.setCronExpression("重复次数:" + (simple.getRepeatCount() == -1 ?
                        "无限" : simple.getRepeatCount()) + ",重复间隔:" + (simple.getRepeatInterval() / 1000L));
                    scheduleJob.setDescription(simple.getDescription());
                }
                if (trigger instanceof CronTrigger) {
                    CronTrigger cron = (CronTrigger) trigger;
                    scheduleJob.setCronExpression(cron.getCronExpression());
                    scheduleJob.setDescription(cron.getDescription());
                }
                scheduleJobList.add(scheduleJob);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduleJobList;
    }

    /**
     * 获取所有的触发器
     *
     * @return 结果集合
     */
    public List<ScheduleJob> getTriggersInfo() {
        try {
            GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();
            Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher);
            List<ScheduleJob> triggers = new ArrayList<>();

            for (TriggerKey key : Keys) {
                Trigger trigger = scheduler.getTrigger(key);
                ScheduleJob scheduleJob = new ScheduleJob();
                scheduleJob.setName(trigger.getJobKey().getName());
                scheduleJob.setGroup(trigger.getJobKey().getGroup());
                scheduleJob.setStatus(scheduler.getTriggerState(key) + "");
                if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simple = (SimpleTrigger) trigger;
                    scheduleJob.setCronExpression("重复次数:" + (simple.getRepeatCount() == -1 ?
                        "无限" : simple.getRepeatCount()) + ",重复间隔:" + (simple.getRepeatInterval() / 1000L));
                    scheduleJob.setDescription(simple.getDescription());
                }
                if (trigger instanceof CronTrigger) {
                    CronTrigger cron = (CronTrigger) trigger;
                    scheduleJob.setCronExpression(cron.getCronExpression());
                    scheduleJob.setDescription(cron.getDescription());
                }
                triggers.add(scheduleJob);
            }
            return triggers;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 暂停任务
     */
    @Transactional
    public void stopJob(ScheduleJob scheduleJob) {
        JobKey key = new JobKey(scheduleJob.getName(), scheduleJob.getGroup());
        try {
            scheduler.pauseJob(key);
            scheduleJob.setStatus("0");
            super.save(scheduleJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复任务
     */
    @Transactional
    public void restartJob(ScheduleJob scheduleJob) {
        JobKey key = new JobKey(scheduleJob.getName(), scheduleJob.getGroup());
        try {
            scheduler.resumeJob(key);
            scheduleJob.setStatus("1");
            super.save(scheduleJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 立马执行一次任务
     */
    @Transactional
    public void startNowJob(ScheduleJob scheduleJob) {
        JobKey key = new JobKey(scheduleJob.getName(), scheduleJob.getGroup());
        try {
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改触发器时间
     *
     * @param name  任务名
     * @param group 任务组
     * @param cron  cron表达式
     */
    @Transactional
    public void modifyTrigger(String name, String group, String cron) {
        try {
            TriggerKey key = TriggerKey.triggerKey(name, group);

            CronTrigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(key)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
            scheduler.rescheduleJob(key, newTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停调度器
     */
    @Transactional
    public void stopScheduler() {
        try {
            if (!scheduler.isInStandbyMode()) {
                scheduler.standby();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}