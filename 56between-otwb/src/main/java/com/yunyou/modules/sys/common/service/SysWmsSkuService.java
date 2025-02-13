package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsSku;
import com.yunyou.modules.sys.common.entity.SysWmsSkuBarcode;
import com.yunyou.modules.sys.common.entity.SysWmsSkuLoc;
import com.yunyou.modules.sys.common.entity.extend.SysWmsSkuEntity;
import com.yunyou.modules.sys.common.mapper.SysWmsSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsSkuService extends CrudService<SysWmsSkuMapper, SysWmsSku> {
    @Autowired
    private SysWmsSkuBarcodeService sysWmsSkuBarcodeService;
    @Autowired
    private SysWmsSkuLocService sysWmsSkuLocService;

    public SysWmsSkuEntity getEntity(String id) {
        SysWmsSkuEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setLocList(sysWmsSkuLocService.findList(new SysWmsSkuLoc(null, entity.getId(), entity.getDataSet())));
            entity.setBarcodeList(sysWmsSkuBarcodeService.findList(new SysWmsSkuBarcode(null, entity.getId(), entity.getDataSet())));
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public Page<SysWmsSkuEntity> findPage(Page page, SysWmsSkuEntity sysWmsSkuEntity) {
        dataRuleFilter(sysWmsSkuEntity);
        sysWmsSkuEntity.setPage(page);
        page.setList(mapper.findPage(sysWmsSkuEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysWmsSkuEntity> findGrid(Page page, SysWmsSkuEntity sysWmsSkuEntity) {
        dataRuleFilter(sysWmsSkuEntity);
        sysWmsSkuEntity.setPage(page);
        page.setList(mapper.findGrid(sysWmsSkuEntity));
        return page;
    }

    public SysWmsSku getByOwnerAndSkuCode(String ownerCode, String skuCode, String dataSet) {
        SysWmsSku entity = mapper.getByOwnerAndSkuCode(ownerCode, skuCode, dataSet);
        if (entity != null) {
            entity.setLocList(sysWmsSkuLocService.findList(new SysWmsSkuLoc(null, entity.getId(), entity.getDataSet())));
            entity.setBarcodeList(sysWmsSkuBarcodeService.findList(new SysWmsSkuBarcode(null, entity.getId(), entity.getDataSet())));
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(SysWmsSku entity) {
        sysWmsSkuLocService.deleteByHeaderId(entity.getId());
        sysWmsSkuBarcodeService.deleteByHeadId(entity.getId());
        super.delete(entity);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String dataSet) {
        sysWmsSkuLocService.remove(ownerCode, skuCode, dataSet);
        sysWmsSkuBarcodeService.remove(ownerCode, skuCode, dataSet);
        mapper.remove(ownerCode, skuCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysWmsSku> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysWmsSkuBarcode> skuBarcodeList = Lists.newArrayList();
        list.stream().map(SysWmsSku::getBarcodeList).filter(CollectionUtil::isNotEmpty).forEach(skuBarcodeList::addAll);
        sysWmsSkuBarcodeService.batchInsert(skuBarcodeList);

        List<SysWmsSkuLoc> skuLocList = Lists.newArrayList();
        list.stream().map(SysWmsSku::getLocList).filter(CollectionUtil::isNotEmpty).forEach(skuLocList::addAll);
        sysWmsSkuLocService.batchInsert(skuLocList);
    }
}