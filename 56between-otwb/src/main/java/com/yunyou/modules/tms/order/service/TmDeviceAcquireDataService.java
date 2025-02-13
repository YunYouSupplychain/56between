package com.yunyou.modules.tms.order.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDeviceAcquireData;
import com.yunyou.modules.tms.order.mapper.TmDeviceAcquireDataMapper;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 设备温度数据Service
 *
 * @author liujianhua
 * @version 2022.8.8
 */
@Service
@Transactional(readOnly = true)
public class TmDeviceAcquireDataService extends CrudService<TmDeviceAcquireDataMapper, TmDeviceAcquireData> {

    @Transactional
    public void batchInsert(List<TmDeviceAcquireData> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }

    @Transactional
    public void removeByVehicleNoAndTime(String vehicleNo, String gpsNo, Date startTime, Date endTime, String orgId) {
        mapper.removeByVehicleNoAndTime(vehicleNo, gpsNo, startTime, endTime, orgId);
    }

    public List<TmDeviceAcquireData> getTempByVehicleAndTime(String vehicleNo, Date startTime, Date endTime, String orgId) {
        return mapper.getTempByVehicleAndTime(vehicleNo, startTime, endTime, orgId);
    }

    public Double getAvgTempByVehicleAndTime(String vehicleNo, Date startTime, Date endTime, String orgId) {
        List<TmDeviceAcquireData> list = this.getTempByVehicleAndTime(vehicleNo, startTime, endTime, orgId);
        if (CollectionUtil.isEmpty(list))
            return null;
        Double totalTemp = list.stream().map(TmDeviceAcquireData::getTemperature1).filter(Objects::nonNull).reduce(0D, BigDecimalUtil::add);
        return BigDecimalUtil.div(totalTemp, list.size(), 1, BigDecimal.ROUND_HALF_UP);
    }
}
