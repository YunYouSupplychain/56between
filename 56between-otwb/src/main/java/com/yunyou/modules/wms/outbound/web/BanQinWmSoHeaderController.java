package com.yunyou.modules.wms.outbound.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeader;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoExportEntity;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoImportEntity;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoImportOrderNoQueryEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundDuplicateService;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import com.yunyou.modules.wms.report.entity.PickingOrderLabel;
import com.yunyou.modules.wms.report.entity.ShipHandoverOrder;
import com.yunyou.modules.wms.report.entity.ShipOrderLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出库单Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmSoHeader")
public class BanQinWmSoHeaderController extends BaseController {
    @Autowired
    private BanQinWmSoHeaderService banQinWmSoHeaderService;
    @Autowired
    private BanQinOutboundSoService outboundSoService;
    @Autowired
    private BanQinOutboundDuplicateService outboundDuplicateService;

    @ModelAttribute
    public BanQinWmSoEntity get(@RequestParam(required = false) String id) {
        BanQinWmSoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmSoHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmSoEntity();
        }
        return entity;
    }

    /**
     * 出库单列表页面
     */
    @RequiresPermissions("outbound:banQinWmSoHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmSoHeaderList";
    }

    /**
     * 出库单列表数据
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSoEntity banQinWmSoEntity, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (null != banQinWmSoEntity.getCustomerNoFile()) {
                ImportExcel customerNoExcel = new ImportExcel(banQinWmSoEntity.getCustomerNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> customerNoList = customerNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(customerNoList)) {
                    banQinWmSoEntity.setCustomerNoList(customerNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
            if (null != banQinWmSoEntity.getExtendNoFile()) {
                ImportExcel extendNoExcel = new ImportExcel(banQinWmSoEntity.getExtendNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> extendNoList = extendNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(extendNoList)) {
                    banQinWmSoEntity.setExtendNoList(extendNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
        } catch (Exception ignored) { }
        return getBootstrapData(banQinWmSoHeaderService.findPage(new Page<BanQinWmSoEntity>(request, response), banQinWmSoEntity));
    }

    /**
     * 出库单列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinWmSoEntity banQinWmSoEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmSoHeaderService.findGrid(new Page<BanQinWmSoEntity>(request, response), banQinWmSoEntity));
    }

    /**
     * 查看，增加，编辑出库单表单页面
     */
    @RequiresPermissions(value = {"outbound:banQinWmSoHeader:view", "outbound:banQinWmSoHeader:add", "outbound:banQinWmSoHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmSoEntity banQinWmSoEntity, Model model) {
        model.addAttribute("banQinWmSoEntity", banQinWmSoEntity);
        return "modules/wms/outbound/banQinWmSoHeaderForm";
    }

    /**
     * 保存出库单
     */
    @ResponseBody
    @RequiresPermissions(value = {"outbound:banQinWmSoHeader:add", "outbound:banQinWmSoHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmSoEntity banQinWmSoEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmSoEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据！");
            return j;
        }
        try {
            ResultMessage msg = outboundSoService.saveSoEntity(banQinWmSoEntity);
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
     * 批量删除出库单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.removeSoEntity(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(StringUtils.isEmpty(msg.getMessage()) ? WmsConstants.OPERATE_SUCCESS : msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 复制
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:duplicate")
    @RequestMapping(value = "duplicate")
    public AjaxJson duplicate(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundDuplicateService.duplicateSoEntity(banQinWmSoEntity.getSoNo(), banQinWmSoEntity.getOrgId());
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
     * 审核
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.audit(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消审核
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelAudit(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成波次计划
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:generateWave")
    @RequestMapping(value = "generateWave")
    public AjaxJson generateWave(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            List<String> soNos = StringUtils.isBlank(banQinWmSoEntity.getSoNo()) ? null : Arrays.asList(banQinWmSoEntity.getSoNo().split(","));
            ResultMessage msg = outboundSoService.createWave(soNos, banQinWmSoEntity.getWaveRuleCode(), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("数据已过期");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成波次计划-按波次规则组
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:createWaveByGroup")
    @RequestMapping(value = "createWaveByGroup")
    public AjaxJson createWaveByGroup(BanQinCdRuleWvGroupHeader entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.createWaveByGroupSo(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("数据已过期");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 预配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:preAlloc")
    @RequestMapping(value = "preAlloc")
    public AjaxJson preAlloc(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.preallocBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:alloc")
    @RequestMapping(value = "alloc")
    public AjaxJson alloc(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.allocBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(StringUtils.isEmpty(msg.getMessage()) ? WmsConstants.OPERATE_SUCCESS : msg.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拣货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.pickingBySoHandler(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 发货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:shipment")
    @RequestMapping(value = "shipment")
    public AjaxJson shipment(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.shipmentBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 下发运输
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:pushToTms")
    @RequestMapping(value = "pushToTms")
    public AjaxJson pushToTms(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        StringBuilder errMsg = new StringBuilder();
        try {
            if (null != banQinWmSoEntity.getCustomerNoFile()) {
                ImportExcel customerNoExcel = new ImportExcel(banQinWmSoEntity.getCustomerNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> customerNoList = customerNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(customerNoList)) {
                    banQinWmSoEntity.setCustomerNoList(customerNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
            if (null != banQinWmSoEntity.getExtendNoFile()) {
                ImportExcel extendNoExcel = new ImportExcel(banQinWmSoEntity.getExtendNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> extendNoList = extendNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(extendNoList)) {
                    banQinWmSoEntity.setExtendNoList(extendNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
        } catch (Exception ignored) { }
        if (StringUtils.isNotBlank(banQinWmSoEntity.getSoNo()) && banQinWmSoEntity.getSoNo().contains(",")) {
            banQinWmSoEntity.setSoNoList(Arrays.asList(banQinWmSoEntity.getSoNo().split(",")));
            banQinWmSoEntity.setSoNo(null);
        }
        List<BanQinWmSoEntity> list = banQinWmSoHeaderService.findPage(new Page<BanQinWmSoEntity>(), banQinWmSoEntity).getList();
        for (BanQinWmSoEntity o : list) {
            try {
                banQinWmSoHeaderService.pushToTms(o.getId());
            } catch (GlobalException e) {
                errMsg.append("订单[").append(o.getSoNo()).append("]").append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                logger.error("", e);
                errMsg.append("订单[").append(o.getSoNo()).append("]").append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    /**
     * 取消下发运输
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelPushToTms")
    @RequestMapping(value = "cancelPushToTms")
    public AjaxJson cancelPushToTms(BanQinWmSoEntity wmSoEntity) {
        AjaxJson j = new AjaxJson();
        StringBuilder errMsg = new StringBuilder();
        if (StringUtils.isNotBlank(wmSoEntity.getSoNo()) && wmSoEntity.getSoNo().contains(",")) {
            wmSoEntity.setSoNoList(Arrays.asList(wmSoEntity.getSoNo().split(",")));
            wmSoEntity.setSoNo(null);
        }
        List<BanQinWmSoEntity> list = banQinWmSoHeaderService.findPage(new Page<BanQinWmSoEntity>(), wmSoEntity).getList();
        for (BanQinWmSoEntity o : list) {
            try {
                banQinWmSoHeaderService.cancelPushToTms(o.getId());
            } catch (GlobalException e) {
                errMsg.append("订单[").append(o.getSoNo()).append("]").append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                logger.error("", e);
                errMsg.append("订单[").append(o.getSoNo()).append("]").append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    /**
     * 关闭订单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:closeOrder")
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.close(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消订单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelOrder")
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancelOrder(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelSo(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消预配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelPreAlloc")
    @RequestMapping(value = "cancelPreAlloc")
    public AjaxJson cancelPreAlloc(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelPreallocBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelAllocBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消拣货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelPicking")
    @RequestMapping(value = "cancelPicking")
    public AjaxJson cancelPicking(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelPickingBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelShipment")
    @RequestMapping(value = "cancelShipment")
    public AjaxJson cancelShipment(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelShipmentBySo(ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成装车单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:generateLd")
    @RequestMapping(value = "generateLd")
    public AjaxJson generateLd(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.generateLd(Arrays.stream(banQinWmSoEntity.getSoNo().split(",")).collect(Collectors.toList()), banQinWmSoEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmSoHeader:ldTransfer")
    @RequestMapping(value = "ldTransfer")
    public AjaxJson ldTransfer(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.deliverBySoNos(Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmSoHeader:ldCancelTransfer")
    @RequestMapping(value = "ldCancelTransfer")
    public AjaxJson ldCancelTransfer(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelDeliverBySoNos(Arrays.asList(banQinWmSoEntity.getSoNo().split(",")), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 冻结订单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:holdOrder")
    @RequestMapping(value = "holdOrder")
    public AjaxJson holdOrder(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.hold(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消冻结
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:cancelHold")
    @RequestMapping(value = "cancelHold")
    public AjaxJson cancelHold(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelHold(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拦截订单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:intercept")
    @RequestMapping(value = "intercept")
    public AjaxJson intercept(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.intercept(banQinWmSoEntity.getSoNo().split(","), banQinWmSoEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmSoHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSoEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinWmSoEntity> page = banQinWmSoHeaderService.findPage(new Page<BanQinWmSoEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmSoExportEntity.class).setDataList(page.getList()).write(response, "出库单.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出出库单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("outbound:banQinWmSoHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFile(MultipartFile file, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmSoImportEntity> list = ei.getDataList(BanQinWmSoImportEntity.class);
            if (CollectionUtil.isEmpty(list)) {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
                return j;
            }

            list.forEach(v -> v.setOrgId(orgId));
            ResultMessage msg = banQinWmSoHeaderService.importOrder(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            logger.error("导入异常", e);
            j.setSuccess(false);
            j.setMsg("导入异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入出库单数据模板
     */
    @RequiresPermissions("outbound:banQinWmSoHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            List<BanQinWmSoImportEntity> list = Lists.newArrayList();
            new ExportExcel("", BanQinWmSoImportEntity.class, 2).setDataList(list).write(response, "出库单数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSoHeader/?repage";
    }

    /**
     * 打印拣货清单
     *
     * @param ids 出库单ID
     */
    @RequestMapping(value = "/printPickingOrder")
    public String printPickingOrder(Model model, String ids) {
        List<PickingOrderLabel> result = banQinWmSoHeaderService.getPickingOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/pickingOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 打印拣货清单(横版)
     *
     * @param ids 出库单ID
     */
    @RequestMapping(value = "/printPickingOrderLandscape")
    public String printPickingOrderLandscape(Model model, String ids) {
        List<PickingOrderLabel> result = banQinWmSoHeaderService.getPickingOrderLandscape(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/pickingOrderLandscape.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }


    /**
     * 打印发货清单
     *
     * @param ids 出库单ID
     */
    @RequestMapping(value = "/printShipOrder")
    public String printShipOrder(Model model, String ids) {
        List<ShipOrderLabel> result = banQinWmSoHeaderService.getShipOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/shipOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 打印发货清单
     *
     * @param ids 出库单ID
     */
    @RequestMapping(value = "/printShipOrderNew")
    public String printShipOrderNew(Model model, String ids) {
        List<ShipOrderLabel> result = banQinWmSoHeaderService.getShipOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/shipOrderNew.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

    /**
     * 更新收货人信息
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:updateConsigneeInfo")
    @RequestMapping(value = "updateConsigneeInfo")
    public AjaxJson updateConsigneeInfo(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmSoHeaderService.updateConsigneeInfo(banQinWmSoEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 更新承运商信息
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoHeader:updateCarrierInfo")
    @RequestMapping(value = "updateCarrierInfo")
    public AjaxJson updateCarrierInfo(BanQinWmSoEntity banQinWmSoEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmSoHeaderService.updateCarrierInfo(banQinWmSoEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印出库确认交接单
     *
     * @param ids 出库单ID
     */
    @RequestMapping(value = "/printShipHandoverOrder")
    public String printShipHandoverOrder(Model model, String ids) {
        List<ShipHandoverOrder> result = banQinWmSoHeaderService.getShipHandoverOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/shipHandoverOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

}