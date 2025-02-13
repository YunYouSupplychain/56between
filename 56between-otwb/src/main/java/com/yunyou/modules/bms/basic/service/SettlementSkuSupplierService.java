package com.yunyou.modules.bms.basic.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.SettlementSkuSupplier;
import com.yunyou.modules.bms.basic.mapper.SettlementSkuSupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结算商品-供应商Service
 *
 * @author Jianhua Liu
 * @version 2019-06-20
 */
@Service
@Transactional(readOnly = true)
public class SettlementSkuSupplierService extends CrudService<SettlementSkuSupplierMapper, SettlementSkuSupplier> {

    public List<SettlementSkuSupplier> findBySkuId(String skuId, String orgId) {
        return mapper.findBySkuId(skuId, orgId);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<SettlementSkuSupplier> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}