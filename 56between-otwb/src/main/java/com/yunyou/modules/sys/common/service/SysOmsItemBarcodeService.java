package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysOmsItemBarcode;
import com.yunyou.modules.sys.common.mapper.SysOmsItemBarcodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysOmsItemBarcodeService extends CrudService<SysOmsItemBarcodeMapper, SysOmsItemBarcode> {

    @Transactional
    public void batchInsert(List<SysOmsItemBarcode> list) {
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
