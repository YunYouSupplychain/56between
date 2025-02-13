package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysBmsSku;
import com.yunyou.modules.sys.common.entity.SysBmsSkuSupplier;
import com.yunyou.modules.sys.common.entity.extend.SysBmsSkuEntity;
import com.yunyou.modules.sys.common.mapper.SysBmsSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结算商品Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsSkuService extends CrudService<SysBmsSkuMapper, SysBmsSku> {
    @Autowired
    private SysBmsSkuSupplierService sysBmsSkuSupplierService;

    @Override
    public Page<SysBmsSku> findPage(Page<SysBmsSku> page, SysBmsSku entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysBmsSkuEntity> findGrid(Page page, SysBmsSkuEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Override
    public SysBmsSku get(String id) {
        SysBmsSku entity = super.get(id);
        if (entity != null) {
            entity.setSkuSuppliers(sysBmsSkuSupplierService.findBySkuId(entity.getId(), entity.getDataSet()));
        }
        return entity;
    }

    public SysBmsSkuEntity getEntity(String id) {
        SysBmsSkuEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setSkuSuppliers(sysBmsSkuSupplierService.findBySkuId(entity.getId(), entity.getDataSet()));
        }
        return entity;
    }

    public SysBmsSku getByOwnerAndSku(String ownerCode, String skuCode, String dataSet) {
        SysBmsSku entity = mapper.getByOwnerAndSku(ownerCode, skuCode, dataSet);
        if (entity != null) {
            entity.setSkuSuppliers(sysBmsSkuSupplierService.findBySkuId(entity.getId(), entity.getDataSet()));
        }
        return entity;
    }

    @Transactional
    public void save(SysBmsSkuEntity entity) {
        super.save(entity);
        for (SysBmsSkuSupplier skuSupplier : entity.getSkuSuppliers()) {
            if (skuSupplier.getId() == null) {
                continue;
            }
            if (SysBmsSkuSupplier.DEL_FLAG_NORMAL.equals(skuSupplier.getDelFlag())) {
                skuSupplier.setSkuId(entity.getId());
                skuSupplier.setDataSet(entity.getDataSet());
                sysBmsSkuSupplierService.save(skuSupplier);
            } else {
                sysBmsSkuSupplierService.delete(skuSupplier);
            }
        }
    }

    @Transactional
    public void delete(SysBmsSku entity) {
        sysBmsSkuSupplierService.deleteByHeadId(entity.getId());
        super.delete(entity);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String dataSet) {
        sysBmsSkuSupplierService.remove(ownerCode, skuCode, dataSet);
        mapper.remove(ownerCode, skuCode, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysBmsSku> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysBmsSkuSupplier> skuSupplierList = Lists.newArrayList();
        list.stream().map(SysBmsSku::getSkuSuppliers).filter(CollectionUtil::isNotEmpty).forEach(skuSupplierList::addAll);
        sysBmsSkuSupplierService.batchInsert(skuSupplierList);
    }
}