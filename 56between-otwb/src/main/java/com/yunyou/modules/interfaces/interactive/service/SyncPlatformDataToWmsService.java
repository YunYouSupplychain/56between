package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.service.*;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfo;
import com.yunyou.modules.wms.weigh.service.BanQinWeighMachineInfoService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SyncPlatformDataToWmsService {
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinCdWhSkuLocService cdWhSkuLocService;
    @Autowired
    private BanQinCdWhSkuBarcodeService cdWhSkuBarcodeService;
    @Autowired
    private BanQinCdWhPackageService cdWhPackageService;
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;
    @Autowired
    private BanQinCdWhLotHeaderService cdWhLotHeaderService;
    @Autowired
    private BanQinCdWhLotDetailService cdWhLotDetailService;
    @Autowired
    private BanQinCdWhCycleService cdWhCycleService;
    @Autowired
    private BanQinCdWhQcItemHeaderService cdWhQcItemHeaderService;
    @Autowired
    private BanQinCdWhQcItemDetailService cdWhQcItemDetailService;
    @Autowired
    private BanQinCdWhAreaService cdWhAreaService;
    @Autowired
    private BanQinCdWhZoneService cdWhZoneService;
    @Autowired
    private BanQinCdWhLocService cdWhLocService;
    @Autowired
    private BanQinCdRulePaHeaderService cdRulePaHeaderService;
    @Autowired
    private BanQinCdRulePaDetailService cdRulePaDetailService;
    @Autowired
    private BanQinCdRuleQcHeaderService cdRuleQcHeaderService;
    @Autowired
    private BanQinCdRuleQcDetailService cdRuleQcDetailService;
    @Autowired
    private BanQinCdRuleQcClassService cdRuleQcClassService;
    @Autowired
    private BanQinCdRuleRotationHeaderService cdRuleRotationHeaderService;
    @Autowired
    private BanQinCdRuleRotationDetailService cdRuleRotationDetailService;
    @Autowired
    private BanQinCdRuleAllocHeaderService cdRuleAllocHeaderService;
    @Autowired
    private BanQinCdRuleAllocDetailService cdRuleAllocDetailService;
    @Autowired
    private BanQinCdRuleWvHeaderService cdRuleWvHeaderService;
    @Autowired
    private BanQinCdRuleWvDetailService cdRuleWvDetailService;
    @Autowired
    private BanQinCdRuleWvDetailWvService cdRuleWvDetailWvService;
    @Autowired
    private BanQinCdRuleWvDetailOrderService cdRuleWvDetailOrderService;
    @Autowired
    private BanQinCdRuleWvGroupHeaderService cdRuleWvGroupHeaderService;
    @Autowired
    private BanQinCdRuleWvGroupDetailService cdRuleWvGroupDetailService;
    @Autowired
    private BanQinWmCarrierTypeRelationService wmCarrierTypeRelationService;
    @Autowired
    private BanQinWeighMachineInfoService wmWeighMachineInfoService;
    @Autowired
    private CdWhSkuClassificationService cdWhSkuClassificationService;

    /**
     * 同步客户
     */
    @Transactional
    public void sync(SysWmsCustomer entity, String orgId) {
        ebCustomerService.remove(entity.getEbcuCustomerNo(), orgId);

        BanQinEbCustomer ebCustomer = new BanQinEbCustomer();
        BeanUtils.copyProperties(entity, ebCustomer);
        ebCustomer.setId(null);
        ebCustomer.setIsNewRecord(false);
        ebCustomer.setOrgId(orgId);
        ebCustomerService.save(ebCustomer);
    }

    /**
     * 同步商品
     */
    @Transactional
    public void sync(SysWmsSku entity, String orgId) {
        cdWhSkuService.remove(entity.getOwnerCode(), entity.getSkuCode(), orgId);

        BanQinCdWhSku cdWhSku = new BanQinCdWhSku();
        BeanUtils.copyProperties(entity, cdWhSku);
        cdWhSku.setId(null);
        cdWhSku.setIsNewRecord(false);
        cdWhSku.setOrgId(orgId);
        cdWhSkuService.save(cdWhSku);
        for (SysWmsSkuBarcode o : entity.getBarcodeList()) {
            BanQinCdWhSkuBarcode cdWhSkuBarcode = new BanQinCdWhSkuBarcode();
            BeanUtils.copyProperties(o, cdWhSkuBarcode);
            cdWhSkuBarcode.setId(null);
            cdWhSkuBarcode.setIsNewRecord(false);
            cdWhSkuBarcode.setHeaderId(cdWhSku.getId());
            cdWhSkuBarcode.setOrgId(orgId);
            cdWhSkuBarcodeService.save(cdWhSkuBarcode);
        }
        for (SysWmsSkuLoc o : entity.getLocList()) {
            BanQinCdWhSkuLoc cdWhSkuLoc = new BanQinCdWhSkuLoc();
            BeanUtils.copyProperties(o, cdWhSkuLoc);
            cdWhSkuLoc.setId(null);
            cdWhSkuLoc.setIsNewRecord(false);
            cdWhSkuLoc.setHeaderId(cdWhSku.getId());
            cdWhSkuLoc.setOrgId(orgId);
            cdWhSkuLocService.save(cdWhSkuLoc);
        }
    }

    /**
     * 同步包装
     */
    @Transactional
    public void sync(SysWmsPackage entity, String orgId) {
        cdWhPackageService.remove(entity.getCdpaCode(), orgId);

        BanQinCdWhPackage cdWhPackage = new BanQinCdWhPackage();
        List<BanQinCdWhPackageRelation> relations = Lists.newArrayList();
        for (SysWmsPackageRelation relation : entity.getPackageDetailList()) {
            BanQinCdWhPackageRelation cdWhPackageRelation = new BanQinCdWhPackageRelation();
            BeanUtils.copyProperties(relation, cdWhPackageRelation);
            cdWhPackageRelation.setId("");
            cdWhPackageRelation.setOrgId(orgId);
            relations.add(cdWhPackageRelation);
        }
        BeanUtils.copyProperties(entity, cdWhPackage);
        cdWhPackage.setId(null);
        cdWhPackage.setIsNewRecord(false);
        cdWhPackage.setOrgId(orgId);
        cdWhPackage.setPackageDetailList(relations);
        cdWhPackageService.save(cdWhPackage);
    }

    /**
     * 同步批次属性
     */
    @Transactional
    public void sync(SysWmsLotHeader entity, String orgId) {
        cdWhLotHeaderService.remove(entity.getLotCode(), orgId);

        BanQinCdWhLotHeader header = new BanQinCdWhLotHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setRemark(entity.getRemarks());
        header.setOrgId(orgId);
        header.setCdWhLotDetailList(null);
        cdWhLotHeaderService.save(header);
        for (SysWmsLotDetail o : entity.getCdWhLotDetailList()) {
            BanQinCdWhLotDetail detail = new BanQinCdWhLotDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdWhLotDetailService.save(detail);
        }
    }

    /**
     * 同步循环级别
     */
    @Transactional
    public void sync(SysWmsCycle entity, String orgId) {
        cdWhCycleService.remove(entity.getCycleCode(), orgId);

        BanQinCdWhCycle cdWhCycle = new BanQinCdWhCycle();
        BeanUtils.copyProperties(entity, cdWhCycle);
        cdWhCycle.setId(null);
        cdWhCycle.setIsNewRecord(false);
        cdWhCycle.setOrgId(orgId);
        cdWhCycleService.save(cdWhCycle);
    }

    /**
     * 同步质检项
     */
    @Transactional
    public void sync(SysWmsQcItemHeader entity, String orgId) {
        cdWhQcItemHeaderService.remove(entity.getItemGroupCode(), orgId);

        BanQinCdWhQcItemHeader header = new BanQinCdWhQcItemHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdWhQcItemHeaderService.save(header);
        for (SysWmsQcItemDetail o : entity.getQcItemDetailList()) {
            BanQinCdWhQcItemDetail detail = new BanQinCdWhQcItemDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdWhQcItemDetailService.save(detail);
        }
    }

    /**
     * 同步仓库区域
     */
    @Transactional
    public void sync(SysWmsArea entity, String orgId) {
        cdWhAreaService.remove(entity.getAreaCode(), orgId);

        BanQinCdWhArea cdWhArea = new BanQinCdWhArea();
        BeanUtils.copyProperties(entity, cdWhArea);
        cdWhArea.setId(null);
        cdWhArea.setIsNewRecord(false);
        cdWhArea.setOrgId(orgId);
        cdWhAreaService.save(cdWhArea);
    }

    /**
     * 同步库区
     */
    @Transactional
    public void sync(SysWmsZone entity, String orgId) {
        cdWhZoneService.remove(entity.getZoneCode(), orgId);

        BanQinCdWhZone cdWhZone = new BanQinCdWhZone();
        BeanUtils.copyProperties(entity, cdWhZone);
        cdWhZone.setId(null);
        cdWhZone.setIsNewRecord(false);
        cdWhZone.setOrgId(orgId);
        cdWhZoneService.save(cdWhZone);
    }

    /**
     * 同步库位
     */
    @Transactional
    public void sync(SysWmsLoc entity, String orgId) {
        cdWhLocService.remove(entity.getLocCode(), orgId);

        BanQinCdWhLoc cdWhLoc = new BanQinCdWhLoc();
        BeanUtils.copyProperties(entity, cdWhLoc);
        cdWhLoc.setId(null);
        cdWhLoc.setIsNewRecord(false);
        cdWhLoc.setOrgId(orgId);
        cdWhLocService.save(cdWhLoc);
    }

    /**
     * 同步上架规则
     */
    @Transactional
    public void sync(SysWmsRulePaHeader entity, String orgId) {
        cdRulePaHeaderService.remove(entity.getRuleCode(), orgId);

        BanQinCdRulePaHeader header = new BanQinCdRulePaHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdRulePaHeaderService.save(header);
        for (SysWmsRulePaDetail o : entity.getRulePaDetailList()) {
            BanQinCdRulePaDetail detail = new BanQinCdRulePaDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdRulePaDetailService.save(detail);
        }
    }

    /**
     * 同步质检规则
     */
    @Transactional
    public void sync(SysWmsRuleQcHeader entity, String orgId) {
        cdRuleQcHeaderService.remove(entity.getRuleCode(), orgId);

        BanQinCdRuleQcHeader header = new BanQinCdRuleQcHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdRuleQcHeaderService.save(header);
        for (SysWmsRuleQcDetail o : entity.getRuleQcDetailList()) {
            BanQinCdRuleQcDetail detail = new BanQinCdRuleQcDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdRuleQcDetailService.save(detail);
        }
        for (SysWmsRuleQcClass o : entity.getRuleQcClassList()) {
            BanQinCdRuleQcClass detail = new BanQinCdRuleQcClass();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdRuleQcClassService.save(detail);
        }
    }

    /**
     * 同步库存周转规则
     */
    @Transactional
    public void sync(SysWmsRuleRotationHeader entity, String orgId) {
        cdRuleRotationHeaderService.remove(entity.getRuleCode(), orgId);

        BanQinCdRuleRotationHeader header = new BanQinCdRuleRotationHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdRuleRotationHeaderService.save(header);
        for (SysWmsRuleRotationDetail o : entity.getRuleRotationDetailList()) {
            BanQinCdRuleRotationDetail detail = new BanQinCdRuleRotationDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdRuleRotationDetailService.save(detail);
        }
    }

    /**
     * 同步分配规则
     */
    @Transactional
    public void sync(SysWmsRuleAllocHeader entity, String orgId) {
        cdRuleAllocHeaderService.remove(entity.getRuleCode(), orgId);

        BanQinCdRuleAllocHeader header = new BanQinCdRuleAllocHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdRuleAllocHeaderService.save(header);
        for (SysWmsRuleAllocDetail o : entity.getRuleAllocDetailList()) {
            BanQinCdRuleAllocDetail detail = new BanQinCdRuleAllocDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdRuleAllocDetailService.save(detail);
        }
    }

    /**
     * 同步波次规则
     */
    @Transactional
    public void sync(SysWmsRuleWvHeader entity, String orgId) {
        cdRuleWvHeaderService.remove(entity.getRuleCode(), orgId);

        BanQinCdRuleWvHeader header = new BanQinCdRuleWvHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdRuleWvHeaderService.save(header);
        for (SysWmsRuleWvDetail o : entity.getRuleWvDetailList()) {
            BanQinCdRuleWvDetail detail = new BanQinCdRuleWvDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            detail.setRuleWvDetailWvList(null);
            detail.setRuleWvDetailOrderList(null);
            cdRuleWvDetailService.saveEntity(detail);
            for (SysWmsRuleWvDetailWv wv : o.getRuleWvDetailWvList()) {
                BanQinCdRuleWvDetailWv cdRuleWvDetailWv = new BanQinCdRuleWvDetailWv();
                BeanUtils.copyProperties(wv, cdRuleWvDetailWv);
                cdRuleWvDetailWv.setId(null);
                cdRuleWvDetailWv.setIsNewRecord(false);
                cdRuleWvDetailWv.setHeaderId(detail.getId());
                cdRuleWvDetailWv.setOrgId(orgId);
                cdRuleWvDetailWvService.save(cdRuleWvDetailWv);
            }
            for (SysWmsRuleWvDetailOrder order : o.getRuleWvDetailOrderList()) {
                BanQinCdRuleWvDetailOrder cdRuleWvDetailOrder = new BanQinCdRuleWvDetailOrder();
                BeanUtils.copyProperties(order, cdRuleWvDetailOrder);
                cdRuleWvDetailOrder.setId(null);
                cdRuleWvDetailOrder.setIsNewRecord(false);
                cdRuleWvDetailOrder.setHeaderId(detail.getId());
                cdRuleWvDetailOrder.setOrgId(orgId);
                cdRuleWvDetailOrderService.save(cdRuleWvDetailOrder);
            }
        }
    }

    /**
     * 同步波次规则组
     */
    @Transactional
    public void sync(SysWmsRuleWvGroupHeader entity, String orgId) {
        cdRuleWvGroupHeaderService.remove(entity.getGroupCode(), orgId);

        BanQinCdRuleWvGroupHeader header = new BanQinCdRuleWvGroupHeader();
        BeanUtils.copyProperties(entity, header);
        header.setId(null);
        header.setIsNewRecord(false);
        header.setOrgId(orgId);
        cdRuleWvGroupHeaderService.save(header);
        for (SysWmsRuleWvGroupDetail o : entity.getWvGroupDetailList()) {
            BanQinCdRuleWvGroupDetail detail = new BanQinCdRuleWvGroupDetail();
            BeanUtils.copyProperties(o, detail);
            detail.setId(null);
            detail.setIsNewRecord(false);
            detail.setHeaderId(header.getId());
            detail.setOrgId(orgId);
            cdRuleWvGroupDetailService.save(detail);
        }
    }

    /**
     * 同步承运商类型关系
     */
    @Transactional
    public void sync(SysWmsCarrierTypeRelation entity, String orgId) {
        wmCarrierTypeRelationService.remove(entity.getCarrierCode(), orgId);

        BanQinWmCarrierTypeRelation wmCarrierTypeRelation = new BanQinWmCarrierTypeRelation();
        BeanUtils.copyProperties(entity, wmCarrierTypeRelation);
        wmCarrierTypeRelation.setId(null);
        wmCarrierTypeRelation.setIsNewRecord(false);
        wmCarrierTypeRelation.setOrgId(orgId);
        wmCarrierTypeRelationService.save(wmCarrierTypeRelation);
    }

    /**
     * 同步称重设备维护
     */
    @Transactional
    public void sync(SysWmsWeighMachineInfo entity, String orgId) {
        if (!orgId.equals(entity.getOrgId())) {
            return;
        }
        wmWeighMachineInfoService.remove(entity.getMachineNo(), orgId);

        BanQinWeighMachineInfo weighMachineInfo = new BanQinWeighMachineInfo();
        BeanUtils.copyProperties(entity, weighMachineInfo);
        weighMachineInfo.setId(null);
        weighMachineInfo.setIsNewRecord(false);
        weighMachineInfo.setOrgId(orgId);
        wmWeighMachineInfoService.save(weighMachineInfo);
    }

    /**
     * 同步商品分类
     */
    @Transactional
    public void sync(SysCommonSkuClassification entity, String orgId) {
        cdWhSkuClassificationService.remove(entity.getCode(), orgId);

        CdWhSkuClassification newEntity = new CdWhSkuClassification();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        cdWhSkuClassificationService.save(newEntity);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysWmsCustomer... list) {
        List<BanQinEbCustomer> ebCustomerList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysWmsCustomer entity : list) {
                BanQinEbCustomer ebCustomer = new BanQinEbCustomer();
                BeanUtils.copyProperties(entity, ebCustomer);
                ebCustomer.setId(IdGen.uuid());
                ebCustomer.setUpdateDate(date);
                ebCustomer.setPmCode(IdGen.uuid());
                ebCustomer.setOrgId(orgId);
                ebCustomerList.add(ebCustomer);
            }
        }
        ebCustomerService.batchInsert(ebCustomerList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysWmsSku... list) {
        List<BanQinCdWhSku> cdWhSkuList = Lists.newArrayList();
        List<BanQinCdWhSkuBarcode> cdWhSkuBarcodeList = Lists.newArrayList();
        List<BanQinCdWhSkuLoc> cdWhSkuLocList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysWmsSku entity : list) {
                BanQinCdWhSku cdWhSku = new BanQinCdWhSku();
                BeanUtils.copyProperties(entity, cdWhSku);
                cdWhSku.setId(IdGen.uuid());
                cdWhSku.setUpdateDate(date);
                cdWhSku.setOrgId(orgId);
                cdWhSkuList.add(cdWhSku);

                for (SysWmsSkuBarcode o : entity.getBarcodeList()) {
                    BanQinCdWhSkuBarcode cdWhSkuBarcode = new BanQinCdWhSkuBarcode();
                    BeanUtils.copyProperties(o, cdWhSkuBarcode);
                    cdWhSkuBarcode.setId(IdGen.uuid());
                    cdWhSkuBarcode.setUpdateDate(date);
                    cdWhSkuBarcode.setHeaderId(cdWhSku.getId());
                    cdWhSkuBarcode.setOrgId(orgId);
                    cdWhSkuBarcodeList.add(cdWhSkuBarcode);
                }
                for (SysWmsSkuLoc o : entity.getLocList()) {
                    BanQinCdWhSkuLoc cdWhSkuLoc = new BanQinCdWhSkuLoc();
                    BeanUtils.copyProperties(o, cdWhSkuLoc);
                    cdWhSkuLoc.setId(IdGen.uuid());
                    cdWhSkuLoc.setUpdateDate(date);
                    cdWhSkuLoc.setHeaderId(cdWhSku.getId());
                    cdWhSkuLoc.setOrgId(orgId);
                    cdWhSkuLocList.add(cdWhSkuLoc);
                }
            }
        }
        cdWhSkuService.batchInsert(cdWhSkuList);
        cdWhSkuBarcodeService.batchInsert(cdWhSkuBarcodeList);
        cdWhSkuLocService.batchInsert(cdWhSkuLocList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysWmsPackage... list) {
        List<BanQinCdWhPackage> cdWhPackageList = Lists.newArrayList();
        List<BanQinCdWhPackageRelation> cdWhPackageRelationList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysWmsPackage entity : list) {
                BanQinCdWhPackage cdWhPackage = new BanQinCdWhPackage();
                BeanUtils.copyProperties(entity, cdWhPackage);
                cdWhPackage.setId(IdGen.uuid());
                cdWhPackage.setUpdateDate(date);
                cdWhPackage.setPmCode(IdGen.uuid());
                cdWhPackage.setOrgId(orgId);
                cdWhPackageList.add(cdWhPackage);

                for (SysWmsPackageRelation o : entity.getPackageDetailList()) {
                    BanQinCdWhPackageRelation cdWhPackageRelation = new BanQinCdWhPackageRelation();
                    BeanUtils.copyProperties(o, cdWhPackageRelation);
                    cdWhPackageRelation.setId(IdGen.uuid());
                    cdWhPackageRelation.setUpdateDate(date);
                    cdWhPackageRelation.setCdprCdpaPmCode(cdWhPackage.getPmCode());
                    cdWhPackageRelation.setPmCode(cdWhPackage.getCdpaCode());
                    cdWhPackageRelation.setOrgId(orgId);
                    cdWhPackageRelationList.add(cdWhPackageRelation);
                }
            }
        }
        cdWhPackageService.batchInsert(cdWhPackageList);
        cdWhPackageRelationService.batchInsert(cdWhPackageRelationList);
    }

    @Transactional
    public void removeCustomer(String customerNo, String orgId) {
        ebCustomerService.remove(customerNo, orgId);
    }

    @Transactional
    public void removeSku(String ownerCode, String skuCode, String orgId) {
        cdWhSkuService.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void removePackage(String packageCode, String orgId) {
        cdWhPackageService.remove(packageCode, orgId);
    }

    @Transactional
    public void removeArea(String areaCode, String orgId) {
        cdWhAreaService.remove(areaCode, orgId);
    }

    @Transactional
    public void removeCarrierTypeRelation(String carrierCode, String orgId) {
        wmCarrierTypeRelationService.remove(carrierCode, orgId);
    }

    @Transactional
    public void removeCycle(String cycleCode, String orgId) {
        cdWhCycleService.remove(cycleCode, orgId);
    }

    @Transactional
    public void removeLocation(String locCode, String orgId) {
        cdWhLocService.remove(locCode, orgId);
    }

    @Transactional
    public void removeLotAtt(String lotCode, String orgId) {
        cdWhLotHeaderService.remove(lotCode, orgId);
    }

    @Transactional
    public void removeQcItem(String itemGroupCode, String orgId) {
        cdWhQcItemDetailService.remove(itemGroupCode, orgId);
    }

    @Transactional
    public void removeRuleAlloc(String ruleCode, String orgId) {
        cdRuleAllocHeaderService.remove(ruleCode, orgId);
    }

    @Transactional
    public void removeRulePa(String ruleCode, String orgId) {
        cdRulePaHeaderService.remove(ruleCode, orgId);
    }

    @Transactional
    public void removeRuleQc(String ruleCode, String orgId) {
        cdRuleQcHeaderService.remove(ruleCode, orgId);
    }

    @Transactional
    public void removeRuleRotation(String ruleCode, String orgId) {
        cdRuleRotationHeaderService.remove(ruleCode, orgId);
    }

    @Transactional
    public void removeRuleWvGroup(String groupCode, String orgId) {
        cdRuleWvGroupHeaderService.remove(groupCode, orgId);
    }

    @Transactional
    public void removeRuleWv(String ruleCode, String orgId) {
        cdRuleWvHeaderService.remove(ruleCode, orgId);
    }

    @Transactional
    public void removeWeighMachine(String machineNo, String orgId) {
        wmWeighMachineInfoService.remove(machineNo, orgId);
    }

    @Transactional
    public void removeZone(String zoneCode, String orgId) {
        cdWhZoneService.remove(zoneCode, orgId);
    }

    @Transactional
    public void removeSkuClassification(String code, String orgId) {
        cdWhSkuClassificationService.remove(code, orgId);
    }
}