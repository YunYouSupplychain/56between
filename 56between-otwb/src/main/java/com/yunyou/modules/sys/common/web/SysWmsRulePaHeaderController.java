package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaHeader;
import com.yunyou.modules.sys.common.service.SysWmsRulePaHeaderService;
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
 * 上架规则Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/rulePaHeader")
public class SysWmsRulePaHeaderController extends BaseController {
    @Autowired
    private SysWmsRulePaHeaderService sysWmsRulePaHeaderService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsRulePaHeader get(@RequestParam(required = false) String id) {
        SysWmsRulePaHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsRulePaHeaderService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsRulePaHeader();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:rulePa:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsRulePaHeaderList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:rulePa:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRulePaHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRulePaHeaderService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:rulePa:view", "sys:common:wms:rulePa:add", "sys:common:wms:rulePa:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsRulePaHeader sysWmsRulePaHeader, Model model) {
        model.addAttribute("sysWmsRulePaHeader", sysWmsRulePaHeader);
        return "modules/sys/common/sysWmsRulePaHeaderForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:rulePa:add", "sys:common:wms:rulePa:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRulePaHeader sysWmsRulePaHeader, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysWmsRulePaHeader)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsRulePaHeaderService.save(sysWmsRulePaHeader);
            j.put("entity", sysWmsRulePaHeader);
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
    @RequiresPermissions("sys:common:wms:rulePa:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRulePaHeaderService.delete(sysWmsRulePaHeaderService.get(id));
        }
        j.setMsg("删除成功！");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:rulePa:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysWmsRulePaHeaderService.findSync(new SysWmsRulePaHeader(id)).forEach(syncPlatformDataToWmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:rulePa:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsRulePaHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsRulePaHeaderService.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
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
    public Map<String, Object> grid(SysWmsRulePaHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRulePaHeaderService.findGrid(new Page<>(request, response), entity));
    }

}