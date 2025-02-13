package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysCommonCustomerContacts;
import com.yunyou.modules.sys.common.entity.extend.SysCommonCustomerEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonCustomerContactsMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysCommonCustomerContactsService extends CrudService<SysCommonCustomerContactsMapper, SysCommonCustomerContacts> {
    @Autowired
    @Lazy
    private SysCommonCustomerService sysCommonCustomerService;
    @Autowired
    @Lazy
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    @Transactional
    public void batchInsert(List<SysCommonCustomerContacts> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }

    @Transactional
    public void remove(SysCommonCustomerContacts entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            SysCommonCustomerEntity qEntity = new SysCommonCustomerEntity();
            qEntity.setIds(Collections.singletonList(this.get(entity.getId()).getCustomerId()));
            syncPlatformDataCommonAction.syncCustomer(sysCommonCustomerService.findSync(qEntity));
        }
    }
}
