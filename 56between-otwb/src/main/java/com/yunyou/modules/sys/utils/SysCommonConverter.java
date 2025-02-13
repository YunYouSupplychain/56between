package com.yunyou.modules.sys.utils;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.sys.common.entity.*;
import com.google.common.collect.Lists;

import java.util.List;

public class SysCommonConverter {

    /**
     * 平台客户转系统客户
     */
    public static SysOmsCustomer convertToOms(SysCommonCustomer entity, SysOmsCustomer old) {
        boolean isNew = old == null;
        SysOmsCustomer target = old;
        if (isNew) {
            target = new SysOmsCustomer();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
            target.setPmCode(IdGen.uuid());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setEbcuCustomerNo(entity.getCode());
        target.setEbcuCustomerStatus("0");
        target.setEbcuNameCn(entity.getName());
        target.setEbcuShortName(entity.getShortName());
        target.setEbcuNameEn(entity.getForeignName());
        target.setEbcuType(entity.getType());
        target.setEbcuEbplProvinceCode(entity.getProvince());
        target.setEbcuEbplCityCode(entity.getCity());
        target.setEbcuAddress(entity.getAddress());
        target.setEbcuTel(entity.getTel());
        target.setEbcuFax(entity.getFax());
        target.setEbcuZipCode(entity.getZipCode());
        target.setEbcuIndustryType(entity.getIndustryType());
        target.setMajorClass(entity.getCategories());
        target.setRangeType(entity.getScope());
        target.setClerkCode(entity.getClerkCode());
        target.setEbcuFinanceCode(entity.getFinanceCode());
        target.setBrand(entity.getBrand());
        target.setEbcuEbplCityCode(entity.getAreaId());
        target.setEbcuSubstr1(entity.getDef1());
        target.setEbcuSubstr2(entity.getDef2());
        target.setEbcuSubstr3(entity.getDef3());
        target.setEbcuSubstr4(entity.getDef4());
        target.setEbcuSubstr5(entity.getDef5());
        target.setDataSet(entity.getDataSet());
        return target;
    }

    public static SysWmsCustomer convertToWms(SysCommonCustomer entity, SysWmsCustomer old) {
        boolean isNew = old == null;
        SysWmsCustomer target = old;
        if (isNew) {
            target = new SysWmsCustomer();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
            target.setPmCode(IdGen.uuid());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setEbcuCustomerNo(entity.getCode());
        target.setEbcuCustomerStatus("0");
        target.setEbcuNameCn(entity.getName());
        target.setEbcuShortName(entity.getShortName());
        target.setEbcuNameEn(entity.getForeignName());
        target.setEbcuType(entity.getType());
        target.setEbcuAddress(entity.getAddress());
        target.setEbcuTel(entity.getTel());
        target.setEbcuFax(entity.getFax());
        target.setEbcuZipCode(entity.getZipCode());
        target.setEbcuIndustryType(entity.getIndustryType());
        target.setEbcuMainBusiness(entity.getMainBusiness());
        target.setEbcuFinanceCode(entity.getFinanceCode());
        target.setEbcuTaxRegistNo(entity.getTaxRegisterNo());
        target.setEbcuBusinessNo(entity.getBusinessNo());
        target.setEbcuIsGeneralTaxpayer(entity.getIsGeneralTaxpayer());
        target.setEbcuTaxRate(entity.getTaxRate());
        target.setEbcuTaxRateValue(entity.getTaxRateValue());
        target.setEbcuUrl(entity.getUrl());
        target.setEbcuSubstr1(entity.getDef1());
        target.setEbcuSubstr2(entity.getDef2());
        target.setEbcuSubstr3(entity.getDef3());
        target.setEbcuSubstr4(entity.getDef4());
        target.setEbcuSubstr5(entity.getDef5());
        target.setDataSet(entity.getDataSet());
        return target;
    }

    public static SysTmsTransportObj convertToTms(SysCommonCustomer entity, SysTmsTransportObj old) {
        boolean isNew = old == null;
        SysTmsTransportObj target = old;
        if (isNew) {
            target = new SysTmsTransportObj();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setTransportObjCode(entity.getCode());
        target.setTransportObjName(entity.getName());
        target.setTransportObjShortName(entity.getShortName());
        target.setTransportObjType(entity.getType());
        target.setAreaId(entity.getAreaId());
        target.setAddress(entity.getAddress());
        target.setContact(entity.getContacts());
        target.setTel(entity.getTel());
        target.setFax(entity.getFax());
        target.setEmail(entity.getMail());
        target.setUrl(entity.getUrl());
        target.setUnCode(entity.getUnCode());
        target.setClassification(entity.getClassification());
        target.setRouteId(entity.getRouteCode());
        target.setCarrierMatchedOrgId(entity.getCarrierMatchedOrgId());
        target.setOutletMatchedOrgId(entity.getOutletMatchedOrgId());
        target.setRepairPrice(entity.getRepairPrice());
        target.setSettleCode(entity.getSettleCode());
        target.setBrand(entity.getBrand());
        target.setDataSet(entity.getDataSet());
        return target;
    }

    public static SysBmsCustomer convertToBms(SysCommonCustomer entity, SysBmsCustomer old) {
        boolean isNew = old == null;
        SysBmsCustomer target = old;
        if (isNew) {
            target = new SysBmsCustomer();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setEbcuCustomerNo(entity.getCode());
        target.setEbcuNameCn(entity.getName());
        target.setEbcuShortName(entity.getShortName());
        target.setEbcuNameEn(entity.getForeignName());
        target.setEbcuType(entity.getType());
        target.setEbcuIndustryType(entity.getIndustryType());
        target.setEbcuTel(entity.getTel());
        target.setEbcuFax(entity.getFax());
        target.setEbcuZipCode(entity.getZipCode());
        target.setEbcuAddress(entity.getAddress());
        target.setAreaId(entity.getAreaId());
        target.setCheckPerson(entity.getCheckPerson());
        target.setEbcuMainBusiness(entity.getMainBusiness());
        target.setEbcuFinanceCode(entity.getFinanceCode());
        target.setEbcuTaxRegistNo(entity.getTaxRegisterNo());
        target.setEbcuMainBusiness(entity.getMainBusiness());
        target.setEbcuIsGeneralTaxpayer(entity.getIsGeneralTaxpayer());
        target.setEbcuTaxRate(entity.getTaxRate());
        target.setEbcuTaxRateValue(entity.getTaxRateValue());
        target.setEbcuBusinessNo(entity.getBusinessNo());
        target.setProject(entity.getProject());
        target.setDataSet(entity.getDataSet());
        return target;
    }

    /**
     * 平台商品转系统商品
     */
    public static SysOmsItem convertToOms(SysCommonSku entity, SysOmsItem old) {
        boolean isNew = old == null;
        SysOmsItem target = old;
        if (isNew) {
            target = new SysOmsItem();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setOwnerCode(entity.getOwnerCode());
        target.setSkuCode(entity.getSkuCode());
        target.setSkuName(entity.getSkuName());
        target.setShortName(entity.getSkuShortName());
        target.setSkuClass(entity.getSkuClass());
        target.setSkuType(entity.getSkuType());
        target.setSkuTempLayer(entity.getTempLevel());
        target.setUnit(entity.getUnit());
        target.setAuxiliaryUnit(entity.getUnit());
        target.setSkuModel(entity.getSkuModel());
        target.setSpec(entity.getSkuSpec());
        target.setPackCode(entity.getPackCode());
        target.setGrossWeight(entity.getGrossWeight());
        target.setNetWeight(entity.getNetWeight());
        target.setVolume(entity.getVolume());
        target.setLength(entity.getLength());
        target.setWidth(entity.getWidth());
        target.setHeight(entity.getHeight());
        target.setDataSet(entity.getDataSet());
        List<SysOmsItemBarcode> barcodeList = Lists.newArrayList();
        List<SysCommonSkuBarcode> skuBarcodeList = entity.getSkuBarcodeList();
        for (int i = 0; i < skuBarcodeList.size(); i++) {
            SysCommonSkuBarcode o = skuBarcodeList.get(i);

            SysOmsItemBarcode targetBarcode = new SysOmsItemBarcode();
            if (isNew) {
                targetBarcode.setId(IdGen.uuid());
                targetBarcode.setCreateBy(o.getCreateBy());
                targetBarcode.setCreateDate(o.getCreateDate());
            } else {
                targetBarcode.setId("");
            }
            targetBarcode.setUpdateBy(o.getUpdateBy());
            targetBarcode.setUpdateDate(o.getUpdateDate());
            targetBarcode.setItemId(target.getId());
            targetBarcode.setLineNo(String.format("%04d", i + 1));
            targetBarcode.setBarcode(o.getBarcode());
            targetBarcode.setIsDefault(o.getIsDefault());
            targetBarcode.setDataSet(o.getDataSet());
            barcodeList.add(targetBarcode);
        }
        target.setOmItemBarcodeList(barcodeList);
        return target;
    }

    public static SysWmsSku convertToWms(SysCommonSku entity, SysWmsSku old) {
        boolean isNew = old == null;
        SysWmsSku target = old;
        if (isNew) {
            target = new SysWmsSku();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setOwnerCode(entity.getOwnerCode());
        target.setSkuCode(entity.getSkuCode());
        target.setIsEnable("Y");
        target.setSkuName(entity.getSkuName());
        target.setShortName(entity.getSkuShortName());
        target.setForeignName(entity.getSkuForeignName());
        target.setPackCode(entity.getPackCode());
        target.setGrossWeight(entity.getGrossWeight());
        target.setNetWeight(entity.getNetWeight());
        target.setCubic(entity.getVolume());
        target.setPrice(entity.getPrice() == null ? null : entity.getPrice().doubleValue());
        target.setLength(entity.getLength());
        target.setWidth(entity.getWidth());
        target.setHeight(entity.getHeight());
        target.setBarCode(entity.getBarCode());
        target.setSupBarCode(entity.getSupBarCode());
        target.setLotCode(entity.getLotCode());
        target.setAbc(entity.getAbc());
        target.setGroupCode(entity.getGroupCode());
        target.setMaterialCode(entity.getMaterialCode());
        target.setRcvUom(entity.getRcvUom());
        target.setShipUom(entity.getShipUom());
        target.setPrintUom(entity.getPrintUom());
        target.setMaxLimit(entity.getMaxLimit());
        target.setMinLimit(entity.getMinLimit());
        target.setIsValidity(entity.getIsValidity());
        target.setShelfLife(entity.getShelfLife());
        target.setLifeType(entity.getLifeType());
        target.setInLifeDays(entity.getInLifeDays());
        target.setOutLifeDays(entity.getOutLifeDays());
        target.setIsOverRcv(entity.getIsOverRcv());
        target.setOverRcvPct(entity.getOverRcvPct());
        target.setPaZone(entity.getPaZone());
        target.setPaLoc(entity.getPaLoc());
        target.setReserveCode(entity.getReserveCode());
        target.setPaRule(entity.getPaRule());
        target.setRotationRule(entity.getRotationRule());
        target.setAllocRule(entity.getAllocRule());
        target.setCycleCode(entity.getCycleCode());
        target.setCdClass(entity.getCdClass());
        target.setStyle(entity.getStyle());
        target.setColor(entity.getColor());
        target.setSkuSize(entity.getSkuSize());
        target.setIsDg(entity.getIsDg());
        target.setDgClass(entity.getDgClass());
        target.setUnno(entity.getUnno());
        target.setIsCold(entity.getIsCold());
        target.setMinTemp(entity.getMinTemp());
        target.setMaxTemp(entity.getMaxTemp());
        target.setHsCode(entity.getHsCode());
        target.setIsSerial(entity.getIsSerial());
        target.setIsParent(entity.getIsParent());
        target.setIsQc(entity.getIsQc());
        target.setQcPhase(entity.getQcPhase());
        target.setQcRule(entity.getQcRule());
        target.setItemGroupCode(entity.getItemGroupCode());
        target.setRateGroup(entity.getRateGroup());
        target.setPeriodOfValidity(entity.getPeriodOfValidity());
        target.setValidityUnit(entity.getValidityUnit());
        target.setTypeCode(entity.getSkuClass());
        target.setStockCurId(entity.getStockCurId());
        target.setQuickCode(entity.getQuickCode());
        target.setFormCode(entity.getFormCode());
        target.setEmergencyTel(entity.getEmergencyTel());
        target.setEffectiveDate(entity.getEffectiveDate());
        target.setExpirationDate(entity.getExpirationDate());
        target.setFlashPoint(entity.getFlashPoint());
        target.setBurningPoint(entity.getBurningPoint());
        target.setProvideCustCode(entity.getProvideCustCode());
        target.setTempLevel(entity.getTempLevel());
        target.setSpec(entity.getSkuSpec());
        target.setSkuCustomerCode(entity.getSkuCustomerCode());
        target.setDataSet(entity.getDataSet());
        List<SysWmsSkuBarcode> skuBarcodeList = Lists.newArrayList();
        for (SysCommonSkuBarcode o : entity.getSkuBarcodeList()) {
            SysWmsSkuBarcode targetBarcode = new SysWmsSkuBarcode();
            if (isNew) {
                targetBarcode.setId(IdGen.uuid());
                targetBarcode.setCreateBy(o.getCreateBy());
                targetBarcode.setCreateDate(o.getCreateDate());
            } else {
                targetBarcode.setId("");
            }
            targetBarcode.setUpdateBy(o.getUpdateBy());
            targetBarcode.setUpdateDate(o.getUpdateDate());
            targetBarcode.setHeaderId(target.getId());
            targetBarcode.setOwnerCode(o.getOwnerCode());
            targetBarcode.setSkuCode(o.getSkuCode());
            targetBarcode.setBarcode(o.getBarcode());
            targetBarcode.setDataSet(o.getDataSet());
            targetBarcode.setIsDefault(o.getIsDefault());
            skuBarcodeList.add(targetBarcode);
        }
        target.setBarcodeList(skuBarcodeList);
        List<SysWmsSkuLoc> skuLocList = Lists.newArrayList();
        for (SysCommonSkuLoc o : entity.getSkuLocList()) {
            SysWmsSkuLoc targetLoc = new SysWmsSkuLoc();
            if (isNew) {
                targetLoc.setId(IdGen.uuid());
                targetLoc.setCreateBy(o.getCreateBy());
                targetLoc.setCreateDate(o.getCreateDate());
            } else {
                targetLoc.setId("");
            }
            targetLoc.setUpdateBy(o.getUpdateBy());
            targetLoc.setUpdateDate(o.getUpdateDate());
            targetLoc.setHeaderId(target.getId());
            targetLoc.setOwnerCode(o.getOwnerCode());
            targetLoc.setSkuCode(o.getSkuCode());
            targetLoc.setLocCode(o.getLocCode());
            targetLoc.setSkuLocType(o.getSkuLocType());
            targetLoc.setMinRp(o.getMinRp());
            targetLoc.setRpUom(o.getRpUom());
            targetLoc.setMaxLimit(o.getMaxLimit());
            targetLoc.setMinLimit(o.getMinLimit());
            targetLoc.setIsOverAlloc(o.getIsOverAlloc());
            targetLoc.setIsAutoRp(o.getIsAutoRp());
            targetLoc.setIsRpAlloc(o.getIsRpAlloc());
            targetLoc.setIsOverRp(o.getIsOverRp());
            targetLoc.setIsFmRs(o.getIsFmRs());
            targetLoc.setIsFmCs(o.getIsFmCs());
            targetLoc.setDataSet(o.getDataSet());
            skuLocList.add(targetLoc);
        }
        target.setLocList(skuLocList);
        return target;
    }

    public static SysTmsItem convertToTms(SysCommonSku entity, SysTmsItem old) {
        boolean isNew = old == null;
        SysTmsItem target = old;
        if (isNew) {
            target = new SysTmsItem();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setOwnerCode(entity.getOwnerCode());
        target.setSkuCode(entity.getSkuCode());
        target.setUnit("EA");
        target.setSkuName(entity.getSkuName());
        target.setSkuModel(entity.getSkuModel());
        target.setSkuClass(entity.getSkuClass());
        target.setGrossweight(entity.getGrossWeight());
        target.setNetweight(entity.getNetWeight());
        target.setCubic(entity.getVolume());
        target.setLength(entity.getLength());
        target.setWidth(entity.getWidth());
        target.setHeight(entity.getHeight());
        target.setDataSet(entity.getDataSet());
        List<SysTmsItemBarcode> skuBarcodeList = Lists.newArrayList();
        for (SysCommonSkuBarcode o : entity.getSkuBarcodeList()) {
            SysTmsItemBarcode targetBarcode = new SysTmsItemBarcode();
            if (isNew) {
                targetBarcode.setId(IdGen.uuid());
                targetBarcode.setCreateBy(o.getCreateBy());
                targetBarcode.setCreateDate(o.getCreateDate());
            } else {
                targetBarcode.setId("");
            }
            targetBarcode.setUpdateBy(o.getUpdateBy());
            targetBarcode.setUpdateDate(o.getUpdateDate());
            targetBarcode.setOwnerCode(o.getOwnerCode());
            targetBarcode.setSkuCode(o.getSkuCode());
            targetBarcode.setBarcode(o.getBarcode());
            targetBarcode.setDataSet(o.getDataSet());
            targetBarcode.setIsDefault(o.getIsDefault());
            skuBarcodeList.add(targetBarcode);
        }
        target.setBarcodeList(skuBarcodeList);
        return target;
    }

    public static SysBmsSku convertToBms(SysCommonSku entity, List<SysCommonPackageRelation> relations, SysBmsSku old) {
        boolean isNew = old == null;
        SysBmsSku target = old;
        if (isNew) {
            target = new SysBmsSku();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
        }
        Double ea = relations.stream().filter(o -> "EA".equals(o.getCdprUnitLevel())).map(SysCommonPackageRelation::getCdprQuantity).findAny().orElse(1D);
        Double ip = relations.stream().filter(o -> "IP".equals(o.getCdprUnitLevel())).map(SysCommonPackageRelation::getCdprQuantity).findAny().orElse(1D);
        Double cs = relations.stream().filter(o -> "CS".equals(o.getCdprUnitLevel())).map(SysCommonPackageRelation::getCdprQuantity).findAny().orElse(1D);
        Double pl = relations.stream().filter(o -> "PL".equals(o.getCdprUnitLevel())).map(SysCommonPackageRelation::getCdprQuantity).findAny().orElse(1D);
        Double ot = relations.stream().filter(o -> "OT".equals(o.getCdprUnitLevel())).map(SysCommonPackageRelation::getCdprQuantity).findAny().orElse(1D);

        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setOwnerCode(entity.getOwnerCode());
        target.setSkuCode(entity.getSkuCode());
        target.setSkuName(entity.getSkuName());
        target.setSkuShortName(entity.getSkuShortName());
        target.setSkuSpec(entity.getSkuSpec());
        target.setLength(entity.getLength());
        target.setWidth(entity.getWidth());
        target.setHeight(entity.getHeight());
        target.setGrossWeight(entity.getGrossWeight());
        target.setNetWeight(entity.getNetWeight());
        target.setVolume(entity.getVolume());
        target.setSkuModel(entity.getSkuModel());
        target.setSkuType(entity.getSkuType());
        target.setSalesUnit(entity.getSalesUnit());
        target.setStoreOrderTimes(entity.getStoreOrderTimes());
        target.setDetachableFlag(entity.getDetachableFlag());
        target.setDeductWeightRatio(entity.getDeductWeightRatio());
        target.setSkuWeighType(entity.getSkuWeighType());
        target.setSkuTempLayer(entity.getTempLevel());
        target.setSkuClass(entity.getSkuClass());
        target.setWeighWay(entity.getWeighWay());
        target.setTareWeight(entity.getTareWeight());
        target.setIsIqc(entity.getIsIqc());
        target.setIsIces(entity.getIsIces());
        target.setBoxVolume(entity.getBoxVolume());
        target.setSkuCheckType(entity.getSkuCheckType());
        target.setUnno(entity.getUnno());
        target.setEaQuantity(ea);
        target.setIpQuantity(ip);
        target.setCsQuantity(cs);
        target.setPlQuantity(pl);
        target.setOtQuantity(ot);
        target.setDataSet(entity.getDataSet());
        List<SysBmsSkuSupplier> skuSupplierList = Lists.newArrayList();
        for (SysCommonSkuSupplier skuSupplier : entity.getSkuSupplierList()) {
            SysBmsSkuSupplier targetSupplier = new SysBmsSkuSupplier();
            if (isNew) {
                targetSupplier.setId(IdGen.uuid());
                targetSupplier.setCreateBy(skuSupplier.getCreateBy());
                targetSupplier.setCreateDate(skuSupplier.getCreateDate());
            } else {
                targetSupplier.setId("");
            }
            targetSupplier.setUpdateBy(skuSupplier.getUpdateBy());
            targetSupplier.setUpdateDate(skuSupplier.getUpdateDate());
            targetSupplier.setSkuId(target.getId());
            targetSupplier.setSupplierCode(skuSupplier.getSupplierCode());
            targetSupplier.setSupplierName(skuSupplier.getSupplierName());
            targetSupplier.setDataSet(skuSupplier.getDataSet());
            skuSupplierList.add(targetSupplier);
        }
        target.setSkuSuppliers(skuSupplierList);
        return target;
    }

    /**
     * 平台包装转系统包装
     */
    public static SysOmsPackage convertToOms(SysCommonPackage entity, SysOmsPackage old) {
        boolean isNew = old == null;
        SysOmsPackage target = old;
        if (isNew) {
            target = new SysOmsPackage();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
            target.setPmCode(IdGen.uuid());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setCdpaCode(entity.getCdpaCode());
        target.setCdpaIsUse(entity.getCdpaIsUse());
        target.setCdpaType(entity.getCdpaType());
        target.setCdpaIsUse(entity.getCdpaIsUse());
        target.setCdpaFormat(entity.getCdpaFormat());
        target.setCdpaFormatEn(entity.getCdpaFormatEn());
        target.setCdpaDesc(entity.getCdpaDesc());
        target.setCdpaWhCode(entity.getCdpaWhCode());
        target.setDataSet(entity.getDataSet());
        List<SysOmsPackageRelation> relations = Lists.newArrayList();
        for (SysCommonPackageRelation relation : entity.getPackageDetailList()) {
            SysOmsPackageRelation targetRelation = new SysOmsPackageRelation();
            if (isNew) {
                targetRelation.setId(IdGen.uuid());
                targetRelation.setCreateBy(relation.getCreateBy());
                targetRelation.setCreateDate(relation.getCreateDate());
            } else {
                targetRelation.setId("");
            }
            targetRelation.setUpdateBy(relation.getUpdateBy());
            targetRelation.setUpdateDate(relation.getUpdateDate());
            targetRelation.setCdprUnit(relation.getCdprUnit());
            targetRelation.setCdprUnitRate(relation.getCdprUnitRate());
            targetRelation.setCdprIsMain(relation.getCdprIsMain());
            targetRelation.setCdprSequencesNo(relation.getCdprSequencesNo());
            targetRelation.setCdprUnitLevel(relation.getCdprUnitLevel());
            targetRelation.setCdprQuantity(relation.getCdprQuantity());
            targetRelation.setCdprDesc(relation.getCdprDesc());
            targetRelation.setCdprMaterial(relation.getCdprMaterial());
            targetRelation.setCdprIsPackBox(relation.getCdprIsPackBox());
            targetRelation.setCdprIsLableIn(relation.getCdprIsLableIn());
            targetRelation.setCdprIsLableOut(relation.getCdprIsLableOut());
            targetRelation.setCdprLength(relation.getCdprLength());
            targetRelation.setCdprWeight(relation.getCdprWeight());
            targetRelation.setCdprHighth(relation.getCdprHeight());
            targetRelation.setCdprWeight(relation.getCdprWeight());
            targetRelation.setCdprVolume(relation.getCdprVolume());
            targetRelation.setCdprTi(relation.getCdprTi());
            targetRelation.setCdprHi(relation.getCdprHi());
            targetRelation.setCdprIsDefault(relation.getCdprIsDefault());
            targetRelation.setCdprIsReview(relation.getCdprIsReview());
            targetRelation.setCdprWhCode(relation.getCdprWhCode());
            targetRelation.setTimeZone(relation.getTimeZone());
            targetRelation.setDataSet(entity.getDataSet());
            targetRelation.setCdprCdpaPmCode(target.getPmCode());
            targetRelation.setPmCode(target.getCdpaCode());
            relations.add(targetRelation);
        }
        target.setPackageDetailList(relations);
        return target;
    }

    public static SysWmsPackage convertToWms(SysCommonPackage entity, SysWmsPackage old) {
        boolean isNew = old == null;
        SysWmsPackage target = old;
        if (isNew) {
            target = new SysWmsPackage();
            target.setId(IdGen.uuid());
            target.setCreateBy(entity.getCreateBy());
            target.setCreateDate(entity.getCreateDate());
            target.setPmCode(IdGen.uuid());
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setCdpaCode(entity.getCdpaCode());
        target.setCdpaIsUse(entity.getCdpaIsUse());
        target.setCdpaType(entity.getCdpaType());
        target.setCdpaIsUse(entity.getCdpaIsUse());
        target.setCdpaFormat(entity.getCdpaFormat());
        target.setCdpaFormatEn(entity.getCdpaFormatEn());
        target.setCdpaDesc(entity.getCdpaDesc());
        target.setCdpaWhCode(entity.getCdpaWhCode());
        target.setDataSet(entity.getDataSet());
        List<SysWmsPackageRelation> relations = Lists.newArrayList();
        for (SysCommonPackageRelation relation : entity.getPackageDetailList()) {
            SysWmsPackageRelation targetRelation = new SysWmsPackageRelation();
            if (isNew) {
                targetRelation.setId(IdGen.uuid());
                targetRelation.setCreateBy(relation.getCreateBy());
                targetRelation.setCreateDate(relation.getCreateDate());
            } else {
                targetRelation.setId("");
            }
            targetRelation.setUpdateBy(relation.getUpdateBy());
            targetRelation.setUpdateDate(relation.getUpdateDate());
            targetRelation.setCdprUnit(relation.getCdprUnit());
            targetRelation.setCdprUnitRate(relation.getCdprUnitRate());
            targetRelation.setCdprIsMain(relation.getCdprIsMain());
            targetRelation.setCdprSequencesNo(relation.getCdprSequencesNo());
            targetRelation.setCdprUnitLevel(relation.getCdprUnitLevel());
            targetRelation.setCdprQuantity(relation.getCdprQuantity());
            targetRelation.setCdprDesc(relation.getCdprDesc());
            targetRelation.setCdprMaterial(relation.getCdprMaterial());
            targetRelation.setCdprIsPackBox(relation.getCdprIsPackBox());
            targetRelation.setCdprIsLableIn(relation.getCdprIsLableIn());
            targetRelation.setCdprIsLableOut(relation.getCdprIsLableOut());
            targetRelation.setCdprLength(relation.getCdprLength());
            targetRelation.setCdprWeight(relation.getCdprWeight());
            targetRelation.setCdprHighth(relation.getCdprHeight());
            targetRelation.setCdprWeight(relation.getCdprWeight());
            targetRelation.setCdprVolume(relation.getCdprVolume());
            targetRelation.setCdprTi(relation.getCdprTi());
            targetRelation.setCdprHi(relation.getCdprHi());
            targetRelation.setCdprIsDefault(relation.getCdprIsDefault());
            targetRelation.setCdprIsReview(relation.getCdprIsReview());
            targetRelation.setCdprWhCode(relation.getCdprWhCode());
            targetRelation.setDataSet(relation.getDataSet());
            targetRelation.setCdprCdpaPmCode(target.getPmCode());
            targetRelation.setPmCode(target.getCdpaCode());
            relations.add(targetRelation);
        }
        target.setPackageDetailList(relations);
        return target;
    }

    /**
     * BMS客户转结算对象
     */
    public static SysBmsSettleObject convert(SysBmsCustomer entity, SysBmsSettleObject old) {
        boolean isNew = old == null;
        SysBmsSettleObject target = old;
        if (isNew) {
            target = new SysBmsSettleObject();
        }
        target.setUpdateBy(entity.getUpdateBy());
        target.setUpdateDate(entity.getUpdateDate());
        target.setRemarks(entity.getRemarks());
        target.setSettleObjectCode(entity.getEbcuCustomerNo());
        target.setSettleObjectName(entity.getEbcuNameCn());
        target.setTelephone(entity.getEbcuTel());
        target.setAddress(entity.getEbcuAddress());
        return target;
    }
}
