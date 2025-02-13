package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.sys.common.entity.SysBmsInvoiceObject;
import com.yunyou.modules.sys.common.service.SysBmsInvoiceObjectService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 开票对象Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/bms/invoiceObject")
public class SysBmsInvoiceObjectController extends BaseController {
    @Autowired
    private SysBmsInvoiceObjectService sysBmsInvoiceObjectService;
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @ModelAttribute
    public SysBmsInvoiceObject get(@RequestParam(required = false) String id) {
        SysBmsInvoiceObject entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysBmsInvoiceObjectService.get(id);
        }
        if (entity == null) {
            entity = new SysBmsInvoiceObject();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:bms:invoiceObject:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysBmsInvoiceObjectList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:bms:invoiceObject:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysBmsInvoiceObject entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsInvoiceObjectService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:bms:invoiceObject:view", "sys:common:bms:invoiceObject:add", "sys:common:bms:invoiceObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysBmsInvoiceObject sysBmsInvoiceObject, Model model) {
        model.addAttribute("sysBmsInvoiceObject", sysBmsInvoiceObject);
        return "modules/sys/common/sysBmsInvoiceObjectForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:bms:invoiceObject:add", "sys:common:bms:invoiceObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysBmsInvoiceObject sysBmsInvoiceObject, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysBmsInvoiceObject)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysBmsInvoiceObjectService.save(sysBmsInvoiceObject);
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(null, e);
            }
            j.setSuccess(false);
            j.setMsg("数据已存在");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:bms:invoiceObject:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysBmsInvoiceObjectService.delete(sysBmsInvoiceObjectService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:invoiceObject:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysBmsInvoiceObjectService.findSync(new SysBmsInvoiceObject(id)).forEach(syncPlatformDataToBmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:invoiceObject:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysBmsInvoiceObject entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysBmsInvoiceObjectService.findSync(entity).forEach(syncPlatformDataToBmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysBmsInvoiceObject entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsInvoiceObjectService.findGrid(new Page<>(request, response), entity));
    }

}