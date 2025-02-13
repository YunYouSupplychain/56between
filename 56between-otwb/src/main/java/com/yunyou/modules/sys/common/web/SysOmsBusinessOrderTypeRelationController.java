package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.common.entity.SysOmsBusinessOrderTypeRelation;
import com.yunyou.modules.sys.common.service.SysOmsBusinessOrderTypeRelationService;
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
 * 业务类型-订单类型关联关系Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/oms/businessOrderTypeRelation")
public class SysOmsBusinessOrderTypeRelationController extends BaseController {
    @Autowired
    private SysOmsBusinessOrderTypeRelationService sysOmsBusinessOrderTypeRelationService;
    @Autowired
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;

    @ModelAttribute
    public SysOmsBusinessOrderTypeRelation get(@RequestParam(required = false) String id) {
        SysOmsBusinessOrderTypeRelation entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysOmsBusinessOrderTypeRelationService.get(id);
        }
        if (entity == null) {
            entity = new SysOmsBusinessOrderTypeRelation();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:oms:businessOrderTypeRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysOmsBusinessOrderTypeRelationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:oms:businessOrderTypeRelation:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysOmsBusinessOrderTypeRelation entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysOmsBusinessOrderTypeRelationService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 表单页面
     */
    @RequiresPermissions(value = {"sys:common:oms:businessOrderTypeRelation:view", "sys:common:oms:businessOrderTypeRelation:add", "sys:common:oms:businessOrderTypeRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysOmsBusinessOrderTypeRelation sysOmsBusinessOrderTypeRelation, Model model) {
        model.addAttribute("sysOmsBusinessOrderTypeRelation", sysOmsBusinessOrderTypeRelation);
        return "modules/sys/common/sysOmsBusinessOrderTypeRelationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:oms:businessOrderTypeRelation:add", "sys:common:oms:businessOrderTypeRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysOmsBusinessOrderTypeRelation sysOmsBusinessOrderTypeRelation, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysOmsBusinessOrderTypeRelation)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysOmsBusinessOrderTypeRelationService.save(sysOmsBusinessOrderTypeRelation);
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
    @RequiresPermissions("sys:common:oms:businessOrderTypeRelation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysOmsBusinessOrderTypeRelationService.delete(sysOmsBusinessOrderTypeRelationService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:oms:businessOrderTypeRelation:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToOmsAction.sync(sysOmsBusinessOrderTypeRelationService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:oms:businessOrderTypeRelation:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysOmsBusinessOrderTypeRelation entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysOmsBusinessOrderTypeRelationService.findList(entity).forEach(syncPlatformDataToOmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}