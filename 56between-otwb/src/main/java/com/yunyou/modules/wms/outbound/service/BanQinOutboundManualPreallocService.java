package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotService;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 手工预配方法类
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundManualPreallocService {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 出库单明细
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    // 波次单明细
    @Autowired
    protected BanQinWmWvDetailService wmWvDetailService;
    // 预配明细
    @Autowired
    protected BanQinWmSoPreallocService wmSoPreallocService;
    // 取消预配
    @Autowired
    protected BanQinWmDelPreallocService wmDelPreallocService;
    // 批次库存
    @Autowired
    protected BanQinWmInvLotService wmInvLotService;
    // 商品
    @Autowired
    protected BanQinCdWhSkuService cdWhSkuService;
    // 编码生成器
    @Autowired
    protected SynchronizedNoService noService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;

    /**
     * 手工预配保存
     *
     * @param soPreallocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoPreallocEntity outboundManualPrealloc(BanQinWmSoPreallocEntity soPreallocEntity) throws WarehouseException {
        BanQinWmSoPreallocEntity entity;
        // 1、获取出库单，校验拦截、冻结状态、取消、关闭状态
        BanQinWmSoHeader wmSoHeaderModel = this.wmSoHeaderService.checkStatus(soPreallocEntity.getSoNo(), ActionCode.PREALLOCATION, soPreallocEntity.getOrgId());
        // 订单审核状态
        if (wmSoHeaderModel.getAuditStatus().equals(WmsCodeMaster.AUDIT_NEW.getCode())) {
            // 订单{0}未审核
            throw new WarehouseException("订单" + soPreallocEntity.getSoNo() + "未审核");
        }
        // 2、获取出库单行信息(出库单号、行号),校验取消状态
        BanQinWmSoDetail wmSoDetailModel = wmSoDetailService.checkStatus(soPreallocEntity.getSoNo(), soPreallocEntity.getLineNo(), soPreallocEntity.getOrgId());
        // 3.1 手工预配新增保存
        if (StringUtils.isEmpty(soPreallocEntity.getId())) {
            entity = insertPrealloc(soPreallocEntity, wmSoDetailModel);
        } else {
            // 3.2 手工预配修改保存
            entity = modifyPrealloc(soPreallocEntity, wmSoDetailModel);
        }
        return entity;
    }

    /**
     * 新增预配明细
     *
     * @param soPreallocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoPreallocEntity insertPrealloc(BanQinWmSoPreallocEntity soPreallocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 1、校验预配数量 小于等于 可预配数量
        // 出库明细行可预配数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
        Double qtyPrealloc = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);
        // 如果可预配数量 < 当前预配数量，不能手工预配，订货数不足
        if (qtyPrealloc < soPreallocEntity.getQtyPreallocEa()) {
            // 预配数量大于订货数，不能操作
            throw new WarehouseException("预配数量大于订货数，不能操作");
        }
        // 2、获取库存批次数量， 校验可用库存数量与预配数量大小
        // 有效期 校验 获取可用库存数量
        BanQinInventoryEntity invEntity = getInvLot(soPreallocEntity, wmSoDetailModel);
        // 可预配数量
        Double qtyPreallocInv = inventoryService.getAvailablePreallocQty(invEntity);
        // 校验可用库存数量与预配数量大小
        if (qtyPreallocInv < soPreallocEntity.getQtyPreallocEa()) {
            // 有效库存不足,批次{0}，不能操作
            throw new WarehouseException("有效库存不足", soPreallocEntity.getLotNum());
        }
        // 3、新增预配明细：生成预配编号， 波次单号，预配单位数量换算
        // 新增：生成预配编号， 波次单号，预配单位数量换算
        soPreallocEntity.setOrgId(wmSoDetailModel.getOrgId());
        // 预配ID生成
        String preallocId = noService.getDocumentNo(GenNoType.WM_PREALLOC_ID.name());
        soPreallocEntity.setPreallocId(preallocId);
        // 波次单号
        BanQinWmWvDetail wvDetailModel = wmWvDetailService.findBySoNo(soPreallocEntity.getSoNo(), soPreallocEntity.getOrgId());
        if (wvDetailModel != null) {
            soPreallocEntity.setWaveNo(wvDetailModel.getWaveNo());
        }
        // 4、统一更新
        return updatePrealloc(ActionCode.PREALLOCATION, soPreallocEntity.getQtyPreallocEa(), soPreallocEntity, wmSoDetailModel);
    }

    /**
     * 修改预配明细
     *
     * @param soPreallocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoPreallocEntity modifyPrealloc(BanQinWmSoPreallocEntity soPreallocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 1、校验预配数量+分配数量+拣货数量+发货数量 小于等于 订货数量
        // 差异数量
        Double qtyPreallocDiff = 0D;
        // 如果是修改预配数量，那么获取当前预配明细，计算差异预配数量
        BanQinWmSoPrealloc soPreallocModel = wmSoPreallocService.getByPreallocId(soPreallocEntity.getPreallocId(), soPreallocEntity.getOrgId());
        // 2、获取库存批次数量， 校验可用库存数量与预配数量大小
        // 有效期 校验 获取可用库存数量
        BanQinInventoryEntity invEntity = getInvLot(soPreallocEntity, wmSoDetailModel);
        // 3、校验可用库存数量与预配数量大小
        ActionCode actionCode = null;
        // 3.1 原预配数量=新预配数量--未修改
        if (soPreallocModel.getQtyPreallocEa().equals(soPreallocEntity.getQtyPreallocEa())) {
            // 操作成功
            throw new WarehouseException("操作成功");
        }
        // 3.2 如果原预配数量>新预配数量,改小
        else if (soPreallocModel.getQtyPreallocEa() > soPreallocEntity.getQtyPreallocEa()) {
            // 差异预配数量=原预配数量-新预配数量
            qtyPreallocDiff = soPreallocModel.getQtyPreallocEa() - soPreallocEntity.getQtyPreallocEa();
            // 1、如果取消数量>出库单行预配数量
            if (qtyPreallocDiff > wmSoDetailModel.getQtyPreallocEa()) {
                // 没有足够的预配数取消
                throw new WarehouseException("没有足够的预配数取消");
            }
            // 2、预配数量改小，canel 校验 库存预配数量 与 取消预配数量大小
            if (invEntity.getQtyPrealloc() < qtyPreallocDiff) {
                // 有效库存不足,批次{0}，不能操作
                throw new WarehouseException("有效库存不足", soPreallocEntity.getLotNum());
            }
            // 3、库存更新 ACTION_CODE 设置
            actionCode = ActionCode.CANCEL_PREALLOCATION; // 预配数量改小，取消预配
        }
        // 3.3 如果原预配数量<新预配数量,改大
        else if (soPreallocModel.getQtyPreallocEa() < soPreallocEntity.getQtyPreallocEa()) {
            // 差异预配数量=新预配数量-原预配数量，改大
            qtyPreallocDiff = soPreallocEntity.getQtyPreallocEa() - soPreallocModel.getQtyPreallocEa();
            // 出库单行可预配数量
            Double qtyPrealloc = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);
            // 1、如果预配数量> 出库单行可预配数量
            if (qtyPreallocDiff > qtyPrealloc) {
                // 预配数量大于订货数，不能操作
                throw new WarehouseException("预配数量大于订货数，不能操作");
            }
            // 可预配数量 =库存总数- 冻结数量-预配数量-分配数量-拣货数量 - 待出数量
            Double qtyPreallocInv = inventoryService.getAvailablePreallocQty(invEntity);
            // 2、校验可用库存数量与预配数量大小
            if (qtyPreallocInv < qtyPreallocDiff) {
                throw new WarehouseException("有效库存不足,批次{0}，不能操作", soPreallocEntity.getLotNum());
            }
            // 3、库存更新 ACTION_CODE 设置
            actionCode = ActionCode.PREALLOCATION; // 预配数量改大，预配
        }
        if (actionCode == null) {
            throw new WarehouseException("操作代码不能为空");
        }
        // 统一更新
        return updatePrealloc(actionCode, qtyPreallocDiff, soPreallocEntity, wmSoDetailModel);
    }

    /**
     * 统一更新
     *
     * @param actionCode
     * @param qtyPreallocDiff
     * @param soPreallocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoPreallocEntity updatePrealloc(ActionCode actionCode, Double qtyPreallocDiff, BanQinWmSoPreallocEntity soPreallocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 如果取消预配，写取消预配表
        if (actionCode.equals(ActionCode.CANCEL_PREALLOCATION)) {
            BanQinWmSoPrealloc wmSoPreallocModel = new BanQinWmSoPrealloc();
            BeanUtils.copyProperties(soPreallocEntity, wmSoPreallocModel);
            // 取消数量
            wmSoPreallocModel.setQtyPreallocEa(qtyPreallocDiff);
            // 包装
            BanQinCdWhPackageRelationEntity packagEntity = wmCommon.getPackageRelationAndQtyUom(wmSoPreallocModel.getPackCode(), wmSoPreallocModel.getUom(), wmSoPreallocModel.getOrgId());
            // 单位换算
            wmSoPreallocModel.setQtyPreallocUom(qtyPreallocDiff / packagEntity.getCdprQuantity());
            wmDelPreallocService.saveDelPrealloc(wmSoPreallocModel);
        }
        // 5、预配明细保存
        BanQinWmSoPrealloc wmSoPreallocModel = new BanQinWmSoPrealloc();
        BeanUtils.copyProperties(soPreallocEntity, wmSoPreallocModel);
        wmSoPreallocService.save(wmSoPreallocModel);
        // 6、库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(actionCode);// 预配、取消预配
        invEntity.setOwnerCode(wmSoDetailModel.getOwnerCode());
        invEntity.setSkuCode(wmSoDetailModel.getSkuCode());
        invEntity.setLotNum(soPreallocEntity.getLotNum());
        invEntity.setQtyEaOp(qtyPreallocDiff);
        invEntity.setOrgId(wmSoDetailModel.getOrgId());
        inventoryService.updateInventory(invEntity);

        // 7、更新出库单明细行、波次单明细、出库单、波次单
        outboundCommon.updateOrder(actionCode, qtyPreallocDiff, wmSoDetailModel.getSoNo(), wmSoDetailModel.getLineNo(), soPreallocEntity.getWaveNo(), wmSoDetailModel.getOrgId());
        return wmSoPreallocService.getEntityByPreallocId(wmSoPreallocModel.getPreallocId(), wmSoPreallocModel.getOrgId());
    }

    /**
     * 获取预配批次号的批次库存
     *
     * @param soPreallocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    public BanQinInventoryEntity getInvLot(BanQinWmSoPreallocEntity soPreallocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 4、获取库存批次数量， 校验可用库存数量与预配数量大小
        // 有效期 校验 获取可用库存数量
        BanQinWmInvLot invLotModel = wmInvLotService.getByLotNum(soPreallocEntity.getLotNum(), soPreallocEntity.getOrgId());
        if (invLotModel == null) {
            throw new WarehouseException("查询不到批次库存", soPreallocEntity.getLotNum());
        }
        // 获取商品信息
        BanQinCdWhSku skuModel = cdWhSkuService.getByOwnerAndSkuCode(soPreallocEntity.getOwnerCode(), soPreallocEntity.getSkuCode(), soPreallocEntity.getOrgId());
        if (skuModel == null) {
            throw new WarehouseException("货主{0}的商品{1}信息不存在或错误", soPreallocEntity.getOwnerCode(), soPreallocEntity.getSkuCode());
        }
        // 校验出库效期
        if (!(wmCommon.checkOutValidity(skuModel.getIsValidity(), skuModel.getShelfLife(), skuModel.getLifeType(), skuModel.getOutLifeDays(), wmSoDetailModel.getLotAtt01(), wmSoDetailModel
                .getLotAtt02()))) {
            // 超过出库效期，不能操作，批次号{0}
            throw new WarehouseException("超过出库效期，不能操作，批次号{0}", soPreallocEntity.getLotNum());
        }
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        BeanUtils.copyProperties(invLotModel, invEntity);
        return invEntity;
    }

}