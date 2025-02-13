package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysOmsBusinessServiceScope;
import com.yunyou.modules.sys.common.mapper.SysOmsBusinessServiceScopeMapper;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务服务范围Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsBusinessServiceScopeService extends CrudService<SysOmsBusinessServiceScopeMapper, SysOmsBusinessServiceScope> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    @Lazy
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;

    @Override
    public Page<SysOmsBusinessServiceScope> findPage(Page<SysOmsBusinessServiceScope> page, SysOmsBusinessServiceScope entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysOmsBusinessServiceScope> findGrid(Page<SysOmsBusinessServiceScope> page, SysOmsBusinessServiceScope entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysOmsBusinessServiceScope> findSync(SysOmsBusinessServiceScope entity) {
        List<SysOmsBusinessServiceScope> list = mapper.findSync(entity);
        list.forEach(o -> o.setAreaList(mapper.getArea(o.getId())));
        return list;
    }

    @Override
    public SysOmsBusinessServiceScope get(String id) {
        SysOmsBusinessServiceScope entity = super.get(id);
        if (entity != null) {
            entity.setAreaList(mapper.getArea(id));
        }
        return entity;
    }

    @Override
    @Transactional
    public void save(SysOmsBusinessServiceScope entity) {
        if (StringUtils.isBlank(entity.getGroupCode())) {
            entity.setGroupCode(noService.getDocumentNo(GenNoType.OM_BUSINESS_GROUP_NO.name()));
        }
        super.save(entity);
        // 更新业务服务范围与区域关联
        mapper.deleteArea(entity);
        if (CollectionUtil.isNotEmpty(entity.getAreaList())) {
            List<Area> areaList = entity.getAreaList();
            int size = entity.getAreaList().size();
            for (int i = 0; i < size; i = i + 999) {
                if (size >= i + 999) {
                    entity.setAreaList(areaList.subList(i, i + 999));
                } else {
                    entity.setAreaList(areaList.subList(i, size));
                }
                mapper.insertArea(entity);
            }
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysOmsBusinessServiceScope entity) {
        mapper.deleteArea(entity);
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.removeBusinessServiceScope(entity.getGroupCode(), entity.getDataSet());
        }
    }

}