package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsPackage;
import com.yunyou.modules.sys.common.entity.SysWmsPackageRelation;
import com.yunyou.modules.sys.common.mapper.SysWmsPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 包装Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsPackageService extends CrudService<SysWmsPackageMapper, SysWmsPackage> {
    @Autowired
    private SysWmsPackageRelationService sysWmsPackageRelationService;

    @Override
    public Page<SysWmsPackage> findPage(Page<SysWmsPackage> page, SysWmsPackage sysWmsPackage) {
        dataRuleFilter(sysWmsPackage);
        sysWmsPackage.setPage(page);
        page.setList(mapper.findPage(sysWmsPackage));
        return page;
    }

    public Page<SysWmsPackage> findGrid(Page<SysWmsPackage> page, SysWmsPackage sysWmsPackage) {
        dataRuleFilter(sysWmsPackage);
        sysWmsPackage.setPage(page);
        page.setList(mapper.findGrid(sysWmsPackage));
        return page;
    }

    public SysWmsPackage findByPackCode(String packCode, String dataSet) {
        return mapper.findByPackageCode(packCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsPackage entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setPmCode(IdGen.uuid());
        }
        super.save(entity);
        for (SysWmsPackageRelation relation : entity.getPackageDetailList()) {
            if (StringUtils.isEmpty(relation.getId())) {
                relation.setCdprCdpaPmCode(entity.getPmCode());
                relation.setPmCode(entity.getCdpaCode());
            }
            sysWmsPackageRelationService.save(relation);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsPackage sysWmsPackage) {
        sysWmsPackageRelationService.deleteByPmCode(sysWmsPackage.getPmCode());
        super.delete(sysWmsPackage);
    }

    @Transactional
    public void remove(String packageCode, String dataSet) {
        sysWmsPackageRelationService.remove(packageCode, dataSet);
        mapper.remove(packageCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysWmsPackage> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysWmsPackageRelation> relationList = Lists.newArrayList();
        list.stream().map(SysWmsPackage::getPackageDetailList).filter(CollectionUtil::isNotEmpty).forEach(relationList::addAll);
        sysWmsPackageRelationService.batchInsert(relationList);
    }
}