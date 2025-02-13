package com.yunyou.modules.oms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.enums.OrderSource;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.common.BusinessOrderType;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.entity.OmChainHeaderEntity;
import com.yunyou.modules.oms.order.entity.extend.OmChainDCImportEntity;
import com.yunyou.modules.oms.order.entity.extend.OmChainHeaderExportEntity;
import com.yunyou.modules.oms.order.entity.extend.OmChainWmsImportEntity;
import com.yunyou.modules.oms.order.manager.OmChainInterceptManager;
import com.yunyou.modules.oms.order.manager.OmChainRemoveManager;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoImportOrderNoQueryEntity;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商配送订单Controller
 *
 * @author ZYF
 * @version 2021-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/order/omChainSupDistributionOrder")
public class OmChainSupDistributionOrderController extends BaseController {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmChainInterceptManager omChainInterceptManager;
    @Autowired
    private OmChainRemoveManager omChainRemoveManager;

    @ModelAttribute
    public OmChainHeaderEntity get(@RequestParam(required = false) String id) {
        OmChainHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omChainHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmChainHeaderEntity();
        }
        return entity;
    }

    /**
     * 供应商配送订单列表页面
     */
    @RequiresPermissions("order:omChainSupDistributionOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/order/omChainSupDistributionOrderList";
    }

    /**
     * 供应商配送订单列表数据
     */
    @ResponseBody
    @RequiresPermissions("order:omChainSupDistributionOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmChainHeaderEntity omChainHeaderEntity, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (null != omChainHeaderEntity.getCustomerNoFile()) {
                ImportExcel customerNoExcel = new ImportExcel(omChainHeaderEntity.getCustomerNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> customerNoList = customerNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(customerNoList)) {
                    omChainHeaderEntity.setCustomerNoList(customerNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
            if (null != omChainHeaderEntity.getExtendNoFile()) {
                ImportExcel extendNoExcel = new ImportExcel(omChainHeaderEntity.getExtendNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> extendNoList = extendNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(extendNoList)) {
                    omChainHeaderEntity.setExtendNoList(extendNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
        } catch (Exception ignored) { }
        omChainHeaderEntity.setBusinessOrderTypeList(
                Lists.newArrayList(BusinessOrderType.SUP_DC_ASN.getBusinessType(), BusinessOrderType.SUP_DC_SO.getBusinessType(),
                        BusinessOrderType.SUP_WMS_ASN.getBusinessType(), BusinessOrderType.SUP_WMS_SO.getBusinessType()));
        return getBootstrapData(omChainHeaderService.findPage(new Page<OmChainHeaderEntity>(request, response), omChainHeaderEntity));
    }

    /**
     * 查看，增加，编辑供应商配送订单表单页面
     */
    @RequiresPermissions(value = {"order:omChainSupDistributionOrder:view", "order:omChainSupDistributionOrder:add", "order:omChainSupDistributionOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmChainHeaderEntity omChainHeaderEntity, Model model) {
        model.addAttribute("omChainHeaderEntity", omChainHeaderEntity);
        return "modules/oms/order/omChainSupDistributionOrderForm";
    }

    /**
     * 保存供应商配送订单
     */
    @ResponseBody
    @RequiresPermissions(value = {"order:omChainSupDistributionOrder:add", "order:omChainSupDistributionOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmChainHeaderEntity omChainHeaderEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omChainHeaderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omChainHeaderService.checkSaveInfo(omChainHeaderEntity);
            omChainHeaderService.save(omChainHeaderEntity);
            j.setSuccess(true);
            j.setMsg("保存供应商配送订单成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除供应商配送订单
     */
    @ResponseBody
    @RequiresPermissions("order:omChainSupDistributionOrder:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        try {
            User user = UserUtils.getUser();
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omChainRemoveManager.delete(omChainHeaderService.get(id), user);
            }
            j.setMsg("删除供应商配送订单成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("order:omChainSupDistributionOrder:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmChainHeaderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "供应商配送订单.xlsx";
            Page<OmChainHeaderEntity> page = omChainHeaderService.findPage(new Page<OmChainHeaderEntity>(request, response, -1), entity);
            new ExportExcel("", OmChainHeaderExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出供应商配送订单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据（直通）
     */
    @RequiresPermissions(value = {"order:omChainSupDistributionOrder:dc:importLC", "order:omChainSupDistributionOrder:dc:importLD", "order:omChainSupDistributionOrder:dc:importV",
            "order:omChainSupDistributionOrder:dc:importF", "order:omChainSupDistributionOrder:dc:importRF"}, logical = Logical.OR)
    @RequestMapping(value = "importDC", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFileDC(MultipartFile file, String orgId, String uploadType) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmChainDCImportEntity> list = ei.getDataList(OmChainDCImportEntity.class);
            if (CollectionUtil.isEmpty(list)) {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
                return j;
            }
            list.forEach(v -> {
                v.setOrgId(orgId);
                v.setOrderSource(OrderSource.IMP.getCode());
                v.setSourceOrderNo(file.getOriginalFilename());
            });
            ResultMessage msg = omChainHeaderService.importDCOrderByBusinessType(list, BusinessOrderType.SUP_DC_ASN, BusinessOrderType.SUP_DC_SO, uploadType);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg("导入异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("供应商配送订单导入异常", e);
            j.setSuccess(false);
            j.setMsg("导入异常：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入供应商配送订单数据模板（直通）
     */
    @RequiresPermissions(value = {"order:omChainSupDistributionOrder:dc:importLC", "order:omChainSupDistributionOrder:dc:importLD", "order:omChainSupDistributionOrder:dc:importV",
            "order:omChainSupDistributionOrder:dc:importF", "order:omChainSupDistributionOrder:dc:importRF"}, logical = Logical.OR)
    @RequestMapping(value = "importDC/template")
    public String importFileDCTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel("", OmChainDCImportEntity.class, 2).setDataList(Lists.newArrayList()).write(response, "供应商配送订单导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/order/omChainSupDistributionOrder/?repage";
    }

    /**
     * 导入Excel数据（存储）
     */
    @RequiresPermissions(value = {"order:omChainSupDistributionOrder:wms:importASN", "order:omChainSupDistributionOrder:wms:importSO"}, logical = Logical.OR)
    @RequestMapping(value = "importWms", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFileWms(MultipartFile file, String orgId, String uploadType) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmChainWmsImportEntity> list = ei.getDataList(OmChainWmsImportEntity.class);
            if (CollectionUtil.isEmpty(list)) {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
                return j;
            }
            list.forEach(v -> {
                v.setOrgId(orgId);
                v.setOrderSource(OrderSource.IMP.getCode());
                v.setSourceOrderNo(file.getOriginalFilename());
            });
            ResultMessage msg;
            if (OmsConstants.IMPORT_TYPE_WMS_ASN.equals(uploadType)) {
                msg = omChainHeaderService.importOrderByBusinessType(list, BusinessOrderType.SUP_WMS_ASN);
            } else {
                msg = omChainHeaderService.importOrderByBusinessType(list, BusinessOrderType.SUP_WMS_SO);
            }
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg("导入失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("供应商配送订单导入失败", e);
            j.setSuccess(false);
            j.setMsg("导入失败：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入供应商配送订单数据模板（存储）
     */
    @RequiresPermissions(value = {"order:omChainSupDistributionOrder:wms:importASN", "order:omChainSupDistributionOrder:wms:importSO"}, logical = Logical.OR)
    @RequestMapping(value = "importWms/template")
    public String importFileWmsTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel("", OmChainWmsImportEntity.class, 2).setDataList(Lists.newArrayList()).write(response, "供应商配送订单导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/order/omChainSupDistributionOrder/?repage";
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public OmChainHeader detail(String id) {
        return omChainHeaderService.get(id);
    }

    /**
     * 描述：审核
     *
     * @author Jianhua on 2019/5/6
     */
    @ResponseBody
    @RequiresPermissions("order:omChainSupDistributionOrder:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            for (String id : ids.split(",")) {
                omChainHeaderService.audit(omChainHeaderService.get(id));
            }

            j.setSuccess(true);
            j.setMsg("审核成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：取消审核
     *
     * @author Jianhua on 2019/5/6
     */
    @ResponseBody
    @RequiresPermissions("order:omChainSupDistributionOrder:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            for (String id : ids.split(",")) {
                omChainHeaderService.cancelAudit(omChainHeaderService.get(id));
            }

            j.setSuccess(true);
            j.setMsg("取消审核成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：取消
     * <p>
     * create by Jianhua on 2019/9/29
     */
    @RequiresPermissions("order:omChainSupDistributionOrder:cancel")
    @ResponseBody
    @RequestMapping("cancel")
    public AjaxJson cancel(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                omChainHeaderService.cancel(id);
            }
        } catch (Exception e) {
            logger.error("供应商配送订单取消", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：截单
     *
     * @author Jianhua on 2020-2-13
     */
    @ResponseBody
    @RequiresPermissions("order:omChainSupDistributionOrder:intercept")
    @RequestMapping("intercept")
    public AjaxJson intercept(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                omChainInterceptManager.intercept(id);
            } catch (Exception e) {
                logger.error("供应链截单", e);
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(j.getMsg() + "，其中" + errMsg);
        }
        return j;
    }

}