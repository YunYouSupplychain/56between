package com.yunyou.modules.oms.inv.service;

import java.util.Date;
import java.util.List;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.oms.inv.entity.OmSaleInventory;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.inv.entity.OmSaleInventoryHistory;
import com.yunyou.modules.oms.inv.mapper.OmSaleInventoryHistoryMapper;

/**
 * 销售库存履历Service
 * @author zyf
 * @version 2020-01-06
 */
@Service
@Transactional(readOnly = true)
public class OmSaleInventoryHistoryService extends CrudService<OmSaleInventoryHistoryMapper, OmSaleInventoryHistory> {

	public OmSaleInventoryHistory get(String id) {
		return super.get(id);
	}
	
	public List<OmSaleInventoryHistory> findList(OmSaleInventoryHistory omSaleInventoryHistory) {
		return super.findList(omSaleInventoryHistory);
	}
	
	public Page<OmSaleInventoryHistory> findPage(Page<OmSaleInventoryHistory> page, OmSaleInventoryHistory omSaleInventoryHistory) {
		return super.findPage(page, omSaleInventoryHistory);
	}
	
	@Transactional
	public void save(OmSaleInventoryHistory omSaleInventoryHistory) {
		super.save(omSaleInventoryHistory);
	}
	
	@Transactional
	public void delete(OmSaleInventoryHistory omSaleInventoryHistory) {
		super.delete(omSaleInventoryHistory);
	}

	@Transactional
	public void saveHistory(Double beforeQty, Double operQty, Double afterQty, OmSaleInventory saleInventory, String operate, String orderNo) {
		OmSaleInventoryHistory history = new OmSaleInventoryHistory();
		history.setId(IdGen.uuid());
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			history.setCreateBy(user);
			history.setUpdateBy(user);
		} else {
			history.setCreateBy(new User("INTERFACE"));
			history.setUpdateBy(new User("INTERFACE"));
		}
		history.setCreateDate(new Date());
		history.setUpdateDate(new Date());
		history.setWarehouse(saleInventory.getWarehouse());
		history.setOrgId(saleInventory.getOrgId());
		history.setOwner(saleInventory.getOwner());
		history.setSkuCode(saleInventory.getSkuCode());
		history.setBeforeQty(beforeQty);
		history.setOperQty(operQty);
		history.setAfterQty(afterQty);
		history.setOperate(operate);
		history.setOrderNo(orderNo);
		mapper.insert(history);
	}
}