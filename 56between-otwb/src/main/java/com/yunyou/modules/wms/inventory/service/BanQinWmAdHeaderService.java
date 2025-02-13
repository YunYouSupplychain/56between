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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.inventory.entity.*;
import com.yunyou.modules.wms.inventory.entity.extend.PrintAdOrder;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmAdHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 调整单Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAdHeaderService extends CrudService<BanQinWmAdHeaderMapper, BanQinWmAdHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private BanQinWmAdDetailService wmAdDetailService;
    @Autowired
    private BanQinWmAdSerialService wmAdSerialService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinWmHoldService wmHoldService;
    @Autowired
    private BanQinInventoryService inventoryService;
    @Autowired
    private BanQinWmInvSerialService wmInvSerialService;
    @Autowired
    private BanQinInventorySerialService inventorySerialService;
    @Autowired
    @Lazy
    private BanQinInvRemoveService invRemoveService;

	public BanQinWmAdHeaderEntity getEntity(String id) {
	    return mapper.getEntity(id);
    }

	public Page<BanQinWmAdHeaderEntity> findPage(Page page, BanQinWmAdHeaderEntity banQinWmAdHeaderEntity) {
        banQinWmAdHeaderEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmAdHeaderEntity.getOrgId()));
        dataRuleFilter(banQinWmAdHeaderEntity);
        banQinWmAdHeaderEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmAdHeaderEntity));
	    return page;
	}
	
	public BanQinWmAdHeader findFirst(BanQinWmAdHeader banQinWmAdHeader) {
        List<BanQinWmAdHeader> list = this.findList(banQinWmAdHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
	@Override
    @Transactional
	public void save(BanQinWmAdHeader banQinWmAdHeader) {
	    if (StringUtils.isEmpty(banQinWmAdHeader.getId())) {
            banQinWmAdHeader.setAdNo(noService.getDocumentNo(GenNoType.WM_AD_NO.name()));
        }
        super.save(banQinWmAdHeader);
    }

    @Transactional
    public BanQinWmAdHeaderEntity saveEntity(BanQinWmAdHeaderEntity banQinWmAdHeaderEntity) {
	    this.save(banQinWmAdHeaderEntity);
	    return banQinWmAdHeaderEntity;
    }

    /**
     * 删除调整单
     * @param ids
     */
    @Transactional
    public ResultMessage removeAdEntity(String[] ids) {
        ResultMessage msg = new ResultMessage();
        for (String id : ids) {
            try {
                ResultMessage isRemoveMsg = invRemoveService.removeAd(id);
                // 批量删除，不是为创建状态的拼接返回
                if (!isRemoveMsg.isSuccess()) {
                    msg.setSuccess(false);
                    msg.addMessage(isRemoveMsg.getData() + "");
                }
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage(e.getMessage());
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
	
    /**
     * 批量审核
     * @param ids
     */
    @Transactional
    public ResultMessage auditAd(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BanQinWmAdHeader wmAdHeaderModel = get(id);
            if (wmAdHeaderModel == null) {
                msg.setSuccess(false);
                msg.addMessage("数据过期");
                return msg;
            }
            // 有序列号管理商品，校验批次下调整数量和序列号调整数量绝对值是否匹配
            ResultMessage rtMsg = wmInvSerialService.checkAdSerialNo(wmAdHeaderModel.getAdNo(), wmAdHeaderModel.getOrgId());
            if (!rtMsg.isSuccess()) {
                msg.addMessage(rtMsg.getMessage());
                continue;
            }
            ResultMessage detailMsg = auditSingle(id);
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
     * 单条审核
     * @param id
     */
    @Transactional
    public ResultMessage auditSingle(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAdHeader wmAdHeaderModel = get(id);
        if (wmAdHeaderModel == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        if (!WmsCodeMaster.AD_NEW.getCode().equals(wmAdHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "不是新建状态");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAdHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "已审核");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_NOT.getCode().equals(wmAdHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "不需要审核");
            return msg;
        }
        wmAdHeaderModel.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
        wmAdHeaderModel.setAuditOp(UserUtils.getUser().getName());
        wmAdHeaderModel.setAuditTime(new Date());
        save(wmAdHeaderModel);
        msg.setSuccess(true);
        return msg;
    }

    @Transactional
    public ResultMessage cancelAuditAd(String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = cancelAuditSingle(id);
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
    public ResultMessage cancelAuditSingle(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAdHeader wmAdHeaderModel = get(id);
        if (wmAdHeaderModel == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        if (!WmsCodeMaster.AD_NEW.getCode().equals(wmAdHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "不是新建状态");
            return msg;
        }
        if (!WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAdHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "不是已审核状态");
            return msg;
        }
        wmAdHeaderModel.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
        wmAdHeaderModel.setAuditOp(null);
        wmAdHeaderModel.setAuditTime(null);
        super.save(wmAdHeaderModel);
        msg.setSuccess(true);
        return msg;
    }

    @Transactional
    public ResultMessage adjust(String ids) {
        ResultMessage msg = new ResultMessage();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            // 获取调整单头信息
            BanQinWmAdHeader wmAdHeader = get(id);
            // 序列号管理商品
            Map<String, List<BanQinWmAdDetail>> serialMap = new HashMap<>();
            ResultMessage detailMsg = checkStatus(wmAdHeader);
            if (!detailMsg.isSuccess()) {
                msg.setSuccess(false);
                msg.addMessage(detailMsg.getMessage());
                continue;
            }
            // 不审核时，有序列号管理商品，校验批次下调整数量和序列号调整数量绝对值是否匹配
            if (WmsCodeMaster.AUDIT_NOT.getCode().equals(wmAdHeader.getAuditStatus())) {
                ResultMessage rtMsg = wmInvSerialService.checkAdSerialNo(wmAdHeader.getAdNo(), wmAdHeader.getOrgId());
                if (!rtMsg.isSuccess()) {
                    msg.addMessage(rtMsg.getMessage());
                    continue;
                }
            }

            List<BanQinWmAdDetail> wmAdDetailModels = wmAdDetailService.findByHeaderId(id, wmAdHeader.getOrgId());
            wmAdDetailModels = wmAdDetailModels.stream().filter(d -> d.getStatus().equals(WmsCodeMaster.AD_NEW.getCode())).collect(Collectors.toList());
            for (BanQinWmAdDetail wmAdDetail : wmAdDetailModels) {
                // 是否序列号管理
                BanQinCdWhSku cdWhSkuModel = cdWhSkuService.getByOwnerAndSkuCode(wmAdDetail.getOwnerCode(), wmAdDetail.getSkuCode(), wmAdDetail.getOrgId());
                if (cdWhSkuModel != null && WmsConstants.YES.equals(cdWhSkuModel.getIsSerial())) {
                    if (serialMap.containsKey(wmAdDetail.getLotNum())) {
                        serialMap.get(wmAdDetail.getLotNum()).add(wmAdDetail);
                    } else {
                        serialMap.put(wmAdDetail.getLotNum(), new ArrayList<BanQinWmAdDetail>(){{add(wmAdDetail);}});
                    }
                    continue;
                }
                try {
                    invAdjustment(wmAdDetail, get(id));
                } catch (WarehouseException e) {
                    msg.setSuccess(false);
                    msg.addMessage("[" + wmAdHeader.getAdNo() + "][" + wmAdDetail.getLineNo() + "]" + e.getMessage() + "<br>");
                    continue;
                }
            }
            // 执行序列号管理商品
            Iterator<String> it = serialMap.keySet().iterator();
            // 循环遍历批次
            while (it.hasNext()) {
                // 批次号
                String key = it.next();
                List<BanQinWmAdDetail> itemList = serialMap.get(key);
                try {
                    invSerialAdjustment(itemList, key, wmAdHeader);
                } catch (WarehouseException e) {
                    msg.setSuccess(false);
                    msg.addMessage(e.getMessage());
                    continue;
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.setMessage("操作成功");
        } else {
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    public ResultMessage checkStatus(BanQinWmAdHeader wmAdHeaderModel) {
        ResultMessage msg = new ResultMessage();
        if (wmAdHeaderModel == null) {
            msg.setSuccess(false);
            msg.addMessage("数据过期");
            return msg;
        }
        // 当状态不为创建或者部分调整，说明状态已经发生变化，不能执行调整
        if (!WmsCodeMaster.AD_PART_ADJUSTMENT.getCode().equals(wmAdHeaderModel.getStatus()) && !WmsCodeMaster.AD_NEW.getCode().equals(wmAdHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "状态不为创建或者部分调整");
            return msg;
        }
        // 当调整单审核状态为未审核，不能执行调整
        if (WmsCodeMaster.AUDIT_NEW.getCode().equals(wmAdHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "审核状态为未审核，不能执行调整");
            return msg;
        }
        return msg;
    }

    @Transactional
    public ResultMessage invAdjustment(BanQinWmAdDetail wmAdDetail, BanQinWmAdHeader wmAdHeader) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 校验冻结是否可调整
        BanQinWmInvLotLoc wmInvLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(wmAdDetail.getLotNum(), wmAdDetail.getLocCode(), wmAdDetail.getTraceId(), wmAdDetail.getOrgId());
        // 获取更新库存数据
        BanQinInventoryEntity inventoryEntity = getInvModel(wmAdDetail);
        // 零库存调整时校验冻结是否可调整
        if (wmInvLotLocModel == null) {
            BanQinWmInvLotLoc model = new BanQinWmInvLotLoc();
            model.setOwnerCode(wmAdDetail.getOwnerCode());
            model.setSkuCode(wmAdDetail.getSkuCode());
            model.setLotNum(wmAdDetail.getLotNum());
            model.setLocCode(wmAdDetail.getLocCode());
            model.setTraceId(wmAdDetail.getTraceId());
            model.setOrgId(wmAdDetail.getOrgId());
            ResultMessage msgHold = wmHoldService.checkHold(model, WmsConstants.AD);
            if (msgHold.isSuccess()) {
                // 存在库存记录
                if ((Boolean) msgHold.getData()) {
                    inventoryEntity.setQtyHold(wmAdDetail.getQtyAdEa());
                }
            } else {
                throw new WarehouseException("订单冻结不能操作");
            }
        }
        if (wmInvLotLocModel != null && wmInvLotLocModel.getQtyHold() > 0) {
            ResultMessage msgHold = wmHoldService.checkHold(wmInvLotLocModel, WmsConstants.AD);
            if (msgHold.isSuccess()) {
                inventoryEntity.setQtyHold(wmAdDetail.getQtyAdEa());
            } else {
                throw new WarehouseException("订单冻结不能操作");
            }
        }
        // 调用库存更新方法
        if (wmAdDetail.getQtyAdEa() > 0) {
            inventoryService.updateInventory(inventoryEntity);
        }
        // 更新明细状态
        wmAdDetail.setStatus(WmsCodeMaster.AD_FULL_ADJUSTMENT.getCode());
        wmAdDetailService.save(wmAdDetail);
        // 更新订单头状态
        BanQinWmAdDetail detail = new BanQinWmAdDetail();
        detail.setAdNo(wmAdHeader.getAdNo());
        detail.setStatus(WmsCodeMaster.AD_NEW.getCode());
        detail.setOrgId(wmAdHeader.getOrgId());
        List<BanQinWmAdDetail> adDetails = wmAdDetailService.findList(detail);
        // 当调整单明细还有创建，更新调整单为部分创建
        wmAdHeader = this.get(wmAdHeader.getId());
        if (adDetails != null && adDetails.size() > 0) {
            wmAdHeader.setStatus(WmsCodeMaster.AD_PART_ADJUSTMENT.getCode());
        } else {
            wmAdHeader.setStatus(WmsCodeMaster.AD_FULL_ADJUSTMENT.getCode());
        }
        this.save(wmAdHeader);
        return msg;
    }

    protected BanQinInventoryEntity getInvModel(BanQinWmAdDetail wmAdDetail) {
        BanQinInventoryEntity inventoryEntity = new BanQinInventoryEntity();
        inventoryEntity.setLotNum(wmAdDetail.getLotNum());
        inventoryEntity.setLocCode(wmAdDetail.getLocCode());
        inventoryEntity.setTraceId(wmAdDetail.getTraceId());
        inventoryEntity.setSkuCode(wmAdDetail.getSkuCode());
        inventoryEntity.setOwnerCode(wmAdDetail.getOwnerCode());
        inventoryEntity.setOrderNo(wmAdDetail.getAdNo());
        inventoryEntity.setLineNo(wmAdDetail.getLineNo());
        if (WmsCodeMaster.AD_A.getCode().equals(wmAdDetail.getAdMode())) {
            inventoryEntity.setAction(ActionCode.ADJUSTMENT_PROFIT);
        } else {
            inventoryEntity.setAction(ActionCode.ADJUSTMENT_LOSS);
        }
        inventoryEntity.setPackCode(wmAdDetail.getPackCode());
        inventoryEntity.setQtyUom(wmAdDetail.getQtyAdUom());
        inventoryEntity.setUom(wmAdDetail.getUom());
        inventoryEntity.setQtyEaOp(wmAdDetail.getQtyAdEa());
        inventoryEntity.setOrgId(wmAdDetail.getOrgId());
        return inventoryEntity;
    }

    /**
     * 关闭调整单
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage closeAdOrder(String ids) {
        ResultMessage msg = new ResultMessage();
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
            msg.setMessage("操作成功");
        }
        return msg;
    }

    @Transactional
    public ResultMessage close(String id) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        BanQinWmAdHeader wmAdHeader = get(id);
        if (!WmsCodeMaster.AD_PART_ADJUSTMENT.getCode().equals(wmAdHeader.getStatus()) && !WmsCodeMaster.AD_FULL_ADJUSTMENT.getCode().equals(wmAdHeader.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeader.getAdNo() + "不是调整状态，不能操作");
        } else {
            wmAdHeader.setStatus(WmsCodeMaster.AD_CLOSE.getCode());
            save(wmAdHeader);
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 取消调整单
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage cancelAdOrder(String ids) {
        ResultMessage msg = new ResultMessage();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = cancel(id);
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
    public ResultMessage cancel(String id) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        ResultMessage detailMsg = checkNewAndNotAudit(id);
        if (detailMsg.isSuccess()) {
            BanQinWmAdHeader wmAdHeader = (BanQinWmAdHeader)detailMsg.getData();
            wmAdHeader.setStatus(WmsCodeMaster.AD_CANCEL.getCode());
            save(wmAdHeader);
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            msg.addMessage(detailMsg.getMessage());
        }

        return msg;
    }

    public ResultMessage checkNewAndNotAudit(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAdHeader wmAdHeader = get(id);
        if (wmAdHeader == null) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeader.getAdNo() + "数据过期");
        } else if (!WmsCodeMaster.AD_NEW.getCode().equals(wmAdHeader.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeader.getAdNo() + "不是创建状态，不能操作");
        } else if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAdHeader.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeader.getAdNo() + "已审核");
        } else {
            msg.setData(wmAdHeader);
        }
        return msg;
    }

    /**
     * 删除调整单明细
     * @param headerId
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage removeDetail(String headerId, String ids) {
        ResultMessage msg = new ResultMessage();
        ResultMessage headerMsg = checkNewAndNotAudit(headerId);
        if (!headerMsg.isSuccess()) {
            msg.setSuccess(false);
            msg.addMessage(headerMsg.getMessage());
            return msg;
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            ResultMessage detailMsg = removeAdDetailByAdId(id);
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
    public ResultMessage removeAdDetailByAdId(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAdDetail wmAdDetail = wmAdDetailService.get(id);
        if (wmAdDetail == null) {
            msg.setSuccess(false);
            msg.addMessage("[" + wmAdDetail.getAdNo() + "][" + wmAdDetail.getLineNo() + "]" + "数据过期");
            return msg;
        }
        if (WmsCodeMaster.AD_NEW.getCode().equals(wmAdDetail.getStatus())) {
            wmAdDetailService.delete(wmAdDetail);
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            msg.addMessage("[" + wmAdDetail.getAdNo() + "][" + wmAdDetail.getLineNo() + "]" + "不是新建状态");
        }
        return msg;
    }

    /**
     * 明细行执行库存调整
     * @param headerId
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage adjustDetail(String headerId, String ids) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        // 序列号管理商品
        Map<String, List<BanQinWmAdDetail>> serialMap = new HashMap<>();
        BanQinWmAdHeader wmAdHeader = get(headerId);
        ResultMessage detailMsg = checkStatus(wmAdHeader);
        if (!detailMsg.isSuccess()) {
            msg.setSuccess(false);
            msg.addMessage(detailMsg.getMessage());
            return msg;
        }
        // 不审核时，有序列号管理商品，校验批次下调整数量和序列号调整数量绝对值是否匹配
        if (WmsCodeMaster.AUDIT_NOT.getCode().equals(wmAdHeader.getAuditStatus())) {
            ResultMessage rtMsg = wmInvSerialService.checkAdSerialNo(wmAdHeader.getAdNo(), wmAdHeader.getOrgId());
            if (!rtMsg.isSuccess()) {
                msg.setSuccess(false);
                msg.addMessage(rtMsg.getMessage());
                return msg;
            }
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BanQinWmAdDetail wmAdDetailModel = wmAdDetailService.get(id);
            if (wmAdDetailModel == null) {
                msg.addMessage("[" + wmAdDetailModel.getAdNo() + "][" + wmAdDetailModel.getLineNo() + "]" + "数据过期");
                continue;
            }
            // 是否序列号管理
            BanQinCdWhSku cdWhSkuModel = cdWhSkuService.getByOwnerAndSkuCode(wmAdDetailModel.getOwnerCode(), wmAdDetailModel.getSkuCode(), wmAdDetailModel.getOrgId());
            if (cdWhSkuModel != null && "Y".equals(cdWhSkuModel.getIsSerial())) {
                // 将调整明细号按批次分组
                if (serialMap.containsKey(wmAdDetailModel.getLotNum())) {// 当map集合中存在批次的key则，value中list加上wmAdDetailModel
                    serialMap.get(wmAdDetailModel.getLotNum()).add(wmAdDetailModel);
                } else {// 当map不存在批次的key，则创建一个list
                    List<BanQinWmAdDetail> list = new ArrayList<>();
                    list.add(wmAdDetailModel);
                    serialMap.put(wmAdDetailModel.getLotNum(), list);
                }
                continue;
            }
            if (!wmAdDetailModel.getStatus().equals(WmsCodeMaster.AD_NEW.getCode())) {
                msg.addMessage("[" + wmAdDetailModel.getAdNo() + "][" + wmAdDetailModel.getLineNo() + "]" + "不是新建状态");
                continue;
            }
            try {
                invAdjustment(wmAdDetailModel, wmAdHeader);
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage("[" + wmAdDetailModel.getAdNo() + "][" + wmAdDetailModel.getLineNo() + "]" + e.getMessage());
            }
        }

        // 执行序列号管理商品
        Iterator<String> it = serialMap.keySet().iterator();
        // 循环遍历批次
        while (it.hasNext()) {
            String key = it.next();
            List<BanQinWmAdDetail> itemList = serialMap.get(key);
            try {
                invSerialAdjustment(itemList, key, wmAdHeader);
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage(e.getMessage());
            }
        }

        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.setMessage("操作成功");
        } else {
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    /**
     * 取消调整单明细
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
            ResultMessage detailMsg = cancelAdDetail(id);
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
     * 按调整单号和行号取消调整单明细
     * @param id
     * @return
     */
    @Transactional
    public ResultMessage cancelAdDetail(String id) {
        ResultMessage msg = new ResultMessage();
        msg.setMessage(null);
        BanQinWmAdDetail wmAdDetailModel = wmAdDetailService.get(id);
        if (wmAdDetailModel == null) {
            msg.setSuccess(false);
            msg.addMessage("[" + wmAdDetailModel.getAdNo() + "][" + wmAdDetailModel.getLineNo() + "]" + "数据过期");
            return msg;
        }
        if (WmsCodeMaster.AD_NEW.getCode().equals(wmAdDetailModel.getStatus())) {
            wmAdDetailModel.setStatus(WmsCodeMaster.AD_CANCEL.getCode());
            wmAdDetailService.save(wmAdDetailModel);
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            msg.addMessage("[" + wmAdDetailModel.getAdNo() + "][" + wmAdDetailModel.getLineNo() + "]" + "不是新建状态");
        }
        return msg;
    }

    /**
     * 序列号库存调整业务逻辑
     * @param wmAdDetailModels
     * @param lotNum
     * @param wmAdHeaderModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage invSerialAdjustment(List<BanQinWmAdDetail> wmAdDetailModels, String lotNum, BanQinWmAdHeader wmAdHeaderModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmAdDetail wmAdDetailModel : wmAdDetailModels) {
            if (!wmAdDetailModel.getStatus().equals(WmsCodeMaster.AD_NEW.getCode())) {
                throw new WarehouseException("[" + wmAdDetailModel.getAdNo() + "][" + wmAdDetailModel.getLineNo() + "]" + "状态不是新建");
            }
            invAdjustment(wmAdDetailModel, wmAdHeaderModel);
        }
        // 获取序列号调整信息
        List<BanQinWmAdSerial> wmAdSerialModels = wmAdSerialService.getModelsByAdNo(wmAdHeaderModel.getAdNo(), lotNum, wmAdHeaderModel.getOrgId());
        // 更新序列号库存
        for (BanQinWmAdSerial wmAdSerialModel : wmAdSerialModels) {
            BanQinInventorySerialEntity entity = new BanQinInventorySerialEntity();
            entity.setOwnerCode(wmAdSerialModel.getOwnerCode());
            entity.setSkuCode(wmAdSerialModel.getSkuCode());
            entity.setLotNum(wmAdSerialModel.getLotNum());
            entity.setSerialNo(wmAdSerialModel.getSerialNo());
            // 增加序列号
            if (WmsCodeMaster.AD_A.getCode().equals(wmAdSerialModel.getAdMode())) {
                entity.setSerialTranType(WmsCodeMaster.AIN.getCode());
            } else {// 减少序列号
                entity.setSerialTranType(WmsCodeMaster.AOUT.getCode());
            }
            entity.setOrderNo(wmAdHeaderModel.getAdNo());
            entity.setOrgId(wmAdHeaderModel.getOrgId());
            inventorySerialService.updateInventorySerial(entity);
        }
        return msg;
    }

    public List<PrintAdOrder> getAdOrder(String[] ids) {
        return mapper.getAdOrder(ids);
    }
}