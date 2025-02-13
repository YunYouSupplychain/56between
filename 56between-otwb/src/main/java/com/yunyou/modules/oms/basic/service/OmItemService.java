package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmItem;
import com.yunyou.modules.oms.basic.entity.OmItemBarcode;
import com.yunyou.modules.oms.basic.entity.extend.OmItemEntity;
import com.yunyou.modules.oms.basic.mapper.OmItemMapper;
import com.yunyou.modules.oms.common.OmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品信息Service
 *
 * @author WMJ
 * @version 2019-04-15
 */
@Service
@Transactional(readOnly = true)
public class OmItemService extends CrudService<OmItemMapper, OmItem> {
    @Autowired
    private OmItemBarcodeService omItemBarcodeService;

    @SuppressWarnings("unchecked")
    public Page<OmItemEntity> findPage(Page page, OmItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    /**
     * 描述：商品标准弹出框数据
     */
    @SuppressWarnings("unchecked")
    public Page<OmItemEntity> findGrid(Page page, OmItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    /**
     * 描述：订单商品弹出框数据
     */
    @SuppressWarnings("unchecked")
    public Page<OmItemEntity> findSkuGrid(Page page, OmItemEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findSkuGrid(entity));
        return page;
    }

    public OmItemEntity getEntity(String id) {
        OmItemEntity entity = mapper.getEntity(id);
        if (null != entity) {
            entity.setOmItemBarcodeList(omItemBarcodeService.findList(new OmItemBarcode(null, id)));
        }
        return entity;
    }

    public OmItem getByOwnerAndSku(String ownerCode, String skuCode, String orgId) {
        OmItem omItem = mapper.getByOwnerAndSku(ownerCode, skuCode, orgId);
        if (omItem != null && StringUtils.isNotBlank(omItem.getId())) {
            omItem.setOmItemBarcodeList(omItemBarcodeService.findList(new OmItemBarcode(null, omItem.getId())));
        }
        return omItem;
    }

    @Transactional
    public void save(OmItem omItem) {
        List<OmItemBarcode> omItemBarcodeList = omItem.getOmItemBarcodeList();
        boolean isDefaultExist = false;
        for (OmItemBarcode omItemBarcode : omItemBarcodeList) {
            if (StringUtils.isBlank(omItemBarcode.getBarcode())) {
                throw new OmsException("条码不能为空");
            }
            if ("Y".equals(omItemBarcode.getIsDefault())) {
                if (isDefaultExist) {
                    throw new OmsException("默认条码不能指定多个");
                }
                isDefaultExist = true;
            }
        }
        super.save(omItem);
        for (OmItemBarcode omItemBarcode : omItem.getOmItemBarcodeList()) {
            if (omItemBarcode.getId() == null) {
                continue;
            }
            if (OmItemBarcode.DEL_FLAG_NORMAL.equals(omItemBarcode.getDelFlag())) {
                omItemBarcode.setItemId(omItem.getId());
                omItemBarcodeService.save(omItemBarcode);
            } else {
                omItemBarcodeService.delete(omItemBarcode);
            }
        }
    }

    @Transactional
    public void delete(OmItem omItem) {
        omItemBarcodeService.delete(new OmItemBarcode(null, omItem.getId()));
        super.delete(omItem);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        omItemBarcodeService.remove(ownerCode, skuCode, orgId);
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<OmItem> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}