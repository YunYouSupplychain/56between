package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysCommonPackage;
import com.yunyou.modules.sys.common.entity.SysCommonPackageRelation;
import com.yunyou.modules.sys.common.mapper.SysCommonPackageMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 包装Service
 *
 * @author WMJ
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class SysCommonPackageService extends CrudService<SysCommonPackageMapper, SysCommonPackage> {
    @Autowired
    private SysCommonPackageRelationService sysCommonPackageRelationService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    @Override
    public Page<SysCommonPackage> findPage(Page<SysCommonPackage> page, SysCommonPackage entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysCommonPackage> findGrid(Page<SysCommonPackage> page, SysCommonPackage entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysCommonPackage> findSync(SysCommonPackage qEntity) {
        List<SysCommonPackage> entities = mapper.findSync(qEntity);
        for (SysCommonPackage entity : entities) {
            entity.setPackageDetailList(sysCommonPackageRelationService.findByPackage(entity.getPmCode()));
        }
        return entities;
    }

    @Override
    public SysCommonPackage get(String id) {
        SysCommonPackage sysCommonPackage = super.get(id);
        if (sysCommonPackage != null) {
            sysCommonPackage.setPackageDetailList(sysCommonPackageRelationService.findByPackage(sysCommonPackage.getPmCode()));
        }
        return sysCommonPackage;
    }

    public SysCommonPackage getByCode(String packCode, String dataSet) {
        return mapper.getByCode(packCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysCommonPackage entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setPmCode(IdGen.uuid());
        }
        if (StringUtils.isBlank(entity.getCdpaIsUse())) {
            entity.setCdpaIsUse("0");
        }
        super.save(entity);
        for (SysCommonPackageRelation relation : entity.getPackageDetailList()) {
            relation.setCdprCdpaPmCode(entity.getPmCode());
            relation.setPmCode(entity.getCdpaCode());
            relation.setDataSet(entity.getDataSet());
            sysCommonPackageRelationService.save(relation);
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.syncPackage(Collections.singletonList(entity));
        }
    }

    @Override
    @Transactional
    public void delete(SysCommonPackage entity) {
        sysCommonPackageRelationService.deleteByPmCode(entity.getPmCode());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.removePackage(entity.getCdpaCode(), entity.getDataSet());
        }
    }

    @Transactional
    public void batchInsert(List<SysCommonPackage> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysCommonPackageRelation> relationList = Lists.newArrayList();
        list.stream().map(SysCommonPackage::getPackageDetailList).filter(CollectionUtil::isNotEmpty).forEach(relationList::addAll);
        sysCommonPackageRelationService.batchInsert(relationList);
    }
}