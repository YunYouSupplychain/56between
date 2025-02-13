package com.yunyou.modules.wms.inbound.web;

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
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoHeader;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPoService;
import com.yunyou.modules.wms.inbound.service.BanQinWmPoDetailService;
import com.yunyou.modules.wms.inbound.service.BanQinWmPoHeaderService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购单Controller
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inbound/banQinWmPoHeader")
public class BanQinWmPoHeaderController extends BaseController {
    @Autowired
    private BanQinWmPoHeaderService banQinWmPoHeaderService;
    @Autowired
    private BanQinInboundPoService inboundPoService;
    @Autowired
    private BanQinWmPoDetailService banQinWmPoDetailService;

    @ModelAttribute
    public BanQinWmPoEntity get(@RequestParam(required = false) String id) {
        BanQinWmPoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmPoHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmPoEntity();
        }
        return entity;
    }

    /**
     * 采购单列表页面
     */
    @RequiresPermissions("inbound:banQinWmPoHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inbound/banQinWmPoHeaderList";
    }

    /**
     * 采购单列表数据
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmPoHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmPoEntity banQinWmPoEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmPoEntity> page = banQinWmPoHeaderService.findPage(new Page<BanQinWmPoEntity>(request, response), banQinWmPoEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑采购单表单页面
     */
    @RequiresPermissions(value = {"inbound:banQinWmPoHeader:view", "inbound:banQinWmPoHeader:add", "inbound:banQinWmPoHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmPoEntity banQinWmPoEntity, Model model) {
        model.addAttribute("banQinWmPoEntity", banQinWmPoEntity);
        if (StringUtils.isBlank(banQinWmPoEntity.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/inbound/banQinWmPoHeaderForm";
    }

    /**
     * 保存采购单
     */
    @RequiresPermissions(value = {"inbound:banQinWmPoHeader:add", "inbound:banQinWmPoHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinWmPoEntity banQinWmPoEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmPoEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        try {
            ResultMessage msg = inboundPoService.savePoEntity(banQinWmPoEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());

            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("entity", msg.getData());
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除采购单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmPoHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.removePoEntity(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmPoHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmPoHeader banQinWmPoHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "采购单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmPoHeader> page = banQinWmPoHeaderService.findPage(new Page<BanQinWmPoHeader>(request, response, -1), banQinWmPoHeader);
            new ExportExcel("采购单", BanQinWmPoHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出采购单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inbound:banQinWmPoHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmPoHeader> list = ei.getDataList(BanQinWmPoHeader.class);
            for (BanQinWmPoHeader banQinWmPoHeader : list) {
                try {
                    banQinWmPoHeaderService.save(banQinWmPoHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条采购单记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条采购单记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入采购单失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inbound/banQinWmPoHeader/?repage";
    }

    /**
     * 下载导入采购单数据模板
     */
    @RequiresPermissions("inbound:banQinWmPoHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "采购单数据导入模板.xlsx";
            List<BanQinWmPoHeader> list = Lists.newArrayList();
            new ExportExcel("采购单数据", BanQinWmPoHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inbound/banQinWmPoHeader/?repage";
    }

    /**
     * 审核
     */
    @ResponseBody
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.auditPo(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 取消审核
     */
    @ResponseBody
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.cancelAuditPo(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 采购单生成ASN单页面
     */
    @RequestMapping(value = "createAsnForm")
    public String createAsnForm(BanQinWmPoEntity banQinWmPoEntity, Model model) {
        model.addAttribute("banQinWmPoEntity", banQinWmPoEntity);
        return "modules/wms/inbound/banQinWmPoCreateAsnForm";
    }

    /**
     * 生成ASN单页面数据
     */
    @RequestMapping(value = "createAsnData")
    @ResponseBody
    public Map<String, Object> createAsnData(BanQinWmPoEntity banQinWmPoEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page page = new Page<BanQinWmPoEntity>(request, response);
        page.setList(banQinWmPoDetailService.findEntityByPoId(banQinWmPoEntity.getId().split(","), banQinWmPoEntity.getOrgId()));
        return getBootstrapData(page);
    }
    
    /**
     * 生成ASN
     */
    @ResponseBody
    @RequestMapping(value = "createAsn")
    public AjaxJson createAsn(@RequestBody BanQinWmPoEntity entity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.createAsn(entity.getOwnerCode(), entity.getSupplierCode(), entity.getWmPoDetailEntitys(), entity.getOrgId());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 关闭订单
     */
    @ResponseBody
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.closePo(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 取消订单
     */
    @ResponseBody
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancelOrder(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.cancelPo(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }
    
}