package com.yunyou.modules.wms.inbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuBarcodeService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.*;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 收货明细Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inbound/banQinWmAsnDetailReceive")
public class BanQinWmAsnDetailReceiveController extends BaseController {
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinCdWhSkuBarcodeService banQinCdWhSkuBarcodeService;
    @Autowired
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private BanQinBatchInboundRcOperationService batchInboundRcOperationService;
    @Autowired
    private BanQinInboundPaOperationService banQinInboundPaOperationService;

    /**
     * 收货明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAsnDetailReceiveEntity banQinWmAsnDetailReceive, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isNotEmpty(banQinWmAsnDetailReceive.getSkuCode())) {
            List<BanQinCdWhSkuBarcode> byBarcode = banQinCdWhSkuBarcodeService.findByBarcode(banQinWmAsnDetailReceive.getOwnerCode(), null, banQinWmAsnDetailReceive.getSkuCode(), banQinWmAsnDetailReceive.getOrgId());
            if (CollectionUtil.isNotEmpty(byBarcode)) {
                banQinWmAsnDetailReceive.setSkuCode(byBarcode.get(0).getSkuCode());
            }
        }
        Page<BanQinWmAsnDetailReceiveEntity> page = banQinWmAsnDetailReceiveService.findPage(new Page<BanQinWmAsnDetailReceiveEntity>(request, response), banQinWmAsnDetailReceive);
        return getBootstrapData(page);
    }

    /**
     * 收货明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinWmAsnDetailReceiveEntity banQinWmAsnDetailReceive, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmAsnDetailReceiveEntity> page = banQinWmAsnDetailReceiveService.findGrid(new Page<BanQinWmAsnDetailReceiveEntity>(request, response), banQinWmAsnDetailReceive);
        return getBootstrapData(page);
    }

    /**
     * 安排库位
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:arrangeLoc")
    @RequestMapping(value = "arrangeLoc")
    public AjaxJson arrangeLoc(@RequestBody List<BanQinWmAsnDetailReceiveEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinInboundOperationService.inboundBatchArrangeLoc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消安排库位
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:cancelArrangeLoc")
    @RequestMapping(value = "cancelArrangeLoc")
    public AjaxJson cancelArrangeLoc(@RequestBody List<BanQinWmAsnDetailReceiveEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinInboundOperationService.inboundBatchCancelArrangeLoc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量收货确认
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:receiveConfirm")
    @RequestMapping(value = "receiving")
    public AjaxJson receiving(@RequestBody List<BanQinWmAsnDetailReceiveEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = batchInboundRcOperationService.inboundBatchReceiving(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量取消收货确认
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:cancelReceive")
    @RequestMapping(value = "cancelReceiving")
    public AjaxJson cancelReceiving(@RequestBody List<BanQinWmAsnDetailReceiveEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = batchInboundRcOperationService.inboundBatchCancelReceiving(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量生成上架任务
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:createTaskPa")
    @RequestMapping(value = "createTaskPa")
    public AjaxJson createTaskPa(@RequestBody List<BanQinWmAsnDetailReceiveEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinInboundPaOperationService.inboundBatchCreateTaskPa(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成凭证号
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:createVoucherNo")
    @RequestMapping(value = "createVoucherNo")
    public AjaxJson createVoucherNo(BanQinWmAsnDetailReceive wmAsnDetailReceive) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.createVoucherNoByLineNo(wmAsnDetailReceive.getHeadId(), wmAsnDetailReceive.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消凭证号
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetailReceive:cancelVoucherNo")
    @RequestMapping(value = "cancelVoucherNo")
    public AjaxJson cancelVoucherNo(BanQinWmAsnDetailReceive wmAsnDetailReceive) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.cancelVoucherNoByLineNo(wmAsnDetailReceive.getHeadId(), wmAsnDetailReceive.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印托盘标签
     */
    @RequestMapping(value = "/printTraceLabel")
    public String printTraceLabel(Model model, String ids) {
        List<TraceLabel> result = banQinWmAsnDetailReceiveService.getTraceLabel(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/traceLabel.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印托盘标签（二维码）
     */
    @RequestMapping(value = "/printTraceLabelQrCode")
    public String printTraceLabelQrCode(Model model, String ids) {
        List<TraceLabel> result = banQinWmAsnDetailReceiveService.getTraceLabel(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/traceLabel_qrcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }
}