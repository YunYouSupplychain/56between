package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.common.entity.SysOmsClerk;
import com.yunyou.modules.sys.common.service.SysOmsClerkService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 业务员Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/oms/clerk")
public class SysOmsClerkController extends BaseController {
    @Autowired
    private SysOmsClerkService sysOmsClerkService;
    @Autowired
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:oms:clerk:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysOmsClerk", new SysOmsClerk());
        return "modules/sys/common/sysOmsClerkList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:oms:clerk:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysOmsClerk entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysOmsClerkService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:oms:clerk:view", "sys:common:oms:clerk:add", "sys:common:oms:clerk:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysOmsClerk entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = sysOmsClerkService.get(entity.getId());
        }
        model.addAttribute("sysOmsClerk", entity);
        return "modules/sys/common/sysOmsClerkForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:oms:clerk:add", "sys:common:oms:clerk:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysOmsClerk sysOmsClerk, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysOmsClerk)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysOmsClerkService.save(sysOmsClerk);
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
    @RequiresPermissions("sys:common:oms:clerk:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysOmsClerkService.delete(sysOmsClerkService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:oms:clerk:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToOmsAction.sync(sysOmsClerkService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:oms:clerk:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysOmsClerk entity) {
        AjaxJson j = new AjaxJson();
        try {
            sysOmsClerkService.findList(entity).forEach(syncPlatformDataToOmsAction::sync);
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
    public Map<String, Object> grid(SysOmsClerk entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysOmsClerkService.findGrid(new Page<>(request, response), entity));
    }

}