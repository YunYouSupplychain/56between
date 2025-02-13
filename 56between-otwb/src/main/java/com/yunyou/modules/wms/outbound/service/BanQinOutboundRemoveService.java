package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 出库管理删除方法类
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundRemoveService {
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	// 出库单商品明细
	@Autowired
	protected BanQinWmSoDetailService wmSoDetailService;
	// 波次单
	@Autowired
	protected BanQinWmWvHeaderService wmWvHeaderService;
	// 波次单明细
	@Autowired
	protected BanQinWmWvDetailService wmWvDetailService;
	// 拣货单
	@Autowired
	protected BanQinWmPickHeaderService wmPickHeaderService;
	// 拣货单明细
	@Autowired
	protected BanQinWmPickDetailService wmPickDetailService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;

    /**
     * 根据出库单号 删除出库单
     * @param soNo
     * @throws WarehouseException
     */
    @Transactional
	public void removeSoEntity(String soNo, String orgId) throws WarehouseException {
		// 1、获取出库单
		BanQinWmSoHeader model = wmSoHeaderService.findBySoNo(soNo, orgId);
		// 如果是拦截状态，那么不能删除
		if (model.getInterceptStatus().equals(WmsCodeMaster.ITC_INTERCEPT.getCode()) || model.getInterceptStatus().equals(WmsCodeMaster.ITC_WAITING.getCode())
				|| model.getInterceptStatus().equals(WmsCodeMaster.ITC_SUCCESS.getCode())) {
			// 订单{0}已拦截
			throw new WarehouseException("订单{0}已拦截", model.getSoNo());
		}
		// 冻结状态
		if (model.getHoldStatus().equals(WmsCodeMaster.ODHL_HOLD.getCode())) {
			// 订单{0}已被冻结
			throw new WarehouseException("订单{0}已被冻结", model.getSoNo());
		}
		// 如果状态不为新增，那么不能删除
		if (!model.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) {
			throw new WarehouseException("状态不为新增，不能删除", model.getSoNo());
		}
		// 订单状态00，但审核，不能删除
		if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
			// 已审核
			throw new WarehouseException("订单已审核", model.getSoNo());
		}
		ResultMessage msg = wmSoDetailService.checkSoExistCd(new String[]{soNo}, orgId);
		Object[] soNos = (Object[]) msg.getData();
		if (soNos.length <= 0) {
			throw new WarehouseException("存在越库标记的明细，不能操作");
		}
		// 删除出库单商品明细
		wmSoDetailService.removeBySoNo(soNo, orgId);
		// 删除出库单
		wmSoHeaderService.delete(model);
	}

    /**
     * 根据波次单号 删除波次单
     * @param waveNo
     * @throws WarehouseException
     */
    @Transactional
	public void removeWvEntity(String waveNo, String orgId) throws WarehouseException {
		// 1、获取波次单
		BanQinWmWvHeader model = wmWvHeaderService.findByWaveNo(waveNo, orgId);
		// 如果状态不为新增，那么不能删除
		if (!model.getStatus().equals(WmsCodeMaster.WAVE_NEW.getCode())) {
			throw new WarehouseException("状态不是新增，不能删除", model.getWaveNo());
		}
		// 删除出库单商品明细
		wmWvDetailService.removeByWaveNo(waveNo, orgId);
		// 删除出库单
		wmWvHeaderService.delete(model);
	}

	/**
	 * 根据拣货单号 删除拣货单
	 * @throws WarehouseException
	 */
	@Transactional
	public void removePickEntity(String pickNo, String orgId) throws WarehouseException {
		BanQinWmPickHeader pickHeader = wmPickHeaderService.findByPickNo(pickNo, orgId);
		List<BanQinWmPickDetail> pickDetails = wmPickDetailService.findByPickNo(pickNo, orgId);
		if (CollectionUtil.isNotEmpty(pickDetails)) {
			// 回刷分配明细拣货单号
			List<String> allocIds = pickDetails.stream().map(t -> t.getAllocId()).collect(Collectors.toList());
			List<BanQinWmSoAllocEntity> allocEntityList = wmSoAllocService.getEntityByProcessByCode(ProcessByCode.BY_ALLOC.getCode(), allocIds, WmsCodeMaster.ALLOC_FULL_ALLOC.getCode(), orgId);
			if (allocIds.size() != allocEntityList.size()) {
				throw new WarehouseException("存在状态不是完全分配的明细，不能删除", pickNo);
			}
			for (BanQinWmSoAllocEntity allocEntity : allocEntityList) {
				allocEntity.setPickNo(null);
				wmSoAllocService.save(allocEntity);
			}
		}
		wmPickHeaderService.delete(pickHeader);
		wmPickDetailService.removeByPickNo(pickNo, orgId);

	}
}
