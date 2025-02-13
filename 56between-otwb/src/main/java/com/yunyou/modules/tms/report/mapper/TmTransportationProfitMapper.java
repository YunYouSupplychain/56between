package com.yunyou.modules.tms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.report.entity.TmTransportationProfit;

import java.util.List;

@MyBatisMapper
public interface TmTransportationProfitMapper {

    List<TmTransportationProfit> findList(TmTransportationProfit qEntity);
}
