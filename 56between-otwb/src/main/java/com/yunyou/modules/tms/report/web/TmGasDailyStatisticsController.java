package com.yunyou.modules.tms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.report.entity.TmGasDailyStatistics;
import com.yunyou.modules.tms.report.service.TmGasDailyStatisticsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/report/gasStationDailyStatistics")
public class TmGasDailyStatisticsController extends BaseController {
    @Autowired
    private TmGasDailyStatisticsService tmGasDailyStatisticsService;

    @RequiresPermissions("tms:report:gasStationDailyStatistics")
    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("tmGasDailyStatistics", new TmGasDailyStatistics());
        return "modules/tms/report/tmGasDailyStatisticsList";
    }

    @ResponseBody
    @RequiresPermissions("tms:report:gasStationDailyStatistics")
    @RequestMapping(value = "/data")
    public Map<String, Object> data(TmGasDailyStatistics qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmGasDailyStatisticsService.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:report:gasStationDailyStatistics:export")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody TmGasDailyStatistics qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<TmGasDailyStatistics> page = tmGasDailyStatisticsService.findPage(new Page<>(request, response, -1), qEntity);
            new ExportExcel(null, TmGasDailyStatistics.class).setDataList(page.getList()).write(response, "加油站每日统计表").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出失败！失败信息：" + e.getMessage());
        }
        return j;
    }
}
