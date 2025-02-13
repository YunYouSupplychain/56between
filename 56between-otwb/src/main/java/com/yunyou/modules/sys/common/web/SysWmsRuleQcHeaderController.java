package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcHeader;
import com.yunyou.modules.sys.common.service.SysWmsRuleQcHeaderService;
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
 * 质检规则Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleQcHeader")
public class SysWmsRuleQcHeaderController extends BaseController {
    @Autowired
    private SysWmsRuleQcHeaderService sysWmsRuleQcHeaderService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsRuleQcHeader get(@RequestParam(required = false) String id) {
        SysWmsRuleQcHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsRuleQcHeaderService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsRuleQcHeader();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:ruleQc:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsRuleQcHeaderList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleQc:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRuleQcHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleQcHeaderService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:ruleQc:view", "sys:common:wms:ruleQc:add", "sys:common:wms:ruleQc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsRuleQcHeader sysWmsRuleQcHeader, Model model) {
        model.addAttribute("sysCommonRuleQcHeader", sysWmsRuleQcHeader);
        return "modules/sys/common/sysWmsRuleQcHeaderForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:ruleQc:add", "sys:common:wms:ruleQc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRuleQcHeader sysWmsRuleQcHeader, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysWmsRuleQcHeader)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsRuleQcHeaderService.save(sysWmsRuleQcHeader);
            j.put("entity", sysWmsRuleQcHeader);
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
    @RequiresPermissions("sys:common:wms:ruleQc:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleQcHeaderService.delete(sysWmsRuleQcHeaderService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleQc:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysWmsRuleQcHeaderService.findSync(new SysWmsRuleQcHeader(id)).forEach(syncPlatformDataToWmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleQc:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsRuleQcHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsRuleQcHeaderService.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
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
    public Map<String, Object> grid(SysWmsRuleQcHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleQcHeaderService.findGrid(new Page<>(request, response), entity));
    }

}