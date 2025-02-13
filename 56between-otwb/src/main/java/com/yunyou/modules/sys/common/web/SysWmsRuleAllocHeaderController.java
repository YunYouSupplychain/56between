package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocHeader;
import com.yunyou.modules.sys.common.service.SysWmsRuleAllocHeaderService;
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
 * 分配规则Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleAllocHeader")
public class SysWmsRuleAllocHeaderController extends BaseController {
    @Autowired
    private SysWmsRuleAllocHeaderService sysWmsRuleAllocHeaderService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsRuleAllocHeader get(@RequestParam(required = false) String id) {
        SysWmsRuleAllocHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsRuleAllocHeaderService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsRuleAllocHeader();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:ruleAlloc:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsRuleAllocHeaderList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleAlloc:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRuleAllocHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleAllocHeaderService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:ruleAlloc:view", "sys:common:wms:ruleAlloc:add", "sys:common:wms:ruleAlloc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsRuleAllocHeader sysWmsRuleAllocHeader, Model model) {
        model.addAttribute("sysCommonRuleAllocHeader", sysWmsRuleAllocHeader);
        return "modules/sys/common/sysWmsRuleAllocHeaderForm";
    }

    /**
     * 保存
     */
    @RequiresPermissions(value = {"sys:common:wms:ruleAlloc:add", "sys:common:wms:ruleAlloc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(SysWmsRuleAllocHeader sysWmsRuleAllocHeader, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysWmsRuleAllocHeader)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        try {
            sysWmsRuleAllocHeaderService.save(sysWmsRuleAllocHeader);
            j.put("entity", sysWmsRuleAllocHeader);
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
    @RequiresPermissions("sys:common:wms:ruleAlloc:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleAllocHeaderService.delete(sysWmsRuleAllocHeaderService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleAlloc:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysWmsRuleAllocHeaderService.findSync(new SysWmsRuleAllocHeader(id)).forEach(syncPlatformDataToWmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleAlloc:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsRuleAllocHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsRuleAllocHeaderService.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysWmsRuleAllocHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleAllocHeaderService.findGrid(new Page<>(request, response), entity));
    }

}