package com.yunyou.modules.bms.finance.service.calc;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;
import com.yunyou.modules.bms.basic.service.BmsCarrierRouteService;
import com.yunyou.modules.bms.basic.service.BmsCustomerService;
import com.yunyou.modules.bms.business.entity.*;
import com.yunyou.modules.bms.business.service.BmsDispatchOrderDataService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.CalcMethod;
import com.yunyou.modules.bms.common.CalcSkuQtyType;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcBusinessParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcConditionParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcSkuParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算前参数准备Service
 */
@Service
public class BmsPrepareServiceParamService extends BmsPrepareService {

    @Autowired
    private BmsCalculator bmsCalculator;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private BmsCustomerService bmsCustomerService;
    @Autowired
    private BmsCarrierRouteService bmsCarrierRouteService;
    @Autowired
    private BmsDispatchOrderDataService bmsDispatchOrderDataService;

    /*************************************************************标准逻辑：提取计算业务参数（与{@link CalcMethod}一对一绑定）****************************************************************/
    public List<BmsCalcBusinessParams> getGroupBizParamsFromInbound(BmsCalcConditionParams entity, List<BmsInboundData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromOutbound(BmsCalcConditionParams entity, List<BmsOutboundData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsOutboundData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromInventory(BmsCalcConditionParams entity, List<BmsInventoryData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_011:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.valueOf(list.stream().map(BmsInventoryData::getLocCode).distinct().count()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_012:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.valueOf(list.stream().map(BmsInventoryData::getTraceId).distinct().count()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromDispatchOrder(BmsCalcConditionParams entity, List<BmsDispatchOrderData> data) {
        String outputObjects = entity.getOutputObjects();
        List<BmsCalcBusinessParams> rsList = Lists.newArrayList();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                data.forEach(o -> {
                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(o.getDriverCode());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    rsList.add(bmsCalcBusinessParams);
                });
                break;
            case BmsConstants.OUTPUT_OBJECT_007:
            case BmsConstants.OUTPUT_OBJECT_008:
                break;// 此处逻辑待定
            case BmsConstants.OUTPUT_OBJECT_009:
                data.forEach(o -> {
                    o.setSiteList(bmsDispatchOrderDataService.findSiteData(o.getId(), o.getOrgId()));

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(o.getDriverCode());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.valueOf(o.getSiteList().size()));
                    rsList.add(bmsCalcBusinessParams);
                });
                break;
            case BmsConstants.OUTPUT_OBJECT_010:
                data.forEach(o -> {
                    List<String> siteList = bmsDispatchOrderDataService.findSiteData(o.getId(), o.getOrgId()).stream().sorted(Comparator.comparing(BmsDispatchOrderSiteData::getDispatchSeq)).map(BmsDispatchOrderSiteData::getAreaId).collect(Collectors.toList());
                    for (int i = 1; i < siteList.size(); i++) {
                        String startAreaId = siteList.get(i - 1);
                        String endAreaId = siteList.get(i);
                        BmsCarrierRoute bmsCarrierRoute = bmsCarrierRouteService.getByStartAndEndAreaId(o.getCarrierCode(), startAreaId, endAreaId, o.getOrgId());
                        if (bmsCarrierRoute == null) {
                            continue;
                        }
                        Area startArea = areaService.get(startAreaId);
                        Area endArea = areaService.get(endAreaId);

                        BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                        bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                        bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                        bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                        bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                        bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                        if (startArea != null) {
                            bmsCalcBusinessParams.setStartPlaceCode(startArea.getCode());
                            bmsCalcBusinessParams.setStartPlaceName(startArea.getName());
                        }
                        if (endArea != null) {
                            bmsCalcBusinessParams.setEndPlaceCode(endArea.getCode());
                            bmsCalcBusinessParams.setEndPlaceName(endArea.getName());
                        }
                        bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                        bmsCalcBusinessParams.setDriverCode(o.getDriverCode());
                        bmsCalcBusinessParams.setOrgId(o.getOrgId());
                        Office office = officeService.get(o.getOrgId());
                        if (office != null) {
                            bmsCalcBusinessParams.setOrgCode(office.getCode());
                            bmsCalcBusinessParams.setOrgName(office.getName());
                        }
                        bmsCalcBusinessParams.setOutputValue(bmsCarrierRoute.getMileage());
                        rsList.add(bmsCalcBusinessParams);
                    }
                });
                break;
            default:
                break;
        }
        return rsList;
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromDispatch(BmsCalcConditionParams entity, List<BmsDispatchData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_009:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);
                    List<String> collect = list.stream().map(BmsDispatchData::getConsigneeCode).collect(Collectors.toList());
                    collect.addAll(list.stream().map(BmsDispatchData::getShipCode).collect(Collectors.toList()));

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.valueOf(collect.stream().distinct().count()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_010:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getShipCode() + o.getConsigneeCode() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BigDecimal mileage = BigDecimal.ZERO;
                    BmsCarrierRoute bmsCarrierRoute = bmsCarrierRouteService.getByStartAndEndAreaId(o.getCarrierCode(), o.getShipCityId(), o.getConsigneeCityId(), o.getOrgId());
                    if (bmsCarrierRoute != null) {
                        mileage = bmsCarrierRoute.getMileage();
                    }
                    Area startArea = areaService.get(o.getShipCityId());
                    Area endArea = areaService.get(o.getConsigneeCityId());

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    if (startArea != null) {
                        bmsCalcBusinessParams.setStartPlaceCode(startArea.getCode());
                        bmsCalcBusinessParams.setStartPlaceName(startArea.getName());
                    }
                    if (endArea != null) {
                        bmsCalcBusinessParams.setEndPlaceCode(endArea.getCode());
                        bmsCalcBusinessParams.setEndPlaceName(endArea.getName());
                    }
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(mileage);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromReturn(BmsCalcConditionParams entity, List<BmsReturnData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsReturnData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getCustomerNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromException(BmsCalcConditionParams entity, List<BmsExceptionData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsExceptionData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getDispatchNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(null);
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromWaybill(BmsCalcConditionParams entity, List<BmsWaybillData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsWaybillData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            default:
                break;
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromFixed(BmsCalcConditionParams entity, String periodType) {
        List<BmsCalcBusinessParams> rsList = Lists.newArrayList();
        if (BmsConstants.OUTPUT_OBJECT_999.equals(entity.getOutputObjects())) {
            Date settleDateFm = entity.getSettleDateFm();
            Date settleDateTo = entity.getSettleDateTo();
            Date date;
            if ("Daily".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfDate(settleDateFm));
            } else if ("Monthly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfMonth(settleDateFm));
            } else if ("Yearly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfYear(settleDateFm));
            } else {
                return rsList;
            }
            while (!date.after(settleDateTo)) {
                BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                bmsCalcBusinessParams.setBusinessDate(date);
                bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                rsList.add(bmsCalcBusinessParams);
                if ("Daily".equals(periodType)) {
                    date = DateUtil.addDays(date, 1);
                } else if ("Monthly".equals(periodType)) {
                    date = DateUtil.addMonths(date, 1);
                } else {
                    date = DateUtil.addMonths(date, 12);
                }
            }
        }
        return rsList;
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromFixedForBusinessOrg(BmsCalcConditionParams entity, String periodType) {
        List<BmsCalcBusinessParams> rsList = Lists.newArrayList();
        if (BmsConstants.OUTPUT_OBJECT_999.equals(entity.getOutputObjects())) {
            Date settleDateFm = entity.getSettleDateFm();
            Date settleDateTo = entity.getSettleDateTo();
            Date date;
            if ("Daily".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfDate(settleDateFm));
            } else if ("Monthly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfMonth(settleDateFm));
            } else if ("Yearly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfYear(settleDateFm));
            } else {
                return rsList;
            }

            Set<String> orgCodes = entity.getIncludeParams().stream().filter(o -> BmsConstants.YES.equals(o.getIsEnable()) && BmsConstants.TERMS_PARAM_TYPE_TEXT.equals(o.getType()) && "org_id".equals(o.getField())).map(BmsCalcTermsParams::getFieldValue).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            orgCodes.removeAll(entity.getExcludeParams().stream().filter(o -> BmsConstants.YES.equals(o.getIsEnable()) && BmsConstants.TERMS_PARAM_TYPE_TEXT.equals(o.getType()) && "org_id".equals(o.getField())).map(BmsCalcTermsParams::getFieldValue).filter(StringUtils::isNotBlank).collect(Collectors.toSet()));
            if (CollectionUtil.isEmpty(orgCodes)) {
                return getGroupBizParamsFromFixed(entity, periodType);
            }
            List<String> orgIds = orgCodes.stream().map(s -> Arrays.stream(s.split(",")).map(officeService::getByCode).filter(Objects::nonNull).map(Office::getId).collect(Collectors.toList())).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

            while (!date.after(settleDateTo)) {
                for (String orgId : orgIds) {
                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(date);
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    bmsCalcBusinessParams.setOrgId(orgId);
                    Office office = officeService.get(orgId);
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    rsList.add(bmsCalcBusinessParams);
                }
                if ("Daily".equals(periodType)) {
                    date = DateUtil.addDays(date, 1);
                } else if ("Monthly".equals(periodType)) {
                    date = DateUtil.addMonths(date, 1);
                } else {
                    date = DateUtil.addMonths(date, 12);
                }
            }
        }
        return rsList;
    }

    /*************************************************************定制逻辑：提起计算业务参数（与{@link CalcMethod}一对一绑定）****************************************************************/
    public List<BmsCalcBusinessParams> getGroupBizParamsFromDispatchForRegion(BmsCalcConditionParams entity, List<BmsDispatchData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_009:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_010:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getShipCode() + o.getConsigneeCode() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BigDecimal mileage = BigDecimal.ZERO;
                    BmsCarrierRoute bmsCarrierRoute = bmsCarrierRouteService.getByStartAndEndAreaId(o.getCarrierCode(), o.getShipCityId(), o.getConsigneeCityId(), o.getOrgId());
                    if (bmsCarrierRoute != null) {
                        mileage = bmsCarrierRoute.getMileage();
                    }
                    Area startArea = areaService.get(o.getShipCityId());
                    Area endArea = areaService.get(o.getConsigneeCityId());

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setShipCode(o.getShipCode());
                    bmsCalcBusinessParams.setShipName(o.getShipName());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    if (startArea != null) {
                        bmsCalcBusinessParams.setStartPlaceCode(startArea.getCode());
                        bmsCalcBusinessParams.setStartPlaceName(startArea.getName());
                    }
                    if (endArea != null) {
                        bmsCalcBusinessParams.setEndPlaceCode(endArea.getCode());
                        bmsCalcBusinessParams.setEndPlaceName(endArea.getName());
                    }
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(mileage);
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                    bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                    bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                    bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                    bmsCalcBusinessParams.setDriverCode(null);
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                    bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                    bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());

        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromDispatchForQC(BmsCalcConditionParams entity, List<BmsDispatchData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        if (BmsConstants.OUTPUT_OBJECT_014.equals(outputObjects)) {
            return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getQuarantineType() + o.getOrgId())).values().stream().map(list -> {
                BmsDispatchData o = list.get(0);

                BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                bmsCalcBusinessParams.setOrderNo(o.getOrderNo());
                bmsCalcBusinessParams.setCustomerNo(o.getOrderNoA());
                bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                bmsCalcBusinessParams.setCarrierCode(o.getCarrierCode());
                bmsCalcBusinessParams.setCarrierName(o.getCarrierName());
                bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                bmsCalcBusinessParams.setVehicleNo(o.getVehicleNo());
                bmsCalcBusinessParams.setDriverCode(null);
                bmsCalcBusinessParams.setQuarantineType(o.getQuarantineType());
                bmsCalcBusinessParams.setConsigneeCode(o.getConsigneeCode());
                bmsCalcBusinessParams.setConsigneeName(o.getConsigneeName());
                bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                bmsCalcBusinessParams.setOrgId(o.getOrgId());
                Office office = officeService.get(o.getOrgId());
                if (office != null) {
                    bmsCalcBusinessParams.setOrgCode(office.getCode());
                    bmsCalcBusinessParams.setOrgName(office.getName());
                }
                bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                bmsCalcBusinessParams.setOutputValue(BigDecimal.valueOf(list.stream().map(BmsDispatchData::getQuarantineType).filter(StringUtils::isNotBlank).distinct().count()));
                bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                return bmsCalcBusinessParams;
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public List<BmsCalcBusinessParams> getGroupBizParamsFromInventoryForRentFree(BmsCalcConditionParams entity, List<BmsInventoryData> data) {
        String outputObjects = entity.getOutputObjects(), occurrence = entity.getOccurrence(), settleOrgId = entity.getOrgId();
        switch (outputObjects) {
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(extractSkuParams(list, settleOrgId));
                    bmsCalcBusinessParams.setOutputValue(bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessParams.getSkuParams()));
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, settleOrgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getInvDate() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInventoryData o = list.get(0);
                    List<BmsCalcSkuParams> bmsCalcSkuParams = extractSkuParams(list, settleOrgId);
                    BigDecimal weight = bmsCalculator.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = bmsCalculator.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessParams bmsCalcBusinessParams = new BmsCalcBusinessParams();
                    bmsCalcBusinessParams.setBusinessDate(getBusinessDate(o, getSettleDataField(entity.getIncludeParams())));
                    bmsCalcBusinessParams.setSupplierCode(o.getSupplierCode());
                    bmsCalcBusinessParams.setSupplierName(o.getSupplierName());
                    bmsCalcBusinessParams.setOwnerCode(o.getOwnerCode());
                    bmsCalcBusinessParams.setOwnerName(o.getOwnerName());
                    bmsCalcBusinessParams.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessParams.setSkuName(o.getSkuName());
                    bmsCalcBusinessParams.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessParams.setOrgId(o.getOrgId());
                    Office office = officeService.get(o.getOrgId());
                    if (office != null) {
                        bmsCalcBusinessParams.setOrgCode(office.getCode());
                        bmsCalcBusinessParams.setOrgName(office.getName());
                    }
                    bmsCalcBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessParams.setOutputValue(outputValue);
                    bmsCalcBusinessParams.setOccurrenceQty(bmsCalculator.calcOccurrenceQty(occurrence, bmsCalcBusinessParams.getSkuParams()));
                    return bmsCalcBusinessParams;
                }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

}
