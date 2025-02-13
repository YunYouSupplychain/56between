package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.WmRepInvDailyByZoneEntity;
import com.yunyou.modules.wms.report.entity.WmRepInvDailyEntity;
import com.yunyou.modules.wms.report.service.BanQinWmRepInvDailyService;
import com.google.common.collect.Maps;
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
 * 库存报表Controller
 *
 * @author WMJ
 * @version 2020-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmRepInvDaily")
public class BanQinWmRepInvDailyController extends BaseController {
    @Autowired
    private BanQinWmRepInvDailyService wmRepInvDailyService;

    @ModelAttribute
    public WmRepInvDailyEntity get(@RequestParam(required = false) String id) {
        return new WmRepInvDailyEntity();
    }

    @RequiresPermissions("report:banQinWmRepInvDaily:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmRepInvDailyList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmRepInvDaily:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WmRepInvDailyEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WmRepInvDailyEntity> page = wmRepInvDailyService.findPage(new Page<WmRepInvDailyEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepInvDaily:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WmRepInvDailyEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "库存数据记录.xlsx";
            Page<WmRepInvDailyEntity> page = wmRepInvDailyService.findPage(new Page<WmRepInvDailyEntity>(request, response, -1), entity);
            new ExportExcel("", WmRepInvDailyEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库存数据记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 库区库存报表
     */
    @RequiresPermissions("report:wmRepInvDaily:zone:list")
    @RequestMapping(value = "/zone/list")
    public String zoneList(Model model) {
        model.addAttribute("wmRepInvDailyByZoneEntity", new WmRepInvDailyByZoneEntity());
        return "modules/wms/report/banQinWmRepInvDailyByZoneList";
    }

    /**
     * 库区库存报表
     */
    @ResponseBody
    @RequiresPermissions("report:wmRepInvDaily:zone:list")
    @RequestMapping(value = "/zone/data")
    public Map<String, Object> zoneData(WmRepInvDailyByZoneEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmRepInvDailyService.findPageByZone(new Page<WmRepInvDailyByZoneEntity>(request, response), qEntity));
    }

    /**
     * 库区库存报表
     */
    @ResponseBody
    @RequiresPermissions("report:wmRepInvDaily:zone:list")
    @RequestMapping(value = "/zone/total")
    public Map<String, Object> zoneTotal(WmRepInvDailyByZoneEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = Maps.newHashMap();
        Page<WmRepInvDailyByZoneEntity> page = wmRepInvDailyService.findPageByZone(new Page<WmRepInvDailyByZoneEntity>(request, response, -1), qEntity);
        map.put("totalQty", page.getList().stream().mapToDouble(WmRepInvDailyByZoneEntity::getInvQty).sum());
        map.put("totalWeight", page.getList().stream().mapToDouble(WmRepInvDailyByZoneEntity::getWeight).sum());
        return map;
    }


    /**
     * 库区库存报表导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:wmRepInvDaily:zone:export")
    @RequestMapping(value = "/zone/export")
    public AjaxJson zoneExportFile(WmRepInvDailyByZoneEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "库区库存报表.xlsx";
            Page<WmRepInvDailyByZoneEntity> page = wmRepInvDailyService.findPageByZone(new Page<WmRepInvDailyByZoneEntity>(request, response, -1), qEntity);
            new ExportExcel("", WmRepInvDailyByZoneEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库区库存报表失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}