package com.yunyou.modules.monitor.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 定时任务Entity
 *
 * @author lgf
 * @version 2017-02-04
 */
public class ScheduleJob extends DataEntity<ScheduleJob> {

    private static final long serialVersionUID = 1L;
    private String name;        // 任务名
    private String group;        // 任务组 schedule_task_group
    private String cronExpression;        // 定时规则
    private String status;        // 启用状态 yes_no
    private String isInfo;        // 通知用户 schedule_task_info
    private String className;        // 任务类
    private String description;        // 描述

    public ScheduleJob() {
        super();
    }

    public ScheduleJob(String id) {
        super(id);
    }

    @NotNull(message = "任务名不能为空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "任务组不能为空")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @NotNull(message = "定时规则不能为空")
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @NotNull(message = "启用状态不能为空")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsInfo() {
        return isInfo;
    }

    public void setIsInfo(String isInfo) {
        this.isInfo = isInfo;
    }

    @NotNull(message = "任务类不能为空")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}