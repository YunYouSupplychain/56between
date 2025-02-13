package com.yunyou.modules.tms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.report.entity.TmGasOilStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmGasOilStatisticsMapper {

    List<TmGasOilStatistics> getGas(TmGasOilStatistics qEntity);

    List<TmGasOilStatistics> getGasOil(@Param("gasCode") String gasCode, @Param("orgId") String orgId);

    List<TmGasOilStatistics> getGasOilDailyReceivedQty(TmGasOilStatistics qEntity);

    List<TmGasOilStatistics> getGasOilMonthReceivedQty(TmGasOilStatistics qEntity);

    List<TmGasOilStatistics> getGasOilYearReceivedQty(TmGasOilStatistics qEntity);
}
