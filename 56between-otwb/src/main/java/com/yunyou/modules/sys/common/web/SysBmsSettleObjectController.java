package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.sys.common.entity.SysBmsSettleObject;
import com.yunyou.modules.sys.common.service.SysBmsSettleObjectService;
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
 * 结算对象Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/bms/settleObject")
public class SysBmsSettleObjectController extends BaseController {
    @Autowired
    private SysBmsSettleObjectService sysBmsSettleObjectService;
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @ModelAttribute
    public SysBmsSettleObject get(@RequestParam(required = false) String id) {
        SysBmsSettleObject entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysBmsSettleObjectService.get(id);
        }
        if (entity == null) {
            entity = new SysBmsSettleObject();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:bms:settleObject:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysBmsSettleObjectList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:bms:settleObject:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysBmsSettleObject entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsSettleObjectService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:bms:settleObject:view", "sys:common:bms:settleObject:add", "sys:common:bms:settleObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysBmsSettleObject sysBmsSettleObject, Model model) {
        model.addAttribute("sysBmsSettleObject", sysBmsSettleObject);
        return "modules/sys/common/sysBmsSettleObjectForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:bms:settleObject:add", "sys:common:bms:settleObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysBmsSettleObject sysBmsSettleObject, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysBmsSettleObject)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysBmsSettleObjectService.save(sysBmsSettleObject);
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
    @RequiresPermissions("sys:common:bms:settleObject:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysBmsSettleObjectService.delete(sysBmsSettleObjectService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:settleObject:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToBmsAction.sync(sysBmsSettleObjectService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:settleObject:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysBmsSettleObject entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysBmsSettleObjectService.findList(entity).forEach(syncPlatformDataToBmsAction::sync);
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
    public Map<String, Object> grid(SysBmsSettleObject entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsSettleObjectService.findGrid(new Page<>(request, response), entity));
    }

}