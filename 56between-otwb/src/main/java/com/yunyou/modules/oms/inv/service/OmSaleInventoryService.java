package com.yunyou.modules.oms.inv.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.inv.entity.OmSaleInventory;
import com.yunyou.modules.oms.inv.entity.OmSaleInventoryEntity;
import com.yunyou.modules.oms.inv.mapper.OmSaleInventoryMapper;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 销售库存Service
 *
 * @author Jianhua Liu
 * @version 2019-05-09
 */
@Service
@Transactional(readOnly = true)
public class OmSaleInventoryService extends CrudService<OmSaleInventoryMapper, OmSaleInventory> {
    @Autowired
    private OmSaleInventoryHistoryService omSaleInventoryHistoryService;

    @SuppressWarnings("unchecked")
    public Page<OmSaleInventoryEntity> findInvPage(Page page, OmSaleInventoryEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        List<OmSaleInventoryEntity> list = mapper.findInv(entity);
        page.setList(list);
        return page;
    }

    @Transactional
    public void updateAllocQty(String owner, String skuCode, double addAllocQty, String warehouse, String orgId, String operate, String orderNo) {
        OmSaleInventory inventory = this.findByOwnerAndSku(owner, skuCode, warehouse, orgId);
        if (inventory == null) {
            inventory = new OmSaleInventory();
            inventory.setOwner(owner);
            inventory.setSkuCode(skuCode);
            inventory.setWarehouse(warehouse);
            inventory.setAllocQty(addAllocQty);
            inventory.setShipmentQty(0d);
            inventory.setOrgId(orgId);
            this.save(inventory);
            omSaleInventoryHistoryService.saveHistory(0d, addAllocQty, addAllocQty, inventory, operate, orderNo);
        } else {
            mapper.updateAllocQty(owner, skuCode, addAllocQty, warehouse, orgId, inventory.getRecVer());
            omSaleInventoryHistoryService.saveHistory(inventory.getAllocQty(), addAllocQty, BigDecimalUtil.add(inventory.getAllocQty(), addAllocQty), inventory, operate, orderNo);
        }
        if (inventory.getAllocQty() + addAllocQty <= 0) {
            this.delete(inventory);
        }
    }

    public OmSaleInventory findByOwnerAndSku(String owner, String skuCode, String warehouse, String orgId) {
        OmSaleInventory inventory = new OmSaleInventory();
        inventory.setWarehouse(warehouse);
        inventory.setOwner(owner);
        inventory.setSkuCode(skuCode);
        inventory.setOrgId(orgId);
        List<OmSaleInventory> list = findList(inventory);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public double getAvailableQty(String ownerCode, String skuCode, String warehouse) {
        double availableQty = 0D;
        // WMS库存可用数量（不扣除分配数量，在销售库存数量中扣除）
        OmSaleInventoryEntity omSkuInv = mapper.findOmSkuInv(ownerCode, skuCode, warehouse);
        if (omSkuInv != null && omSkuInv.getQtyAvailable() != null) {
            availableQty = omSkuInv.getQtyAvailable();
        }
        // 扣除销售库存已分配数量未发运数量
        OmSaleInventory omSaleInventory = findByOwnerAndSku(ownerCode, skuCode, warehouse, null);
        if (omSaleInventory != null) {
            double allocQty = omSaleInventory.getAllocQty() == null ? 0D : omSaleInventory.getAllocQty();
            double shipmentQty = omSaleInventory.getShipmentQty() == null ? 0D : omSaleInventory.getShipmentQty();
            // 当数量为负时，作为0看待
            if (allocQty < 0) {
                allocQty = 0;
            }
            if (shipmentQty < 0) {
                shipmentQty = 0;
            }
            availableQty = availableQty - (allocQty - shipmentQty);
        }
        return availableQty;
    }

}