package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmPreTransportOrderSkuAction;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderSkuEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/pre/transport/sku")
public class TmPreTransportOrderSkuController extends BaseController {
    @Autowired
    private TmPreTransportOrderSkuAction tmTransportOrderSkuAction;

    @ResponseBody
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> findPage(TmPreTransportOrderSkuEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderSkuAction.findPage(new Page<TmPreTransportOrderSkuEntity>(request, response), qEntity));
    }

    @ResponseBody
    @RequestMapping(value = "data", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TmPreTransportOrderSkuEntity> findList(TmPreTransportOrderSkuEntity qEntity) {
        return tmTransportOrderSkuAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:pre:transport:sku:add", "tms:order:pre:transport:sku:save"}, logical = Logical.OR)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public AjaxJson save(TmPreTransportOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderSkuAction.saveAll(entity.getTmTransportOrderSkuList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:pre:transport:sku:del")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public AjaxJson delete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderSkuAction.removeAll(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

}
