package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmDispatchOrderSiteAction;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderSiteEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchSiteSelectLabelEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/dispatch/site")
public class TmDispatchOrderSiteController extends BaseController {
    @Autowired
    private TmDispatchOrderSiteAction tmDispatchOrderSiteAction;

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "data", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TmDispatchOrderSiteEntity> findList(TmDispatchOrderSiteEntity qEntity) {
        return tmDispatchOrderSiteAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:dispatch:site:add", "tms:order:dispatch:site:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public AjaxJson save(TmDispatchOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderSiteAction.saveAll(entity.getTmDispatchOrderSiteList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:del")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public AjaxJson remove(@RequestParam("ids") String dispatchSiteIds) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderSiteAction.removeAll(dispatchSiteIds.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "/opt/data")
    public List<TmDispatchSiteSelectLabelEntity> selectLabel(TmDispatchSiteSelectLabelEntity qEntity) {
        return tmDispatchOrderSiteAction.selectLabelForLeft(qEntity);
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "/opted/data")
    public List<TmDispatchSiteSelectLabelEntity> selectedLabel(TmDispatchSiteSelectLabelEntity qEntity) {
        return tmDispatchOrderSiteAction.selectLabelForRight(qEntity);
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "/opt/confirm", method = RequestMethod.POST)
    public AjaxJson selectLabelConfirm(@RequestBody List<TmDispatchSiteSelectLabelEntity> list) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderSiteAction.selectLabelConfirm(list);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "/opt/confirmAll", method = RequestMethod.POST)
    public AjaxJson selectLabelConfirm(TmDispatchSiteSelectLabelEntity qEntity) {
        AjaxJson j = new AjaxJson();

        List<TmDispatchSiteSelectLabelEntity> list = tmDispatchOrderSiteAction.selectLabelForLeft(qEntity);
        for (TmDispatchSiteSelectLabelEntity entity : list) {
            entity.setDispatchNo(qEntity.getDispatchNo());
            entity.setDispatchSiteOutletCode(qEntity.getDispatchSiteOutletCode());
            entity.setOrgId(qEntity.getOrgId());
        }
        ResultMessage message = tmDispatchOrderSiteAction.selectLabelConfirm(list);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "/opted/cancelConfirm", method = RequestMethod.POST)
    public AjaxJson cancelConfirm(@RequestBody List<TmDispatchSiteSelectLabelEntity> list) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderSiteAction.cancelSelectLabelConfirm(list);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:dispatch:site:data")
    @RequestMapping(value = "/opted/cancelConfirmAll", method = RequestMethod.POST)
    public AjaxJson cancelConfirm(TmDispatchSiteSelectLabelEntity qEntity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmDispatchOrderSiteAction.cancelSelectLabelConfirm(tmDispatchOrderSiteAction.selectLabelForRight(qEntity));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

}
