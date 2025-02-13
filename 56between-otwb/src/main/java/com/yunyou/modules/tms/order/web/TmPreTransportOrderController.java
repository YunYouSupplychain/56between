package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmPreTransportOrderAction;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderEntity;
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
 * 运输订单信息Controller
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmPreTransportOrder")
public class TmPreTransportOrderController extends BaseController {
    @Autowired
    private TmPreTransportOrderAction tmTransportOrderAction;

    @ModelAttribute
    public TmPreTransportOrderEntity get(@RequestParam(required = false) String id) {
        TmPreTransportOrderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportOrderAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmPreTransportOrderEntity();
        }
        return entity;
    }

    @RequiresPermissions("order:tmPreTransportOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmPreTransportOrderList";
    }

    @RequiresPermissions(value = {"order:tmPreTransportOrder:view", "order:tmPreTransportOrder:add", "order:tmPreTransportOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmPreTransportOrderEntity entity, Model model) {
        model.addAttribute("tmPreTransportOrderEntity", entity);
        return "modules/tms/order/tmPreTransportOrderForm";
    }

    @ResponseBody
    @RequiresPermissions("order:tmPreTransportOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmPreTransportOrderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderAction.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"order:tmPreTransportOrder:add", "order:tmPreTransportOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmPreTransportOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.saveEntity(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmPreTransportOrder:del")
    @RequestMapping(value = "deleteAll", method = RequestMethod.POST)
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.batchRemove(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmPreTransportOrder:copy")
    @RequestMapping(value = "copy")
    public AjaxJson copy(String id) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmTransportOrderAction.copy(id);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

}