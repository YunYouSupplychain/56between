package com.yunyou.modules.tms.order.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDispatchOrderLabelAction;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderLabelEntity;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/dispatch/label")
public class TmDispatchOrderLabelController extends BaseController {
    @Autowired
    private TmDispatchOrderLabelAction tmDispatchOrderLabelAction;

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:label:data")
    @RequestMapping(value = "page")
    public Map<String, Object> page(TmDispatchOrderLabelEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmDispatchOrderLabelAction.findPage(new Page<>(request, response), qEntity));
    }

}
