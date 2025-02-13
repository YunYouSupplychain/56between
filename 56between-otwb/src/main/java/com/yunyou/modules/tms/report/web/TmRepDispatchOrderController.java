package com.yunyou.modules.tms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.report.entity.TmRepDispatchOrder;
import com.yunyou.modules.tms.report.service.TmRepDispatchOrderService;
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
@RequestMapping(value = "${adminPath}/tms/report/dispatchOrder")
public class TmRepDispatchOrderController extends BaseController {
    @Autowired
    private TmRepDispatchOrderService tmRepDispatchOrderService;

    @RequiresPermissions("tms:report:dispatchOrder")
    @RequestMapping(value = "list")
    public String list(Model model) {
        model.addAttribute("tmRepDispatchOrder", new TmRepDispatchOrder());
        return "modules/tms/report/tmRepDispatchOrderList";
    }

    @RequiresPermissions("tms:report:dispatchOrder")
    @RequestMapping(value = "temp/form")
    public String tempForm(TmRepDispatchOrder entity, Model model) {
        model.addAttribute("data", tmRepDispatchOrderService.findTempList(entity));
        return "modules/tms/report/tmRepDispatchOrderTempForm";
    }

    @RequiresPermissions("tms:report:dispatchOrder")
    @RequestMapping(value = "speed/form")
    public String speedForm(TmRepDispatchOrder entity, Model model) {
        model.addAttribute("data", tmRepDispatchOrderService.findSpeedList(entity));
        return "modules/tms/report/tmRepDispatchOrderSpeedForm";
    }

    @ResponseBody
    @RequiresPermissions("tms:report:dispatchOrder")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmRepDispatchOrder qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmRepDispatchOrderService.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:report:dispatchOrder:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody TmRepDispatchOrder qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<TmRepDispatchOrder> page = tmRepDispatchOrderService.findPage(new Page<>(request, response, -1), qEntity);
            new ExportExcel(null, TmRepDispatchOrder.class).setDataList(page.getList()).write(response, "派车单报表").dispose();
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
