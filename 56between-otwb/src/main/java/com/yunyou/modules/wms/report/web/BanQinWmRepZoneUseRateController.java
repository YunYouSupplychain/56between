package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.BanQinWmRepZoneUseRate;
import com.yunyou.modules.wms.report.service.BanQinWmRepZoneUseRateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/wms/report/wmRepZoneUseRate")
public class BanQinWmRepZoneUseRateController extends BaseController {
    @Autowired
    private BanQinWmRepZoneUseRateService wmRepZoneUseRateService;

    @RequiresPermissions("report:wmRepZoneUseRate:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("wmRepZoneUseRate", new BanQinWmRepZoneUseRate());
        return "modules/wms/report/banQinWmRepZoneUseRateList";
    }

    @ResponseBody
    @RequiresPermissions("report:wmRepZoneUseRate:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmRepZoneUseRate entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmRepZoneUseRateService.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions("report:wmRepZoneUseRate:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(BanQinWmRepZoneUseRate entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinWmRepZoneUseRate> page = wmRepZoneUseRateService.findPage(new Page<>(request, response, -1), entity);
            new ExportExcel("", BanQinWmRepZoneUseRate.class).setDataList(page.getList()).write(response, "仓储使用率.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库位使用率失败！失败信息：" + e.getMessage());
        }
        return j;
    }
}
