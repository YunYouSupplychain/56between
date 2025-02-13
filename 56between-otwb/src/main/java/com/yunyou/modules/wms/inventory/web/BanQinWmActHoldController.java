package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActHold;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmActHoldExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmActHoldService;
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
 * 冻结记录Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmActHold")
public class BanQinWmActHoldController extends BaseController {

    @Autowired
    private BanQinWmActHoldService banQinWmActHoldService;

    @ModelAttribute
    public BanQinWmActHold get(@RequestParam(required = false) String id) {
        BanQinWmActHold entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmActHoldService.get(id);
        }
        if (entity == null) {
            entity = new BanQinWmActHold();
        }
        return entity;
    }

    /**
     * 冻结记录列表页面
     */
    @RequiresPermissions("inventory:banQinWmActHold:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmActHoldList";
    }

    /**
     * 冻结记录列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmActHold:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmActHold banQinWmActHold, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmActHold> page = banQinWmActHoldService.findPage(new Page<BanQinWmActHold>(request, response), banQinWmActHold);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmActHold:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmActHold banQinWmActHold, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "冻结记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmActHold> page = banQinWmActHoldService.findPage(new Page<BanQinWmActHold>(request, response, -1), banQinWmActHold);
            new ExportExcel("", BanQinWmActHoldExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出冻结记录记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}