package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取消拣货 
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCancelPickingService {
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 出库公共
	@Autowired
	protected BanQinOutboundCommonService outboundCommon;
	// 取消预配记录
	@Autowired
	protected BanQinWmDelPreallocService wmDelPreallocService;
	// 预配明细
	@Autowired
	protected BanQinWmSoPreallocService wmSoPreallocService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 取消分配拣货记录表
	@Autowired
	protected BanQinWmDelAllocService wmDelAllocService;
	// 出库序列号
	@Autowired
	protected BanQinWmSoSerialService wmSoSerialService;
	// 装车单明细
	@Autowired
	protected BanQinWmLdDetailService wmLdDetailService;
	// 库存更新
	@Autowired
	protected BanQinInventoryService inventoryService;
	// 取消拣货 生成上架任务
	@Autowired
	protected BanQinOutboundCreateTaskPaService outboundCreateTaskPaService;
	@Autowired
	private BanQinWmSoHeaderService wmSoHeaderService;

    /**
     * 取消拣货
     * @param wmSoAllocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public ResultMessage outboundCancelPicking(BanQinWmSoAllocEntity wmSoAllocEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();

		BanQinWmSoAlloc wmSoAllocModel = wmSoAllocService.getByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
		// 1、校验状态,是否是完全拣货状态60
		if (!wmSoAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
			// 分配拣货明细不是完全拣货状态
			throw new WarehouseException("分配拣货明细不是完全拣货状态", wmSoAllocEntity.getAllocId());
		}
		// 已经生成装车单的订单拣货明细不允许取消拣货
		if (this.wmLdDetailService.CheckIsGeneratorLdByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId())) {
			// "分配拣货记录"+wmSoAllocEntity.getAllocId()+"已经生成装车单，不能操作"
			throw new WarehouseException("分配拣货记录" + wmSoAllocEntity.getAllocId() + "已经生成装车单，不能操作");
		}
		// 3、写入取消分配拣货记录表，状态完全拣货60
		this.wmDelAllocService.saveDelAlloc(wmSoAllocEntity);
		// 删除出库序列号
		this.wmSoSerialService.removeByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
		// 4、删除分配明细
		this.wmSoAllocService.delete(wmSoAllocModel);
		// 5、如果分配明细有预配明细，那么校验存在的预配明细是否为0，是，则删除
		if (StringUtils.isNotEmpty(wmSoAllocModel.getPreallocId())) {
			// 查询预配明细是否存在被分配(即分配明细与预配明细关联)
            BanQinWmSoAlloc condition = new BanQinWmSoAlloc();
            condition.setPreallocId(wmSoAllocEntity.getPreallocId());
            condition.setOrgId(wmSoAllocEntity.getOrgId());
			Integer count = wmSoAllocService.findList(condition).size();
			// 如果=0，那么删除预配明细
			if (count == 0) {
				BanQinWmSoPrealloc wmSoPreallocModel = wmSoPreallocService.getByPreallocId(wmSoAllocModel.getPreallocId(), wmSoAllocModel.getOrgId());
				if (wmSoPreallocModel.getQtyPreallocEa() == 0D) {
					// 删除预配明细表
					this.wmSoPreallocService.delete(wmSoPreallocModel);
				}
			}
		}
		// 6、更新库存、交易(操作数量=实际拣货数量)
		BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
		invEntity.setAction(ActionCode.CANCEL_PICKING);// ACTION_CODE 取消拣货
		invEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());// 货主
		invEntity.setSkuCode(wmSoAllocEntity.getSkuCode());// 商品
		invEntity.setLotNum(wmSoAllocEntity.getLotNum());// 批次号
		invEntity.setLocCode(wmSoAllocEntity.getToLoc());// 拣货库位
		invEntity.setTraceId(wmSoAllocEntity.getToId());// 目标拣货位跟踪号
		invEntity.setQtyEaOp(wmSoAllocEntity.getQtyEa());// 取消拣货数量 = 分配数量
		invEntity.setOrderNo(wmSoAllocEntity.getSoNo());// 出库单号
		invEntity.setLineNo(wmSoAllocEntity.getLineNo());// 出库单行号
		invEntity.setTaskId(wmSoAllocEntity.getAllocId());// 任务号 = 分配号
		invEntity.setPackCode(wmSoAllocEntity.getPackCode());// 包装代码
		invEntity.setUom(wmSoAllocEntity.getUom());// 包装单位
		invEntity.setQtyUom(wmSoAllocEntity.getQtyUom());// 取消拣货数量 = 分配数量UOM
        invEntity.setOrgId(wmSoAllocEntity.getOrgId());
		inventoryService.updateInventory(invEntity);
		// 7、更新出库单明细、波次单明细、出库单状态、波次单状态
		outboundCommon.updateOrder(ActionCode.CANCEL_PICKING, wmSoAllocEntity.getQtyEa(), wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getLineNo(), wmSoAllocEntity.getWaveNo(), wmSoAllocEntity.getOrgId());
		// 是否生成上架任务
		if (StringUtils.isNotEmpty(wmSoAllocEntity.getIsTaskPa()) && wmSoAllocEntity.getIsTaskPa().equals(WmsConstants.YES)) {
			// 生成上架任务
			outboundCreateTaskPaService.outboundCreateTaskPaByAlloc(wmSoAllocEntity);
		}
		// 更新出库单头的打包状态
		BanQinWmSoEntity wmSoEntity = wmSoHeaderService.findEntityBySoNo(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
		outboundCommon.updatePackStatus(wmSoEntity);

		return msg;
	}

}