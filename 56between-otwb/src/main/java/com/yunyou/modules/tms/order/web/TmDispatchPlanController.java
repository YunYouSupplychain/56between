package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDispatchPlanAction;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanConfigEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanVehicleEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 调度计划信息Controller
 *
 * @author WMJ
 * @version 2020-07-07
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmDispatchPlan")
public class TmDispatchPlanController extends BaseController {
    @Autowired
    private TmDispatchPlanAction tmDispatchPlanAction;

    @RequiresPermissions("tms:order:tmDispatchPlan:list")
    @RequestMapping(value = "list")
    public String list() {
        return "modules/tms/order/tmDispatchPlanList";
    }

    @RequestMapping(value = "result")
    public String result(TmDispatchPlanEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanResult";
    }

    @RequestMapping(value = "dispatchAllDialog")
    public String dispatchAllDialog(TmDispatchPlanEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanDialog";
    }

    @RequestMapping(value = "detailLis")
    public String detailLis(TmDispatchPlanConfigEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanDetailList";
    }

    @RequestMapping(value = "resultLis")
    public String resultLis(TmDispatchPlanConfigEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanResultList";
    }

    @RequestMapping(value = "editForm")
    public String editForm(TmDispatchPlanConfigEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanEditForm";
    }

    @RequestMapping(value = "editAll")
    public String editAll(TmDispatchPlanConfigEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanEditAll";
    }

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmDispatchPlanEntity entity, HttpServletRequest request, HttpServletResponse response) {
        Page<TmDispatchPlanEntity> page = tmDispatchPlanAction.findPage(new Page<>(request, response), entity);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequestMapping(value = "detailData")
    public Map<String, Object> detailData(TmDispatchPlanDetailEntity entity, HttpServletRequest request, HttpServletResponse response) {
        Page<TmDispatchPlanDetailEntity> page = tmDispatchPlanAction.findDetailPage(new Page<>(request, response), entity);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequestMapping(value = "resultData")
    public Map<String, Object> resultData(TmDispatchPlanDetailEntity entity, HttpServletRequest request, HttpServletResponse response) {
        Page<TmDispatchPlanDetailEntity> page = tmDispatchPlanAction.findResultPage(new Page<>(request, response), entity);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequestMapping(value = "createInitPlan")
    public AjaxJson createInitPlan(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.createInitPlan(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.removeEntitys(ids);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getDispatchInfo")
    public AjaxJson getDispatchInfo(TmDispatchPlanDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.getDispatchInfo(entity.getPlanNo(), entity.getOrgId(), entity.getBaseOrgId());
        j.put("data", msg.getData());

        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getDispatchInfoByLine")
    public AjaxJson getDispatchInfoByLine(TmDispatchPlanDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.getDispatchInfoByLine(entity);
        if (msg.isSuccess()) {
            j.put("data", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getConfigInfo")
    public AjaxJson getConfigInfo(TmDispatchPlanConfigEntity entity) {
        AjaxJson j = new AjaxJson();
        List<TmDispatchPlanConfigEntity> list = tmDispatchPlanAction.getConfigInfo(entity);
        j.put("data", list);
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getWareRoomConfigInfo")
    public List<TmDispatchPlanConfigEntity> getWareRoomConfigInfo(TmDispatchPlanConfigEntity entity) {
        return tmDispatchPlanAction.getWareRoomConfigInfo(entity.getPlanNo(), entity.getVehicleNo(), entity.getTrip(), entity.getOrgId(), entity.getBaseOrgId());
    }

    @ResponseBody
    @RequestMapping(value = "dispatchAll")
    public AjaxJson dispatchAll(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.dispatchAll(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getCustomer")
    public Map<String, Object> getCustomer(TmDispatchPlanDetailEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchPlanAction.findCustomerPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequestMapping(value = "getSku")
    public Map<String, Object> getSku(TmDispatchPlanDetailEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchPlanAction.findSkuPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequestMapping(value = "getVehicle")
    public Map<String, Object> getVehicle(TmDispatchPlanVehicleEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchPlanAction.findVehiclePage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequestMapping(value = "saveConfig")
    public AjaxJson saveConfig(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        if (CollectionUtil.isNotEmpty(entity.getDetailList())) {
            entity.getDetailList().forEach(o -> o.setRemarks(entity.getRemarks()));
        }
        ResultMessage msg = tmDispatchPlanAction.saveEditConfig(entity.getDetailList());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "deleteConfig")
    public AjaxJson deleteConfig(String configId) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.deleteConfig(configId);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "audit")
    public AjaxJson audit(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.audit(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.cancelAudit(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @RequestMapping(value = "addDemandPlan")
    public String addDemandPlan(TmDispatchPlanEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanAddDemand";
    }

    @ResponseBody
    @RequestMapping(value = "addDemandPlanConfirm")
    public AjaxJson addDemandPlanConfirm(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.addDemandPlan(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @RequestMapping(value = "addVehicle")
    public String addVehicle(TmDispatchPlanEntity entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/tms/order/tmDispatchPlanAddVehicle";
    }

    @ResponseBody
    @RequestMapping(value = "addVehicleConfirm")
    public AjaxJson addVehicleConfirm(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.addVehicle(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "recall")
    public AjaxJson recall(TmDispatchPlanEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmDispatchPlanAction.recall(entity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }
}