package com.yunyou.modules.tms.order.web;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.gps.GpsUtils;
import com.yunyou.modules.interfaces.gps.GpsNewestLocationInfo;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.order.action.TmDispatchOrderAction;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchVehicleEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运行车辆Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/other/tmRunningVehicle")
public class TmRunningVehicleController extends BaseController {
    @Autowired
    private TmDispatchOrderAction action;

    @RequiresPermissions("tms:other:tmRunningVehicle:list")
    @RequestMapping(value = {"list", ""})
    public String railwayList(Model model) {
        model.addAttribute("ak", SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_CLIENT_AK));
        return "modules/tms/other/tmVehicleMonitoringMapList";
    }

    @ResponseBody
    @RequiresPermissions("tms:other:tmRunningVehicle:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(String orgId) {
        List<TmDispatchVehicleEntity> entities = action.findRunningVehicle(orgId);

        List<GpsNewestLocationInfo> list = Lists.newArrayList();
        Map<String, List<TmDispatchVehicleEntity>> map = entities.stream().collect(Collectors.groupingBy(TmDispatchVehicleEntity::getGpsManufacturer));
        for (Map.Entry<String, List<TmDispatchVehicleEntity>> entry : map.entrySet()) {
            String gpsManufacturer = entry.getKey();
            List<String> vehicleNos = entry.getValue().stream().map(TmDispatchVehicleEntity::getVehicleNo).distinct().collect(Collectors.toList());
            try {
                List<GpsNewestLocationInfo> infos = GpsUtils.getVehicleNewestLocationInfo(gpsManufacturer, vehicleNos);
                if (CollectionUtil.isNotEmpty(infos)) {
                    list.addAll(infos);
                }
            } catch (Exception e) {
                logger.error("车辆监控获取运行车辆GPS最新位置信息失败", e);
            }
        }
        return getBootstrapData(new Page<>(1, list.size(), list.size(), list));
    }
}
