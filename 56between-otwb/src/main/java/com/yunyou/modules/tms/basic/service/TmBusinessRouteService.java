package com.yunyou.modules.tms.basic.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmBusinessRoute;
import com.yunyou.modules.tms.basic.entity.extend.TmBusinessRouteEntity;
import com.yunyou.modules.tms.basic.mapper.TmBusinessRouteMapper;
import com.yunyou.modules.tms.common.TmsException;

@Service
@Transactional(readOnly = true)
public class TmBusinessRouteService extends CrudService<TmBusinessRouteMapper, TmBusinessRoute> {

    public TmBusinessRouteEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public TmBusinessRouteEntity getByCode(String code, String orgId) {
        return mapper.getByCode(code, orgId);
    }

    @SuppressWarnings("unchecked")
    public Page<TmBusinessRouteEntity> findPage(Page page, TmBusinessRouteEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmBusinessRouteEntity> findGrid(Page page, TmBusinessRouteEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    @Override
    @Transactional
    public void save(TmBusinessRoute entity) {
        this.saveValidator(entity);
        super.save(entity);
    }

    public void saveValidator(TmBusinessRoute entity) {
        if (StringUtils.isBlank(entity.getCode())) {
            throw new TmsException("编码不能为空");
        }
        if (StringUtils.isBlank(entity.getName())) {
            throw new TmsException("路线不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(entity.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
    }

    @Transactional
    public void remove(String code, String orgId) {
        mapper.remove(code, orgId);
    }
}
