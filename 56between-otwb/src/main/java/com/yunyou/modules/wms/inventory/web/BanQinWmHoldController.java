package com.yunyou.modules.wms.inventory.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHold;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHoldEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmHoldExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmHoldService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 库存冻结Controller
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmHold")
public class BanQinWmHoldController extends BaseController {

    @Autowired
    private BanQinWmHoldService banQinWmHoldService;

    @ModelAttribute
    public BanQinWmHoldEntity get(@RequestParam(required = false) String id) {
        BanQinWmHoldEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmHoldService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmHoldEntity();
        }
        return entity;
    }

    /**
     * 库存冻结列表页面
     */
    @RequiresPermissions("inventory:banQinWmHold:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmHoldList";
    }

    /**
     * 库存冻结列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmHold:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmHoldEntity banQinWmHoldEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmHoldEntity> page = banQinWmHoldService.findPage(new Page<BanQinWmHoldEntity>(request, response), banQinWmHoldEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑库存冻结表单页面
     */
    @RequiresPermissions(value = {"inventory:banQinWmHold:view", "inventory:banQinWmHold:add"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmHoldEntity BanQinWmHoldEntity, Model model) {
        model.addAttribute("banQinWmHoldEntity", BanQinWmHoldEntity);
        if (StringUtils.isBlank(BanQinWmHoldEntity.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/inventory/banQinWmHoldForm";
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmHold:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmHoldEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "库存冻结" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmHoldEntity> page = banQinWmHoldService.findPage(new Page<BanQinWmHoldEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmHoldExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库存冻结记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inventory:banQinWmHold:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmHold> list = ei.getDataList(BanQinWmHold.class);
            for (BanQinWmHold banQinWmHold : list) {
                try {
                    banQinWmHoldService.save(banQinWmHold);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条库存冻结记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条库存冻结记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入库存冻结失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmHold/?repage";
    }

    /**
     * 下载导入库存冻结数据模板
     */
    @RequiresPermissions("inventory:banQinWmHold:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "库存冻结数据导入模板.xlsx";
            List<BanQinWmHold> list = Lists.newArrayList();
            new ExportExcel("库存冻结数据", BanQinWmHold.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmHold/?repage";
    }

    /**
     * 冻结
     *
     * @param wmHold
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmHold:frost")
    @RequestMapping(value = "frost")
    public AjaxJson frost(BanQinWmHoldEntity wmHold, Model model) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmHoldService.frost(wmHold);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 解冻
     *
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmHold:thaw")
    @RequestMapping(value = "thaw")
    public AjaxJson thaw(String ids, Model model) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmHoldService.thaw(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}