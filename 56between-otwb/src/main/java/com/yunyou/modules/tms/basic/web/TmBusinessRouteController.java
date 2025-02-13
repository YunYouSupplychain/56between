package com.yunyou.modules.tms.basic.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmBusinessRoute;
import com.yunyou.modules.tms.basic.entity.extend.TmBusinessRouteEntity;
import com.yunyou.modules.tms.basic.service.TmBusinessRouteService;
import com.yunyou.modules.tms.common.TmsException;

@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmBusinessRoute")
public class TmBusinessRouteController extends BaseController {
    @Autowired
    private TmBusinessRouteService tmBusinessRouteService;

    @ModelAttribute
    public TmBusinessRouteEntity get(@RequestParam(required = false) String id) {
        TmBusinessRouteEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmBusinessRouteService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmBusinessRouteEntity();
        }
        return entity;
    }

    @RequiresPermissions("basic:tmBusinessRoute:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmBusinessRouteList";
    }

    @ResponseBody
    @RequiresPermissions("basic:tmBusinessRoute:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmBusinessRouteEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmBusinessRouteService.findPage(new Page<TmBusinessRouteEntity>(request, response), qEntity));
    }

    @RequiresPermissions(value = {"basic:tmBusinessRoute:view", "basic:tmBusinessRoute:add", "basic:tmBusinessRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmBusinessRouteEntity entity, Model model) {
        model.addAttribute("tmBusinessRouteEntity", entity);
        return "modules/tms/basic/tmBusinessRouteForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"basic:tmBusinessRoute:add", "basic:tmBusinessRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmBusinessRouteEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmBusinessRouteService.save(entity);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("basic:tmBusinessRoute:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmBusinessRoute tmBusinessRoute = tmBusinessRouteService.get(id);
            try {
                tmBusinessRouteService.delete(tmBusinessRoute);
            } catch (Exception e) {
                logger.error("删除业务路线id=[" + id + "]", e);
                errMsg.append("<br>").append("业务路线[").append(tmBusinessRoute.getCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmBusinessRouteEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmBusinessRouteService.findGrid(new Page<TmBusinessRouteEntity>(request, response), qEntity));
    }
}
