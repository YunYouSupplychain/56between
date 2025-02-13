package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupHeader;
import com.yunyou.modules.sys.common.service.SysWmsRuleWvGroupHeaderService;
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
 * 波次规则组Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleWvGroupHeader")
public class SysWmsRuleWvGroupHeaderController extends BaseController {
    @Autowired
    private SysWmsRuleWvGroupHeaderService sysWmsRuleWvGroupHeaderService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsRuleWvGroupHeader get(@RequestParam(required = false) String id) {
        SysWmsRuleWvGroupHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsRuleWvGroupHeaderService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsRuleWvGroupHeader();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:ruleWvGroup:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsRuleWvGroupHeaderList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleWvGroup:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRuleWvGroupHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleWvGroupHeaderService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:ruleWvGroup:view", "sys:common:wms:ruleWvGroup:add", "sys:common:wms:ruleWvGroup:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsRuleWvGroupHeader sysWmsRuleWvGroupHeader, Model model) {
        model.addAttribute("cdRuleWvGroupHeader", sysWmsRuleWvGroupHeader);
        return "modules/sys/common/sysWmsRuleWvGroupHeaderForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:ruleWvGroup:add", "sys:common:wms:ruleWvGroup:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRuleWvGroupHeader sysWmsRuleWvGroupHeader, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysWmsRuleWvGroupHeader)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        try {
            sysWmsRuleWvGroupHeaderService.save(sysWmsRuleWvGroupHeader);
            j.put("entity", sysWmsRuleWvGroupHeader);
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
    @RequiresPermissions("sys:common:wms:ruleWvGroup:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        try {
            for (String id : idArray) {
                sysWmsRuleWvGroupHeaderService.delete(sysWmsRuleWvGroupHeaderService.get(id));
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleWvGroup:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysWmsRuleWvGroupHeaderService.findSync(new SysWmsRuleWvGroupHeader(id)).forEach(syncPlatformDataToWmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleWvGroup:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsRuleWvGroupHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsRuleWvGroupHeaderService.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
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
    public Map<String, Object> grid(SysWmsRuleWvGroupHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleWvGroupHeaderService.findGrid(new Page<>(request, response), entity));
    }

}