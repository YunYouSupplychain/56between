package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysBmsSkuSupplier;
import com.yunyou.modules.sys.common.mapper.SysBmsSkuSupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结算商品-供应商Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsSkuSupplierService extends CrudService<SysBmsSkuSupplierMapper, SysBmsSkuSupplier> {

    public List<SysBmsSkuSupplier> findBySkuId(String skuId, String dataSet) {
        return mapper.findBySkuId(skuId, dataSet);
    }

    @Transactional
    public void deleteByHeadId(String skuId) {
        mapper.deleteByHeadId(skuId);
    }

    @Transactional
    public void batchInsert(List<SysBmsSkuSupplier> list) {
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