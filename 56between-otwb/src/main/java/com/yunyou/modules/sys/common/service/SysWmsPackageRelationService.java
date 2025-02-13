package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsPackageRelation;
import com.yunyou.modules.sys.common.mapper.SysWmsPackageRelationMapper;
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
public class SysWmsPackageRelationService extends CrudService<SysWmsPackageRelationMapper, SysWmsPackageRelation> {
    @Autowired
    private DictValueMapper dictValueMapper;

    public Page<SysWmsPackageRelation> findPage(Page<SysWmsPackageRelation> page, SysWmsPackageRelation sysWmsPackageRelation) {
        dataRuleFilter(sysWmsPackageRelation);
        sysWmsPackageRelation.setPage(page);
        page.setList(mapper.findPage(sysWmsPackageRelation));
        return page;
    }

    @Transactional
    public void deleteByPmCode(String cdpaPmCode) {
        mapper.deleteByPmCode(cdpaPmCode);
    }

    @Transactional
    public List<SysWmsPackageRelation> initialList() {
        List<SysWmsPackageRelation> result = new ArrayList<>();
        List<DictValue> initialItemList = dictValueMapper.findInitList("package_list_initial", "", "");
        for (DictValue dictValue : initialItemList) {
            SysWmsPackageRelation packageDetail = new SysWmsPackageRelation();
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

    public List<SysWmsPackageRelation> findByPackage(String pmCode, String dataSet) {
        SysWmsPackageRelation relation = new SysWmsPackageRelation();
        relation.setCdprCdpaPmCode(pmCode);
        relation.setDataSet(dataSet);
        return findList(relation);
    }

    public List<SysWmsPackageRelation> findByPackCode(String packCode, String dataSet) {
        return mapper.findByPackCode(packCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysWmsPackageRelation> list) {
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