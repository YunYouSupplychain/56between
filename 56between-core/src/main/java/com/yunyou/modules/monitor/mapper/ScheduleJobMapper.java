package com.yunyou.modules.monitor.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.monitor.entity.ScheduleJob;

/**
 * 定时任务MAPPER接口
 * @author lgf
 * @version 2017-02-04
 */
@MyBatisMapper
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {

}