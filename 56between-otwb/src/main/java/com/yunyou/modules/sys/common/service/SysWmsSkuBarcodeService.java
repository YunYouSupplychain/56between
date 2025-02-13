package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsSkuBarcode;
import com.yunyou.modules.sys.common.mapper.SysWmsSkuBarcodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品条码Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsSkuBarcodeService extends CrudService<SysWmsSkuBarcodeMapper, SysWmsSkuBarcode> {

    public List<SysWmsSkuBarcode> findPage(SysWmsSkuBarcode sysWmsSkuBarcode) {
        return mapper.findPage(sysWmsSkuBarcode);
    }

    @Transactional
    public void saveEntity(List<SysWmsSkuBarcode> list) {
        for (SysWmsSkuBarcode skuBarcode : list) {
            this.save(skuBarcode);
        }
    }

    @Transactional
    public void deleteByHeadId(String headId) {
        mapper.deleteByHeadId(headId);
    }

    @Transactional
    public void batchInsert(List<SysWmsSkuBarcode> list) {
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
    public void remove(String ownerCode, String skuCode, String dataSet) {
        mapper.remove(ownerCode, skuCode, dataSet);
    }
}