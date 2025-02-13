package com.yunyou.modules.tms.order.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmTransportOrderLabelAction;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/transport/label")
public class TmTransportOrderLabelController extends BaseController {
    @Autowired
    private TmTransportOrderLabelAction tmTransportOrderLabelAction;

    @ResponseBody
    @RequiresPermissions("tms:order:transport:label:data")
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> findPage(TmTransportOrderLabelEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderLabelAction.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:label:data")
    @RequestMapping(value = "data", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TmTransportOrderLabel> findList(TmTransportOrderLabel qEntity) {
        return tmTransportOrderLabelAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:label:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public AjaxJson add(@RequestParam("ids") String ids, @RequestParam("lineNo") String lineNo, @RequestParam("skuCode") String skuCode, @RequestParam("labelQty") Long labelQty, @RequestParam("totalQty") Double totalQty, @RequestParam("totalWeight") Double totalWeight, @RequestParam("totalCubic") Double totalCubic) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderLabelAction.addLabel(ids.split(","), lineNo, skuCode, labelQty, totalQty == null ? 0D : totalQty, totalWeight == null ? 0D : totalWeight, totalCubic == null ? 0D : totalCubic);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:label:del")
    @RequestMapping(value = "deleteAll", method = RequestMethod.POST)
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderLabelAction.removeLabel(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

}
