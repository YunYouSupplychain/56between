package com.yunyou.modules.oms.order.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmPoHeader;
import com.yunyou.modules.oms.order.entity.OmPoHeaderEntity;
import com.yunyou.modules.oms.order.entity.extend.OmPoPrintData;
import com.yunyou.modules.oms.order.service.OmPoHeaderService;
import com.yunyou.modules.oms.order.service.OmSaleHeaderService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 采购订单Controller
 *
 * @author WMJ
 * @version 2019-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/order/omPoHeader")
public class OmPoHeaderController extends BaseController {
    @Autowired
    private OmPoHeaderService omPoHeaderService;
    @Autowired
    private OmSaleHeaderService omSaleHeaderService;

    @ModelAttribute
    public OmPoHeaderEntity get(@RequestParam(required = false) String id) {
        OmPoHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omPoHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmPoHeaderEntity();
        }
        return entity;
    }

    /**
     * 采购订单列表页面
     */
    @RequiresPermissions("order:omPoHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/order/omPoHeaderList";
    }

    /**
     * 采购订单列表数据
     */
    @ResponseBody
    @RequiresPermissions("order:omPoHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmPoHeaderEntity omPoHeaderEntity, HttpServletRequest request, HttpServletResponse response) {
        Page<OmPoHeaderEntity> page = omPoHeaderService.findPage(new Page<OmPoHeaderEntity>(request, response), omPoHeaderEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑采购订单表单页面
     */
    @RequiresPermissions(value = {"order:omPoHeader:view", "order:omPoHeader:add", "order:omPoHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmPoHeaderEntity omPoHeaderEntity, Model model) {
        model.addAttribute("omPoHeaderEntity", omPoHeaderEntity);
        return "modules/oms/order/omPoHeaderForm";
    }

    /**
     * 保存采购订单
     */
    @ResponseBody
    @RequiresPermissions(value = {"order:omPoHeader:add", "order:omPoHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmPoHeaderEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omPoHeaderService.checkAndSave(entity);
            if (StringUtils.isNotBlank(entity.getSaleOrderNos())) {
                String[] saleOrderNos = entity.getSaleOrderNos().split(",");
                omSaleHeaderService.associatedPo(saleOrderNos, entity.getPoNo(), entity.getOrgId());
            } else {
                omSaleHeaderService.unAssociatedPo(entity.getPoNo(), entity.getOrgId());
            }

            j.setSuccess(true);
            j.setMsg("保存采购订单成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除采购订单
     */
    @ResponseBody
    @RequiresPermissions("order:omPoHeader:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(OmPoHeader omPoHeader) {
        AjaxJson j = new AjaxJson();
        try {
            omPoHeaderService.delete(omPoHeader);
            j.setMsg("删除采购订单成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除采购订单
     */
    @ResponseBody
    @RequiresPermissions("order:omPoHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                omPoHeaderService.delete(omPoHeaderService.get(id));
            }
            j.setMsg("删除采购订单成功");
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
    @RequiresPermissions("order:omPoHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmPoHeader omPoHeader, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "采购订单.xlsx";
            Page<OmPoHeader> page = omPoHeaderService.findPage(new Page<OmPoHeader>(request, response, -1), omPoHeader);
            new ExportExcel("采购订单", OmPoHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出采购订单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public OmPoHeader detail(String id) {
        return omPoHeaderService.get(id);
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("order:omPoHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmPoHeader> list = ei.getDataList(OmPoHeader.class);
            for (OmPoHeader omPoHeader : list) {
                try {
                    omPoHeaderService.checkAndSave(omPoHeader);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条采购订单记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条采购订单记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入采购订单失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/order/omPoHeader/?repage";
    }

    /**
     * 下载导入采购订单数据模板
     */
    @RequiresPermissions("order:omPoHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "采购订单数据导入模板.xlsx";
            List<OmPoHeader> list = Lists.newArrayList();
            new ExportExcel("采购订单数据", OmPoHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/order/omPoHeader/?repage";
    }

    /**
     * 描述：审核
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omPoHeader:audit")
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
                omPoHeaderService.audit(id);
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
    @RequiresPermissions("order:omPoHeader:cancelAudit")
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
                omPoHeaderService.cancelAudit(id);
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
    @RequiresPermissions("order:omPoHeader:createChainOrder")
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
                omPoHeaderService.createChainOrder(id);
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
     * 描述：打印采购订单
     * <p>
     * create by Jianhua on 2019/8/9
     */
    @RequestMapping(value = "/printPo")
    public String printPo(String ids, Model model) {
        List<OmPoPrintData> result = omPoHeaderService.getPoPrintData(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/omsPurchaseOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：关闭订单
     *
     * @author zyf on 2019/8/13
     */
    @ResponseBody
    @RequiresPermissions("order:omPoHeader:closeOrder")
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
                omPoHeaderService.closeOrder(id);
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