package com.yunyou.modules.sys.common.service;

import com.yunyou.common.enums.CustomerType;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.tms.common.TmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysCommonDefaultDataService {
    @Autowired
    private SysCommonCustomerService sysCommonCustomerService;
    @Autowired
    private SysWmsAreaService sysWmsAreaService;
    @Autowired
    private SysWmsZoneService sysWmsZoneService;
    @Autowired
    private SysWmsLocService sysWmsLocService;
    @Autowired
    private SysWmsLotHeaderService sysWmsLotHeaderService;
    @Autowired
    private SysWmsLotDetailService sysWmsLotDetailService;
    @Autowired
    private SysWmsQcItemHeaderService sysWmsQcItemHeaderService;
    @Autowired
    private SysWmsQcItemDetailService sysWmsQcItemDetailService;
    @Autowired
    private SysWmsRulePaHeaderService sysWmsRulePaHeaderService;
    @Autowired
    private SysWmsRulePaDetailService sysWmsRulePaDetailService;
    @Autowired
    private SysWmsRuleQcHeaderService sysWmsRuleQcHeaderService;
    @Autowired
    private SysWmsRuleRotationHeaderService sysWmsRuleRotationHeaderService;
    @Autowired
    private SysWmsRuleRotationDetailService sysWmsRuleRotationDetailService;
    @Autowired
    private SysWmsRuleAllocHeaderService sysWmsRuleAllocHeaderService;
    @Autowired
    private SysWmsRuleAllocDetailService sysWmsRuleAllocDetailService;
    @Autowired
    private SysCommonPackageService sysCommonPackageService;
    @Autowired
    private SysCommonPackageRelationService sysCommonPackageRelationService;

    /**
     * 生成默认资料
     *
     * @param dataSet 数据套
     */
    public void genDefaultData(String dataSet) {
        genAreaData(dataSet);// 仓库区域
        genZoneData(dataSet);// 库区
        genLocData(dataSet);// 库位
        genCustomerData(dataSet);// 客户
        genLotAttData(dataSet);// 批次属性
        genQcItemData(dataSet);// 质检项
        genPaRuleData(dataSet);// 上架规则
        genQcRuleData(dataSet);// 质检规则
        genRotationRuleData(dataSet);// 库存周转规则
        genAllocRuleData(dataSet);// 分配规则
        genTerms(dataSet);// 计费条款
        genPackage(dataSet);// 包装
    }

    private void genAreaData(String dataSet) {
        SysWmsArea entity = sysWmsAreaService.getByCode("AREA", dataSet);
        if (entity == null) {
            SysWmsArea sysWmsArea = new SysWmsArea();
            sysWmsArea.setAreaCode("AREA");
            sysWmsArea.setAreaName("AREA");
            sysWmsArea.setRemarks("系统默认");
            sysWmsArea.setDataSet(dataSet);
            sysWmsAreaService.save(sysWmsArea);
        }
    }

    private void genZoneData(String dataSet) {
        SysWmsZone entity = sysWmsZoneService.getByCode("ZONE", dataSet);
        if (entity == null) {
            SysWmsZone sysWmsZone = new SysWmsZone();
            sysWmsZone.setZoneCode("ZONE");
            sysWmsZone.setZoneName("ZONE");
            sysWmsZone.setType("1");
            sysWmsZone.setAreaCode("AREA");
            sysWmsZone.setAreaName("AREA");
            sysWmsZone.setRemarks("系统默认");
            sysWmsZone.setDataSet(dataSet);
            sysWmsZoneService.save(sysWmsZone);
        }
    }

    private void genLocData(String dataSet) {
        SysWmsLoc stage = sysWmsLocService.getByCode("STAGE", dataSet);
        if (stage == null) {
            stage = new SysWmsLoc();
            stage.setLocCode("STAGE");
            stage.setLocUseType("ST");
            stage.setCategory("1");
            stage.setZoneCode("ZONE");
            stage.setZoneName("ZONE");
            stage.setPaSeq("STAGE");
            stage.setPkSeq("STAGE");
            stage.setStatus("00");
            stage.setMaxMixLot(0L);
            stage.setMaxMixSku(0L);
            stage.setIsEnable("Y");
            stage.setIsLoseId("N");
            stage.setIsMixLot("Y");
            stage.setIsMixSku("Y");
            stage.setDataSet(dataSet);
            sysWmsLocService.save(stage);
        }
        SysWmsLoc crossDock = sysWmsLocService.getByCode("CROSSDOCK", dataSet);
        if (crossDock == null) {
            crossDock = new SysWmsLoc();
            crossDock.setLocCode("CROSSDOCK");
            crossDock.setLocUseType("CD");
            crossDock.setCategory("1");
            crossDock.setZoneCode("ZONE");
            crossDock.setZoneName("ZONE");
            crossDock.setPaSeq("CROSSDOCK");
            crossDock.setPkSeq("CROSSDOCK");
            crossDock.setStatus("00");
            crossDock.setMaxMixLot(0L);
            crossDock.setMaxMixSku(0L);
            crossDock.setIsEnable("Y");
            crossDock.setIsLoseId("N");
            crossDock.setIsMixLot("Y");
            crossDock.setIsMixSku("Y");
            crossDock.setDataSet(dataSet);
            sysWmsLocService.save(crossDock);
        }
        SysWmsLoc sortation = sysWmsLocService.getByCode("SORTATION", dataSet);
        if (sortation == null) {
            sortation = new SysWmsLoc();
            sortation.setLocCode("SORTATION");
            sortation.setLocUseType("SS");
            sortation.setCategory("1");
            sortation.setZoneCode("ZONE");
            sortation.setZoneName("ZONE");
            sortation.setPaSeq("SORTATION");
            sortation.setPkSeq("SORTATION");
            sortation.setStatus("00");
            sortation.setMaxMixLot(0L);
            sortation.setMaxMixSku(0L);
            sortation.setIsEnable("Y");
            sortation.setIsLoseId("N");
            sortation.setIsMixLot("Y");
            sortation.setIsMixSku("Y");
            sortation.setDataSet(dataSet);
            sysWmsLocService.save(sortation);
        }
        SysWmsLoc workbench = sysWmsLocService.getByCode("WORKBENCH", dataSet);
        if (workbench == null) {
            workbench = new SysWmsLoc();
            workbench.setLocCode("WORKBENCH");
            workbench.setLocUseType("KT");
            workbench.setCategory("1");
            workbench.setZoneCode("ZONE");
            workbench.setZoneName("ZONE");
            workbench.setPaSeq("WORKBENCH");
            workbench.setPkSeq("WORKBENCH");
            workbench.setStatus("00");
            workbench.setMaxMixLot(0L);
            workbench.setMaxMixSku(0L);
            workbench.setIsEnable("Y");
            workbench.setIsLoseId("N");
            workbench.setIsMixLot("Y");
            workbench.setIsMixSku("Y");
            workbench.setDataSet(dataSet);
            sysWmsLocService.save(workbench);
        }
    }

    private void genCustomerData(String dataSet) {
        SysCommonCustomer dds = sysCommonCustomerService.getByCode(TmsConstants.DEFAULT_DELIVERY_SITE, dataSet);
        if (dds == null) {
            dds = new SysCommonCustomer();
            dds.setCode(TmsConstants.DEFAULT_DELIVERY_SITE);
            dds.setName("默认提货网点");
            dds.setType(CustomerType.OUTLET.getCode());
            dds.setDataSet(dataSet);
            sysCommonCustomerService.save(dds);
        }
        SysCommonCustomer drs = sysCommonCustomerService.getByCode(TmsConstants.DEFAULT_RECEIVE_SITE, dataSet);
        if (drs == null) {
            drs = new SysCommonCustomer();
            drs.setCode(TmsConstants.DEFAULT_RECEIVE_SITE);
            drs.setName("默认送货网点");
            drs.setType(CustomerType.OUTLET.getCode());
            drs.setDataSet(dataSet);
            sysCommonCustomerService.save(drs);
        }
    }

    private void genLotAttData(String dataSet) {
        SysWmsLotHeader sysWmsLotHeader = sysWmsLotHeaderService.getByCode("01", dataSet);
        if (sysWmsLotHeader == null) {
            sysWmsLotHeader = new SysWmsLotHeader();
            sysWmsLotHeader.setLotCode("01");
            sysWmsLotHeader.setLotName("标准批次");
            sysWmsLotHeader.setDataSet(dataSet);
            sysWmsLotHeader.setCdWhLotDetailList(sysWmsLotDetailService.initialList());
            sysWmsLotHeaderService.save(sysWmsLotHeader);
        }
    }

    private void genQcItemData(String dataSet) {
        SysWmsQcItemHeader sysWmsQcItemHeader = sysWmsQcItemHeaderService.getByCode("01", dataSet);
        if (sysWmsQcItemHeader == null) {
            sysWmsQcItemHeader = new SysWmsQcItemHeader();
            sysWmsQcItemHeader.setItemGroupCode("01");
            sysWmsQcItemHeader.setItemGroupName("标准质检项");
            sysWmsQcItemHeader.setDataSet(dataSet);
            sysWmsQcItemHeaderService.save(sysWmsQcItemHeader);
            SysWmsQcItemDetail sysWmsQcItemDetail = new SysWmsQcItemDetail();
            sysWmsQcItemDetail.setHeaderId(sysWmsQcItemHeader.getId());
            sysWmsQcItemDetail.setItemGroupCode(sysWmsQcItemHeader.getItemGroupCode());
            sysWmsQcItemDetail.setQcItem("目视");
            sysWmsQcItemDetail.setQcRef("VISUAL");
            sysWmsQcItemDetail.setDataSet(dataSet);
            sysWmsQcItemDetailService.save(sysWmsQcItemDetail);
        }
    }

    private void genPaRuleData(String dataSet) {
        SysWmsRulePaHeader sysWmsRulePaHeader = sysWmsRulePaHeaderService.getByCode("01", dataSet);
        if (sysWmsRulePaHeader == null) {
            sysWmsRulePaHeader = new SysWmsRulePaHeader();
            sysWmsRulePaHeader.setRuleCode("01");
            sysWmsRulePaHeader.setRuleName("标准上架");
            sysWmsRulePaHeader.setDataSet(dataSet);
            sysWmsRulePaHeaderService.save(sysWmsRulePaHeader);
            SysWmsRulePaDetail sysWmsRulePaDetail1 = new SysWmsRulePaDetail();
            sysWmsRulePaDetail1.setHeaderId(sysWmsRulePaHeader.getId());
            sysWmsRulePaDetail1.setRuleCode(sysWmsRulePaHeader.getRuleCode());
            sysWmsRulePaDetail1.setLineNo("01");
            sysWmsRulePaDetail1.setMainCode("B01");
            sysWmsRulePaDetail1.setIsEnable("Y");
            sysWmsRulePaDetail1.setToZone("ZONE");
            sysWmsRulePaDetail1.setIsPackageRestrict("N");
            sysWmsRulePaDetail1.setIsLessCs("N");
            sysWmsRulePaDetail1.setIsMoreCsLessPl("N");
            sysWmsRulePaDetail1.setIsMorePl("N");
            sysWmsRulePaDetail1.setIsAsnTypeRestrict("N");
            sysWmsRulePaDetail1.setIsLotAttRestrict("N");
            sysWmsRulePaDetail1.setIsUseTypeRestrict("N");
            sysWmsRulePaDetail1.setIsSpaceRestrict("N");
            sysWmsRulePaDetail1.setIsPlRestrict("N");
            sysWmsRulePaDetail1.setIsCubicRestrict("N");
            sysWmsRulePaDetail1.setIsWeightRestrict("N");
            sysWmsRulePaDetail1.setIsCategoryRestrict("N");
            sysWmsRulePaDetail1.setIsAbcRestrict("N");
            sysWmsRulePaDetail1.setDataSet(dataSet);
            sysWmsRulePaDetailService.save(sysWmsRulePaDetail1);
            SysWmsRulePaDetail sysWmsRulePaDetail2 = new SysWmsRulePaDetail();
            sysWmsRulePaDetail2.setHeaderId(sysWmsRulePaHeader.getId());
            sysWmsRulePaDetail2.setRuleCode(sysWmsRulePaHeader.getRuleCode());
            sysWmsRulePaDetail2.setLineNo("02");
            sysWmsRulePaDetail2.setMainCode("C01");
            sysWmsRulePaDetail2.setIsEnable("Y");
            sysWmsRulePaDetail2.setToZone("ZONE");
            sysWmsRulePaDetail2.setIsPackageRestrict("N");
            sysWmsRulePaDetail2.setIsLessCs("N");
            sysWmsRulePaDetail2.setIsMoreCsLessPl("N");
            sysWmsRulePaDetail2.setIsMorePl("N");
            sysWmsRulePaDetail2.setIsAsnTypeRestrict("N");
            sysWmsRulePaDetail2.setIsLotAttRestrict("N");
            sysWmsRulePaDetail2.setIsUseTypeRestrict("N");
            sysWmsRulePaDetail2.setIsSpaceRestrict("N");
            sysWmsRulePaDetail2.setIsPlRestrict("N");
            sysWmsRulePaDetail2.setIsCubicRestrict("N");
            sysWmsRulePaDetail2.setIsWeightRestrict("N");
            sysWmsRulePaDetail2.setIsCategoryRestrict("N");
            sysWmsRulePaDetail2.setIsAbcRestrict("N");
            sysWmsRulePaDetail2.setDataSet(dataSet);
            sysWmsRulePaDetailService.save(sysWmsRulePaDetail2);
        }
    }

    private void genQcRuleData(String dataSet) {
        SysWmsRuleQcHeader sysWmsRuleQcHeader = sysWmsRuleQcHeaderService.getByCode("01", dataSet);
        if (sysWmsRuleQcHeader == null) {
            sysWmsRuleQcHeader = new SysWmsRuleQcHeader();
            sysWmsRuleQcHeader.setRuleCode("01");
            sysWmsRuleQcHeader.setRuleName("标准质检");
            sysWmsRuleQcHeader.setDataSet(dataSet);
            sysWmsRuleQcHeaderService.save(sysWmsRuleQcHeader);
        }
    }

    private void genRotationRuleData(String dataSet) {
        SysWmsRuleRotationHeader sysWmsRuleRotationHeader = sysWmsRuleRotationHeaderService.getByCode("01", dataSet);
        if (sysWmsRuleRotationHeader == null) {
            sysWmsRuleRotationHeader = new SysWmsRuleRotationHeader();
            sysWmsRuleRotationHeader.setRuleCode("01");
            sysWmsRuleRotationHeader.setRuleName("标准周转");
            sysWmsRuleRotationHeader.setRotationType("ROTATION");
            sysWmsRuleRotationHeader.setLotCode("01");
            sysWmsRuleRotationHeader.setDataSet(dataSet);
            sysWmsRuleRotationHeaderService.save(sysWmsRuleRotationHeader);
            SysWmsRuleRotationDetail sysWmsRuleRotationDetail = new SysWmsRuleRotationDetail();
            sysWmsRuleRotationDetail.setHeaderId(sysWmsRuleRotationHeader.getId());
            sysWmsRuleRotationDetail.setRuleCode(sysWmsRuleRotationHeader.getRuleCode());
            sysWmsRuleRotationDetail.setLineNo("01");
            sysWmsRuleRotationDetail.setLotAtt("LOT_ATT03");
            sysWmsRuleRotationDetail.setOrderBy("A");
            sysWmsRuleRotationDetail.setDataSet(dataSet);
            sysWmsRuleRotationDetailService.save(sysWmsRuleRotationDetail);
        }
    }

    private void genAllocRuleData(String dataSet) {
        SysWmsRuleAllocHeader sysWmsRuleAllocHeader = sysWmsRuleAllocHeaderService.getByCode("01", dataSet);
        if (sysWmsRuleAllocHeader == null) {
            sysWmsRuleAllocHeader = new SysWmsRuleAllocHeader();
            sysWmsRuleAllocHeader.setRuleCode("01");
            sysWmsRuleAllocHeader.setRuleName("标准分配");
            sysWmsRuleAllocHeader.setDataSet(dataSet);
            sysWmsRuleAllocHeaderService.save(sysWmsRuleAllocHeader);
            SysWmsRuleAllocDetail sysWmsRuleAllocDetail = new SysWmsRuleAllocDetail();
            sysWmsRuleAllocDetail.setHeaderId(sysWmsRuleAllocHeader.getId());
            sysWmsRuleAllocDetail.setRuleCode(sysWmsRuleAllocHeader.getRuleCode());
            sysWmsRuleAllocDetail.setLineNo("01");
            sysWmsRuleAllocDetail.setUom("EA");
            sysWmsRuleAllocDetail.setIsClearFirst("Y");
            sysWmsRuleAllocDetail.setDataSet(dataSet);
            sysWmsRuleAllocDetailService.save(sysWmsRuleAllocDetail);
        }
    }

    private void genTerms(String dataSet) {

    }

    private void genPackage(String dataSet) {
        SysCommonPackage sysCommonPackage = sysCommonPackageService.getByCode("01", dataSet);
        if (sysCommonPackage == null) {
            sysCommonPackage = new SysCommonPackage();
            sysCommonPackage.setCdpaCode("01");
            sysCommonPackage.setCdpaType("M_P_G");
            sysCommonPackage.setCdpaFormat("1/1/1");
            sysCommonPackage.setCdpaFormatEn("1/1/1");
            sysCommonPackage.setCdpaDesc("默认包装规格");
            sysCommonPackage.setCdpaIsUse("0");
            sysCommonPackage.setDataSet(dataSet);
            sysCommonPackage.setPackageDetailList(sysCommonPackageRelationService.initialList());
            sysCommonPackageService.save(sysCommonPackage);
        }
    }
}
