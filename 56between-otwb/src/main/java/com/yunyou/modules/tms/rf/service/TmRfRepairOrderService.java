package com.yunyou.modules.tms.rf.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderInboundInfoEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderOutboundInfoEntity;
import com.yunyou.modules.tms.order.manager.TmRepairOrderManager;
import com.yunyou.modules.tms.rf.entity.*;
import com.yunyou.modules.tms.spare.entity.TmSparePartInv;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePartInvEntity;
import com.yunyou.modules.tms.spare.service.TmSparePartInvService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * TmsRf出库单service
 * @author ZYF
 * @version 2020/09/16
 */
@Service
public class TmRfRepairOrderService {

    @Autowired
    private TmRepairOrderManager tmRepairOrderManager;
    @Autowired
    private TmSparePartInvService tmSparePartInvService;

    /**
     * 维修单查询
     * @param request
     * @return
     */
    public ResultMessage queryRepairOrderInfo(TMSRF_Repair_QueryOrder_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getVehicleNo()) && StringUtils.isBlank(request.getRepairNo())) {
            msg.setSuccess(false);
            msg.setMessage("车牌号和维修工单号不能都为空！");
            return msg;
        }
        TmRepairOrderEntity con = new TmRepairOrderEntity();
        con.setCarNo(request.getVehicleNo());
        con.setRepairNo(request.getRepairNo());
        con.setOrgId(user.getOffice().getId());
        List<TmRepairOrderEntity> entityList = tmRepairOrderManager.findEntityList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_Repair_QueryOrder_Response> result = Lists.newArrayList();
            for (TmRepairOrderEntity entity : entityList) {
                TMSRF_Repair_QueryOrder_Response response = new TMSRF_Repair_QueryOrder_Response();
                BeanUtils.copyProperties(entity, response);
                response.setDriver(entity.getDriverName());
                if (null != entity.getOrderTime()) {
                    response.setOrderTime(DateUtils.formatDate(entity.getOrderTime(), "yyyy-MM-dd"));
                }
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }

    /**
     * 维修单受理
     * @param request
     * @return
     */
    @Transactional
    public ResultMessage repairAccept(TMSRF_Repair_Accept_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getRepairNo())) {
            msg.setSuccess(false);
            msg.setMessage("维修工单号不能为空！");
            return msg;
        }
        TmRepairOrderEntity repairOrderEntity = tmRepairOrderManager.getByNo(request.getRepairNo(), user.getOffice().getId());
        if (null == repairOrderEntity) {
            msg.setSuccess(false);
            msg.setMessage("维修工单不存在！");
            return msg;
        }
        if (!TmsConstants.REPAIR_ORDER_STATUS_00.equals(repairOrderEntity.getStatus())) {
            msg.setSuccess(false);
            msg.setMessage("维修工单状态不为新建！");
            return msg;
        }
        if (StringUtils.isNotBlank(repairOrderEntity.getRepairman())) {
            msg.setSuccess(false);
            msg.setMessage("已有维修员受理！");
            return msg;
        }
        repairOrderEntity.setRepairman(user.getName());
        tmRepairOrderManager.saveForRepair(repairOrderEntity);
        TMSRF_Repair_Accept_Response response = new TMSRF_Repair_Accept_Response();
        BeanUtils.copyProperties(repairOrderEntity, response);
        if (null != repairOrderEntity.getOrderTime()) {
            response.setOrderTime(DateUtils.formatDate(repairOrderEntity.getOrderTime(), "yyyy-MM-dd"));
        }
        response.setDriver(repairOrderEntity.getDriverName());
        msg.setData(response);
        return msg;
    }

    /**
     * 维修单备件入库
     * @param request
     * @return
     */
    @Transactional
    public ResultMessage repairOrderSpareInbound(TMSRF_Repair_ScanSpareBarcode_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getRepairNo())) {
            msg.setSuccess(false);
            msg.setMessage("维修工单号不能为空！");
            return msg;
        }
        TmRepairOrderEntity repairOrderEntity = tmRepairOrderManager.getByNo(request.getRepairNo(), user.getOffice().getId());
        if (null == repairOrderEntity) {
            msg.setSuccess(false);
            msg.setMessage("维修工单不存在！");
            return msg;
        }
        TmRepairOrderOutboundInfoEntity outCon = new TmRepairOrderOutboundInfoEntity();
        outCon.setRepairNo(request.getRepairNo());
        outCon.setBarcode(request.getSpareBarCode());
        outCon.setOrgId(user.getOffice().getId());
        List<TmRepairOrderOutboundInfoEntity> outboundInfoList = tmRepairOrderManager.findOutboundInfoList(outCon);
        if (CollectionUtil.isEmpty(outboundInfoList)) {
            msg.setSuccess(false);
            msg.setMessage("出库信息不存在！");
            return msg;
        }
        TmRepairOrderOutboundInfoEntity outboundInfo = outboundInfoList.get(0);
        // 删除出库信息
        tmRepairOrderManager.deleteOutboundInfo(outboundInfo.getId());

        // 保存库存信息
        TmSparePartInv sparePartInv = tmSparePartInvService.getOnly(request.getSpareBarCode(), repairOrderEntity.getBaseOrgId());
        if (sparePartInv != null) {
            msg.setSuccess(false);
            msg.setMessage("条码库存已存在，无法入库！");
            return msg;
        }
        sparePartInv = new TmSparePartInv();
        sparePartInv.setFittingCode(outboundInfo.getFittingCode());
        sparePartInv.setInboundTime(new Date());
        sparePartInv.setBarcode(request.getSpareBarCode());
        sparePartInv.setSupplierCode(outboundInfo.getSupplierCode());
        sparePartInv.setPrice(outboundInfo.getPrice());
        sparePartInv.setOrgId(outboundInfo.getOrgId());
        sparePartInv.setRecVer(0);
        sparePartInv.setBaseOrgId(outboundInfo.getBaseOrgId());
        tmSparePartInvService.save(sparePartInv);

        // 保存入库信息
        TmRepairOrderInboundInfoEntity inboundInfo = new TmRepairOrderInboundInfoEntity();
        inboundInfo.setRepairNo(repairOrderEntity.getRepairNo());
        inboundInfo.setFittingCode(outboundInfo.getFittingCode());
        inboundInfo.setBarcode(request.getSpareBarCode());
        inboundInfo.setOperateTime(new Date());
        inboundInfo.setOperator(user.getName());
        inboundInfo.setSupplierCode(outboundInfo.getSupplierCode());
        inboundInfo.setPrice(outboundInfo.getPrice());
        inboundInfo.setOrgId(repairOrderEntity.getOrgId());
        inboundInfo.setRecVer(0);
        inboundInfo.setBaseOrgId(repairOrderEntity.getBaseOrgId());
        tmRepairOrderManager.saveInboundInfo(inboundInfo);

        TMSRF_Repair_ScanSpareBarcode_Response response = new TMSRF_Repair_ScanSpareBarcode_Response();
        BeanUtils.copyProperties(outboundInfo, response);
        msg.setData(response);
        return msg;
    }

    /**
     * 维修单备件出库
     * @param request
     * @return
     */
    @Transactional
    public ResultMessage repairOrderSpareOutbound(TMSRF_Repair_ScanSpareBarcode_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getRepairNo())) {
            msg.setSuccess(false);
            msg.setMessage("维修工单号不能为空！");
            return msg;
        }
        TmRepairOrderEntity repairOrderEntity = tmRepairOrderManager.getByNo(request.getRepairNo(), user.getOffice().getId());
        if (null == repairOrderEntity) {
            msg.setSuccess(false);
            msg.setMessage("维修工单不存在！");
            return msg;
        }
        TmSparePartInvEntity sparePartInv = tmSparePartInvService.getOnlyEntity(request.getSpareBarCode(), repairOrderEntity.getBaseOrgId());
        if (sparePartInv == null) {
            msg.setSuccess(false);
            msg.setMessage("条码库存不存在，无法出库！");
            return msg;
        }
        tmSparePartInvService.deleteByNo(request.getSpareBarCode(), repairOrderEntity.getBaseOrgId());
        // 保存出库信息
        TmRepairOrderOutboundInfoEntity outboundInfo = new TmRepairOrderOutboundInfoEntity();
        outboundInfo.setRepairNo(repairOrderEntity.getRepairNo());
        outboundInfo.setFittingCode(sparePartInv.getFittingCode());
        outboundInfo.setBarcode(request.getSpareBarCode());
        outboundInfo.setOperateTime(new Date());
        outboundInfo.setOperator(user.getName());
        outboundInfo.setSupplierCode(sparePartInv.getSupplierCode());
        outboundInfo.setPrice(sparePartInv.getPrice());
        outboundInfo.setOrgId(repairOrderEntity.getOrgId());
        outboundInfo.setRecVer(0);
        outboundInfo.setBaseOrgId(repairOrderEntity.getBaseOrgId());
        tmRepairOrderManager.saveOutboundInfo(outboundInfo);

        TMSRF_Repair_ScanSpareBarcode_Response response = new TMSRF_Repair_ScanSpareBarcode_Response();
        BeanUtils.copyProperties(sparePartInv, response);
        msg.setData(response);
        return msg;
    }
}
