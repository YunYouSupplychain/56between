package com.yunyou.modules.interfaces.interactive.action;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.service.SyncPlatformDataToOmsService;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.service.*;
import com.yunyou.modules.sys.utils.SysCommonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class SyncPlatformDataToOmsAction extends BaseAction {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;
    @Autowired
    private SyncPlatformDataToOmsService syncPlatformDataToOmsService;
    @Autowired
    private SysOmsCustomerService sysOmsCustomerService;
    @Autowired
    private SysOmsItemService sysOmsItemService;
    @Autowired
    private SysOmsItemBarcodeService sysOmsItemBarcodeService;
    @Autowired
    private SysOmsPackageService sysOmsPackageService;
    @Autowired
    private SysOmsPackageRelationService sysOmsPackageRelationService;
    @Autowired
    private SysOmsBusinessOrderTypeRelationService sysOmsBusinessOrderTypeRelationService;
    @Autowired
    private SysOmsBusinessServiceScopeService sysOmsBusinessServiceScopeService;
    @Autowired
    private SysOmsCarrierServiceScopeService sysOmsCarrierServiceScopeService;
    @Autowired
    private SysOmsClerkService sysOmsClerkService;
    @Autowired
    private SysCommonSkuClassificationService sysCommonSkuClassificationService;

    public void sync(SysCommonCustomer entity) {
        SysOmsCustomer old = sysOmsCustomerService.getByCode(entity.getCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysOmsCustomer sysOmsCustomer = SysCommonConverter.convertToOms(entity, old);
        if (isNew) {
            sysOmsCustomerService.batchInsert(Collections.singletonList(sysOmsCustomer));
        } else {
            sysOmsCustomerService.save(sysOmsCustomer);
        }
        this.sync(sysOmsCustomer);
    }

    public void sync(SysCommonSku entity) {
        SysOmsItem old = sysOmsItemService.getByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysOmsItem sysOmsItem = SysCommonConverter.convertToOms(entity, old);
        if (isNew) {
            sysOmsItemService.batchInsert(Collections.singletonList(sysOmsItem));
        } else {
            sysOmsItemBarcodeService.delete(new SysOmsItemBarcode(null, sysOmsItem.getId()));
            sysOmsItemService.save(sysOmsItem);
        }
        this.sync(sysOmsItem);
    }

    public void sync(SysCommonPackage entity) {
        SysOmsPackage old = sysOmsPackageService.findByPackCode(entity.getCdpaCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysOmsPackage sysOmsPackage = SysCommonConverter.convertToOms(entity, old);
        if (isNew) {
            sysOmsPackageService.batchInsert(Collections.singletonList(sysOmsPackage));
        } else {
            sysOmsPackageRelationService.deleteByPmCode(sysOmsPackage.getPmCode());
            sysOmsPackageService.save(sysOmsPackage);
        }
        this.sync(sysOmsPackage);
    }

    public void sync(SysOmsCustomer entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysOmsItem entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysOmsPackage entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysOmsBusinessServiceScope entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysOmsCarrierServiceScope entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysOmsBusinessOrderTypeRelation entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysOmsClerk entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(SysCommonSkuClassification entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.sync(entity, orgId));
    }

    public void sync(String dataSet, String orgId) {
        this.syncCustomer(dataSet, orgId);
        this.syncSku(dataSet, orgId);
        this.syncPackage(dataSet, orgId);
        this.syncOrderTypeRelation(dataSet, orgId);
        this.syncBusinessServiceScope(dataSet, orgId);
        this.syncCarrierServiceScope(dataSet, orgId);
        this.syncClerk(dataSet, orgId);
        this.syncSkuClassification(dataSet, orgId);
    }

    public void syncCustomer(String dataSet, String orgId) {
        List<SysOmsCustomer> list = sysOmsCustomerService.findList(new SysOmsCustomer(null, dataSet));
        for (SysOmsCustomer entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncSku(String dataSet, String orgId) {
        List<SysOmsItem> list = sysOmsItemService.findList(new SysOmsItem(null, dataSet));
        for (SysOmsItem entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncPackage(String dataSet, String orgId) {
        List<SysOmsPackage> list = sysOmsPackageService.findList(new SysOmsPackage(null, dataSet));
        for (SysOmsPackage entity : list) {
            entity.setPackageDetailList(sysOmsPackageRelationService.findByPackage(entity.getPmCode(), dataSet));
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncOrderTypeRelation(String dataSet, String orgId) {
        List<SysOmsBusinessOrderTypeRelation> list = sysOmsBusinessOrderTypeRelationService.findList(new SysOmsBusinessOrderTypeRelation(null, dataSet));
        for (SysOmsBusinessOrderTypeRelation entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncBusinessServiceScope(String dataSet, String orgId) {
        List<SysOmsBusinessServiceScope> list = sysOmsBusinessServiceScopeService.findSync(new SysOmsBusinessServiceScope(null, dataSet));
        for (SysOmsBusinessServiceScope entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncCarrierServiceScope(String dataSet, String orgId) {
        List<SysOmsCarrierServiceScope> list = sysOmsCarrierServiceScopeService.findList(new SysOmsCarrierServiceScope(null, dataSet));
        for (SysOmsCarrierServiceScope entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncClerk(String dataSet, String orgId) {
        List<SysOmsClerk> list = sysOmsClerkService.findList(new SysOmsClerk(null, null, dataSet));
        for (SysOmsClerk entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    public void syncSkuClassification(String dataSet, String orgId) {
        List<SysCommonSkuClassification> list = sysCommonSkuClassificationService.findList(new SysCommonSkuClassification(null, dataSet));
        for (SysCommonSkuClassification entity : list) {
            syncPlatformDataToOmsService.sync(entity, orgId);
        }
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysOmsCustomer... list) {
        syncPlatformDataToOmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysOmsItem... list) {
        syncPlatformDataToOmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysOmsPackage... list) {
        syncPlatformDataToOmsService.syncInsert(orgIds, list);
    }

    @Transactional
    public void removeBusinessOrderTypeRelation(String businessOrderType, String orderType, String pushSystem, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.removeBusinessOrderTypeRelation(businessOrderType, orderType, pushSystem, orgId));
    }

    @Transactional
    public void removeBusinessServiceScope(String groupCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.removeBusinessServiceScope(groupCode, orgId));
    }

    @Transactional
    public void removeCarrierServiceScope(String ownerCode, String carrierCode, String groupCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.removeCarrierServiceScope(ownerCode, carrierCode, groupCode, orgId));
    }

    @Transactional
    public void removeClerk(String clerkCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToOmsService.removeClerk(clerkCode, orgId));
    }

}