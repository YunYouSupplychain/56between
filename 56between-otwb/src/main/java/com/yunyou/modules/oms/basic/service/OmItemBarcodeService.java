package com.yunyou.modules.oms.basic.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmItemBarcode;
import com.yunyou.modules.oms.basic.mapper.OmItemBarcodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OmItemBarcodeService extends CrudService<OmItemBarcodeMapper, OmItemBarcode> {

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<OmItemBarcode> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}
