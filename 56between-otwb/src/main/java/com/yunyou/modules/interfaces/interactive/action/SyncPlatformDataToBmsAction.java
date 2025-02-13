package com.yunyou.modules.interfaces.interactive.action;

import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.service.SyncPlatformDataToBmsService;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.service.*;
import com.yunyou.modules.sys.utils.SysCommonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SyncPlatformDataToBmsAction extends BaseAction {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;
    @Autowired
    private SyncPlatformDataToBmsService syncPlatformDataToBmsService;
    @Autowired
    private SysCommonPackageRelationService sysCommonPackageRelationService;
    @Autowired
    private SysBmsCustomerService sysBmsCustomerService;
    @Autowired
    private SysBmsSettleObjectService sysBmsSettleObjectService;
    @Autowired
    private SysBmsSkuService sysBmsSkuService;
    @Autowired
    private SysBmsSkuSupplierService sysBmsSkuSupplierService;
    @Autowired
    private SysBmsInvoiceObjectService sysBmsInvoiceObjectService;
    @Autowired
    private SysBmsSubjectService sysBmsSubjectService;
    @Autowired
    private SysTmsCarrierRouteRelationService sysTmsCarrierRouteRelationService;
    @Autowired
    private SysCommonSkuClassificationService sysCommonSkuClassificationService;

    public void sync(SysCommonCustomer entity) {
        SysBmsCustomer old = sysBmsCustomerService.getByCode(entity.getCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysBmsCustomer sysBmsCustomer = SysCommonConverter.convertToBms(entity, old);
        if (isNew) {
            sysBmsCustomerService.batchInsert(Collections.singletonList(sysBmsCustomer));
            if (sysBmsCustomer.getEbcuType().contains(CustomerType.SETTLEMENT.getCode())) {
                SysBmsSettleObject oldSettle = sysBmsSettleObjectService.getByCode(sysBmsCustomer.getEbcuCustomerNo(), sysBmsCustomer.getDataSet());
                sysBmsSettleObjectService.save(SysCommonConverter.convert(sysBmsCustomer, oldSettle));
            }
        } else {
            sysBmsCustomerService.save(sysBmsCustomer);
        }
        this.sync(sysBmsCustomer);
    }

    public void sync(SysCommonSku entity) {
        SysBmsSku old = sysBmsSkuService.getByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getDataSet());
        List<SysCommonPackageRelation> relations = sysCommonPackageRelationService.findByPackageCode(entity.getPackCode(), entity.getDataSet());
        boolean isNew = old == null;
        SysBmsSku sysBmsSku = SysCommonConverter.convertToBms(entity, relations, old);
        if (isNew) {
            sysBmsSkuService.batchInsert(Collections.singletonList(sysBmsSku));
        } else {
            sysBmsSkuSupplierService.deleteByHeadId(sysBmsSku.getId());
            sysBmsSkuService.save(sysBmsSku);
        }
        this.sync(sysBmsSku);
    }

    public void sync(SysBmsCustomer entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(SysBmsSettleObject entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(SysBmsSku entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(SysBmsInvoiceObject entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(SysBmsSubject entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(SysTmsCarrierRouteRelation entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(SysCommonSkuClassification entity) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entity.getDataSet());
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.sync(entity, orgId));
    }

    public void sync(String dataSet, String orgId) {
        this.syncCustomer(dataSet, orgId);
        this.syncSettle(dataSet, orgId);
        this.syncSku(dataSet, orgId);
        this.syncInvoiceObject(dataSet, orgId);
        this.syncSubject(dataSet, orgId);
        this.syncRoute(dataSet, orgId);
    }

    public void syncCustomer(String dataSet, String orgId) {
        List<SysBmsCustomer> list = sysBmsCustomerService.findList(new SysBmsCustomer(null, dataSet));
        for (SysBmsCustomer entity : list) {
            syncPlatformDataToBmsService.sync(entity, orgId);
        }
    }

    public void syncSettle(String dataSet, String orgId) {
        List<SysBmsSettleObject> list = sysBmsSettleObjectService.findList(new SysBmsSettleObject(null, dataSet));
        for (SysBmsSettleObject entity : list) {
            syncPlatformDataToBmsService.sync(entity, orgId);
        }
    }

    public void syncSku(String dataSet, String orgId) {
        List<SysBmsSku> list = sysBmsSkuService.findList(new SysBmsSku(null, dataSet));
        for (SysBmsSku entity : list) {
            syncPlatformDataToBmsService.sync(sysBmsSkuService.get(entity.getId()), orgId);
        }
    }

    public void syncInvoiceObject(String dataSet, String orgId) {
        List<SysBmsInvoiceObject> list = sysBmsInvoiceObjectService.findSync(new SysBmsInvoiceObject(null, dataSet));
        for (SysBmsInvoiceObject entity : list) {
            syncPlatformDataToBmsService.sync(entity, orgId);
        }
    }

    public void syncSubject(String dataSet, String orgId) {
        List<SysBmsSubject> list = sysBmsSubjectService.findList(new SysBmsSubject(null, dataSet));
        for (SysBmsSubject entity : list) {
            syncPlatformDataToBmsService.sync(entity, orgId);
        }
    }

    public void syncRoute(String dataSet, String orgId) {
        List<SysTmsCarrierRouteRelation> list = sysTmsCarrierRouteRelationService.findList(new SysTmsCarrierRouteRelation(null, dataSet));
        for (SysTmsCarrierRouteRelation entity : list) {
            syncPlatformDataToBmsService.sync(entity, orgId);
        }
    }

    public void syncSkuClassification(String dataSet, String orgId) {
        List<SysCommonSkuClassification> list = sysCommonSkuClassificationService.findList(new SysCommonSkuClassification(null, dataSet));
        for (SysCommonSkuClassification entity : list) {
            syncPlatformDataToBmsService.sync(entity, orgId);
        }
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysBmsCustomer... list) {
        syncPlatformDataToBmsService.syncInsert(orgIds, list);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(List<String> orgIds, SysBmsSku... list) {
        syncPlatformDataToBmsService.syncInsert(orgIds, list);
    }

    public void removeFormula(String formulaCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.removeFormula(formulaCode, orgId));
    }

    public void removeInvoiceObject(String code, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.removeInvoiceObject(code, orgId));
    }

    public void removeRoute(String carrierCode, String routeCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.removeRoute(carrierCode, routeCode, orgId));
    }

    public void removeSettleObject(String settleCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.removeSettleObject(settleCode, orgId));
    }

    public void removeSubject(String subjectCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.removeSubject(subjectCode, orgId));
    }

    public void removeTerms(String termsCode, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) return;
        orgIds.forEach(orgId -> syncPlatformDataToBmsService.removeTerms(termsCode, orgId));
    }
}
