package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysCommonSkuLoc;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuLocEntity;
import com.yunyou.modules.sys.common.entity.extend.SysWmsSkuLocEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonSkuLocMapper;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品拣货位Service
 */
@Service
@Transactional(readOnly = true)
public class SysCommonSkuLocService extends CrudService<SysCommonSkuLocMapper, SysCommonSkuLoc> {

    @SuppressWarnings("unchecked")
    public Page<SysWmsSkuLocEntity> findPage(Page page, SysCommonSkuLocEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(SysCommonSkuLoc entity) {
        if (null == entity.getMaxLimit()) {
            entity.setMaxLimit(0d);
        }
        if (null == entity.getMinLimit()) {
            entity.setMinLimit(0d);
        }
        if (null == entity.getMinRp()) {
            entity.setMinRp(0d);
        }
        if (StringUtils.isEmpty(entity.getRpUom())) {
            entity.setRpUom("EA");
        }
        if (WmsConstants.NO.equals(entity.getIsOverAlloc()) && WmsConstants.NO.equals(entity.getIsRpAlloc())
            && WmsConstants.NO.equals(entity.getIsOverRp()) && WmsConstants.NO.equals(entity.getIsFmRs())
            && WmsConstants.NO.equals(entity.getIsFmCs())) {
            entity.setIsFmRs(WmsConstants.YES);
        }
        super.save(entity);
    }
}