package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsBusinessRoute;
import com.yunyou.modules.sys.common.entity.extend.SysTmsBusinessRouteEntity;
import com.yunyou.modules.sys.common.service.SysTmsBusinessRouteService;
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
 * 业务路线Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/businessRoute")
public class SysTmsBusinessRouteController extends BaseController {
    @Autowired
    private SysTmsBusinessRouteService sysTmsBusinessRouteService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @ModelAttribute
    public SysTmsBusinessRouteEntity get(@RequestParam(required = false) String id) {
        SysTmsBusinessRouteEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsBusinessRouteService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsBusinessRouteEntity();
        }
        return entity;
    }

    @RequiresPermissions("sys:common:tms:businessRoute:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsBusinessRouteList";
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:businessRoute:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsBusinessRouteEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsBusinessRouteService.findPage(new Page<SysTmsBusinessRouteEntity>(request, response), entity));
    }

    @RequiresPermissions(value = {"sys:common:tms:businessRoute:view", "sys:common:tms:businessRoute:add", "sys:common:tms:businessRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsBusinessRouteEntity entity, Model model) {
        model.addAttribute("tmBusinessRouteEntity", entity);
        return "modules/sys/common/sysTmsBusinessRouteForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:businessRoute:add", "sys:common:tms:businessRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsBusinessRouteEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsBusinessRouteService.save(entity);
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

    @ResponseBody
    @RequiresPermissions("sys:common:tms:businessRoute:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsBusinessRoute sysTmsBusinessRoute = sysTmsBusinessRouteService.get(id);
            try {
                sysTmsBusinessRouteService.delete(sysTmsBusinessRoute);
            } catch (Exception e) {
                logger.error("删除业务路线id=[" + id + "]", e);
                errMsg.append("<br>").append("业务路线[").append(sysTmsBusinessRoute.getCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:businessRoute:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToTmsAction.sync(sysTmsBusinessRouteService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:businessRoute:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsBusinessRoute entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsBusinessRouteService.findList(entity).forEach(syncPlatformDataToTmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysTmsBusinessRouteEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsBusinessRouteService.findGrid(new Page<SysTmsBusinessRouteEntity>(request, response), entity));
    }
}
