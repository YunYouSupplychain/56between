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
import com.yunyou.modules.tms.order.action.TmReceiveAction;
import com.yunyou.modules.tms.order.entity.extend.TmReceiveEntity;

/**
 * 收货Controller
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmReceive")
public class TmReceiveController extends BaseController {
    @Autowired
    private TmReceiveAction tmReceiveAction;

    /**
     * 收货标签列表页面
     */
    @RequiresPermissions("order:tmReceive:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("tmReceiveEntity", new TmReceiveEntity());
        return "modules/tms/order/tmReceiveList";
    }

    /**
     * 收货标签列表数据
     */
    @ResponseBody
    @RequiresPermissions("order:tmReceive:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmReceiveEntity tmReceive, HttpServletRequest request, HttpServletResponse response, Model model) {
        return getBootstrapData(tmReceiveAction.findPage(new Page<>(request, response), tmReceive));
    }

    /**
     * 收货
     */
    @ResponseBody
    @RequiresPermissions("order:tmReceive:receive")
    @RequestMapping(value = "receive", method = RequestMethod.POST)
    public AjaxJson receive(@RequestBody List<TmReceiveEntity> list) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmReceiveAction.receive(list);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 按条件收货
     */
    @ResponseBody
    @RequiresPermissions("order:tmReceive:receiveCondition")
    @RequestMapping(value = "receiveCondition", method = RequestMethod.POST)
    public AjaxJson receiveCondition(TmReceiveEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmReceiveAction.receiveCondition(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }


}