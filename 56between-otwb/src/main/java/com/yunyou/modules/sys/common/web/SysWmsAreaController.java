package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsArea;
import com.yunyou.modules.sys.common.service.SysWmsAreaService;
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
 * 仓库区域Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/area")
public class SysWmsAreaController extends BaseController {
    @Autowired
    private SysWmsAreaService sysWmsAreaService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsArea get(@RequestParam(required = false) String id) {
        SysWmsArea entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsAreaService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsArea();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:area:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsAreaList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:area:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsArea entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsAreaService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:area:view", "sys:common:wms:area:add", "sys:common:wms:area:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsArea entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/sys/common/sysWmsAreaForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:area:add", "sys:common:wms:area:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsArea entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsAreaService.save(entity);
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
     * 删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:area:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsAreaService.delete(sysWmsAreaService.get(id));
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:area:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToWmsAction.sync(sysWmsAreaService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:area:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsArea entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsAreaService.findList(entity).forEach(syncPlatformDataToWmsAction::sync);
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
    public Map<String, Object> grid(SysWmsArea entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsAreaService.findGrid(new Page<>(request, response), entity));
    }

}