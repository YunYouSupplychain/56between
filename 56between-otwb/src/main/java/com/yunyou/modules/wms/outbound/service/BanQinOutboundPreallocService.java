package com.yunyou.modules.wms.outbound.service;

import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePreallocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinPreallocUpdateEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预配
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundPreallocService {
    // 公共类
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 预配明细
    @Autowired
    protected BanQinWmSoPreallocService wmSoPreallocService;
    // 编号生成器
    @Autowired
    protected SynchronizedNoService noService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;

    /**
     * 预配更新
     *
     * @param updateEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public Double updatePrealloc(BanQinPreallocUpdateEntity updateEntity) throws WarehouseException {
        // 出库明细行可预配数量
        Double qtyPrealloc = updateEntity.getQtyPrealloc();
        // 库存
        BanQinWmInvLot invLotModel = updateEntity.getInvLotModel();
        // 包装单位
        List<BanQinCdWhPackageRelation> packageRelationList = updateEntity.getPackageRelationList();
        // 预配明细
        List<BanQinCdRulePreallocDetail> rulePreallocDetailList = updateEntity.getRulePreallocDetailList();
        // 出库单明细行
        BanQinWmSoDetailEntity soDetail = updateEntity.getSoDetail();
        // 库存可预配数量计算
        BanQinInventoryEntity entity = new BanQinInventoryEntity();
        entity.setLotNum(invLotModel.getLotNum());
        // 计算库存可预配数量
        Double qtyPrealllocInv = inventoryService.getAvailablePreallocQty(entity);

        // 预配明细行记录数量
        Double qtyPreallocEa = 0D;
        Double qtyPreallocUom = 0D;
        // 总预配数量
        Double qtyResult = 0D;
        // 2、新增预配明细记录
        // 循环预配明细记录，根据出库明细可预配数量 与 批次库存可预配数量
        // 换算：一条批次库存可预配数量 按预配单位可以 换算成几条预配明细()
        for (BanQinCdRulePreallocDetail rulePreallocDetailModel : rulePreallocDetailList) {
            // 不可预配，退出
            if (qtyPrealloc == 0 || qtyPrealllocInv == 0) {
                break;
            }
            // 单位换算数量
            BanQinCdWhPackageRelation packageRelationEntity = new BanQinCdWhPackageRelation();
            for (BanQinCdWhPackageRelation item : packageRelationList) {
                if (rulePreallocDetailModel.getUom().equals(item.getCdprUnitLevel())) {
                    BeanUtils.copyProperties(item, packageRelationEntity);
                    // 单位换算，根据包装代码获取包装单位换算信息，根据EA数量及单位 换算出单位数量
                    Integer unitQty = item.getCdprQuantity().intValue();
                    Double qtyUom = wmCommon.doubleDivide(qtyPrealloc, unitQty.doubleValue());
                    packageRelationEntity.setCdprQuantity(qtyUom);
                    break;
                }
            }
            Integer unitQty = packageRelationEntity.getCdprQuantity().intValue();
            if (unitQty == null || unitQty == 0) {
                continue;
            }
            // 如果预配数量>=库存数量，那么按库存数量换算 如果预配数量<库存数量，那么按预配数量换算
            qtyPreallocEa = outboundCommon.qtyEaOpByUom(qtyPrealloc, qtyPrealllocInv, unitQty);
            qtyPreallocUom = wmCommon.doubleDivide(qtyPreallocEa, unitQty.doubleValue());
            // 单位数量
            if (qtyPreallocEa <= 0) {
                continue;
            }

            // 新增预配明细记录
            BanQinWmSoPrealloc soPreallocModel = new BanQinWmSoPrealloc();
            soPreallocModel.setWaveNo(soDetail.getWaveNo());// 波次单号
            soPreallocModel.setSoNo(soDetail.getSoNo());// 出库单号
            soPreallocModel.setLineNo(soDetail.getLineNo());// 行号
            soPreallocModel.setOwnerCode(soDetail.getOwnerCode());// 货主
            soPreallocModel.setSkuCode(soDetail.getSkuCode());// 商品
            soPreallocModel.setLotNum(invLotModel.getLotNum());// 批次号
            soPreallocModel.setPackCode(soDetail.getPackCode());// 包装代码
            soPreallocModel.setUom(rulePreallocDetailModel.getUom());// 包装单位
            soPreallocModel.setQtyPreallocEa(qtyPreallocEa);// 预配数量EA
            soPreallocModel.setQtyPreallocUom(qtyPreallocUom);// 预配数量
            // 生成预配ID
            String preallocId = noService.getDocumentNo(GenNoType.WM_PREALLOC_ID.name());
            soPreallocModel.setPreallocId(preallocId);
            soPreallocModel.setOrgId(soDetail.getOrgId());
            this.wmSoPreallocService.save(soPreallocModel);
            // 总预配数量
            qtyResult += qtyPreallocEa;
            // 剩余出库单行可预配数量
            qtyPrealloc -= qtyPreallocEa;
            // 剩余库存可预配数量
            qtyPrealllocInv -= qtyPreallocEa;
        }

        // 总预配数量>0
        if (qtyResult > 0) {
            // 更新库存
            BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
            invEntity.setAction(ActionCode.PREALLOCATION);// 库存更新执行动作 预配
            invEntity.setOwnerCode(soDetail.getOwnerCode());// 货主
            invEntity.setSkuCode(soDetail.getSkuCode());// 商品
            invEntity.setLotNum(invLotModel.getLotNum());// 批次号
            invEntity.setQtyEaOp(qtyResult);// 预配操作数量
            invEntity.setOrgId(soDetail.getOrgId());
            inventoryService.updateInventory(invEntity);
            // 更新出库单行、波次单行状态、出库单状态、波次单状态
            outboundCommon.updateOrder(ActionCode.PREALLOCATION, qtyResult, soDetail.getSoNo(), soDetail.getLineNo(), soDetail.getWaveNo(), soDetail.getOrgId());
        }
        return qtyPrealloc;
    }

}