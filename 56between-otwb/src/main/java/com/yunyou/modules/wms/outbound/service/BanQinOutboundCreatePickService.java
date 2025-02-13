package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPickDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPickHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 拣货单生成类
 * @author ZYF
 * @version 2020/05/13
 */
@Service
public class BanQinOutboundCreatePickService {
	@Autowired
	private BanQinWmSoAllocService wmSoAllocService;
	@Autowired
	private SynchronizedNoService noService;
	@Autowired
	private BanQinWmPickHeaderService wmPickHeaderService;
	@Autowired
	private BanQinWmPickDetailService wmPickDetailService;

	/**
	 * 生成拣货单
	 * @throws WarehouseException
	 */
	@Transactional
	public void createPick(List<String> allocIds, String orgId) throws WarehouseException {
		List<BanQinWmSoAllocEntity> wmSoAllocEntityList = Lists.newArrayList();
		// 获取分配明细
		wmSoAllocEntityList = wmSoAllocService.getEntityByProcessByCode(ProcessByCode.BY_ALLOC.getCode(), allocIds, WmsCodeMaster.ALLOC_FULL_ALLOC.getCode(), orgId);
		if (wmSoAllocEntityList.size() != allocIds.size()) {
			throw new WarehouseException("只能选择【完全分配】状态的数据");
		}
		wmSoAllocEntityList = wmSoAllocEntityList.stream().filter(t -> StringUtils.isBlank(t.getPickNo())).collect(Collectors.toList());
		if (wmSoAllocEntityList.size() == 0) {
			throw new WarehouseException("没有可生成拣货单的数据");
		}
		if (wmSoAllocEntityList.size() != allocIds.size()) {
			throw new WarehouseException("只能选择【未生成拣货单】的数据");
		}
		// 生成拣货单数据
		BanQinWmPickHeader pickHeader = new BanQinWmPickHeader();
		String pickNo = noService.getDocumentNo(GenNoType.WM_PICK_NO.name());
		pickHeader.setPickNo(pickNo);
		pickHeader.setOrgId(orgId);
		wmPickHeaderService.save(pickHeader);
		for (BanQinWmSoAllocEntity allocEntity : wmSoAllocEntityList) {
			BanQinWmPickDetail pickDetail = new BanQinWmPickDetail();
			pickDetail.setPickNo(pickNo);
			pickDetail.setSoNo(allocEntity.getSoNo());
			pickDetail.setAllocId(allocEntity.getAllocId());
			pickDetail.setOrgId(orgId);
			wmPickDetailService.save(pickDetail);
			// 回刷拣货单号
			allocEntity.setPickNo(pickNo);
			wmSoAllocService.save(allocEntity);
		}
	}
}