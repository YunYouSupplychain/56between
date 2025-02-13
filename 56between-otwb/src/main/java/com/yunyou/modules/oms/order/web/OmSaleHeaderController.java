package com.yunyou.modules.oms.order.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.FileUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmSaleHeader;
import com.yunyou.modules.oms.order.entity.OmSaleHeaderEntity;
import com.yunyou.modules.oms.order.service.OmSaleHeaderService;
import com.yunyou.modules.oms.report.entity.OmShipOrderLabel;
import com.google.common.collect.Lists;
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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单Controller
 *
 * @author WMJ
 * @version 2019-04-17
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/order/omSaleHeader")
public class OmSaleHeaderController extends BaseController {

    @Autowired
    private OmSaleHeaderService omSaleHeaderService;

    @ModelAttribute
    public OmSaleHeaderEntity get(@RequestParam(required = false) String id) {
        OmSaleHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omSaleHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmSaleHeaderEntity();
        }
        return entity;
    }

    /**
     * 销售订单列表页面
     */
    @RequiresPermissions("order:omSaleHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/order/omSaleHeaderList";
    }

    /**
     * 销售订单列表数据
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmSaleHeaderEntity omSaleHeaderEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omSaleHeaderService.findPage(new Page<OmSaleHeaderEntity>(request, response), omSaleHeaderEntity));
    }

    /**
     * 查看，增加，编辑销售订单表单页面
     */
    @RequiresPermissions(value = {"order:omSaleHeader:view", "order:omSaleHeader:add", "order:omSaleHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmSaleHeaderEntity omSaleHeaderEntity, Model model) {
        model.addAttribute("omSaleHeaderEntity", omSaleHeaderEntity);
        return "modules/oms/order/omSaleHeaderForm";
    }

    /**
     * 保存销售订单
     */
    @ResponseBody
    @RequiresPermissions(value = {"order:omSaleHeader:add", "order:omSaleHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmSaleHeaderEntity omSaleHeaderEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omSaleHeaderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omSaleHeaderService.checkAndSave(omSaleHeaderEntity);
            j.setSuccess(true);
            j.setMsg("保存销售订单成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除销售订单
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(OmSaleHeader omSaleHeader) {
        AjaxJson j = new AjaxJson();

        try {
            omSaleHeaderService.delete(omSaleHeader);
            j.setMsg("删除销售订单成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除销售订单
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omSaleHeaderService.delete(omSaleHeaderService.get(id));
            }
            j.setMsg("删除销售订单成功");
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
    @RequiresPermissions("order:omSaleHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmSaleHeader omSaleHeader, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "销售订单.xlsx";
            Page<OmSaleHeader> page = omSaleHeaderService.findPage(new Page<>(request, response, -1), omSaleHeader);
            new ExportExcel("销售订单", OmSaleHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出销售订单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public OmSaleHeader detail(String id) {
        return omSaleHeaderService.get(id);
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("order:omSaleHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmSaleHeader> list = ei.getDataList(OmSaleHeader.class);
            for (OmSaleHeader omSaleHeader : list) {
                try {
                    omSaleHeaderService.checkAndSave(omSaleHeader);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条销售订单记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条销售订单记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入销售订单失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/order/omSaleHeader/?repage";
    }

    /**
     * 下载导入销售订单数据模板
     */
    @RequiresPermissions("order:omSaleHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "销售订单数据导入模板.xlsx";
            List<OmSaleHeader> list = Lists.newArrayList();
            new ExportExcel("销售订单数据", OmSaleHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/order/omSaleHeader/?repage";
    }

    /**
     * 描述：审核
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omSaleHeaderService.audit(id);
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
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omSaleHeaderService.cancelAudit(id);
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
     * 描述：生成供应链订单
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:createChainOrder")
    @RequestMapping(value = "createChainOrder")
    public AjaxJson createChainOrder(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omSaleHeaderService.createChainOrder(id);
            }

            j.setSuccess(true);
            j.setMsg("生成供应链订单成功");
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
     * 描述：弹出窗数据
     * <p>
     * create by Jianhua on 2019/7/29
     */
    @ResponseBody
    @RequestMapping(value = "getUnAssociatedPoData")
    public Map<String, Object> getUnAssociatedPoData(OmSaleHeaderEntity omSaleHeaderEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omSaleHeaderService.getUnAssociatedPoData(new Page<OmSaleHeaderEntity>(request, response), omSaleHeaderEntity));
    }

    /**
     * 描述：上传附件
     * <p>
     * create by Jianhua on 2019/7/31
     */
    @ResponseBody
    @RequestMapping("uploadAnnex")
    public AjaxJson uploadAnnex(MultipartFile file, @RequestParam String id, @RequestParam String annexType, String oldAnnex) {
        AjaxJson j = new AjaxJson();

        if (file != null) {
            try {
                String path = ".." + File.separator + "annex";
                String fileName = path + File.separator + annexType + "_" + id + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if (StringUtils.isNotBlank(oldAnnex)) {
                    FileUtils.deleteFile(oldAnnex);
                }
                // 上传目录
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file.transferTo(new File(fileName));

                if ("appAnnex".equals(annexType)) {
                    omSaleHeaderService.updateAppAnnex(id, fileName);
                } else if ("annex".equals(annexType)) {
                    omSaleHeaderService.updateAnnex(id, fileName);
                }

                LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
                rsMap.put("annex", fileName);
                j.setBody(rsMap);
                j.setSuccess(true);
                j.setMsg("上传成功");
            } catch (IOException e) {
                logger.error("", e);
                j.setSuccess(false);
                j.setMsg("上传失败");
            }
        } else {
            j.setSuccess(false);
            j.setMsg("请选择需要上传的附件");
        }
        return j;
    }

    /**
     * 描述：下载附件
     * <p>
     * create by Jianhua on 2019/7/31
     */
    @ResponseBody
    @RequestMapping("downloadAnnex")
    public void downloadAnnex(String json, HttpServletResponse response) {

    }

    /**
     * 描述：整单折扣
     * <p>
     * create by Jianhua on 2019/7/31
     */
    @ResponseBody
    @RequestMapping("discount")
    public AjaxJson discount(@RequestParam String id, @RequestParam Double discountRate) {
        AjaxJson j = new AjaxJson();

        if (discountRate <= 0 || discountRate > 1) {
            j.setSuccess(false);
            j.setMsg("折扣率须大于0且小于等于1");
            return j;
        }
        try {
            OmSaleHeaderEntity entity = omSaleHeaderService.discount(id, discountRate);

            LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
            rsMap.put("entity", entity);
            j.setBody(rsMap);
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印发货清单
     */
    @RequestMapping(value = "/printShipOrder")
    public String printShipOrder(Model model, String ids) {
        List<OmShipOrderLabel> result = omSaleHeaderService.getShipOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/omsShipOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }


    /**
     * 描述：修改备注信息
     *
     * @author zyf on 2019/8/2
     */
    @ResponseBody
    @RequestMapping(value = "editRemarks")
    public AjaxJson editRemarks(@RequestParam String id, @RequestParam String remarks) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(id)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }
        try {
            omSaleHeaderService.editRemarks(id, remarks);
            j.setSuccess(true);
            j.setMsg("修改备注成功");
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：关闭订单
     *
     * @author zyf on 2019/8/13
     */
    @ResponseBody
    @RequiresPermissions("order:omSaleHeader:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omSaleHeaderService.closeOrder(id);
            }

            j.setSuccess(true);
            j.setMsg("操作成功");
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

}