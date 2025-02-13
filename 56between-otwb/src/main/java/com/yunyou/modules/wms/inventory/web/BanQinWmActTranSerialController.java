package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerialEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmActTranSerialExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmActTranSerialService;
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
 * 库存序列号交易Controller
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmActTranSerial")
public class BanQinWmActTranSerialController extends BaseController {
    @Autowired
    private BanQinWmActTranSerialService banQinWmActTranSerialService;

    @ModelAttribute
    public BanQinWmActTranSerialEntity get(@RequestParam(required=false) String id) {
        return new BanQinWmActTranSerialEntity();
    }

    /**
     * 库存序列号交易列表页面
     */
    @RequiresPermissions("inventory:banQinWmActTranSerial:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmActTranSerialList";
    }

    /**
     * 库存序列号交易列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmActTranSerial:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmActTranSerialEntity banQinWmActTranSerialEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmActTranSerialEntity> page = banQinWmActTranSerialService.findPage(new Page<BanQinWmActTranSerialEntity>(request, response), banQinWmActTranSerialEntity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmActTranSerial:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmActTranSerialEntity banQinWmActTranSerial, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "库存序列号交易" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmActTranSerialEntity> page = banQinWmActTranSerialService.findPage(new Page<BanQinWmActTranSerialEntity>(request, response, -1), banQinWmActTranSerial);
            new ExportExcel("", BanQinWmActTranSerialExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库存序列号交易记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}