package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysCommonCustomer;
import com.yunyou.modules.sys.common.entity.SysCommonCustomerContacts;
import com.yunyou.modules.sys.common.entity.extend.SysCommonCustomerEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonCustomerMapper;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 客户信息Service
 *
 * @author WMJ
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class SysCommonCustomerService extends CrudService<SysCommonCustomerMapper, SysCommonCustomer> {
    @Autowired
    private SysCommonCustomerContactsService sysCommonCustomerContactsService;
    @Autowired
    @Lazy
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;
    @Autowired
    private AreaService areaService;

    @SuppressWarnings("unchecked")
    public Page<SysCommonCustomerEntity> findPage(Page page, SysCommonCustomerEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<SysCommonCustomerEntity> list = mapper.findPage(entity);
        list.forEach(o -> o.setAreaName(areaService.getFullName(o.getAreaId())));
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysCommonCustomerEntity> findGrid(Page page, SysCommonCustomerEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysCommonCustomer> findSync(SysCommonCustomerEntity qEntity) {
        List<SysCommonCustomer> entities = mapper.findSync(qEntity);
        for (SysCommonCustomer entity : entities) {
            entity.setContactsList(sysCommonCustomerContactsService.findList(new SysCommonCustomerContacts(entity)));
        }
        return entities;
    }

    public SysCommonCustomerEntity getEntity(String id) {
        SysCommonCustomerEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setAreaName(areaService.getFullName(entity.getAreaId()));
            entity.setContactsList(sysCommonCustomerContactsService.findList(new SysCommonCustomerContacts(entity)));
        }
        return entity;
    }

    public SysCommonCustomer getByCode(String code, String dataSet) {
        return mapper.getByCode(code, dataSet);
    }

    @Override
    @Transactional
    public void save(SysCommonCustomer entity) {
        if (entity.getType().contains(CustomerType.SETTLEMENT.getCode())) {
            entity.setSettleCode(entity.getCode());
        }
        super.save(entity);
        for (SysCommonCustomerContacts sysCommonCustomerContacts : entity.getContactsList()) {
            if (sysCommonCustomerContacts.getId() == null) {
                continue;
            }
            sysCommonCustomerContacts.setCustomer(entity);
            sysCommonCustomerContacts.setDataSet(entity.getDataSet());
            sysCommonCustomerContactsService.save(sysCommonCustomerContacts);
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.syncCustomer(Collections.singletonList(entity));
        }
    }

    @Override
    @Transactional
    public void delete(SysCommonCustomer entity) {
        sysCommonCustomerContactsService.delete(new SysCommonCustomerContacts(entity));
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.removeCustomer(entity.getCode(), entity.getDataSet());
        }
    }

    @Transactional
    public void batchInsert(List<SysCommonCustomer> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysCommonCustomerContacts> contactsList = Lists.newArrayList();
        list.stream().map(SysCommonCustomer::getContactsList).filter(CollectionUtil::isNotEmpty).forEach(contactsList::addAll);
        sysCommonCustomerContactsService.batchInsert(contactsList);
    }
}