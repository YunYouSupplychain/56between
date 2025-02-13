package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsLotHeader;
import com.yunyou.modules.sys.common.service.SysWmsLotHeaderService;
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
 * 批次属性Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/lotHeader")
public class SysWmsLotHeaderController extends BaseController {
    @Autowired
    private SysWmsLotHeaderService sysWmsLotHeaderService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsLotHeader get(@RequestParam(required = false) String id) {
        SysWmsLotHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsLotHeaderService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsLotHeader();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:lot:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsLotHeaderList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:lot:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsLotHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsLotHeaderService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:lot:view", "sys:common:wms:lot:add", "sys:common:wms:lot:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsLotHeader entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/sys/common/sysWmsLotHeaderForm";
    }

    /**
     * 保存
     */
    @RequiresPermissions(value = {"sys:common:wms:lot:add", "sys:common:wms:lot:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(SysWmsLotHeader entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            sysWmsLotHeaderService.save(entity);
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
    @RequiresPermissions("sys:common:wms:lot:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsLotHeaderService.delete(sysWmsLotHeaderService.get(id));
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:lot:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysWmsLotHeaderService.findSync(new SysWmsLotHeader(id)).forEach(syncPlatformDataToWmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:lot:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsLotHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsLotHeaderService.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
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
    public Map<String, Object> grid(SysWmsLotHeader entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsLotHeaderService.findGrid(new Page<>(request, response), entity));
    }


}