package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysCommonPackage;
import com.yunyou.modules.sys.common.service.SysCommonPackageService;
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
import java.util.Arrays;
import java.util.Map;

/**
 * 包装Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/package")
public class SysCommonPackageController extends BaseController {
    @Autowired
    private SysCommonPackageService sysCommonPackageService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    @ModelAttribute
    public SysCommonPackage get(@RequestParam(required = false) String id) {
        SysCommonPackage entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysCommonPackageService.get(id);
        }
        if (entity == null) {
            entity = new SysCommonPackage();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:package:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysCommonPackageList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:package:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysCommonPackage entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonPackageService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:package:view", "sys:common:package:add", "sys:common:package:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysCommonPackage sysCommonPackage, Model model) {
        model.addAttribute("sysCommonPackage", sysCommonPackage);
        return "modules/sys/common/sysCommonPackageForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:package:add", "sys:common:package:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysCommonPackage sysCommonPackage, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysCommonPackage)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysCommonPackageService.save(sysCommonPackage);
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
    @RequiresPermissions("sys:common:package:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonPackageService.delete(sysCommonPackageService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:package:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            SysCommonPackage entity = new SysCommonPackage();
            entity.setIds(Arrays.asList(ids.split(",")));
            syncPlatformDataCommonAction.syncPackage(sysCommonPackageService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:package:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysCommonPackage entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            syncPlatformDataCommonAction.syncPackage(sysCommonPackageService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonPackage entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonPackageService.findGrid(new Page<>(request, response), entity));
    }

}