package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmBusinessServiceScope;
import com.yunyou.modules.oms.basic.entity.extend.OmBusinessServiceScopeEntity;
import com.yunyou.modules.oms.basic.mapper.OmBusinessServiceScopeMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务服务范围Service
 *
 * @author Jianhua Liu
 * @version 2019-10-23
 */
@Service
@Transactional(readOnly = true)
public class OmBusinessServiceScopeService extends CrudService<OmBusinessServiceScopeMapper, OmBusinessServiceScope> {
    @Autowired
    private SynchronizedNoService noService;

    public Page<OmBusinessServiceScopeEntity> findPage(Page page, OmBusinessServiceScopeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public OmBusinessServiceScopeEntity getEntity(String id) {
        OmBusinessServiceScopeEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setAreaList(mapper.getArea(id));
        }
        return entity;
    }

    public OmBusinessServiceScopeEntity getByCode(String groupCode, String orgId) {
        return mapper.getByCode(groupCode, orgId);
    }

    public List<OmBusinessServiceScope> findByAreaId(String areaId, String orgId) {
        return mapper.findByAreaId(areaId, orgId);
    }

    @Transactional
    public void saveEntity(OmBusinessServiceScopeEntity entity) {
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
    }

    @Transactional
    public void deleteEntity(String id) {
        OmBusinessServiceScopeEntity entity = this.getEntity(id);
        this.deleteArea(entity);
        super.delete(entity);
    }

    @Transactional
    public void deleteArea(OmBusinessServiceScopeEntity entity) {
        mapper.deleteArea(entity);
    }

    @Transactional
    public void copy(String id) {
        OmBusinessServiceScopeEntity entity = this.getEntity(id);
        entity.setRemarks("复制来自" + entity.getGroupCode());
        entity.setGroupCode(null);
        entity.setId("");
        this.saveEntity(entity);
    }

    @Transactional
    public void remove(String groupCode, String orgId) {
        mapper.removeArea(groupCode, orgId);
        mapper.remove(groupCode, orgId);
    }
}