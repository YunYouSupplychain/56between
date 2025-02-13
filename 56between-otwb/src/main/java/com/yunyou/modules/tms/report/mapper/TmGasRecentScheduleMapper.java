package com.yunyou.modules.tms.report.mapper;

import java.util.List;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.report.entity.TmGasRecentSchedule;
import com.yunyou.modules.tms.report.entity.TmGetActualQty;

@MyBatisMapper
public interface TmGasRecentScheduleMapper {

    List<TmGasRecentSchedule> findList(TmGasRecentSchedule qEntity);

    List<Double> getActualQty(TmGetActualQty query);
}
