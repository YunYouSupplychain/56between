package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.bms.basic.entity.*;
import com.yunyou.modules.bms.basic.entity.extend.SettlementSkuEntity;
import com.yunyou.modules.bms.basic.service.*;
import com.yunyou.modules.sys.common.entity.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SyncPlatformDataToBmsService {
    @Autowired
    private BmsCustomerService bmsCustomerService;
    @Autowired
    private BmsSettleObjectService bmsSettleService;
    @Autowired
    private SettlementSkuService bmsSkuService;
    @Autowired
    private SettlementSkuSupplierService bmsSkuSupplierService;
    @Autowired
    private BmsInvoiceObjectService bmsInvoiceObjectService;
    @Autowired
    private BmsBillTermsService bmsBillTermsService;
    @Autowired
    private BmsBillSubjectService bmsBillSubjectService;
    @Autowired
    private BmsBillFormulaService bmsBillFormulaService;
    @Autowired
    private BmsCarrierRouteService bmsCarrierRouteService;
    @Autowired
    private BmsSkuClassificationService bmsSkuClassificationService;

    /**
     * 同步客户
     */
    @Transactional
    public void sync(SysBmsCustomer entity, String orgId) {
        bmsCustomerService.remove(entity.getEbcuCustomerNo(), orgId);

        BmsCustomer bmsCustomer = new BmsCustomer();
        BeanUtils.copyProperties(entity, bmsCustomer);
        bmsCustomer.setId(null);
        bmsCustomer.setIsNewRecord(false);
        bmsCustomer.setOrgId(orgId);
        bmsCustomerService.save(bmsCustomer);
    }

    /**
     * 同步结算方
     */
    @Transactional
    public void sync(SysBmsSettleObject entity, String orgId) {
        bmsSettleService.remove(entity.getSettleObjectCode(), orgId);

        BmsSettleObject bmsSettleObject = new BmsSettleObject();
        BeanUtils.copyProperties(entity, bmsSettleObject);
        bmsSettleObject.setId(null);
        bmsSettleObject.setIsNewRecord(false);
        bmsSettleObject.setOrgId(orgId);
        bmsSettleService.save(bmsSettleObject);
    }

    /**
     * 同步商品
     */
    @Transactional
    public void sync(SysBmsSku entity, String orgId) {
        bmsSkuService.remove(entity.getOwnerCode(), entity.getSkuCode(), orgId);

        SettlementSkuEntity settlementSku = new SettlementSkuEntity();
        List<SettlementSkuSupplier> skuSuppliers = Lists.newArrayList();
        for (SysBmsSkuSupplier o : entity.getSkuSuppliers()) {
            SettlementSkuSupplier settlementSkuSupplier = new SettlementSkuSupplier();
            BeanUtils.copyProperties(o, settlementSkuSupplier);
            settlementSkuSupplier.setId("");
            settlementSkuSupplier.setIsNewRecord(false);
            settlementSkuSupplier.setOrgId(orgId);
            skuSuppliers.add(settlementSkuSupplier);
        }
        BeanUtils.copyProperties(entity, settlementSku);
        settlementSku.setId(null);
        settlementSku.setIsNewRecord(false);
        settlementSku.setOrgId(orgId);
        settlementSku.setSkuSuppliers(skuSuppliers);
        bmsSkuService.save(settlementSku);
    }

    /**
     * 同步开票对象
     */
    @Transactional
    public void sync(SysBmsInvoiceObject entity, String orgId) {
        bmsInvoiceObjectService.remove(entity.getCode(), orgId);

        BmsInvoiceObject baseInvoiceObject = new BmsInvoiceObject();
        BeanUtils.copyProperties(entity, baseInvoiceObject);
        baseInvoiceObject.setId(null);
        baseInvoiceObject.setIsNewRecord(false);
        baseInvoiceObject.setOrgId(orgId);
        bmsInvoiceObjectService.save(baseInvoiceObject);
    }

    /**
     * 同步费用科目
     */
    @Transactional
    public void sync(SysBmsSubject entity, String orgId) {
        bmsBillSubjectService.remove(entity.getBillSubjectCode(), orgId);

        BmsBillSubject bmsBillSubject = new BmsBillSubject();
        BeanUtils.copyProperties(entity, bmsBillSubject);
        bmsBillSubject.setId(null);
        bmsBillSubject.setIsNewRecord(false);
        bmsBillSubject.setOrgId(orgId);
        bmsBillSubjectService.save(bmsBillSubject);
    }

    /**
     * 同步路由
     */
    @Transactional
    public void sync(SysTmsCarrierRouteRelation entity, String orgId) {
        bmsCarrierRouteService.remove(entity.getOldCarrierCode(), entity.getCode(), orgId);

        BmsCarrierRoute bmsCarrierRoute = new BmsCarrierRoute();
        bmsCarrierRoute.setCarrierCode(entity.getCarrierCode());
        bmsCarrierRoute.setRouteCode(entity.getCode());
        bmsCarrierRoute.setRouteName(entity.getName());
        bmsCarrierRoute.setStartAreaId(entity.getOriginId());
        bmsCarrierRoute.setEndAreaId(entity.getDestinationId());
        if (entity.getMileage() != null) {
            bmsCarrierRoute.setMileage(BigDecimal.valueOf(entity.getMileage()));
        }
        if (entity.getTime() != null) {
            bmsCarrierRoute.setTimeliness(BigDecimal.valueOf(entity.getTime()));
        }
        bmsCarrierRoute.setOrgId(orgId);
        bmsCarrierRouteService.save(bmsCarrierRoute);
    }

    /**
     * 同步商品分类
     */
    @Transactional
    public void sync(SysCommonSkuClassification entity, String orgId) {
        bmsSkuClassificationService.remove(entity.getCode(), orgId);

        BmsSkuClassification newEntity = new BmsSkuClassification();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        bmsSkuClassificationService.save(newEntity);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysBmsCustomer... list) {
        List<BmsCustomer> bmsCustomerList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysBmsCustomer entity : list) {
                BmsCustomer bmsCustomer = new BmsCustomer();
                BeanUtils.copyProperties(entity, bmsCustomer);
                bmsCustomer.setId(IdGen.uuid());
                bmsCustomer.setUpdateDate(date);
                bmsCustomer.setOrgId(orgId);
                bmsCustomerList.add(bmsCustomer);
            }
        }
        bmsCustomerService.batchInsert(bmsCustomerList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysBmsSku... list) {
        List<SettlementSku> settlementSkuList = Lists.newArrayList();
        List<SettlementSkuSupplier> skuSupplierList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysBmsSku entity : list) {
                SettlementSku settlementSku = new SettlementSku();
                BeanUtils.copyProperties(entity, settlementSku);
                settlementSku.setId(IdGen.uuid());
                settlementSku.setUpdateDate(date);
                settlementSku.setOrgId(orgId);
                settlementSkuList.add(settlementSku);
                for (SysBmsSkuSupplier o : entity.getSkuSuppliers()) {
                    SettlementSkuSupplier settlementSkuSupplier = new SettlementSkuSupplier();
                    BeanUtils.copyProperties(o, settlementSkuSupplier);
                    settlementSkuSupplier.setId(IdGen.uuid());
                    settlementSkuSupplier.setUpdateDate(date);
                    settlementSkuSupplier.setSkuId(settlementSku.getId());
                    settlementSkuSupplier.setOrgId(orgId);
                    skuSupplierList.add(settlementSkuSupplier);
                }
            }
        }
        bmsSkuService.batchInsert(settlementSkuList);
        bmsSkuSupplierService.batchInsert(skuSupplierList);
    }

    @Transactional
    public void removeCustomer(String customerNo, String orgId) {
        bmsCustomerService.remove(customerNo, orgId);
    }

    @Transactional
    public void removeSku(String ownerCode, String skuCode, String orgId) {
        bmsSkuService.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void removeFormula(String formulaCode, String orgId) {
        bmsBillFormulaService.remove(formulaCode);
    }

    @Transactional
    public void removeInvoiceObject(String code, String orgId) {
        bmsInvoiceObjectService.remove(code, orgId);
    }

    @Transactional
    public void removeRoute(String carrierCode, String routeCode, String orgId) {
        bmsCarrierRouteService.remove(carrierCode, routeCode, orgId);
    }

    @Transactional
    public void removeSettleObject(String settleCode, String orgId) {
        bmsSettleService.remove(settleCode, orgId);
    }

    @Transactional
    public void removeSubject(String subjectCode, String orgId) {
        bmsBillSubjectService.remove(subjectCode, orgId);
    }

    @Transactional
    public void removeTerms(String termsCode, String orgId) {
        bmsBillTermsService.remove(termsCode);
    }

    @Transactional
    public void removeSkuClassification(String code, String orgId) {
        bmsSkuClassificationService.remove(code, orgId);
    }
}
