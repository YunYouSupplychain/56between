package com.yunyou.modules.tms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.report.entity.TmGasDailyStatistics;

import java.util.List;

@MyBatisMapper
public interface TmGasDailyStatisticsMapper extends BaseMapper<TmGasDailyStatistics> {

    List<TmGasDailyStatistics> findPage(TmGasDailyStatistics qEntity);
}
