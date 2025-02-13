package com.yunyou.modules.oms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.oms.report.entity.OmDelayOrderEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 供应链订单卡单Controller
 *
 * @author WMJ
 * @version 2020-03-24
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/report/omDelayOrder")
public class OmDelayOrderController extends BaseController {
    @Autowired
    private OmChainHeaderService omChainHeaderService;

    @ModelAttribute
    public OmDelayOrderEntity get(@RequestParam(required = false) String id) {
        return new OmDelayOrderEntity();
    }

    /**
     * 供应链订单列表页面
     */
    @RequiresPermissions("report:omDelayOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/report/omDelayOrderList";
    }

    /**
     * 供应链订单列表数据
     */
    @ResponseBody
    @RequiresPermissions("report:omDelayOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmDelayOrderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omChainHeaderService.findDelayOrder(new Page<OmDelayOrderEntity>(request, response), entity));
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:omDelayOrder:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmDelayOrderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "供应链订单卡单.xlsx";
            Page<OmDelayOrderEntity> page = omChainHeaderService.findDelayOrder(new Page<OmDelayOrderEntity>(request, response, -1), entity);
            new ExportExcel("", OmDelayOrderEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出供应链订单卡单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}