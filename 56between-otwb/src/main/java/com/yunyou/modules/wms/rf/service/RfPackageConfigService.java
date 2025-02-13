package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageRelationService;
import com.yunyou.modules.wms.rf.entity.WMSRF_CF_ProductionPackageConfig;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RfPackageConfigService {
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;

    /**
     * 获取包装规格配置信息 参数PackId为包装代码
     */
    public List<WMSRF_CF_ProductionPackageConfig> getPackageConfigs(String packCode, String orgId) {
        List<BanQinCdWhPackageRelation> items = cdWhPackageRelationService.findByPackCode(packCode, orgId);
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_CF_ProductionPackageConfig> packageConfigs = Lists.newArrayList();
            for (BanQinCdWhPackageRelation relation : items) {
                WMSRF_CF_ProductionPackageConfig packageConfig = new WMSRF_CF_ProductionPackageConfig();
                packageConfig.setPackageCode(relation.getCdprUnit());
                packageConfig.setPackageName(relation.getCdprDesc());
                packageConfig.setSeq(relation.getCdprSequencesNo());
                packageConfig.setContainerValue(relation.getCdprQuantity() == null ? 0 : relation.getCdprQuantity().intValue());
                packageConfigs.add(packageConfig);
            }
            return packageConfigs;
        } else {
            return null;
        }
    }
}
