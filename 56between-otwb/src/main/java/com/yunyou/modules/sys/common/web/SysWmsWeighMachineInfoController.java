package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsWeighMachineInfo;
import com.yunyou.modules.sys.common.entity.extend.SysWmsWeighMachineInfoEntity;
import com.yunyou.modules.sys.common.service.SysWmsWeighMachineInfoService;
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
 * 称重设备Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/weighMachineInfo")
public class SysWmsWeighMachineInfoController extends BaseController {
    @Autowired
    private SysWmsWeighMachineInfoService sysWmsWeighMachineInfoService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsWeighMachineInfoEntity get(@RequestParam(required = false) String id) {
        SysWmsWeighMachineInfoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsWeighMachineInfoService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysWmsWeighMachineInfoEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:weighMachineInfo:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsWeighMachineInfoList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:weighMachineInfo:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsWeighMachineInfoEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsWeighMachineInfoService.findPage(new Page<SysWmsWeighMachineInfoEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:weighMachineInfo:view", "sys:common:wms:weighMachineInfo:add", "sys:common:wms:weighMachineInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsWeighMachineInfoEntity sysWmsWeighMachineInfoEntity, Model model) {
        model.addAttribute("sysWmsWeighMachineInfoEntity", sysWmsWeighMachineInfoEntity);
        return "modules/sys/common/sysWmsWeighMachineInfoForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:weighMachineInfo:add", "sys:common:wms:weighMachineInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsWeighMachineInfo entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsWeighMachineInfoService.save(entity);
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
    @RequiresPermissions("sys:common:wms:weighMachineInfo:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsWeighMachineInfoService.delete(sysWmsWeighMachineInfoService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:weighMachineInfo:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToWmsAction.sync(sysWmsWeighMachineInfoService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:weighMachineInfo:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsWeighMachineInfo entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsWeighMachineInfoService.findList(entity).forEach(syncPlatformDataToWmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}