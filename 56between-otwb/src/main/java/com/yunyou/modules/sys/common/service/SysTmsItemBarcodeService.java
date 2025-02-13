package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysTmsItemBarcode;
import com.yunyou.modules.sys.common.mapper.SysTmsItemBarcodeMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品条码信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsItemBarcodeService extends CrudService<SysTmsItemBarcodeMapper, SysTmsItemBarcode> {

    public void saveValidator(SysTmsItemBarcode sysTmsItemBarcode) {
        if (StringUtils.isBlank(sysTmsItemBarcode.getOwnerCode())) {
            throw new TmsException("货主编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsItemBarcode.getSkuCode())) {
            throw new TmsException("商品编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsItemBarcode.getBarcode())) {
            throw new TmsException("条码不能为空");
        }
        List<SysTmsItemBarcode> list = findList(new SysTmsItemBarcode(sysTmsItemBarcode.getOwnerCode(), sysTmsItemBarcode.getSkuCode(), sysTmsItemBarcode.getBarcode(), sysTmsItemBarcode.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsItemBarcode.getId()))) {
                throw new TmsException("条码[" + sysTmsItemBarcode.getBarcode() + "]已存在");
            }
        }
        List<SysTmsItemBarcode> list1 = findList(new SysTmsItemBarcode(sysTmsItemBarcode.getOwnerCode(), sysTmsItemBarcode.getSkuCode(), sysTmsItemBarcode.getDataSet()));
        if (CollectionUtil.isNotEmpty(list1)) {
            if (list1.stream().anyMatch(o -> !o.getId().equals(sysTmsItemBarcode.getId()) && "Y".equals(sysTmsItemBarcode.getIsDefault()))) {
                throw new TmsException("货主[" + sysTmsItemBarcode.getOwnerCode() + "]商品[" + sysTmsItemBarcode.getSkuCode() + "]已存在默认条码");
            }
        }

    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String dataSet) {
        mapper.remove(ownerCode, skuCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysTmsItemBarcode> list) {
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