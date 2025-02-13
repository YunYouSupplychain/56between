package com.yunyou.modules.interfaces.interactive.action;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.service.SyncPlatformDataToTmsService;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.service.*;
import com.yunyou.modules.sys.utils.SysCommonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SyncPlatformDataToTmsAction extends BaseAction {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;
    @Autowired
    private SyncPlatformDataToTmsService syncPlatformDataToTmsService;
    @Autowired
    private SysTmsTransportObjService sysTmsTransportObjService;
    @Autowired
    private SysTmsItemService sysTmsItemService;
    @Autowired
    private SysTmsItemBarcodeService sysTmsItemBarcodeService;
    @Autowired
    private SysTmsBusinessRouteService sysTmsBusinessRouteService;
    @Autowired
    private SysTmsCarrierRouteRelationService sysTmsCarrierRouteRelationService;
    @Autowired
    private SysTmsDriverService sysTmsDriverService;
    @Autowired
    private SysTmsFittingService sysTmsFittingService;
    @Autowired
    private SysTmsTransportEquipmentTypeService sysTmsTransportEquipmentTypeService;
    @Autowired
    private SysTmsTransportScopeService sysTmsTransportScopeService;
    @Autowired
    private SysTmsTransportObjScopeService sysTmsTransportObjScopeService;
    @Autowired
    private SysTmsVehicleService sysTmsVehicleService;
    @Autowired
    private SysTmsOutletRelationService sysTmsOutletRelationService;

    public void sync(SysCommonCustomer entity) {
        SysTmsTransportObj old = sysTmsTransportObjService.getByCode(entity.getCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysTmsTransportObj sysTmsTransportObj = SysCommonConverter.convertToTms(entity, old);
        if (isNew) {
            sysTmsTransportObjService.batchInsert(Collections.singletonList(sysTmsTransportObj));
        } else {
            sysTmsTransportObjService.save(sysTmsTransportObj);
        }
        this.sync(sysTmsTransportObj);
    }

    public void sync(SysCommonSku entity) {
        SysTmsItem old = sysTmsItemService.getByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysTmsItem sysTmsItem = SysCommonConverter.convertToTms(entity, old);
        if (isNew) {
            sysTmsItemService.batchInsert(Collections.singletonList(sysTmsItem));
        } else {
            sysTmsItemBarcodeService.remove(sysTmsItem.getOwnerCode(), sysTmsItem.getSkuCode(), sysTmsItem.getDataSet());
            sysTmsItemService.save(sysTmsItem);
            for (SysTmsItemBarcode sysTmsItemBarcode : sysTmsItem.getBarcodeList()) {
                sysTmsItemBarcodeService.save(sysTmsItemBarcode);
            }
        }
        this.sync(sysTmsItem);
    }

    public void sync(SysTmsTransportObj entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsItem entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsBusinessRoute entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsCarrierRouteRelation entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsDriver entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsFitting entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsTransportEquipmentType entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsTransportScope entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsTransportObjScope entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsVehicle entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(SysTmsOutletRelation entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.sync(entity, orgId));
    }

    public void sync(String dataSet, String orgId) {
        this.syncCustomer(dataSet, orgId);
        this.syncSku(dataSet, orgId);
        this.syncBusinessRoute(dataSet, orgId);
        this.syncCarrierRouteRelation(dataSet, orgId);
        this.syncDriver(dataSet, orgId);
        this.syncFitting(dataSet, orgId);
        this.syncTransportEquipmentType(dataSet, orgId);
        this.syncTransportScope(dataSet, orgId);
        this.syncTransportObjScope(dataSet, orgId);
        this.syncVehicle(dataSet, orgId);
        this.syncOutletRelation(dataSet, orgId);
    }

    public void syncCustomer(String dataSet, String orgId) {
        List<SysTmsTransportObj> list = sysTmsTransportObjService.findList(new SysTmsTransportObj(null, dataSet));
        for (SysTmsTransportObj entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncSku(String dataSet, String orgId) {
        List<SysTmsItem> list = sysTmsItemService.findList(new SysTmsItem(null, null, dataSet));
        for (SysTmsItem entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncBusinessRoute(String dataSet, String orgId) {
        List<SysTmsBusinessRoute> list = sysTmsBusinessRouteService.findList(new SysTmsBusinessRoute(null, dataSet));
        for (SysTmsBusinessRoute entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncCarrierRouteRelation(String dataSet, String orgId) {
        List<SysTmsCarrierRouteRelation> list = sysTmsCarrierRouteRelationService.findList(new SysTmsCarrierRouteRelation(null, dataSet));
        for (SysTmsCarrierRouteRelation entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncDriver(String dataSet, String orgId) {
        List<SysTmsDriver> list = sysTmsDriverService.findSync(new SysTmsDriver(null, null, dataSet));
        for (SysTmsDriver entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncFitting(String dataSet, String orgId) {
        List<SysTmsFitting> list = sysTmsFittingService.findList(new SysTmsFitting(null, dataSet));
        for (SysTmsFitting entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncTransportEquipmentType(String dataSet, String orgId) {
        List<SysTmsTransportEquipmentType> list = sysTmsTransportEquipmentTypeService.findSync(new SysTmsTransportEquipmentType(null, dataSet));
        for (SysTmsTransportEquipmentType entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncTransportScope(String dataSet, String orgId) {
        List<SysTmsTransportScope> list = sysTmsTransportScopeService.findSync(new SysTmsTransportScope(null, dataSet));
        for (SysTmsTransportScope entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncTransportObjScope(String dataSet, String orgId) {
        List<SysTmsTransportObjScope> list = sysTmsTransportObjScopeService.findList(new SysTmsTransportObjScope(null, dataSet));
        for (SysTmsTransportObjScope entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncVehicle(String dataSet, String orgId) {
        List<SysTmsVehicle> list = sysTmsVehicleService.findSync(new SysTmsVehicle(null, dataSet));
        for (SysTmsVehicle entity : list) {
            syncPlatformDataToTmsService.sync(entity, orgId);
        }
    }

    public void syncOutletRelation(String dataSet, String orgId) {
        syncInsert(Lists.newArrayList(orgId), sysTmsOutletRelationService.findSync(dataSet).toArray(new SysTmsOutletRelation[0]));
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysTmsTransportObj... list) {
        syncPlatformDataToTmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysTmsItem... list) {
        syncPlatformDataToTmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysTmsOutletRelation... list) {
        syncPlatformDataToTmsService.syncInsert(orgIds, list);
    }

    public void removeCarrierRouteRelation(String carrierCode, String code, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeCarrierRouteRelation(carrierCode, code, orgId));
    }

    public void removeDriver(String code, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeDriver(code, orgId));
    }

    public void removeFitting(String fittingCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeFitting(fittingCode, orgId));
    }

    public void removeEquipmentType(String transportEquipmentTypeCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeEquipmentType(transportEquipmentTypeCode, orgId));
    }

    public void removeTransportObjScope(String transportObjCode, String transportScopeCode, String transportScopeType, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeTransportObjScope(transportObjCode, transportScopeCode, transportScopeType, orgId));
    }

    public void removeTransportScope(String code, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeTransportScope(code, orgId));
    }

    public void removeVehicle(String carNo, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeVehicle(carNo, orgId));
    }

    public void removeBusinessRoute(String code, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeBusinessRoute(code, orgId));
    }

    public void removeOutletRelation(String code, String parentIds, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToTmsService.removeOutletRelation(code, parentIds, orgId));
    }
}
