package com.yunyou.modules.wms.basicdata.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.entity.extend.BanQinCdWhSkuImportEntity;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhSkuMapper;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailService;
import com.yunyou.modules.wms.inbound.service.BanQinWmPoDetailService;
import com.yunyou.modules.wms.inventory.service.BanQinWmAdDetailService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotService;
import com.yunyou.modules.wms.inventory.service.BanQinWmTfDetailService;
import com.yunyou.modules.wms.kit.service.BanQinCdWhBomHeaderService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSaleDetailService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhSkuService extends CrudService<BanQinCdWhSkuMapper, BanQinCdWhSku> {
    @Autowired
    @Lazy
    private BanQinWmInvLotService wmInvLotService;
    @Autowired
    @Lazy
    private BanQinCdWhBomHeaderService cdWhBomHeaderService;
    @Autowired
    @Lazy
    private BanQinWmAsnDetailService wmAsnDetailService;
    @Autowired
    @Lazy
    private BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    @Lazy
    private BanQinWmTfDetailService wmTfDetailService;
    @Autowired
    @Lazy
    private BanQinWmAdDetailService wmAdDetailService;
    @Autowired
    @Lazy
    private BanQinWmTaskPaService wmTaskPaService;
    @Autowired
    @Lazy
    private BanQinWmSaleDetailService wmSaleDetailService;
    @Autowired
    @Lazy
    private BanQinWmPoDetailService wmPoDetailService;
    @Autowired
    private BanQinCdWhSkuLocService cdWhSkuLocService;
    @Autowired
    private BanQinCdWhSkuBarcodeService cdWhSkuBarcodeService;
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinCdWhPackageService cdWhPackageService;
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;
    @Autowired
    private BanQinCdWhZoneService cdWhZoneService;
    @Autowired
    private BanQinCdWhLocService cdWhLocService;

    public BanQinCdWhSkuEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    @SuppressWarnings("unchecked")
    public Page<BanQinCdWhSkuEntity> findPage(Page page, BanQinCdWhSkuEntity banQinCdWhSkuEntity) {
        dataRuleFilter(banQinCdWhSkuEntity);
        banQinCdWhSkuEntity.setPage(page);
        page.setList(mapper.findPage(banQinCdWhSkuEntity));
        return page;
    }

    public BanQinCdWhSku findFirst(BanQinCdWhSku banQinCdWhSku) {
        List<BanQinCdWhSku> list = findList(banQinCdWhSku);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Transactional
    public void saveEntity(BanQinCdWhSkuEntity entity) {
        ResultMessage msg = new ResultMessage();
        if (StringUtils.isNotEmpty(entity.getId())) {
            BanQinCdWhSku model = get(entity.getId());
            // 校验商品是否存在序列号库存，如果存在，则不能修改是否序列号管理属性
            String isSerial = StringUtils.isEmpty(entity.getIsSerial()) ? WmsConstants.NO : entity.getIsSerial();
            String isSerialOld = StringUtils.isEmpty(model.getIsSerial()) ? WmsConstants.NO : model.getIsSerial();
            if (!isSerial.equals(isSerialOld)) {
                // 是否存在序列号库存
                List<Object> invSerial = mapper.execSelectSql("select 1 from wm_inv_serial where owner_code = '" + entity.getOwnerCode() + "' and sku_code = '" + entity.getSkuCode() + "' and org_id = '" + entity.getOrgId() + "'");
                // 是否存在库存
                List<Object> inv = mapper.execSelectSql("select 1 from wm_inv_lot_loc where owner_code = '" + entity.getOwnerCode() + "' and sku_code = '" + entity.getSkuCode() + "' and org_id = '" + entity.getOrgId() + "'");
                if (invSerial.size() > 0) {
                    throw new WarehouseException("商品已存在序列号库存，[是否序列号管理]属性不能修改");
                } else if (inv.size() > 0) {
                    throw new WarehouseException("商品已存在非序列号库存，[是否序列号管理]属性不能修改");
                }
            }
            // 判断商品是否存在待检库存
            String isQc = StringUtils.isEmpty(entity.getIsQc()) ? WmsConstants.NO : entity.getIsQc();
            String isQcOld = StringUtils.isEmpty(model.getIsQc()) ? WmsConstants.NO : model.getIsQc();
            if (!isQc.equals(isQcOld)) {
                boolean isToQc = wmInvLotService.checkIsToQc(entity.getOwnerCode(), entity.getSkuCode(), isQc, entity.getOrgId());
                if (isToQc) {
                    msg.setSuccess(false);
                    if (WmsConstants.NO.equals(isQc)) {
                        throw new WarehouseException("商品存在待检库存，[是否质检管理]属性不能修改");
                    } else {
                        throw new WarehouseException("商品存在质检库存，[是否质检管理]属性不能修改");
                    }
                }
            }
            // 校验质检阶段为收货前质检时，上架库位指定规则为：人工指定、收货时计算、上架时计算；
            // 质检阶段为收货后上架前质检时，上架库位指定规则为：人工指定、上架时计算
            if (WmsConstants.YES.equals(isQc)) {
                if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(entity.getQcPhase())) {
                    if (!(WmsCodeMaster.RESERVE_MAN.getCode().equals(entity.getReserveCode()) || WmsCodeMaster.RESERVE_PA.getCode().equals(entity.getReserveCode()))) {
                        throw new WarehouseException("收货后上架前质检，上架库位指定规则需为人工指定库位或上架时计算库位");
                    }
                } else {
                    if (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(entity.getReserveCode()) || WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(entity.getReserveCode())) {
                        throw new WarehouseException("收货前质检，上架库位指定规则不能为收货前计算库位");
                    }
                }
            }
            // 校验是否已存在父件
            String isParent = StringUtils.isEmpty(entity.getIsParent()) ? WmsConstants.NO : entity.getIsParent();
            String isParentOld = StringUtils.isEmpty(model.getIsParent()) ? WmsConstants.NO : model.getIsParent();
            if (!isParent.equals(isParentOld) && WmsConstants.NO.equals(isParent)) {
                if (cdWhBomHeaderService.checkIsParent(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId())) {
                    throw new WarehouseException("商品存在组合件，[是否父件]属性不能修改");
                }
            }
        }
        this.save(entity);
    }

    /**
     * 根据货主、商品编号查询商品信息
     */
    public BanQinCdWhSku getByOwnerAndSkuCode(String ownerCode, String skuCode, String orgId) {
        BanQinCdWhSku example = new BanQinCdWhSku();
        example.setSkuCode(skuCode);
        example.setOwnerCode(ownerCode);
        example.setOrgId(orgId);
        return findFirst(example);
    }

    public List<BanQinCdWhSku> getExistSku(String ownerSkuList, String orgId) {
        return mapper.getExistSku(ownerSkuList, orgId);
    }

    /**
     * 删除商品
     */
    @Transactional
    public ResultMessage removeSkuEntity(String[] skuIds) {
        ResultMessage msg = new ResultMessage();
        boolean havEx = false;
        for (String id : skuIds) {
            try {
                BanQinCdWhSku model = this.get(id);
                if (null == model) {
                    continue;
                }
                msg = this.checkSkuHeader(model);
                if (msg.isSuccess()) {
                    msg = this.removeSku(model);
                } else {
                    msg.addMessage("商品[" + model.getSkuCode() + "]" + "已经被引用，不能删除<br>");
                    havEx = true;
                }
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage(e.getMessage());
            }
        }
        if (havEx) {
            msg.setSuccess(false);
        } else {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 对删除的数据进行删除验证
     */
    public ResultMessage checkSkuHeader(BanQinCdWhSku model) {
        ResultMessage msg;
        msg = this.wmAsnDetailService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmInvLotService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmPoDetailService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmSoDetailService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmTfDetailService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmAdDetailService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmTaskPaService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg = this.wmSaleDetailService.getBySkuCodeAndOwnerCode(model.getOwnerCode(), model.getSkuCode(), model.getOrgId());
        if (!msg.isSuccess()) {
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    @Transactional
    public ResultMessage removeSku(BanQinCdWhSku cdWhSkuModel) {
        ResultMessage msg = new ResultMessage();
        if (null == cdWhSkuModel) {
            throw new WarehouseException("商品不存在");
        }
        cdWhSkuLocService.deleteByHeaderId(cdWhSkuModel.getId());
        cdWhSkuBarcodeService.deleteByHeadId(cdWhSkuModel.getId());
        this.delete(cdWhSkuModel);
        return msg;
    }

    @Transactional
    public ResultMessage importSku(List<BanQinCdWhSkuImportEntity> list) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        List<BanQinCdWhSku> result = Lists.newArrayList();
        int index = 2;
        for (BanQinCdWhSkuImportEntity entity : list) {
            StringBuilder checkNull = checkNullForImport(entity);
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                break;
            }
            StringBuilder checkExist = checkIsExistForImport(entity);
            if (StringUtils.isNotEmpty(checkExist.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkExist.toString()).append("<br>");
                break;
            }
            index++;

            BanQinCdWhSku cdWhSku = new BanQinCdWhSku();
            BeanUtils.copyProperties(entity, cdWhSku);
            setSkuDetail(cdWhSku, entity);
            result.add(cdWhSku);
        }
        Map<String, List<BanQinCdWhSku>> collect = result.stream().collect(Collectors.groupingBy(c -> "货主[" + c.getOwnerCode() + "]商品[" + c.getSkuCode() + "]"));
        for (Map.Entry<String, List<BanQinCdWhSku>> entry : collect.entrySet()) {
            if (entry.getValue().size() > 1) {
                errorMsg.append(entry.getKey()).append("存在重复行<br>");
            }
        }
        if (errorMsg.length() > 0) {
            msg.setSuccess(false);
            msg.setMessage(errorMsg.toString());
            return msg;
        }
        for (BanQinCdWhSku cdWhSku : result) {
            this.save(cdWhSku);
        }
        msg.setMessage("导入成功");
        return msg;
    }

    private StringBuilder checkNullForImport(BanQinCdWhSkuImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(entity.getOwnerCode())) {
            builder.append("货主编码为空!");
        }
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
        if (StringUtils.isEmpty(entity.getSkuName())) {
            builder.append("商品名称为空!");
        }
        if (StringUtils.isEmpty(entity.getReserveCode())) {
            builder.append("上架库位指定规则不能为空!");
        }
        return builder;
    }

    private StringBuilder checkIsExistForImport(BanQinCdWhSkuImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        BanQinEbCustomer owner = ebCustomerService.find(entity.getOwnerCode(), CustomerType.OWNER.getCode(), entity.getOrgId());
        if (null == owner) {
            builder.append("货主编码不存在!");
        }
        BanQinCdWhSku item = this.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        if (null != item) {
            builder.append("商品编码已存在!");
        }
        if (StringUtils.isNotBlank(entity.getPaZone())) {
            BanQinCdWhZone paZone = cdWhZoneService.getByCode(entity.getPaZone(), entity.getOrgId());
            if (null == paZone) {
                builder.append("上架库区编码不存在!");
            }
        }
        if (StringUtils.isNotBlank(entity.getPaLoc())) {
            BanQinCdWhLoc paLoc = cdWhLocService.findByLocCode(entity.getPaLoc(), entity.getOrgId());
            if (null == paLoc) {
                builder.append("上架库位编码不存在!");
            }
        }
        List<String> ruleType = Arrays.asList(WmsCodeMaster.RESERVE_MAN.getCode(), WmsCodeMaster.RESERVE_RCV.getCode(), WmsCodeMaster.RESERVE_PA.getCode(),
            WmsCodeMaster.RESERVE_B_RCV_ONE.getCode(), WmsCodeMaster.RESERVE_B_RCV_TWO.getCode());
        if (!ruleType.contains(entity.getReserveCode())) {
            builder.append("上架库位指定规则值填写错误!");
        }
        if (StringUtils.isNotEmpty(entity.getDef1()) && entity.getDef1().length() > 64) {
            builder.append("自定义1长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef2()) && entity.getDef2().length() > 64) {
            builder.append("自定义2长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef3()) && entity.getDef3().length() > 64) {
            builder.append("自定义3长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef4()) && entity.getDef4().length() > 64) {
            builder.append("自定义4长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef5()) && entity.getDef5().length() > 64) {
            builder.append("自定义5长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef6()) && entity.getDef6().length() > 64) {
            builder.append("自定义6长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef7()) && entity.getDef7().length() > 64) {
            builder.append("自定义7长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef8()) && entity.getDef8().length() > 64) {
            builder.append("自定义8长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef9()) && entity.getDef9().length() > 64) {
            builder.append("自定义9长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef10()) && entity.getDef10().length() > 64) {
            builder.append("自定义10长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef11()) && entity.getDef11().length() > 64) {
            builder.append("自定义11长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef12()) && entity.getDef12().length() > 64) {
            builder.append("自定义12长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef13()) && entity.getDef13().length() > 64) {
            builder.append("自定义13长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef14()) && entity.getDef14().length() > 64) {
            builder.append("自定义14长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getDef15()) && entity.getDef15().length() > 64) {
            builder.append("自定义15长度不能超过64个字符!");
        }

        return builder;
    }

    private void setSkuDetail(BanQinCdWhSku cdWhSku, BanQinCdWhSkuImportEntity importEntity) {
        cdWhSku.setId(IdGen.uuid());
        cdWhSku.setIsNewRecord(true);
        cdWhSku.setLotCode("01");
        cdWhSku.setRcvUom(StringUtils.isNotBlank(importEntity.getUom()) ? importEntity.getUom() : "EA");
        cdWhSku.setShipUom(StringUtils.isNotBlank(importEntity.getUom()) ? importEntity.getUom() : "EA");
        cdWhSku.setPrintUom(StringUtils.isNotBlank(importEntity.getUom()) ? importEntity.getUom() : "EA");
        cdWhSku.setIsValidity("N");
        cdWhSku.setIsOverRcv("N");
        cdWhSku.setPaRule(StringUtils.isNotBlank(importEntity.getPaRule()) ? importEntity.getPaRule() : "01");
        cdWhSku.setRotationRule(StringUtils.isNotBlank(importEntity.getRotationRule()) ? importEntity.getRotationRule() : "01");
        cdWhSku.setAllocRule(StringUtils.isNotBlank(importEntity.getAllocRule()) ? importEntity.getAllocRule() : "01");
        cdWhSku.setIsDg("N");
        cdWhSku.setIsCold("N");
        cdWhSku.setIsSerial("N");
        cdWhSku.setIsParent("N");
        cdWhSku.setIsQc("N");
        cdWhSku.setIsEnable("Y");
        cdWhSku.setTempLevel(importEntity.getTempLevel());
        cdWhSku.setPackCode(cdWhSku.getSkuCode() + cdWhSku.getOwnerCode());
        BanQinCdWhPackageRelation cs = cdWhPackageRelationService.findByPackageUom(cdWhSku.getPackCode(), "CS", cdWhSku.getOrgId());
        if (cs == null) {
            BanQinCdWhPackage cdWhPackage = new BanQinCdWhPackage();
            cdWhPackage.setCdpaCode(cdWhSku.getPackCode());
            cdWhPackage.setCdpaType("M_P_G");
            cdWhPackage.setCdpaFormat(cdWhSku.getPackCode());
            cdWhPackage.setCdpaIsUse("0");
            cdWhPackage.setCdpaFormatEn(cdWhSku.getPackCode());
            cdWhPackage.setOrgId(cdWhSku.getOrgId());
            cdWhPackage.setRecVer(0);
            cdWhPackage.setCreateBy(UserUtils.getUser());
            cdWhPackage.setCreateDate(new Date());
            cdWhPackage.setUpdateBy(UserUtils.getUser());
            cdWhPackage.setUpdateDate(new Date());
            List<BanQinCdWhPackageRelation> cdWhPackageRelations = cdWhPackageRelationService.initialList();
            for (BanQinCdWhPackageRelation cdWhPackageRelation : cdWhPackageRelations) {
                cdWhPackageRelation.setOrgId(cdWhSku.getOrgId());
                cdWhPackageRelation.setRecVer(0);
                cdWhPackageRelation.setCreateBy(UserUtils.getUser());
                cdWhPackageRelation.setCreateDate(new Date());
                cdWhPackageRelation.setUpdateBy(UserUtils.getUser());
                cdWhPackageRelation.setUpdateDate(new Date());
                if ("CS".equals(cdWhPackageRelation.getCdprUnitLevel())) {
                    cdWhPackageRelation.setCdprQuantity(importEntity.getCsQty().doubleValue());
                }
                if ("PL".equals(cdWhPackageRelation.getCdprUnitLevel())) {
                    cdWhPackageRelation.setCdprQuantity(importEntity.getPlQty() != null ? importEntity.getPlQty().doubleValue() : 1d);
                    cdWhPackageRelation.setCdprTi(importEntity.getCdprTi() != null ? importEntity.getCdprTi().doubleValue() : 0d);
                    cdWhPackageRelation.setCdprHi(importEntity.getCdprHi() != null ? importEntity.getCdprHi().doubleValue() : 0d);
                }
            }
            cdWhPackage.setPackageDetailList(cdWhPackageRelations);
            cdWhPackageService.save(cdWhPackage);
        }
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        cdWhSkuLocService.remove(ownerCode, skuCode, orgId);
        cdWhSkuBarcodeService.remove(ownerCode, skuCode, orgId);
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<BanQinCdWhSku> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}