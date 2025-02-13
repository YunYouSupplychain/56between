package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdHeader;
import com.yunyou.modules.wms.outbound.entity.extend.PrintLdOrder;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundLdService;
import com.yunyou.modules.wms.outbound.service.BanQinWmLdHeaderService;
import com.yunyou.modules.wms.report.entity.WmStoreCheckAcceptOrderLabel;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 装车单Controller
 *
 * @author WMJ
 * @version 2020-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmLdHeader")
public class BanQinWmLdHeaderController extends BaseController {
    @Autowired
    private BanQinWmLdHeaderService banQinWmLdHeaderService;
    @Autowired
    private BanQinOutboundLdService banQinOutboundLdService;

    @ModelAttribute
    public BanQinWmLdEntity get(@RequestParam(required = false) String id) {
		BanQinWmLdEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmLdHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmLdEntity();
        }
        return entity;
    }

    /**
     * 装车单列表页面
     */
    @RequiresPermissions("outbound:banQinWmLdHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmLdHeaderList";
    }

    /**
     * 装车单列表数据
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmLdEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmLdHeaderService.findPage(new Page<BanQinWmLdEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑装车单表单页面
     */
    @RequiresPermissions(value = {"outbound:banQinWmLdHeader:view", "outbound:banQinWmLdHeader:add", "outbound:banQinWmLdHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmLdEntity entity, Model model) {
        model.addAttribute("banQinWmLdEntity", entity);
        //如果ID是空为添加
        if (StringUtils.isBlank(entity.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/outbound/banQinWmLdHeaderForm";
    }

    /**
     * 保存装车单
     */
    @RequiresPermissions(value = {"outbound:banQinWmLdHeader:add", "outbound:banQinWmLdHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
	@ResponseBody
    public AjaxJson save(BanQinWmLdEntity entity, Model model) {
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, entity)) {
			j.setSuccess(false);
			j.setMsg("非法数据！");
			return j;
		}
		try {
            ResultMessage msg = banQinOutboundLdService.saveWmLdHeader(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.put("entity", msg.getData());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
    }

    /**
     * 批量删除装车单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.removeByLdNo(entity.getLdNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmLdHeader banQinWmLdHeader, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "装车单.xlsx";
            Page<BanQinWmLdHeader> page = banQinWmLdHeaderService.findPage(new Page<>(request, response, -1), banQinWmLdHeader);
            new ExportExcel("装车单", BanQinWmLdHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出装车单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 发货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:shipment")
    @RequestMapping(value = "shipment")
    public AjaxJson shipment(BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.outboundShipmentByLdNo(entity.getLdNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 装车交接
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:loadDelivery")
    @RequestMapping(value = "loadDelivery")
    public AjaxJson loadDelivery(BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.deliveryByLdNo(entity.getLdNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消发货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:cancelShipment")
    @RequestMapping(value = "cancelShipment")
    public AjaxJson cancelShipment(BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.cancelShipmentByLdNo(entity.getLdNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消交接
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:cancelDelivery")
    @RequestMapping(value = "cancelDelivery")
    public AjaxJson cancelDelivery(BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.cancelDeliveryByLdNo(entity.getLdNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 关闭订单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdHeader:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.closeOrder(entity.getLdNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印门店验收单
     */
    @RequestMapping(value = "/printStoreCheckAcceptOrder")
    public String printStoreCheckAcceptOrder(Model model, String ids) {
        List<WmStoreCheckAcceptOrderLabel> result = banQinWmLdHeaderService.getStoreCheckAcceptOrder(Arrays.asList(ids.split(",")));

        model.addAttribute("url", "classpath:/jasper/wmsStoreCheckAcceptOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

    /**
     * 打印装车清单
     */
    @RequestMapping(value = "/printLdOrder")
    @RequiresPermissions("outbound:banQinWmLdHeader:printLdOrder")
    public String printLdOrder(Model model, String ids) {
        List<PrintLdOrder> result = banQinWmLdHeaderService.getLdOrder(ids.split(","));

        model.addAttribute("url", "classpath:/jasper/wm_ld_order.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}