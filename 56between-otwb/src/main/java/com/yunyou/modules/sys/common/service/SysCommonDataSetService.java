package com.yunyou.modules.sys.common.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysCommonDataSet;
import com.yunyou.modules.sys.common.entity.SysDataSetOrgRelation;
import com.yunyou.modules.sys.common.entity.extend.SysCommonDataSetEntity;
import com.yunyou.modules.sys.common.entity.extend.SysDataSetOrgRelationEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonDataSetMapper;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据套Service
 */
@Service
@Transactional(readOnly = true)
public class SysCommonDataSetService extends CrudService<SysCommonDataSetMapper, SysCommonDataSet> {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;

    @Override
    public Page<SysCommonDataSet> findPage(Page<SysCommonDataSet> page, SysCommonDataSet entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysCommonDataSet> findGrid(Page<SysCommonDataSet> page, SysCommonDataSet entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysCommonDataSet getByCode(String code) {
        return mapper.getByCode(code);
    }

    public SysCommonDataSet getDefault() {
        SysCommonDataSet aDefault = mapper.getDefault();
        return aDefault == null ? new SysCommonDataSet() : aDefault;
    }

    public SysCommonDataSetEntity getEntity(String id) {
        SysCommonDataSetEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setRelationList(sysDataSetOrgRelationService.findEntity(new SysDataSetOrgRelationEntity(null, entity.getCode(), null)));
        }
        return entity;
    }

    @Transactional
    public void save(SysCommonDataSetEntity entity) {
        if ("Y".equals(entity.getIsDefault())) {
            SysCommonDataSet aDefault = this.getDefault();
            if (aDefault != null && StringUtils.isNotBlank(aDefault.getId()) && !aDefault.getId().equals(entity.getId())) {
                aDefault.setIsDefault("N");
                super.save(aDefault);
            }
        }
        if (StringUtils.isBlank(entity.getIsDefault())) {
            entity.setIsDefault("N");
        }
        super.save(entity);
        for (SysDataSetOrgRelation relation : entity.getRelationList()) {
            if (relation.getId() == null) {
                continue;
            }
            relation.setDataSet(entity.getCode());
            sysDataSetOrgRelationService.saveValidator(relation);
            sysDataSetOrgRelationService.save(relation);
        }
    }

    @Override
    @Transactional
    public void delete(SysCommonDataSet entity) {
        sysDataSetOrgRelationService.delete(new SysDataSetOrgRelation(null, entity.getCode(), null));
        super.delete(entity);
    }

    @Transactional
    public void setDefault(String id) {
        SysCommonDataSet sysCommonDataSet = this.get(id);
        if (sysCommonDataSet == null) throw new GlobalException("数据已过期");
        SysCommonDataSet aDefault = this.getDefault();
        if (aDefault != null && StringUtils.isNotBlank(aDefault.getId()) && !aDefault.getId().equals(sysCommonDataSet.getId())) {
            aDefault.setIsDefault("N");
            super.save(aDefault);
        }
        sysCommonDataSet.setIsDefault("Y");
        super.save(sysCommonDataSet);
        // 设置用户数据套缓存
        SysDataSetUtils.pushUserDataSetCache(sysCommonDataSet);
    }
}