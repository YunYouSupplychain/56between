package com.yunyou.modules.wms.weigh.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingConstant;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.weigh.entity.BanQinWmMiddleWeigh;
import com.yunyou.modules.wms.weigh.mapper.BanQinWmMiddleWeighMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 称重中间表Service
 * @author zyf
 * @version 2020-01-06
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmMiddleWeighService extends CrudService<BanQinWmMiddleWeighMapper, BanQinWmMiddleWeigh> {

	public BanQinWmMiddleWeigh get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmMiddleWeigh> findList(BanQinWmMiddleWeigh banQinWmMiddleWeigh) {
		return super.findList(banQinWmMiddleWeigh);
	}
	
	public Page<BanQinWmMiddleWeigh> findPage(Page<BanQinWmMiddleWeigh> page, BanQinWmMiddleWeigh banQinWmMiddleWeigh) {
		return super.findPage(page, banQinWmMiddleWeigh);
	}
	
	@Transactional
	public void save(BanQinWmMiddleWeigh banQinWmMiddleWeigh) {
		super.save(banQinWmMiddleWeigh);
	}
	
	@Transactional
	public void delete(BanQinWmMiddleWeigh banQinWmMiddleWeigh) {
		super.delete(banQinWmMiddleWeigh);
	}

	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveWeighInfo(String boxNum, String deviceNo, String orgId, Double tareQty, Double weighQty, Date weighTime, User user) {
		BanQinWmMiddleWeigh info = new BanQinWmMiddleWeigh();
		info.setId(IdGen.uuid());
		info.setCreateBy(user);
		info.setCreateDate(new Date());
		info.setUpdateBy(user);
		info.setUpdateDate(new Date());
		info.setBoxNum(boxNum);
		info.setOrgId(orgId);
		info.setTareQty(tareQty);
		info.setWeighQty(weighQty);
		info.setWeighTime(weighTime);
		info.setWeighStatus(BanQinWeighingConstant.STATUS_N);
		info.setShipStatus(BanQinWeighingConstant.STATUS_N);
		info.setDeviceNo(deviceNo);
		mapper.insert(info);
	}

	public List<BanQinWmMiddleWeigh> findListByStatus(String weighStatus, String shipStatus) {
		BanQinWmMiddleWeigh condition = new BanQinWmMiddleWeigh();
		condition.setWeighStatus(weighStatus);
		condition.setShipStatus(shipStatus);
		return mapper.findList(condition);
	}

	@Transactional
	public void update(BanQinWmMiddleWeigh banQinWmMiddleWeigh) {
		mapper.update(banQinWmMiddleWeigh);
	}
}