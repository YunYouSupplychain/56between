package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.basic.entity.TmRouteScope;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeObjEntity;
import com.yunyou.modules.tms.basic.mapper.TmRouteScopeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 路由范围Service
 *
 * @author liujianhua
 * @version 2021-10-15
 */
@Service
@Transactional(readOnly = true)
public class TmRouteScopeService extends CrudService<TmRouteScopeMapper, TmRouteScope> {
    @Autowired
    private TmRouteScopeObjService tmRouteScopeObjService;

    public TmRouteScopeEntity getEntity(String id) {
        TmRouteScopeEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setObjList(tmRouteScopeObjService.findEntity(new TmRouteScopeObjEntity(entity.getCode(), entity.getOrgId())));
        }
        return entity;
    }

    public Page<TmRouteScope> findPage(Page<TmRouteScope> page, TmRouteScopeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void saveEntity(TmRouteScopeEntity entity) {
        super.save(entity);
        if (StringUtils.isNotBlank(entity.getId())) {
            tmRouteScopeObjService.remove(entity.getId());
        }
        for (TmRouteScopeObjEntity objEntity : entity.getObjList()) {
            objEntity.setId(null);
            objEntity.setRouteScopeCode(entity.getCode());
            objEntity.setOrgId(entity.getOrgId());
            tmRouteScopeObjService.save(objEntity);
        }
    }

    @Override
    @Transactional
    public void delete(TmRouteScope entity) {
        tmRouteScopeObjService.remove(entity.getId());
        super.delete(entity);
    }
}