package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.inventory.entity.*;
import com.yunyou.modules.wms.inventory.entity.extend.PrintTfOrder;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmTfHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 转移单Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTfHeaderService extends CrudService<BanQinWmTfHeaderMapper, BanQinWmTfHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    @Lazy
    private BanQinInventoryService inventoryService;
    @Autowired
    private BanQinWmTfDetailService wmTfDetailService;
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private BanQinWmHoldService wmHoldService;
    @Autowired
    private BanQinWmTfSerialService wmTfSerialService;
    @Autowired
    private BanQinInventorySerialService inventorySerialService;
    @Autowired
    @Lazy
    private BanQinInvRemoveService invRemoveService;

    public BanQinWmTfHeaderEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

	public Page<BanQinWmTfHeaderEntity> findPage(Page page, BanQinWmTfHeaderEntity banQinWmTfHeaderEntity) {
        banQinWmTfHeaderEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmTfHeaderEntity.getOrgId()));
        dataRuleFilter(banQinWmTfHeaderEntity);
        banQinWmTfHeaderEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmTfHeaderEntity));
		return page;
	}
	
	public BanQinWmTfHeader findFirst(BanQinWmTfHeader banQinWmTfHeader) {
        List<BanQinWmTfHeader> list = this.findList(banQinWmTfHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
	
	@Override
    @Transactional
	public void save(BanQinWmTfHeader banQinWmTfHeader) {
	    if (StringUtils.isEmpty(banQinWmTfHeader.getId())) {
            banQinWmTfHeader.setTfNo(noService.getDocumentNo(GenNoType.WM_TF_NO.name()));
        }
		super.save(banQinWmTfHeader);
	}

    @Transactional
    public BanQinWmTfHeaderEntity saveEntity(BanQinWmTfHeaderEntity banQinWmTfHeaderEntity) {
        this.save(banQinWmTfHeaderEntity);
        return banQinWmTfHeaderEntity;
    }

    /**
     * 删除转移单
     * @return
     */
	@Transactional
	public ResultMessage removeTfEntity(String[] ids) {
        ResultMessage msg = new ResultMessage();
        for (String id : ids) {
            ResultMessage isRemoveMsg = invRemoveService.removeTf(id);
            // 批量删除，不是为创建状态的拼接返回
            if (!isRemoveMsg.isSuccess()) {
                msg.addMessage(isRemoveMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }
	
    /**
     * 审核
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage audit(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
//            ResultMessage rtMsg = invSerial.checkTfSerialNo(tfNo);
//            if (!rtMsg.isSuccess()) {
//                msg.addMessage(rtMsg.getMessage());
//                continue;
//            }
            ResultMessage detailMsg = auditTf(id);
            if (!detailMsg.isSuccess()) {
                msg.setSuccess(false);
                msg.addMessage(detailMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    @Transactional
    public ResultMessage auditTf(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmTfHeader wmTfHeader = get(id);
        if (wmTfHeader == null) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "数据过期");
            return msg;
        }
        if (!WmsCodeMaster.AD_NEW.getCode().equals(wmTfHeader.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "不是新建状态，不能操作");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmTfHeader.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "已审核");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_NOT.getCode().equals(wmTfHeader.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "不需要审核");
            return msg;
        }
        wmTfHeader.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
        wmTfHeader.setAuditOp(UserUtils.getUser().getName());
        wmTfHeader.setAuditTime(new Date());
        save(wmTfHeader);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 取消审核
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage cancelAudit(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = cancelAuditTf(id);
            if (!detailMsg.isSuccess()) {
                msg.addMessage(detailMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 取消审核
     * @param id
     * @return
     */
    @Transactional
    public ResultMessage cancelAuditTf(String id) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        BanQinWmTfHeader wmTfHeader = get(id);
        if (wmTfHeader == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        if (!WmsCodeMaster.TF_NEW.getCode().equals(wmTfHeader.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "不是新疆状态，不能操作");
            return msg;
        }
        if (!WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmTfHeader.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "不是审核状态");
            return msg;
        }
        wmTfHeader.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
        wmTfHeader.setAuditOp(null);
        wmTfHeader.setAuditTime(null);
        save(wmTfHeader);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 关闭转移单
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage closeOrder(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = close(id);
            if (!detailMsg.isSuccess()) {
                msg.setSuccess(false);
                msg.addMessage(detailMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 关闭
     * @param id
     * @return
     */
    @Transactional
    public ResultMessage close(String id) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        BanQinWmTfHeader wmTfHeader = get(id);
        // 当状态为部分转移或者完全转移时才能对调整单进行关闭
        if (WmsCodeMaster.TF_PART_TRANSFER.getCode().equals(wmTfHeader.getStatus()) || WmsCodeMaster.TF_FULL_TRANSFER.getCode().equals(wmTfHeader.getStatus())) {
            wmTfHeader.setStatus(WmsCodeMaster.TF_CLOSE.getCode());
            save(wmTfHeader);
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "不是转移状态，不能操作");
        }
        return msg;
    }

    /**
     * 取消转移单
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage cancelOrder(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                cancel(id);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        } else {
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    /**
     * 取消
     * @param id
     * @throws WarehouseException
     */
    @Transactional
    public void cancel(String id) throws WarehouseException {
        BanQinWmTfHeader wmTfHeader = get(id);
        if (wmTfHeader == null) {
            throw new WarehouseException("数据过期");
        }
        if (!WmsCodeMaster.ASN_NEW.getCode().equals(wmTfHeader.getStatus())) {
            throw new WarehouseException(wmTfHeader.getTfNo() + "不是创建状态，不能操作");
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmTfHeader.getAuditStatus())) {
            throw new WarehouseException(wmTfHeader.getTfNo() + "已审核，不能操作");
        }
        wmTfHeader.setStatus(WmsCodeMaster.TF_CANCEL.getCode());
        save(wmTfHeader);
    }

    /**
     * 批量执行库存转移
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage transfer(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = checkStatus(id);
            if (!detailMsg.isSuccess()) {
                msg.addMessage(detailMsg.getMessage());
                continue;
            }
            BanQinWmTfHeader wmTfHeader = (BanQinWmTfHeader) detailMsg.getData();
            // 不审核时，需要校验序列号管理商品，转移数和序列号转移数是否匹配
            if (WmsCodeMaster.AUDIT_NOT.getCode().equals(wmTfHeader.getAuditStatus())) {
//                ResultMessage rtMsg = invSerial.checkTfSerialNo(tfNo);
//                if (!rtMsg.isSuccess()) {
//                    msg.addMessage(rtMsg.getMessagesAsString());
//                    continue;
//                }
            }
            List<BanQinWmTfDetail> wmTfDetailModels = wmTfDetailService.findByHeaderId(wmTfHeader.getId(), wmTfHeader.getOrgId());
            for (BanQinWmTfDetail wmTfDetail : wmTfDetailModels) {
                invTransfer(wmTfDetail, wmTfHeader);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 执转移单前，校验转移是否满足条件
     * @param id
     * @return
     */
    public ResultMessage checkStatus(String id) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        // 获取调整单头信息
        BanQinWmTfHeader wmTfHeader = get(id);
        if (wmTfHeader == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        // 当状态不为创建或者部分调整，说明状态已经发生变化，不能执行调整
        if (!WmsCodeMaster.TF_PART_TRANSFER.getCode().equals(wmTfHeader.getStatus()) && !WmsCodeMaster.TF_NEW.getCode().equals(wmTfHeader.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "状态不为创建或者部分调整");
            return msg;
        }
        // 当调整单审核状态为未审核，不能执行调整
        if (WmsCodeMaster.AUDIT_NEW.getCode().equals(wmTfHeader.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "状态为未审核，不能执行调整");
            return msg;
        }
        msg.setData(wmTfHeader);
        return msg;
    }

    /**
     * 库存转移业务逻辑
     * @param wmTfDetail
     * @param wmTfHeader
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage invTransfer(BanQinWmTfDetail wmTfDetail, BanQinWmTfHeader wmTfHeader) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // set源转移信息
        BanQinInventoryEntity fminvm = getFmInvModel(wmTfDetail);
        // 校验冻结是否可调整
        BanQinWmInvLotLoc wmInvLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(wmTfDetail.getFmLot(), wmTfDetail.getFmLoc(), wmTfDetail.getFmId(), wmTfDetail.getOrgId());
        if (wmInvLotLocModel != null && wmInvLotLocModel.getQtyHold() > 0) {
            ResultMessage msgHold = wmHoldService.checkHold(wmInvLotLocModel, WmsConstants.TF);
            if (msgHold.isSuccess()) {
                fminvm.setQtyHold(wmTfDetail.getFmQtyEa());
            } else {
                throw new WarehouseException("已冻结");
            }
        }
        // set目标转移信息
        BanQinInventoryEntity toinvm = getToInvModel(wmTfDetail);
        // 调用更新库存
        BanQinInventoryEntity inventoryEntity = inventoryService.updateInventory(fminvm, toinvm);
        // 更新转移单明细状态
        wmTfDetail.setStatus(WmsCodeMaster.TF_FULL_TRANSFER.getCode());
        wmTfDetailService.save(wmTfDetail);
        // 更新转移单状态
        BanQinWmTfDetail exampleAdDetail = new BanQinWmTfDetail();
        exampleAdDetail.setTfNo(wmTfHeader.getTfNo());
        exampleAdDetail.setStatus(WmsCodeMaster.TF_NEW.getCode());
        exampleAdDetail.setOrgId(wmTfHeader.getOrgId());
        List<BanQinWmTfDetail> adDetails = wmTfDetailService.findList(exampleAdDetail);
        // 当转移单明细还有创建，更新转移单为部分创建
        wmTfHeader = this.get(wmTfHeader.getId());
        if (adDetails != null && adDetails.size() > 0) {
            wmTfHeader.setStatus(WmsCodeMaster.TF_PART_TRANSFER.getCode());
        } else {
            wmTfHeader.setStatus(WmsCodeMaster.TF_FULL_TRANSFER.getCode());
        }
        this.save(wmTfHeader);
        // 更新序列号库存
        BanQinWmTfSerial example = new BanQinWmTfSerial();
        example.setTfNo(wmTfDetail.getTfNo());
        example.setLineNo(wmTfDetail.getLineNo());
        example.setOrgId(wmTfDetail.getOrgId());
        List<BanQinWmTfSerial> wmTfSerialModels = wmTfSerialService.findList(example);
        for (BanQinWmTfSerial wmTfSerialModel : wmTfSerialModels) {
            BanQinInventorySerialEntity entity = new BanQinInventorySerialEntity();
            entity.setOwnerCode(wmTfSerialModel.getOwnerCode());
            entity.setSkuCode(wmTfSerialModel.getSkuCode());
            entity.setLotNum(wmTfSerialModel.getLotNum());
            entity.setSerialNo(wmTfSerialModel.getSerialNo());
            // 序列号转入
            if (WmsCodeMaster.O.getCode().equals(wmTfSerialModel.getTfMode())) {
                entity.setSerialTranType(WmsCodeMaster.TOUT.getCode());
            } else {// 序列号转出
                entity.setSerialTranType(WmsCodeMaster.TIN.getCode());
            }
            entity.setOrderNo(wmTfSerialModel.getTfNo());
            entity.setLineNo(wmTfSerialModel.getLineNo());
            entity.setTranId(inventoryEntity.getTranId());
            entity.setOrgId(wmTfSerialModel.getOrgId());
            inventorySerialService.updateInventorySerial(entity);
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * set源转移信息
     * @param wmTfDetail
     * @return
     */
    protected BanQinInventoryEntity getFmInvModel(BanQinWmTfDetail wmTfDetail) {
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setLotNum(wmTfDetail.getFmLot());
        fminvm.setLocCode(wmTfDetail.getFmLoc());
        fminvm.setTraceId(wmTfDetail.getFmId());
        fminvm.setSkuCode(wmTfDetail.getFmSku());
        fminvm.setOwnerCode(wmTfDetail.getFmOwner());
        fminvm.setOrderNo(wmTfDetail.getTfNo());
        fminvm.setLineNo(wmTfDetail.getLineNo());
        fminvm.setAction(ActionCode.TRANSFER);
        fminvm.setPackCode(wmTfDetail.getFmPack());
        fminvm.setQtyUom(wmTfDetail.getFmQtyUom());
        fminvm.setUom(wmTfDetail.getFmUom());
        fminvm.setQtyEaOp(wmTfDetail.getFmQtyEa());
        fminvm.setOrgId(wmTfDetail.getOrgId());
        return fminvm;
    }

    /**
     * set目标转移信息
     */
    protected BanQinInventoryEntity getToInvModel(BanQinWmTfDetail wmTfDetail) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setLotNum(wmTfDetail.getToLot());
        toinvm.setLocCode(wmTfDetail.getToLoc());
        toinvm.setTraceId(wmTfDetail.getToId());
        toinvm.setSkuCode(wmTfDetail.getToSku());
        toinvm.setOwnerCode(wmTfDetail.getToOwner());
        toinvm.setOrderNo(wmTfDetail.getTfNo());
        toinvm.setLineNo(wmTfDetail.getLineNo());
        toinvm.setAction(ActionCode.TRANSFER);
        toinvm.setPackCode(wmTfDetail.getToPack());
        toinvm.setQtyUom(wmTfDetail.getToQtyUom());
        toinvm.setUom(wmTfDetail.getToUom());
        toinvm.setQtyEaOp(wmTfDetail.getToQtyEa());
        toinvm.setOrgId(wmTfDetail.getOrgId());
        return toinvm;
    }

    /**
     * 明细行执行库存转移
     * @param headerId
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage transferDetail(String headerId, String ids) {
        ResultMessage msg = new ResultMessage();
        // 校验转移单头状态
        ResultMessage detailMsg = checkStatus(headerId);
        if (!detailMsg.isSuccess()) {
            msg.setSuccess(false);
            msg.addMessage(detailMsg.getMessage());
            return msg;
        }
        BanQinWmTfHeader wmTfHeader = (BanQinWmTfHeader) detailMsg.getData();
        // 不审核时，需要校验序列号管理商品，转移数和序列号转移数是否匹配
        if (WmsCodeMaster.AUDIT_NOT.getCode().equals(wmTfHeader.getAuditStatus())) {
//            Message rtMsg = invSerial.checkTfSerialNo(tfNo);
//            if (!rtMsg.isSuccess()) {
//                msg.addMessage(rtMsg.getMessagesAsString());
//                Message getEntityMsg = getEntityByTfNo(tfNo);
//                msg.setData(getEntityMsg.getData());
//                return msg;
//            }
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BanQinWmTfDetail wmTfDetail = wmTfDetailService.get(id);
            if (wmTfDetail == null) {
                msg.addMessage("[" + wmTfDetail.getTfNo() + "][" + wmTfDetail.getLineNo() + "]" + "数据过期");
                continue;
            }
            if (!wmTfDetail.getStatus().equals(WmsCodeMaster.TF_NEW.getCode())) {
                msg.addMessage("[" + wmTfDetail.getTfNo() + "][" + wmTfDetail.getLineNo() + "]" + "不是新建状态");
                continue;
            }
            try {
                invTransfer(wmTfDetail, wmTfHeader);
            } catch (WarehouseException e) {
                e.printStackTrace();
                msg.setSuccess(false);
                msg.addMessage("[" + wmTfDetail.getTfNo() + "][" + wmTfDetail.getLineNo() + "]" + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        } else {
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    public ResultMessage cancelTfDetailById(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmTfDetail wmTfDetailModel = wmTfDetailService.get(id);
        if (wmTfDetailModel == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        if (WmsCodeMaster.AD_NEW.getCode().equals(wmTfDetailModel.getStatus())) {
            wmTfDetailModel.setStatus(WmsCodeMaster.TF_CANCEL.getCode());
            wmTfDetailService.save(wmTfDetailModel);
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            msg.addMessage(wmTfDetailModel.getLineNo() + "不是新建状态");
        }
        return msg;
    }

    /**
     * 取消转移单明细
     * @param headerId
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage cancelDetail(String headerId, String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        ResultMessage headerMsg = checkNewAndNotAudit(headerId);
        if (!headerMsg.isSuccess()) {
            msg.setSuccess(false);
            msg.addMessage(headerMsg.getMessage());
            return msg;
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = cancelTfDetailById(id);
            if (!detailMsg.isSuccess()) {
                msg.setSuccess(false);
                msg.addMessage(detailMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 校验是否创建 未审核
     * @param headerId
     * @return
     */
    public ResultMessage checkNewAndNotAudit(String headerId) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        BanQinWmTfHeader wmTfHeader = get(headerId);
        if (wmTfHeader == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        if (!WmsCodeMaster.TF_NEW.getCode().equals(wmTfHeader.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "不是新建状态");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmTfHeader.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeader.getTfNo() + "已审核");
            return msg;
        }
        msg.setData(wmTfHeader);
        return msg;
    }

    public List<PrintTfOrder> getTfOrder(String[] ids) {
        return mapper.getTfOrder(ids);
    }
}