package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.service.BanQinWmCarrierTypeRelationService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.yunyou.modules.wms.report.entity.WmSoSkuLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 波次单操作类
 * @author WMJ
 * @version 2019/02/26
 */
@Service
public class BanQinOutboundWaveService {
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 波次单
	@Autowired
	protected BanQinWmWvHeaderService wmWvHeaderService;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	// 出库单明细
	@Autowired
	protected BanQinWmSoDetailService wmSoDetailService;
	// 预配明细
	@Autowired
	protected BanQinWmSoPreallocService wmSoPreallocService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 取消分配
	@Autowired
	protected BanQinWmDelAllocService wmDelAllocService;
	// 取消预配
	@Autowired
	protected BanQinWmDelPreallocService wmDelPreallocService;
	// 删除
	@Autowired
	protected BanQinOutboundRemoveService outboundRemoveService;
	// 预配
	@Autowired
	protected BanQinOutboundBatchPreallocAction outboundBatchPreallocAction;
	// 两步分配
	@Autowired
	protected BanQinOutboundBatchAllocTwoStepAction outboundBatchAllocTwoStepAction;
	// 一步分配
	@Autowired
	protected BanQinOutboundBatchAllocOneStepAction outboundBatchAllocOneStepAction;
	// 拣货
	@Autowired
	protected BanQinOutboundBatchPickingAction outboundBatchPickingAction;
	// 发货
	@Autowired
	protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;
	// 取消预配
	@Autowired
	protected BanQinOutboundBatchCancelPreallocAction outboundBatchCancelPreallocAction;
	// 取消分配
	@Autowired
	protected BanQinOutboundBatchCancelAllocAction outboundBatchCancelAllocAction;
	// 取消拣货
	@Autowired
	protected BanQinOutboundBatchCancelPickingAction outboundBatchCancelPickingAction;
	// 手工预配
	@Autowired
	protected BanQinOutboundManualPreallocService outboundManualPreallocService;
	// 手工分配
	@Autowired
	protected BanQinOutboundManualAllocService outboundManualAllocService;
	// 分派拣货
	@Autowired
	protected BanQinOutboundDispathPkTaskService outboundDispathPkTask;
	// 取消发货
	@Autowired
	protected BanQinOutboundBatchCancelShipmentAction outboundBatchCancelShipmentAction;
	@Autowired
	private BanQinWmCarrierTypeRelationService banQinWmCarrierTypeRelationService;
	@Autowired
	private BanQinOutboundPackService outboundPackService;
	@Autowired
	private BanQinWmGetWayBillLogService wmGetWayBillLogService;
	@Autowired
	private BanQinOutboundTrackingService outboundTrackingService;

    /**
     * 根据波次单号获取波次计划信息 包括波次单、波次单明细等
     * @param waveNo
     * @return
     */
	public BanQinWmWvEntity getWvEntityByWaveNo(String waveNo, String orgId) {
		// 波次单
        BanQinWmWvEntity wvEntity = wmWvHeaderService.findEntityByWaveNo(waveNo, orgId);
		// 发运订单
		List<BanQinWmSoEntity> soHeaderList = wmSoHeaderService.findEntityByWaveNo(waveNo, orgId);
		// 波次单明细
		List<BanQinWmSoDetailEntity> detailList = wmSoDetailService.findEntityByWaveNo(waveNo, orgId);
		// 预配明细
		List<BanQinWmSoPreallocEntity> preallocList = wmSoPreallocService.getEntityByWaveNo(waveNo, orgId);
		// 分配明细
		List<BanQinWmSoAllocEntity> allocList = wmSoAllocService.getEntityByWaveNo(waveNo, orgId);
		// 设置
		wvEntity.setSoHeaderList(soHeaderList);
		wvEntity.setDetailList(detailList);
		wvEntity.setPreallocList(preallocList);
		wvEntity.setAllocList(allocList);

		return wvEntity;
	}

    /**
     * 保存波次信息
     * @param wvEntity
     * @return
     */
	public ResultMessage saveWvEntity(BanQinWmWvHeaderEntity wvEntity) {
		ResultMessage msg = new ResultMessage();
		BanQinWmWvHeader banQinWmWvHeader = new BanQinWmWvHeader();
		try {
			banQinWmWvHeader = wmWvHeaderService.saveWvHeader(wvEntity);
		} catch (WarehouseException e) {
			msg.addMessage(e.getMessage());
			msg.setSuccess(false);
		}
		msg.addMessage("操作成功");
		msg.setSuccess(true);
		msg.setData(banQinWmWvHeader);
		return msg;
	}

    /**
     * 删除波次单
     * @param waveNos
     * @return
     */
	public ResultMessage removeWvEntity(String[] waveNos, String orgId) {
		ResultMessage msg = new ResultMessage();
		// 循环出库单号
		for (String waveNo : waveNos) {
			try {
				outboundRemoveService.removeWvEntity(waveNo, orgId);
			} catch (WarehouseException e) {
				msg.addMessage(e.getMessage());
			}
		}
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.setSuccess(true);
			msg.addMessage("操作成功");
		}
		return msg;

	}

    /**
     * 波次 预配 - 按波次号
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage preallocByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchPreallocAction.outboundBatchPrealloc(processByCode, noList, orgId);
	}

    /**
     * 波次 分配 - 按波次号
     * @param processByCode
     * @param noList
     * @return
     */
    public ResultMessage allocByWave(String processByCode, List<String> noList, String orgId) {
        // 两步分配控制参数
        String twoStep = WmsConstants.NO;
        if (WmsConstants.YES.equals(twoStep)) {
            // 执行两步分配
            return outboundBatchAllocTwoStepAction.outboundBatchAllocTwoStep(processByCode, noList, orgId);
        } else {
            // 执行一步分配
            return outboundBatchAllocOneStepAction.outboundBatchAllocOneStep(processByCode, noList, orgId);
        }
    }

    /**
     * 分派拣货
     * @param waveNos
     * @param entity
     * @return
     */
	public ResultMessage dispatchPicking(List<String> waveNos, BanQinDispatchPkRuleEntity entity, String orgId) {
		return outboundDispathPkTask.outboundDispathPkTask(waveNos, entity, orgId);
	}

    /**
     * 波次 拣货 - 按波次号
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage pickingByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchPickingAction.outboundBatchPicking(processByCode, noList, orgId);
	}

    /**
     * 发运确认- 按波次号
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage shipmentByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchShipmentAction.outboundBatchShipment(processByCode, noList, WmsConstants.YES, orgId);
	}

    /**
     * 取消预配-按波次号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPreallocByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
	}

    /**
     * 取消分配-按波次号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelAllocByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(processByCode, noList, orgId);
	}

    /**
     * 取消拣货 - 按波次号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPickingByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPickingAction.outboundBatchCancelPicking(processByCode, noList, orgId);
	}

    /**
     * 取消发货 - 按波次号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelShipmentByWave(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelShipmentAction.outboundBatchCancelShipment(processByCode, noList, orgId);
	}

    /**
     * 波次 预配 - 按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage preallocBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchPreallocAction.outboundBatchPrealloc(processByCode, noList, orgId);
	}

    /**
     * 波次 分配 - 按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
    public ResultMessage allocBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
        // 两步分配控制参数
        String twoStep = WmsConstants.NO;
        if (twoStep.equals(WmsConstants.YES)) {
            // 执行两步分配
            return outboundBatchAllocTwoStepAction.outboundBatchAllocTwoStep(processByCode, noList, orgId);
        } else {
            // 执行一步分配
            return outboundBatchAllocOneStepAction.outboundBatchAllocOneStep(processByCode, noList, orgId);
        }
    }

    /**
     * 拣货确认- 按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage pickingBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchPickingAction.outboundBatchPicking(processByCode, noList, orgId);
	}

    /**
     * 发运确认- 按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage shipmentBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchShipmentAction.outboundBatchShipment(processByCode, noList, WmsConstants.YES, orgId);
	}

    /**
     * 取消预配-按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPreallocBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
	}

    /**
     * 取消分配-按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelAllocBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(processByCode, noList, orgId);
	}

    /**
     * 取消拣货-按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPickingBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPickingAction.outboundBatchCancelPicking(processByCode, noList, orgId);
	}

    /**
     * 取消发货-按发运订单号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelShipmentBySo(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelShipmentAction.outboundBatchCancelShipment(processByCode, noList, orgId);
	}

    /**
     * 波次 预配 - 按发运订单号+行号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage preallocBySoLine(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchPreallocAction.outboundBatchPrealloc(processByCode, noList, orgId);
	}

    /**
     * 波次 分配 - 按发运订单号+行号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
    public ResultMessage allocBySoLine(String waveNo, String processByCode, List<String> noList, String orgId) {
        // 两步分配控制参数
        String twoStep = WmsConstants.NO;
        if (WmsConstants.YES.equals(twoStep)) {
            // 执行两步分配
            return outboundBatchAllocTwoStepAction.outboundBatchAllocTwoStep(processByCode, noList, orgId);
        } else {
            // 执行一步分配
            return outboundBatchAllocOneStepAction.outboundBatchAllocOneStep(processByCode, noList, orgId);
        }
    }

    /**
     * 取消预配-按发运订单号+行号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPreallocBySoLine(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
	}

    /**
     * 取消分配-按发运订单号+行号
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelAllocBySoLine(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(processByCode, noList, orgId);
	}

    /**
     * 预配明细 操作后获取信息 渲染前台
     * @param entity
     * @return
     */
	protected BanQinWmWvEntity getWvEntityByPrealloc(BanQinWmSoPreallocEntity entity) {
		// 波次单
		BanQinWmWvEntity wvEntity = wmWvHeaderService.findEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 发运订单
		List<BanQinWmSoEntity> soHeaderList = wmSoHeaderService.findEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 波次单明细
		List<BanQinWmSoDetailEntity> detailList = wmSoDetailService.findEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 预配明细
		List<BanQinWmSoPreallocEntity> preallocList = wmSoPreallocService.getEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 分配明细
		List<BanQinWmSoAllocEntity> allocList = wmSoAllocService.getEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 设置
		wvEntity.setSoHeaderList(soHeaderList);
		wvEntity.setDetailList(detailList);
		wvEntity.setPreallocList(preallocList);
		// 预配当前行
		wvEntity.setPreallocEntity(entity);
		wvEntity.setAllocList(allocList);

		return wvEntity;
	}

    /**
     * 手工预配
     * @param entity
     * @return
     */
	public ResultMessage manualPrealloc(BanQinWmSoPreallocEntity entity) {
		ResultMessage msg = new ResultMessage();
		try {
			entity = outboundManualPreallocService.outboundManualPrealloc(entity);
		} catch (WarehouseException e) {
			msg.setSuccess(false);
			msg.addMessage(e.getMessage());
			return msg;
		}
		msg.setSuccess(true);
		msg.addMessage("操作成功");
		return msg;
	}

    /**
     * 取消预配
     * @param waveNo
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPreallocByPrelloc(String waveNo, String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
	}

    /**
     * 分配明细 操作后获取信息 渲染前台
     * @param entity
     * @return
     */
	protected BanQinWmWvEntity getWvEntityByAlloc(BanQinWmSoAllocEntity entity) {
		// 波次单
        BanQinWmWvEntity wvEntity = wmWvHeaderService.findEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 发运订单
		List<BanQinWmSoEntity> soHeaderList = wmSoHeaderService.findEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 波次单明细
		List<BanQinWmSoDetailEntity> detailList = wmSoDetailService.findEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 预配明细
		List<BanQinWmSoPreallocEntity> preallocList = wmSoPreallocService.getEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 分配明细
		List<BanQinWmSoAllocEntity> allocList = wmSoAllocService.getEntityByWaveNo(entity.getWaveNo(), entity.getOrgId());
		// 分配明细entity
		BanQinWmSoAllocEntity allocEntity = wmSoAllocService.getEntityByAllocId(entity.getAllocId(), entity.getOrgId());
		// 设置
		wvEntity.setSoHeaderList(soHeaderList);
		wvEntity.setDetailList(detailList);
		wvEntity.setPreallocList(preallocList);
		// 分配当前行
		wvEntity.setAllocEntity(allocEntity);
		wvEntity.setAllocList(allocList);

		return wvEntity;
	}

    /**
     * 手工分配
     * @param entity
     * @return
     */
	public ResultMessage manualAlloc(BanQinWmSoAllocEntity entity) {
		ResultMessage msg = new ResultMessage();
		try {
			outboundManualAllocService.outboundManualAlloc(entity);
		} catch (WarehouseException e) {
			msg.setSuccess(false);
			msg.addMessage(e.getMessage());
			return msg;
		}

		msg.setSuccess(true);
		msg.addMessage("操作成功");
		return msg;
	}

    /**
     * 拣货确认 -- 分配明细
     * @param entityList
     * @return
     */
	public ResultMessage pickingByAlloc(List<BanQinWmSoAllocEntity> entityList) {
		return outboundBatchPickingAction.outboundBatchPicking(entityList);
	}

    /**
     * 发运确认 -- 分配明细
     * @param entityList
     * @return
     */
	public ResultMessage shipmentByAlloc(List<BanQinWmSoAllocEntity> entityList) {
		return outboundBatchShipmentAction.outboundBatchShipment(entityList);
	}

    /**
     * 取消分配 -- 分配明细
     * @param entityList
     * @return
     */
	public ResultMessage cancelAllocByAlloc(List<BanQinWmSoAllocEntity> entityList) {
		return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(entityList);
	}

    /**
     * 取消拣货 -- 分配明细
     * @param entityList
     * @return
     */
	public ResultMessage cancelPickingByAlloc(List<BanQinWmSoAllocEntity> entityList) {
		return outboundBatchCancelPickingAction.outboundBatchCancelPicking(entityList);
	}

    /**
     * 取消发货 -- 分配明细
     * @param entityList
     * @return
     */
	public ResultMessage cancelShipmentByAlloc(List<BanQinWmSoAllocEntity> entityList) {
		return outboundBatchCancelShipmentAction.outboundBatchCancelShipment(entityList);
	}

    /**
     * 根据波次号获取取消分配记录
     * @param waveNo
     * @return
     */
	public List<BanQinWmDelAllocEntity> getWmDelAllocEntityByWaveNo(String waveNo, String orgId) {
		return wmDelAllocService.getEntityByWaveNo(waveNo, orgId);
	}

    /**
     * 根据波次单号获取取消预配记录
     * @param waveNo
     * @return
     */
	public List<BanQinWmDelPreallocEntity> getWmDelPreallocEntityByWaveNo(String waveNo, String orgId) {
		return wmDelPreallocService.getEntityByWaveNo(waveNo, orgId);
	}

	/**
	 * 获取面单
	 * @param waveNoList
	 * @param orgId
	 * @return
	 */
	public ResultMessage getWaybill(List<String> waveNoList, String orgId) {
		return outboundTrackingService.getWaybillByWave(waveNoList, orgId);
	}

	/**
	 * 打印面单
	 * @param waveNoList
	 * @param orgId
	 * @return
	 */
	public ResultMessage printWaybill(List<String> waveNoList, String orgId) {
		return outboundTrackingService.printWaybillByWave(waveNoList, orgId);
	}

    /**
     * 获取面单并打印
     *
     * @param waveNoList 波次单号
     * @param orgId      机构ID
     */
    public ResultMessage getAndPrintWaybill(List<String> waveNoList, String orgId) {
        ResultMessage msg = getWaybill(waveNoList, orgId);
        if (msg.isSuccess()) {
            msg.setData(printWaybill(waveNoList, orgId).getData());
        }
        return msg;
    }

	/**
	 * 获取标签数据
	 * @return
	 */
	public List<WmSoSkuLabel> getWmSoSkuLabel(BanQinWmSoAllocEntity entity) {
		return wmSoAllocService.getWmSoSkuLabel(entity);
	}

}