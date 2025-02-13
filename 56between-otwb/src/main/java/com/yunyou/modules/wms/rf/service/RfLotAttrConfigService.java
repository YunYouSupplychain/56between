package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLotDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.rf.entity.WMSRF_CF_ProductionLotConfig;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RfLotAttrConfigService {
    @Autowired
    private BanQinCdWhLotDetailService cdWhLotDetailService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;

    /**
     * 获取批次属性配置信息
     */
    public List<WMSRF_CF_ProductionLotConfig> getLotAttrConfigs(String lotNum, String skuCode, String ownerCode, String orgId) {
        List<BanQinCdWhLotDetail> items = this.cdWhLotDetailService.getLotConfig(lotNum, ownerCode, skuCode, orgId);
        BanQinCdWhSku resultModel = cdWhSkuService.getByOwnerAndSkuCode(ownerCode, skuCode, orgId);
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_CF_ProductionLotConfig> lotConfigs = Lists.newArrayList();
            for (BanQinCdWhLotDetail detail : items) {
                WMSRF_CF_ProductionLotConfig lotConfig = new WMSRF_CF_ProductionLotConfig();
                lotConfig.setLotCode(detail.getLotAtt()); // 批次属性
                lotConfig.setLotType(detail.getFieldType()); // 字段显示类型
                lotConfig.setIsNeed(detail.getInputControl()); // 必填控制
                lotConfig.setLotTitle(detail.getTitle()); // 标签
                lotConfig.setLotRfTitle(detail.getTitle()); // RF标签
                if ("LOT_ATT04".equals(detail.getLotAtt()) && "Y".equals(resultModel.getIsQc())) {
                    lotConfig.setLotKey("SYS_WM_QUALIFY");
                } else if ("LOT_ATT04".equals(detail.getLotAtt()) && "N".equals(resultModel.getIsQc())) {
                    lotConfig.setLotKey("SYS_WM_QC_ATT");
                } else {
                    lotConfig.setLotKey(detail.getKey()); // 下拉框的cfgkey
                }
                lotConfigs.add(lotConfig);
            }
            return lotConfigs;
        } else {
            return null;
        }
    }
}
