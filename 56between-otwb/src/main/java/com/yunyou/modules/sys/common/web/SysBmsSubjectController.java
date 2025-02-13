package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.sys.common.entity.SysBmsSubject;
import com.yunyou.modules.sys.common.entity.extend.SysBmsSubjectEntity;
import com.yunyou.modules.sys.common.service.SysBmsSubjectService;
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
 * 费用科目Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/bms/subject")
public class SysBmsSubjectController extends BaseController {
    @Autowired
    private SysBmsSubjectService sysBmsSubjectService;
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @ModelAttribute
    public SysBmsSubject get(@RequestParam(required = false) String id) {
        SysBmsSubject entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysBmsSubjectService.get(id);
        }
        if (entity == null) {
            entity = new SysBmsSubject();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:bms:subject:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysBmsSubjectList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:bms:subject:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysBmsSubjectEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsSubjectService.findPage(new Page<SysBmsSubjectEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:bms:subject:view", "sys:common:bms:subject:add", "sys:common:bms:subject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysBmsSubject entity, Model model) {
        model.addAttribute("sysBmsSubject", entity);
        return "modules/sys/common/sysBmsSubjectForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:bms:subject:add", "sys:common:bms:subject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysBmsSubject sysBmsSubject, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysBmsSubject)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysBmsSubjectService.save(sysBmsSubject);
            j.put("entity", sysBmsSubject);
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
    @RequiresPermissions("sys:common:bms:subject:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysBmsSubjectService.delete(sysBmsSubjectService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:subject:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToBmsAction.sync(sysBmsSubjectService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:subject:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysBmsSubject entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysBmsSubjectService.findList(entity).forEach(syncPlatformDataToBmsAction::sync);
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
    public Map<String, Object> grid(SysBmsSubjectEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsSubjectService.findGrid(new Page<SysBmsSubjectEntity>(request, response), entity));
    }

}