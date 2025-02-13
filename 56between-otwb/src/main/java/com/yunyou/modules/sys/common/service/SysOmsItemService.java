package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.sys.common.entity.SysOmsItem;
import com.yunyou.modules.sys.common.entity.SysOmsItemBarcode;
import com.yunyou.modules.sys.common.entity.extend.SysOmsItemEntity;
import com.yunyou.modules.sys.common.mapper.SysOmsItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsItemService extends CrudService<SysOmsItemMapper, SysOmsItem> {
    @Autowired
    private SysOmsItemBarcodeService sysOmsItemBarcodeService;

    @SuppressWarnings("unchecked")
    public Page<SysOmsItemEntity> findPage(Page page, SysOmsItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysOmsItemEntity> findGrid(Page page, SysOmsItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysOmsItemEntity getEntity(String id) {
        SysOmsItemEntity entity = mapper.getEntity(id);
        if (null != entity) {
            entity.setOmItemBarcodeList(sysOmsItemBarcodeService.findList(new SysOmsItemBarcode(null, id)));
        }
        return entity;
    }

    public SysOmsItem getByOwnerAndSku(String ownerCode, String skuCode, String dataSet) {
        SysOmsItem sysOmsItem = mapper.getByOwnerAndSku(ownerCode, skuCode, dataSet);
        if (sysOmsItem != null && StringUtils.isNotBlank(sysOmsItem.getId())) {
            sysOmsItem.setOmItemBarcodeList(sysOmsItemBarcodeService.findList(new SysOmsItemBarcode(null, sysOmsItem.getId())));
        }
        return sysOmsItem;
    }

    public void saveValidator(SysOmsItem sysOmsItem) {
        SysOmsItem sku = getByOwnerAndSku(sysOmsItem.getOwnerCode(), sysOmsItem.getSkuCode(), sysOmsItem.getDataSet());
        if (sku != null && !sku.getId().equals(sysOmsItem.getId())) {
            throw new OmsException("商品编码【" + sysOmsItem.getSkuCode() + "】已存在");
        }
        List<SysOmsItemBarcode> sysOmsItemBarcodeList = sysOmsItem.getOmItemBarcodeList();
        if (sysOmsItemBarcodeList.stream().anyMatch(o -> StringUtils.isBlank(o.getBarcode()))) {
            throw new OmsException("条码不能为空");
        }
        if (sysOmsItemBarcodeList.stream().filter(o -> "Y".equals(o.getIsDefault())).count() > 1) {
            throw new OmsException("默认条码不能指定多个");
        }
    }

    @Override
    @Transactional
    public void save(SysOmsItem sysOmsItem) {
        super.save(sysOmsItem);
        for (SysOmsItemBarcode sysOmsItemBarcode : sysOmsItem.getOmItemBarcodeList()) {
            if (sysOmsItemBarcode.getId() == null) {
                continue;
            }
            sysOmsItemBarcode.setItemId(sysOmsItem.getId());
            sysOmsItemBarcodeService.save(sysOmsItemBarcode);
        }
    }

    @Override
    @Transactional
    public void delete(SysOmsItem sysOmsItem) {
        sysOmsItemBarcodeService.delete(new SysOmsItemBarcode(null, sysOmsItem.getId()));
        super.delete(sysOmsItem);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String dataSet) {
        sysOmsItemBarcodeService.remove(ownerCode, skuCode, dataSet);
        mapper.remove(ownerCode, skuCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysOmsItem> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysOmsItemBarcode> omsItemBarcodeList = Lists.newArrayList();
        list.stream().map(SysOmsItem::getOmItemBarcodeList).filter(CollectionUtil::isNotEmpty).forEach(omsItemBarcodeList::addAll);
        sysOmsItemBarcodeService.batchInsert(omsItemBarcodeList);
    }
}