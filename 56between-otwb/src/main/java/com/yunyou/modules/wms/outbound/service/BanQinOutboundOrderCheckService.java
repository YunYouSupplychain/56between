package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单校验单操作类
 * @author ZYF
 * @version 2020/05/13
 */
@Service
public class BanQinOutboundOrderCheckService {
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

    /**
     * 预配 - 按发运订单号+行号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage preallocBySoLine(String processByCode, List<String> noList, String orgId) {
		return outboundBatchPreallocAction.outboundBatchPrealloc(processByCode, noList, orgId);
	}

    /**
     * 分配 - 按发运订单号+行号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage allocBySoLine(String processByCode, List<String> noList, String orgId) {
		ResultMessage msg;
		// 两步分配控制参数
		String twoStep = "N";
		if (twoStep == null) {
			twoStep = WmsConstants.NO;
		}
		if (twoStep.equals(WmsConstants.YES)) {
			// 执行两步分配
			msg = outboundBatchAllocTwoStepAction.outboundBatchAllocTwoStep(processByCode, noList, orgId);
		} else {
			// 执行一步分配
			msg = outboundBatchAllocOneStepAction.outboundBatchAllocOneStep(processByCode, noList, orgId);
		}
		return msg;
	}

	/**
	 * 拣货确认-按发运订单号+行号
	 * @param processByCode
	 * @return
	 */
	public ResultMessage pickingBySoLine(String processByCode, List<String> noList, String orgId) {
		return outboundBatchPickingAction.outboundBatchPicking(processByCode, noList, orgId);
	}

    /**
     * 取消预配-按发运订单号+行号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelPreallocBySoLine(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
	}

    /**
     * 取消分配-按发运订单号+行号
     * @param processByCode
     * @param noList
     * @param orgId
     * @return
     */
	public ResultMessage cancelAllocBySoLine(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(processByCode, noList, orgId);
	}

	/**
	 * 取消拣货-按发运订单号+行号
	 * @param processByCode
	 * @return
	 */
	public ResultMessage cancelPickingBySoLine(String processByCode, List<String> noList, String orgId) {
		return outboundBatchCancelPickingAction.outboundBatchCancelPicking(processByCode, noList, orgId);
	}

	/**
	 * 平均分配
	 * @param noList
	 * @param orgId
	 * @return
	 */
	@Transactional
	public ResultMessage allocAvg(List<String> noList, String orgId) {
		ResultMessage msg = new ResultMessage();
		List<BanQinWmSoDetailEntity> soDetailList = wmSoDetailService.findEntityByProcessCode(ProcessByCode.BY_SO_LINE.getCode(), noList, orgId);
		Map<String, List<BanQinWmSoDetailEntity>> skuMap = soDetailList.stream()
				.filter(s -> WmsCodeMaster.SO_NEW.getCode().equals(s.getStatus()) || WmsCodeMaster.SO_PART_ALLOC.getCode().equals(s.getStatus()))
				.collect(Collectors.groupingBy(o -> o.getOwnerCode() + "@" + o.getSkuCode()));
		for (Map.Entry<String, List<BanQinWmSoDetailEntity>> skuEntry : skuMap.entrySet()) {
			String[] ownerSku = skuEntry.getKey().split("@");
			String ownerCode = ownerSku[0];
			String skuCode = ownerSku[1];
			List<BanQinWmSoDetailEntity> storeList = skuEntry.getValue();
			storeList.sort(Comparator.comparing(BanQinWmSoDetailEntity::getSoNo));
//			getBySkuCodeAndOwnerCode
		}

		return msg;
	}

}