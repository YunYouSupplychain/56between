package com.yunyou.modules.tms.order.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmTransportOrderRouteAction;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderRouteEntity;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/transport/route")
public class TmTransportOrderRouteController extends BaseController {
    @Autowired
    private TmTransportOrderRouteAction tmTransportOrderRouteAction;

    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("tms:order:transport:route:list")
    public String findLabelRouteList(Model model) {
        model.addAttribute("tmTransportOrderRouteEntity", new TmTransportOrderRouteEntity());
        return "modules/tms/order/tmOrderLabelRouteList";
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:route:list")
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> findPage(TmTransportOrderRouteEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderRouteAction.findPage(new Page<>(request, response), qEntity));
    }

}