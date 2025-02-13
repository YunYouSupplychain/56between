package com.yunyou.modules.interfaces.gps;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.gps.e6.util.E6Util;
import com.yunyou.modules.interfaces.gps.g7.util.G7Util;
import com.yunyou.modules.tms.common.GpsManufacturer;

import java.util.Date;
import java.util.List;

/**
 * GPS工具类
 *
 * @author yunyou
 * @since 2023/6/28 10:43
 */
public class GpsUtils {

    public static List<GpsRunTrackInfo> getVehicleTrackInfo(String manufacturer, String vehicleNo, Date from, Date to) throws Exception {
        List<GpsRunTrackInfo> records;

        switch (GpsManufacturer.valueOf(manufacturer)) {
            case G7:
                records = G7Util.getRunTrackInfoByVehicleNo(vehicleNo, from, to);
                break;
            case E6:
                records = E6Util.getRunTrackInfoByVehicleNo(vehicleNo, from, to);
                break;
            default:
                records = Lists.newArrayList();
                break;
        }
        return records;
    }

    public static List<GpsNewestLocationInfo> getVehicleNewestLocationInfo(String manufacturer, List<String> vehicleNos) throws Exception {
        List<GpsNewestLocationInfo> records = Lists.newArrayList();

        for (int i = 0; i < vehicleNos.size(); i += 50) {
            List<GpsNewestLocationInfo> infos;
            List<String> vnos;
            if (vehicleNos.size() - i < 999) {
                vnos = vehicleNos.subList(i, vehicleNos.size());
            } else {
                vnos = vehicleNos.subList(i, i + 50);
            }
            switch (GpsManufacturer.valueOf(manufacturer)) {
                case G7:
                    infos = G7Util.getNewestLocationInfoByVehicleNo(vnos);
                    break;
                case E6:
                    infos = E6Util.getNewestLocationInfoByVehicleNo(vnos);
                    break;
                default:
                    infos = Lists.newArrayList();
                    break;
            }
            if (CollectionUtil.isNotEmpty(infos)) {
                records.addAll(infos);
            }
        }
        return records;
    }
}
