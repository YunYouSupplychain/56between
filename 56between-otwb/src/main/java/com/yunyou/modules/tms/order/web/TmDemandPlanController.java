package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDemandPlanAction;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 需求计划信息Controller
 *
 * @author WMJ
 * @version 2020-07-07
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmDemandPlan")
public class TmDemandPlanController extends BaseController {
    @Autowired
    private TmDemandPlanAction tmDemandPlanAction;

    @ModelAttribute
    public TmDemandPlanEntity get(@RequestParam(required = false) String id) {
        TmDemandPlanEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmDemandPlanAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmDemandPlanEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:order:tmDemandPlan:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmDemandPlanList";
    }

    @RequiresPermissions(value = {"tms:order:tmDemandPlan:view", "tms:order:tmDemandPlan:add", "tms:order:tmDemandPlan:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmDemandPlanEntity entity, Model model) {
        model.addAttribute("tmDemandPlanEntity", entity);
        return "modules/tms/order/tmDemandPlanForm";
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmDemandPlan:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmDemandPlanEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDemandPlanAction.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmDemandPlanEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDemandPlanAction.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmDemandPlan:add", "tms:order:tmDemandPlan:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmDemandPlanEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDemandPlanAction.saveEntity(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmDemandPlan:del")
    @RequestMapping(value = "deleteAll", method = RequestMethod.POST)
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDemandPlanAction.batchRemove(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "/detail/data")
    public List<TmDemandPlanDetailEntity> findList(TmDemandPlanDetailEntity qEntity) {
        return tmDemandPlanAction.findDetailList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmDemandPlan:detail:add", "tms:order:tmDemandPlan:detail:save"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson detailSave(TmDemandPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = tmDemandPlanAction.saveDetailList(entity.getTmDemandPlanDetailList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmDemandPlan:detail:del")
    @RequestMapping(value = "/detail/delete", method = RequestMethod.POST)
    public AjaxJson detailDelete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDemandPlanAction.batchRemoveDetails(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

}