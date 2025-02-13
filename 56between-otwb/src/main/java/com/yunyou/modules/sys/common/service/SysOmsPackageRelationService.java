package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysOmsPackageRelation;
import com.yunyou.modules.sys.common.mapper.SysOmsPackageRelationMapper;
import com.yunyou.modules.sys.entity.DictValue;
import com.yunyou.modules.sys.mapper.DictValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装明细Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsPackageRelationService extends CrudService<SysOmsPackageRelationMapper, SysOmsPackageRelation> {
    @Autowired
    private DictValueMapper dictValueMapper;

    public Page<SysOmsPackageRelation> findPage(Page<SysOmsPackageRelation> page, SysOmsPackageRelation entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void deleteByPmCode(String cdpaPmCode) {
        mapper.deleteByPmCode(cdpaPmCode);
    }

    @Transactional
    public List<SysOmsPackageRelation> initialList() {
        List<SysOmsPackageRelation> result = new ArrayList<>();
        List<DictValue> initialItemList = dictValueMapper.findInitList("package_list_initial", "", "");
        for (DictValue dictValue : initialItemList) {
            SysOmsPackageRelation packageDetail = new SysOmsPackageRelation();
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

    public List<SysOmsPackageRelation> findByPackage(String pmCode, String dataSet) {
        SysOmsPackageRelation relation = new SysOmsPackageRelation();
        relation.setCdprCdpaPmCode(pmCode);
        relation.setDataSet(dataSet);
        return this.findList(relation);
    }

    public SysOmsPackageRelation findByPackageUom(String packageCode, String uom, String dataSet) {
        return mapper.findByPackageUom(packageCode, uom, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysOmsPackageRelation> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }

    @Transactional
    public void remove(String packageCode, String dataSet) {
        mapper.remove(packageCode, dataSet);
    }
}