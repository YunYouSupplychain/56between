package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdHeaderEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmAdHeaderExportEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintAdOrder;
import com.yunyou.modules.wms.inventory.service.BanQinWmAdHeaderService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 调整单Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmAdHeader")
public class BanQinWmAdHeaderController extends BaseController {

    @Autowired
    private BanQinWmAdHeaderService banQinWmAdHeaderService;

    @ModelAttribute
    public BanQinWmAdHeaderEntity get(@RequestParam(required = false) String id) {
        BanQinWmAdHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmAdHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmAdHeaderEntity();
        }
        return entity;
    }

    /**
     * 调整单列表页面
     */
    @RequiresPermissions("inventory:banQinWmAdHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmAdHeaderList";
    }

    /**
     * 调整单列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAdHeaderEntity banQinWmAdHeaderEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        return getBootstrapData(banQinWmAdHeaderService.findPage(new Page<BanQinWmAdHeaderEntity>(request, response), banQinWmAdHeaderEntity));
    }

    /**
     * 查看，增加，编辑调整单表单页面
     */
    @RequiresPermissions(value = {"inventory:banQinWmAdHeader:view", "inventory:banQinWmAdHeader:add", "inventory:banQinWmAdHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmAdHeaderEntity banQinWmAdHeaderEntity, Model model) {
        model.addAttribute("banQinWmAdHeaderEntity", banQinWmAdHeaderEntity);
        if (StringUtils.isBlank(banQinWmAdHeaderEntity.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/inventory/banQinWmAdHeaderForm";
    }

    /**
     * 保存调整单
     */
    @ResponseBody
    @RequiresPermissions(value = {"inventory:banQinWmAdHeader:add", "inventory:banQinWmAdHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmAdHeaderEntity banQinWmAdHeaderEntity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmAdHeaderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            BanQinWmAdHeaderEntity entity = banQinWmAdHeaderService.saveEntity(banQinWmAdHeaderEntity);
            j.setMsg("保存成功");
            j.put("entity", entity);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除调整单
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.removeAdEntity(ids.split(","));
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
    @RequiresPermissions("inventory:banQinWmAdHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmAdHeader banQinWmAdHeader, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinWmAdHeader> page = banQinWmAdHeaderService.findPage(new Page<>(request, response, -1), banQinWmAdHeader);
            new ExportExcel("", BanQinWmAdHeaderExportEntity.class).setDataList(page.getList()).write(response, "调整单.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出调整单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 审核
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdHeader:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.auditAd(ids);
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
    @RequiresPermissions("inventory:banQinWmAdHeader:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.cancelAuditAd(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 执行调整
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdHeader:adjust")
    @RequestMapping(value = "adjust")
    public AjaxJson adjust(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.adjust(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 关闭订单
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdHeader:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.closeAdOrder(ids);
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
    @RequiresPermissions("inventory:banQinWmAdHeader:cancelOrder")
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancelOrder(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.cancelAdOrder(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印调整单
     */
    @RequestMapping(value = "/printAdOrder", method = RequestMethod.POST)
    @RequiresPermissions("inventory:banQinWmAdHeader:printAdOrder")
    public String printAdOrder(Model model, String ids) {
        List<PrintAdOrder> result = banQinWmAdHeaderService.getAdOrder(ids.split(","));

        model.addAttribute("url", "classpath:/jasper/wm_ad_order.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}