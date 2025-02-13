package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.oms.basic.entity.*;
import com.yunyou.modules.oms.basic.entity.extend.OmBusinessServiceScopeEntity;
import com.yunyou.modules.oms.basic.service.*;
import com.yunyou.modules.sys.common.entity.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SyncPlatformDataToOmsService {
    @Autowired
    private OmCustomerService omCustomerService;
    @Autowired
    private OmItemService omItemService;
    @Autowired
    private OmItemBarcodeService omItemBarcodeService;
    @Autowired
    private OmPackageService omPackageService;
    @Autowired
    private OmPackageRelationService omPackageRelationService;
    @Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;
    @Autowired
    private OmBusinessServiceScopeService omBusinessServiceScopeService;
    @Autowired
    private OmCarrierServiceScopeService omCarrierServiceScopeService;
    @Autowired
    private OmClerkService omClerkService;
    @Autowired
    private OmSkuClassificationService omSkuClassificationService;

    /**
     * 同步客户
     */
    @Transactional
    public void sync(SysOmsCustomer entity, String orgId) {
        omCustomerService.remove(entity.getEbcuCustomerNo(), orgId);

        OmCustomer newEntity = new OmCustomer();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        omCustomerService.save(newEntity);
    }

    /**
     * 同步商品
     */
    @Transactional
    public void sync(SysOmsItem entity, String orgId) {
        omItemService.remove(entity.getOwnerCode(), entity.getSkuCode(), orgId);

        OmItem newEntity = new OmItem();
        List<OmItemBarcode> barcodes = Lists.newArrayList();
        BeanUtils.copyProperties(entity, newEntity);
        for (SysOmsItemBarcode o : entity.getOmItemBarcodeList()) {
            OmItemBarcode omItemBarcode = new OmItemBarcode();
            BeanUtils.copyProperties(o, omItemBarcode);
            omItemBarcode.setId("");
            omItemBarcode.setIsNewRecord(false);
            omItemBarcode.setOrgId(orgId);
            barcodes.add(omItemBarcode);
        }
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        newEntity.setOmItemBarcodeList(barcodes);
        omItemService.save(newEntity);
    }

    /**
     * 同步包装
     */
    @Transactional
    public void sync(SysOmsPackage entity, String orgId) {
        omPackageService.remove(entity.getCdpaCode(), orgId);

        OmPackage newEntity = new OmPackage();
        BeanUtils.copyProperties(entity, newEntity);
        List<OmPackageRelation> relations = Lists.newArrayList();
        for (SysOmsPackageRelation o : entity.getPackageDetailList()) {
            OmPackageRelation omPackageRelation = new OmPackageRelation();
            BeanUtils.copyProperties(o, omPackageRelation);
            omPackageRelation.setId("");
            omPackageRelation.setIsNewRecord(false);
            omPackageRelation.setOrgId(orgId);
            relations.add(omPackageRelation);
        }
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setPackageDetailList(relations);
        newEntity.setOrgId(orgId);
        omPackageService.save(newEntity);
    }

    /**
     * 同步业务订单类型关系
     */
    @Transactional
    public void sync(SysOmsBusinessOrderTypeRelation entity, String orgId) {
        omBusinessOrderTypeRelationService.remove(entity.getOldBusinessOrderType(), entity.getOldOrderType(), entity.getOldPushSystem(), orgId);

        OmBusinessOrderTypeRelation newEntity = new OmBusinessOrderTypeRelation();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        omBusinessOrderTypeRelationService.save(newEntity);
    }

    /**
     * 同步业务服务范围
     */
    @Transactional
    public void sync(SysOmsBusinessServiceScope entity, String orgId) {
        omBusinessServiceScopeService.remove(entity.getGroupCode(), orgId);

        OmBusinessServiceScopeEntity newEntity = new OmBusinessServiceScopeEntity();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        omBusinessServiceScopeService.saveEntity(newEntity);
    }

    /**
     * 同步承运商服务范围
     */
    @Transactional
    public void sync(SysOmsCarrierServiceScope entity, String orgId) {
        omCarrierServiceScopeService.remove(entity.getOldOwnerCode(), entity.getOldCarrierCode(), entity.getOldGroupCode(), orgId);

        OmCarrierServiceScope newEntity = new OmCarrierServiceScope();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        omCarrierServiceScopeService.save(newEntity);
    }

    /**
     * 同步业务员
     */
    @Transactional
    public void sync(SysOmsClerk entity, String orgId) {
        omClerkService.remove(entity.getClerkCode(), orgId);

        OmClerk newEntity = new OmClerk();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        omClerkService.save(newEntity);
    }

    /**
     * 同步商品分类
     */
    @Transactional
    public void sync(SysCommonSkuClassification entity, String orgId) {
        omSkuClassificationService.remove(entity.getCode(), orgId);

        OmSkuClassification newEntity = new OmSkuClassification();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        omSkuClassificationService.save(newEntity);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysOmsCustomer... list) {
        List<OmCustomer> omCustomerList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysOmsCustomer entity : list) {
                OmCustomer omCustomer = new OmCustomer();
                BeanUtils.copyProperties(entity, omCustomer);
                omCustomer.setId(IdGen.uuid());
                omCustomer.setUpdateDate(date);
                omCustomer.setOrgId(orgId);
                omCustomerList.add(omCustomer);
            }
        }
        omCustomerService.batchInsert(omCustomerList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysOmsItem... list) {
        List<OmItem> omItemList = Lists.newArrayList();
        List<OmItemBarcode> omItemBarcodeList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysOmsItem entity : list) {
                OmItem omItem = new OmItem();
                BeanUtils.copyProperties(entity, omItem);
                omItem.setId(IdGen.uuid());
                omItem.setUpdateDate(date);
                omItem.setOrgId(orgId);
                omItemList.add(omItem);

                for (SysOmsItemBarcode o : entity.getOmItemBarcodeList()) {
                    OmItemBarcode omItemBarcode = new OmItemBarcode();
                    BeanUtils.copyProperties(o, omItemBarcode);
                    omItemBarcode.setId(IdGen.uuid());
                    omItemBarcode.setUpdateDate(date);
                    omItemBarcode.setItemId(omItem.getId());
                    omItemBarcode.setOrgId(orgId);
                    omItemBarcodeList.add(omItemBarcode);
                }
            }
        }
        omItemService.batchInsert(omItemList);
        omItemBarcodeService.batchInsert(omItemBarcodeList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysOmsPackage... list) {
        List<OmPackage> omPackageList = Lists.newArrayList();
        List<OmPackageRelation> omPackageRelationList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysOmsPackage entity : list) {
                OmPackage omPackage = new OmPackage();
                BeanUtils.copyProperties(entity, omPackage);
                omPackage.setId(IdGen.uuid());
                omPackage.setUpdateDate(date);
                omPackage.setPmCode(IdGen.uuid());
                omPackage.setOrgId(orgId);
                omPackageList.add(omPackage);

                for (SysOmsPackageRelation relation : entity.getPackageDetailList()) {
                    OmPackageRelation omPackageRelation = new OmPackageRelation();
                    BeanUtils.copyProperties(relation, omPackageRelation);
                    omPackageRelation.setId(IdGen.uuid());
                    omPackageRelation.setUpdateDate(date);
                    omPackageRelation.setPmCode(omPackage.getCdpaCode());
                    omPackageRelation.setCdprCdpaPmCode(omPackage.getPmCode());
                    omPackageRelation.setOrgId(orgId);
                    omPackageRelationList.add(omPackageRelation);
                }
            }
        }
        omPackageService.batchInsert(omPackageList);
        omPackageRelationService.batchInsert(omPackageRelationList);
    }

    @Transactional
    public void removeCustomer(String customerNo, String orgId) {
        omCustomerService.remove(customerNo, orgId);
    }

    @Transactional
    public void removeSku(String ownerCode, String skuCode, String orgId) {
        omItemService.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void removePackage(String packageCode, String orgId) {
        omPackageService.remove(packageCode, orgId);
    }

    @Transactional
    public void removeBusinessOrderTypeRelation(String businessOrderType, String orderType, String pushSystem, String orgId) {
        omBusinessOrderTypeRelationService.remove(businessOrderType, orderType, pushSystem, orgId);
    }

    @Transactional
    public void removeBusinessServiceScope(String groupCode, String orgId) {
        omBusinessServiceScopeService.remove(groupCode, orgId);
    }

    @Transactional
    public void removeCarrierServiceScope(String ownerCode, String carrierCode, String groupCode, String orgId) {
        omCarrierServiceScopeService.remove(ownerCode, carrierCode, groupCode, orgId);
    }

    @Transactional
    public void removeClerk(String clerkCode, String orgId) {
        omClerkService.remove(clerkCode, orgId);
    }

    @Transactional
    public void removeSkuClassification(String code, String orgId) {
        omSkuClassificationService.remove(code, orgId);
    }
}
