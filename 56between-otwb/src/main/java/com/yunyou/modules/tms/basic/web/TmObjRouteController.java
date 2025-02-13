package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmObjRoute;
import com.yunyou.modules.tms.basic.entity.extend.TmObjRouteEntity;
import com.yunyou.modules.tms.basic.service.TmObjRouteService;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 业务对象路由Controller
 *
 * @author liujianhua
 * @version 2021-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmObjRoute")
public class TmObjRouteController extends BaseController {
    @Autowired
    private TmObjRouteService tmObjRouteService;

    @ModelAttribute
    public TmObjRouteEntity get(@RequestParam(required = false) String id) {
        TmObjRouteEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmObjRouteService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmObjRouteEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("tms:basic:objRoute:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmObjRouteList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:objRoute:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmObjRouteEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmObjRouteService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"tms:basic:objRoute:view", "tms:basic:objRoute:add", "tms:basic:objRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmObjRouteEntity entity, Model model) {
        model.addAttribute("tmObjRouteEntity", entity);
        return "modules/tms/basic/tmObjRouteForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"tms:basic:objRoute:add", "tms:basic:objRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmObjRoute entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        TmObjRoute tmObjRoute = tmObjRouteService.getByCode(entity.getCarrierCode(), entity.getStartObjCode(), entity.getEndObjCode(), entity.getOrgId());
        if (tmObjRoute!=null&& !tmObjRoute.getId().equals(entity.getId())) {
            j.setSuccess(false);
            j.setMsg("业务对象路由已存在！");
            return j;
        }
        try {
            tmObjRouteService.save(entity);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:objRoute:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmObjRoute tmObjRoute = tmObjRouteService.get(id);
            if (!"00".equals(tmObjRoute.getAuditStatus())) {
                continue;
            }
            tmObjRouteService.delete(tmObjRoute);
        }
        return j;
    }

    /**
     * 审核
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:objRoute:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                tmObjRouteService.audit(tmObjRouteService.get(id));
            }
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 审核
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:objRoute:audit")
    @RequestMapping(value = "batchAudit")
    public AjaxJson batchAudit(TmObjRouteEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            List<TmObjRoute> list = tmObjRouteService.findPage(new Page<>(), entity).getList();
            for (TmObjRoute tmObjRoute : list) {
                tmObjRouteService.audit(tmObjRoute);
            }
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消审核
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:objRoute:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                tmObjRouteService.cancelAudit(tmObjRouteService.get(id));
            }
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消审核
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:objRoute:cancelAudit")
    @RequestMapping(value = "batchCancelAudit")
    public AjaxJson batchCancelAudit(TmObjRouteEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            List<TmObjRoute> list = tmObjRouteService.findPage(new Page<>(), entity).getList();
            for (TmObjRoute tmObjRoute : list) {
                tmObjRouteService.cancelAudit(tmObjRoute);
            }
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}
