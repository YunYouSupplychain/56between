package com.yunyou.modules.tms.order.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
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
import com.yunyou.modules.tms.order.action.TmTransportOrderSkuAction;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderSkuEntity;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/transport/sku")
public class TmTransportOrderSkuController extends BaseController {
    @Autowired
    private TmTransportOrderSkuAction tmTransportOrderSkuAction;

    @ResponseBody
    @RequiresPermissions("tms:order:transport:sku:data")
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> findPage(TmTransportOrderSkuEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderSkuAction.findPage(new Page<TmTransportOrderSkuEntity>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:sku:data")
    @RequestMapping(value = "data", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TmTransportOrderSkuEntity> findList(TmTransportOrderSkuEntity qEntity) {
        return tmTransportOrderSkuAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:transport:sku:add", "tms:order:transport:sku:save"}, logical = Logical.OR)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public AjaxJson save(TmTransportOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderSkuAction.saveAll(entity.getTmTransportOrderSkuList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:sku:del")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public AjaxJson delete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderSkuAction.removeAll(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

}
