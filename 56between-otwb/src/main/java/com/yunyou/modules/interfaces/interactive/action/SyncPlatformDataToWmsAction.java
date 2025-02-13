package com.yunyou.modules.interfaces.interactive.action;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.service.SyncPlatformDataToWmsService;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.service.*;
import com.yunyou.modules.sys.utils.SysCommonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SyncPlatformDataToWmsAction extends BaseAction {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;
    @Autowired
    private SyncPlatformDataToWmsService syncPlatformDataToWmsService;
    @Autowired
    private SysWmsCustomerService sysWmsCustomerService;
    @Autowired
    private SysWmsSkuService sysWmsSkuService;
    @Autowired
    private SysWmsSkuBarcodeService sysWmsSkuBarcodeService;
    @Autowired
    private SysWmsSkuLocService sysWmsSkuLocService;
    @Autowired
    private SysWmsPackageService sysWmsPackageService;
    @Autowired
    private SysWmsPackageRelationService sysWmsPackageRelationService;
    @Autowired
    private SysWmsAreaService sysWmsAreaService;
    @Autowired
    private SysWmsZoneService sysWmsZoneService;
    @Autowired
    private SysWmsLocService sysWmsLocService;
    @Autowired
    private SysWmsCycleService sysWmsCycleService;
    @Autowired
    private SysWmsLotHeaderService sysWmsLotHeaderService;
    @Autowired
    private SysWmsQcItemHeaderService sysWmsQcItemHeaderService;
    @Autowired
    private SysWmsRulePaHeaderService sysWmsRulePaHeaderService;
    @Autowired
    private SysWmsRuleQcHeaderService sysWmsRuleQcHeaderService;
    @Autowired
    private SysWmsRuleRotationHeaderService sysWmsRuleRotationHeaderService;
    @Autowired
    private SysWmsRuleAllocHeaderService sysWmsRuleAllocHeaderService;
    @Autowired
    private SysWmsRuleWvHeaderService sysWmsRuleWvHeaderService;
    @Autowired
    private SysWmsRuleWvGroupHeaderService sysWmsRuleWvGroupHeaderService;
    @Autowired
    private SysWmsCarrierTypeRelationService sysWmsCarrierTypeRelationService;
    @Autowired
    private SysWmsWeighMachineInfoService sysWmsWeighMachineInfoService;
    @Autowired
    private SysCommonSkuClassificationService sysCommonSkuClassificationService;

    public void sync(SysCommonCustomer entity) {
        SysWmsCustomer old = sysWmsCustomerService.getByCode(entity.getCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysWmsCustomer sysWmsCustomer = SysCommonConverter.convertToWms(entity, old);
        if (isNew) {
            sysWmsCustomerService.batchInsert(Collections.singletonList(sysWmsCustomer));
        } else {
            sysWmsCustomerService.save(sysWmsCustomer);
        }
        this.sync(sysWmsCustomer);
    }

    public void sync(SysCommonSku entity) {
        SysWmsSku old = sysWmsSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysWmsSku sysWmsSku = SysCommonConverter.convertToWms(entity, old);
        if (isNew) {
            sysWmsSkuService.batchInsert(Collections.singletonList(sysWmsSku));
        } else {
            sysWmsSkuLocService.deleteByHeaderId(sysWmsSku.getId());
            sysWmsSkuBarcodeService.deleteByHeadId(sysWmsSku.getId());
            sysWmsSkuService.save(sysWmsSku);
            for (SysWmsSkuBarcode sysWmsSkuBarcode : sysWmsSku.getBarcodeList()) {
                sysWmsSkuBarcode.setHeaderId(sysWmsSku.getId());
                sysWmsSkuBarcodeService.save(sysWmsSkuBarcode);
            }
            for (SysWmsSkuLoc sysWmsSkuLoc : sysWmsSku.getLocList()) {
                sysWmsSkuLoc.setHeaderId(sysWmsSku.getId());
                sysWmsSkuLocService.save(sysWmsSkuLoc);
            }
        }
        this.sync(sysWmsSku);
    }

    public void sync(SysCommonPackage entity) {
        SysWmsPackage old = sysWmsPackageService.findByPackCode(entity.getCdpaCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysWmsPackage sysWmsPackage = SysCommonConverter.convertToWms(entity, old);
        if (isNew) {
            sysWmsPackageService.batchInsert(Collections.singletonList(sysWmsPackage));
        } else {
            sysWmsPackageRelationService.deleteByPmCode(sysWmsPackage.getPmCode());
            sysWmsPackageService.save(sysWmsPackage);
        }
        this.sync(sysWmsPackage);
    }

    public void sync(SysWmsCustomer entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsSku entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsPackage entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsLotHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsCycle entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsQcItemHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsArea entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsZone entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsLoc entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsRulePaHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsRuleQcHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsRuleRotationHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsRuleAllocHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsRuleWvHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsRuleWvGroupHeader entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsCarrierTypeRelation entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysWmsWeighMachineInfo entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(SysCommonSkuClassification entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.sync(entity, orgId));
    }

    public void sync(String dataSet, String orgId) {
        this.syncCustomer(dataSet, orgId);
        this.syncSku(dataSet, orgId);
        this.syncPackage(dataSet, orgId);
        this.syncLot(dataSet, orgId);
        this.syncCycle(dataSet, orgId);
        this.syncQcItem(dataSet, orgId);
        this.syncArea(dataSet, orgId);
        this.syncZone(dataSet, orgId);
        this.syncLoc(dataSet, orgId);
        this.syncRulePa(dataSet, orgId);
        this.syncRuleQc(dataSet, orgId);
        this.syncRuleRotation(dataSet, orgId);
        this.syncRuleAlloc(dataSet, orgId);
        this.syncRuleWv(dataSet, orgId);
        this.syncRuleWvGroup(dataSet, orgId);
        this.syncCarrierTypeRelation(dataSet, orgId);
        this.syncWeighMachineInfo(dataSet, orgId);
        this.syncSkuClassification(dataSet, orgId);
    }

    public void syncCustomer(String dataSet, String orgId) {
        List<SysWmsCustomer> list = sysWmsCustomerService.findList(new SysWmsCustomer(null, dataSet));
        for (SysWmsCustomer entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncSku(String dataSet, String orgId) {
        List<SysWmsSku> list = sysWmsSkuService.findList(new SysWmsSku(null, dataSet));
        for (SysWmsSku entity : list) {
            syncPlatformDataToWmsService.sync(sysWmsSkuService.getEntity(entity.getId()), orgId);
        }
    }

    public void syncPackage(String dataSet, String orgId) {
        List<SysWmsPackage> list = sysWmsPackageService.findList(new SysWmsPackage(null, dataSet));
        for (SysWmsPackage entity : list) {
            entity.setPackageDetailList(sysWmsPackageRelationService.findByPackage(entity.getPmCode(), dataSet));
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncLot(String dataSet, String orgId) {
        List<SysWmsLotHeader> list = sysWmsLotHeaderService.findSync(new SysWmsLotHeader(null, dataSet));
        for (SysWmsLotHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncCycle(String dataSet, String orgId) {
        List<SysWmsCycle> list = sysWmsCycleService.findList(new SysWmsCycle(null, dataSet));
        for (SysWmsCycle entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncQcItem(String dataSet, String orgId) {
        List<SysWmsQcItemHeader> list = sysWmsQcItemHeaderService.findSync(new SysWmsQcItemHeader(null, dataSet));
        for (SysWmsQcItemHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncArea(String dataSet, String orgId) {
        List<SysWmsArea> list = sysWmsAreaService.findList(new SysWmsArea(null, dataSet));
        for (SysWmsArea entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncZone(String dataSet, String orgId) {
        List<SysWmsZone> list = sysWmsZoneService.findList(new SysWmsZone(null, dataSet));
        for (SysWmsZone entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncLoc(String dataSet, String orgId) {
        List<SysWmsLoc> list = sysWmsLocService.findList(new SysWmsLoc(null, dataSet));
        for (SysWmsLoc entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncRulePa(String dataSet, String orgId) {
        List<SysWmsRulePaHeader> list = sysWmsRulePaHeaderService.findSync(new SysWmsRulePaHeader(null, dataSet));
        for (SysWmsRulePaHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncRuleQc(String dataSet, String orgId) {
        List<SysWmsRuleQcHeader> list = sysWmsRuleQcHeaderService.findSync(new SysWmsRuleQcHeader(null, dataSet));
        for (SysWmsRuleQcHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncRuleRotation(String dataSet, String orgId) {
        List<SysWmsRuleRotationHeader> list = sysWmsRuleRotationHeaderService.findSync(new SysWmsRuleRotationHeader(null, dataSet));
        for (SysWmsRuleRotationHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncRuleAlloc(String dataSet, String orgId) {
        List<SysWmsRuleAllocHeader> list = sysWmsRuleAllocHeaderService.findSync(new SysWmsRuleAllocHeader(null, dataSet));
        for (SysWmsRuleAllocHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncRuleWv(String dataSet, String orgId) {
        List<SysWmsRuleWvHeader> list = sysWmsRuleWvHeaderService.findSync(new SysWmsRuleWvHeader(null, dataSet));
        for (SysWmsRuleWvHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncRuleWvGroup(String dataSet, String orgId) {
        List<SysWmsRuleWvGroupHeader> list = sysWmsRuleWvGroupHeaderService.findSync(new SysWmsRuleWvGroupHeader(null, dataSet));
        for (SysWmsRuleWvGroupHeader entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncCarrierTypeRelation(String dataSet, String orgId) {
        List<SysWmsCarrierTypeRelation> list = sysWmsCarrierTypeRelationService.findList(new SysWmsCarrierTypeRelation(null, dataSet));
        for (SysWmsCarrierTypeRelation entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncWeighMachineInfo(String dataSet, String orgId) {
        List<SysWmsWeighMachineInfo> list = sysWmsWeighMachineInfoService.findList(new SysWmsWeighMachineInfo(null, dataSet));
        for (SysWmsWeighMachineInfo entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    public void syncSkuClassification(String dataSet, String orgId) {
        List<SysCommonSkuClassification> list = sysCommonSkuClassificationService.findList(new SysCommonSkuClassification(null, dataSet));
        for (SysCommonSkuClassification entity : list) {
            syncPlatformDataToWmsService.sync(entity, orgId);
        }
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysWmsCustomer... list) {
        syncPlatformDataToWmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysWmsSku... list) {
        syncPlatformDataToWmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysWmsPackage... list) {
        syncPlatformDataToWmsService.syncInsert(orgIds, list);
    }

    public void removeArea(String areaCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeArea(areaCode, orgId));
    }

    public void removeCarrierTypeRelation(String carrierCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeCarrierTypeRelation(carrierCode, orgId));
    }

    public void removeCycle(String cycleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeCycle(cycleCode, orgId));
    }

    public void removeLocation(String locCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeLocation(locCode, orgId));
    }

    public void removeLotAtt(String lotCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeLotAtt(lotCode, orgId));
    }

    public void removeQcItem(String itemGroupCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeQcItem(itemGroupCode, orgId));
    }

    public void removeRuleAlloc(String ruleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeRuleAlloc(ruleCode, orgId));
    }

    public void removeRulePa(String ruleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeRulePa(ruleCode, orgId));
    }

    public void removeRuleQc(String ruleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeRuleQc(ruleCode, orgId));
    }

    public void removeRuleRotation(String ruleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeRuleRotation(ruleCode, orgId));
    }

    public void removeRuleWvGroup(String groupCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeRuleWvGroup(groupCode, orgId));
    }

    public void removeRuleWv(String ruleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeRuleWv(ruleCode, orgId));
    }

    public void removeWeighMachine(String machineNo, String orgId, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        if (orgIds.stream().anyMatch(orgId::equals)) {
            syncPlatformDataToWmsService.removeWeighMachine(machineNo, orgId);
        }
    }

    public void removeZone(String zoneCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        orgIds.forEach(orgId -> syncPlatformDataToWmsService.removeZone(zoneCode, orgId));
    }
}
