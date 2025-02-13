package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysCommonRegion;
import com.yunyou.modules.sys.common.entity.extend.SysCommonRegionEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonRegionMapper;
import com.yunyou.modules.sys.entity.Area;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 */
@Service
@Transactional(readOnly = true)
public class SysCommonRegionService extends CrudService<SysCommonRegionMapper, SysCommonRegion> {

    @SuppressWarnings("unchecked")
    public Page<SysCommonRegionEntity> findPage(Page page, SysCommonRegionEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysCommonRegionEntity> findGrid(Page page, SysCommonRegionEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Override
    public SysCommonRegion get(String id) {
        SysCommonRegion sysCommonRegion = super.get(id);
        if (sysCommonRegion != null) {
            sysCommonRegion.setAreaList(mapper.findPlace(id));
        }
        return sysCommonRegion;
    }

    public SysCommonRegionEntity getEntity(String id) {
        SysCommonRegionEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setAreaList(mapper.findPlace(id));
        }
        return entity;
    }

    public SysCommonRegion getByCode(String code, String dataSet) {
        return mapper.getByCode(code, dataSet);
    }

    @Override
    @Transactional
    public void delete(SysCommonRegion entity) {
        mapper.deleteArea(entity.getId());
        super.delete(entity);
    }

    @Transactional
    public void saveArea(SysCommonRegion entity) {
        mapper.deleteArea(entity.getId());
        if (CollectionUtil.isEmpty(entity.getAreaList())) {
            return;
        }
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