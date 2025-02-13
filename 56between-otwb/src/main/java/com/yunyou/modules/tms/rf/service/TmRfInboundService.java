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
import com.yunyou.modules.tms.spare.entity.TmSparePoScanInfo;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoScanInfoEntity;
import com.yunyou.modules.tms.spare.manager.TmSparePoManager;
import com.yunyou.modules.tms.spare.service.TmSparePartInvService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * TmsRf入库单service
 * @author ZYF
 * @version 2020/09/16
 */
@Service
public class TmRfInboundService {

    @Autowired
    private TmSparePoManager tmSparePoManager;
    @Autowired
    private TmSparePartInvService tmSparePartInvService;

    /**
     * 备件入库单查询
     * @param request
     * @return
     */
    public ResultMessage querySpareInboundInfo(TMSRF_Inbound_Query_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getSparePoNo()) && StringUtils.isBlank(request.getOperator())) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单号和经办人不能都为空！");
            return msg;
        }
        TmSparePoEntity con = new TmSparePoEntity();
        con.setSparePoNo(request.getSparePoNo());
        con.setOperator(request.getOperator());
        con.setOrgId(user.getOffice().getId());
        List<TmSparePoEntity> entityList = tmSparePoManager.findList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_Inbound_QueryOrder_Response> result = Lists.newArrayList();
            for (TmSparePoEntity poEntity : entityList) {
                TMSRF_Inbound_QueryOrder_Response response = new TMSRF_Inbound_QueryOrder_Response();
                BeanUtils.copyProperties(poEntity, response);
                if (null != poEntity.getOrderTime()) {
                    response.setOrderTime(DateUtils.formatDate(poEntity.getOrderTime(), "yyyy-MM-dd"));
                }
                response.setOrderType(DictUtils.getDictLabel(response.getOrderType(), "TMS_SPARE_ASN_TYPE", ""));
                response.setOrderStatus(DictUtils.getDictLabel(response.getOrderStatus(), "TMS_SPARE_ASN_STATUS", ""));
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("入库单查询结果为空");
        }
        return msg;
    }


    /**
     * 备件入库单明细查询
     * @param request
     * @return
     */
    public ResultMessage querySpareInboundDetail(TMSRF_Inbound_Query_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getSparePoNo())) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单号不能为空！");
            return msg;
        }
        TmSparePoDetailEntity con = new TmSparePoDetailEntity();
        con.setSparePoNo(request.getSparePoNo());
        con.setOrgId(user.getOffice().getId());
        List<TmSparePoDetailEntity> entityList = tmSparePoManager.findList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_Inbound_QueryDetail_Response> result = Lists.newArrayList();
            for (TmSparePoDetailEntity entity : entityList) {
                TMSRF_Inbound_QueryDetail_Response response = new TMSRF_Inbound_QueryDetail_Response();
                BeanUtils.copyProperties(entity, response);

                TmSparePoScanInfoEntity scanCon = new TmSparePoScanInfoEntity();
                scanCon.setSparePoNo(entity.getSparePoNo());
                scanCon.setLineNo(entity.getLineNo());
                scanCon.setFittingCode(entity.getFittingCode());
                List<TmSparePoScanInfoEntity> scanInfoEntityList = tmSparePoManager.findList(scanCon);
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
     * 扫码入库
     * @param request
     * @return
     */
    @Transactional
    public ResultMessage spareInboundScanConfirm(TMSRF_Inbound_Confirm_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getBarcode())) {
            msg.setSuccess(false);
            msg.setMessage("条码不能为空！");
            return msg;
        }
        if (StringUtils.isBlank(request.getSparePoNo()) || StringUtils.isBlank(request.getFittingCode())) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单号和备件不能为空！");
            return msg;
        }
        TmSparePoEntity poEntity = tmSparePoManager.getByNo(request.getSparePoNo(), user.getOffice().getId());
        if (null == poEntity) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单不存在！");
            return msg;
        }
        if (!TmsConstants.SPARE_ASN_STATUS_00.equals(poEntity.getOrderStatus())) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单不是新建状态！");
            return msg;
        }
        TmSparePoDetailEntity detailEntity = tmSparePoManager.getDetailByNoAndFitting(request.getSparePoNo(), request.getFittingCode(), user.getOffice().getId());
        if (null == detailEntity) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单明细不存在！");
            return msg;
        }

        // 备件条码入库
        TmSparePartInv sparePartInv = tmSparePartInvService.getOnly(request.getBarcode(), poEntity.getBaseOrgId());
        if (sparePartInv != null) {
            msg.setSuccess(false);
            msg.setMessage("条码库存已存在，无法入库！");
            return msg;
        }
        sparePartInv = new TmSparePartInv();
        sparePartInv.setFittingCode(detailEntity.getFittingCode());
        sparePartInv.setInboundTime(new Date());
        sparePartInv.setBarcode(request.getBarcode());
        sparePartInv.setSupplierCode(detailEntity.getSupplierCode());
        sparePartInv.setPrice(detailEntity.getPrice());
        sparePartInv.setOrgId(poEntity.getOrgId());
        sparePartInv.setRecVer(0);
        sparePartInv.setBaseOrgId(poEntity.getBaseOrgId());
        tmSparePartInvService.save(sparePartInv);

        // 保存入库单扫码信息
        TmSparePoScanInfo poScanInfo = new TmSparePoScanInfo();
        poScanInfo.setSparePoNo(poEntity.getSparePoNo());
        poScanInfo.setOrderType(poEntity.getOrderType());
        poScanInfo.setLineNo(detailEntity.getLineNo());
        poScanInfo.setFittingCode(detailEntity.getFittingCode());
        poScanInfo.setSupplierCode(detailEntity.getSupplierCode());
        poScanInfo.setPrice(detailEntity.getPrice());
        poScanInfo.setBarcode(request.getBarcode());
        poScanInfo.setOperateTime(new Date());
        poScanInfo.setOperator(user.getName());
        poScanInfo.setOrgId(poEntity.getOrgId());
        poScanInfo.setRecVer(0);
        poScanInfo.setBaseOrgId(poEntity.getBaseOrgId());
        tmSparePoManager.saveScanInfo(poScanInfo);

        // 构建反馈信息
        TMSRF_InOutbound_Confirm_Response response = new TMSRF_InOutbound_Confirm_Response();
        response.setOrderQty(detailEntity.getPoQty());

        TmSparePoScanInfoEntity scanCon = new TmSparePoScanInfoEntity();
        scanCon.setSparePoNo(poEntity.getSparePoNo());
        scanCon.setLineNo(detailEntity.getLineNo());
        scanCon.setFittingCode(detailEntity.getFittingCode());
        List<TmSparePoScanInfoEntity> scanInfoEntityList = tmSparePoManager.findList(scanCon);
        if (CollectionUtil.isNotEmpty(scanInfoEntityList)) {
            response.setScanQty(Double.valueOf(scanInfoEntityList.size()));
        } else {
            response.setScanQty(0d);
        }
        msg.setData(response);
        return msg;
    }

    /**
     * 备件入库单扫码明细查询
     * @param request
     * @return
     */
    public ResultMessage queryScanInfo(TMSRF_InOutbound_QueryScanInfo_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getOrderNo()) || StringUtils.isBlank(request.getFittingCode())) {
            msg.setSuccess(false);
            msg.setMessage("备件入库单号和备件不能为空！");
            return msg;
        }
        TmSparePoScanInfoEntity con = new TmSparePoScanInfoEntity();
        con.setSparePoNo(request.getOrderNo());
        con.setFittingCode(request.getFittingCode());
        con.setOrgId(user.getOffice().getId());
        List<TmSparePoScanInfoEntity> entityList = tmSparePoManager.findList(con);
        if (CollectionUtil.isNotEmpty(entityList)) {
            List<TMSRF_InOutbound_QueryScanInfo_Response> result = Lists.newArrayList();
            for (TmSparePoScanInfoEntity entity : entityList) {
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
