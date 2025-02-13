package com.yunyou.modules.wms.basicdata.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhLotDetailMapper;
import com.yunyou.modules.wms.common.entity.ConstantsVWMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 批次属性Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhLotDetailService extends CrudService<BanQinCdWhLotDetailMapper, BanQinCdWhLotDetail> {
    @Autowired
    @Lazy
    private BanQinCdWhSkuService cdWhSkuService;

    public Page<BanQinCdWhLotDetail> findPage(Page<BanQinCdWhLotDetail> page, BanQinCdWhLotDetail banQinCdWhLotDetail) {
        dataRuleFilter(banQinCdWhLotDetail);
        banQinCdWhLotDetail.setPage(page);
        page.setList(mapper.findPage(banQinCdWhLotDetail));
        return page;
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    /**
     * 根据批次属性编码查询明细信息
     */
    public List<BanQinCdWhLotDetail> findByLotCode(String lotCode, String orgId) {
        BanQinCdWhLotDetail example = new BanQinCdWhLotDetail();
        example.setLotCode(lotCode);
        example.setOrgId(orgId);
        return findList(example);
    }

    /**
     * 初始化
     */
    public List<BanQinCdWhLotDetail> initialList() {
        List<BanQinCdWhLotDetail> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int j = i + 1;
            BanQinCdWhLotDetail cdWhLotDetail = new BanQinCdWhLotDetail();
            String str = "";
            // 生产日期
            if (i == 0) {
                str = ConstantsVWMS.PRODUCTION_DATE;
                cdWhLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_DATE);
                cdWhLotDetail.setInputControl("O");
                cdWhLotDetail.setLotAtt("LOT_ATT01");
            }
            // 失效日期
            if (i == 1) {
                str = ConstantsVWMS.EXPIRY_DATE;
                cdWhLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_DATE);
                cdWhLotDetail.setInputControl("O");
                cdWhLotDetail.setLotAtt("LOT_ATT02");
            }
            // 入库日期
            if (i == 2) {
                str = ConstantsVWMS.INSTOCKROOM_DATE;
                cdWhLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_DATE);
                cdWhLotDetail.setInputControl("O");
                cdWhLotDetail.setLotAtt("LOT_ATT03");
            }
            // 品质
            if (i == 3) {
                str = ConstantsVWMS.QUALITY;
                cdWhLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_COMBOBOX);
                cdWhLotDetail.setInputControl("O");
                cdWhLotDetail.setLotAtt("LOT_ATT04");
                cdWhLotDetail.setKey("SYS_WM_QC_ATT");
            }
            // 其他批次属性
            if (i >= 4) {
                str = ConstantsVWMS.BATCH_ATTRIBUTE_INFO + j;
                cdWhLotDetail.setInputControl("F");
                cdWhLotDetail.setLotAtt("LOT_ATT" + String.format("%02d", i + 1));
            }
            cdWhLotDetail.setTitle(str);
            result.add(cdWhLotDetail);
        }
        return result;
    }

    /**
     * 获取批次属性控件
     */
    public List<BanQinCdWhLotDetail> getInfo(String ownerCode, String skuCode, String orgId) {
        List<BanQinCdWhLotDetail> list = Lists.newArrayList();
        BanQinCdWhSku sku = cdWhSkuService.getByOwnerAndSkuCode(ownerCode, skuCode, orgId);
        if (null != sku) {
            list = this.findByLotCode(sku.getLotCode(), orgId);
        }
        return list;
    }

    /**
     * 获取批次属性配置 优先从skuCode取批次属性的ID 如果skuCode为空 则直接取lotNum
     */
    public List<BanQinCdWhLotDetail> getLotConfig(String lotNum, String ownerCode, String skuCode, String orgId) {
        if (StringUtils.isNotEmpty(skuCode) && StringUtils.isNotEmpty(ownerCode)) {
            BanQinCdWhSku resultModel = cdWhSkuService.getByOwnerAndSkuCode(ownerCode, skuCode, orgId);
            if (null != resultModel) {
                lotNum = resultModel.getLotCode();
            } else {
                return null;
            }
        }
        return this.findByLotCode(lotNum, orgId);
    }

    @Transactional
    public void remove(String lotCode, String orgId) {
        mapper.remove(lotCode, orgId);
    }
}