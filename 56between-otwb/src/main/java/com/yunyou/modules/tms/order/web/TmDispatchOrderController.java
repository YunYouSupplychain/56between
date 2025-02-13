package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDispatchOrderAction;
import com.yunyou.modules.tms.order.entity.TmCarrierFreight;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderLabelEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 派车单Controller
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmDispatchOrder")
public class TmDispatchOrderController extends BaseController {
    @Autowired
    private TmDispatchOrderAction tmDispatchOrderAction;

    @ModelAttribute
    public TmDispatchOrderEntity get(@RequestParam(required = false) String id) {
        TmDispatchOrderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmDispatchOrderAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmDispatchOrderEntity();
        }
        return entity;
    }

    @RequiresPermissions("order:tmDispatchOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmDispatchOrderList";
    }

    @RequiresPermissions(value = {"order:tmDispatchOrder:view", "order:tmDispatchOrder:add", "order:tmDispatchOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmDispatchOrderEntity entity, Model model) {
        model.addAttribute("tmDispatchOrderEntity", entity);
        return "modules/tms/order/tmDispatchOrderForm";
    }

    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:list")
    @RequestMapping(value = "page")
    public Map<String, Object> page(TmDispatchOrderEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchOrderAction.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"order:tmDispatchOrder:add", "order:tmDispatchOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmDispatchOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderAction.saveEntity(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:del")
    @RequestMapping(value = "deleteAll", method = RequestMethod.POST)
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderAction.batchRemove(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:audit")
    @RequestMapping(value = "audit", method = RequestMethod.POST)
    public AjaxJson audit(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderAction.batchAudit(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:cancelAudit")
    @RequestMapping(value = "cancelAudit", method = RequestMethod.POST)
    public AjaxJson cancelAudit(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderAction.batchCancelAudit(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:depart")
    @RequestMapping(value = "depart", method = RequestMethod.POST)
    public AjaxJson depart(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderAction.depart(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:copy")
    @RequestMapping(value = "copy")
    public AjaxJson copy(String id) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmDispatchOrderAction.copy(id);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:transport:data")
    @RequestMapping(value = "/transport/page")
    public Map<String, Object> transportPage(TmDispatchOrderLabelEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchOrderAction.findTransportPage(new Page<>(request, response), qEntity));
    }


    @ResponseBody
    @RequiresPermissions("order:tmDispatchOrder:carrierFreight")
    @RequestMapping(value = "/initCarrierFreight")
    public Map<String, Object> carrierFreight(TmCarrierFreight qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchOrderAction.findCarrierFreight(new Page<>(), qEntity));
    }

    @ResponseBody
    @RequestMapping(value = "transportCheckDispatchPage")
    public Map<String, Object> transportCheckDispatchPage(TmDispatchOrderEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchOrderAction.transportCheckDispatchPage(new Page<>(request, response), qEntity));
    }

}