package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHold;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvMvEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvDetailEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvMvMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yunyou.core.service.BaseService.dataRuleFilter;

/**
 * 库存移动Service
 * @author WMJ
 * @version 2019-06-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvMvService {
    @Autowired
    private BanQinWmInvMvMapper wmInvMvMapper;
    @Autowired
    private BanQinWmHoldService wmHoldService;
    @Autowired
    protected BanQinInventoryService inventoryService;
    @Autowired
    protected BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    protected BanQinCdWhLocService cdWhLocService;

	public Page<BanQinWmInvMvEntity> findPage(Page page, BanQinWmInvMvEntity banQinWmInvMvEntity) {
        dataRuleFilter(banQinWmInvMvEntity);
        // 获取可用数大于0的记录
        BanQinWmInvMvEntity condition1 = new BanQinWmInvMvEntity();
        BeanUtils.copyProperties(banQinWmInvMvEntity, condition1);
        condition1.setIsAvailable("Y");
        condition1.setOrgId(banQinWmInvMvEntity.getOrgId());
        List<BanQinWmInvMvEntity> items1 = wmInvMvMapper.findPage(condition1);
        // 获取库存数等于冻结数的记录
        BanQinWmInvMvEntity condition2 = new BanQinWmInvMvEntity();
        BeanUtils.copyProperties(banQinWmInvMvEntity, condition2);
        condition2.setIsHold("Y");
        condition2.setOrgId(banQinWmInvMvEntity.getOrgId());
        List<BanQinWmInvMvEntity> items2 = wmInvMvMapper.findPage(condition2);
        // 查询冻结信息
        BanQinWmHold wmHoldModel = new BanQinWmHold();
        wmHoldModel.setDataScope(banQinWmInvMvEntity.getDataScope());
        wmHoldModel.setOrgId(banQinWmInvMvEntity.getOrgId());
        List<BanQinWmHold> lists = wmHoldService.findList(wmHoldModel);
        List<BanQinWmInvMvEntity> list6 = Lists.newArrayList();
        for (BanQinWmInvMvEntity item : items2) {
            for (BanQinWmHold list : lists) {
                if (list6.contains(item)) {
                    break;
                }
                if (WmsCodeMaster.HOLD_BY_ID.getCode().equals(list.getHoldType()) && item.getFmTraceId().equals(list.getTraceId())) {
                    if (list.getIsAllowMv().equals("Y")) {
                        item.setIsAllowMv("Y");
                        items1.add(item);
                    }
                    list6.add(item);
                }
                if (WmsCodeMaster.HOLD_BY_LOC.getCode().equals(list.getHoldType()) && item.getFmLoc().equals(list.getLocCode())) {
                    if (list.getIsAllowMv().equals("Y")) {
                        item.setIsAllowMv("Y");
                        items1.add(item);
                    }
                    list6.add(item);
                }
                if (WmsCodeMaster.HOLD_BY_LOT.getCode().equals(list.getHoldType()) && item.getLotNum().equals(list.getLotNum())) {
                    if (list.getIsAllowMv().equals("Y")) {
                        item.setIsAllowMv("Y");
                        items1.add(item);
                    }
                    list6.add(item);
                }
                if (WmsCodeMaster.HOLD_BY_SKU.getCode().equals(list.getHoldType()) && item.getSkuCode().equals(list.getSkuCode())) {
                    if (list.getIsAllowMv().equals("Y")) {
                        item.setIsAllowMv("Y");
                        items1.add(item);
                    }
                    list6.add(item);
                }
                if (WmsCodeMaster.HOLD_BY_OWNER.getCode().equals(list.getHoldType()) && item.getOwnerCode().equals(list.getOwnerCode())) {
                    if (list.getIsAllowMv().equals("Y")) {
                        item.setIsAllowMv("Y");
                        items1.add(item);
                    }
                    list6.add(item);
                }
            }
        }
        List<BanQinWmInvMvEntity> entityList = Lists.newArrayList();
        for (Object item : items1) {
            BanQinWmInvMvEntity entity = new BanQinWmInvMvEntity();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        }

        banQinWmInvMvEntity.setPage(page);
        page.setCount(entityList.size());
        page.setList(entityList.subList((page.getPageNo() - 1) * page.getPageSize(), page.getPageNo() * page.getPageSize() > entityList.size() ? entityList.size() :  page.getPageNo() * page.getPageSize()));

        return page;
    }

    /**
     * 库存移动
     * @param wmInvMvEntity
     */
    @Transactional
    public ResultMessage invMovement(BanQinWmInvMvEntity wmInvMvEntity) {
        ResultMessage msg = new ResultMessage();
        // set源转移信息
        BanQinInventoryEntity fminvm = getFmInvModel(wmInvMvEntity);
        // 校验冻结是否可调整
        BanQinWmInvLotLoc wmInvLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(wmInvMvEntity.getLotNum(), wmInvMvEntity.getFmLoc(), wmInvMvEntity.getTraceId(), wmInvMvEntity.getOrgId());
        // 判断目标库位是否丢失跟踪号
        BanQinCdWhLoc cdWhLocModel = cdWhLocService.findByLocCode(wmInvMvEntity.getToLoc(), wmInvMvEntity.getOrgId());
        if (cdWhLocModel == null) {
            throw new WarehouseException(wmInvMvEntity.getToLoc() + "不存在");
        }
        // 校验其他库位是否含有和目标跟踪号相同的跟踪号
        if (!wmInvMvEntity.getFmTraceId().equals(wmInvMvEntity.getToTraceId()) && WmsConstants.NO.equals(cdWhLocModel.getIsLoseId())) {
            if (!WmsConstants.TRACE_ID.equals(wmInvMvEntity.getToTraceId())) {
                BanQinWmInvLotLoc traceIdExample = new BanQinWmInvLotLoc();
                traceIdExample.setTraceId(wmInvMvEntity.getToTraceId());
                traceIdExample.setOrgId(wmInvMvEntity.getOrgId());
                List<BanQinWmInvLotLoc> traceIdModel = wmInvLotLocService.findList(traceIdExample);
                if (traceIdModel != null && traceIdModel.size() > 0) {
                    throw new WarehouseException("同一个跟踪号不允许出现在不同库位");
                }
            }
        }
        if (wmInvLotLocModel != null && wmInvLotLocModel.getQtyHold() > 0) {
            // 校验源跟踪号和目标跟踪号是否相同
            if (!wmInvLotLocModel.getQty().equals(wmInvMvEntity.getToQty())) {
                if (wmInvMvEntity.getFmTraceId().equals(wmInvMvEntity.getToTraceId()) && WmsConstants.NO.equals(cdWhLocModel.getIsLoseId())
                        && !WmsConstants.TRACE_ID.equals(wmInvMvEntity.getToTraceId())) {
                    throw new WarehouseException("同一个跟踪号不允许出现在不同库位");
                }
            }

            ResultMessage msgHold = wmHoldService.checkHold(wmInvLotLocModel, WmsConstants.MV);
            if (msgHold.isSuccess()) {
                fminvm.setQtyHold(wmInvMvEntity.getToQty());
            } else {
                throw new WarehouseException("已冻结，不能操作");
            }
        }
        // set目标转移信息
        BanQinInventoryEntity toinvm = getToInvModel(wmInvMvEntity);
        // 更新库存方法
        inventoryService.updateInventory(fminvm, toinvm);
        msg.setMessage("操作成功");
        return msg;
    }

    /**
     * 库存移动单移动
     *
     * @param wmMvDetailEntity
     */
    @Transactional
    public ResultMessage invMovementByOrder(BanQinWmMvDetailEntity wmMvDetailEntity) {
        ResultMessage msg = new ResultMessage();
        // set源转移信息
        BanQinInventoryEntity fmInv = getFmInvModel(wmMvDetailEntity);
        // 校验冻结是否可调整
        BanQinWmInvLotLoc wmInvLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(wmMvDetailEntity.getLotNum(), wmMvDetailEntity.getFmLocCode(), wmMvDetailEntity.getFmTraceId(), wmMvDetailEntity.getOrgId());
        // 判断目标库位是否丢失跟踪号
        BanQinCdWhLoc cdWhLocModel = cdWhLocService.findByLocCode(wmMvDetailEntity.getToLocCode(), wmMvDetailEntity.getOrgId());
        if (cdWhLocModel == null) {
            throw new WarehouseException(wmMvDetailEntity.getToLocCode() + "不存在");
        }
        // 校验其他库位是否含有和目标跟踪号相同的跟踪号
        if (!wmMvDetailEntity.getFmTraceId().equals(wmMvDetailEntity.getToTraceId()) && WmsConstants.NO.equals(cdWhLocModel.getIsLoseId())) {
            if (!WmsConstants.TRACE_ID.equals(wmMvDetailEntity.getToTraceId())) {
                BanQinWmInvLotLoc traceIdExample = new BanQinWmInvLotLoc();
                traceIdExample.setTraceId(wmMvDetailEntity.getToTraceId());
                traceIdExample.setOrgId(wmMvDetailEntity.getOrgId());
                List<BanQinWmInvLotLoc> traceIdModel = wmInvLotLocService.findList(traceIdExample);
                if (traceIdModel != null && traceIdModel.size() > 0) {
                    if (traceIdModel.stream().anyMatch(o -> !o.getLocCode().equals(wmMvDetailEntity.getToLocCode()) && !o.getLocCode().equals(wmMvDetailEntity.getFmLocCode()))
                            || !wmMvDetailEntity.getQtyMvEa().equals(wmInvLotLocModel.getQty())) {
                        throw new WarehouseException("同一个跟踪号不允许出现在不同库位");
                    }
                }
            }
        }
        if (wmInvLotLocModel != null && wmInvLotLocModel.getQtyHold() > 0) {
            // 校验源跟踪号和目标跟踪号是否相同
            if (!wmInvLotLocModel.getQty().equals(wmMvDetailEntity.getQtyMvEa())) {
                if (wmMvDetailEntity.getFmTraceId().equals(wmMvDetailEntity.getToTraceId()) && WmsConstants.NO.equals(cdWhLocModel.getIsLoseId())
                        && !WmsConstants.TRACE_ID.equals(wmMvDetailEntity.getToTraceId())) {
                    throw new WarehouseException("同一个跟踪号不允许出现在不同库位");
                }
            }

            ResultMessage msgHold = wmHoldService.checkHold(wmInvLotLocModel, WmsConstants.MV);
            if (msgHold.isSuccess()) {
                fmInv.setQtyHold(wmMvDetailEntity.getQtyMvEa());
            } else {
                throw new WarehouseException("已冻结，不能操作");
            }
        }
        // set目标转移信息
        BanQinInventoryEntity toInv = getToInvModel(wmMvDetailEntity);
        // 更新库存方法
        inventoryService.updateInventory(fmInv, toInv);
        msg.setMessage("操作成功");
        return msg;
    }

    private BanQinInventoryEntity getFmInvModel(BanQinWmInvMvEntity wmInvMvEntity) {
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setLotNum(wmInvMvEntity.getLotNum());
        fminvm.setLocCode(wmInvMvEntity.getFmLoc());
        fminvm.setTraceId(wmInvMvEntity.getFmTraceId());
        fminvm.setSkuCode(wmInvMvEntity.getSkuCode());
        fminvm.setOwnerCode(wmInvMvEntity.getOwnerCode());
        fminvm.setQtyUom(wmInvMvEntity.getToQty());
        fminvm.setUom(WmsConstants.UOM_EA);
        if (wmInvMvEntity.getIsRf() != null) {
            fminvm.setAction(ActionCode.MOVING_BY_TRACEID);
        } else {
            fminvm.setAction(ActionCode.MOVING);
        }
        fminvm.setPackCode(wmInvMvEntity.getPackCode());
        fminvm.setQtyEaOp(wmInvMvEntity.getToQty());
        fminvm.setOrgId(wmInvMvEntity.getOrgId());
        return fminvm;
    }

    private BanQinInventoryEntity getFmInvModel(BanQinWmMvDetailEntity wmMvDetailEntity) {
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setOrderNo(wmMvDetailEntity.getMvNo());
        fminvm.setLineNo(wmMvDetailEntity.getLineNo());
        fminvm.setLotNum(wmMvDetailEntity.getLotNum());
        fminvm.setLocCode(wmMvDetailEntity.getFmLocCode());
        fminvm.setTraceId(wmMvDetailEntity.getFmTraceId());
        fminvm.setSkuCode(wmMvDetailEntity.getSkuCode());
        fminvm.setOwnerCode(wmMvDetailEntity.getOwnerCode());
        fminvm.setQtyUom(wmMvDetailEntity.getQtyMvEa());
        fminvm.setUom(WmsConstants.UOM_EA);
        if (wmMvDetailEntity.getIsRf() != null) {
            fminvm.setAction(ActionCode.MOVING_BY_TRACEID);
        } else {
            fminvm.setAction(ActionCode.MOVING);
        }
        fminvm.setPackCode(wmMvDetailEntity.getPackCode());
        fminvm.setQtyEaOp(wmMvDetailEntity.getQtyMvEa());
        fminvm.setOrgId(wmMvDetailEntity.getOrgId());
        return fminvm;
    }

    private BanQinInventoryEntity getToInvModel(BanQinWmInvMvEntity wmInvMvEntity) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setLotNum(wmInvMvEntity.getLotNum());
        toinvm.setLocCode(wmInvMvEntity.getToLoc());
        toinvm.setTraceId(wmInvMvEntity.getToTraceId());
        toinvm.setSkuCode(wmInvMvEntity.getSkuCode());
        toinvm.setOwnerCode(wmInvMvEntity.getOwnerCode());
        toinvm.setAction(ActionCode.MOVING);
        toinvm.setPackCode(wmInvMvEntity.getPackCode());
        toinvm.setQtyUom(wmInvMvEntity.getToQtyUom());
        toinvm.setUom(wmInvMvEntity.getToUom());
        toinvm.setQtyEaOp(wmInvMvEntity.getToQty());
        toinvm.setOrgId(wmInvMvEntity.getOrgId());
        return toinvm;
    }

    private BanQinInventoryEntity getToInvModel(BanQinWmMvDetailEntity wmMvDetailEntity) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setLotNum(wmMvDetailEntity.getLotNum());
        toinvm.setLocCode(wmMvDetailEntity.getToLocCode());
        toinvm.setTraceId(wmMvDetailEntity.getToTraceId());
        toinvm.setSkuCode(wmMvDetailEntity.getSkuCode());
        toinvm.setOwnerCode(wmMvDetailEntity.getOwnerCode());
        toinvm.setAction(ActionCode.MOVING);
        toinvm.setPackCode(wmMvDetailEntity.getPackCode());
        toinvm.setQtyUom(wmMvDetailEntity.getQtyMvUom());
        toinvm.setUom(wmMvDetailEntity.getToUom());
        toinvm.setQtyEaOp(wmMvDetailEntity.getQtyMvEa());
        toinvm.setOrgId(wmMvDetailEntity.getOrgId());
        return toinvm;
    }
}