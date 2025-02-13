package com.yunyou.modules.wms.customer.service;

import java.util.List;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.persistence.Page;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.mapper.BanQinEbCustomerMapper;

/**
 * 客户Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinEbCustomerService extends CrudService<BanQinEbCustomerMapper, BanQinEbCustomer> {

	public Page<BanQinEbCustomer> findPage(Page<BanQinEbCustomer> page, BanQinEbCustomer banQinEbCustomer) {
		dataRuleFilter(banQinEbCustomer);
		banQinEbCustomer.setPage(page);
		page.setList(mapper.findPage(banQinEbCustomer));
		return page;
	}

	@Override
	@Transactional
	public void save(BanQinEbCustomer banQinEbCustomer) {
	    if (StringUtils.isEmpty(banQinEbCustomer.getId())) {
	        banQinEbCustomer.setPmCode(IdGen.uuid());
        }
		super.save(banQinEbCustomer);
	}

	public BanQinEbCustomer find(String customerNo, String type, String orgId) {
		return mapper.find(customerNo, type, orgId);
	}

	public BanQinEbCustomer getByCode(String customerNo, String orgId) {
		return mapper.getByCode(customerNo, orgId);
	}

	public List<BanQinEbCustomer> getExistCustomer(List<String> ownerCodeList, String orgId) {
		return mapper.getExistCustomer(ownerCodeList, orgId);
	}

	@Transactional
	public void remove(String customerNo, String orgId) {
		mapper.remove(customerNo, orgId);
	}

	@Transactional
	public void batchInsert(List<BanQinEbCustomer> list) {
		for (int i = 0; i < list.size(); i += 999) {
			if (list.size() - i < 999) {
				mapper.batchInsert(list.subList(i, list.size()));
			} else {
				mapper.batchInsert(list.subList(i, i + 999));
			}
		}
	}
}