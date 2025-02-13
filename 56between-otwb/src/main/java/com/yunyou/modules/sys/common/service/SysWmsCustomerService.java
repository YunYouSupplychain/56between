package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsCustomer;
import com.yunyou.modules.sys.common.mapper.SysWmsCustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsCustomerService extends CrudService<SysWmsCustomerMapper, SysWmsCustomer> {

    @SuppressWarnings("unchecked")
    public Page<SysWmsCustomer> findPage(Page<SysWmsCustomer> page, SysWmsCustomer sysWmsCustomer) {
        dataRuleFilter(sysWmsCustomer);
        sysWmsCustomer.setPage(page);
        page.setList(mapper.findPage(sysWmsCustomer));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysWmsCustomer> findGrid(Page<SysWmsCustomer> page, SysWmsCustomer sysWmsCustomer) {
        dataRuleFilter(sysWmsCustomer);
        sysWmsCustomer.setPage(page);
        page.setList(mapper.findGrid(sysWmsCustomer));
        return page;
    }

    public SysWmsCustomer getByCode(String customerNo, String orgId) {
        return mapper.getByCode(customerNo, orgId);
    }

    @Override
    @Transactional
    public void save(SysWmsCustomer entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setPmCode(IdGen.uuid());
        }
        super.save(entity);
    }

    @Transactional
    public void remove(String customerNo, String dataSet) {
        mapper.remove(customerNo, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysWmsCustomer> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}