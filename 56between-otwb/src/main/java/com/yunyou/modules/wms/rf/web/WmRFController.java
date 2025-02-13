package com.yunyou.modules.wms.rf.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.rf.entity.*;
import com.yunyou.modules.wms.rf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * RF接口入口
 *
 * @author WMJ
 * @version 2020-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/rf")
public class WmRFController extends BaseController {
    @Autowired
    private RfReceiveService rfReceiveService;
    @Autowired
    private RfPutawayService rfPutawayService;
    @Autowired
    private RfPickingService rfPickingService;
    @Autowired
    private RfShipmentService rfShipmentService;
    @Autowired
    private RfMovementService rfMovementService;
    @Autowired
    private RfReplenishmentService rfReplenishmentService;
    @Autowired
    private RfTaskCountService rfTaskCountService;
    @Autowired
    private RfInventoryService rfInventoryService;
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
     * 校验ASN单据是否码盘
     */
    @ResponseBody
    @RequestMapping(value = "checkAsnIsPalletize")
    public AjaxJson checkAsnIsPalletize(@RequestBody WMSRF_RC_CheckAsnIsPalletize_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = rfReceiveService.checkAsnIsPalletize(request);
        if (!msg.isSuccess()) {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 根据托盘ID查询收货明细
     */
    @ResponseBody
    @RequestMapping(value = "queryAsnDetailByTraceIdOrSku")
    public AjaxJson queryAsnDetailByTraceIdOrSku(@RequestBody WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = rfReceiveService.queryAsnDetailByTraceIDOrSku(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 根据托盘Id确认收货明细
     */
    @ResponseBody
    @RequestMapping(value = "saveAsnDetailByTraceId")
    public AjaxJson saveAsnDetailByTraceId(@RequestBody WMSRF_RC_SaveAsnDetailByTraceID_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfReceiveService.saveAsnDetailByTraceId(request);
            if (!msg.isSuccess()) {
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
     * 获取上架信息:根据托盘号查询
     */
    @ResponseBody
    @RequestMapping(value = "queryPutAwayTaskByTraceId")
    public AjaxJson queryPutAwayTaskByTraceId(@RequestBody WMSRF_PA_QueryPutAwayTaskByTraceID_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = rfPutawayService.queryPutAwayTaskByTraceId(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 获取上架信息:根据任务查询
     */
    @ResponseBody
    @RequestMapping(value = "queryPutAwayTaskByTaskNo")
    public AjaxJson queryPutAwayTaskByTaskNo(@RequestBody WMSRF_PA_QueryPutAwayTaskByTaskNo_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = rfPutawayService.queryPutAwayTaskByTaskNo(request);
        if (msg.isSuccess()) {
            j.put("detailEntityList", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 确认上架
     */
    @ResponseBody
    @RequestMapping(value = "savePutAwayByTask")
    public AjaxJson savePutAwayByTask(@RequestBody WMSRF_PA_SavePutAwayByTask_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfPutawayService.savePutAwayByTask(request);
            if (!msg.isSuccess()) {
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
     * 查询拣货明细
     */
    @ResponseBody
    @RequestMapping(value = "queryPickDetail")
    public AjaxJson queryPickDetail(@RequestBody WMSRF_PK_QueryPickDetail_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfPickingService.queryPickDetail(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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
     * 拣货确认
     */
    @ResponseBody
    @RequestMapping(value = "savePick")
    public AjaxJson savePick(@RequestBody WMSRF_PK_SavePick_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfPickingService.savePick(request);
            if (!msg.isSuccess()) {
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
     * 标签拣货确认
     */
    @ResponseBody
    @RequestMapping(value = "savePickByTraceId")
    public AjaxJson savePickByTraceId(@RequestBody WMSRF_PK_SavePick_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfPickingService.savePickByTraceId(request);
            if (!msg.isSuccess()) {
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
     * 波次拣货确认
     */
    @ResponseBody
    @RequestMapping(value = "savePickByWave")
    public AjaxJson savePickByWave(@RequestBody WMSRF_PK_SavePick_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfPickingService.savePickByWave(request);
            if (!msg.isSuccess()) {
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
     * 手工拣货确认
     */
    @ResponseBody
    @RequestMapping(value = "savePickByManual")
    public AjaxJson savePickByManual(@RequestBody WMSRF_PK_SavePick_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfPickingService.savePickByManual(request);
            if (!msg.isSuccess()) {
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
     * 发货确认
     */
    @ResponseBody
    @RequestMapping(value = "saveShipment")
    public AjaxJson saveShipment(@RequestBody WMSRF_SHIP_SaveShipment_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfShipmentService.saveShipment(request);
            if (!msg.isSuccess()) {
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
     * 查询移库信息
     */
    @ResponseBody
    @RequestMapping(value = "queryMovement")
    public AjaxJson queryMovement(@RequestBody WMSRF_MV_QueryMovement_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfMovementService.queryMovement(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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
     * 移库确认
     */
    @ResponseBody
    @RequestMapping(value = "saveMovement")
    public AjaxJson saveMovement(@RequestBody List<WMSRF_MV_SaveMovement_Request> request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfMovementService.saveMovement(request);
            if (!msg.isSuccess()) {
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
     * 查询补货明细
     */
    @ResponseBody
    @RequestMapping(value = "queryReplenishmentDetail")
    public AjaxJson queryReplenishmentDetail(@RequestBody WMSRF_RP_QueryReplenishmentDetail_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfReplenishmentService.queryReplenishmentDetail(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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
     * 确认补货
     */
    @ResponseBody
    @RequestMapping(value = "saveReplenishment")
    public AjaxJson saveReplenishment(@RequestBody WMSRF_RP_SaveReplenishment_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfReplenishmentService.saveReplenishment(request);
            if (!msg.isSuccess()) {
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
     * 校验盘点单
     */
    @ResponseBody
    @RequestMapping(value = "checkTaskCount")
    public AjaxJson checkTaskCount(@RequestBody WMSRF_TC_CheckTaskCount_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = rfTaskCountService.checkTaskCount(request);
        if (!msg.isSuccess()) {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 查询盘点明细
     */
    @ResponseBody
    @RequestMapping(value = "queryTaskCountDetail")
    public AjaxJson queryTaskCountDetail(@RequestBody WMSRF_TC_QueryTaskCountDetail_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfTaskCountService.queryTaskCountDetail(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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
     * 确认盘点
     */
    @ResponseBody
    @RequestMapping(value = "saveTaskCount")
    public AjaxJson saveTaskCount(@RequestBody WMSRF_TC_SaveTaskCount_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfTaskCountService.saveTaskCount(request);
            if (!msg.isSuccess()) {
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
     * 查询可用货主
     */
    @ResponseBody
    @RequestMapping(value = "queryOwner")
    public AjaxJson queryOwner(@RequestBody WMSRF_TC_QueryOwner_Request request) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = rfTaskCountService.queryOwner(request);
        if (!msg.isSuccess()) {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    /**
     * 新增盘点明细
     */
    @ResponseBody
    @RequestMapping(value = "addNewTaskCount")
    public AjaxJson addNewTaskCount(@RequestBody WMSRF_TC_AddNewTaskCount_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfTaskCountService.addNewTaskCount(request);
            if (!msg.isSuccess()) {
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
     * 查询商品库存信息
     */
    @ResponseBody
    @RequestMapping(value = "querySkuInv")
    public AjaxJson querySkuInv(@RequestBody WMSRF_INV_QuerySkuInv_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfInventoryService.querySkuInv(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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
     * 拣货箱查询单头信息
     */
    @ResponseBody
    @RequestMapping(value = "queryPickBoxHeader")
    public AjaxJson queryPickBoxHeader(@RequestBody WMSRF_INV_QueryPickBoxHeaderOrDetail_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfInventoryService.queryPickBoxHeader(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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
     * 拣货箱查询明细信息
     */
    @ResponseBody
    @RequestMapping(value = "queryPickBoxDetail")
    public AjaxJson queryPickBoxDetail(@RequestBody WMSRF_INV_QueryPickBoxHeaderOrDetail_Request request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = rfInventoryService.queryPickBoxDetail(request);
            if (msg.isSuccess()) {
                j.put("detailEntityList", msg.getData());
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