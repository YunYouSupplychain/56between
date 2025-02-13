package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmVehicleSafetyCheckAction;
import com.yunyou.modules.tms.order.entity.extend.TmVehicleSafetyCheckEntity;
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
 * 出车安全检查信息Controller
 *
 * @author ZYF
 * @version 2020-09-07
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmVehicleSafeTyCheck")
public class TmVehicleSafetyCheckController extends BaseController {
    @Autowired
    private TmVehicleSafetyCheckAction tmVehicleSafetyCheckAction;

    @ModelAttribute
    public TmVehicleSafetyCheckEntity get(@RequestParam(required = false) String id) {
        TmVehicleSafetyCheckEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmVehicleSafetyCheckAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmVehicleSafetyCheckEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:order:tmVehicleSafetyCheck:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmVehicleSafetyCheckList";
    }

    @RequiresPermissions(value = {"tms:order:tmVehicleSafetyCheck:view", "tms:order:tmVehicleSafetyCheck:add", "tms:order:tmVehicleSafetyCheck:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmVehicleSafetyCheckEntity entity, Model model) {
        model.addAttribute("tmVehicleSafetyCheckEntity", entity);
        return "modules/tms/order/tmVehicleSafetyCheckForm";
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmVehicleSafetyCheck:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmVehicleSafetyCheckEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmVehicleSafetyCheckAction.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmVehicleSafetyCheck:add", "tms:order:tmVehicleSafetyCheck:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmVehicleSafetyCheckEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = tmVehicleSafetyCheckAction.saveEntity(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }
}