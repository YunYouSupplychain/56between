package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundCreateSoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 根据库存生成SOController
 *
 * @author WMJ
 * @version 2020-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmCreateSoByInv")
public class BanQinWmCreateSoByInvController extends BaseController {
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private BanQinOutboundCreateSoService outboundCreateSoService;

    /**
     * 库存查询列表页面
     */
    @RequiresPermissions("outbound:banQinWmCreateSoByInv:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmCreateSoByInvList";
    }

    /**
     * 批次库位库存表列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmInvLotLocEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvLotLocService.findPage(new Page<BanQinWmInvLotLocEntity>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmCreateSoByInv:createSo")
    @RequestMapping(value = "createSo")
    public AjaxJson createSo(@RequestBody List<BanQinWmInvLotLocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundCreateSoService.createSoByInv(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}