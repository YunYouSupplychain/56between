package com.yunyou.modules.tms.print.web;

import java.util.List;

import com.yunyou.modules.tms.print.entity.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.print.service.TmPrintService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * TMS打印Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/print")
public class TmPrintController extends BaseController {
    @Autowired
    private TmPrintService tmPrintService;

    /**
     * 描述：打印维修工单（维修工单）
     */
    @RequiresPermissions("tms:print:repairOrder:printRepairOrder")
    @RequestMapping(value = "/repairOrder")
    public String printRepairOrder(Model model, String ids) {
        List<RepairOrder> result = tmPrintService.getPrintRepairOrder(ids.split(","));

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/REPAIR_ORDER.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：打印运输信封（运输订单）
     */
    @RequiresPermissions("tms:print:transportOrder:printTransportEnvelope")
    @RequestMapping(value = "/transportOrder/transportEnvelope")
    public String printTransportEnvelope(String ids, Model model) {
        List<TransportEnvelope> result = tmPrintService.getPrintTransportEnvelope(ids.split(","));

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/TRANSPORT_ENVELOPE.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：打印运输协议（派车单）
     */
    @RequiresPermissions("tms:print:dispatchOrder:printTransportAgreement")
    @RequestMapping(value = "/dispatchOrder/transportAgreement")
    public String printTransportAgreement(String ids, Model model) {
        List<TransportAgreement> result = tmPrintService.getPrintTransportAgreement(ids.split(","));

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/TRANSPORT_AGREEMENT.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：打印装车清单（派车单）
     */
    @RequiresPermissions("tms:print:dispatchOrder:printGoodsList")
    @RequestMapping(value = "/dispatchOrder/goodsList")
    public String printGoodsList(String ids, Model model) {
        List<DispatchGoodsList> result = tmPrintService.getPrintGoodsList(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/DISPATCH_GOODS_LIST.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店送货单
     */
    @RequiresPermissions("tms:print:dispatchOrder:printStoreDelivery")
    @RequestMapping(value = "/dispatchOrder/printStoreDelivery")
    public String printStoreDelivery(String ids, Model model) {
        List<StoreDelivery> result = tmPrintService.getPrintStoreDelivery(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeDelivery.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店验收单
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintStoreCheckAcceptOrder")
    @RequestMapping(value = "/dispatchOrder/getPrintStoreCheckAcceptOrder")
    public String getPrintStoreCheckAcceptOrder(String ids, Model model) {
        List<StoreCheckAcceptOrderReport> result = tmPrintService.getPrintStoreCheckAcceptOrder(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeCheckAcceptOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店验收单（横版）
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintStoreCheckAcceptOrder")
    @RequestMapping(value = "/dispatchOrder/getPrintStoreCheckAcceptOrderLandscape")
    public String getPrintStoreCheckAcceptOrderLandscape(String ids, Model model) {
        List<StoreCheckAcceptOrderReport> result = tmPrintService.getPrintStoreCheckAcceptOrder(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeCheckAcceptOrderLandscape.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店验收单（蔬果送货单）
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintStoreCheckAcceptOrderFresh")
    @RequestMapping(value = "/dispatchOrder/getPrintStoreCheckAcceptOrderFresh")
    public String getPrintStoreCheckAcceptOrderFresh(String ids, Model model) {
        List<StoreCheckAcceptOrderReport> result = tmPrintService.getPrintStoreCheckAcceptOrderFresh(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeCheckAcceptOrderFresh.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店验收单（RF栈板）
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintStoreCheckAcceptOrderPAL")
    @RequestMapping(value = "/dispatchOrder/getPrintStoreCheckAcceptOrderPAL")
    public String getPrintStoreCheckAcceptOrderPAL(String ids, Model model) {
        List<StoreCheckAcceptOrderReport> result = tmPrintService.getPrintStoreCheckAcceptOrderPAL(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeCheckAcceptOrderRF.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店验收单（RF栈板横版）
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintStoreCheckAcceptOrderPAL")
    @RequestMapping(value = "/dispatchOrder/getPrintStoreCheckAcceptOrderPALLandscape")
    public String getPrintStoreCheckAcceptOrderPALLandscape(String ids, Model model) {
        List<StoreCheckAcceptOrderReport> result = tmPrintService.getPrintStoreCheckAcceptOrderPAL(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeCheckAcceptOrderRFLandscape.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：门店验收单（WMS）
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintStoreCheckAcceptOrderWms")
    @RequestMapping(value = "/dispatchOrder/getPrintStoreCheckAcceptOrderWms")
    public String getPrintStoreCheckAcceptOrderWms(String ids, Model model) {
        List<StoreCheckAcceptOrderReport> result = tmPrintService.getPrintStoreCheckAcceptOrderWms(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/storeCheckAcceptOrderWms.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：登票本
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintTicketBook")
    @RequestMapping(value = "/dispatchOrder/getPrintTicketBook")
    public String getPrintTicketBook(String ids, Model model) {
        List<TicketBook> result = tmPrintService.getPrintTicketBook(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/ticketBook.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：派车装车交接单
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintDispatchHandover")
    @RequestMapping(value = "/dispatchOrder/getPrintDispatchHandover")
    public String getPrintDispatchHandover(String ids, Model model) {
        List<DispatchHandoverReport> result = tmPrintService.getPrintDispatchHandover(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/dispatchHandover.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：派车装车交接单（RF栈板）
     */
    @RequiresPermissions("tms:print:dispatchOrder:getPrintDispatchHandoverPAL")
    @RequestMapping(value = "/dispatchOrder/getPrintDispatchHandoverPAL")
    public String getPrintDispatchHandoverPAL(String ids, Model model) {
        List<DispatchHandoverReport> result = tmPrintService.getPrintDispatchHandover(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/dispatchHandoverPAL.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：运输订单
     */
    @RequiresPermissions("tms:print:transportOrder:printTransportOrder")
    @RequestMapping(value = "/transportOrder/printTransportOrder")
    public String getPrintTransportOrder(String ids, Model model) {
        List<TransportOrderReport> result = tmPrintService.getPrintTransportOrder(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/tm_transport_order.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 描述：派车单
     */
    @RequiresPermissions("tms:print:dispatchOrder:printDispatchOrder")
    @RequestMapping(value = "/dispatchOrder/printDispatchOrder")
    public String getPrintDispatchOrder(String ids, Model model) {
        List<DispatchOrderReport> result = tmPrintService.getPrintDispatchOrder(ids.split(","));
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        model.addAttribute("url", "classpath:/jasper/tms/tm_dispatch_order.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }
}
