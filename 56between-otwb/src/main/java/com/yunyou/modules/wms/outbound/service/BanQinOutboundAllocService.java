package com.yunyou.modules.wms.outbound.service;

import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
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
 * 分配
 * @author WMJ
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundAllocService {
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 出库公共
	@Autowired
	protected BanQinOutboundCommonService outboundCommon;
	// 出库单明细
	@Autowired
	protected BanQinWmSoDetailService wmSoDetailManager;
	// 预配明细
	@Autowired
	protected BanQinWmSoPreallocService wmSoPreallocManager;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocManager;
	// 编号生成
	@Autowired
	protected SynchronizedNoService noService;
	// 批次库位库存
	@Autowired
	protected BanQinWmInvLotLocService wmInvLotLocManager;
	// 库存更新
	@Autowired
	protected BanQinInventoryService inventoryServiceManager;

    /**
     * 两步分配更新
     * @param updateEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public Double updateAllocTwoStep(BanQinAllocUpdateEntity updateEntity) throws WarehouseException {
		BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();
		String orgId = invLotLocEntity.getOrgId();
		// 1、单位数量计算 - 更新数量
		Double qtyEaOp = getQtyAllocEaOp(updateEntity);
		invLotLocEntity.setQtyEaOp(qtyEaOp);
		// 操作单位数量
		invLotLocEntity.setQtyUomOp(wmCommon.doubleDivide(qtyEaOp, updateEntity.getPackageEntity().getCdprQuantity().doubleValue()));

		// 2、预配明细更新
		BanQinWmSoPrealloc wmSoPreallocModel = wmSoPreallocManager.getByPreallocId(updateEntity.getPreallocId(), orgId);
		if (wmSoPreallocModel.getQtyPreallocEa() < qtyEaOp) {
			// 预配明细预配数量<操作数(并发控制)
			throw new WarehouseException("预配明细预配数量<操作数");
		}
		wmSoPreallocModel.setQtyPreallocEa(wmSoPreallocModel.getQtyPreallocEa() - qtyEaOp);
		wmSoPreallocModel.setQtyPreallocUom(wmCommon.doubleDivide(wmSoPreallocModel.getQtyPreallocEa(), updateEntity.getQtyPackUom()));
		wmSoPreallocManager.save(wmSoPreallocModel);

		// 3、更新分配
		updateSoAllocBySplit(updateEntity);

		// 4、库存更新
		updateInventory(updateEntity);

		// 5、更新出库单明细行数量、状态，波次单行、出库单、波次单状态
		outboundCommon.updateOrder(updateEntity.getActionCode(), qtyEaOp, updateEntity.getSoNo(), updateEntity.getLineNo(), updateEntity.getWaveNo(), orgId);
		// 返回剩余可分配数量
		return updateEntity.getQtyAllocEa() - qtyEaOp;
	}

	/**
	 * 一步分配更新
	 * @param updateEntity
	 * @throws WarehouseException
	 */
	@Transactional
	public Double updateAllocOneStep(BanQinAllocUpdateEntity updateEntity) throws WarehouseException {
		BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();
		String orgId = invLotLocEntity.getOrgId();
		// 1、单位数量计算 - 更新数量
		Double qtyEaOp = getQtyAllocEaOp(updateEntity);
		if (qtyEaOp == 0D) {
		    return 0d;
        }
		invLotLocEntity.setQtyEaOp(qtyEaOp);// 操作数量EA
		// 操作单位数量
		invLotLocEntity.setQtyUomOp(wmCommon.doubleDivide(qtyEaOp, updateEntity.getPackageEntity().getCdprQuantity().doubleValue()));
		// 2、更新分配
		updateSoAllocBySplit(updateEntity);
		// 3、库存更新
		updateInventory(updateEntity);
		// 并发控制
		BanQinWmSoDetail soDetail = wmSoDetailManager.findBySoNoAndLineNo(updateEntity.getSoNo(), updateEntity.getLineNo(), orgId);
		// 最新获取到的可分配数
		Double qtyAllocEaNew = soDetail.getQtySoEa() - soDetail.getQtyPreallocEa() - soDetail.getQtyAllocEa() - soDetail.getQtyPkEa() - soDetail.getQtyShipEa();
		// 可分配数前后不一致，数据过期
		if (!qtyAllocEaNew.equals(updateEntity.getQtyAllocEa())) {
			throw new WarehouseException(soDetail.getSoNo() + "-" + soDetail.getLineNo() + "可分配数前后不一致，数据过期");
		}
		// 4、更新出库单明细行数量、状态，波次单行、出库单、波次单状态
		outboundCommon.updateOrder(updateEntity.getActionCode(), qtyEaOp, updateEntity.getSoNo(), updateEntity.getLineNo(), updateEntity.getWaveNo(), orgId);
		// 返回剩余可分配数量
		return updateEntity.getQtyAllocEa() - qtyEaOp;
	}

    /**
     * 分配更新数量计算
     * @param updateEntity
     * @throws WarehouseException
     */
	protected Double getQtyAllocEaOp(BanQinAllocUpdateEntity updateEntity) {
		// 可分配数计算
		BanQinWmInvLotLoc invLotLocModel = new BanQinWmInvLotLoc();
		BeanUtils.copyProperties(updateEntity.getInvLotLocEntity(), invLotLocModel);
		Double qtyInv = wmInvLotLocManager.getQtyAllocAvailable(invLotLocModel);
		// 单位数量计算 - 更新数量
		Double qtyEaOp = outboundCommon.qtyEaOpByUom(updateEntity.getQtyAllocEa(), qtyInv, updateEntity.getPackageEntity().getCdprQuantity());
		return qtyEaOp;
	}

	/**
	 * 按单位拆分 更新分配明细 例如：单位PL，库存可分配单位数量超过1PL
	 * 那么分配时，分配明细结果显示为：1PL显示一条分配记录
	 * @param updateEntity
	 * @throws WarehouseException
	 */
	@Transactional
	protected void updateSoAllocBySplit(BanQinAllocUpdateEntity updateEntity) {
		BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();// 批次库位库存
		// 库存更新数
		Double qtyEa = invLotLocEntity.getQtyEaOp();// 缓存
		BanQinCdWhPackageRelationEntity packageEntity = updateEntity.getPackageEntity();// 包装
		// 是否拆单位标记,N:不拆分，Y:拆分
		String flag = WmsConstants.NO;
		// 校验是否拆单位更新分配明细
		// 单位OT
		if (packageEntity.getCdprUnitLevel().equals(WmsConstants.UOM_OT)) {
			flag = WmsConstants.NO;
		}
		// 单位PL
		else if (packageEntity.getCdprUnitLevel().equals(WmsConstants.UOM_PL)) {
			flag = WmsConstants.NO;
		}
		// 单位CS
		else if (packageEntity.getCdprUnitLevel().equals(WmsConstants.UOM_CS)) {
			flag = WmsConstants.NO;
		}
		// 单位IP
		else if (packageEntity.getCdprUnitLevel().equals(WmsConstants.UOM_IP)) {
			flag = WmsConstants.NO;
		}
		if (flag == null) {
			flag = WmsConstants.NO;
		}

		// 如果不拆分,那么直接更新分配明细
		if (flag.equals(WmsConstants.NO)) {
			// 更新分配明细
			updateSoAlloc(updateEntity);
		} else {
			// 拆单位更新
			int size = invLotLocEntity.getQtyUomOp().intValue();
			Double qtyEaOp = invLotLocEntity.getQtyEaOp();
			for (int i = 0; i < size; i++) {
				if (qtyEaOp <= 0) {
					break;
				}
				// 写保存分配明细
				invLotLocEntity.setQtyEaOp(packageEntity.getCdprQuantity().doubleValue());
				updateSoAlloc(updateEntity);
				// 剩余库存可分配数量
				qtyEaOp -= packageEntity.getCdprQuantity().doubleValue();
			}
			invLotLocEntity.setQtyEaOp(qtyEa);// 值回归
		}

	}

	/**
	 * 新增分配明细
	 * @param updateEntity
	 * @return
	 */
	@Transactional
	public void updateSoAlloc(BanQinAllocUpdateEntity updateEntity) {
		BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();// 批次库位库存
		BanQinCdWhPackageRelationEntity packageEntity = updateEntity.getPackageEntity();// 包装
        String orgId = invLotLocEntity.getOrgId();
        
		// 分配EA数量
		Double qtyEa = packageEntity.getCdprQuantity().doubleValue();
		// 新增分配明细
		BanQinWmSoAlloc wmSoAllocModel = new BanQinWmSoAlloc();
		wmSoAllocModel.setOrgId(orgId);// 分公司
		// 分配ID
		String allocId = noService.getDocumentNo(GenNoType.WM_ALLOC_ID.name());
		wmSoAllocModel.setAllocId(allocId);
		wmSoAllocModel.setPreallocId(updateEntity.getPreallocId());// 预配ID
		wmSoAllocModel.setWaveNo(updateEntity.getWaveNo());// 波次单号
		wmSoAllocModel.setSoNo(updateEntity.getSoNo());// 出库单号
		wmSoAllocModel.setLineNo(updateEntity.getLineNo());// 行号
		wmSoAllocModel.setOwnerCode(invLotLocEntity.getOwnerCode());// 货主编码
		wmSoAllocModel.setSkuCode(invLotLocEntity.getSkuCode());// 商品编码
		wmSoAllocModel.setLotNum(invLotLocEntity.getLotNum());// 批次号
		wmSoAllocModel.setLocCode(invLotLocEntity.getLocCode());// 库位编码
		wmSoAllocModel.setTraceId(invLotLocEntity.getTraceId());// 分配库位跟踪号
		wmSoAllocModel.setConsigneeCode(updateEntity.getConsigneeCode());// 收货人编码-由出库单header获得
		// 添加快递单号
		wmSoAllocModel.setTrackingNo(updateEntity.getTrackingNo());// 快递单号-由出库单header获得
		wmSoAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode());// 状态 -
		// 40
		// 复核状态(00未复核、99已复核、90不复核)
		String checkStatus = null;
		// 复核状态初始值获取修改
		// 复核打包参数：是否一定要复核才可以发运（Y：需要复核；N：不用复核）
		String shipNeedCheck = "N";
		// 如果Y,那么复核状态全部为【未复核】，不考虑包装维护的装箱标记
		if (shipNeedCheck.equals(WmsConstants.YES)) {
			checkStatus = WmsCodeMaster.CHECK_NEW.getCode();
		} else {
			// 如果N,那么复核状态根据包装维护的是否装箱标记，装箱标记为Y,状态为【未复核】，装箱标记为N,状态为【不复核】
			if (WmsConstants.YES.equals(packageEntity.getCdprIsPackBox())) {
				checkStatus = WmsCodeMaster.CHECK_NEW.getCode();// 需要复核，
			} else {
				checkStatus = WmsCodeMaster.CHECK_NOT.getCode();
			}
		}

		wmSoAllocModel.setCheckStatus(checkStatus);
		wmSoAllocModel.setPackCode(packageEntity.getCdpaCode());// 包装编码
		wmSoAllocModel.setUom(packageEntity.getCdprUnitLevel());// 包装单位
		wmSoAllocModel.setQtyEa(invLotLocEntity.getQtyEaOp());// 操作数量
		wmSoAllocModel.setQtyUom(wmCommon.doubleDivide(invLotLocEntity.getQtyEaOp(), packageEntity.getCdprQuantity().doubleValue()));// 包装数量
		String toLoc = "SORTATION";
		wmSoAllocModel.setToLoc(toLoc);// 目标库位编码
		// 目标跟踪号
		String toId = null;
		// 分配参数：分配时是否自动产生跟踪号（Y：自动产生；N：默认为*）
		String allocTraceId = "N";
		if (allocTraceId.equals(WmsConstants.YES)) {
			toId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
		} else {
			toId = WmsConstants.TRACE_ID;
		}
		wmSoAllocModel.setToId(toId);// 目标跟踪号
		wmSoAllocModel.setPickOp(null);// 拣货人员
		wmSoAllocModel.setPickTime(null);// 拣货时间
		wmSoAllocModel.setCheckOp(null);// 复核人员
		wmSoAllocModel.setCheckTime(null);// 复核时间
		wmSoAllocModel.setShipOp(null);// 发货人员
		wmSoAllocModel.setShipTime(null);// 发货时间
		this.wmSoAllocManager.save(wmSoAllocModel);
	}

	/**
	 * 库存更新
	 * @param updateEntity
	 * @throws WarehouseException
	 */
	@Transactional
	public void updateInventory(BanQinAllocUpdateEntity updateEntity) throws WarehouseException {
        BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();
		// 库存更新
		BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
		invEntity.setAction(updateEntity.getActionCode());// 两步分配
		invEntity.setOwnerCode(invLotLocEntity.getOwnerCode());// 货主
		invEntity.setSkuCode(invLotLocEntity.getSkuCode());// 商品
		invEntity.setLotNum(invLotLocEntity.getLotNum());// 批次号
		invEntity.setLocCode(invLotLocEntity.getLocCode());// 库位
		invEntity.setTraceId(invLotLocEntity.getTraceId());// 跟踪号
		invEntity.setQtyEaOp(invLotLocEntity.getQtyEaOp());// 分配操作数量
        invEntity.setOrgId(invLotLocEntity.getOrgId());

		inventoryServiceManager.updateInventory(invEntity);
	}
}