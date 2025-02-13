package com.yunyou.modules.sys.common.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysDataSetOrgRelation;
import com.yunyou.modules.sys.common.entity.extend.SysDataSetOrgRelationEntity;
import com.yunyou.modules.sys.common.mapper.SysDataSetOrgRelationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据套机构关系Service
 */
@Service
@Transactional(readOnly = true)
public class SysDataSetOrgRelationService extends CrudService<SysDataSetOrgRelationMapper, SysDataSetOrgRelation> {

    @SuppressWarnings("unchecked")
    public Page<SysDataSetOrgRelationEntity> findGrid(Page page, SysDataSetOrgRelationEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysDataSetOrgRelationEntity> findEntity(SysDataSetOrgRelationEntity entity) {
        return mapper.findEntity(entity);
    }

    public List<String> findOrgIdByDataSet(String dataSet) {
        return mapper.findList(new SysDataSetOrgRelation(null, dataSet, null)).stream().map(SysDataSetOrgRelation::getOrgId).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
    }

    public SysDataSetOrgRelationEntity getEntityByOrgId(@NotNull String orgId) {
        return mapper.getEntityByOrgId(orgId);
    }

    public void saveValidator(SysDataSetOrgRelation entity) {
        SysDataSetOrgRelationEntity relation = this.getEntityByOrgId(entity.getOrgId());
        if (relation != null) {
            if (!relation.getId().equals(entity.getId())) {
                throw new GlobalException("机构[" + relation.getOrgCode() + "]重复");
            }
            if (!relation.getDataSet().equals(entity.getDataSet())) {
                throw new GlobalException("机构[" + relation.getOrgCode() + "]已存在在数据套[" + relation.getDataSet() + "]中");
            }
        }
    }

    public List<String> findDataSetByOrgCodes(List<String> orgCodes) {
        return mapper.findDataSetByOrgCodes(orgCodes);
    }
}