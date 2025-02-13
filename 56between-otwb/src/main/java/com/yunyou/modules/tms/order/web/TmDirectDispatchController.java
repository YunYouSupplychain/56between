package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDirectDispatchAction;
import com.yunyou.modules.tms.order.entity.TmDirectDispatch;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 调度派车Controller
 *
 * @author zyf
 * @version 2023-11-14
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmDirectDispatch")
public class TmDirectDispatchController extends BaseController {

    @Autowired
    private TmDirectDispatchAction action;

    @ResponseBody
    @PostMapping(value = "checkOrders")
    public AjaxJson checkOrders(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = action.checkOrders(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
        rsMap.put("hasDispatch", message.getData());
        j.setBody(rsMap);
        return j;
    }

    @RequestMapping(value = "directDispatchForm")
    public String directDispatchForm(@RequestParam("ids") String ids, Model model) {
        TmDirectDispatch tmDirectDispatch = new TmDirectDispatch();
        tmDirectDispatch.setIds(ids);
        model.addAttribute("tmDirectDispatch", tmDirectDispatch);
        TmTransportOrderLabelEntity tmTransportOrderLabelEntity = new TmTransportOrderLabelEntity();
        tmTransportOrderLabelEntity.setTransportIdList(Arrays.asList(ids.split(",")));
        model.addAttribute("tmTransportOrderLabelEntity", tmTransportOrderLabelEntity);
        return "modules/tms/order/tmDirectDispatchForm";
    }

    @ResponseBody
    @RequestMapping(value = "page")
    public Map<String, Object> page(TmTransportOrderLabelEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(action.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @PostMapping(value = "checkVehicle")
    public AjaxJson checkVehicle(TmDirectDispatch entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = action.checkVehicle(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
        rsMap.put("hasVehicle", message.getData());
        j.setBody(rsMap);
        return j;
    }

    @ResponseBody
    @PostMapping(value = "directDispatch")
    public AjaxJson directDispatch(TmDirectDispatch entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = action.directDispatch(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }
}
