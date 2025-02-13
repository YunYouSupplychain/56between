package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmCustomer;
import com.yunyou.modules.oms.basic.entity.extend.OmCustomerEntity;
import com.yunyou.modules.oms.basic.mapper.OmCustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class OmCustomerService extends CrudService<OmCustomerMapper, OmCustomer> {

    public Page<OmCustomerEntity> findPage(Page page, OmCustomerEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<OmCustomerEntity> findGrid(Page page, OmCustomerEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public OmCustomerEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public OmCustomer getByCode(String customerNo, String orgId) {
        OmCustomer omCustomer = new OmCustomer();
        omCustomer.setEbcuCustomerNo(customerNo);
        omCustomer.setOrgId(orgId);
        List<OmCustomer> list = findList(omCustomer);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(OmCustomer omCustomer) {
        if (StringUtils.isEmpty(omCustomer.getId())) {
            omCustomer.setPmCode(IdGen.uuid());
        }
        super.save(omCustomer);
    }

    @Transactional
    public void remove(String customerNo, String orgId) {
        mapper.remove(customerNo, orgId);
    }

    @Transactional
    public void batchInsert(List<OmCustomer> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}