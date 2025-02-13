package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationHeader;
import com.yunyou.modules.sys.common.service.SysWmsRuleRotationHeaderService;
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
 * 库存周转规则Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleRotationHeader")
public class SysWmsRuleRotationHeaderController extends BaseController {
    @Autowired
    private SysWmsRuleRotationHeaderService sysWmsRuleRotationHeaderService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsRuleRotationHeader get(@RequestParam(required = false) String id) {
        SysWmsRuleRotationHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsRuleRotationHeaderService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsRuleRotationHeader();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:ruleRotation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsRuleRotationHeaderList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleRotation:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRuleRotationHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleRotationHeaderService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:ruleRotation:view", "sys:common:wms:ruleRotation:add", "sys:common:wms:ruleRotation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsRuleRotationHeader sysWmsRuleRotationHeaderEntity, Model model) {
        model.addAttribute("sysCommonRuleRotationHeaderEntity", sysWmsRuleRotationHeaderEntity);
        return "modules/sys/common/sysWmsRuleRotationHeaderForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:ruleRotation:add", "sys:common:wms:ruleRotation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRuleRotationHeader sysWmsRuleRotationHeader, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysWmsRuleRotationHeader)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsRuleRotationHeaderService.save(sysWmsRuleRotationHeader);
            j.put("entity", sysWmsRuleRotationHeader);
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
    @RequiresPermissions("sys:common:wms:ruleRotation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleRotationHeaderService.delete(sysWmsRuleRotationHeaderService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleRotation:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysWmsRuleRotationHeaderService.findSync(new SysWmsRuleRotationHeader(id)).forEach(syncPlatformDataToWmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleRotation:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsRuleRotationHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsRuleRotationHeaderService.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
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
    public Map<String, Object> grid(SysWmsRuleRotationHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleRotationHeaderService.findGrid(new Page<>(request, response), entity));
    }

}