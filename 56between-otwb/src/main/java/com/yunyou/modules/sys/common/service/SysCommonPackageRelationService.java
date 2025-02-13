package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysCommonPackageRelation;
import com.yunyou.modules.sys.common.mapper.SysCommonPackageRelationMapper;
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
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class SysCommonPackageRelationService extends CrudService<SysCommonPackageRelationMapper, SysCommonPackageRelation> {
    @Autowired
    private DictValueMapper dictValueMapper;

    @Transactional
    public void deleteByPmCode(String cdpaPmCode) {
        mapper.deleteByPmCode(cdpaPmCode);
    }

    @Transactional
    public List<SysCommonPackageRelation> initialList() {
        List<SysCommonPackageRelation> result = new ArrayList<>();
        List<DictValue> initialItemList = dictValueMapper.findInitList("package_list_initial", "", "");
        for (DictValue dictValue : initialItemList) {
            SysCommonPackageRelation detail = new SysCommonPackageRelation();
            detail.setCdprSequencesNo(dictValue.getSort());
            detail.setCdprUnit(dictValue.getValue());
            detail.setCdprUnitLevel(dictValue.getValue());
            detail.setCdprDesc(dictValue.getLabel());
            detail.setCdprQuantity(1D);
            if (StringUtils.isNotEmpty(dictValue.getValue())) {
                if (dictValue.getValue().equals("EA")) {
                    detail.setCdprIsMain("Y");
                    detail.setCdprIsDefault("Y");
                } else {
                    detail.setCdprIsMain("N");
                    detail.setCdprIsDefault("N");
                }
            }
            detail.setCdprIsReview("N");
            detail.setCdprIsPackBox("N");
            detail.setCdprIsLableIn("N");
            detail.setCdprIsLableOut("N");
            result.add(detail);
        }
        return result;
    }

    public List<SysCommonPackageRelation> findByPackage(String pmCode) {
        SysCommonPackageRelation relation = new SysCommonPackageRelation();
        relation.setCdprCdpaPmCode(pmCode);
        return this.findList(relation);
    }

    public List<SysCommonPackageRelation> findByPackageCode(String packageCode, String dataSet) {
        return mapper.findByPackageCode(packageCode, dataSet);
    }

    public SysCommonPackageRelation findByPackageUom(String packageCode, String uom, String dataSet) {
        return mapper.findByPackageUom(packageCode, uom, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysCommonPackageRelation> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }

}