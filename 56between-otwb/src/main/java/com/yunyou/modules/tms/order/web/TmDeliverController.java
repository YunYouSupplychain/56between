package com.yunyou.modules.tms.order.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDeliverAction;
import com.yunyou.modules.tms.order.entity.extend.TmDeliverEntity;

/**
 * 发货标签Controller
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmDeliver")
public class TmDeliverController extends BaseController {
    @Autowired
    private TmDeliverAction tmDeliverAction;

    @RequiresPermissions("order:tmDeliver:list")
    @RequestMapping(value = {"list", ""}, method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("tmDeliverEntity", new TmDeliverEntity());
        return "modules/tms/order/tmDeliverList";
    }

    @ResponseBody
    @RequiresPermissions("order:tmDeliver:list")
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Map<String, Object> page(TmDeliverEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDeliverAction.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("order:tmDeliver:deliver")
    @RequestMapping(value = "deliver", method = RequestMethod.POST)
    public AjaxJson deliver(@RequestBody List<TmDeliverEntity> list) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDeliverAction.deliver(list);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmDeliver:deliverCondition")
    @RequestMapping(value = "deliverByCondition", method = RequestMethod.POST)
    public AjaxJson deliverByCondition(TmDeliverEntity qEntity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDeliverAction.deliverByCondition(qEntity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

}