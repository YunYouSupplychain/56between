package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysCommonDataSet;
import com.yunyou.modules.sys.common.entity.extend.SysCommonDataSetEntity;
import com.yunyou.modules.sys.common.service.SysCommonDataSetService;
import com.yunyou.modules.sys.common.service.SysCommonDefaultDataService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

/**
 * 数据套Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/dataSet")
public class SysCommonDataSetController extends BaseController {
    @Autowired
    private SysCommonDataSetService sysCommonDataSetService;
    @Autowired
    private SysCommonDefaultDataService sysCommonDefaultDataService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:dataSet:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysCommonDataSet", new SysCommonDataSet());
        return "modules/sys/common/sysCommonDataSetList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:dataSet:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysCommonDataSet sysCommonDataSet, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(sysCommonDataSetService.findPage(new Page<>(request, response), sysCommonDataSet));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:dataSet:view", "sys:common:dataSet:add", "sys:common:dataSet:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysCommonDataSetEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = sysCommonDataSetService.getEntity(entity.getId());
        }
        model.addAttribute("sysCommonDataSetEntity", entity);
        return "modules/sys/common/sysCommonDataSetForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:dataSet:add", "sys:common:dataSet:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid SysCommonDataSetEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            // 创建数据套时生成默认平台数据资料
            if (StringUtils.isBlank(entity.getId())) {
                sysCommonDefaultDataService.genDefaultData(entity.getCode());
            }
            sysCommonDataSetService.save(entity);
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
    @RequiresPermissions("sys:common:dataSet:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonDataSetService.delete(sysCommonDataSetService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    /**
     * 设置默认
     */
    @ResponseBody
    @RequiresPermissions("sys:common:dataSet:setDefault")
    @RequestMapping(value = "setDefault")
    public AjaxJson setDefault(String id) {
        AjaxJson j = new AjaxJson();
        try {
            sysCommonDataSetService.setDefault(id);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequiresPermissions(value = "sys:common:dataSet:sync")
    @RequestMapping(value = "sync/form")
    public String syncForm(String id, Model model) {
        model.addAttribute("sysCommonDataSet", sysCommonDataSetService.get(id));
        return "modules/sys/common/sysCommonDataSetSyncForm";
    }

    @ResponseBody
    @RequiresPermissions(value = "sys:common:dataSet:sync")
    @RequestMapping(value = "sync/confirm")
    public AjaxJson syncAll(String systems, String dataSet, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            syncPlatformDataCommonAction.syncAll(Arrays.asList(systems.split(",")), dataSet, orgId);
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonDataSet sysCommonDataSet, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(sysCommonDataSetService.findGrid(new Page<>(request, response), sysCommonDataSet));
    }

    @ResponseBody
    @RequestMapping(value = "getDefault")
    public SysCommonDataSet getDefault() {
        return sysCommonDataSetService.getDefault();
    }
}