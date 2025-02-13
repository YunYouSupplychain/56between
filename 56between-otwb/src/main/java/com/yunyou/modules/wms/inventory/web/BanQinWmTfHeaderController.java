package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfHeaderEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmTfHeaderExportEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintTfOrder;
import com.yunyou.modules.wms.inventory.service.BanQinWmTfHeaderService;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.List;
import java.util.Map;

/**
 * 转移单Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmTfHeader")
public class BanQinWmTfHeaderController extends BaseController {

    @Autowired
    private BanQinWmTfHeaderService banQinWmTfHeaderService;

    @ModelAttribute
    public BanQinWmTfHeaderEntity get(@RequestParam(required = false) String id) {
        BanQinWmTfHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmTfHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmTfHeaderEntity();
        }
        return entity;
    }

    /**
     * 转移单列表页面
     */
    @RequiresPermissions("inventory:banQinWmTfHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmTfHeaderList";
    }

    /**
     * 转移单列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmTfHeaderEntity banQinWmTfHeaderEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmTfHeaderService.findPage(new Page<BanQinWmTfHeaderEntity>(request, response), banQinWmTfHeaderEntity));
    }

    /**
     * 查看，增加，编辑转移单表单页面
     */
    @RequiresPermissions(value = {"inventory:banQinWmTfHeader:view", "inventory:banQinWmTfHeader:add", "inventory:banQinWmTfHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmTfHeaderEntity banQinWmTfHeaderEntity, Model model) {
        model.addAttribute("banQinWmTfHeaderEntity", banQinWmTfHeaderEntity);
        if (StringUtils.isBlank(banQinWmTfHeaderEntity.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/inventory/banQinWmTfHeaderForm";
    }

    /**
     * 保存转移单
     */
    @ResponseBody
    @RequiresPermissions(value = {"inventory:banQinWmTfHeader:add", "inventory:banQinWmTfHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmTfHeaderEntity banQinWmTfHeaderEntity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmTfHeaderEntity)) {
            j.setSuccess(false);
            return j;
        }
        try {
            BanQinWmTfHeaderEntity entity = banQinWmTfHeaderService.saveEntity(banQinWmTfHeaderEntity);
            j.put("entity", entity);
            j.setMsg("保存成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除转移单
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.removeTfEntity(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmTfHeaderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "转移单.xlsx";
            Page<BanQinWmTfHeaderEntity> page = banQinWmTfHeaderService.findPage(new Page<BanQinWmTfHeaderEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmTfHeaderExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出转移单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inventory:banQinWmTfHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmTfHeader> list = ei.getDataList(BanQinWmTfHeader.class);
            for (BanQinWmTfHeader banQinWmTfHeader : list) {
                try {
                    banQinWmTfHeaderService.save(banQinWmTfHeader);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条转移单记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条转移单记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入转移单失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmTfHeader/?repage";
    }

    /**
     * 下载导入转移单数据模板
     */
    @RequiresPermissions("inventory:banQinWmTfHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "转移单数据导入模板.xlsx";
            List<BanQinWmTfHeader> list = Lists.newArrayList();
            new ExportExcel("转移单数据", BanQinWmTfHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmTfHeader/?repage";
    }

    /**
     * 审核
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.audit(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消审核
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.cancelAudit(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 转移
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:transfer")
    @RequestMapping(value = "transfer")
    public AjaxJson transfer(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.transfer(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 关闭订单
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.closeOrder(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消订单
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfHeader:cancelOrder")
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancelOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.cancelOrder(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印转移单
     */
    @RequestMapping(value = "/printTfOrder", method = RequestMethod.POST)
    @RequiresPermissions("inventory:banQinWmAdHeader:printTfOrder")
    public String printTfOrder(Model model, String ids) {
        List<PrintTfOrder> result = banQinWmTfHeaderService.getTfOrder(ids.split(","));

        model.addAttribute("url", "classpath:/jasper/wm_tf_order.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}