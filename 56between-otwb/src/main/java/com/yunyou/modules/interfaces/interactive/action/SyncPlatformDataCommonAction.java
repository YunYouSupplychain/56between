package com.yunyou.modules.interfaces.interactive.action;

import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.service.SyncPlatformDataCommonService;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.service.*;
import com.yunyou.modules.sys.utils.SysCommonConverter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SyncPlatformDataCommonAction extends BaseAction {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;
    @Autowired
    private SysCommonPackageRelationService sysCommonPackageRelationService;
    @Autowired
    private SyncPlatformDataCommonService syncPlatformDataCommonService;

    @Autowired
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

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

    /**
     * 供标准常规同步使用
     */
    public void syncAll(List<String> systems, String dataSet, String orgId) {
        logger.info("平台数据同步开始 [dataSet=" + dataSet + "&orgId=" + orgId + "]");
        for (String system : systems) {
            switch (SystemAliases.valueOf(system)) {
                case OMS:
                    syncPlatformDataToOmsAction.sync(dataSet, orgId);
                    break;
                case WMS:
                    syncPlatformDataToWmsAction.sync(dataSet, orgId);
                    break;
                case TMS:
                    syncPlatformDataToTmsAction.sync(dataSet, orgId);
                    break;
                case BMS:
                    syncPlatformDataToBmsAction.sync(dataSet, orgId);
                    break;
                default:
                    break;
            }
        }
        logger.info("平台数据同步结束 [dataSet=" + dataSet + "&orgId=" + orgId + "]");
    }

    public void syncCustomer(List<SysCommonCustomer> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        for (SysCommonCustomer entity : entities) {
            syncPlatformDataToOmsAction.sync(entity);
            syncPlatformDataToWmsAction.sync(entity);
            syncPlatformDataToTmsAction.sync(entity);
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    public void syncSku(List<SysCommonSku> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        for (SysCommonSku entity : entities) {
            syncPlatformDataToOmsAction.sync(entity);
            syncPlatformDataToWmsAction.sync(entity);
            syncPlatformDataToTmsAction.sync(entity);
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    public void syncPackage(List<SysCommonPackage> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        for (SysCommonPackage entity : entities) {
            syncPlatformDataToOmsAction.sync(entity);
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    public void syncSkuClassification(List<SysCommonSkuClassification> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        for (SysCommonSkuClassification entity : entities) {
            syncPlatformDataToOmsAction.sync(entity);
            syncPlatformDataToWmsAction.sync(entity);
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    public void syncInsert(SysCommonCustomer... entities) {
        Map<String, List<SysCommonCustomer>> map = Arrays.stream(entities).collect(Collectors.groupingBy(SysCommonCustomer::getDataSet));
        for (Map.Entry<String, List<SysCommonCustomer>> entry : map.entrySet()) {
            List<SysOmsCustomer> omsCustomerList = Lists.newArrayList();
            List<SysWmsCustomer> wmsCustomerList = Lists.newArrayList();
            List<SysTmsTransportObj> tmsTransportObjList = Lists.newArrayList();
            List<SysBmsCustomer> bmsCustomerList = Lists.newArrayList();

            for (SysCommonCustomer o : entry.getValue()) {
                omsCustomerList.add(SysCommonConverter.convertToOms(o, null));
                wmsCustomerList.add(SysCommonConverter.convertToWms(o, null));
                tmsTransportObjList.add(SysCommonConverter.convertToTms(o, null));
                bmsCustomerList.add(SysCommonConverter.convertToBms(o, null));
            }
            sysOmsCustomerService.batchInsert(omsCustomerList);
            sysWmsCustomerService.batchInsert(wmsCustomerList);
            sysTmsTransportObjService.batchInsert(tmsTransportObjList);
            sysBmsCustomerService.batchInsert(bmsCustomerList);

            List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entry.getKey());
            syncPlatformDataToOmsAction.syncInsert(orgIds, omsCustomerList.toArray(new SysOmsCustomer[0]));
            syncPlatformDataToWmsAction.syncInsert(orgIds, wmsCustomerList.toArray(new SysWmsCustomer[0]));
            syncPlatformDataToTmsAction.syncInsert(orgIds, tmsTransportObjList.toArray(new SysTmsTransportObj[0]));
            syncPlatformDataToBmsAction.syncInsert(orgIds, bmsCustomerList.toArray(new SysBmsCustomer[0]));
        }
    }

    public void syncInsert(SysCommonSku... entities) {
        Map<String, List<SysCommonSku>> map = Arrays.stream(entities).collect(Collectors.groupingBy(SysCommonSku::getDataSet));
        for (Map.Entry<String, List<SysCommonSku>> entry : map.entrySet()) {
            List<SysOmsItem> omsItemList = Lists.newArrayList();
            List<SysWmsSku> wmsSkuList = Lists.newArrayList();
            List<SysTmsItem> tmsItemList = Lists.newArrayList();
            List<SysBmsSku> bmsSkuList = Lists.newArrayList();

            for (SysCommonSku o : entry.getValue()) {
                omsItemList.add(SysCommonConverter.convertToOms(o, null));
                wmsSkuList.add(SysCommonConverter.convertToWms(o, null));
                tmsItemList.add(SysCommonConverter.convertToTms(o, null));
                bmsSkuList.add(SysCommonConverter.convertToBms(o, sysCommonPackageRelationService.findByPackageCode(o.getPackCode(), o.getDataSet()), null));
            }
            sysOmsItemService.batchInsert(omsItemList);
            sysWmsSkuService.batchInsert(wmsSkuList);
            sysTmsItemService.batchInsert(tmsItemList);
            sysBmsSkuService.batchInsert(bmsSkuList);

            List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entry.getKey());
            syncPlatformDataToOmsAction.syncInsert(orgIds, omsItemList.toArray(new SysOmsItem[0]));
            syncPlatformDataToWmsAction.syncInsert(orgIds, wmsSkuList.toArray(new SysWmsSku[0]));
            syncPlatformDataToTmsAction.syncInsert(orgIds, tmsItemList.toArray(new SysTmsItem[0]));
            syncPlatformDataToBmsAction.syncInsert(orgIds, bmsSkuList.toArray(new SysBmsSku[0]));
        }
    }

    public void syncInsert(SysCommonPackage... entities) {
        Map<String, List<SysCommonPackage>> map = Arrays.stream(entities).collect(Collectors.groupingBy(SysCommonPackage::getDataSet));
        for (Map.Entry<String, List<SysCommonPackage>> entry : map.entrySet()) {
            List<SysOmsPackage> omsPackageList = Lists.newArrayList();
            List<SysWmsPackage> wmsPackageList = Lists.newArrayList();

            for (SysCommonPackage o : entry.getValue()) {
                omsPackageList.add(SysCommonConverter.convertToOms(o, null));
                wmsPackageList.add(SysCommonConverter.convertToWms(o, null));
            }
            sysOmsPackageService.batchInsert(omsPackageList);
            sysWmsPackageService.batchInsert(wmsPackageList);

            List<String> orgIds = sysDataSetOrgRelationService.findOrgIdByDataSet(entry.getKey());
            syncPlatformDataToOmsAction.syncInsert(orgIds, omsPackageList.toArray(new SysOmsPackage[0]));
            syncPlatformDataToWmsAction.syncInsert(orgIds, wmsPackageList.toArray(new SysWmsPackage[0]));
        }
    }

    public void syncInsert(SysTmsOutletRelation... entities) {
        Map<String, List<SysTmsOutletRelation>> map = Arrays.stream(entities).collect(Collectors.groupingBy(SysTmsOutletRelation::getDataSet));
        for (Map.Entry<String, List<SysTmsOutletRelation>> entry : map.entrySet()) {
            syncPlatformDataToTmsAction.syncInsert(sysDataSetOrgRelationService.findOrgIdByDataSet(entry.getKey()), entities);
        }
    }

    public void removeCustomer(String customerNo, String dataSet) {
        syncPlatformDataCommonService.removeCustomer(customerNo, dataSet);
    }

    public void removeSku(String ownerCode, String skuCode, String dataSet) {
        syncPlatformDataCommonService.removeSku(ownerCode, skuCode, dataSet);
    }

    public void removePackage(String packageCode, String dataSet) {
        syncPlatformDataCommonService.removePackage(packageCode, dataSet);
    }

    public void removeSkuClassification(String code, String dataSet) {
        syncPlatformDataCommonService.removeSkuClassification(code, dataSet);
    }
}
