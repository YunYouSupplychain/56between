package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysOmsCustomer;
import com.yunyou.modules.sys.common.entity.extend.SysOmsCustomerEntity;
import com.yunyou.modules.sys.common.mapper.SysOmsCustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsCustomerService extends CrudService<SysOmsCustomerMapper, SysOmsCustomer> {

    @SuppressWarnings("unchecked")
    public Page<SysOmsCustomerEntity> findPage(Page page, SysOmsCustomerEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysOmsCustomerEntity> findGrid(Page page, SysOmsCustomerEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysOmsCustomerEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public SysOmsCustomer getByCode(String owner, String dataSet) {
        SysOmsCustomer sysOmsCustomer = new SysOmsCustomer();
        sysOmsCustomer.setEbcuCustomerNo(owner);
        sysOmsCustomer.setDataSet(dataSet);
        List<SysOmsCustomer> list = findList(sysOmsCustomer);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(SysOmsCustomer sysOmsCustomer) {
        if (StringUtils.isEmpty(sysOmsCustomer.getId())) {
            sysOmsCustomer.setPmCode(IdGen.uuid());
        }
        super.save(sysOmsCustomer);
    }

    @Transactional
    public void remove(String customerNo, String dataSet) {
        mapper.remove(customerNo, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysOmsCustomer> list) {
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