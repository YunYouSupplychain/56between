package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.DictValue;
import com.yunyou.modules.sys.mapper.DictValueMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhPackageRelationMapper;
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
public class BanQinCdWhPackageRelationService extends CrudService<BanQinCdWhPackageRelationMapper, BanQinCdWhPackageRelation> {
    @Autowired
    private DictValueMapper dictValueMapper;

    public Page<BanQinCdWhPackageRelation> findPage(Page<BanQinCdWhPackageRelation> page, BanQinCdWhPackageRelation banQinCdWhPackageRelation) {
        dataRuleFilter(banQinCdWhPackageRelation);
        banQinCdWhPackageRelation.setPage(page);
        page.setList(mapper.findPage(banQinCdWhPackageRelation));
        return page;
    }

    @Transactional
    public void deleteByPmCode(String cdpaPmCode) {
        mapper.deleteByPmCode(cdpaPmCode);
    }

    @Transactional
    public List<BanQinCdWhPackageRelation> initialList() {
        List<BanQinCdWhPackageRelation> result = new ArrayList<>();
        List<DictValue> initialItemList = dictValueMapper.findInitList("package_list_initial", "", "");
        for (DictValue dictValue : initialItemList) {
            BanQinCdWhPackageRelation packageDetail = new BanQinCdWhPackageRelation();
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

    /**
     * 描述：查询包装明细
     *
     * @param pmCode 包装外键
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinCdWhPackageRelation> findByPackage(String pmCode, String orgId) {
        BanQinCdWhPackageRelation relation = new BanQinCdWhPackageRelation();
        relation.setCdprCdpaPmCode(pmCode);
        relation.setOrgId(orgId);
        return findList(relation);
    }

    /**
     * 描述：查询包装明细
     * @param packageCode 包装代码
     * @param uom         单位
     * @param orgId
     */
    public BanQinCdWhPackageRelation findByPackageUom(String packageCode, String uom, String orgId) {
        return mapper.findByPackageUom(packageCode, uom, orgId);
    }

    public List<BanQinCdWhPackageRelation> findAllDetails(String orgId) {
        BanQinCdWhPackageRelation condition = new BanQinCdWhPackageRelation();
        condition.setOrgId(orgId);
        return this.findList(condition);
    }

    public List<BanQinCdWhPackageRelation> findByPackCode(String packCode, String orgId) {
        return mapper.findByPackCode(packCode, orgId);
    }

    @Transactional
    public void remove(String packCode, String orgId) {
        mapper.remove(packCode, orgId);
    }

    @Transactional
    public void batchInsert(List<BanQinCdWhPackageRelation> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}