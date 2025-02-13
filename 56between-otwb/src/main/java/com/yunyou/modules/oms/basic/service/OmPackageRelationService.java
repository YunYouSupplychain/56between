package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmPackageRelation;
import com.yunyou.modules.oms.basic.mapper.OmPackageRelationMapper;
import com.yunyou.modules.sys.entity.DictValue;
import com.yunyou.modules.sys.mapper.DictValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装明细Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class OmPackageRelationService extends CrudService<OmPackageRelationMapper, OmPackageRelation> {
    @Autowired
    private DictValueMapper dictValueMapper;

    public Page<OmPackageRelation> findPage(Page<OmPackageRelation> page, OmPackageRelation entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public List<OmPackageRelation> initialList() {
        List<OmPackageRelation> result = new ArrayList<>();
        List<DictValue> initialItemList = dictValueMapper.findInitList("package_list_initial", "", "");
        for (DictValue dictValue : initialItemList) {
            OmPackageRelation packageDetail = new OmPackageRelation();
            packageDetail.setCdprSequencesNo(dictValue.getSort());
            packageDetail.setCdprUnit(dictValue.getValue());
            packageDetail.setCdprUnitLevel(dictValue.getValue());
            packageDetail.setCdprDesc(dictValue.getLabel());
            packageDetail.setCdprQuantity(1D);
            if (StringUtils.isNotEmpty(dictValue.getValue())) {
                if (dictValue.getValue().equals("EA")) {
                    packageDetail.setCdprIsMain("Y");
                    packageDetail.setCdprIsDefault("Y");
                } else {
                    packageDetail.setCdprIsMain("N");
                    packageDetail.setCdprIsDefault("N");
                }
            }
            packageDetail.setCdprIsReview("N");
            packageDetail.setCdprIsPackBox("N");
            packageDetail.setCdprIsLableIn("N");
            packageDetail.setCdprIsLableOut("N");
            result.add(packageDetail);
        }
        return result;
    }

    public List<OmPackageRelation> findByPackage(String pmCode, String orgId) {
        OmPackageRelation relation = new OmPackageRelation();
        relation.setCdprCdpaPmCode(pmCode);
        relation.setOrgId(orgId);
        return this.findList(relation);
    }

    public OmPackageRelation findByPackageUom(String packageCode, String uom, String orgId) {
        return mapper.findByPackageUom(packageCode, uom, orgId);
    }

    @Transactional
    public void deleteByPmCode(String cdpaPmCode) {
        mapper.deleteByPmCode(cdpaPmCode);
    }

    @Transactional
    public void remove(String packCode, String orgId) {
        mapper.remove(packCode, orgId);
    }

    @Transactional
    public void batchInsert(List<OmPackageRelation> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}