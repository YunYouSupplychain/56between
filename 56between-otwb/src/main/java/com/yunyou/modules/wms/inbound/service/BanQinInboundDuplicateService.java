package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：入库模块功能的复制Service
 *
 * @auther: Jianhua on 2019/1/30
 */
@Service
@Transactional(readOnly = true)
public class BanQinInboundDuplicateService extends BaseService {
    @Autowired
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private BanQinWmPoHeaderService banQinWmPoHeaderService;
    @Autowired
    private BanQinWmPoDetailService banQinWmPoDetailService;
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;

    /**
     * 描述： 复制PO
     *
     * @param poNo  PO单号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage duplicatePo(String poNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmPoEntity wmPoEntity = banQinWmPoHeaderService.findEntityByPoNo(poNo, orgId);
        String newPoNo = "";
        if (null != wmPoEntity) {
            // 复制PO单头
            BanQinWmPoHeader model = new BanQinWmPoHeader();
            BeanUtils.copyProperties(model, wmPoEntity);
            model.setId(null);
            model.setLogisticNo(null);
            msg = banQinWmPoHeaderService.savePoHeader(model);
            model = (BanQinWmPoHeader) msg.getData();
            newPoNo = model.getPoNo();
            if (msg.isSuccess()) {
                // 复制PO明细
                List<BanQinWmPoDetailEntity> WmPoDetailList = wmPoEntity.getWmPoDetailEntitys();
                for (BanQinWmPoDetailEntity wmPoDetailEntity : WmPoDetailList) {
                    BanQinWmPoDetail wmPoDetailModel = new BanQinWmPoDetail();
                    BeanUtils.copyProperties(wmPoDetailEntity, wmPoDetailModel);
                    wmPoDetailModel.setId(null);
                    wmPoDetailModel.setPoNo(newPoNo);
                    wmPoDetailModel.setLogisticLineNo(null);
                    wmPoDetailModel.setLogisticNo(null);
                    banQinWmPoDetailService.savePoDetail(wmPoDetailModel);
                }
            }
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 复制Asn
     * @param asnId
     * @return
     */
    @Transactional
    public ResultMessage duplicateAsn(String asnId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAsnEntity wmAsnEntity = banQinWmAsnHeaderService.getEntity(asnId);
        String newAsnNo = "";
        if (null != wmAsnEntity) {
            wmAsnEntity.setWmAsnDetailEntities(banQinWmAsnDetailService.getEntityByAsnNo(wmAsnEntity.getAsnNo(), wmAsnEntity.getOrgId()));
            // 复制单头
            BanQinWmAsnHeader model = new BanQinWmAsnHeader();
            BeanUtils.copyProperties(wmAsnEntity, model);
            model.setId(null);
            model.setLogisticNo(null);
            model.setQcStatus(null);
            msg = banQinInboundOperationService.saveAsnHeader(model);
            model = (BanQinWmAsnHeader) msg.getData();
            newAsnNo = model.getAsnNo();
            if (msg.isSuccess()) {
                // 复制明细
                List<BanQinWmAsnDetailEntity> WmAsnDetailList = wmAsnEntity.getWmAsnDetailEntities();
                for (BanQinWmAsnDetailEntity wmAsnDetailEntity : WmAsnDetailList) {
                    BanQinWmAsnDetail wmAsnDetailModel = new BanQinWmAsnDetail();
                    BeanUtils.copyProperties(wmAsnDetailEntity, wmAsnDetailModel);
                    wmAsnDetailModel.setAsnNo(newAsnNo);
                    wmAsnDetailModel.setId(null);
                    wmAsnDetailModel.setPoNo(null);
                    wmAsnDetailModel.setPoLineNo(null);
                    wmAsnDetailModel.setLogisticLineNo(null);
                    wmAsnDetailModel.setLogisticNo(null);
                    wmAsnDetailModel.setQcStatus(null);
                    wmAsnDetailModel.setHeadId(model.getId());
                    wmAsnDetailModel = banQinInboundOperationService.saveAsnDetail(wmAsnDetailModel);
                    // 复制收货明细
                    BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
                    BeanUtils.copyProperties(wmAsnDetailEntity, wmAsnDetailReceiveModel);
                    wmAsnDetailReceiveModel.setAsnNo(newAsnNo);
                    wmAsnDetailReceiveModel.setId(null);
                    wmAsnDetailReceiveModel.setPoNo(null);
                    wmAsnDetailReceiveModel.setPoLineNo(null);
                    wmAsnDetailReceiveModel.setLogisticLineNo(null);
                    wmAsnDetailReceiveModel.setLogisticNo(null);
                    wmAsnDetailReceiveModel.setQcStatus(null);
                    wmAsnDetailReceiveModel.setAsnLineNo(wmAsnDetailModel.getLineNo());
                    wmAsnDetailReceiveModel.setAsnNo(wmAsnDetailModel.getAsnNo());
                    wmAsnDetailReceiveModel.setQtyPlanEa(wmAsnDetailModel.getQtyAsnEa());
                    wmAsnDetailReceiveModel.setQtyRcvEa(0D);
                    wmAsnDetailReceiveModel.setLotNum(null);
                    wmAsnDetailReceiveModel.setPlanId(wmAsnDetailModel.getTraceId());
                    wmAsnDetailReceiveModel.setToId(wmAsnDetailModel.getTraceId());
                    wmAsnDetailReceiveModel.setToLoc(wmAsnDetailModel.getPlanToLoc());
                    wmAsnDetailReceiveModel.setHeadId(model.getId());
                    banQinInboundOperationService.saveAsnDetailReceive(wmAsnDetailReceiveModel);
                }
            }
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功, 新的单号为：" + newAsnNo);
        return msg;
    }

    /**
     * 复制ASN明细及收货明细
     * @param asnNo
     * @param lineNo
     * @param orgId
     * @return
     */
    @Transactional
    public ResultMessage duplicateAsnDetail(String asnNo, String lineNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        // ASN单头
        BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(asnNo, orgId);
        if (StringUtils.isNotEmpty(wmAsnHeaderModel.getStatus()) && !WmsCodeMaster.ASN_NEW.getCode().equals(wmAsnHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(asnNo + "不是创建状态，不能操作");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAsnHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(asnNo + "已审核，不能操作");
            return msg;
        }
        String holdStatus = wmAsnHeaderModel.getHoldStatus();
        if (holdStatus.equals(WmsCodeMaster.ODHL_HOLD.getCode())) {
            msg.setSuccess(false);
            msg.addMessage(asnNo + "订单冻结，不能操作");
            return msg;
        }

        BanQinWmAsnDetail model = banQinWmAsnDetailService.getByAsnNoAndLineNo(asnNo, lineNo, orgId);
        // 复制明细
        BanQinWmAsnDetail wmAsnDetailModel = new BanQinWmAsnDetail();
        BeanUtils.copyProperties(model, wmAsnDetailModel);
        wmAsnDetailModel.setId(null);
        wmAsnDetailModel.setPoNo(null);
        wmAsnDetailModel.setPoLineNo(null);
        wmAsnDetailModel.setLogisticLineNo(null);
        wmAsnDetailModel.setLogisticNo(null);
        wmAsnDetailModel.setQcStatus(null);
        wmAsnDetailModel = banQinInboundOperationService.saveAsnDetail(wmAsnDetailModel);
        // 复制收货明细
        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
        BeanUtils.copyProperties(model, wmAsnDetailReceiveModel);
        wmAsnDetailReceiveModel.setId(null);
        wmAsnDetailReceiveModel.setPoNo(null);
        wmAsnDetailReceiveModel.setPoLineNo(null);
        wmAsnDetailReceiveModel.setLogisticLineNo(null);
        wmAsnDetailReceiveModel.setLogisticNo(null);
        wmAsnDetailReceiveModel.setQcStatus(null);
        wmAsnDetailReceiveModel.setAsnLineNo(wmAsnDetailModel.getLineNo());
        wmAsnDetailReceiveModel.setAsnNo(wmAsnDetailModel.getAsnNo());
        wmAsnDetailReceiveModel.setQtyPlanEa(wmAsnDetailModel.getQtyAsnEa());
        wmAsnDetailReceiveModel.setQtyRcvEa(0D);
        wmAsnDetailReceiveModel.setLotNum(null);
        wmAsnDetailReceiveModel.setPlanId(wmAsnDetailModel.getTraceId());
        wmAsnDetailReceiveModel.setToId(wmAsnDetailModel.getTraceId());
        wmAsnDetailReceiveModel.setToLoc(wmAsnDetailModel.getPlanToLoc());
        banQinInboundOperationService.saveAsnDetailReceive(wmAsnDetailReceiveModel);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }
}
