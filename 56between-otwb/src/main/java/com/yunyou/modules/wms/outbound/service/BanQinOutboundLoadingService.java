package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 装载确认
 * @author WMj
 * @version 2019/02/28
 */
@Service
public class BanQinOutboundLoadingService {
    // 公共接口
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 编码生成器
    @Autowired
    protected SynchronizedNoService noService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;
    // 装车明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    // 装车单
    @Autowired
    protected BanQinWmLdHeaderService wmLdHeaderService;
    // 出库单头
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 出库单明细
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;

    /**
     * 装载
     * @param ldDetailEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundLoading(BanQinWmLdDetailEntity ldDetailEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String ldNo = ldDetailEntity.getLdNo();
        String lineNo = ldDetailEntity.getLineNo();
        Double loadQty = ldDetailEntity.getLoadQty();
        String allocId = ldDetailEntity.getAllocId();
        String status = ldDetailEntity.getStatus();
        String orgId = ldDetailEntity.getOrgId();
        if (!WmsCodeMaster.LD_NEW.getCode().equals(status)) {
            throw new WarehouseException(allocId + "不是创建状态，不能操作");
        }
        BanQinWmSoAlloc soAllocModel = wmSoAllocService.getByAllocId(allocId, orgId);
        if (soAllocModel == null) {
            throw new WarehouseException("查询不到分配明细" + allocId);
        }
        // 未复核明细不能通过装载
        if (soAllocModel.getCheckStatus().equals(WmsCodeMaster.CHECK_NEW.getCode())) {
            throw new WarehouseException(allocId + "未复核明细不能通过装载");
        }
        if (!(WmsCodeMaster.SO_FULL_PICKING.getCode().equals(soAllocModel.getStatus()) || WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(soAllocModel.getStatus()))) {
            throw new WarehouseException(allocId + "不是完全拣货或完全发运");
        }
        // 拣货数量
        Double pickQty = soAllocModel.getQtyUom();
        Double eaQty = soAllocModel.getQtyEa();
        Double uomQty = eaQty / pickQty;
        BanQinWmLdDetail model = wmLdDetailService.getByLdNoAndLineNo(ldNo, lineNo, orgId);
        if (!WmsCodeMaster.LD_NEW.getCode().equals(model.getStatus())) {
            // 不是创建状态，不能操作！
            throw new WarehouseException(model.getLdNo() + "不是创建状态，不能操作");
        }
        if (null == loadQty || loadQty.equals(pickQty) || loadQty.equals(0D)) {
            // 装载完成
            model.setStatus(WmsCodeMaster.LD_FULL_LOAD.getCode());
        } else if (loadQty > 0.0 && loadQty < pickQty) {
            model.setStatus(WmsCodeMaster.LD_FULL_LOAD.getCode());
            soAllocModel.setQtyUom(loadQty);
            soAllocModel.setQtyEa(loadQty * uomQty);
            // 拆分出剩余未装车数量的分配明细记录
            BanQinWmSoAlloc newAllocModel = new BanQinWmSoAlloc();
            BeanUtils.copyProperties(soAllocModel, newAllocModel);
            String newAllocId = noService.getDocumentNo(GenNoType.WM_ALLOC_ID.name());
            newAllocModel.setAllocId(newAllocId);
            double unLoadQtyEa = eaQty - loadQty * uomQty;
            double unLoadQtyUom = pickQty - loadQty;
            newAllocModel.setQtyEa(unLoadQtyEa);
            newAllocModel.setQtyUom(unLoadQtyUom);
            newAllocModel.setId(IdGen.uuid());
            String newToId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
            newAllocModel.setToId(newToId);

            // 新生成的剩余未装载的装车明细
            BanQinWmLdDetail newModel = new BanQinWmLdDetail();
            newModel.setLdNo(model.getLdNo());

            BanQinWmLdDetail condition = new BanQinWmLdDetail();
            condition.setOrgId(orgId);
            condition.setLdNo(ldNo);
            List<BanQinWmLdDetail> LdDetails = wmLdDetailService.findList(condition);
            int count = CollectionUtil.isNotEmpty(LdDetails) ? LdDetails.stream().map(m -> Integer.valueOf(m.getLineNo())).mapToInt(Integer::intValue).max().getAsInt() : 1;
            newModel.setLineNo(wmCommon.getLineNo(count + 1, 5));
            newModel.setStatus(WmsCodeMaster.LD_NEW.getCode());
            newModel.setAllocId(newAllocId);

            // 如果拣货明细是完全发货状态，那么不进行库存拆分，完全发货后，已无库存
            // 拣货明细是完全拣货状态，才可进行库存更新
            if (newAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
                // 库存更新
                BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
                fminvm.setLotNum(soAllocModel.getLotNum());
                fminvm.setLocCode(soAllocModel.getToLoc());
                fminvm.setTraceId(soAllocModel.getToId());
                fminvm.setSkuCode(soAllocModel.getSkuCode());
                fminvm.setOwnerCode(soAllocModel.getOwnerCode());
                fminvm.setAction(ActionCode.LOADING);
                fminvm.setPackCode(soAllocModel.getPackCode());
                fminvm.setQtyUom(unLoadQtyUom);
                fminvm.setUom(soAllocModel.getUom());
                fminvm.setQtyEaOp(unLoadQtyEa);
                fminvm.setOrderNo(soAllocModel.getSoNo());
                fminvm.setLineNo(soAllocModel.getLineNo());
                fminvm.setTaskId(soAllocModel.getAllocId());
                fminvm.setOrgId(soAllocModel.getOrgId());

                // set目标转移信息
                BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
                toinvm.setLotNum(soAllocModel.getLotNum());
                toinvm.setLocCode(soAllocModel.getToLoc());
                toinvm.setTraceId(newToId);
                toinvm.setSkuCode(soAllocModel.getSkuCode());
                toinvm.setOwnerCode(soAllocModel.getOwnerCode());
                toinvm.setAction(ActionCode.LOADING);
                toinvm.setPackCode(soAllocModel.getPackCode());
                toinvm.setQtyUom(unLoadQtyUom);
                toinvm.setUom(soAllocModel.getUom());
                toinvm.setQtyEaOp(unLoadQtyEa);
                toinvm.setOrderNo(soAllocModel.getSoNo());
                toinvm.setLineNo(soAllocModel.getLineNo());
                toinvm.setTaskId(newAllocId);
                toinvm.setOrgId(soAllocModel.getOrgId());

                // 更新库存方法
                inventoryService.updateInventory(fminvm, toinvm);
            }
            wmLdDetailService.save(newModel);
            wmSoAllocService.save(soAllocModel);
            wmSoAllocService.save(newAllocModel);
        }
        model.setLdOp(UserUtils.getUser().getName());
        model.setLdTime(new Date());
        wmLdDetailService.save(model);
        // 更新装车单状态
        wmLdHeaderService.updateStatus(ldNo, orgId);
        wmSoDetailService.updateLdStatus(soAllocModel.getSoNo(), soAllocModel.getLineNo(), soAllocModel.getOrgId());
        wmSoHeaderService.updateLdStatus(soAllocModel.getSoNo(), soAllocModel.getOrgId());
        return msg;
    }

    /**
     * 装载
     * @param ldDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundLoading(BanQinWmLdDetail ldDetailModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String ldNo = ldDetailModel.getLdNo();
        String lineNo = ldDetailModel.getLineNo();
        String allocId = ldDetailModel.getAllocId();
        String orgId = ldDetailModel.getOrgId();
        BanQinWmSoAlloc soAllocModel = wmSoAllocService.getByAllocId(allocId, orgId);
        if (soAllocModel == null) {
            throw new WarehouseException(allocId + "查询不到分配明细");
        }
        // 未复核明细不能通过装载
        if (soAllocModel.getCheckStatus().equals(WmsCodeMaster.CHECK_NEW.getCode())) {
            throw new WarehouseException(allocId + "未复核明细不能通过装载");
        }
        if (!(WmsCodeMaster.SO_FULL_PICKING.getCode().equals(soAllocModel.getStatus()) || WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(soAllocModel.getStatus()))) {
            throw new WarehouseException(allocId + "不是完全拣货状态");
        }
        BanQinWmLdDetail model = wmLdDetailService.getByLdNoAndLineNo(ldNo, lineNo, orgId);
        if (!WmsCodeMaster.LD_NEW.getCode().equals(model.getStatus())) {
            // 不是创建状态，不能操作！
            throw new WarehouseException(model.getLdNo() + "不是创建状态，不能操作");
        }
        // 装载完成
        model.setStatus(WmsCodeMaster.LD_FULL_LOAD.getCode());
        model.setLdOp(UserUtils.getUser().getName());
        model.setLdTime(new Date());
        wmLdDetailService.save(model);
        // 更新装车单状态
        wmLdHeaderService.updateStatus(ldNo, orgId);
        wmSoDetailService.updateLdStatus(soAllocModel.getSoNo(), soAllocModel.getLineNo(), soAllocModel.getOrgId());
        wmSoHeaderService.updateLdStatus(soAllocModel.getSoNo(), soAllocModel.getOrgId());
        return msg;
    }

    /**
     * 装载 by订单号SO
     * @param ldNo
     * @param soNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundLoadingBySoNo(String ldNo, String soNo, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmLdDetail> models = wmLdDetailService.getByLdNoAndStatus(ldNo, WmsCodeMaster.LD_NEW.getCode(), orgId);
        List<BanQinWmLdDetail> doModels = new ArrayList<>();
        for (BanQinWmLdDetail model : models) {
            BanQinWmSoAlloc soAlloc = wmSoAllocService.getByAllocId(model.getAllocId(), orgId);
            if (soAlloc != null) {
                String soNoG = soAlloc.getSoNo();
                if (soNoG.equals(soNo)) {
                    doModels.add(model);
                }
            }
        }
        for (BanQinWmLdDetail model : doModels) {
            outboundLoading(model);
        }
        return msg;
    }

    /**
     * 装载 by箱号TraceId
     * @param ldNo
     * @param traceId
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundLoadingByTraceId(String ldNo, String traceId, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmLdDetail> models = wmLdDetailService.getByLdNoAndStatus(ldNo, WmsCodeMaster.LD_NEW.getCode(), orgId);
        List<BanQinWmLdDetail> doModels = new ArrayList<>();
        for (BanQinWmLdDetail model : models) {
            BanQinWmSoAlloc soAlloc = wmSoAllocService.getByAllocId(model.getAllocId(), orgId);
            if (soAlloc != null) {
                String toId = soAlloc.getToId();
                if (toId.equals(traceId)) {
                    doModels.add(model);
                }
            }
        }
        for (BanQinWmLdDetail model : doModels) {
            outboundLoading(model);
        }
        return msg;
    }

    /**
     * 装载 by装车单号LdNo
     * @param ldNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundLoadingByLdNo(String ldNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmLdDetail> models = wmLdDetailService.getByLdNoAndStatus(ldNo, WmsCodeMaster.LD_NEW.getCode(), orgId);
        for (BanQinWmLdDetail model : models) {
            try {
                msg = outboundLoading(model);
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.setMessage(e.getMessage());
            }
        }

        return msg;
    }

}