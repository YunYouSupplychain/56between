package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.WmRepInvDailyByZoneEntity;
import com.yunyou.modules.wms.report.entity.WmRepInvDailyEntity;

import java.util.List;

@MyBatisMapper
public interface BanQinWmRepInvDailyMapper {
    List<WmRepInvDailyEntity> findPage(WmRepInvDailyEntity entity);
    List<WmRepInvDailyByZoneEntity> findPageByZone(WmRepInvDailyByZoneEntity qEntity);
}
