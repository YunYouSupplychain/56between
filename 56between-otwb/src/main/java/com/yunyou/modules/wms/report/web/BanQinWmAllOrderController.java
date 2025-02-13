package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.WmAllOrderEntity;
import com.yunyou.modules.wms.report.service.BanQinWmAllOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 供应链全部订单数据Controller
 *
 * @author WMJ
 * @version 2020-03-24
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmAllOrder")
public class BanQinWmAllOrderController extends BaseController {
    @Autowired
    private BanQinWmAllOrderService banQinWmAllOrderService;

    @ModelAttribute
    public WmAllOrderEntity get(@RequestParam(required = false) String id) {
        return new WmAllOrderEntity();
    }

    @RequiresPermissions("report:banQinWmAllOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmAllOrderList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmAllOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WmAllOrderEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WmAllOrderEntity> page = banQinWmAllOrderService.findPage(new Page<WmAllOrderEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmAllOrder:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WmAllOrderEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "供应链全部订单数据记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<WmAllOrderEntity> page = banQinWmAllOrderService.findPage(new Page<WmAllOrderEntity>(request, response, -1), entity);
            new ExportExcel("", WmAllOrderEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出供应链全部订单数据记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}