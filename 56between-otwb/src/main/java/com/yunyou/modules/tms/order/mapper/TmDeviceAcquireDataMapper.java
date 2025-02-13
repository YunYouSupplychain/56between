package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDeviceAcquireData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@MyBatisMapper
public interface TmDeviceAcquireDataMapper extends BaseMapper<TmDeviceAcquireData> {

    void batchInsert(@Param("items") List<TmDeviceAcquireData> items);

    void removeByVehicleNoAndTime(@Param("vehicleNo") String vehicleNo, @Param("gpsNo") String gpsNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orgId") String orgId);

    List<TmDeviceAcquireData> getTempByVehicleAndTime(@Param("vehicleNo") String vehicleNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orgId") String orgId);
}
