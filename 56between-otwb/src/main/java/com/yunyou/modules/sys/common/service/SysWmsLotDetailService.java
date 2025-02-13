package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsLotDetail;
import com.yunyou.modules.sys.common.mapper.SysWmsLotDetailMapper;
import com.yunyou.modules.wms.common.entity.ConstantsVWMS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批次属性Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsLotDetailService extends CrudService<SysWmsLotDetailMapper, SysWmsLotDetail> {

    public Page<SysWmsLotDetail> findPage(Page<SysWmsLotDetail> page, SysWmsLotDetail entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsLotDetail> findGrid(Page<SysWmsLotDetail> page, SysWmsLotDetail entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    /**
     * 初始化
     */
    public List<SysWmsLotDetail> initialList() {
        List<SysWmsLotDetail> result = Lists.newArrayList();
        for (int i = 0; i < 12; i++) {
            SysWmsLotDetail sysWmsLotDetail = new SysWmsLotDetail();
            String str = "";
            if (i == 0) {// 生产日期
                str = ConstantsVWMS.PRODUCTION_DATE;
                sysWmsLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_DATE);
                sysWmsLotDetail.setInputControl("O");
                sysWmsLotDetail.setLotAtt("LOT_ATT01");
            } else if (i == 1) {// 失效日期
                str = ConstantsVWMS.EXPIRY_DATE;
                sysWmsLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_DATE);
                sysWmsLotDetail.setInputControl("O");
                sysWmsLotDetail.setLotAtt("LOT_ATT02");
            } else if (i == 2) {// 入库日期
                str = ConstantsVWMS.INSTOCKROOM_DATE;
                sysWmsLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_DATE);
                sysWmsLotDetail.setInputControl("O");
                sysWmsLotDetail.setLotAtt("LOT_ATT03");
            } else if (i == 3) {// 品质
                str = ConstantsVWMS.QUALITY;
                sysWmsLotDetail.setFieldType(ConstantsVWMS.BATCH_ATTRIBUTE_TYPE_COMBOBOX);
                sysWmsLotDetail.setInputControl("O");
                sysWmsLotDetail.setLotAtt("LOT_ATT04");
                sysWmsLotDetail.setKey("SYS_WM_QC_ATT");
            } else {// 其他批次属性
                str = ConstantsVWMS.BATCH_ATTRIBUTE_INFO + (i + 1);
                sysWmsLotDetail.setInputControl("F");
                sysWmsLotDetail.setLotAtt("LOT_ATT" + String.format("%02d", i + 1));
            }
            sysWmsLotDetail.setTitle(str);
            result.add(sysWmsLotDetail);
        }
        return result;
    }
}