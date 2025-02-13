package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmPackage;
import com.yunyou.modules.oms.basic.entity.OmPackageRelation;
import com.yunyou.modules.oms.basic.mapper.OmPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 包装Service
 *
 * @author WMJ
 * @version 2019-04-19
 */
@Service
@Transactional(readOnly = true)
public class OmPackageService extends CrudService<OmPackageMapper, OmPackage> {
    @Autowired
    private OmPackageRelationService omPackageRelationService;

    public Page<OmPackage> findPage(Page<OmPackage> page, OmPackage entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public OmPackage findByPackCode(String packCode, String orgId) {
        return mapper.findByPackageCode(packCode, orgId);
    }

    @Transactional
    public void save(OmPackage omPackage) {
        if (StringUtils.isBlank(omPackage.getId())) {
            omPackage.setPmCode(IdGen.uuid());
        }
        super.save(omPackage);
        if (CollectionUtil.isNotEmpty(omPackage.getPackageDetailList())) {
            for (OmPackageRelation relation : omPackage.getPackageDetailList()) {
                relation.setCdprCdpaPmCode(omPackage.getPmCode());
                relation.setPmCode(omPackage.getCdpaCode());
                omPackageRelationService.save(relation);
            }
        }
    }

    @Transactional
    public void delete(OmPackage omPackage) {
        omPackageRelationService.deleteByPmCode(omPackage.getPmCode());
        super.delete(omPackage);
    }

    @Transactional
    public void remove(String packCode, String orgId) {
        omPackageRelationService.remove(packCode, orgId);
        mapper.remove(packCode, orgId);
    }

    @Transactional
    public void batchInsert(List<OmPackage> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}