package com.yunyou.modules.tms.rf.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.rf.entity.*;
import com.yunyou.modules.tms.spare.entity.TmSparePartInv;
import com.yunyou.modules.tms.spare.entity.extend.*;
import com.yunyou.modules.tms.spare.manager.TmSpareSoManager;
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
public class TmRfOutboundService {

    @Autowired
    private TmSpareSoManager tmSpareSoManager;
    @Autowired
    private TmSparePartInvService tmSparePartInvService;

    /**
     * 备件出库单查询
     * @param request
     * @return
     */
    public ResultMessage querySpareOutboundInfo(TMSRF_Outbound_Query_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getSpareSoNo()) && StringUtils.isBlank(request.getOperator())) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单号和经办人不能都为空！");
            return msg;
        }
        TmSpareSoEntity con = new TmSpareSoEntity();
        con.setSpareSoNo(request.getSpareSoNo());
        con.setOperator(request.getOperator());
        con.setOrgId(user.getOffice().getId());
        List<TmSpareSoEntity> entityList = tmSpareSoManager.findList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_Outbound_QueryOrder_Response> result = Lists.newArrayList();
            for (TmSpareSoEntity entity : entityList) {
                TMSRF_Outbound_QueryOrder_Response response = new TMSRF_Outbound_QueryOrder_Response();
                BeanUtils.copyProperties(entity, response);
                if (null != entity.getOrderTime()) {
                    response.setOrderTime(DateUtils.formatDate(entity.getOrderTime(), "yyyy-MM-dd"));
                }
                response.setOrderType(DictUtils.getDictLabel(response.getOrderType(), "TMS_SPARE_SO_TYPE", ""));
                response.setOrderStatus(DictUtils.getDictLabel(response.getOrderStatus(), "TMS_SPARE_SO_STATUS", ""));
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("出库单查询结果为空");
        }
        return msg;
    }


    /**
     * 备件出库单明细查询
     * @param request
     * @return
     */
    public ResultMessage querySpareOutboundDetail(TMSRF_Outbound_Query_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getSpareSoNo())) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单号不能为空！");
            return msg;
        }
        TmSpareSoDetailEntity con = new TmSpareSoDetailEntity();
        con.setSpareSoNo(request.getSpareSoNo());
        con.setOrgId(user.getOffice().getId());
        List<TmSpareSoDetailEntity> entityList = tmSpareSoManager.findList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_Outbound_QueryDetail_Response> result = Lists.newArrayList();
            for (TmSpareSoDetailEntity entity : entityList) {
                TMSRF_Outbound_QueryDetail_Response response = new TMSRF_Outbound_QueryDetail_Response();
                BeanUtils.copyProperties(entity, response);

                TmSpareSoScanInfoEntity scanCon = new TmSpareSoScanInfoEntity();
                scanCon.setSpareSoNo(entity.getSpareSoNo());
                scanCon.setLineNo(entity.getLineNo());
                scanCon.setFittingCode(entity.getFittingCode());
                List<TmSpareSoScanInfoEntity> scanInfoEntityList = tmSpareSoManager.findList(scanCon);
                if (CollectionUtil.isNotEmpty(scanInfoEntityList)) {
                    response.setScanQty(Double.valueOf(scanInfoEntityList.size()));
                } else {
                    response.setScanQty(0d);
                }
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("明细查询结果为空");
        }
        return msg;
    }

    /**
     * 扫码出库
     * @param request
     * @return
     */
    @Transactional
    public ResultMessage spareOutboundScanConfirm(TMSRF_Outbound_Confirm_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getBarcode())) {
            msg.setSuccess(false);
            msg.setMessage("条码不能为空！");
            return msg;
        }
        if (StringUtils.isBlank(request.getSpareSoNo()) || StringUtils.isBlank(request.getFittingCode())) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单号和备件不能为空！");
            return msg;
        }
        TmSpareSoEntity soEntity = tmSpareSoManager.getByNo(request.getSpareSoNo(), user.getOffice().getId());
        if (null == soEntity) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单不存在！");
            return msg;
        }
        if (!TmsConstants.SPARE_SO_STATUS_00.equals(soEntity.getOrderStatus())) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单不是新建状态！");
            return msg;
        }
        TmSpareSoDetailEntity detailEntity = tmSpareSoManager.getDetailByNoAndFitting(request.getSpareSoNo(), request.getFittingCode(), user.getOffice().getId());
        if (null == detailEntity) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单明细不存在！");
            return msg;
        }
        // 条码出库
        TmSparePartInv sparePartInv = tmSparePartInvService.getOnly(request.getBarcode(), soEntity.getBaseOrgId());
        if (sparePartInv == null) {
            msg.setSuccess(false);
            msg.setMessage("条码库存不存在，无法出库！");
            return msg;
        }
        tmSparePartInvService.deleteByNo(request.getBarcode(), soEntity.getBaseOrgId());

        // 保存出库单扫码信息
        TmSpareSoScanInfoEntity scanInfo = new TmSpareSoScanInfoEntity();
        scanInfo.setSpareSoNo(soEntity.getSpareSoNo());
        scanInfo.setOrderType(soEntity.getOrderType());
        scanInfo.setLineNo(detailEntity.getLineNo());
        scanInfo.setFittingCode(detailEntity.getFittingCode());
        scanInfo.setSupplierCode(detailEntity.getSupplierCode());
        scanInfo.setPrice(detailEntity.getPrice() + "");
        scanInfo.setBarcode(request.getBarcode());
        scanInfo.setOperateTime(new Date());
        scanInfo.setOperator(user.getName());
        scanInfo.setOrgId(soEntity.getOrgId());
        scanInfo.setRecVer(0);
        scanInfo.setBaseOrgId(soEntity.getBaseOrgId());
        tmSpareSoManager.saveScanInfo(scanInfo);

        // 构建反馈信息
        TMSRF_InOutbound_Confirm_Response response = new TMSRF_InOutbound_Confirm_Response();
        response.setOrderQty(detailEntity.getSoQty());

        TmSpareSoScanInfoEntity scanCon = new TmSpareSoScanInfoEntity();
        scanCon.setSpareSoNo(soEntity.getSpareSoNo());
        scanCon.setLineNo(detailEntity.getLineNo());
        scanCon.setFittingCode(detailEntity.getFittingCode());
        List<TmSpareSoScanInfoEntity> scanInfoEntityList = tmSpareSoManager.findList(scanCon);
        if (CollectionUtil.isNotEmpty(scanInfoEntityList)) {
            response.setScanQty(Double.valueOf(scanInfoEntityList.size()));
        } else {
            response.setScanQty(0d);
        }
        msg.setData(response);
        return msg;
    }

    /**
     * 备件出库单扫码明细查询
     * @param request
     * @return
     */
    public ResultMessage queryScanInfo(TMSRF_InOutbound_QueryScanInfo_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getOrderNo()) || StringUtils.isBlank(request.getFittingCode())) {
            msg.setSuccess(false);
            msg.setMessage("备件出库单号和备件不能为空！");
            return msg;
        }
        TmSpareSoScanInfoEntity con = new TmSpareSoScanInfoEntity();
        con.setSpareSoNo(request.getOrderNo());
        con.setFittingCode(request.getFittingCode());
        con.setOrgId(user.getOffice().getId());
        List<TmSpareSoScanInfoEntity> entityList = tmSpareSoManager.findList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_InOutbound_QueryScanInfo_Response> result = Lists.newArrayList();
            for (TmSpareSoScanInfoEntity entity : entityList) {
                TMSRF_InOutbound_QueryScanInfo_Response response = new TMSRF_InOutbound_QueryScanInfo_Response();
                BeanUtils.copyProperties(entity, response);
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }
}
