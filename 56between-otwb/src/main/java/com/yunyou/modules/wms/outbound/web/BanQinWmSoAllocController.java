package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoAllocExportEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundTrackingService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.google.common.collect.Lists;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 分配拣货明细Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmSoAlloc")
public class BanQinWmSoAllocController extends BaseController {
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;
    @Autowired
    private BanQinOutboundSoService outboundSoService;
    @Autowired
    private BanQinOutboundTrackingService outboundTrackingService;

    @ModelAttribute
    public BanQinWmSoAllocEntity get(@RequestParam(required = false) String id) {
        BanQinWmSoAllocEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmSoAllocService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmSoAllocEntity();
        }
        return entity;
    }

    /**
     * 分配拣货明细列表页面
     */
    @RequiresPermissions("outbound:banQinWmSoAlloc:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmSoAllocList";
    }

    /**
     * 分配拣货明细列表数据
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSoAllocEntity banQinWmSoAllocEntity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotBlank(banQinWmSoAllocEntity.getStatuss())) {
            banQinWmSoAllocEntity.setStatusList(Arrays.asList(banQinWmSoAllocEntity.getStatuss().split(",")));
        }
        return getBootstrapData(banQinWmSoAllocService.findPage(new Page<BanQinWmSoAllocEntity>(request, response), banQinWmSoAllocEntity));
    }

    /**
     * 查看，增加，编辑分配拣货明细表单页面
     */
    @RequiresPermissions(value = {"outbound:banQinWmSoAlloc:view", "outbound:banQinWmSoAlloc:add", "outbound:banQinWmSoAlloc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmSoAllocEntity banQinWmSoAllocEntity, Model model) {
        model.addAttribute("banQinWmSoAllocEntity", banQinWmSoAllocEntity);
        return "modules/wms/outbound/banQinWmSoAllocForm";
    }

    /**
     * 手工分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:save")
    @RequestMapping(value = "manualAlloc")
    public AjaxJson manualAlloc(@RequestBody BanQinWmSoAllocEntity banQinWmSoAllocEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.manualAlloc(banQinWmSoAllocEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拣货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(@RequestBody List<BanQinWmSoAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.pickingByAlloc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 手工拣货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:manualPicking")
    @RequestMapping(value = "manualPicking")
    public AjaxJson manualPicking(@RequestBody BanQinWmSoAllocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.pickingByManual(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 发货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:shipment")
    @RequestMapping(value = "shipment")
    public AjaxJson shipment(@RequestBody List<BanQinWmSoAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.shipmentByAlloc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(@RequestBody List<BanQinWmSoAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelAllocByAlloc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消拣货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:cancelPick")
    @RequestMapping(value = "cancelPick")
    public AjaxJson cancelPick(@RequestBody List<BanQinWmSoAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelPickingByAlloc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消发运
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:cancelShipment")
    @RequestMapping(value = "cancelShipment")
    public AjaxJson cancelShipment(@RequestBody List<BanQinWmSoAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelShipmentByAlloc(list);
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
    @RequiresPermissions("outbound:banQinWmSoAlloc:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSoAllocEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinWmSoAllocEntity> page = banQinWmSoAllocService.findPage(new Page<BanQinWmSoAllocEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmSoAllocExportEntity.class).setDataList(page.getList()).write(response, "分配拣货明细.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出分配拣货明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("outbound:banQinWmSoAlloc:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmSoAlloc> list = ei.getDataList(BanQinWmSoAlloc.class);
            for (BanQinWmSoAlloc banQinWmSoAlloc : list) {
                try {
                    banQinWmSoAllocService.save(banQinWmSoAlloc);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条分配拣货明细记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条分配拣货明细记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入分配拣货明细失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/outbound/banQinWmSoAlloc/?repage";
    }

    /**
     * 下载导入分配拣货明细数据模板
     */
    @RequiresPermissions("outbound:banQinWmSoAlloc:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "分配拣货明细数据导入模板.xlsx";
            List<BanQinWmSoAlloc> list = Lists.newArrayList();
            new ExportExcel("分配拣货明细数据", BanQinWmSoAlloc.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/outbound/banQinWmSoAlloc/?repage";
    }

    /**
     * 打印面单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:printWaybill")
    @RequestMapping(value = "printWaybill")
    public AjaxJson printWaybill(@RequestBody BanQinWmSoAllocEntity entity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundTrackingService.printWaybillByAlloc(Arrays.asList(entity.getAllocId().split(",")), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.put("imageList", msg.getData());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成拣货单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoAlloc:generatePick")
    @RequestMapping(value = "generatePick")
    public AjaxJson generatePick(@RequestBody List<BanQinWmSoAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.createPick(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }


    /**
     * 打印标签商品列表数据
     */
    @ResponseBody
    @RequestMapping(value = "skuData")
    public Map<String, Object> skuData(BanQinWmSoAllocEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmSoAllocService.findSkuDataPage(new Page<BanQinWmSoAllocEntity>(request, response), entity));
    }

}