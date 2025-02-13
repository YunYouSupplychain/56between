package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.common.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 全基础资料同步删除服务
 */
@Service
@Transactional(readOnly = true)
public class SyncPlatformDataCommonService {
    @Autowired
    private SysOmsCustomerService sysOmsCustomerService;
    @Autowired
    private SysWmsCustomerService sysWmsCustomerService;
    @Autowired
    private SysTmsTransportObjService sysTmsTransportObjService;
    @Autowired
    private SysBmsCustomerService sysBmsCustomerService;
    @Autowired
    private SysOmsItemService sysOmsItemService;
    @Autowired
    private SysWmsSkuService sysWmsSkuService;
    @Autowired
    private SysTmsItemService sysTmsItemService;
    @Autowired
    private SysBmsSkuService sysBmsSkuService;
    @Autowired
    private SysOmsPackageService sysOmsPackageService;
    @Autowired
    private SysWmsPackageService sysWmsPackageService;
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;
    @Autowired
    private SyncPlatformDataToOmsService syncPlatformDataToOmsService;
    @Autowired
    private SyncPlatformDataToWmsService syncPlatformDataToWmsService;
    @Autowired
    private SyncPlatformDataToTmsService syncPlatformDataToTmsService;
    @Autowired
    private SyncPlatformDataToBmsService syncPlatformDataToBmsService;

    @Transactional
    public void removeCustomer(String customerNo, String dataSet) {
        sysOmsCustomerService.remove(customerNo, dataSet);
        sysWmsCustomerService.remove(customerNo, dataSet);
        sysTmsTransportObjService.remove(customerNo, dataSet);
        sysBmsCustomerService.remove(customerNo, dataSet);
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        for (String orgId : orgIds) {
            syncPlatformDataToOmsService.removeCustomer(customerNo, orgId);
            syncPlatformDataToWmsService.removeCustomer(customerNo, orgId);
            syncPlatformDataToTmsService.removeCustomer(customerNo, orgId);
            syncPlatformDataToBmsService.removeCustomer(customerNo, orgId);
        }
    }

    @Transactional
    public void removeSku(String ownerCode, String skuCode, String dataSet) {
        sysOmsItemService.remove(ownerCode, skuCode, dataSet);
        sysWmsSkuService.remove(ownerCode, skuCode, dataSet);
        sysTmsItemService.remove(ownerCode, skuCode, dataSet);
        sysBmsSkuService.remove(ownerCode, skuCode, dataSet);
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        for (String orgId : orgIds) {
            syncPlatformDataToOmsService.removeSku(ownerCode, skuCode, orgId);
            syncPlatformDataToWmsService.removeSku(ownerCode, skuCode, orgId);
            syncPlatformDataToTmsService.removeSku(ownerCode, skuCode, orgId);
            syncPlatformDataToBmsService.removeSku(ownerCode, skuCode, orgId);
        }
    }

    @Transactional
    public void removePackage(String packageCode, String dataSet) {
        sysOmsPackageService.remove(packageCode, dataSet);
        sysWmsPackageService.remove(packageCode, dataSet);
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        for (String orgId : orgIds) {
            syncPlatformDataToOmsService.removePackage(packageCode, orgId);
            syncPlatformDataToWmsService.removePackage(packageCode, orgId);
        }
    }

    public void removeSkuClassification(String code, String dataSet) {
        List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(dataSet);
        if (CollectionUtil.isEmpty(orgIds)) {
            return;
        }
        for (String orgId : orgIds) {
            syncPlatformDataToOmsService.removeSkuClassification(code, orgId);
            syncPlatformDataToWmsService.removeSkuClassification(code, orgId);
            syncPlatformDataToBmsService.removeSkuClassification(code, orgId);
        }
    }
}
