package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysTmsItem;
import com.yunyou.modules.sys.common.entity.SysTmsItemBarcode;
import com.yunyou.modules.sys.common.entity.extend.SysTmsItemEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsItemMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsItemService extends CrudService<SysTmsItemMapper, SysTmsItem> {
    @Autowired
    private SysTmsItemBarcodeService sysTmsItemBarcodeService;

    @SuppressWarnings("unchecked")
    public Page<SysTmsItemEntity> findPage(Page page, SysTmsItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsItemEntity> findGrid(Page page, SysTmsItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysTmsItemEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public SysTmsItem getByOwnerAndSku(String ownerCode, String skuCode, String dataSet) {
        return mapper.getByOwnerAndSku(ownerCode, skuCode, dataSet);
    }

    public void saveValidator(SysTmsItem sysTmsItem) {
        if (StringUtils.isBlank(sysTmsItem.getOwnerCode())) {
            throw new TmsException("货主不能为空");
        }
        if (StringUtils.isBlank(sysTmsItem.getSkuCode())) {
            throw new TmsException("商品编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsItem.getSkuName())) {
            throw new TmsException("商品名称不能为空");
        }
        List<SysTmsItem> list = findList(new SysTmsItem(sysTmsItem.getOwnerCode(), sysTmsItem.getSkuCode(), sysTmsItem.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsItem.getId()))) {
                throw new TmsException("货主[" + sysTmsItem.getOwnerCode() + "]商品[" + sysTmsItem.getSkuCode() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsItem sysTmsItem) {
        sysTmsItemBarcodeService.remove(sysTmsItem.getOwnerCode(), sysTmsItem.getSkuCode(), sysTmsItem.getDataSet());
        super.delete(sysTmsItem);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String dataSet) {
        sysTmsItemBarcodeService.remove(ownerCode, skuCode, dataSet);
        mapper.remove(ownerCode, skuCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysTmsItem> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysTmsItemBarcode> skuBarcodeList = Lists.newArrayList();
        list.stream().map(SysTmsItem::getBarcodeList).filter(CollectionUtil::isNotEmpty).forEach(skuBarcodeList::addAll);
        sysTmsItemBarcodeService.batchInsert(skuBarcodeList);
    }
}