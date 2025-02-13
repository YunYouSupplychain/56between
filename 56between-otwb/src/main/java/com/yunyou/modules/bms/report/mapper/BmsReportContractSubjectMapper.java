package com.yunyou.modules.bms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.report.entity.BmsReportContractSubjectEntity;
import com.yunyou.modules.bms.report.entity.BmsReportContractSubjectQuery;

import java.util.List;

@MyBatisMapper
public interface BmsReportContractSubjectMapper extends BaseMapper<BmsReportContractSubjectEntity> {

    List<BmsReportContractSubjectEntity> findList(BmsReportContractSubjectQuery query);

    List<BmsReportContractSubjectEntity> findPage(BmsReportContractSubjectQuery query);
}
