package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmMvHeader;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvDetailEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintMvOrder;
import com.yunyou.modules.wms.inventory.service.BanQinWmMvDetailService;
import com.yunyou.modules.wms.inventory.service.BanQinWmMvHeaderService;
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
 * 库存移动单Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmMv")
public class BanQinWmMvController extends BaseController {
    @Autowired
    private BanQinWmMvHeaderService wmMvService;
    @Autowired
    private BanQinWmMvDetailService wmMvDetailService;

    @ModelAttribute
    public BanQinWmMvEntity get(@RequestParam(required = false) String id) {
        BanQinWmMvEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wmMvService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmMvEntity();
        }
        return entity;
    }

    @RequiresPermissions("inventory:banQinWmMv:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmMvList";
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmMvEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmMvService.findPage(new Page<BanQinWmMvEntity>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:list")
    @RequestMapping(value = "/detail/data")
    public Map<String, Object> dataDetail(BanQinWmMvDetailEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmMvDetailService.findPage(new Page<BanQinWmMvDetailEntity>(request, response), qEntity));
    }

    @RequiresPermissions(value = {"inventory:banQinWmMv:view", "inventory:banQinWmMv:add", "inventory:banQinWmMv:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmMvEntity wmMvEntity, Model model) {
        model.addAttribute("banQinWmMvEntity", wmMvEntity);
        return "modules/wms/inventory/banQinWmMvForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"inventory:banQinWmMv:add", "inventory:banQinWmMv:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmMvHeader wmMvHeader, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, wmMvHeader)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            wmMvService.saveValidator(wmMvHeader);
            j.put("id", wmMvService.saveHeader(wmMvHeader));
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions(value = {"inventory:banQinWmMvDetail:add", "inventory:banQinWmMvDetail:edit"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson saveDetail(@RequestBody BanQinWmMvDetailEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        try {
            wmMvDetailService.saveValidator(entity);
            wmMvDetailService.save(entity);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvService.remove(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-删除", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMvDetail:del")
    @RequestMapping(value = "/detail/deleteAll")
    public AjaxJson deleteDetail(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvDetailService.remove(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-删除", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvService.audit(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-审核", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvService.cancelAudit(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-取消审核", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:executeMv")
    @RequestMapping(value = "executeMv")
    public AjaxJson executeMv(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvService.executeMv(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-执行移动", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMvDetail:executeMv")
    @RequestMapping(value = "/detail/executeMv")
    public AjaxJson executeMvDetail(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvDetailService.executeMv(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-执行移动", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson close(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvService.close(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-关闭", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:cancelOrder")
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancel(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvService.cancel(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-取消", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMvDetail:cancelDetail")
    @RequestMapping(value = "/detail/cancel")
    public AjaxJson cancelDetail(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                wmMvDetailService.cancel(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("库存移动单-取消", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmMv:export")
    @RequestMapping(value = "export")
    public AjaxJson exportExcel(BanQinWmMvEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();

        return j;
    }

    /**
     * 打印移动单
     */
    @RequestMapping(value = "/printMvOrder", method = RequestMethod.POST)
    @RequiresPermissions("inventory:banQinWmAdHeader:printMvOrder")
    public String printMvOrder(Model model, String ids) {
        List<PrintMvOrder> result = wmMvService.getMvOrder(ids.split(","));

        model.addAttribute("url", "classpath:/jasper/wm_mv_order.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}