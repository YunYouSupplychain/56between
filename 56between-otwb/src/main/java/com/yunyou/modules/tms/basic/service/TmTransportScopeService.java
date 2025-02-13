package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportScope;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportScopeEntity;
import com.yunyou.modules.tms.basic.mapper.TmTransportScopeMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务服务范围Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmTransportScopeService extends CrudService<TmTransportScopeMapper, TmTransportScope> {

    @Override
    @Transactional
    public void delete(TmTransportScope tmTransportScope) {
        // 删除服务范围区域
        this.deleteAreaByScope(tmTransportScope.getId());
        super.delete(tmTransportScope);
    }

    public void saveValidator(TmTransportScope tmTransportScope) {
        if (StringUtils.isBlank(tmTransportScope.getCode())) {
            throw new TmsException("服务范围编码不能为空");
        }
        if (StringUtils.isBlank(tmTransportScope.getName())) {
            throw new TmsException("服务范围名称不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmTransportScope.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmTransportScope> list = findList(new TmTransportScope(tmTransportScope.getCode(), tmTransportScope.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmTransportScope.getId()))) {
                throw new TmsException("服务范围编码[" + tmTransportScope.getCode() + "]已存在");
            }
        }
    }

    public TmTransportScopeEntity getEntity(String id) {
        TmTransportScopeEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setAreaList(mapper.findArea(id));
        }
        return entity;
    }

    @Override
    public Page<TmTransportScope> findPage(Page<TmTransportScope> page, TmTransportScope tmTransportScope) {
        dataRuleFilter(tmTransportScope);
        tmTransportScope.setPage(page);
        page.setList(mapper.findPage(tmTransportScope));
        return page;
    }

    public Page<TmTransportScopeEntity> findGrid(Page page, TmTransportScopeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Transactional
    public void saveArea(TmTransportScopeEntity entity) {
        this.deleteAreaByScope(entity.getId());
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
    public void deleteAreaByScope(String headId) {
        mapper.deleteAreaByScope(headId);
    }

    public List<Area> findAreaByScopeCode(String code, String orgId) {
        return mapper.findAreaByScopeCode(code, orgId);
    }

    public TmTransportScope getByCode(String code, String orgId) {
        return mapper.getByCode(code, orgId);
    }

    @Transactional
    public void remove(String code, String orgId) {
        mapper.removeArea(code, orgId);
        mapper.remove(code, orgId);
    }
}