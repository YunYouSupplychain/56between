package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLoc;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 手工分配方法类
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundManualAllocService {
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
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 波次单明细
    @Autowired
    protected BanQinWmWvDetailService wmWvDetailService;
    // 批次库位库存
    @Autowired
    protected BanQinWmInvLotLocService wmInvLotLocService;
    // 商品
    @Autowired
    protected BanQinCdWhSkuService cdWhSkuService;
    // 编码生成器
    @Autowired
    protected SynchronizedNoService noService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;
    // 商品拣货区
    @Autowired
    protected BanQinCdWhSkuLocService cdWhSkuLocService;
    // 预配明细
    @Autowired
    protected BanQinWmSoPreallocService wmSoPreallocService;
    // 取消分配
    @Autowired
    protected BanQinWmDelAllocService wmDelAllocService;
    @Autowired
    private BanQinOutboundCancelAllocService outboundManualAlloc;
    @Autowired
    private BanQinOutboundPickingService outboundPickingService;

    /**
     * 手工分配 表更新顺序：取消预配、取消分配、预配、分配、出库单行、波次单行(状态)、出库单、波次单 库存、交易
     * @param wmSoAllocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoAllocEntity outboundManualAlloc(BanQinWmSoAllocEntity wmSoAllocEntity) throws WarehouseException {
        BanQinWmSoAllocEntity entity;
        // 前台校验：保存结束后界面控制除数量外、都不可修改
        // 1、获取出库单，校验拦截、冻结状态、取消、关闭状态
        BanQinWmSoHeader wmSoHeaderModel = this.wmSoHeaderService.checkStatus(wmSoAllocEntity.getSoNo(), ActionCode.ONESTEP_ALLOCATION, wmSoAllocEntity.getOrgId());
        // 订单审核状态
        if (wmSoHeaderModel.getAuditStatus().equals(WmsCodeMaster.AUDIT_NEW.getCode())) {
            // 订单{0}未审核
            throw new WarehouseException("订单" + wmSoHeaderModel.getSoNo() + "未审核");
        }
        // 2、获取出库单行信息(出库单号、行号),校验取消状态
        BanQinWmSoDetail wmSoDetailModel = wmSoDetailService.checkStatus(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getLineNo(), wmSoAllocEntity.getOrgId());
        // 3.1 手工分配新增保存
        if (StringUtils.isEmpty(wmSoAllocEntity.getId())) {
            // 分配明细、收货人
            wmSoAllocEntity.setConsigneeCode(wmSoHeaderModel.getConsigneeCode());
            entity = insertAlloc(wmSoAllocEntity, wmSoDetailModel);
        }
        // 3.2 手工分配修改保存
        else {
            entity = modifyAlloc(wmSoAllocEntity, wmSoDetailModel);
        }

        return entity;
    }

    /**
     * 新增分配明细
     * @param wmSoAllocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoAllocEntity insertAlloc(BanQinWmSoAllocEntity wmSoAllocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 1、校验分配数量 小于等于 可分配数量
        // 出库明细行可分配数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
        Double qtyAlloc = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);
        // 如果可分配数量 < 当前分配数量，不能手工分配，订货数不足
        if (qtyAlloc < wmSoAllocEntity.getQtyEa()) {
            // 分配数量大于订货数，不能操作
            throw new WarehouseException("分配数量大于订货数，不能操作");
        }
        // 2、获取包装单位装箱标记
        BanQinCdWhPackageRelationEntity packageRelationEntity = wmCommon.getPackageRelationAndQtyUom(wmSoAllocEntity.getPackCode(), wmSoAllocEntity.getUom(), wmSoAllocEntity.getQtyEa(), wmSoAllocEntity.getOrgId());
        /*
         * 3、根据界面传入的批次号、库位、跟踪号 获取批次库位库存 可分配数量， 校验可用库存数量与分配数量大小
         * 校验效期：如果商品配置开启效期控制，那么校验批次库存是否符合效期期, 不符合，提示信息 校验可分配数量 大于等于
         * 分配数量(分配明细输入的分配操作数)，固定拣货位，超量分配，超量补货
         */
        BanQinAllocInvLotLocEntity invLotLocEntity = new BanQinAllocInvLotLocEntity();
        invLotLocEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());
        invLotLocEntity.setSkuCode(wmSoAllocEntity.getSkuCode());
        invLotLocEntity.setLotNum(wmSoAllocEntity.getLotNum());
        invLotLocEntity.setLocCode(wmSoAllocEntity.getLocCode());
        invLotLocEntity.setTraceId(wmSoAllocEntity.getTraceId());
        invLotLocEntity.setLotAtt01(wmSoDetailModel.getLotAtt01());
        invLotLocEntity.setLotAtt02(wmSoDetailModel.getLotAtt02());
        invLotLocEntity.setOrgId(wmSoDetailModel.getOrgId());

        BanQinInventoryEntity invEntity = getInvLotLoc(invLotLocEntity);
        // 库存可分配数量 = 可超量分配数量+ 库存总数量-冻结数量-分配数量-已拣货数量-上架待出数量-补货待出数量-移动待出数量
        Double qtyAllocInv = inventoryService.getAvailableAllocQty(invEntity);

        // 超量分配数量
        invLotLocEntity.setQtyEaOp(qtyAlloc);// 操作数=需要分配的数量
        invLotLocEntity.setQtyUomOp(wmSoAllocEntity.getQtyUom());
        Double qtyOverAlloc = getOverAlloc(invLotLocEntity, invEntity, packageRelationEntity.getQtyUom().intValue());
        // 库存更新类型
        ActionCode actionCode = ActionCode.ONESTEP_ALLOCATION;// 默认一步分配
        if (qtyOverAlloc != 0) {
            qtyAllocInv += qtyOverAlloc;
            actionCode = ActionCode.ONESTEP_OVER_ALLOCATION;// 一步超量分配
        }
        // 如果库存总可分配数量<将分配数量,库存不足
        if (qtyAllocInv < wmSoAllocEntity.getQtyEa()) {
            // 有效库存不足,批次{0} 库位{1} 跟踪号{2}
            throw new WarehouseException("有效库存不足, 批次[" + wmSoAllocEntity.getLotNum() + "]库位[" + wmSoAllocEntity.getToLoc() + "]跟踪号[" + wmSoAllocEntity.getToId() + "]");
        }

        // 4、获取包装单位装箱标记
        String isPackBox = packageRelationEntity.getCdprIsPackBox();
        // 5、新增分配明细：生成分配编号， 波次单号
        // 新增：生成分配编号， 波次单号，预配单位数量换算
        // 如果装箱标记为N，那么不复核 ，拣货后不能执行复核，直接发货
        // 如果装箱标记为Y，那么需要复核后，并且拣货并且复核后才可发货
        if (WmsConstants.NO.equals(isPackBox)) {
            wmSoAllocEntity.setCheckStatus(WmsCodeMaster.CHECK_NOT.getCode());
        } else {
            wmSoAllocEntity.setCheckStatus(WmsCodeMaster.CHECK_NEW.getCode());
        }
        // 生成TO_ID(跟踪号)
        if (StringUtils.isEmpty(wmSoAllocEntity.getToId())) {
            // 目标跟踪号
            String toId = null;
            // 分配参数：分配时是否自动产生跟踪号（Y：自动产生；N：默认为*）
            String allocTraceId = "N";
            if (allocTraceId.equals(WmsConstants.YES)) {
                toId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
            } else {
                toId = WmsConstants.TRACE_ID;
            }
            wmSoAllocEntity.setToId(toId);
        }

        wmSoAllocEntity.setOrgId(wmSoDetailModel.getOrgId());
        // 分配ID生成
        String allocId = noService.getDocumentNo(GenNoType.WM_ALLOC_ID.name());
        wmSoAllocEntity.setAllocId(allocId);
        // 波次号
        BanQinWmWvDetail wvDetailModel = wmWvDetailService.findBySoNo(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
        if (wvDetailModel != null) {
            wmSoAllocEntity.setWaveNo(wvDetailModel.getWaveNo());
        }
        // 6、统一更新
        return updateAlloc(actionCode, wmSoAllocEntity.getQtyEa(), null, wmSoAllocEntity, wmSoDetailModel);
    }

    /**
     * 修改分配明细
     * @param wmSoAllocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoAllocEntity modifyAlloc(BanQinWmSoAllocEntity wmSoAllocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 1、校验预配数量+分配数量+拣货数量+发货数量 小于等于 订货数量
        // 源分配明细获取
        BanQinWmSoAlloc wmSoAllocModel = wmSoAllocService.getByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
        // 2、获取包装维护，装箱标记
        BanQinCdWhPackageRelationEntity packageRelationEntity = wmCommon.getPackageRelationAndQtyUom(wmSoAllocEntity.getPackCode(), wmSoAllocEntity.getUom(), wmSoAllocEntity.getOrgId());
        // 包装装箱标记--确定复核状态
        String isPackBox = packageRelationEntity.getCdprIsPackBox();
        // 包装单位换算数量
        Integer uomQty = packageRelationEntity.getCdprQuantity();
        // 复核状态
        String checkStatus = null;
        // 如果装箱标记为Y,那么复核状态为"00"，需要复核，并且需拣货并且复核后才可发货
        // 如果装箱标记为N,那么复核状态为"90"，不需要复核，拣货后直接发货
        if (isPackBox.equals(WmsConstants.YES)) {
            checkStatus = WmsCodeMaster.CHECK_NEW.getCode();
        } else {
            checkStatus = WmsCodeMaster.CHECK_NOT.getCode();
        }
        // 设置复核状态
        wmSoAllocEntity.setCheckStatus(checkStatus);
        /*
         * 3、根据界面传入的批次号、库位、跟踪号 获取批次库位库存 可分配数量， 校验可用库存数量与分配数量大小
         * 校验效期：如果商品配置开启效期控制，那么校验批次库存是否符合效期期，不符合，提示信息 校验可分配数量 大于等于
         * 分配数量(分配明细输入的分配操作数)，否，提示信息
         * 超量分配计算、补货：如果该商品拣货位配置该拣货位，那么校验是否超量分配，获取可超量分配的库存
         */
        // 获取批次库位库存
        BanQinAllocInvLotLocEntity invLotLocEntity = new BanQinAllocInvLotLocEntity();
        invLotLocEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());
        invLotLocEntity.setSkuCode(wmSoAllocEntity.getSkuCode());
        invLotLocEntity.setLotNum(wmSoAllocEntity.getLotNum());
        invLotLocEntity.setLocCode(wmSoAllocEntity.getLocCode());
        invLotLocEntity.setTraceId(wmSoAllocEntity.getTraceId());
        invLotLocEntity.setLotAtt01(wmSoDetailModel.getLotAtt01());
        invLotLocEntity.setLotAtt02(wmSoDetailModel.getLotAtt02());
        invLotLocEntity.setOrgId(wmSoDetailModel.getOrgId());

        BanQinInventoryEntity invEntity = getInvLotLoc(invLotLocEntity);
        // 4、数量修改处理
        Double qtyAllocDiff = 0D; // 差异数量
        ActionCode actionCode = null;

        // 4.1、数量未更改 ，原分配数量=新分配数量
        if (wmSoAllocModel.getQtyEa().equals(wmSoAllocEntity.getQtyEa())) {
            // 操作成功
            throw new WarehouseException("操作成功");
        }
        // 4.2、数量改小，取消分配
        else if (wmSoAllocModel.getQtyEa() > wmSoAllocEntity.getQtyEa()) {
            // 数量改小，比较 差异数量 与 库存记录分配数量, 原分配数量>新增分配数量，取消分配
            qtyAllocDiff = wmSoAllocModel.getQtyEa() - wmSoAllocEntity.getQtyEa();
            // 1、校验取消分配数量与出库单行分配数量，是否足够取消分配
            if (wmSoDetailModel.getQtyAllocEa() < qtyAllocDiff) {
                // 没有足够的分配数取消
                throw new WarehouseException("没有足够的分配数取消");
            }
            // 2、比较库存分配数量与分配数 大小，是否足够取消分配
            if (invEntity.getQtyAlloc() < qtyAllocDiff) {
                // 有效库存不足,批次{0} 库位{1} 跟踪号{2}
                throw new WarehouseException("有效库存不足", wmSoAllocEntity.getLotNum(), wmSoAllocEntity.getToLoc(), wmSoAllocEntity.getToId());
            }
            // 3、库存更新ACTION_CODE设置
            actionCode = ActionCode.CANCEL_ALLOCATION;// 取消分配
        }
        // 4.3、数量改大，一步分配
        else if (wmSoAllocModel.getQtyEa() < wmSoAllocEntity.getQtyEa()) {
            // 差异数量,原分配数量<新增分配数量 , 新分配数量-原分配数量
            qtyAllocDiff = wmSoAllocEntity.getQtyEa() - wmSoAllocModel.getQtyEa();

            // 更新预配明细表,预配数量-
            BanQinWmSoPrealloc soPreallocModel = null;// 预配记录
            // 是否是两步分配的标识，true: 两步分配，false ：一步分配
            Boolean twoStepFlag = false;//
            if (StringUtils.isNotEmpty(wmSoAllocModel.getPreallocId())) {
                twoStepFlag = true;
                // 获取预配记录
                soPreallocModel = wmSoPreallocService.getByPreallocId(wmSoAllocModel.getPreallocId(), wmSoAllocModel.getOrgId());
            }
            // 如果可分配数量 < 当前分配数量，不能手工分配，订货数不足
            if (twoStepFlag) {
                // 两步分配
                // 出库明细行可分配数量 = 预配数量
                Double qtyAlloc = soPreallocModel.getQtyPreallocEa();
                if (qtyAlloc < qtyAllocDiff) {
                    // 分配数量大于预配数量，不能操作
                    throw new WarehouseException("分配数量大于预配数量，不能操作");
                }
            } else {
                // 一步分配
                // 出库明细行可分配数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
                Double qtyAlloc = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);
                if (qtyAlloc < qtyAllocDiff) {
                    // 分配数量大于订货数，不能操作
                    throw new WarehouseException("分配数量大于订货数，不能操作");
                }
            }
            // 库存可分配数量 = 可超量分配 + 库存总数量-冻结数量-分配数量-已拣货数量-上架待出数量-补货待出数量-移动待出数量
            Double qtyAllocInv = inventoryService.getAvailableAllocQty(invEntity);
            invLotLocEntity.setQtyEaOp(qtyAllocDiff);// 操作数=需要分配的数量
            invLotLocEntity.setQtyUomOp(wmSoAllocEntity.getQtyUom());
            Double qtyOverAlloc = getOverAlloc(invLotLocEntity, invEntity, uomQty);
            // 总可分配数量
            qtyAllocInv += qtyOverAlloc;

            // 4、比较可用库存与分配数 大小，是否足够分配
            if (qtyAllocInv < qtyAllocDiff) {
                // 有效库存不足,批次{0} 库位{1} 跟踪号{2}
                throw new WarehouseException("有效库存不足", wmSoAllocEntity.getLotNum(), wmSoAllocEntity.getToLoc(), wmSoAllocEntity.getToId());
            }
            // 更新
            if (!twoStepFlag) {
                // 一步分配
                // actionCode设置,
                if (qtyOverAlloc > 0D) {
                    actionCode = ActionCode.ONESTEP_OVER_ALLOCATION;// 一步超量分配
                } else {
                    actionCode = ActionCode.ONESTEP_ALLOCATION;
                }
            } else {
                // 两步分配
                if (qtyOverAlloc > 0D) {
                    actionCode = ActionCode.OVER_ALLOCATION;// 两步超量分配
                } else {
                    actionCode = ActionCode.ALLOCATION;
                }
                // 更新预配明细表,预配数量-
                soPreallocModel.setQtyPreallocEa(soPreallocModel.getQtyPreallocEa() - qtyAllocDiff);
                wmSoPreallocService.save(soPreallocModel);
            }
        }
        if (actionCode == null) {
            throw new WarehouseException("操作代码不能为空");
        }
        // 统一更新
        return updateAlloc(actionCode, qtyAllocDiff, uomQty.doubleValue(), wmSoAllocEntity, wmSoDetailModel);
    }

    /**
     * 手工分配 修改分配明细 保存统一方法
     *
     * @param actionCode
     * @param qtyAllocDiff
     * @param uomQty
     * @param wmSoAllocEntity
     * @param wmSoDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmSoAllocEntity updateAlloc(ActionCode actionCode, Double qtyAllocDiff, Double uomQty, BanQinWmSoAllocEntity wmSoAllocEntity, BanQinWmSoDetail wmSoDetailModel) throws WarehouseException {
        // 取消分配，写取消分配记录
        if (actionCode.equals(ActionCode.CANCEL_ALLOCATION)) {
            BanQinWmSoAlloc soAllocModel = new BanQinWmSoAlloc();
            BeanUtils.copyProperties(wmSoAllocEntity, soAllocModel);
            soAllocModel.setQtyEa(qtyAllocDiff);
            soAllocModel.setQtyUom(qtyAllocDiff / uomQty);
            wmDelAllocService.saveDelAlloc(soAllocModel);
        }
        // 1、保存分配明细
        BanQinWmSoAlloc soAllocModel = new BanQinWmSoAlloc();
        BeanUtils.copyProperties(wmSoAllocEntity, soAllocModel);
        if (StringUtils.isBlank(soAllocModel.getId())) {
            soAllocModel.setId(IdGen.uuid());
            soAllocModel.setIsNewRecord(true);
        }
        soAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode());
        wmSoAllocService.save(soAllocModel);

        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        // 2、更新库存
        invEntity.setAction(actionCode);
        invEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());// 货主
        invEntity.setSkuCode(wmSoAllocEntity.getSkuCode());// 商品
        invEntity.setLotNum(wmSoAllocEntity.getLotNum());// 批次号
        invEntity.setLocCode(wmSoAllocEntity.getLocCode());// 分配库位
        invEntity.setTraceId(wmSoAllocEntity.getTraceId());// 分配库位跟踪号
        invEntity.setQtyEaOp(qtyAllocDiff);// 分配数量
        invEntity.setOrgId(wmSoAllocEntity.getOrgId());
        inventoryService.updateInventory(invEntity);

        // 3、更新出库单行数量、状态，波次单状态，出库单状态，波次单状态
        outboundCommon.updateOrder(actionCode, qtyAllocDiff, wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getLineNo(), wmSoAllocEntity.getWaveNo(), wmSoAllocEntity.getOrgId());
        return wmSoAllocService.getEntityByAllocId(soAllocModel.getAllocId(), soAllocModel.getOrgId());
    }

    /**
     * 获取批次库位库存
     *
     * @param invLotLocEntity
     * @return
     * @throws WarehouseException
     */
    public BanQinInventoryEntity getInvLotLoc(BanQinAllocInvLotLocEntity invLotLocEntity) throws WarehouseException {
        /*
         * 4、根据界面传入的批次号、库位、跟踪号 获取批次库位库存 可分配数量， 校验可用库存数量与分配数量大小
         * 校验效期：如果商品配置开启效期控制，那么校验批次库存是否符合效期期, 不符合，提示信息 校验可分配数量 大于等于
         * 分配数量(分配明细输入的分配操作数)，固定拣货位，超量分配，超量补货
         */
        BanQinWmInvLotLoc invLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(invLotLocEntity.getLotNum(), invLotLocEntity.getLocCode(), invLotLocEntity.getTraceId(), invLotLocEntity.getOrgId());
        if (invLotLocModel == null) {
            throw new WarehouseException("查询不到批次库位库存,批次号[" + invLotLocEntity.getLotNum() + "]库位[" + invLotLocEntity.getLocCode() + "]跟踪号[" + invLotLocEntity.getTraceId() + "]");
        }
        // 获取商品信息
        BanQinCdWhSku skuModel = cdWhSkuService.getByOwnerAndSkuCode(invLotLocEntity.getOwnerCode(), invLotLocEntity.getSkuCode(), invLotLocEntity.getOrgId());
        if (skuModel == null) {
            throw new WarehouseException("货主[" + invLotLocEntity.getOwnerCode() + "]的商品[" + invLotLocEntity.getSkuCode() + "]信息不存在或错误");
        }
        // 校验出库效期
        if (!(wmCommon.checkOutValidity(skuModel.getIsValidity(), skuModel.getShelfLife(), skuModel.getLifeType(), skuModel.getOutLifeDays(), invLotLocEntity.getLotAtt01(), invLotLocEntity
                .getLotAtt02()))) {
            throw new WarehouseException("超过出库效期，不能操作，批次号[" + invLotLocEntity.getLotNum() + "]库位[" + invLotLocEntity.getLocCode() + "]跟踪号[" + invLotLocEntity.getTraceId() + "]");
        }
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        BeanUtils.copyProperties(invLotLocModel, invEntity);
        return invEntity;
    }

    /**
     * 获取超量分配数量
     *
     * @param invLotLocEntity
     * @param invEntity
     * @param uomQty
     * @return
     * @throws WarehouseException
     */
    public Double getOverAlloc(BanQinAllocInvLotLocEntity invLotLocEntity, BanQinInventoryEntity invEntity, Integer uomQty) throws WarehouseException {
        // 超量分配数量
        Double qtyOverAlloc = 0D;
        // 是否超量分配
        // 库存可分配数量 =
        Double qtyAllocInv = inventoryService.getAvailableAllocQty(invEntity);
        // 分配数量<=库存数量,可以完全分配,不需要超量分配
        if (invLotLocEntity.getQtyEaOp() <= qtyAllocInv) {
            return qtyOverAlloc;
        }
        // 获取商品固定拣货位，如果该分配库位是固定拣货位，那么可以执行超量分配
        BanQinCdWhSkuLoc cdWhSkuLocModel = cdWhSkuLocService.getByLocCode(invLotLocEntity.getOwnerCode(), invLotLocEntity.getSkuCode(), invLotLocEntity.getLocCode(), invLotLocEntity.getOrgId());
        if (cdWhSkuLocModel == null) {
            return qtyOverAlloc;
        }
        // 超量分配
        if (cdWhSkuLocModel.getIsOverAlloc().equals(WmsConstants.YES)) {
            /**
             * 超量分配数量计算
             */
            invLotLocEntity = outboundCommon.getOverInvLot(invLotLocEntity.getQtyUomOp(), uomQty.doubleValue(), invLotLocEntity.getLocCode(), invLotLocEntity.getOwnerCode(), invLotLocEntity
                    .getSkuCode(), invLotLocEntity.getLotNum(), invLotLocEntity.getOrgId());
            if (invLotLocEntity != null) {
                qtyOverAlloc = invLotLocEntity.getQtyEaOp();
            }
        }
        return qtyOverAlloc;
    }

    /**
     * 手工拣货确认
     * @param entity
     * @return
     */
    @Transactional
    public ResultMessage pickingByManual(BanQinWmSoAllocEntity entity) {
        ResultMessage msg = new ResultMessage();
        if (StringUtils.isNotEmpty(entity.getCdType())) {
            // 只能在越库任务界面执行出库，[{0}]属于越库，不能操作
            msg.addMessage("只能在越库任务界面执行出库" + entity.getAllocId());
            msg.setSuccess(false);
            return msg;
        }
        outboundManualAlloc.OutboundCancelAlloc(entity);
        // 手工分配 剩余的再次分配
        if (entity.getQtyPkEa() < entity.getQtyEa()) {
            BanQinWmSoAllocEntity newEntity = new BanQinWmSoAllocEntity();
            BeanUtils.copyProperties(entity, newEntity);
            newEntity.setId(null);
            newEntity.setQtyUom(entity.getQtyUom() - entity.getQtyPkUom());
            newEntity.setQtyEa(entity.getQtyEa() - entity.getQtyPkEa());
            outboundManualAlloc(newEntity);
        }
        entity.setId(null);
        entity.setLocCode(entity.getPickLoc());
        entity.setLotNum(entity.getPickLotNum());
        entity.setTraceId(entity.getPickTraceId());
        entity.setQtyUom(entity.getQtyPkUom());
        entity.setQtyEa(entity.getQtyPkEa());
        BanQinWmSoAllocEntity allocEntity = outboundManualAlloc(entity);
        // 拣货确认
        entity.setToLoc(entity.getPickToLoc());
        entity.setToId(entity.getPickToId());
        msg = outboundPickingService.outboundPicking(entity);
        return msg;
    }
}