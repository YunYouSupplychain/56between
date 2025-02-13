package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackage;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhPackageMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 包装Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhPackageService extends CrudService<BanQinCdWhPackageMapper, BanQinCdWhPackage> {
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;

    public Page<BanQinCdWhPackage> findPage(Page<BanQinCdWhPackage> page, BanQinCdWhPackage banQinCdWhPackage) {
        dataRuleFilter(banQinCdWhPackage);
        banQinCdWhPackage.setPage(page);
        page.setList(mapper.findPage(banQinCdWhPackage));
        return page;
    }

    @Transactional
    public void save(BanQinCdWhPackage banQinCdWhPackage) {
        if (StringUtils.isBlank(banQinCdWhPackage.getId())) {
            banQinCdWhPackage.setPmCode(IdGen.uuid());
        }
        super.save(banQinCdWhPackage);
        if (CollectionUtil.isNotEmpty(banQinCdWhPackage.getPackageDetailList())) {
            for (BanQinCdWhPackageRelation relation : banQinCdWhPackage.getPackageDetailList()) {
                relation.setCdprCdpaPmCode(banQinCdWhPackage.getPmCode());
                relation.setPmCode(banQinCdWhPackage.getCdpaCode());
                relation.setOrgId(banQinCdWhPackage.getOrgId());
                cdWhPackageRelationService.save(relation);
            }
        }
    }

    @Transactional
    public void delete(BanQinCdWhPackage banQinCdWhPackage) {
        cdWhPackageRelationService.deleteByPmCode(banQinCdWhPackage.getPmCode());
        super.delete(banQinCdWhPackage);
    }

    public BanQinCdWhPackage findByPackCode(String packCode, String orgId) {
        return mapper.findByPackageCode(packCode, orgId);
    }

    /**
     * 描述：根据包装代码获取包装
     */
    public BanQinCdWhPackageEntity findByPackageCode(String packCode, String orgId) {
        BanQinCdWhPackageEntity entity = new BanQinCdWhPackageEntity();

        BanQinCdWhPackage banQinCdWhPackage = mapper.findByPackageCode(packCode, orgId);
        if (banQinCdWhPackage != null) {
            BeanUtils.copyProperties(banQinCdWhPackage, entity);
            entity.setCdWhPackageRelations(cdWhPackageRelationService.findByPackage(banQinCdWhPackage.getPmCode(), banQinCdWhPackage.getOrgId()));
        }
        return entity;
    }

    @Transactional
    public void remove(String packCode, String orgId) {
        cdWhPackageRelationService.remove(packCode, orgId);
        mapper.remove(packCode, orgId);
    }

    @Transactional
    public void batchInsert(List<BanQinCdWhPackage> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}