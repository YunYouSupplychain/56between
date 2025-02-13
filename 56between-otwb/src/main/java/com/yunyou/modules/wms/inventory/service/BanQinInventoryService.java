package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.inventory.entity.*;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvLotLocMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 库存统一操作更新服务
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinInventoryService extends CrudService<BanQinWmInvLotLocMapper, BanQinWmInvLotLoc> {
	@Autowired
	protected BanQinWmInvLotService wmInvLotService;
	@Autowired
	protected BanQinWmInvLotLocService wmInvLotLocService;
	@Autowired
	protected BanQinWmActTranService wmActTranService;
	@Autowired
	protected BanQinWmHoldService wmHoldService;
	@Autowired
	protected BanQinWmInvLotAttService wmInvLotAttService;
	@Autowired
	protected BanQinCdWhLocService cdWhLocService;
	@Autowired
    protected SynchronizedNoService noService;

	/**
	 * 库存更新：
	 *
	 * @param entity
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public BanQinInventoryEntity updateInventory(BanQinInventoryEntity entity) {
		ActionCode actionCode = entity.getAction();
		String tranId = null;
        BanQinInventoryEntity invm = new BanQinInventoryEntity();
		invm.setLotNum(entity.getLotNum());
		invm.setLocCode(entity.getLocCode());
		invm.setTraceId(entity.getTraceId());
		invm.setSkuCode(entity.getSkuCode());
		invm.setOwnerCode(entity.getOwnerCode());
		invm.setOrderNo(entity.getOrderNo());
		invm.setLineNo(entity.getLineNo());
		invm.setAction(entity.getAction());
		invm.setPackCode(entity.getPackCode());
		invm.setQtyUom(entity.getQtyUom());
		invm.setUom(entity.getUom());
		invm.setTaskId(entity.getTaskId());
		invm.setPlanLocCode(entity.getPlanLocCode());
		invm.setPlanTraceId(entity.getPlanTraceId());
		invm.setOrgId(entity.getOrgId());

		if (actionCode == null) {
			throw new WarehouseException("操作代码不能为空");
		} else if (actionCode == ActionCode.PLAN_LOCATION) {
			invm.setQtyPaIn(entity.getQtyEaOp());
			this.updateToInvLotLoc(invm);
		} else if (actionCode == ActionCode.CANCEL_PLAN_LOCATION) {
			invm.setQtyPaIn(entity.getQtyEaOp());
			this.updateFmInvLotLoc(invm);
		} else if (actionCode == ActionCode.RECEIVING) {
			// 库存校验
			checkInvMixSkuMixLot(entity);
			// 忽略ID
			entity = this.loseTraceId(entity);
			invm.setTraceId(entity.getTraceId());
			// 是否冻结
			entity = this.holdInventory(entity);
			invm.setQtyHold(entity.getQtyHold());

			invm.setQty(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			invm = this.updateToInvLotLoc(invm);
			checkInvTraceidDiffLoc(entity);
			tranId = this.addActTranTo(invm);
		} else if (actionCode == ActionCode.CANCEL_RECEIVING) {
			invm.setQty(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm = this.updateFmInvLotLoc(invm);
			tranId = this.addActTranFm(invm);
		} else if (actionCode == ActionCode.CANCEL_PICKING) {
			invm.setQtyPk(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm = this.updateFmInvLotLoc(invm);
		} else if (actionCode == ActionCode.SHIPMENT) {
			invm.setQty(entity.getQtyEaOp());
			invm.setQtyPk(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm = this.updateFmInvLotLoc(invm);
			tranId = this.addActTranFm(invm);
		} else if (actionCode == ActionCode.CANCEL_SHIPMENT) {
			// 库存校验
			checkInvMixSkuMixLot(entity);
			invm.setQty(entity.getQtyEaOp());
			invm.setQtyPk(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			invm = this.updateToInvLotLoc(invm);
			checkInvTraceidDiffLoc(entity);
			tranId = this.addActTranTo(invm);
		} else if (actionCode == ActionCode.PREALLOCATION) {
			Double availableQty = this.getAvailablePreallocQty(entity);
			if (availableQty.doubleValue() < entity.getQtyEaOp()) {
				// 库存可预配数不足
				throw new WarehouseException("库存可预配数不足");
			}
			invm.setQtyPrealloc(entity.getQtyEaOp());
			this.updateToInvLot(invm);
		} else if (actionCode == ActionCode.CANCEL_PREALLOCATION) {
			invm.setQtyPrealloc(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
		} else if (actionCode == ActionCode.ALLOCATION) {
			Double availableQty = this.getAvailableAllocQty(entity);
			if (availableQty.doubleValue() < entity.getQtyEaOp()) {
				throw new WarehouseException("库存可分配数不足");
			}
			invm.setQtyPrealloc(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm.setQtyPrealloc(null);
			invm.setQtyAlloc(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			this.updateToInvLotLoc(invm);
		} else if (actionCode == ActionCode.OVER_ALLOCATION) {
			invm.setQtyPrealloc(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm.setQtyPrealloc(null);
			invm.setQtyAlloc(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			this.updateToInvLotLoc(invm);
		} else if (actionCode == ActionCode.ONESTEP_OVER_ALLOCATION) {
			invm.setQtyAlloc(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			this.updateToInvLotLoc(invm);
		} else if (actionCode == ActionCode.CANCEL_ALLOCATION) {
			invm.setQtyAlloc(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			this.updateFmInvLotLoc(invm);
		} else if (actionCode == ActionCode.ONESTEP_ALLOCATION) {
			Double availableQty = this.getAvailableAllocQty(entity);
			if (availableQty.doubleValue() < entity.getQtyEaOp()) {
				// 库存可分配数不足
				throw new WarehouseException("库存可分配数不足");
			}
			invm.setQtyAlloc(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			this.updateToInvLotLoc(invm);
		} else if (actionCode == ActionCode.KIN) {
			// 库存校验
			checkInvMixSkuMixLot(entity);
			// 忽略ID
			entity = this.loseTraceId(entity);
			invm.setTraceId(entity.getTraceId());
			// 是否冻结
			entity = this.holdInventory(entity);
			invm.setQtyHold(entity.getQtyHold());

			invm.setQty(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			invm = this.updateToInvLotLoc(invm);
			checkInvTraceidDiffLoc(entity);
			tranId = this.addActTranTo(invm);
		} else if (actionCode == ActionCode.CANCEL_KIN) {
			invm.setQty(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm = this.updateFmInvLotLoc(invm);
			tranId = this.addActTranFm(invm);
		} else if (actionCode == ActionCode.KOUT) {
			invm.setQty(entity.getQtyEaOp());
			invm.setQtyPk(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm = this.updateFmInvLotLoc(invm);
			tranId = this.addActTranFm(invm);
		} else if (actionCode == ActionCode.CANCEL_KOUT) {
			// 库存校验
			checkInvMixSkuMixLot(entity);
			invm.setQty(entity.getQtyEaOp());
			invm.setQtyPk(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			invm = this.updateToInvLotLoc(invm);
			checkInvTraceidDiffLoc(entity);
			tranId = this.addActTranTo(invm);
		} else if (actionCode == ActionCode.HOLD) {
			entity = this.holdInventory(entity);
			invm.setQtyHold(entity.getQtyHold());
			this.updateToInvLot(invm);
			this.updateToInvLotLoc(invm);
		} else if (actionCode == ActionCode.CANCEL_HOLD) {
			entity = this.cancelHoldInventory(entity);
			invm.setQtyHold(entity.getQtyHold());
			this.updateFmInvLot(invm);
			this.updateFmInvLotLoc(invm);
		} else if (actionCode == ActionCode.ADJUSTMENT_LOSS) {
			if (entity.getQtyHold() != null && entity.getQtyHold() > 0.0) {
				invm.setQtyHold(entity.getQtyHold());
			}
			invm = this.checkAdjustment(invm);
			if (invm.getAvailableQty().doubleValue() < entity.getQtyEaOp()) {
				// 库存可调整数不足
				throw new WarehouseException("库存可调整数不足");
			}
			invm.setQty(entity.getQtyEaOp());
			this.updateFmInvLot(invm);
			invm = this.updateFmInvLotLoc(invm);
			tranId = this.addActTranFm(invm);
		} else if (actionCode == ActionCode.ADJUSTMENT_PROFIT) {
			// 库存校验
			checkInvMixSkuMixLot(entity);
			entity = this.holdInventory(entity);
			invm.setQtyHold(entity.getQtyHold());
			invm.setQty(entity.getQtyEaOp());
			this.updateToInvLot(invm);
			invm = this.updateToInvLotLoc(invm);
			checkInvTraceidDiffLoc(entity);
			tranId = this.addActTranTo(invm);
		}
		// 清除库存表垃圾记录
		this.cleanZeroInventory(invm);
		invm.setTranId(tranId);
		return invm;
	}

	/**
	 * 库存更新：
	 *
	 * @param fmEntity
	 * @param toEntity
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public BanQinInventoryEntity updateInventory(BanQinInventoryEntity fmEntity, BanQinInventoryEntity toEntity) {
		ActionCode actionCode = fmEntity.getAction();
		String tranId = null;
		BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
		BanQinInventoryEntity toinvm = new BanQinInventoryEntity();

		fminvm.setLotNum(fmEntity.getLotNum());
		fminvm.setLocCode(fmEntity.getLocCode());
		fminvm.setTraceId(fmEntity.getTraceId());
		fminvm.setSkuCode(fmEntity.getSkuCode());
		fminvm.setOwnerCode(fmEntity.getOwnerCode());
		fminvm.setOrderNo(fmEntity.getOrderNo());
		fminvm.setLineNo(fmEntity.getLineNo());
		fminvm.setAction(fmEntity.getAction());
		fminvm.setPackCode(fmEntity.getPackCode());
		fminvm.setQtyUom(fmEntity.getQtyUom());
		fminvm.setUom(fmEntity.getUom());
		fminvm.setTaskId(fmEntity.getTaskId());
		fminvm.setPlanLocCode(fmEntity.getPlanLocCode());
		fminvm.setPlanTraceId(fmEntity.getPlanTraceId());
        fminvm.setOrgId(fmEntity.getOrgId());

		toinvm.setLotNum(toEntity.getLotNum());
		toinvm.setLocCode(toEntity.getLocCode());
		toinvm.setTraceId(toEntity.getTraceId());
		toinvm.setSkuCode(toEntity.getSkuCode());
		toinvm.setOwnerCode(toEntity.getOwnerCode());
		toinvm.setOrderNo(toEntity.getOrderNo());
		toinvm.setLineNo(toEntity.getLineNo());
		toinvm.setAction(toEntity.getAction());
		toinvm.setPackCode(toEntity.getPackCode());
		toinvm.setQtyUom(toEntity.getQtyUom());
		toinvm.setUom(toEntity.getUom());
		toinvm.setTaskId(toEntity.getTaskId());
		toinvm.setPlanLocCode(toEntity.getPlanLocCode());
		toinvm.setPlanTraceId(toEntity.getPlanTraceId());
        toinvm.setOrgId(toEntity.getOrgId());

		if (actionCode == null) {
			throw new WarehouseException("操作代码不能为空");
		}

		else if (actionCode == ActionCode.ONESTEP_PLAN_RECEIVING) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			fminvm.setQtyPaIn(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);
			toinvm.setQty(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);
			toinvm = this.updateToInvLotLoc(toinvm);
			tranId = this.addActTranTo(toinvm);
			checkInvTraceidDiffLoc(toEntity);
		}

		else if (actionCode == ActionCode.PLAN_RECEIVING) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			if (fmEntity.getQtyEaOp().doubleValue() != fmEntity.getPlanRecQty()) {
				fminvm.setQtyPaIn(fmEntity.getPlanRecQty());
				this.updateFmInvLotLoc(fminvm);
				fminvm.setQtyPaIn(fmEntity.getQtyEaOp());
				fminvm = this.updateToInvLotLoc(fminvm);
			}

			if (!fmEntity.getLotNum().equals(toEntity.getLotNum()) || !fmEntity.getTraceId().equals(toEntity.getTraceId())) {
				fminvm.setQtyPaIn(fmEntity.getQtyEaOp());
				this.updateFmInvLotLoc(fminvm);
				// 清除库存表垃圾记录
				this.cleanZeroInventory(fminvm);
				fminvm.setLotNum(toEntity.getLotNum());
				fminvm.setTraceId(toEntity.getTraceId());
				this.updateToInvLotLoc(fminvm);
			}

			toinvm.setQty(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);
			toinvm.setQtyPaOut(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			tranId = this.addActTranTo(toinvm);
			checkInvTraceidDiffLoc(toEntity);
		}

		else if (actionCode == ActionCode.CREATE_PA_TASK) {
			fminvm.setQtyPaOut(fmEntity.getQtyEaOp());
			fminvm = this.updateToInvLotLoc(fminvm);
			// 生成的上架任务但是计算出来的计划库位为空
			if (StringUtils.isNotEmpty(toinvm.getPlanLocCode())) {
				toinvm.setLocCode(toinvm.getPlanLocCode());
				toinvm.setQtyPaIn(toEntity.getQtyEaOp());
				toinvm = this.updateToInvLotLoc(toinvm);
			}

		} else if (actionCode == ActionCode.CANCEL_PA_TASK) {
			// 取消上架任务但是任务中计算出来的计划库位为空
			if (StringUtils.isNotEmpty(fminvm.getPlanLocCode())) {
				fminvm.setLocCode(fminvm.getPlanLocCode());
				fminvm.setQtyPaIn(fmEntity.getQtyEaOp());
				fminvm = this.updateFmInvLotLoc(fminvm);
			}
			toinvm.setQtyPaOut(toEntity.getQtyEaOp());
			toinvm = this.updateFmInvLotLoc(toinvm);
		}

		else if (actionCode == ActionCode.PUTAWAY) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm.setQtyPaOut(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);
			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			tranId = this.addActTran(fminvm, toinvm);

			if (toEntity.getLocCode().equals(toEntity.getPlanLocCode()) && toEntity.getTraceId().equals(toEntity.getPlanTraceId())) {
				toinvm.setQty(null);
				toinvm.setQtyPaIn(toEntity.getQtyEaOp());
				this.updateFmInvLotLoc(toinvm);
			} else {
				if (StringUtils.isNotEmpty(toEntity.getPlanLocCode()) && StringUtils.isNotEmpty(toEntity.getPlanTraceId())) {
					toinvm.setQty(null);
					toinvm.setLocCode(toinvm.getPlanLocCode());
					toinvm.setTraceId(toEntity.getPlanTraceId());
					toinvm.setQtyPaIn(toEntity.getQtyEaOp());
					this.updateFmInvLotLoc(toinvm);
				}
			}
			checkInvTraceidDiffLoc(toEntity);
		} else if (actionCode == ActionCode.PICKING) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			fminvm.setQtyAlloc(fmEntity.getQtyEaOp());
			this.updateFmInvLot(fminvm);
			toinvm.setQtyPk(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);
			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);
		} else if (actionCode == ActionCode.MOVING) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkMoving(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				// 库存可移动数不足
				throw new WarehouseException("库存可移动数不足");
			}
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);
		} else if (actionCode == ActionCode.MOVING_BY_TRACEID) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkMoving(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				// 库存可移动数不足
				throw new WarehouseException("库存可移动数不足");
			}
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			tranId = this.addActTran(fminvm, toinvm);
		} else if (actionCode == ActionCode.TRANSFER) {
			boolean flag = this.isCheckInvMixSkuMixLot(fmEntity, toEntity);
			if (flag) {
				checkInvMixSkuMixLot(toEntity);
			}
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkTransfer(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				throw new WarehouseException("库存可转移数不足");
			}

			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			fminvm.setQty(fmEntity.getQtyEaOp());
			this.updateFmInvLot(fminvm);
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);

		} else if (actionCode == ActionCode.QC) {
			boolean flag = this.isCheckInvMixSkuMixLot(fmEntity, toEntity);
			if (flag) {
				checkInvMixSkuMixLot(toEntity);
			}
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkTransfer(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				throw new WarehouseException("库存可转移数不足");
			}

			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			fminvm.setQty(fmEntity.getQtyEaOp());
			this.updateFmInvLot(fminvm);
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);

		} else if (actionCode == ActionCode.KTF) {
			boolean flag = this.isCheckInvMixSkuMixLot(fmEntity, toEntity);
			if (flag) {
				checkInvMixSkuMixLot(toEntity);
			}
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkKitTransfer(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				// 库存可转移数不足
				throw new WarehouseException("库存可转移数不足");
			}

			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm.setQtyPk(fmEntity.getQtyEaOp());
			this.updateFmInvLot(fminvm);
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);

		} else if (actionCode == ActionCode.CANCEL_KTF) {
			boolean flag = this.isCheckInvMixSkuMixLot(fmEntity, toEntity);
			if (flag) {
				// 库存校验
				checkInvMixSkuMixLot(toEntity);
			}
			// 忽略ID 2015-1-22.1 Morice 去掉忽略ID校验 ，取消发货必须回到拣货记录的跟踪号库存中
			/*
			 * toEntity = this.loseTraceId(toEntity);
			 * toinvm.setTraceId(toEntity.getTraceId());
			 */
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkTransfer(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				// 库存可转移数不足
				throw new WarehouseException("库存可转移数不足");
			}

			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());

			fminvm.setQty(fmEntity.getQtyEaOp());
			this.updateFmInvLot(fminvm);
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm.setQtyPk(toEntity.getQtyEaOp());
			this.updateToInvLot(toinvm);
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);

		} else if (actionCode == ActionCode.PACKING) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkPacking(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				// 库存可打包数不足
				throw new WarehouseException("库存可打包数不足");
			}
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm.setQtyPk(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm.setQtyPk(fmEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);
		} else if (actionCode == ActionCode.LOADING) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			if (fmEntity.getQtyHold() != null && fmEntity.getQtyHold() > 0.0) {
				fminvm.setQtyHold(fmEntity.getQtyHold());
			}
			fminvm = this.checkLoading(fminvm);
			if (fminvm.getAvailableQty().doubleValue() < fmEntity.getQtyEaOp()) {
				// 库存装载移动数不足
				throw new WarehouseException("库存装载移动数不足");
			}
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm.setQtyPk(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm.setQtyPk(fmEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			checkInvTraceidDiffLoc(toEntity);
			tranId = this.addActTran(fminvm, toinvm);
		} else if (actionCode == ActionCode.CREATE_RP_TASK) {
			fminvm.setQtyRpOut(fmEntity.getQtyEaOp());
			fminvm = this.updateToInvLotLoc(fminvm);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());

			toinvm.setQtyRpIn(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);

		} else if (actionCode == ActionCode.CANCEL_RP_TASK) {
			fminvm.setQtyRpIn(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);

			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());

			toinvm.setQtyRpOut(toEntity.getQtyEaOp());
			toinvm = this.updateFmInvLotLoc(toinvm);

		} else if (actionCode == ActionCode.REPLENISHMENT) {
			// 库存校验
			checkInvMixSkuMixLot(toEntity);
			// 忽略ID
			toEntity = this.loseTraceId(toEntity);
			toinvm.setTraceId(toEntity.getTraceId());
			// 是否冻结
			toEntity = this.holdInventory(toEntity);
			toinvm.setQtyHold(toEntity.getQtyHold());
			this.updateToInvLot(toinvm);

			fminvm.setQty(fmEntity.getQtyEaOp());
			fminvm.setQtyRpOut(fmEntity.getQtyEaOp());
			fminvm = this.updateFmInvLotLoc(fminvm);

			toinvm.setQty(toEntity.getQtyEaOp());
			toinvm = this.updateToInvLotLoc(toinvm);
			tranId = this.addActTran(fminvm, toinvm);

			toinvm.setQty(null);
			toinvm.setQtyRpIn(toEntity.getQtyEaOp());
			this.updateFmInvLotLoc(toinvm);
		}

		// 清除库存表垃圾记录
		this.cleanZeroInventory(fminvm);
		this.cleanZeroInventory(toinvm);
		toEntity.setTranId(tranId);
		return toEntity;
	}

	/**
	 * 库存初始化校验：判断是否已经初始化，是否能不能在进行初始化。
	 * @param bool=true 为删除原本记录，bool = false 为在原本库存初始化基础上继续增加不用删除原本库存
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public void checkInitInventory(boolean bool, String orgId) {
		List<BanQinWmActTran> items = wmActTranService.checkInitInvQuery(WmsConstants.INNIT_INVENTORY, orgId);
		if (CollectionUtil.isNotEmpty(items)) {
			// 存在正式的业务数据，不能重新初始化
			throw new WarehouseException("存在正式的业务数据，不能重新初始化");
		} else {
			if (bool) {
				// 删除初始化数据
				mapper.execDeleteSql("delete from wm_inv_lot where org_id = '" + orgId + "'");
				mapper.execDeleteSql("delete from wm_inv_lot_loc where org_id = '" + orgId + "'");
				mapper.execDeleteSql("delete from wm_inv_lot_att where org_id = '" + orgId + "'");
				mapper.execDeleteSql("delete from wm_act_tran where org_id = '" + orgId + "'");
			}
		}
	}

	/**
	 * 库存初始化：
	 *
	 * @param entity
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public BanQinInventoryEntity initInventory(BanQinInventoryEntity entity) {
		String tranId = null;
		BanQinInventoryEntity invm = new BanQinInventoryEntity();
		invm.setLotNum(entity.getLotNum());
		invm.setLocCode(entity.getLocCode());
		invm.setTraceId(entity.getTraceId());
		invm.setSkuCode(entity.getSkuCode());
		invm.setOwnerCode(entity.getOwnerCode());
		invm.setOrderNo(WmsConstants.INNIT_INVENTORY);
		invm.setLineNo(null);
		invm.setPackCode(entity.getPackCode());
		invm.setQtyUom(entity.getQtyUom());
		invm.setUom(entity.getUom());
		invm.setTaskId(entity.getTaskId());
		invm.setAction(ActionCode.RECEIVING);
        invm.setOrgId(entity.getOrgId());

		// 库存校验
		checkInvMixSkuMixLot(entity);
		// 忽略ID
		entity = this.loseTraceId(entity);
		invm.setTraceId(entity.getTraceId());
		// 是否冻结
		entity = this.holdInventory(entity);
		invm.setQtyHold(entity.getQtyHold());

		invm.setQty(entity.getQtyEaOp());
		this.updateToInvLot(invm);
		invm = this.updateToInvLotLoc(invm);
		checkInvTraceidDiffLoc(entity);
		tranId = this.addActTranTo(invm);

		// 清除库存表垃圾记录
		this.cleanZeroInventory(invm);
		invm.setTranId(tranId);
		return invm;
	}

	/**
	 * 库存冻结
	 *
	 * @param entity
	 * @return BanQinInventoryEntity
	 */
	public BanQinInventoryEntity holdInventory(BanQinInventoryEntity entity) {
		List<BanQinWmHold> holdModels = new ArrayList<>();
		if (StringUtils.isNotEmpty(entity.getOwnerCode())) {
			BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setOwnerCode(entity.getOwnerCode());
			holdModel.setHoldType(WmsCodeMaster.HOLD_BY_OWNER.getCode());
            holdModel.setOrgId(entity.getOrgId());
			holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getSkuCode())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setSkuCode(entity.getSkuCode());
			holdModel.setHoldType(WmsCodeMaster.HOLD_BY_SKU.getCode());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getLotNum())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setLotNum(entity.getLotNum());
			holdModel.setOrgId(entity.getOrgId());
			holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getLocCode())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setLocCode(entity.getLocCode());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getTraceId())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setTraceId(entity.getTraceId());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (holdModels.size() > 0) {
			entity.setQtyHold(entity.getQtyEaOp());
			return entity;
		} else {
			return entity;
		}
	}

	/**
	 * 取消库存冻结
	 *
	 * @param entity
	 * @return BanQinInventoryEntity
	 */
	public BanQinInventoryEntity cancelHoldInventory(BanQinInventoryEntity entity) {
		List<BanQinWmHold> holdModels = new ArrayList<>();
		if (StringUtils.isNotEmpty(entity.getOwnerCode())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setOwnerCode(entity.getOwnerCode());
			holdModel.setHoldType(WmsCodeMaster.HOLD_BY_OWNER.getCode());
			holdModel.setOrgId(entity.getOrgId());
			holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getSkuCode())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setSkuCode(entity.getSkuCode());
			holdModel.setHoldType(WmsCodeMaster.HOLD_BY_SKU.getCode());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getLotNum())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setLotNum(entity.getLotNum());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getLocCode())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setLocCode(entity.getLocCode());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (StringUtils.isNotEmpty(entity.getTraceId())) {
            BanQinWmHold holdModel = new BanQinWmHold();
			holdModel.setTraceId(entity.getTraceId());
            holdModel.setOrgId(entity.getOrgId());
            holdModels.addAll(this.wmHoldService.findList(holdModel));
		}
		if (holdModels.size() > 0) {
			return entity;
		} else {
			entity.setQtyHold(entity.getQtyEaOp());
			return entity;
		}
	}

	/**
	 * 清除库存垃圾记录
	 *
	 * @param entity
	 */
	@Transactional
	public void cleanZeroInventory(BanQinInventoryEntity entity) {
		if (StringUtils.isNotEmpty(entity.getLotNum())) {
			BanQinWmInvLot invLotModel = new BanQinWmInvLot();
			invLotModel.setLotNum(entity.getLotNum());
			invLotModel.setOrgId(entity.getOrgId());
			invLotModel = this.wmInvLotService.findFirst(invLotModel);
			if (invLotModel != null) {
				if (invLotModel.getQty() == 0.0 && invLotModel.getQtyAlloc() == 0.0) {
					this.wmInvLotService.delete(invLotModel);
				}
			}
		}
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc invLotLocModel = new BanQinWmInvLotLoc();
			invLotLocModel.setLotNum(entity.getLotNum());
			invLotLocModel.setLocCode(entity.getLocCode());
			invLotLocModel.setTraceId(entity.getTraceId());
			invLotLocModel.setOrgId(entity.getOrgId());
			invLotLocModel = this.wmInvLotLocService.findFirst(invLotLocModel);
			if (invLotLocModel != null) {
				if (invLotLocModel.getQty() == 0.0 && invLotLocModel.getQtyAlloc() == 0.0 && invLotLocModel.getQtyMvIn() == 0.0
                        && invLotLocModel.getQtyPaIn() == 0.0 && invLotLocModel.getQtyRpIn() == 0.0) {
					this.wmInvLotLocService.delete(invLotLocModel);
				}
			}
		}

	}

	/**
	 * 忽略跟踪号traceId
	 * @param entity
	 * @return BanQinInventoryEntity
	 */
	public BanQinInventoryEntity loseTraceId(BanQinInventoryEntity entity) {
		String locCode = entity.getLocCode();
		if (StringUtils.isNotEmpty(locCode)) {
			BanQinCdWhLoc loc = new BanQinCdWhLoc();
			loc.setLocCode(locCode);
			loc.setOrgId(entity.getOrgId());
			List<BanQinCdWhLoc> locs = this.cdWhLocService.findList(loc);
			if (locs != null && locs.size() > 1) {
				throw new WarehouseException("库位错误");
			} else if (locs != null && locs.size() == 1) {
				loc = locs.get(0);
				String isLoseId = loc.getIsLoseId();
				if (WmsConstants.YES.equals(isLoseId)) {
					entity.setTraceId(WmsConstants.TRACE_ID);
					return entity;
				} else {
					return entity;
				}
			} else {
				throw new WarehouseException("库位编码不存在");
			}
		} else {
			// 库位编码不能为空
			throw new WarehouseException("库位编码不能为空");
		}
	}

	/**
	 * 获取库存批次号InvLotNum
	 *
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public synchronized String createInvLotNum(BanQinWmInvLotAtt model) {
        BanQinWmInvLotAtt invLotAttModel = new BanQinWmInvLotAtt();
		if (StringUtils.isNotEmpty(model.getOwnerCode()) && StringUtils.isNotEmpty(model.getSkuCode())) {
            List<BanQinWmInvLotAtt> lotNumItems = this.wmInvLotAttService.getInvLotNum(model);
            if (lotNumItems != null && lotNumItems.size() > 1) {
				// 库存批次属性错误
				throw new WarehouseException("库存批次属性错误");
			} else if (lotNumItems != null && lotNumItems.size() == 1) {
				return lotNumItems.get(0).getLotNum();
			} else {
                BeanUtils.copyProperties(model, invLotAttModel);
                invLotAttModel.setId(IdGen.uuid());
				invLotAttModel.setLotNum(noService.getDocumentNo(GenNoType.WM_LOT_NUM.name()));
				this.wmInvLotAttService.save(invLotAttModel);
				return invLotAttModel.getLotNum();
			}
		} else {
			// 货主和商品编码不能为空
			throw new WarehouseException("货主和商品编码不能为空");
		}
	}

	/**
	 * 字符串空转换
	 *
	 * @param value
	 * @param nvlvalue
	 * @return
	 */
	protected String nvl(String value, String nvlvalue) {
		return StringUtils.isNotEmpty(value) ? value : nvlvalue;
	}

	/**
	 * 库存校验：混商品混批次库位容量校验。
	 * @param entity
	 * @return
	 */
	public void checkInvMixSkuMixLot(BanQinInventoryEntity entity) {
		String locCode = entity.getLocCode();
		String skuCode = entity.getSkuCode();
		String lotNum = entity.getLotNum();
		String orgId = entity.getOrgId();
		if (WmsConstants.STAGE.equals(locCode) || WmsConstants.SORTATION.equals(locCode)) {
			return;
		}
		if (StringUtils.isNotEmpty(locCode)) {
			BanQinCdWhLoc loc = new BanQinCdWhLoc();
			loc.setLocCode(locCode);
			loc.setOrgId(orgId);
			List<BanQinCdWhLoc> locs = this.cdWhLocService.findList(loc);
			if (locs != null && locs.size() > 1) {
				// 库位错误
				throw new WarehouseException("库位错误");
			} else if (locs != null && locs.size() == 1) {
				loc = locs.get(0);
				String isMixSku = loc.getIsMixSku();
				Long maxMixSku = loc.getMaxMixSku() == null ? 0L : loc.getMaxMixSku();

				if (WmsConstants.NO.equals(isMixSku) || (WmsConstants.YES.equals(isMixSku) && maxMixSku != 0)) {
					Map<String, List<String>> map = this.wmInvLotLocService.getSkuQtyByLocCode(locCode, orgId);
					List<String> sku = map.get("SKU");
					if (WmsConstants.NO.equals(isMixSku) && sku.size() > 0 && !sku.contains(skuCode)) {
						// 库位不允许混放商品
						throw new WarehouseException("库位不允许混放商品");
					}
					int skuqty = sku.size();
					if (!sku.contains(skuCode)) {
						skuqty = skuqty + 1;
					}
					if (WmsConstants.YES.equals(isMixSku) && maxMixSku != null && maxMixSku > 0 && skuqty > maxMixSku) {
						// 库位超过最大的混放商品数
						throw new WarehouseException("库位超过最大的混放商品数");
					}
				}

				String isMixLot = loc.getIsMixLot();
				Long maxMixLot = loc.getMaxMixLot() == null ? 0L : loc.getMaxMixLot();

				if (WmsConstants.NO.equals(isMixLot) || (WmsConstants.YES.equals(isMixLot) && maxMixLot != 0)) {
					Map<String, List<String>> map = this.wmInvLotLocService.getLotNumQtyByLocCode(locCode, orgId);
					List<String> lot = map.get("LOT");
					if (WmsConstants.NO.equals(isMixLot) && lot.size() > 0 && !lot.contains(lotNum)) {
						// 库位不允许混放批次
						throw new WarehouseException("库位不允许混放批次");
					}
					int lotqty = lot.size();
					if (!lot.contains(lotNum)) {
						lotqty = lotqty + 1;
					}
					if (WmsConstants.YES.equals(isMixLot) && maxMixLot != null && maxMixLot > 0 && lotqty > maxMixLot) {
						// 库位超过最大的混放批次数
						throw new WarehouseException("库位超过最大的混放批次数");
					}
				}
			} else {
				// 库位编码不存在
				throw new WarehouseException("库位编码不存在");
			}
		} else {
			// 库位编码不能为空
			throw new WarehouseException("库位编码不能为空");
		}
	}

	/**
	 * 库存校验：库位托盘数，EA数，重量，体积限制
	 * @param entity
	 */
	public void checkInvLocLimit(BanQinInventoryEntity entity) {
		String locCode = entity.getLocCode();
		String orgId = entity.getOrgId();
		if (StringUtils.isNotEmpty(locCode)) {
			BanQinCdWhLoc loc = new BanQinCdWhLoc();
			loc.setLocCode(locCode);
			loc.setOrgId(entity.getOrgId());
			List<BanQinCdWhLoc> locs = this.cdWhLocService.findList(loc);
			if (locs != null && locs.size() > 1) {
				// 库位错误
				throw new WarehouseException("库位错误");
			} else if (locs != null && locs.size() == 1) {
				loc = locs.get(0);
				Double cubic = entity.getCubic();
				Double grossWeight = entity.getGrossWeight();
				Double palletQty = entity.getPalletQty();
				if (loc.getMaxCubic() != null && loc.getMaxCubic() > 0.0) {
					Double invCubic = this.wmInvLotLocService.getCubicByLocCode(locCode, orgId);
					if (cubic + invCubic > loc.getMaxCubic()) {
						// 库位体积限制
						throw new WarehouseException("库位体积限制");
					}
				}
				if (loc.getMaxWeight() != null && loc.getMaxWeight() > 0.0) {
					Double invGrossWeight = this.wmInvLotLocService.getGrossWeightByLocCode(locCode, orgId);
					if (grossWeight + invGrossWeight > loc.getMaxWeight()) {
						// 库位重量限制
						throw new WarehouseException("库位重量限制");
					}
				}
				if (loc.getMaxPl() != null && loc.getMaxPl() > 0.0) {
					String isLoseId = loc.getIsLoseId();
					Long invPl = 0l;
					if (WmsConstants.YES.equals(isLoseId)) {
						invPl = this.wmInvLotLocService.getPalletQty1ByLocCode(locCode, orgId);
					} else {
						invPl = this.wmInvLotLocService.getPalletQtyByLocCode(locCode, orgId);
					}
					if (invPl + palletQty > loc.getMaxPl()) {
						// 库位托盘限制
						throw new WarehouseException("库位托盘限制");
					}
				}
			} else {
				// 库位编码不存在
				throw new WarehouseException("库位编码不存在");
			}
		} else {
			// 库位编码不能为空
			throw new WarehouseException("库位编码不能为空");
		}
	}

	/**
	 * 库存校验：同一个跟踪号不允许出现在不同库位。
	 * @param entity
	 */
    public void checkInvTraceidDiffLoc(BanQinInventoryEntity entity) {
        String traceId = entity.getTraceId();
        String orgId = entity.getOrgId();
        if (!WmsConstants.TRACE_ID.equals(traceId) && StringUtils.isNotEmpty(traceId)) {
            List<BanQinWmInvLotLoc> lotLocModels = this.wmInvLotLocService.getByTraceId(traceId, orgId);
            if (lotLocModels.stream().filter(o -> o.getQty() > 0).map(BanQinWmInvLotLoc::getLocCode).distinct().count() > 1) {
                throw new WarehouseException("同一个跟踪号不允许出现在不同库位");
            }
        }
    }

	/**
	 * 可用数：
	 * @param entity
	 * @return Double
	 */
	public Double getAvailableQty(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyPk() - wmInvLotLocModel.getQtyMvOut()
						- wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (availableQty < 0.0) {
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		return availableQty;
	}

	/**
	 * 是否可调整校验：
	 * @param entity
	 * @return Double
	 */
	public BanQinInventoryEntity checkAdjustment(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
            BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyPk() - wmInvLotLocModel.getQtyMvOut()
						- wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (entity.getQtyHold() != null && entity.getQtyHold() != 0.0) {
				availableQty = availableQty + entity.getQtyHold();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		entity.setAvailableQty(availableQty);
		return entity;
	}

	/**
	 * 是否可移动校验：
	 * @param entity
	 * @return Double
	 */
	public BanQinInventoryEntity checkMoving(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (StringUtils.isNotEmpty(entity.getLotNum()) && StringUtils.isNotEmpty(entity.getLocCode()) && StringUtils.isNotEmpty(entity.getTraceId())) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyPk()
                        - wmInvLotLocModel.getQtyMvOut() - wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (entity.getQtyHold() != null && entity.getQtyHold() != 0.0) {
				availableQty = availableQty + entity.getQtyHold();
			}
			if (availableQty < 0.0) {
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		entity.setAvailableQty(availableQty);
		return entity;
	}

	/**
	 * 是否可打包校验：
	 * @param entity
	 * @return Double
	 */
	public BanQinInventoryEntity checkPacking(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyMvOut()
                        - wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (entity.getQtyHold() != null && entity.getQtyHold() != 0.0) {
				availableQty = availableQty + entity.getQtyHold();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		entity.setAvailableQty(availableQty);
		return entity;
	}

	/**
	 * 是否可装载移动：
	 * @param entity
	 * @return Double
	 */
	public BanQinInventoryEntity checkLoading(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyMvOut()
                        - wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (entity.getQtyHold() != null && entity.getQtyHold() != 0.0) {
				availableQty = availableQty + entity.getQtyHold();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		entity.setAvailableQty(availableQty);
		return entity;
	}

	/**
	 * 是否要进行混商品混批次校验
	 * @param fmEntity
	 */
	public boolean isCheckInvMixSkuMixLot(BanQinInventoryEntity fmEntity, BanQinInventoryEntity toEntity) {
		if (fmEntity.getLotNum() != null && fmEntity.getLocCode() != null && fmEntity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(fmEntity.getLotNum(), fmEntity.getLocCode(), fmEntity.getTraceId(), fmEntity.getOrgId());
			if (wmInvLotLocModel != null) {
				Double qty = wmInvLotLocModel.getQty();
				if (qty.doubleValue() == toEntity.getQtyEaOp() && fmEntity.getLocCode().equals(toEntity.getLocCode())) {
					return false;
				}
			}
		} else {
			throw new WarehouseException("批次号库位跟踪号都不能为空");
		}
		return true;
	}

	/**
	 * 是否可转移校验：
	 * @param entity
	 * @return Double
	 */
	public BanQinInventoryEntity checkTransfer(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyPk() - wmInvLotLocModel.getQtyMvOut()
						- wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (entity.getQtyHold() != null && entity.getQtyHold() != 0.0) {
				availableQty = availableQty + entity.getQtyHold();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		entity.setAvailableQty(availableQty);
		return entity;
	}

	/**
	 * 是否可加工转移校验：
	 * @param entity
	 * @return Double
	 */
	public BanQinInventoryEntity checkKitTransfer(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyMvOut() - wmInvLotLocModel.getQtyPaOut()
						- wmInvLotLocModel.getQtyRpOut();
			}
			if (entity.getQtyHold() != null && entity.getQtyHold() != 0.0) {
				availableQty = availableQty + entity.getQtyHold();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		entity.setAvailableQty(availableQty);
		return entity;
	}

	/**
	 * 可分配数：
	 * @param entity
	 * @return Double
	 */
	public Double getAvailableAllocQty(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (entity.getLotNum() != null && entity.getLocCode() != null && entity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			if (wmInvLotLocModel != null) {
				availableQty = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyHold()
                        - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyPk() - wmInvLotLocModel.getQtyMvOut()
						- wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum(), entity.getLocCode(), entity.getTraceId());
			}
		}
		return availableQty;
	}

	/**
	 * 可预配数：
	 * @param entity
	 * @return Double
	 */
	public Double getAvailablePreallocQty(BanQinInventoryEntity entity) {
		Double availableQty = 0.0;
		if (StringUtils.isNotEmpty(entity.getLotNum())) {
			BanQinWmInvLot wmInvLotModel = this.wmInvLotService.getByLotNum(entity.getLotNum(), entity.getOrgId());
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getInvQtyWaitOutByLotNum(entity.getLotNum(), entity.getOrgId());
			if (wmInvLotModel != null) {
				availableQty = wmInvLotModel.getQty() - wmInvLotModel.getQtyPrealloc()
                        - wmInvLotModel.getQtyHold() - wmInvLotModel.getQtyAlloc() - wmInvLotModel.getQtyPk()
						- wmInvLotLocModel.getQtyMvOut() - wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
			}
			if (availableQty < 0.0) {
				// 库存数量错误
				throw new WarehouseException("库存数量错误", entity.getLotNum());
			}
		}
		return availableQty;
	}

	/**
	 * 更新目标库存WM_INV_LOT记录,
	 * @param toEntity
	 */
	@Transactional
	public void updateToInvLot(BanQinInventoryEntity toEntity) {
		// 更新库存WM_INV_LOT，查询库存表WmInvLotModel，找到则更新，找不到则新增
		if (StringUtils.isNotEmpty(toEntity.getLotNum())) {
			BanQinWmInvLot wmInvLotModel = this.wmInvLotService.getByLotNum(toEntity.getLotNum(), toEntity.getOrgId());
			if (wmInvLotModel != null) {
				if (toEntity.getQty() != null && 0 < toEntity.getQty()) {
					wmInvLotModel.setQty(wmInvLotModel.getQty() + toEntity.getQty());
				}
				if (toEntity.getQtyAlloc() != null && 0 < toEntity.getQtyAlloc()) {
					wmInvLotModel.setQtyAlloc(wmInvLotModel.getQtyAlloc() + toEntity.getQtyAlloc());
				}
				if (toEntity.getQtyPrealloc() != null && 0 < toEntity.getQtyPrealloc()) {
					wmInvLotModel.setQtyPrealloc(wmInvLotModel.getQtyPrealloc() + toEntity.getQtyPrealloc());
				}
				if (toEntity.getQtyHold() != null && 0 < toEntity.getQtyHold()) {
					wmInvLotModel.setQtyHold(wmInvLotModel.getQtyHold() + toEntity.getQtyHold());
				}
				if (toEntity.getQtyPk() != null && 0 < toEntity.getQtyPk()) {
					wmInvLotModel.setQtyPk(wmInvLotModel.getQtyPk() + toEntity.getQtyPk());
				}
			} else {
				wmInvLotModel = new BanQinWmInvLot();
				wmInvLotModel.setLotNum(toEntity.getLotNum());
				wmInvLotModel.setOwnerCode(toEntity.getOwnerCode());
				wmInvLotModel.setSkuCode(toEntity.getSkuCode());
				wmInvLotModel.setOrgId(toEntity.getOrgId());
				if (toEntity.getQty() != null && 0 < toEntity.getQty()) {
					wmInvLotModel.setQty(toEntity.getQty());
				}
				if (toEntity.getQtyAlloc() != null && 0 < toEntity.getQtyAlloc()) {
					wmInvLotModel.setQtyAlloc(toEntity.getQtyAlloc());
				}
				if (toEntity.getQtyPrealloc() != null && 0 < toEntity.getQtyPrealloc()) {
					wmInvLotModel.setQtyPrealloc(toEntity.getQtyPrealloc());
				}
				if (toEntity.getQtyHold() != null && 0 < toEntity.getQtyHold()) {
					wmInvLotModel.setQtyHold(toEntity.getQtyHold());
				}
				if (toEntity.getQtyPk() != null && 0 < toEntity.getQtyPk()) {
					wmInvLotModel.setQtyPk(toEntity.getQtyPk());
				}
			}
			this.wmInvLotService.save(wmInvLotModel);
		} else {
			// 批次号不能为空
			throw new WarehouseException("批次号不能为空");
		}
	}

	/**
	 * 更新目标库存WM_INV_LOT_LOC记录
	 *
	 * @param toEntity
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public BanQinInventoryEntity updateToInvLotLoc(BanQinInventoryEntity toEntity) {
		// 更新库存WM_INV_LOT_LOC，查询库存表WmInvLotLocModel，找到则更新，找不到则新增
		if (toEntity.getLotNum() != null && toEntity.getLocCode() != null && toEntity.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(toEntity.getLotNum(), toEntity.getLocCode(), toEntity.getTraceId(), toEntity.getOrgId());
			if (wmInvLotLocModel != null) {
				if (toEntity.getQty() != null && 0 < toEntity.getQty()) {
					toEntity.setQtyEaBefore(wmInvLotLocModel.getQty());
					wmInvLotLocModel.setQty(wmInvLotLocModel.getQty() + toEntity.getQty());
				}
				if (toEntity.getQtyAlloc() != null && 0 < toEntity.getQtyAlloc()) {
					wmInvLotLocModel.setQtyAlloc(wmInvLotLocModel.getQtyAlloc() + toEntity.getQtyAlloc());
				}
				if (toEntity.getQtyPk() != null && 0 < toEntity.getQtyPk()) {
					wmInvLotLocModel.setQtyPk(wmInvLotLocModel.getQtyPk() + toEntity.getQtyPk());
				}
				if (toEntity.getQtyHold() != null && 0 < toEntity.getQtyHold()) {
					wmInvLotLocModel.setQtyHold(wmInvLotLocModel.getQtyHold() + toEntity.getQtyHold());
				}
				if (toEntity.getQtyPaOut() != null && 0 < toEntity.getQtyPaOut()) {
					wmInvLotLocModel.setQtyPaOut(wmInvLotLocModel.getQtyPaOut() + toEntity.getQtyPaOut());
				}
				if (toEntity.getQtyPaIn() != null && 0 < toEntity.getQtyPaIn()) {
					wmInvLotLocModel.setQtyPaIn(wmInvLotLocModel.getQtyPaIn() + toEntity.getQtyPaIn());
				}
				if (toEntity.getQtyMvOut() != null && 0 < toEntity.getQtyMvOut()) {
					wmInvLotLocModel.setQtyMvOut(wmInvLotLocModel.getQtyMvOut() + toEntity.getQtyMvOut());
				}
				if (toEntity.getQtyMvIn() != null && 0 < toEntity.getQtyMvIn()) {
					wmInvLotLocModel.setQtyMvIn(wmInvLotLocModel.getQtyMvIn() + toEntity.getQtyMvIn());
				}
				if (toEntity.getQtyRpOut() != null && 0 < toEntity.getQtyRpOut()) {
					wmInvLotLocModel.setQtyRpOut(wmInvLotLocModel.getQtyRpOut() + toEntity.getQtyRpOut());
				}
				if (toEntity.getQtyRpIn() != null && 0 < toEntity.getQtyRpIn()) {
					wmInvLotLocModel.setQtyRpIn(wmInvLotLocModel.getQtyRpIn() + toEntity.getQtyRpIn());
				}

			} else {
				wmInvLotLocModel = new BanQinWmInvLotLoc();
				wmInvLotLocModel.setLotNum(toEntity.getLotNum());
				wmInvLotLocModel.setLocCode(toEntity.getLocCode());
				wmInvLotLocModel.setTraceId(toEntity.getTraceId());
				wmInvLotLocModel.setOwnerCode(toEntity.getOwnerCode());
				wmInvLotLocModel.setSkuCode(toEntity.getSkuCode());
                wmInvLotLocModel.setOrgId(toEntity.getOrgId());
				if (toEntity.getQty() != null && 0 < toEntity.getQty()) {
					toEntity.setQtyEaBefore(0d);
					wmInvLotLocModel.setQty(toEntity.getQty());
				}
				if (toEntity.getQtyAlloc() != null && 0 < toEntity.getQtyAlloc()) {
					wmInvLotLocModel.setQtyAlloc(toEntity.getQtyAlloc());
				}
				if (toEntity.getQtyPk() != null && 0 < toEntity.getQtyPk()) {
					wmInvLotLocModel.setQtyPk(toEntity.getQtyPk());
				}
				if (toEntity.getQtyHold() != null && 0 < toEntity.getQtyHold()) {
					wmInvLotLocModel.setQtyHold(toEntity.getQtyHold());
				}
				if (toEntity.getQtyPaOut() != null && 0 < toEntity.getQtyPaOut()) {
					wmInvLotLocModel.setQtyPaOut(toEntity.getQtyPaOut());
				}
				if (toEntity.getQtyPaIn() != null && 0 < toEntity.getQtyPaIn()) {
					wmInvLotLocModel.setQtyPaIn(toEntity.getQtyPaIn());
				}
				if (toEntity.getQtyMvOut() != null && 0 < toEntity.getQtyMvOut()) {
					wmInvLotLocModel.setQtyMvOut(toEntity.getQtyMvOut());
				}
				if (toEntity.getQtyMvIn() != null && 0 < toEntity.getQtyMvIn()) {
					wmInvLotLocModel.setQtyMvIn(toEntity.getQtyMvIn());
				}
				if (toEntity.getQtyRpOut() != null && 0 < toEntity.getQtyRpOut()) {
					wmInvLotLocModel.setQtyRpOut(toEntity.getQtyRpOut());
				}
				if (toEntity.getQtyRpIn() != null && 0 < toEntity.getQtyRpIn()) {
					wmInvLotLocModel.setQtyRpIn(toEntity.getQtyRpIn());
				}
			}
			this.wmInvLotLocService.save(wmInvLotLocModel);
		} else {
			// 批次号库位跟踪号都不能为空
			throw new WarehouseException("批次号库位跟踪号都不能为空");
		}
		return toEntity;
	}

	/**
	 * 更新源库存WM_INV_LOT记录
	 * @param fmEntiy
	 */
	@Transactional
	public void updateFmInvLot(BanQinInventoryEntity fmEntiy) {
		// 更新库存WM_INV_LOT，查询库存表WmInvLotModel
		if (StringUtils.isNotEmpty(fmEntiy.getLotNum())) {
			BanQinWmInvLot wmInvLotModel = this.wmInvLotService.getByLotNum(fmEntiy.getLotNum(), fmEntiy.getOrgId());
			if (wmInvLotModel != null) {
				if (fmEntiy.getQty() != null && 0 < fmEntiy.getQty()) {
					if (fmEntiy.getQty() <= wmInvLotModel.getQty()) {
						wmInvLotModel.setQty(wmInvLotModel.getQty() - fmEntiy.getQty());
					} else {
						// 库存数量不足
						throw new WarehouseException("库存数量不足");
					}
				}
				if (fmEntiy.getQtyAlloc() != null && 0 < fmEntiy.getQtyAlloc()) {
					if (fmEntiy.getQtyAlloc() <= wmInvLotModel.getQtyAlloc()) {
						wmInvLotModel.setQtyAlloc(wmInvLotModel.getQtyAlloc() - fmEntiy.getQtyAlloc());
					} else {
						// 库存分配数量不足
						throw new WarehouseException("库存分配数量不足");
					}
				}
				if (fmEntiy.getQtyPrealloc() != null && 0 < fmEntiy.getQtyPrealloc()) {
					if (fmEntiy.getQtyPrealloc() <= wmInvLotModel.getQtyPrealloc()) {
						wmInvLotModel.setQtyPrealloc(wmInvLotModel.getQtyPrealloc() - fmEntiy.getQtyPrealloc());
					} else {
						// 库存预配数量不足
						throw new WarehouseException("库存预配数量不足");
					}
				}
				if (fmEntiy.getQtyHold() != null && 0 < fmEntiy.getQtyHold()) {
					if (fmEntiy.getQtyHold() <= wmInvLotModel.getQtyHold()) {
						wmInvLotModel.setQtyHold(wmInvLotModel.getQtyHold() - fmEntiy.getQtyHold());
					} else {
						// 库存冻结数量不足
						throw new WarehouseException("库存冻结数量不足");
					}
				}
				if (fmEntiy.getQtyPk() != null && 0 < fmEntiy.getQtyPk()) {
					if (fmEntiy.getQtyPk() <= wmInvLotModel.getQtyPk()) {
						wmInvLotModel.setQtyPk(wmInvLotModel.getQtyPk() - fmEntiy.getQtyPk());
					} else {
						// 拣货数量不足
						throw new WarehouseException("拣货数量不足");
					}
				}
				this.wmInvLotService.save(wmInvLotModel);
			} else {
				// 批次库存不存在
				throw new WarehouseException("批次库存不存在", fmEntiy.getLotNum());
			}
		} else {
			// 批次号不能为空
			throw new WarehouseException("批次号不能为空");
		}
	}

	/**
	 * 更新源库存WM_INV_LOT_LOC记录
	 * @param fmEntiy
	 * @return BanQinInventoryEntity
	 */
	@Transactional
	public BanQinInventoryEntity updateFmInvLotLoc(BanQinInventoryEntity fmEntiy) {
		// 更新库存WM_INV_LOT_LOC，查询库存表WmInvLotLocModel
		if (fmEntiy.getLotNum() != null && fmEntiy.getLocCode() != null && fmEntiy.getTraceId() != null) {
			BanQinWmInvLotLoc wmInvLotLocModel = this.wmInvLotLocService.getByLotNumAndLocationAndTraceId(fmEntiy.getLotNum(), fmEntiy.getLocCode(), fmEntiy.getTraceId(), fmEntiy.getOrgId());
			if (wmInvLotLocModel != null) {
				if (fmEntiy.getQty() != null && 0 < fmEntiy.getQty()) {
					if (fmEntiy.getQty() <= wmInvLotLocModel.getQty()) {
						fmEntiy.setQtyEaBefore(wmInvLotLocModel.getQty());
						wmInvLotLocModel.setQty(wmInvLotLocModel.getQty() - fmEntiy.getQty());
					} else {
						// 库存数量不足
						throw new WarehouseException("库存数量不足", fmEntiy.getLotNum(), fmEntiy.getLocCode(), fmEntiy.getTraceId());
					}
				}
				if (fmEntiy.getQtyAlloc() != null && 0 < fmEntiy.getQtyAlloc()) {
					if (fmEntiy.getQtyAlloc() <= wmInvLotLocModel.getQtyAlloc()) {
						wmInvLotLocModel.setQtyAlloc(wmInvLotLocModel.getQtyAlloc() - fmEntiy.getQtyAlloc());
					} else {
						// 库存分配数量不足
						throw new WarehouseException("库存分配数量不足");
					}
				}
				if (fmEntiy.getQtyPk() != null && 0 < fmEntiy.getQtyPk()) {
					if (fmEntiy.getQtyPk() <= wmInvLotLocModel.getQtyPk()) {
						wmInvLotLocModel.setQtyPk(wmInvLotLocModel.getQtyPk() - fmEntiy.getQtyPk());
					} else {
						// 库存拣货数量不足
						throw new WarehouseException("库存拣货数量不足");
					}
				}
				if (fmEntiy.getQtyHold() != null && 0 < fmEntiy.getQtyHold()) {
					if (fmEntiy.getQtyHold() <= wmInvLotLocModel.getQtyHold()) {
						wmInvLotLocModel.setQtyHold(wmInvLotLocModel.getQtyHold() - fmEntiy.getQtyHold());
					} else {
						// 库存冻结数量不足
						throw new WarehouseException("库存冻结数量不足");
					}
				}
				if (fmEntiy.getQtyPaOut() != null && 0 < fmEntiy.getQtyPaOut()) {
					if (fmEntiy.getQtyPaOut() <= wmInvLotLocModel.getQtyPaOut()) {
						wmInvLotLocModel.setQtyPaOut(wmInvLotLocModel.getQtyPaOut() - fmEntiy.getQtyPaOut());
					} else {
						// 库存上架待出数量不足
						throw new WarehouseException("库存上架待出数量不足");
					}
				}
				if (fmEntiy.getQtyPaIn() != null && 0 < fmEntiy.getQtyPaIn()) {
					if (fmEntiy.getQtyPaIn() <= wmInvLotLocModel.getQtyPaIn()) {
						wmInvLotLocModel.setQtyPaIn(wmInvLotLocModel.getQtyPaIn() - fmEntiy.getQtyPaIn());
					} else {
						// 库存上架待入数量不足
						throw new WarehouseException("库存上架待入数量不足");
					}
				}
				if (fmEntiy.getQtyMvOut() != null && 0 < fmEntiy.getQtyMvOut()) {
					if (fmEntiy.getQtyMvOut() <= wmInvLotLocModel.getQtyMvOut()) {
						wmInvLotLocModel.setQtyMvOut(wmInvLotLocModel.getQtyMvOut() - fmEntiy.getQtyMvOut());
					} else {
						// 库存移动待出数量不足
						throw new WarehouseException("库存移动待出数量不足");
					}
				}
				if (fmEntiy.getQtyMvIn() != null && 0 < fmEntiy.getQtyMvIn()) {
					if (fmEntiy.getQtyMvIn() <= wmInvLotLocModel.getQtyMvIn()) {
						wmInvLotLocModel.setQtyMvIn(wmInvLotLocModel.getQtyMvIn() - fmEntiy.getQtyMvIn());
					} else {
						// 库存移动待入数量不足
						throw new WarehouseException("库存移动待入数量不足");
					}
				}
				if (fmEntiy.getQtyRpOut() != null && 0 < fmEntiy.getQtyRpOut()) {
					if (fmEntiy.getQtyRpOut() <= wmInvLotLocModel.getQtyRpOut()) {
						wmInvLotLocModel.setQtyRpOut(wmInvLotLocModel.getQtyRpOut() - fmEntiy.getQtyRpOut());
					} else {
						// 库存补货待出数量不足
						throw new WarehouseException("库存补货待出数量不足");
					}
				}
				if (fmEntiy.getQtyRpIn() != null && 0 < fmEntiy.getQtyRpIn()) {
					if (fmEntiy.getQtyRpIn() <= wmInvLotLocModel.getQtyRpIn()) {
						wmInvLotLocModel.setQtyRpIn(wmInvLotLocModel.getQtyRpIn() - fmEntiy.getQtyRpIn());
					} else {
						// 库存补货待入数量不足
						throw new WarehouseException("库存补货待入数量不足");
					}
				}
				this.wmInvLotLocService.save(wmInvLotLocModel);
			} else {
				// 批次库位跟踪号库存不存在
				throw new WarehouseException("批次库位跟踪号库存不存在", fmEntiy.getLotNum(), fmEntiy.getLocCode(), fmEntiy.getTraceId());
			}
		} else {
			// 批次号库位跟踪号都不能为空
			throw new WarehouseException("批次号库位跟踪号都不能为空");
		}
		return fmEntiy;
	}

	/**
	 * 新增库存交易日志记录
	 * @param fmEntity
	 * @param toEntity
	 */
	@Transactional
	public String addActTran(BanQinInventoryEntity fmEntity, BanQinInventoryEntity toEntity) {
		BanQinWmActTran model = new BanQinWmActTran();
		model.setTranId(noService.getDocumentNo(GenNoType.WM_TRAN_ID.name()));
		model.setTranType(this.getTranType(toEntity.getAction()));
		model.setOrderNo(fmEntity.getOrderNo());
		if (StringUtils.isNotEmpty(fmEntity.getOrderNo())) {
			model.setOrderType(this.getOrderType(toEntity.getAction()));
		} else {
			model.setOrderType(null);
		}
		model.setLineNo(fmEntity.getLineNo());
		model.setTranOp(UserUtils.getUser().getName());
		model.setTranTime(new Date());
		model.setFmOwner(fmEntity.getOwnerCode());
		model.setFmSku(fmEntity.getSkuCode());
		model.setFmLot(fmEntity.getLotNum());
		model.setFmLoc(fmEntity.getLocCode());
		model.setFmId(fmEntity.getTraceId());
		model.setFmPack(fmEntity.getPackCode());
		model.setFmUom(fmEntity.getUom());
		model.setFmQtyUomOp(fmEntity.getQtyUom());
		model.setFmQtyEaOp(fmEntity.getQty());
		model.setFmQtyEaBefore(fmEntity.getQtyEaBefore());
		model.setFmQtyEaAfter(fmEntity.getQtyEaBefore() - fmEntity.getQty());
		model.setToOwner(toEntity.getOwnerCode());
		model.setToSku(toEntity.getSkuCode());
		model.setToLot(toEntity.getLotNum());
		model.setToLoc(toEntity.getLocCode());
		model.setToId(toEntity.getTraceId());
		model.setToPack(toEntity.getPackCode());
		model.setToUom(toEntity.getUom());
		model.setToQtyUomOp(toEntity.getQtyUom());
		model.setToQtyEaOp(toEntity.getQty());
		model.setToQtyEaBefore(toEntity.getQtyEaBefore());
		model.setToQtyEaAfter(toEntity.getQtyEaBefore() + toEntity.getQty());
		model.setTaskId(toEntity.getTaskId());
		model.setTaskLineNo(toEntity.getTaskLineNo());
        model.setOrgId(toEntity.getOrgId());
		this.wmActTranService.save(model);
		return model.getTranId();
	}

	/**
	 * 新增库存交易日志记录
	 * @param toEntity
	 */
	@Transactional
	public String addActTranTo(BanQinInventoryEntity toEntity) {
		BanQinWmActTran model = new BanQinWmActTran();
		model.setTranId(noService.getDocumentNo(GenNoType.WM_TRAN_ID.name()));
		model.setTranType(this.getTranType(toEntity.getAction()));
		model.setOrderNo(toEntity.getOrderNo());
		if (StringUtils.isNotEmpty(toEntity.getOrderNo())) {
			model.setOrderType(this.getOrderType(toEntity.getAction()));
		} else {
			model.setOrderType(null);
		}
		model.setLineNo(toEntity.getLineNo());
		model.setTranOp(UserUtils.getUser().getName());
		model.setTranTime(new Date());
		model.setFmOwner(null);
		model.setFmSku(null);
		model.setFmLot(null);
		model.setFmLoc(null);
		model.setFmId(null);
		model.setFmPack(null);
		model.setFmUom(null);
		model.setFmQtyUomOp(null);
		model.setFmQtyEaOp(null);
		model.setFmQtyEaBefore(null);
		model.setFmQtyEaAfter(null);
		model.setToOwner(toEntity.getOwnerCode());
		model.setToSku(toEntity.getSkuCode());
		model.setToLot(toEntity.getLotNum());
		model.setToLoc(toEntity.getLocCode());
		model.setToId(toEntity.getTraceId());
		model.setToPack(toEntity.getPackCode());
		model.setToUom(toEntity.getUom());
		model.setToQtyUomOp(toEntity.getQtyUom());
		model.setToQtyEaOp(toEntity.getQty());
		model.setToQtyEaBefore(toEntity.getQtyEaBefore());
		model.setToQtyEaAfter(null == toEntity.getQtyEaBefore() ? 0d : toEntity.getQtyEaBefore() + toEntity.getQty());
		model.setTaskId(toEntity.getTaskId());
		model.setTaskLineNo(toEntity.getTaskLineNo());
        model.setOrgId(toEntity.getOrgId());
		this.wmActTranService.save(model);
		return model.getTranId();
	}

	/**
	 * 新增库存交易日志记录
	 * @param fmEntity
	 */
	@Transactional
	public String addActTranFm(BanQinInventoryEntity fmEntity) {
        BanQinWmActTran model = new BanQinWmActTran();
		model.setTranId(noService.getDocumentNo(GenNoType.WM_TRAN_ID.name()));
		model.setTranType(this.getTranType(fmEntity.getAction()));
		model.setOrderNo(fmEntity.getOrderNo());
		if (StringUtils.isNotEmpty(fmEntity.getOrderNo())) {
			model.setOrderType(this.getOrderType(fmEntity.getAction()));
		} else {
			model.setOrderType(null);
		}
		model.setLineNo(fmEntity.getLineNo());
		model.setTranOp(UserUtils.getUser().getName());
		model.setTranTime(new Date());
		model.setFmOwner(fmEntity.getOwnerCode());
		model.setFmSku(fmEntity.getSkuCode());
		model.setFmLot(fmEntity.getLotNum());
		model.setFmLoc(fmEntity.getLocCode());
		model.setFmId(fmEntity.getTraceId());
		model.setFmPack(fmEntity.getPackCode());
		model.setFmUom(fmEntity.getUom());
		model.setFmQtyUomOp(fmEntity.getQtyUom());
		model.setFmQtyEaOp(fmEntity.getQty());
		model.setFmQtyEaBefore(fmEntity.getQtyEaBefore());
		model.setFmQtyEaAfter(fmEntity.getQtyEaBefore() - fmEntity.getQty());
		model.setToOwner(null);
		model.setToSku(null);
		model.setToLot(null);
		model.setToLoc(null);
		model.setToId(null);
		model.setToPack(null);
		model.setToUom(null);
		model.setToQtyUomOp(null);
		model.setToQtyEaOp(null);
		model.setToQtyEaBefore(null);
		model.setToQtyEaAfter(null);
		model.setTaskId(fmEntity.getTaskId());
		model.setTaskLineNo(fmEntity.getTaskLineNo());
        model.setOrgId(fmEntity.getOrgId());
		this.wmActTranService.save(model);
		return model.getTranId();
	}

	/**
	 * 根据操作，获取库存交易日志类型
	 *
	 * @param actionCode
	 * @return
	 */
	public String getTranType(ActionCode actionCode) {
		String tranType = null;
		if (ActionCode.RECEIVING == actionCode || ActionCode.ONESTEP_PLAN_RECEIVING == actionCode || ActionCode.PLAN_RECEIVING == actionCode) {
			return WmsCodeMaster.TRAN_RCV.getCode();
		}
		if (ActionCode.CANCEL_RECEIVING == actionCode) {
			return WmsCodeMaster.TRAN_CRCV.getCode();
		}
		if (ActionCode.PUTAWAY == actionCode) {
			return WmsCodeMaster.TRAN_PA.getCode();
		}
		if (ActionCode.PICKING == actionCode) {
			return WmsCodeMaster.TRAN_PK.getCode();
		}
		if (ActionCode.CANCEL_PICKING == actionCode) {
			return WmsCodeMaster.TRAN_CPK.getCode();
		}
		if (ActionCode.SHIPMENT == actionCode) {
			return WmsCodeMaster.TRAN_SP.getCode();
		}
		if (ActionCode.CANCEL_SHIPMENT == actionCode) {
			return WmsCodeMaster.TRAN_CSP.getCode();
		}
		if (ActionCode.REPLENISHMENT == actionCode) {
			return WmsCodeMaster.TRAN_RP.getCode();
		}
		if (ActionCode.MOVING == actionCode) {
			return WmsCodeMaster.TRAN_MV.getCode();
		}
		if (ActionCode.ADJUSTMENT_LOSS == actionCode) {
			return WmsCodeMaster.TRAN_AD.getCode();
		}
		if (ActionCode.ADJUSTMENT_PROFIT == actionCode) {
			return WmsCodeMaster.TRAN_AD.getCode();
		}
		if (ActionCode.TRANSFER == actionCode) {
			return WmsCodeMaster.TRAN_TF.getCode();
		}
		if (ActionCode.PACKING == actionCode) {
			return WmsCodeMaster.TRAN_PACK.getCode();
		}
		if (ActionCode.LOADING == actionCode) {
			return WmsCodeMaster.TRAN_LD.getCode();
		}
		if (ActionCode.QC == actionCode) {
			return WmsCodeMaster.TRAN_QTF.getCode();
		}
		if (ActionCode.KIN == actionCode) {
			return WmsCodeMaster.TRAN_KIN.getCode();
		}
		if (ActionCode.CANCEL_KIN == actionCode) {
			return WmsCodeMaster.TRAN_CKIN.getCode();
		}
		if (ActionCode.KOUT == actionCode) {
			return WmsCodeMaster.TRAN_KOUT.getCode();
		}
		if (ActionCode.CANCEL_KOUT == actionCode) {
			return WmsCodeMaster.TRAN_CKOUT.getCode();
		}
		if (ActionCode.KTF == actionCode) {
			return WmsCodeMaster.TRAN_KTF.getCode();
		}
		if (ActionCode.CANCEL_KTF == actionCode) {
			return WmsCodeMaster.TRAN_CKTF.getCode();
		}
		return tranType;
	}

	/**
	 * 根据操作，获取单据类型
	 *
	 * @param actionCode
	 * @return String
	 */
	public String getOrderType(ActionCode actionCode) {
		String orderType = null;
		if (ActionCode.RECEIVING == actionCode || ActionCode.CANCEL_RECEIVING == actionCode || ActionCode.PUTAWAY == actionCode || ActionCode.ONESTEP_PLAN_RECEIVING == actionCode
				|| ActionCode.PLAN_RECEIVING == actionCode) {
			return WmsCodeMaster.ORDER_ASN.getCode();
		}
		if (ActionCode.PICKING == actionCode || ActionCode.CANCEL_PICKING == actionCode || ActionCode.SHIPMENT == actionCode || ActionCode.CANCEL_SHIPMENT == actionCode) {
			return WmsCodeMaster.ORDER_SO.getCode();
		}
		if (ActionCode.ADJUSTMENT_PROFIT == actionCode) {
			return WmsCodeMaster.ORDER_AD.getCode();
		}
		if (ActionCode.ADJUSTMENT_LOSS == actionCode) {
			return WmsCodeMaster.ORDER_AD.getCode();
		}
		if (ActionCode.TRANSFER == actionCode) {
			return WmsCodeMaster.ORDER_TF.getCode();
		}
		if (ActionCode.QC == actionCode) {
			return WmsCodeMaster.ORDER_QC.getCode();
		}
		if (ActionCode.KIN == actionCode || ActionCode.CANCEL_KIN == actionCode || ActionCode.KOUT == actionCode || ActionCode.CANCEL_KOUT == actionCode || ActionCode.KTF == actionCode
				|| ActionCode.CANCEL_KTF == actionCode) {
			return WmsCodeMaster.ORDER_KIT.getCode();
		}
		return orderType;
	}
}
