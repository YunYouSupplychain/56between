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
import com.yunyou.modules.tms.order.action.TmTransportOrderCostAction;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderCostEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/transport/cost")
public class TmTransportOrderCostController extends BaseController {
    @Autowired
    private TmTransportOrderCostAction tmTransportOrderCostAction;

    @ResponseBody
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    @RequiresPermissions("tms:order:transport:cost:data")
    public Map<String, Object> findPage(TmTransportOrderCostEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderCostAction.findPage(new Page<>(request, response), qEntity));
    }

    @ResponseBody
    @RequestMapping(value = "data", method = {RequestMethod.GET, RequestMethod.POST})
    @RequiresPermissions("tms:order:transport:cost:data")
    public List<TmTransportOrderCostEntity> findList(TmTransportOrderCostEntity qEntity) {
        return tmTransportOrderCostAction.findList(qEntity);
    }

    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"tms:order:transport:cost:add", "tms:order:transport:cost:edit"}, logical = Logical.OR)
    public AjaxJson save(TmTransportOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderCostAction.saveAll(entity.getTmTransportOrderCostList());
        if (message.isSuccess()) {
            j.put("data", tmTransportOrderCostAction.findList(new TmTransportOrderCostEntity(entity.getTransportNo(), entity.getOrgId())));
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @RequiresPermissions("tms:order:transport:cost:del")
    public AjaxJson delete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderCostAction.deleteAll(ids.split(",ids"));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }
}
