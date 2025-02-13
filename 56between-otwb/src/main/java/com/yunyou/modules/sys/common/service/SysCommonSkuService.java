package com.yunyou.modules.sys.common.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuImportEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonSkuMapper;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品信息Service
 *
 * @author WMJ
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class SysCommonSkuService extends CrudService<SysCommonSkuMapper, SysCommonSku> {
    @Autowired
    private SysCommonSkuSupplierService sysCommonSkuSupplierService;
    @Autowired
    private SysCommonSkuBarcodeService sysCommonSkuBarcodeService;
    @Autowired
    private SysCommonSkuLocService sysCommonSkuLocService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;
    @Autowired
    private SysCommonCustomerService sysCommonCustomerService;
    @Autowired
    private SysCommonPackageService sysCommonPackageService;
    @Autowired
    private SysCommonPackageRelationService sysCommonPackageRelationService;
    @Autowired
    private SysWmsZoneService sysWmsZoneService;
    @Autowired
    private SysWmsLocService sysWmsLocService;

    @Override
    public Page<SysCommonSku> findPage(Page<SysCommonSku> page, SysCommonSku entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysCommonSku> findGrid(Page<SysCommonSku> page, SysCommonSku entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysCommonSku> findSync(SysCommonSku qEntity) {
        List<SysCommonSku> entities = mapper.findSync(qEntity);
        for (SysCommonSku entity : entities) {
            entity.setSkuSupplierList(sysCommonSkuSupplierService.findList(new SysCommonSkuSupplier(entity)));
            entity.setSkuBarcodeList(sysCommonSkuBarcodeService.findList(new SysCommonSkuBarcode(null, entity.getId(), entity.getDataSet())));
            entity.setSkuLocList(sysCommonSkuLocService.findList(new SysCommonSkuLoc(null, entity.getId(), entity.getDataSet())));
        }
        return entities;
    }

    @Override
    public SysCommonSku get(String id) {
        SysCommonSku sysCommonSku = super.get(id);
        if (sysCommonSku != null) {
            sysCommonSku.setSkuSupplierList(sysCommonSkuSupplierService.findList(new SysCommonSkuSupplier(sysCommonSku)));
            sysCommonSku.setSkuBarcodeList(sysCommonSkuBarcodeService.findList(new SysCommonSkuBarcode(null, sysCommonSku.getId(), sysCommonSku.getDataSet())));
            sysCommonSku.setSkuLocList(sysCommonSkuLocService.findList(new SysCommonSkuLoc(null, sysCommonSku.getId(), sysCommonSku.getDataSet())));
        }
        return sysCommonSku;
    }

    public SysCommonSku getByCode(String ownerCode, String skuCode, String dataSet) {
        SysCommonSku con = new SysCommonSku();
        con.setOwnerCode(ownerCode);
        con.setSkuCode(skuCode);
        con.setDataSet(dataSet);
        List<SysCommonSku> list = mapper.findList(con);
        if (CollectionUtil.isNotEmpty(list)) {
            SysCommonSku sysCommonSku = list.get(0);
            if (sysCommonSku != null) {
                sysCommonSku.setSkuSupplierList(sysCommonSkuSupplierService.findList(new SysCommonSkuSupplier(sysCommonSku)));
                sysCommonSku.setSkuBarcodeList(sysCommonSkuBarcodeService.findList(new SysCommonSkuBarcode(null, sysCommonSku.getId(), sysCommonSku.getDataSet())));
                sysCommonSku.setSkuLocList(sysCommonSkuLocService.findList(new SysCommonSkuLoc(null, sysCommonSku.getId(), sysCommonSku.getDataSet())));
            }
            return sysCommonSku;
        } else {
            return null;
        }
    }

    /**
     * 新增商品时设置默认参数
     */
    public void setSkuDefault(SysCommonSku entity) {
        if (StringUtils.isBlank(entity.getPackCode())) {
            entity.setPackCode("01");
        }
        if (StringUtils.isBlank(entity.getLotCode())) {
            entity.setLotCode("01");
        }
        if (StringUtils.isBlank(entity.getReserveCode())) {
            entity.setReserveCode(WmsCodeMaster.RESERVE_B_RCV_ONE.getCode());
        }
        if (StringUtils.isBlank(entity.getPaRule())) {
            entity.setPaRule("01");
        }
        if (StringUtils.isBlank(entity.getRotationRule())) {
            entity.setRotationRule("01");
        }
        if (StringUtils.isBlank(entity.getAllocRule())) {
            entity.setAllocRule("01");
        }
        if (entity.getFilingTime() == null) {
            entity.setFilingTime(new Date());
        }
        if (StringUtils.isBlank(entity.getStockCurId())) {
            entity.setStockCurId("CNY");
        }
        if (StringUtils.isBlank(entity.getRcvUom())) {
            entity.setRcvUom("EA");
        }
        if (StringUtils.isBlank(entity.getShipUom())) {
            entity.setShipUom("EA");
        }
        if (StringUtils.isBlank(entity.getPrintUom())) {
            entity.setPrintUom("EA");
        }
        if (StringUtils.isBlank(entity.getIsIqc())) {
            entity.setIsIqc("N");
        }
        if (StringUtils.isBlank(entity.getIsIces())) {
            entity.setIsIces("N");
        }
        if (StringUtils.isBlank(entity.getIsValidity())) {
            entity.setIsValidity("N");
        }
        if (StringUtils.isBlank(entity.getIsOverRcv())) {
            entity.setIsOverRcv("N");
        }
        if (StringUtils.isBlank(entity.getIsDg())) {
            entity.setIsDg("N");
        }
        if (StringUtils.isBlank(entity.getIsCold())) {
            entity.setIsCold("N");
        }
        if (StringUtils.isBlank(entity.getIsSerial())) {
            entity.setIsSerial("N");
        }
        if (StringUtils.isBlank(entity.getIsParent())) {
            entity.setIsParent("N");
        }
        if (StringUtils.isBlank(entity.getIsQc())) {
            entity.setIsQc("N");
        }
    }

    @Override
    @Transactional
    public void save(SysCommonSku entity) {
        this.setSkuDefault(entity);
        super.save(entity);
        for (SysCommonSkuSupplier sysCommonSkuSupplier : entity.getSkuSupplierList()) {
            if (sysCommonSkuSupplier.getId() == null || StringUtils.isBlank(sysCommonSkuSupplier.getSupplierCode())) {
                continue;
            }
            sysCommonSkuSupplier.setSku(entity);
            sysCommonSkuSupplier.setDataSet(entity.getDataSet());
            sysCommonSkuSupplierService.save(sysCommonSkuSupplier);
        }
        for (SysCommonSkuBarcode sysCommonSkuBarcode : entity.getSkuBarcodeList()) {
            if (sysCommonSkuBarcode.getId() == null || StringUtils.isBlank(sysCommonSkuBarcode.getBarcode())) {
                continue;
            }
            sysCommonSkuBarcode.setOwnerCode(entity.getOwnerCode());
            sysCommonSkuBarcode.setSkuCode(entity.getSkuCode());
            sysCommonSkuBarcode.setHeaderId(entity.getId());
            sysCommonSkuBarcode.setDataSet(entity.getDataSet());
            sysCommonSkuBarcodeService.save(sysCommonSkuBarcode);
        }
        for (SysCommonSkuLoc sysCommonSkuLoc : entity.getSkuLocList()) {
            if (sysCommonSkuLoc.getId() == null || StringUtils.isBlank(sysCommonSkuLoc.getLocCode())) {
                continue;
            }
            sysCommonSkuLoc.setOwnerCode(entity.getOwnerCode());
            sysCommonSkuLoc.setSkuCode(entity.getSkuCode());
            sysCommonSkuLoc.setHeaderId(entity.getId());
            sysCommonSkuLoc.setDataSet(entity.getDataSet());
            sysCommonSkuLocService.save(sysCommonSkuLoc);
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.syncSku(Collections.singletonList(entity));
        }
    }

    @Override
    @Transactional
    public void delete(SysCommonSku entity) {
        sysCommonSkuSupplierService.delete(new SysCommonSkuSupplier(entity));
        sysCommonSkuBarcodeService.delete(new SysCommonSkuBarcode(null, entity.getId(), entity.getDataSet()));
        sysCommonSkuLocService.delete(new SysCommonSkuLoc(null, entity.getId(), entity.getDataSet()));
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.removeSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getDataSet());
        }
    }

    @Transactional
    public void batchInsert(List<SysCommonSku> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }

        List<SysCommonSkuSupplier> skuSupplierList = Lists.newArrayList();
        list.stream().map(SysCommonSku::getSkuSupplierList).filter(CollectionUtil::isNotEmpty).forEach(skuSupplierList::addAll);
        sysCommonSkuSupplierService.batchInsert(skuSupplierList);

        List<SysCommonSkuBarcode> skuBarcodeList = Lists.newArrayList();
        list.stream().map(SysCommonSku::getSkuBarcodeList).filter(CollectionUtil::isNotEmpty).forEach(skuBarcodeList::addAll);
        sysCommonSkuBarcodeService.batchInsert(skuBarcodeList);
    }

    @Transactional
    public ResultMessage importSku(List<SysCommonSkuImportEntity> list) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        List<SysCommonSku> result = Lists.newArrayList();
        int index = 2;
        for (SysCommonSkuImportEntity entity : list) {
            StringBuilder checkNull = new StringBuilder();
            if (StringUtils.isBlank(entity.getOwnerCode())) {
                checkNull.append("货主编码为空!");
            }
            if (StringUtils.isBlank(entity.getSkuCode())) {
                checkNull.append("商品编码为空!");
            }
            if (StringUtils.isBlank(entity.getSkuName())) {
                checkNull.append("商品名称为空!");
            }
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                break;
            }
            StringBuilder checkExist = new StringBuilder();
            SysCommonCustomer sysCommonCustomer = sysCommonCustomerService.getByCode(entity.getOwnerCode(), entity.getDataSet());
            if (null == sysCommonCustomer) {
                checkExist.append("货主编码不存在!");
            } else if (!sysCommonCustomer.getType().contains(CustomerType.OWNER.getCode())) {
                checkExist.append("货主编码无效!");
            }
            SysCommonSku sysCommonSku = this.getByCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getDataSet());
            if (null != sysCommonSku) {
                checkExist.append("商品编码已存在!");
            }
            if (StringUtils.isNotBlank(entity.getPaZone())) {
                SysWmsZone paZone = sysWmsZoneService.getByCode(entity.getPaZone(), entity.getDataSet());
                if (null == paZone) {
                    checkExist.append("上架库区编码不存在!");
                }
            }
            if (StringUtils.isNotBlank(entity.getPaLoc())) {
                SysWmsLoc paLoc = sysWmsLocService.getByCode(entity.getPaLoc(), entity.getDataSet());
                if (null == paLoc) {
                    checkExist.append("上架库位编码不存在!");
                }
            }
            if (StringUtils.isNotBlank(entity.getReserveCode())) {
                List<String> ruleType = Arrays.asList(WmsCodeMaster.RESERVE_MAN.getCode(), WmsCodeMaster.RESERVE_RCV.getCode(), WmsCodeMaster.RESERVE_PA.getCode(),
                        WmsCodeMaster.RESERVE_B_RCV_ONE.getCode(), WmsCodeMaster.RESERVE_B_RCV_TWO.getCode());
                if (!ruleType.contains(entity.getReserveCode())) {
                    checkExist.append("上架库位指定规则值填写错误!");
                }
            }
            if (StringUtils.isNotBlank(entity.getSupplierCode())) {
                SysCommonCustomer supplier = sysCommonCustomerService.getByCode(entity.getSupplierCode(), entity.getDataSet());
                if (supplier == null) {
                    checkExist.append("供应商编码不存在!");
                }
            }
            if (StringUtils.isNotBlank(entity.getBarCode())) {
                SysCommonSkuBarcode sysCommonSkuBarcode = sysCommonSkuBarcodeService.getByOwnerAndBar(entity.getOwnerCode(), entity.getBarCode(), entity.getDataSet());
                if (sysCommonSkuBarcode != null) {
                    checkExist.append("条码已存在在商品[").append(sysCommonSkuBarcode.getSkuCode()).append("]中！");
                }
            }
            if (StringUtils.isNotBlank(entity.getSkuType())) {
                String skuType = DictUtils.getDictValue(entity.getSkuType(), "sku_type", null);
                if (StringUtils.isBlank(skuType)) {
                    checkExist.append("课别不存在！");
                } else {
                    entity.setSkuType(skuType);
                }
            }
            if (StringUtils.isNotBlank(entity.getSkuWeighType())) {
                String weighType = DictUtils.getDictValue(entity.getSkuWeighType(), "weighing_type", null);
                if (StringUtils.isBlank(weighType)) {
                    checkExist.append("称重类型不存在！");
                } else {
                    entity.setSkuWeighType(weighType);
                }
            }
            if (entity.getCsQty() != null && BigDecimal.ZERO.compareTo(entity.getCsQty()) >= 0) {
                checkExist.append("箱含量必须大于0！");
            }
            if (entity.getPlQty() != null && BigDecimal.ZERO.compareTo(entity.getPlQty()) >= 0) {
                checkExist.append("托含量必须大于0！");
            }
            if (StringUtils.isNotEmpty(checkExist.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkExist.toString()).append("<br>");
                break;
            }
            result.add(this.getImportSkuDetail(entity));
            index++;
        }
        Map<String, List<SysCommonSku>> collect = result.stream().collect(Collectors.groupingBy(c -> "货主[" + c.getOwnerCode() + "]商品[" + c.getSkuCode() + "]"));
        for (Map.Entry<String, List<SysCommonSku>> entry : collect.entrySet()) {
            if (entry.getValue().size() > 1) {
                errorMsg.append(entry.getKey()).append("存在重复行<br>");
            }
        }
        if (errorMsg.length() > 0) {
            msg.setSuccess(false);
            msg.setMessage(errorMsg.toString());
            return msg;
        }
        for (SysCommonSku sysCommonSku : result) {
            this.save(sysCommonSku);
        }
        msg.setMessage("导入成功");
        return msg;
    }

    private SysCommonSku getImportSkuDetail(SysCommonSkuImportEntity importEntity) {
        SysCommonSku sysCommonSku = new SysCommonSku();
        BeanUtils.copyProperties(importEntity, sysCommonSku);
        sysCommonSku.setId(null);
        sysCommonSku.setIsNewRecord(false);
        sysCommonSku.setRcvUom(importEntity.getUom());
        sysCommonSku.setShipUom(importEntity.getUom());
        sysCommonSku.setPrintUom(importEntity.getUom());
        sysCommonSku.setPaRule(importEntity.getPaRule());
        sysCommonSku.setRotationRule(importEntity.getRotationRule());
        sysCommonSku.setAllocRule(importEntity.getAllocRule());

        double csQty = importEntity.getCsQty() == null || BigDecimal.ZERO.compareTo(importEntity.getCsQty()) >= 0 ? 1d : importEntity.getCsQty().doubleValue();
        double plQty = importEntity.getPlQty() == null || BigDecimal.ZERO.compareTo(importEntity.getPlQty()) >= 0 ? 1d : importEntity.getPlQty().doubleValue();
        String packCode = 1 + "/" + ((int) Math.ceil(csQty)) + "/" + ((int) Math.ceil(plQty));
        sysCommonSku.setPackCode(packCode);
        double ti = importEntity.getTi() == null ? 0d : importEntity.getTi().doubleValue();
        double hi = importEntity.getHi() == null ? 0d : importEntity.getHi().doubleValue();
        SysCommonPackageRelation cs = sysCommonPackageRelationService.findByPackageUom(sysCommonSku.getPackCode(), "CS", sysCommonSku.getDataSet());
        if (cs == null) {
            SysCommonPackage sysCommonPackage = new SysCommonPackage();
            sysCommonPackage.setCdpaCode(sysCommonSku.getPackCode());
            sysCommonPackage.setCdpaType("M_P_G");
            sysCommonPackage.setCdpaFormat("1/" + ((int) Math.ceil(csQty)) + "/" + ((int) Math.ceil(plQty)));
            sysCommonPackage.setCdpaFormatEn(sysCommonSku.getPackCode());
            sysCommonPackage.setCdpaIsUse("0");
            sysCommonPackage.setDataSet(sysCommonSku.getDataSet());
            List<SysCommonPackageRelation> sysCommonPackageRelations = sysCommonPackageRelationService.initialList();
            for (SysCommonPackageRelation sysCommonPackageRelation : sysCommonPackageRelations) {
                if ("CS".equals(sysCommonPackageRelation.getCdprUnitLevel())) {
                    sysCommonPackageRelation.setCdprQuantity(csQty);
                }
                if ("PL".equals(sysCommonPackageRelation.getCdprUnitLevel())) {
                    sysCommonPackageRelation.setCdprQuantity(plQty);
                    sysCommonPackageRelation.setCdprTi(ti);
                    sysCommonPackageRelation.setCdprHi(hi);
                }
            }
            sysCommonPackage.setPackageDetailList(sysCommonPackageRelations);
            sysCommonPackageService.save(sysCommonPackage);
        }
        if (StringUtils.isNotBlank(importEntity.getSupplierCode())) {
            SysCommonCustomer supplier = sysCommonCustomerService.getByCode(importEntity.getSupplierCode(), importEntity.getDataSet());
            SysCommonSkuSupplier sysCommonSkuSupplier = new SysCommonSkuSupplier("");
            sysCommonSkuSupplier.setSku(sysCommonSku);
            sysCommonSkuSupplier.setSupplierCode(importEntity.getSupplierCode());
            sysCommonSkuSupplier.setSupplierName(supplier.getName());
            sysCommonSkuSupplier.setIsDefault("Y");
            sysCommonSkuSupplier.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
            sysCommonSkuSupplier.setDataSet(importEntity.getDataSet());
            sysCommonSku.setSkuSupplierList(Lists.newArrayList(sysCommonSkuSupplier));
        }
        if (StringUtils.isNotBlank(importEntity.getBarCode())) {
            SysCommonSkuBarcode sysCommonSkuBarcode = new SysCommonSkuBarcode("");
            sysCommonSkuBarcode.setOwnerCode(importEntity.getOwnerCode());
            sysCommonSkuBarcode.setSkuCode(importEntity.getSkuCode());
            sysCommonSkuBarcode.setBarcode(importEntity.getBarCode());
            sysCommonSkuBarcode.setIsDefault("Y");
            sysCommonSkuBarcode.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
            sysCommonSkuBarcode.setDataSet(importEntity.getDataSet());
            sysCommonSku.setSkuBarcodeList(Lists.newArrayList(sysCommonSkuBarcode));
        }
        return sysCommonSku;
    }

}