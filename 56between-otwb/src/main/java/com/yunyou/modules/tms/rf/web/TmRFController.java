package com.yunyou.modules.tms.rf.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.rf.entity.*;
import com.yunyou.modules.tms.rf.service.*;
import com.yunyou.modules.wms.rf.entity.WmRfVersionInfo;
import com.yunyou.modules.wms.rf.service.WmRfVersionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * RF接口入口
 *
 * @author ZYF
 * @version 2020-09-16
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/rf")
public class TmRFController extends BaseController {
    @Autowired
    private TmRfInboundService tmRfInboundService;
    @Autowired
    private TmRfOutboundService tmRfOutboundService;
    @Autowired
    private TmRfRepairOrderService tmRfRepairOrderService;
    @Autowired
    private WmRfVersionInfoService rfVersionInfoService;

    /**
     * 获取版本信息
     */
    @ResponseBody
    @RequestMapping(value = "getAppVersion")
    public AjaxJson getAppVersion(String appId) {
        AjaxJson j = new AjaxJson();
        WmRfVersionInfo versionInfo = rfVersionInfoService.getLastVersionInfo(appId);
        if (null == versionInfo || StringUtils.isEmpty(versionInfo.getId())) {
            j.setSuccess(false);
            j.setMsg("未维护自动更新地址");
        } else {
            j.put("versionCode", 10);
            j.put("versionName", versionInfo.getVersionName());
            j.put("versionInfo", versionInfo.getVersionInfo());
            j.put("downloadAddress", versionInfo.getDownloadAddress());
        }

        return j;
    }

    /**
     * 备件入库单查询
     */
    @ResponseBody
    @RequestMapping(value = "querySpareInboundInfo")
    public AjaxJson querySpareInboundInfo(@RequestBody TMSRF_Inbound_Query_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmRfInboundService.querySpareInboundInfo(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 备件入库单明细查询
     */
    @ResponseBody
    @RequestMapping(value = "querySpareInboundDetail")
    public AjaxJson querySpareInboundDetail(@RequestBody TMSRF_Inbound_Query_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmRfInboundService.querySpareInboundDetail(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 备件扫码入库
     */
    @ResponseBody
    @RequestMapping(value = "spareInboundScanConfirm")
    public AjaxJson spareInboundScanConfirm(@RequestBody TMSRF_Inbound_Confirm_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = tmRfInboundService.spareInboundScanConfirm(request);
            if (msg.isSuccess()) {
                j.put("entity", msg.getData());
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(StringUtils.isBlank(e.getMessage()) ? "系统异常" : e.getMessage());
        }

        return j;
    }

    /**
     * 备件出入库扫码信息查询
     */
    @ResponseBody
    @RequestMapping(value = "queryScanInfo")
    public AjaxJson queryScanInfo(@RequestBody TMSRF_InOutbound_QueryScanInfo_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = new ResultMessage();
        if ("INBOUND".equals(request.getOrderType())) {
            msg = tmRfInboundService.queryScanInfo(request);
        } else if ("OUTBOUND".equals(request.getOrderType())) {
            msg = tmRfOutboundService.queryScanInfo(request);
        } else {
            msg.setSuccess(false);
            msg.setMessage("单据类型错误！");
        }
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 备件出库单查询
     */
    @ResponseBody
    @RequestMapping(value = "querySpareOutboundInfo")
    public AjaxJson querySpareOutboundInfo(@RequestBody TMSRF_Outbound_Query_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmRfOutboundService.querySpareOutboundInfo(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 备件出库单明细查询
     */
    @ResponseBody
    @RequestMapping(value = "querySpareOutboundDetail")
    public AjaxJson querySpareOutboundDetail(@RequestBody TMSRF_Outbound_Query_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmRfOutboundService.querySpareOutboundDetail(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 备件扫码出库
     */
    @ResponseBody
    @RequestMapping(value = "spareOutboundScanConfirm")
    public AjaxJson spareOutboundScanConfirm(@RequestBody TMSRF_Outbound_Confirm_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = tmRfOutboundService.spareOutboundScanConfirm(request);
            if (msg.isSuccess()) {
                j.put("entity", msg.getData());
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(StringUtils.isBlank(e.getMessage()) ? "系统异常" : e.getMessage());
        }

        return j;
    }

    /**
     * 维修单查询
     */
    @ResponseBody
    @RequestMapping(value = "queryRepairOrderInfo")
    public AjaxJson queryRepairOrderInfo(@RequestBody TMSRF_Repair_QueryOrder_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = tmRfRepairOrderService.queryRepairOrderInfo(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 维修单受理
     */
    @ResponseBody
    @RequestMapping(value = "repairAccept")
    public AjaxJson repairAccept(@RequestBody TMSRF_Repair_Accept_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = tmRfRepairOrderService.repairAccept(request);
            if (msg.isSuccess()) {
                j.put("entity", msg.getData());
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(StringUtils.isBlank(e.getMessage()) ? "系统异常" : e.getMessage());
        }

        return j;
    }

    /**
     * 维修单备件入库
     */
    @ResponseBody
    @RequestMapping(value = "repairOrderSpareInbound")
    public AjaxJson repairOrderSpareInbound(@RequestBody TMSRF_Repair_ScanSpareBarcode_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = tmRfRepairOrderService.repairOrderSpareInbound(request);
            if (msg.isSuccess()) {
                j.put("entity", msg.getData());
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(StringUtils.isBlank(e.getMessage()) ? "系统异常" : e.getMessage());
        }

        return j;
    }

    /**
     * 维修单备件出库
     */
    @ResponseBody
    @RequestMapping(value = "repairOrderSpareOutbound")
    public AjaxJson repairOrderSpareOutbound(@RequestBody TMSRF_Repair_ScanSpareBarcode_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = tmRfRepairOrderService.repairOrderSpareOutbound(request);
            if (msg.isSuccess()) {
                j.put("entity", msg.getData());
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(StringUtils.isBlank(e.getMessage()) ? "系统异常" : e.getMessage());
        }

        return j;
    }
}