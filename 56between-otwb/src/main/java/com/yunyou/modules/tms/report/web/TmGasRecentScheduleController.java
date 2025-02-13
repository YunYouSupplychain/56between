package com.yunyou.modules.tms.report.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.report.entity.TmGasRecentSchedule;
import com.yunyou.modules.tms.report.service.TmGasRecentScheduleService;

@Controller
@RequestMapping(value = "${adminPath}/tms/report/gasRecentSchedule")
public class TmGasRecentScheduleController extends BaseController {
    @Autowired
    private TmGasRecentScheduleService tmGasRecentScheduleService;

    @RequiresPermissions("tms:report:gasRecentSchedule")
    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("tmGasRecentSchedule", new TmGasRecentSchedule());
        return "modules/tms/report/tmGasRecentScheduleList";
    }

    @ResponseBody
    @RequiresPermissions("tms:report:gasRecentSchedule")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmGasRecentSchedule qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmGasRecentScheduleService.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:report:gasRecentSchedule:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody TmGasRecentSchedule qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<TmGasRecentSchedule> page = tmGasRecentScheduleService.findPage(new Page<>(request, response, -1), qEntity);
            new ExportExcel(null, TmGasRecentSchedule.class).setDataList(page.getList()).write(response, "加油站最近计划表").dispose();
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
