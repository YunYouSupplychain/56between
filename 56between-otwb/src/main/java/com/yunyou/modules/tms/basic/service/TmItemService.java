package com.yunyou.modules.tms.basic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmItem;
import com.yunyou.modules.tms.basic.entity.extend.TmItemEntity;
import com.yunyou.modules.tms.basic.mapper.TmItemMapper;
import com.yunyou.modules.tms.common.TmsException;

/**
 * 商品信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmItemService extends CrudService<TmItemMapper, TmItem> {
    @Autowired
    private TmItemBarcodeService tmItemBarcodeService;

    public TmItemEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    @SuppressWarnings("unchecked")
    public Page<TmItemEntity> findPage(Page page, TmItem tmItem) {
        dataRuleFilter(tmItem);
        tmItem.setPage(page);
        page.setList(mapper.findPage(tmItem));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmItemEntity> findGrid(Page page, TmItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public TmItem getByOwnerAndSku(String ownerCode, String skuCode, String orgId) {
        return mapper.getByOwnerAndSku(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void delete(TmItem tmItem) {
        tmItemBarcodeService.remove(tmItem.getOwnerCode(), tmItem.getSkuCode(), tmItem.getOrgId());
        super.delete(tmItem);
    }

    @Transactional
    public void saveValidator(TmItem tmItem) {
        if (StringUtils.isBlank(tmItem.getOwnerCode())) {
            throw new TmsException("货主不能为空");
        }
        if (StringUtils.isBlank(tmItem.getSkuCode())) {
            throw new TmsException("商品编码不能为空");
        }
        if (StringUtils.isBlank(tmItem.getSkuName())) {
            throw new TmsException("商品名称不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmItem.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmItem> list = findList(new TmItem(tmItem.getOwnerCode(), tmItem.getSkuCode(), tmItem.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmItem.getId()))) {
                throw new TmsException("货主[" + tmItem.getOwnerCode() + "]商品[" + tmItem.getSkuCode() + "]已存在");
            }
        }
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        tmItemBarcodeService.remove(ownerCode, skuCode, orgId);
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<TmItem> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}