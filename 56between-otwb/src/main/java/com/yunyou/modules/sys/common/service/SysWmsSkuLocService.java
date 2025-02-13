package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsSkuLoc;
import com.yunyou.modules.sys.common.entity.extend.SysWmsSkuLocEntity;
import com.yunyou.modules.sys.common.mapper.SysWmsSkuLocMapper;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品拣货位Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsSkuLocService extends CrudService<SysWmsSkuLocMapper, SysWmsSkuLoc> {

    @SuppressWarnings("unchecked")
    public Page<SysWmsSkuLocEntity> findPage(Page page, SysWmsSkuLocEntity sysWmsSkuLocEntity) {
        dataRuleFilter(sysWmsSkuLocEntity);
        sysWmsSkuLocEntity.setPage(page);
        page.setList(mapper.findPage(sysWmsSkuLocEntity));
        return page;
    }

    @Override
    @Transactional
    public void save(SysWmsSkuLoc entity) {
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

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String dataSet) {
        mapper.remove(ownerCode, skuCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysWmsSkuLoc> list) {
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