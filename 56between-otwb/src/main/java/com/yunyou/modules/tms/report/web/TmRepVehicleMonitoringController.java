package com.yunyou.modules.tms.report.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.map.MapConstants;
import com.yunyou.modules.tms.report.entity.TmRepVehicleMonitoring;
import com.yunyou.modules.tms.report.service.TmRepVehicleMonitoringService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/report/vehicleMonitoring")
public class TmRepVehicleMonitoringController extends BaseController {
    @Autowired
    private TmRepVehicleMonitoringService tmRepVehicleMonitoringService;

    @RequestMapping("list")
    @RequiresPermissions("tms:report:vehicleMonitoring")
    public String list(Model model) {
        model.addAttribute("ak", SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_CLIENT_AK));
        if (MapConstants.B_MAP.equals(MapConstants.USE_MAP)) {
            return "modules/tms/report/tmRepVehicleMonitoringBMapList";
        } else {
            return "modules/tms/report/tmRepVehicleMonitoringGMapList";
        }
    }

    @ResponseBody
    @RequiresPermissions("tms:report:vehicleMonitoring")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmRepVehicleMonitoring entity) {
        return getBootstrapData(tmRepVehicleMonitoringService.findPage(new Page<>(), entity));
    }

}
