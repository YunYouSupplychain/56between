package com.yunyou.modules.tms.order.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmRepairOrderAction;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderEntity;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmRepairOrder")
public class TmRepairOrderController extends BaseController {
    @Autowired
    private TmRepairOrderAction tmRepairOrderAction;

    @ModelAttribute
    public TmRepairOrderEntity get(@RequestParam(required = false) String id) {
        TmRepairOrderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmRepairOrderAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmRepairOrderEntity();
        }
        return entity;
    }

    @RequiresPermissions("order:tmRepairOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmRepairOrderList";
    }

    @RequiresPermissions(value = {"order:tmRepairOrder:view", "order:tmRepairOrder:add", "order:tmRepairOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form() {
        return "modules/tms/order/tmRepairOrderForm";
    }

    @ResponseBody
    @RequiresPermissions("order:tmRepairOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmRepairOrderEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmRepairOrderAction.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"order:tmRepairOrder:add", "order:tmRepairOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "saveForUnRepair")
    public AjaxJson saveForUnRepair(TmRepairOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmRepairOrderAction.saveForUnRepair(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("id", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions(value = {"order:tmRepairOrder:add", "order:tmRepairOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "saveForRepair")
    public AjaxJson saveForRepair(TmRepairOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmRepairOrderAction.saveForRepair(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("id", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmRepairOrder:del")
    @RequestMapping(value = "deleteAll", method = RequestMethod.POST)
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmRepairOrderAction.batchRemove(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmRepairOrder:list")
    @RequestMapping(value = "/detail/data")
    public List<TmRepairOrderDetailEntity> detailData(TmRepairOrderDetailEntity qEntity) {
        return tmRepairOrderAction.findDetailList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"order:tmRepairOrderDetail:add", "order:tmRepairOrderDetail:save"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson saveDetail(TmRepairOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmRepairOrderAction.saveDetail(entity.getTmRepairOrderDetailList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmRepairOrderDetail:del")
    @RequestMapping(value = "/detail/remove")
    public AjaxJson removeDetail(String repairNo, String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmRepairOrderAction.removeDetail(repairNo, ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 查看图片
     */
    @ResponseBody
    @RequiresPermissions("order:tmRepairOrder:imgList")
    @RequestMapping(value = "/img/data")
    public Map<String, Object> imgData(TmAttachementDetail tmAttachementDetail, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmRepairOrderAction.findImgPage(new Page<>(request, response), tmAttachementDetail));
    }

}
