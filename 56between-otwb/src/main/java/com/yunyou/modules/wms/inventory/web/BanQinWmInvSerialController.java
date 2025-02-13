package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerialEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmInvSerialExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSerialService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 序列号库存表Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvSerial")
public class BanQinWmInvSerialController extends BaseController {
    @Autowired
    private BanQinWmInvSerialService banQinWmInvSerialService;

    @ModelAttribute
    public BanQinWmInvSerialEntity get(@RequestParam(required = false) String id) {
        return new BanQinWmInvSerialEntity();
    }

    /**
     * 序列号库存表列表页面
     */
    @RequiresPermissions("inventory:banQinWmInvSerial:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmInvSerialList";
    }

    /**
     * 序列号库存表列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmInvSerial:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmInvSerialEntity banQinWmInvSerialEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmInvSerialEntity> page = banQinWmInvSerialService.findPage(new Page<BanQinWmInvSerialEntity>(request, response), banQinWmInvSerialEntity);
        return getBootstrapData(page);
    }

    /**
     * 序列号库存表列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinWmInvSerialEntity banQinWmInvSerialEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmInvSerialEntity> page = banQinWmInvSerialService.findPage(new Page<BanQinWmInvSerialEntity>(request, response), banQinWmInvSerialEntity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmInvSerial:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmInvSerialEntity banQinWmInvSerial, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "序列号库存表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmInvSerialEntity> page = banQinWmInvSerialService.findPage(new Page<BanQinWmInvSerialEntity>(request, response, -1), banQinWmInvSerial);
            new ExportExcel("", BanQinWmInvSerialExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出序列号库存表记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}