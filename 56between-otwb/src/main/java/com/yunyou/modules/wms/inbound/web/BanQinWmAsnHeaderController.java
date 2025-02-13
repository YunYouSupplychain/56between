package com.yunyou.modules.wms.inbound.web;

import com.yunyou.modules.wms.inbound.entity.extend.BanQinWmAsnSerialReceiveImportEntity;
import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnEntity;
import com.yunyou.modules.wms.inbound.entity.extend.BanQinWmAsnExportEntity;
import com.yunyou.modules.wms.inbound.entity.extend.BanQinWmAsnImportEntity;
import com.yunyou.modules.wms.inbound.service.BanQinInboundDuplicateService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnHeaderService;
import com.yunyou.modules.wms.report.entity.ReceivingOrderLabel;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import com.yunyou.modules.wms.report.entity.WmCheckReceiveOrderLabel;
import net.sf.jasperreports.engine.JRDataSource;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库单Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inbound/banQinWmAsnHeader")
public class BanQinWmAsnHeaderController extends BaseController {
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinInboundDuplicateService inboundDuplicateService;

    @ModelAttribute
    public BanQinWmAsnEntity get(@RequestParam(required = false) String id) {
        BanQinWmAsnEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmAsnHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmAsnEntity();
        }
        return entity;
    }

    /**
     * 入库单列表页面
     */
    @RequiresPermissions("inbound:banQinWmAsnHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inbound/banQinWmAsnHeaderList";
    }

    /**
     * 入库单列表数据
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAsnEntity banQinWmAsnEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmAsnEntity> page = banQinWmAsnHeaderService.findPage(new Page<BanQinWmAsnEntity>(request, response), banQinWmAsnEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑入库单表单页面
     */
    @RequiresPermissions(value = {"inbound:banQinWmAsnHeader:view", "inbound:banQinWmAsnHeader:add", "inbound:banQinWmAsnHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmAsnEntity banQinWmAsnEntity, Model model) {
        model.addAttribute("banQinWmAsnEntity", banQinWmAsnEntity);
        if (StringUtils.isBlank(banQinWmAsnEntity.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/inbound/banQinWmAsnHeaderForm";
    }

    /**
     * 保存入库单
     */
    @ResponseBody
    @RequiresPermissions(value = {"inbound:banQinWmAsnHeader:add", "inbound:banQinWmAsnHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmAsnEntity banQinWmAsnEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmAsnEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据！");
        }
        try {
            ResultMessage msg = banQinWmAsnHeaderService.saveAsnEntity(banQinWmAsnEntity);
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
     * 批量删除入库单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.removeAsnEntity(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 入库单复制
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:duplicate")
    @RequestMapping(value = "duplicate")
    public AjaxJson duplicate(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundDuplicateService.duplicateAsn(ids);
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
     * 批量审核入库单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.auditAsn(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量取消审核入库单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.cancelAuditAsn(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量采购成本分摊
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:costAlloc")
    @RequestMapping(value = "costAlloc")
    public AjaxJson costAlloc(String ids, String strategy) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.costAlloc(ids.split(","), strategy);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量生成质检单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:cancelAudit")
    @RequestMapping(value = "createQc")
    public AjaxJson createQc(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.createQcByAsn(ids.split(","));
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
    @RequiresPermissions("inbound:banQinWmAsnHeader:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.closeAsn(ids.split(","));
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
    @RequiresPermissions("inbound:banQinWmAsnHeader:cancelOrder")
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancelOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.cancelAsn(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 冻结订单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:holdOrder")
    @RequestMapping(value = "holdOrder")
    public AjaxJson holdOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            banQinWmAsnHeaderService.holdAsn(ids.split(","));
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消冻结
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:cancelHold")
    @RequestMapping(value = "cancelHold")
    public AjaxJson cancelHold(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            banQinWmAsnHeaderService.cancelHoldAsn(ids.split(","));
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成凭证号
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:createVoucherNo")
    @RequestMapping(value = "createVoucherNo")
    public AjaxJson createVoucherNo(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.createVoucherNoByAsn(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消凭证号
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnHeader:cancelVoucherNo")
    @RequestMapping(value = "cancelVoucherNo")
    public AjaxJson cancelVoucherNo(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.cancelVoucherNoByAsn(ids.split(","));
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
    @RequiresPermissions("inbound:banQinWmAsnHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmAsnEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "入库单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmAsnEntity> page = banQinWmAsnHeaderService.findPage(new Page<BanQinWmAsnEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmAsnExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出入库单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inbound:banQinWmAsnHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmAsnImportEntity> list = ei.getDataList(BanQinWmAsnImportEntity.class);
            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(v -> {
                    v.setOrgId(orgId);
                });
                ResultMessage msg = banQinWmAsnHeaderService.importOrder(list);
                j.setSuccess(msg.isSuccess());
                j.setMsg(msg.getMessage());
            } else {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
            }
        } catch (Exception e) {
            logger.error("导入异常", e);
            j.setSuccess(false);
            j.setMsg("导入异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入入库单数据模板
     */
    @RequiresPermissions("inbound:banQinWmAsnHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "入库单数据导入模板.xlsx";
            List<BanQinWmAsnImportEntity> list = Lists.newArrayList();
            new ExportExcel("", BanQinWmAsnImportEntity.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inbound/banQinWmAsnHeader/?repage";
    }

    /**
     * 序列号收货确认
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnSerial:serialReceiving")
    @RequestMapping(value = "serialReceiving")
    public AjaxJson serialReceiving(@RequestBody BanQinWmAsnDetailReceiveEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.serialReceiving(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 序列号收货
     */
    @RequiresPermissions("inbound:banQinWmAsnHeader:serialReceive")
    @RequestMapping(value = "serialReceive", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson serialReceive(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmAsnSerialReceiveImportEntity> list = ei.getDataList(BanQinWmAsnSerialReceiveImportEntity.class);
            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(v -> {
                    v.setOrgId(orgId);
                });
                ResultMessage msg = banQinWmAsnHeaderService.serialReceive(list);
                j.setSuccess(msg.isSuccess());
                j.setMsg(msg.getMessage());
            } else {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
            }
        } catch (Exception e) {
            logger.error("序列号导入收货异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印收货单
     * @param model
     * @return
     */
    @RequestMapping(value = "/printReceivingOrder")
    public String printReceivingOrder(Model model, String ids) {
        List<ReceivingOrderLabel> result = banQinWmAsnHeaderService.getReceivingOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/receivingOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印托盘标签
     * @param model
     * @return
     */
    @RequestMapping(value = "/printTraceLabel")
    public String printTraceLabel(Model model, String ids) {
        List<TraceLabel> result = banQinWmAsnHeaderService.getTraceLabel(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/traceLabel.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印托盘标签（二维码）
     * @param model
     * @return
     */
    @RequestMapping(value = "/printTraceLabelQrCode")
    public String printTraceLabelQrCode(Model model, String ids) {
        List<TraceLabel> result = banQinWmAsnHeaderService.getTraceLabel(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/traceLabel_qrcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印验收单
     * @param model
     * @return
     */
    @RequestMapping(value = "/printCheckReceiveOrder")
    public String printCheckReceiveOrder(Model model, String ids) {
        List<WmCheckReceiveOrderLabel> result = banQinWmAsnHeaderService.getCheckReceiveOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/wmsCheckReceiveOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印收货单(横版)
     * @param model
     * @return
     */
    @RequestMapping(value = "/printReceivingOrderLandscape")
    public String printReceivingOrderLandscape(Model model, String ids) {
        List<ReceivingOrderLabel> result = banQinWmAsnHeaderService.getReceivingOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/receivingOrderLandscape.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }
}