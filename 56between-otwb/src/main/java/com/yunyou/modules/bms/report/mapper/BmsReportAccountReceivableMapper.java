package com.yunyou.modules.bms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.report.entity.BmsReportAccountReceivableEntity;
import com.yunyou.modules.bms.report.entity.BmsReportAccountReceivableQuery;

import java.util.List;

@MyBatisMapper
public interface BmsReportAccountReceivableMapper extends BaseMapper<BmsReportAccountReceivableEntity> {

    List<BmsReportAccountReceivableEntity> findPage(BmsReportAccountReceivableQuery query);
}
