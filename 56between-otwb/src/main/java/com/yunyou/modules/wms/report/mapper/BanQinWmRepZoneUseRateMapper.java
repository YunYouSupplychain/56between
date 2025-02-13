package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.BanQinWmRepZoneUseRate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface BanQinWmRepZoneUseRateMapper extends BaseMapper<BanQinWmRepZoneUseRate> {

    List<BanQinWmRepZoneUseRate> findPage(BanQinWmRepZoneUseRate entity);

    Integer getUseQty(@Param("zoneCode") String zoneCode, @Param("orgId") String orgId);
}
