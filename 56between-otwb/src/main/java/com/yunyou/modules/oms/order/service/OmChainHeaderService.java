package com.yunyou.modules.oms.order.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmClerk;
import com.yunyou.modules.oms.basic.entity.OmCustomer;
import com.yunyou.modules.oms.basic.entity.OmItem;
import com.yunyou.modules.oms.basic.entity.OmPackageRelation;
import com.yunyou.modules.oms.basic.service.OmClerkService;
import com.yunyou.modules.oms.basic.service.OmCustomerService;
import com.yunyou.modules.oms.basic.service.OmItemService;
import com.yunyou.modules.oms.basic.service.OmPackageRelationService;
import com.yunyou.modules.oms.common.BusinessOrderType;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.*;
import com.yunyou.modules.oms.order.entity.extend.OmChainDCImportEntity;
import com.yunyou.modules.oms.order.entity.extend.OmChainImportEntity;
import com.yunyou.modules.oms.order.entity.extend.OmChainWmsImportEntity;
import com.yunyou.modules.oms.order.mapper.OmChainHeaderMapper;
import com.yunyou.modules.oms.report.entity.OmDelayOrderEntity;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 供应链订单Service
 *
 * @author WMJ
 * @version 2019-04-17
 */
@Service
@Transactional(readOnly = true)
public class OmChainHeaderService extends CrudService<OmChainHeaderMapper, OmChainHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OmChainDetailService omChainDetailService;
    @Autowired
    private OmOrderAppendixService omOrderAppendixService;
    @Autowired
    private OmOrderDetailAppendixService omOrderDetailAppendixService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;
    @Autowired
    private OmCustomerService omCustomerService;
    @Autowired
    private OmItemService omItemService;
    @Autowired
    private OmPackageRelationService omPackageRelationService;
    @Autowired
    private OmClerkService omClerkService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;

    @Override
    public OmChainHeader get(String id) {
        OmChainHeader omChainHeader = mapper.get(id);
        if (omChainHeader != null) {
            omChainHeader.setOmChainDetailList(this.findDetails(omChainHeader));
        }
        return omChainHeader;
    }

    public OmChainHeader get(String chainNo, String orgId) {
        OmChainHeader omChainHeader = mapper.getByNo(chainNo, orgId);
        if (omChainHeader != null) {
            omChainHeader.setOmChainDetailList(this.findDetails(omChainHeader));
        }
        return omChainHeader;
    }

    public OmChainHeaderEntity getEntity(String id) {
        OmChainHeaderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setOmChainDetailList(this.findDetails(entity));
        }
        return entity;
    }

    public List<OmChainDetail> findDetails(OmChainHeader omChainHeader) {
        List<OmChainDetail> omChainDetails = omChainDetailService.getDetails(omChainHeader.getId());
        for (OmChainDetail omChainDetail : omChainDetails) {
            BigDecimal qty = omChainDetail.getQty() == null ? BigDecimal.ZERO : omChainDetail.getQty();
            BigDecimal taskQty = omChainDetail.getTaskQty() == null ? BigDecimal.ZERO : omChainDetail.getTaskQty();
            double availableInvQty = omSaleInventoryService.getAvailableQty(omChainHeader.getOwner(), omChainDetail.getSkuCode(), omChainHeader.getWarehouse());

            omChainDetail.setPlanTaskQty(qty.compareTo(taskQty) < 0 ? BigDecimal.ZERO : qty.subtract(taskQty));
            omChainDetail.setAvailableQty(BigDecimal.valueOf(availableInvQty));
        }
        return omChainDetails;
    }

    @SuppressWarnings("unchecked")
    public Page<OmChainHeaderEntity> findPage(Page page, OmChainHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<OmDelayOrderEntity> findDelayOrder(Page page, OmDelayOrderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findDelayOrder(entity));
        return page;
    }

    public void checkSaveInfo(OmChainHeader omChainHeader) {
        if (!OmsConstants.OMS_CO_STATUS_00.equals(omChainHeader.getStatus())) {
            throw new OmsException("订单状态必须为新建状态");
        }
        if (omChainHeader.getOrderDate() == null) {
            throw new OmsException("订单日期不能为空");
        }
        if (StringUtils.isBlank(omChainHeader.getOwner())) {
            throw new OmsException("货主不能为空");
        }
        if (StringUtils.isBlank(omChainHeader.getBusinessOrderType())) {
            throw new OmsException("业务订单类型不能为空");
        }
        if (CollectionUtil.isNotEmpty(omChainHeader.getOmChainDetailList())) {
            for (OmChainDetail omChainDetail : omChainHeader.getOmChainDetailList()) {
                if (omChainDetail.getId() == null) {
                    continue;
                }
                if (StringUtils.isBlank(omChainDetail.getSkuCode())) {
                    throw new OmsException("商品编码不能为空");
                }
                if (omChainDetail.getQty() == null) {
                    throw new OmsException("数量不能为空");
                }
            }
        }
    }

    @Override
    @Transactional
    public void save(OmChainHeader omChainHeader) {
        if (StringUtils.isBlank(omChainHeader.getId())) {
            if (StringUtils.isBlank(omChainHeader.getChainNo())) {
                omChainHeader.setChainNo(noService.getDocumentNo(GenNoType.OM_CHAIN_NO.name()));
            }
            omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
            if (StringUtils.isBlank(omChainHeader.getPreparedBy())) {
                omChainHeader.setPreparedBy(UserUtils.getUser().getName());
            }
            if (StringUtils.isBlank(omChainHeader.getHandleStatus())) {
                omChainHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_N);
            }
            if (StringUtils.isBlank(omChainHeader.getInterceptStatus())) {
                omChainHeader.setInterceptStatus(OmsConstants.OMS_INTERCEPT_STATUS_00);
            }
        }
        super.save(omChainHeader);
        for (OmChainDetail omChainDetail : omChainHeader.getOmChainDetailList()) {
            if (omChainDetail.getId() == null) {
                continue;
            }
            if (StringUtils.isBlank(omChainDetail.getLineNo())) {
                omChainDetail.setLineNo(omChainDetailService.getNewLineNo(omChainHeader.getId(), omChainHeader.getOrgId()));
            }
            omChainDetail.setHeaderId(omChainHeader.getId());
            omChainDetail.setChainNo(omChainHeader.getChainNo());
            omChainDetail.setOrgId(omChainHeader.getOrgId());
            if (OmChainDetail.DEL_FLAG_NORMAL.equals(omChainDetail.getDelFlag())) {
                omChainDetailService.save(omChainDetail);
            } else {
                omChainDetailService.delete(omChainDetail);
                omOrderDetailAppendixService.deleteByOrderNoAndLine(omChainDetail.getChainNo(), omChainDetail.getLineNo(), omChainDetail.getOrgId());
            }
        }
    }

    /**
     * 描述：审核
     */
    @Transactional
    public void audit(OmChainHeader omChainHeader) {
        if (omChainHeader == null) {
            throw new OmsException("记录不存在，无法审核");
        }
        if (!OmsConstants.OMS_CO_STATUS_00.equals(omChainHeader.getStatus())) {
            throw new OmsException("非新建状态，无法审核");
        }
        if (StringUtils.isBlank(omChainHeader.getWarehouse())) {
            throw new OmsException("下发机构不能为空");
        }
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_30);
        omChainHeader.setAuditBy(UserUtils.getUser().getName());
        omChainHeader.setAuditDate(new Date());
        this.save(omChainHeader);
    }

    /**
     * 描述：取消审核
     */
    @Transactional
    public void cancelAudit(OmChainHeader omChainHeader) {
        if (omChainHeader == null) {
            throw new OmsException("记录不存在，无法取消审核");
        }
        if (!OmsConstants.OMS_CO_STATUS_30.equals(omChainHeader.getStatus())) {
            throw new OmsException("非审核状态，无法取消审核");
        }
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
        omChainHeader.setAuditBy(null);
        omChainHeader.setAuditDate(null);
        this.save(omChainHeader);
    }

    /**
     * 描述：取消
     * <p>
     * create by Jianhua on 2019/9/29
     */
    @Transactional
    public void cancel(String id) {
        OmChainHeader omChainHeader = mapper.get(id);
        if (OmsConstants.OMS_CO_STATUS_90.equals(omChainHeader.getStatus())) {
            return;
        }
        if (OmsConstants.OMS_CO_STATUS_35.equals(omChainHeader.getStatus()) || OmsConstants.OMS_CO_STATUS_40.equals(omChainHeader.getStatus())) {
            throw new OmsException("已生成任务，无法取消");
        }
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_90);
        super.save(omChainHeader);
    }

    /**
     * 描述：根据客户订单号查询
     * <p>
     * create by Jianhua on 2019/9/29
     */
    public OmChainHeader getByCustomerNo(String customerNo, String orderType, String dataSource, String orgId) {
        return mapper.getByCustomerNo(customerNo, orderType, dataSource, orgId);
    }

    @Transactional
    public ResultMessage importOrder(List<OmChainImportEntity> list) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        Map<String, List<OmChainImportEntity>> collect = list.stream().collect(Collectors.groupingBy(OmChainImportEntity::getCustomerNo));
        List<OmChainHeader> result = Lists.newArrayList();
        int index = 2;
        for (Map.Entry<String, List<OmChainImportEntity>> entry : collect.entrySet()) {
            OmChainImportEntity entity = entry.getValue().get(0);
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
            index += entry.getValue().size();

            OmChainHeader omChainHeader = new OmChainHeader();
            BeanUtils.copyProperties(entity, omChainHeader);
            omChainHeader.setId(null);
            omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
            omChainHeader.setWarehouse(officeService.getByCode(entity.getWarehouse()).getId());
            setOrderArea(entity, omChainHeader);
            int lineIndex = 1;
            List<OmChainDetail> detailList = Lists.newArrayList();
            for (OmChainImportEntity importEntity : entry.getValue()) {
                OmChainDetail omChainDetail = setChainDetailForImport(importEntity);
                omChainDetail.setLineNo(String.format("%04d", lineIndex));
                detailList.add(omChainDetail);
                lineIndex++;
            }
            omChainHeader.setOmChainDetailList(detailList);
            omChainHeader.setConsigneeAddress(StringUtils.trim(omChainHeader.getConsigneeAddress()));
            omChainHeader.setConsigneeAddressArea(StringUtils.trim(omChainHeader.getConsigneeAddressArea()));
            result.add(omChainHeader);
        }

        if (errorMsg.length() > 0) {
            msg.setSuccess(false);
            msg.setMessage(errorMsg.toString());
            return msg;
        }
        for (OmChainHeader header : result) {
            this.save(header);
        }

        msg.setMessage("导入成功");
        return msg;
    }

    private StringBuilder checkNullForImport(OmChainImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (null == entity.getOrderDate()) {
            builder.append("订单日期为空或订单日期格式不正确!");
        }
        if (StringUtils.isEmpty(entity.getBusinessOrderType())) {
            builder.append("业务订单类型为空!");
        }
        if (StringUtils.isEmpty(entity.getOwner())) {
            builder.append("货主编码为空!");
        }
        if (StringUtils.isEmpty(entity.getCustomerNo())) {
            builder.append("客户订单号为空!");
        }
        if (StringUtils.isEmpty(entity.getWarehouse())) {
            builder.append("下发机构为空!");
        }
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
        if (null == entity.getQty()) {
            builder.append("数量为空或格式错误!");
        }
        return builder;
    }

    private StringBuilder checkIsExistForImport(OmChainImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        List<String> orderType = Arrays.asList(OmsConstants.OMS_BUSINESS_ORDER_TYPE_01, OmsConstants.OMS_BUSINESS_ORDER_TYPE_02, OmsConstants.OMS_BUSINESS_ORDER_TYPE_03, OmsConstants.OMS_BUSINESS_ORDER_TYPE_04,
                OmsConstants.OMS_BUSINESS_ORDER_TYPE_05, OmsConstants.OMS_BUSINESS_ORDER_TYPE_06, OmsConstants.OMS_BUSINESS_ORDER_TYPE_07, OmsConstants.OMS_BUSINESS_ORDER_TYPE_08);
        if (!orderType.contains(entity.getBusinessOrderType())) {
            builder.append("订单类型值填写错误!");
        }
        OmCustomer owner = omCustomerService.getByCode(entity.getOwner(), entity.getOrgId());
        if (null == owner) {
            builder.append("货主编码不存在!");
        }
        if (StringUtils.isNotEmpty(entity.getCustomer())) {
            OmCustomer customer = omCustomerService.getByCode(entity.getCustomer(), entity.getOrgId());
            if (null == customer) {
                builder.append("客户编码不存在!");
            }
        }
        if (StringUtils.isNotEmpty(entity.getSupplierCode())) {
            OmCustomer supplier = omCustomerService.getByCode(entity.getSupplierCode(), entity.getOrgId());
            if (null == supplier) {
                builder.append("供应商编码不存在!");
            }
        }
        if (StringUtils.isNotBlank(entity.getVipStatus()) && DictUtils.getDictLabel(entity.getVipStatus(), "OMS_VIP_LEVEL", null) == null) {
            builder.append("会员级别值填写错误!");
        }
        if (StringUtils.isNotEmpty(entity.getClerkCode())) {
            OmClerk clerk = omClerkService.findByCode(entity.getClerkCode(), entity.getOrgId());
            if (null == clerk) {
                builder.append("业务员编码不存在!");
            }
        }
        Office office = officeService.getByCode(entity.getWarehouse());
        if (null == office) {
            builder.append("下发机构不存在!");
        }
        List<String> isAvailableStock = Arrays.asList(OmsConstants.OMS_Y, OmsConstants.OMS_N);
        if (StringUtils.isNotEmpty(entity.getIsAvailableStock()) && !isAvailableStock.contains(entity.getIsAvailableStock())) {
            builder.append("校验库存充足值错误!");
        }
        List<String> transportMode = Arrays.asList(OmsConstants.OMS_TRANSPORT_MODE_01, OmsConstants.OMS_TRANSPORT_MODE_02, OmsConstants.OMS_TRANSPORT_MODE_03, OmsConstants.OMS_TRANSPORT_MODE_04, OmsConstants.OMS_TRANSPORT_MODE_05);
        if (StringUtils.isNotEmpty(entity.getTransportMode()) && !transportMode.contains(entity.getTransportMode())) {
            builder.append("运输方式值错误!");
        }
        if (StringUtils.isNotEmpty(entity.getCarrier())) {
            OmCustomer carrier = omCustomerService.getByCode(entity.getCarrier(), entity.getOrgId());
            if (null == carrier) {
                builder.append("承运商编码不存在!");
            }
        }
        OmItem item = omItemService.getByOwnerAndSku(entity.getOwner(), entity.getSkuCode(), entity.getOrgId());
        if (null == item) {
            builder.append("商品编码不存在!");
        }
        if (StringUtils.isNotEmpty(entity.getPrincipal())) {
            OmCustomer principal = omCustomerService.getByCode(entity.getPrincipal(), entity.getOrgId());
            if (null == principal) {
                builder.append("委托方编码不存在!");
            }
        }

        return builder;
    }

    private OmChainDetail setChainDetailForImport(OmChainImportEntity entity) {
        OmChainDetail omChainDetail = null;
        OmItem omItem = omItemService.getByOwnerAndSku(entity.getOwner(), entity.getSkuCode(), entity.getOrgId());
        if (null != omItem) {
            omChainDetail = new OmChainDetail();
            omChainDetail.setId("");
            omChainDetail.setOrgId(entity.getOrgId());
            omChainDetail.setSkuCode(entity.getSkuCode());
            omChainDetail.setSkuName(omItem.getSkuName());
            omChainDetail.setSpec(omItem.getSpec());
            omChainDetail.setQty(entity.getQty());
        }
        return omChainDetail;
    }

    private void setOrderArea(OmChainImportEntity entity, OmChainHeader header) {
        if (StringUtils.isNotBlank(entity.getConsigneeArea())) {
            List<Area> areaByName = areaService.findAreaByName(entity.getConsigneeArea());
            if (CollectionUtil.isNotEmpty(areaByName)) {
                header.setConsigneeArea(areaByName.get(0).getId());
            }
        }
        if (StringUtils.isNotBlank(entity.getShipperArea())) {
            List<Area> areaByName = areaService.findAreaByName(entity.getShipperArea());
            if (CollectionUtil.isNotEmpty(areaByName)) {
                header.setShipperArea(areaByName.get(0).getId());
            }
        }

    }

    /**
     * 存储流程订单导入
     */
    @Transactional
    public ResultMessage importOrderByBusinessType(List<OmChainWmsImportEntity> list, BusinessOrderType orderType) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        long count = list.stream().filter(t -> StringUtils.isNotBlank(t.getCustomerNo())).count();
        if (list.size() != count) {
            msg.setSuccess(false);
            msg.setMessage("模板存在空行 或 客户订单号为空的数据");
            return msg;
        }
        Map<String, List<OmChainWmsImportEntity>> collect = list.stream().collect(Collectors.groupingBy(OmChainWmsImportEntity::getCustomerNo, LinkedHashMap::new, Collectors.toList()));
        List<OmChainHeader> result = Lists.newArrayList();
        int index = 2;
        for (Map.Entry<String, List<OmChainWmsImportEntity>> entry : collect.entrySet()) {
            OmChainWmsImportEntity entity = entry.getValue().get(0);
            StringBuilder checkNull = checkNullForImport(entity, orderType);
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                break;
            }
            StringBuilder checkExist = checkIsExistForImport(entity);
            if (StringUtils.isNotEmpty(checkExist.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkExist.toString()).append("<br>");
                break;
            }
            OmChainHeader checkCon = new OmChainHeader();
            checkCon.setCustomerNo(entity.getCustomerNo());
            checkCon.setOrgId(entity.getOrgId());
            List<OmChainHeader> checkList = mapper.findList(checkCon);
            if (CollectionUtil.isNotEmpty(checkList)) {
                errorMsg.append("第[").append(index).append("]行,").append("客户订单号已存在！").append("<br>");
                break;
            }
            OmChainHeader omChainHeader = setChainHeaderForImport(entity, orderType);
            int lineIndex = 1;
            List<OmChainDetail> detailList = Lists.newArrayList();
            for (OmChainWmsImportEntity importEntity : entry.getValue()) {
                StringBuilder detailNull = checkNullForDetail(importEntity);
                if (StringUtils.isNotEmpty(detailNull.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailNull.toString()).append("<br>");
                    lineIndex++;
                    continue;
                }
                StringBuilder detailExist = checkIsExistForDetail(importEntity);
                if (StringUtils.isNotEmpty(detailExist.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailExist.toString()).append("<br>");
                    lineIndex++;
                    continue;
                }
                importEntity.setChainNo(omChainHeader.getChainNo());
                OmChainDetail omChainDetail = setChainDetailForImport(importEntity, String.format("%04d", lineIndex));
                detailList.add(omChainDetail);
                lineIndex++;
            }
            omChainHeader.setOmChainDetailList(detailList);
            omChainHeader.setConsigneeAddress(StringUtils.trim(omChainHeader.getConsigneeAddress()));
            omChainHeader.setConsigneeAddressArea(StringUtils.trim(omChainHeader.getConsigneeAddressArea()));
            result.add(omChainHeader);
            index += entry.getValue().size();
        }

        if (errorMsg.length() > 0) {
            throw new OmsException(errorMsg.toString());
        }
        for (OmChainHeader header : result) {
            this.save(header);
        }
        msg.setMessage("导入成功");
        return msg;
    }

    private OmChainHeader setChainHeaderForImport(OmChainWmsImportEntity entity, BusinessOrderType type) {
        OmChainHeader omChainHeader = new OmChainHeader();
        BeanUtils.copyProperties(entity, omChainHeader);
        omChainHeader.setId(IdGen.uuid());
        omChainHeader.setIsNewRecord(true);
        omChainHeader.setChainNo(noService.getDocumentNo(GenNoType.OM_CHAIN_NO.name()));
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
        omChainHeader.setWarehouse(entity.getOrgId());
        omChainHeader.setBusinessOrderType(type.getBusinessType());
        omChainHeader.setCustomer(omChainHeader.getConsigneeCode());
        omChainHeader.setShipperCode(omChainHeader.getSupplierCode());
        omChainHeader.setTransportMode(TmsConstants.TRANSPORT_METHOD_3);
        omChainHeader.setWarehouse(officeService.getByCode(entity.getOrgCode()).getId());
        return omChainHeader;
    }

    private OmChainDetail setChainDetailForImport(OmChainWmsImportEntity entity, String lineNo) {
        OmItem omItem = omItemService.getByOwnerAndSku(entity.getOwner(), entity.getSkuCode(), entity.getOrgId());
        OmChainDetail omChainDetail = new OmChainDetail();
        omChainDetail.setId("");
        omChainDetail.setChainNo(entity.getChainNo());
        omChainDetail.setOrgId(entity.getOrgId());
        omChainDetail.setLineNo(lineNo);
        omChainDetail.setSkuCode(entity.getSkuCode());
        omChainDetail.setSkuName(omItem.getSkuName());
        omChainDetail.setSpec(omItem.getSpec());
        omChainDetail.setUnit(entity.getUom());
        omChainDetail.setAuxiliaryUnit(entity.getUom());
        omChainDetail.setQty(entity.getQty());
        omChainDetail.setAuxiliaryQty(entity.getQty());
        omChainDetail.setRatio(BigDecimal.ONE);
        omChainDetail.setDef1(omItem.getSkuTempLayer());
        omChainDetail.setDef2(omItem.getSkuType());
        omChainDetail.setDef3(omItem.getSkuClass());

        OmOrderDetailAppendix omOrderDetailAppendix = omOrderDetailAppendixService.initChainDetailInfo(omChainDetail);
        omOrderDetailAppendix.setDefTime1(entity.getLotAtt01());
        omOrderDetailAppendix.setDefTime2(entity.getLotAtt02());
        omOrderDetailAppendix.setDefTime3(entity.getLotAtt03());
        omOrderDetailAppendix.setDef4(entity.getLotAtt04());
        omOrderDetailAppendix.setDef5(entity.getLotAtt05());
        omOrderDetailAppendix.setDef6(entity.getLotAtt06());
        omOrderDetailAppendix.setDef7(entity.getLotAtt07());
        omOrderDetailAppendix.setDef8(entity.getLotAtt08());
        omOrderDetailAppendix.setDef9(entity.getLotAtt09());
        omOrderDetailAppendix.setDef10(entity.getLotAtt10());
        omOrderDetailAppendix.setDef11(entity.getLotAtt11());
        omOrderDetailAppendix.setDef12(entity.getLotAtt12());
        omOrderDetailAppendix.setDef1(entity.getPlanRcvLoc());
        omOrderDetailAppendixService.save(omOrderDetailAppendix);
        return omChainDetail;
    }

    private StringBuilder checkNullForImport(OmChainWmsImportEntity entity, BusinessOrderType orderType) {
        StringBuilder builder = new StringBuilder();
        if (null == entity.getOrderDate()) {
            builder.append("订单日期为空或订单日期格式不正确!");
        }
        if (StringUtils.isEmpty(entity.getCustomerNo())) {
            builder.append("客户订单号为空!");
        }
        if (StringUtils.isEmpty(entity.getOwner())) {
            builder.append("货主编码为空!");
        }
        if (StringUtils.isEmpty(entity.getOrgCode())) {
            builder.append("下发机构为空!");
        }
        if ("SO".equals(orderType.getOrderType())) {
            if (StringUtils.isEmpty(entity.getConsigneeCode())) {
                builder.append("收货人为空!");
            }
        }
        return builder;
    }

    private StringBuilder checkIsExistForImport(OmChainWmsImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        OmCustomer owner = omCustomerService.getByCode(entity.getOwner(), entity.getOrgId());
        if (null == owner) {
            builder.append(MessageFormat.format("货主编码[{0}]不存在!", entity.getOwner()));
        }
        if (StringUtils.isNotBlank(entity.getSupplierCode())) {
            OmCustomer supplier = omCustomerService.getByCode(entity.getSupplierCode(), entity.getOrgId());
            if (null == supplier) {
                builder.append(MessageFormat.format("供应商编码不存在!", entity.getSupplierCode()));
            }
        }
        if (StringUtils.isNotBlank(entity.getCarrier())) {
            OmCustomer carrier = omCustomerService.getByCode(entity.getCarrier(), entity.getOrgId());
            if (null == carrier) {
                builder.append(MessageFormat.format("承运商编码不存在!", entity.getCarrier()));
            }
        }
        if (StringUtils.isNotBlank(entity.getConsigneeCode())) {
            OmCustomer consignee = omCustomerService.getByCode(entity.getConsigneeCode(), entity.getOrgId());
            if (null == consignee) {
                builder.append(MessageFormat.format("收货方编码不存在!", entity.getConsigneeCode()));
            }
        }
        Office office = officeService.getByCode(entity.getOrgCode());
        if (null == office) {
            builder.append(MessageFormat.format("下发机构[{0}]不存在!", entity.getOrgCode()));
        }
        return builder;
    }

    private StringBuilder checkNullForDetail(OmChainWmsImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
        if (null == entity.getQty()) {
            builder.append("数量不能为空!");
        }
        if (StringUtils.isBlank(entity.getUom())) {
            builder.append("包装单位不能为空!");
        }
        return builder;
    }

    private StringBuilder checkIsExistForDetail(OmChainWmsImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        OmItem item = omItemService.getByOwnerAndSku(entity.getOwner(), entity.getSkuCode(), entity.getOrgId());
        if (null == item) {
            builder.append(MessageFormat.format("商品编码[{0}]不存在!", entity.getSkuCode()));
        }
        return builder;
    }

    /**
     * DC流程订单导入
     */
    @Transactional
    public ResultMessage importDCOrderByBusinessType(List<OmChainDCImportEntity> list, BusinessOrderType asnType, BusinessOrderType soType, String uploadType) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        long count = list.stream().filter(t -> StringUtils.isNotBlank(t.getCustomerNo())).count();
        if (list.size() != count) {
            msg.setSuccess(false);
            msg.setMessage("模板存在空行 或 客户订单号为空的数据");
            return msg;
        }
        Map<String, List<OmChainDCImportEntity>> collect = list.stream().collect(Collectors.groupingBy(t -> t.getCustomerNo() + t.getConsigneeCode(), LinkedHashMap::new, Collectors.toList()));
        List<OmChainHeader> result = Lists.newArrayList();
        List<String> checkBusinessNoList = Lists.newArrayList();
        int index = 2;
        // 校验 并 构建出库数据
        for (Map.Entry<String, List<OmChainDCImportEntity>> entry : collect.entrySet()) {
            OmChainDCImportEntity entity = entry.getValue().get(0);
            if (StringUtils.isNotBlank(entity.getBusinessNo())) {
                if (checkBusinessNoList.contains(entity.getBusinessNo())) {
                    errorMsg.append("第[").append(index).append("]行,").append("订单号重复！").append("<br>");
                    index += entry.getValue().size();
                    break;
                }
                checkBusinessNoList.add(entity.getBusinessNo());
            }
            StringBuilder checkNull = checkNullForDCImport(entity);
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                index += entry.getValue().size();
                break;
            }
            StringBuilder checkExist = checkIsExistForDCImport(entity);
            if (StringUtils.isNotEmpty(checkExist.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkExist.toString()).append("<br>");
                index += entry.getValue().size();
                break;
            }
            OmChainHeader omChainHeader = setChainHeaderForImportDC(entity, soType, uploadType);
            OmChainHeader checkCon = new OmChainHeader();
            checkCon.setBusinessNo(omChainHeader.getBusinessNo());
            checkCon.setCustomerNo(omChainHeader.getCustomerNo());
            checkCon.setOrgId(omChainHeader.getOrgId());
            List<OmChainHeader> checkList = mapper.findList(checkCon);
            if (CollectionUtil.isNotEmpty(checkList)) {
                errorMsg.append("第[").append(index).append("]行,").append(MessageFormat.format("客户单据号[{0}]已存在！", omChainHeader.getCustomerNo())).append("<br>");
                index += entry.getValue().size();
                break;
            }
            int lineIndex = 1;
            List<OmChainDetail> detailList = Lists.newArrayList();
            List<String> checkSkuList = Lists.newArrayList();
            for (OmChainDCImportEntity importEntity : entry.getValue()) {
                StringBuilder detailNull = checkNullForDCDetail(importEntity);
                if (StringUtils.isNotEmpty(detailNull.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailNull.toString()).append("<br>");
                    lineIndex++;
                    continue;
                }
                StringBuilder detailExist = checkIsExistForDCDetail(importEntity);
                if (StringUtils.isNotEmpty(detailExist.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailExist.toString()).append("<br>");
                    lineIndex++;
                    continue;
                }
                if (checkSkuList.contains(importEntity.getSkuCode())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(MessageFormat.format("订单[{0}]商品编码[{1}]已存在！", omChainHeader.getBusinessNo(), importEntity.getSkuCode())).append("<br>");
                    lineIndex++;
                    continue;
                }
                checkSkuList.add(importEntity.getSkuCode());
                OmChainDetail omChainDetail = setChainDetailForDCImport(importEntity);
                omChainDetail.setLineNo(String.format("%04d", lineIndex));
                detailList.add(omChainDetail);
                lineIndex++;
            }
            omChainHeader.setOmChainDetailList(detailList);
            result.add(omChainHeader);
            index += entry.getValue().size();
        }
        if (errorMsg.length() > 0) {
            throw new OmsException(errorMsg.toString());
        }
        // 构建入库数据
        Map<String, List<OmChainDCImportEntity>> asnMap = list.stream().collect(Collectors.groupingBy(OmChainDCImportEntity::getCustomerNo, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<String, List<OmChainDCImportEntity>> entry : asnMap.entrySet()) {
            OmChainDCImportEntity entity = entry.getValue().get(0);
            OmChainHeader omChainHeader = setChainHeaderForImportDC(entity, asnType, uploadType);
            OmChainHeader checkCon = new OmChainHeader();
            checkCon.setBusinessNo(omChainHeader.getBusinessNo());
            checkCon.setCustomerNo(omChainHeader.getCustomerNo());
            checkCon.setOrgId(omChainHeader.getOrgId());
            List<OmChainHeader> checkList = mapper.findList(checkCon);
            if (CollectionUtil.isNotEmpty(checkList)) {
                throw new OmsException("客户订单号【" + omChainHeader.getCustomerNo() + "】已存在！");
            }
            Map<String, OmChainDCImportEntity> mergeSkuMap = Maps.newHashMap();
            for (OmChainDCImportEntity importEntity : entry.getValue()) {
                if (mergeSkuMap.keySet().contains(importEntity.getSkuCode())) {
                    OmChainDCImportEntity temp = mergeSkuMap.get(importEntity.getSkuCode());
                    if (null != temp.getBoxQty() && null != importEntity.getBoxQty()) {
                        temp.setBoxQty(temp.getBoxQty().add(importEntity.getBoxQty()));
                    }
                    if (null != temp.getQty() && null != importEntity.getQty()) {
                        temp.setQty(temp.getQty().add(importEntity.getQty()));
                    }
                    mergeSkuMap.put(importEntity.getSkuCode(), temp);
                } else {
                    OmChainDCImportEntity temp = new OmChainDCImportEntity();
                    BeanUtils.copyProperties(importEntity, temp);
                    mergeSkuMap.put(importEntity.getSkuCode(), temp);
                }
            }
            int lineIndex = 1;
            List<OmChainDetail> detailList = Lists.newArrayList();
            for (OmChainDCImportEntity importEntity : mergeSkuMap.values()) {
                OmChainDetail omChainDetail = setChainDetailForDCImport(importEntity);
                omChainDetail.setLineNo(String.format("%04d", lineIndex));
                detailList.add(omChainDetail);
                lineIndex++;
            }
            omChainHeader.setOmChainDetailList(detailList);
            result.add(omChainHeader);
        }
        for (OmChainHeader header : result) {
            this.save(header);
        }
        msg.setMessage("导入成功");
        return msg;
    }

    private OmChainHeader setChainHeaderForImportDC(OmChainDCImportEntity entity, BusinessOrderType type, String uploadType) {
        OmChainHeader omChainHeader = new OmChainHeader();
        BeanUtils.copyProperties(entity, omChainHeader);
        omChainHeader.setId(IdGen.uuid());
        omChainHeader.setIsNewRecord(true);
        omChainHeader.setChainNo(noService.getDocumentNo(GenNoType.OM_CHAIN_NO.name()));
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
        omChainHeader.setWarehouse(entity.getOrgId());
        omChainHeader.setBusinessOrderType(type.getBusinessType());
        omChainHeader.setShipperCode(omChainHeader.getSupplierCode());
        if ("ASN".equals(type.getOrderType())) {
            omChainHeader.setCustomer(omChainHeader.getSupplierCode());
            omChainHeader.setCustomerNo(entity.getCustomerNo());
            omChainHeader.setBusinessNo("");
        }
        if ("SO".equals(type.getOrderType())) {
            omChainHeader.setCustomer(omChainHeader.getConsigneeCode());
            omChainHeader.setBusinessNo(entity.getCustomerNo());
            if (StringUtils.isBlank(entity.getBusinessNo())) {
                omChainHeader.setCustomerNo(omChainHeader.getCustomerNo() + omChainHeader.getConsigneeCode());
            } else {
                omChainHeader.setCustomerNo(entity.getBusinessNo());
            }
        }
        omChainHeader.setWarehouse(officeService.getByCode(entity.getOrgCode()).getId());
        omChainHeader.setTransportMode(TmsConstants.TRANSPORT_METHOD_3);
        omChainHeader.setDef7(DictUtils.getDictValue(entity.getDef7(), "SMS_ASN_ORDER_TYPE", "NORMAL"));
        OmOrderAppendix orderAppendix = omOrderAppendixService.initChainInfo(omChainHeader);
        orderAppendix.setDef1(entity.getPoNo());
        orderAppendix.setDef4(uploadType);
        omOrderAppendixService.save(orderAppendix);
        return omChainHeader;
    }

    private OmChainDetail setChainDetailForDCImport(OmChainDCImportEntity entity) {
        OmItem omItem = omItemService.getByOwnerAndSku(entity.getOwner(), entity.getSkuCode(), entity.getOrgId());
        OmChainDetail omChainDetail = new OmChainDetail();
        omChainDetail.setId("");
        omChainDetail.setOrgId(entity.getOrgId());
        omChainDetail.setSkuCode(entity.getSkuCode());
        omChainDetail.setSkuName(omItem.getSkuName());
        omChainDetail.setSpec(omItem.getSpec());
        omChainDetail.setUnit(OmsConstants.OMS_PACKAGE_EA);
        omChainDetail.setAuxiliaryUnit("CS");
        OmPackageRelation cs = omPackageRelationService.findByPackageUom(omItem.getPackCode(), "CS", omItem.getOrgId());
        omChainDetail.setQty(null == entity.getQty() ? entity.getBoxQty().multiply(BigDecimal.valueOf(cs.getCdprQuantity())) : entity.getQty());
        omChainDetail.setAuxiliaryQty(null == entity.getBoxQty() ? entity.getQty().divide(BigDecimal.valueOf(cs.getCdprQuantity()), 0, BigDecimal.ROUND_HALF_UP) : entity.getBoxQty());
        omChainDetail.setRatio(null == cs ? omChainDetail.getQty().divide(omChainDetail.getAuxiliaryQty(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(cs.getCdprQuantity()));
        omChainDetail.setDef1(omItem.getSkuTempLayer());
        omChainDetail.setDef2(omItem.getSkuType());
        omChainDetail.setDef3(omItem.getSkuClass());
        return omChainDetail;
    }

    private StringBuilder checkNullForDCImport(OmChainDCImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (null == entity.getOrderDate()) {
            builder.append("订单日期为空或订单日期格式不正确!");
        }
        if (StringUtils.isEmpty(entity.getCustomerNo())) {
            builder.append("客户订单号为空!");
        }
        if (StringUtils.isEmpty(entity.getOwner())) {
            builder.append("货主编码为空!");
        }
        if (StringUtils.isEmpty(entity.getSupplierCode())) {
            builder.append("供应商编码为空!");
        }
        if (StringUtils.isEmpty(entity.getConsigneeCode())) {
            builder.append("收货方编码为空!");
        }
        if (StringUtils.isEmpty(entity.getOrgCode())) {
            builder.append("下发机构为空!");
        }
        return builder;
    }

    private StringBuilder checkNullForDCDetail(OmChainDCImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
        if (null == entity.getQty() && null == entity.getBoxQty()) {
            builder.append("数量和箱数不能都为空!");
        }
        return builder;
    }

    private StringBuilder checkIsExistForDCImport(OmChainDCImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        OmCustomer owner = omCustomerService.getByCode(entity.getOwner(), entity.getOrgId());
        if (null == owner) {
            builder.append(MessageFormat.format("货主编码[{0}]不存在!", entity.getOwner()));
        }
        OmCustomer supplier = omCustomerService.getByCode(entity.getSupplierCode(), entity.getOrgId());
        if (null == supplier) {
            builder.append(MessageFormat.format("供应商编码[{0}]不存在!", entity.getSupplierCode()));
        }
        OmCustomer consignee = omCustomerService.getByCode(entity.getConsigneeCode(), entity.getOrgId());
        if (null == consignee) {
            builder.append(MessageFormat.format("收货方编码[{0}]不存在!", entity.getConsigneeCode()));
        }
        Office office = officeService.getByCode(entity.getOrgCode());
        if (null == office) {
            builder.append(MessageFormat.format("下发机构[{0}]不存在!", entity.getOrgCode()));
        }
        return builder;
    }

    private StringBuilder checkIsExistForDCDetail(OmChainDCImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        OmItem item = omItemService.getByOwnerAndSku(entity.getOwner(), entity.getSkuCode(), entity.getOrgId());
        if (null == item) {
            builder.append(MessageFormat.format("商品编码[{0}]不存在!", entity.getSkuCode()));
        }
        return builder;
    }

    /**
     * 描述：查询审核的供应链订单提供给自动进行任务的定时器操作数据
     *
     * @author liujianhua created on 2019-12-24
     */
    public List<String> findChainIdForTimer() {
        return mapper.findChainIdForTimer();
    }

    /**
     * 描述：查询供应链订单是否被拦截
     *
     * @author Jianhua on 2020-2-12
     */
    public boolean isIntercepted(String chainNo, String orgId) {
        return StringUtils.isNotBlank(mapper.isIntercepted(chainNo, orgId));
    }

    /**
     * 描述：查询供应链订单提供给自动进行任务的定时器操作数据（截单）
     *
     * @author Jianhua on 2020-2-12
     */
    public List<String> findInterceptChainIdForTimer() {
        return mapper.findInterceptChainIdForTimer();
    }

}