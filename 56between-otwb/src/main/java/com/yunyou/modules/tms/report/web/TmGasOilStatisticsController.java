package com.yunyou.modules.tms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.report.entity.TmGasOilStatistics;
import com.yunyou.modules.tms.report.service.TmGasOilStatisticsService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/report/gasOilStatistics")
public class TmGasOilStatisticsController extends BaseController {
    @Autowired
    private TmGasOilStatisticsService tmGasOilStatisticsService;

    @RequiresPermissions("tms:report:gasOilStatistics")
    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("tmGasOilStatistics", new TmGasOilStatistics());
        return "modules/tms/report/tmGasOilStatisticsList";
    }

    @ResponseBody
    @RequiresPermissions("tms:report:gasOilStatistics")
    @RequestMapping(value = "/data")
    public List<TmGasOilStatistics> data(TmGasOilStatistics qEntity) {
        return tmGasOilStatisticsService.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions("tms:report:gasOilStatistics:export")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody TmGasOilStatistics qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            List<TmGasOilStatistics> list = tmGasOilStatisticsService.findList(qEntity);
            new ExportExcel(null, TmGasOilStatistics.class).setDataList(list).write(response, "加油站卸油统计表").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "/getGas")
    public Map<String, Object> getGas(TmGasOilStatistics qEntity, HttpServletRequest request, HttpServletResponse response){
        return getBootstrapData(tmGasOilStatisticsService.getGas(new Page<>(request, response), qEntity));
    }
}
