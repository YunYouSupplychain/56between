package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhSkuBarcodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品条码Service
 *
 * @author WMJ
 * @version 2019-10-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhSkuBarcodeService extends CrudService<BanQinCdWhSkuBarcodeMapper, BanQinCdWhSkuBarcode> {

    @Transactional
    public void saveEntity(List<BanQinCdWhSkuBarcode> list) {
        for (BanQinCdWhSkuBarcode skuBarcode : list) {
            this.save(skuBarcode);
        }
    }

    public List<BanQinCdWhSkuBarcode> findByBarcode(String ownerCode, String skuCode, String barcode, String orgId) {
        BanQinCdWhSkuBarcode condition = new BanQinCdWhSkuBarcode();
        condition.setOwnerCode(ownerCode);
        condition.setSkuCode(skuCode);
        condition.setBarcode(barcode);
        condition.setOrgId(orgId);
        return this.findList(condition);
    }

    @Transactional
    public void deleteByHeadId(String headId){
        mapper.deleteByHeadId(headId);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<BanQinCdWhSkuBarcode> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}