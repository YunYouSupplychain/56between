package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyEntity;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyQuery;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyReport;

import java.util.List;

@MyBatisMapper
public interface BanQinWmRepWorkEfficiencyMapper extends BaseMapper<WmRepWorkEfficiencyReport> {

    List<WmRepWorkEfficiencyEntity> findRcvList(WmRepWorkEfficiencyQuery query);

    List<WmRepWorkEfficiencyEntity> findPkList(WmRepWorkEfficiencyQuery query);

    List<WmRepWorkEfficiencyEntity> findReviewList(WmRepWorkEfficiencyQuery query);
}
