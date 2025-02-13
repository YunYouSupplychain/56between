package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysOmsPackage;
import com.yunyou.modules.sys.common.entity.SysOmsPackageRelation;
import com.yunyou.modules.sys.common.mapper.SysOmsPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 包装Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsPackageService extends CrudService<SysOmsPackageMapper, SysOmsPackage> {
    @Autowired
    private SysOmsPackageRelationService sysOmsPackageRelationService;

    @Override
    public Page<SysOmsPackage> findPage(Page<SysOmsPackage> page, SysOmsPackage entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public SysOmsPackage findByPackCode(String packCode, String dataSet) {
        return mapper.findByPackageCode(packCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysOmsPackage sysOmsPackage) {
        if (StringUtils.isEmpty(sysOmsPackage.getId())) {
            sysOmsPackage.setPmCode(IdGen.uuid());
        }
        super.save(sysOmsPackage);
        if (CollectionUtil.isNotEmpty(sysOmsPackage.getPackageDetailList())) {
            for (SysOmsPackageRelation relation : sysOmsPackage.getPackageDetailList()) {
                if (StringUtils.isBlank(relation.getId())) {
                    relation.setCdprCdpaPmCode(sysOmsPackage.getPmCode());
                    relation.setPmCode(sysOmsPackage.getCdpaCode());
                }
                sysOmsPackageRelationService.save(relation);
            }
        }
    }

    @Override
    @Transactional
    public void delete(SysOmsPackage sysOmsPackage) {
        sysOmsPackageRelationService.deleteByPmCode(sysOmsPackage.getPmCode());
        super.delete(sysOmsPackage);
    }

    @Transactional
    public void remove(String packageCode, String dataSet) {
        sysOmsPackageRelationService.remove(packageCode, dataSet);
        mapper.remove(packageCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysOmsPackage> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysOmsPackageRelation> relationList = Lists.newArrayList();
        list.stream().map(SysOmsPackage::getPackageDetailList).filter(CollectionUtil::isNotEmpty).forEach(relationList::addAll);
        sysOmsPackageRelationService.batchInsert(relationList);
    }
}