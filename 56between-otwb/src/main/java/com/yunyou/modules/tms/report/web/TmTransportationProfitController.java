package com.yunyou.modules.tms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.report.entity.TmTransportationProfit;
import com.yunyou.modules.tms.report.service.TmTransportationProfitService;
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
@RequestMapping(value = "${adminPath}/tms/report/transportationProfit")
public class TmTransportationProfitController extends BaseController {
    @Autowired
    private TmTransportationProfitService tmTransportationProfitService;

    @RequiresPermissions("tms:report:transportationProfit")
    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("tmTransportationProfit", new TmTransportationProfit());
        return "modules/tms/report/tmTransportationProfitList";
    }

    @ResponseBody
    @RequiresPermissions("tms:report:transportationProfit")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmTransportationProfit qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportationProfitService.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:report:transportationProfit:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody TmTransportationProfit qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<TmTransportationProfit> page = tmTransportationProfitService.findPage(new Page<>(request, response, -1), qEntity);
            new ExportExcel(null, TmTransportationProfit.class).setDataList(page.getList()).write(response, "运输利润率报表").dispose();
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
