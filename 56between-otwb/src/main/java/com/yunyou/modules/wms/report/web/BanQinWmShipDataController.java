package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.report.entity.WmShipDataEntity;
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
 * 发货数据Controller
 *
 * @author WMJ
 * @version 2020-03-24
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmShipData")
public class BanQinWmShipDataController extends BaseController {
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;

    @ModelAttribute
    public WmShipDataEntity get(@RequestParam(required = false) String id) {
        return new WmShipDataEntity();
    }

    @RequiresPermissions("report:banQinWmShipData:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmShipDataList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmShipData:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WmShipDataEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WmShipDataEntity> page = banQinWmSoAllocService.findShipData(new Page<WmShipDataEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmShipData:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WmShipDataEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "发货数据记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<WmShipDataEntity> page = banQinWmSoAllocService.findShipData(new Page<WmShipDataEntity>(request, response, -1), entity);
            new ExportExcel("", WmShipDataEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出发货数据记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}