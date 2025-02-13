package com.yunyou.modules.wms.weigh.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.weigh.entity.BanQinWmMiddleWeigh;
import com.yunyou.modules.wms.weigh.entity.BanQinWmWeighHistory;
import com.yunyou.modules.wms.weigh.mapper.BanQinWmWeighHistoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 称重履历表Service
 * @author zyf
 * @version 2020-01-08
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmWeighHistoryService extends CrudService<BanQinWmWeighHistoryMapper, BanQinWmWeighHistory> {

	public BanQinWmWeighHistory get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmWeighHistory> findList(BanQinWmWeighHistory banQinWmWeighHistory) {
		return super.findList(banQinWmWeighHistory);
	}
	
	public Page<BanQinWmWeighHistory> findPage(Page<BanQinWmWeighHistory> page, BanQinWmWeighHistory banQinWmWeighHistory) {
		return super.findPage(page, banQinWmWeighHistory);
	}
	
	@Transactional
	public void save(BanQinWmWeighHistory banQinWmWeighHistory) {
		super.save(banQinWmWeighHistory);
	}
	
	@Transactional
	public void delete(BanQinWmWeighHistory banQinWmWeighHistory) {
		super.delete(banQinWmWeighHistory);
	}

	@Transactional
	public void saveHistory(BanQinWmMiddleWeigh middleWeigh, BanQinWmSoAllocEntity allocEntity, BanQinWmSoEntity soEntity, String handleAction, String handleMsg, User user) {
		BanQinWmWeighHistory history = new BanQinWmWeighHistory();
		BeanUtils.copyProperties(middleWeigh, history);
		history.setId(IdGen.uuid());
		history.setCreateBy(user);
		history.setCreateDate(new Date());
		history.setUpdateBy(user);
		history.setUpdateDate(new Date());
		history.setOrderNo(allocEntity.getSoNo());
		history.setAllocId(allocEntity.getAllocId());
		history.setCustomerOrderNo(soEntity.getDef5());
		history.setBusinessNo(soEntity.getDef1());
		history.setChainNo(soEntity.getDef2());
		history.setTaskNo(soEntity.getDef3());
		history.setExternalNo(soEntity.getDef16());
		history.setWaveNo(soEntity.getWaveNo());
		history.setOwnerCode(allocEntity.getOwnerCode());
		history.setOwnerName(allocEntity.getOwnerName());
		history.setSkuCode(allocEntity.getSkuCode());
		history.setSkuName(allocEntity.getSkuName());
		history.setCaseNo(allocEntity.getCaseNo());
		history.setHandleAction(handleAction);
		history.setHandleMsg(handleMsg);
		mapper.insert(history);
	}
}