package com.yunyou.modules.bms.calculate.business;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;
import com.yunyou.modules.bms.basic.service.BmsCarrierRouteService;
import com.yunyou.modules.bms.basic.service.BmsCustomerService;
import com.yunyou.modules.bms.basic.service.SettlementSkuService;
import com.yunyou.modules.bms.business.entity.BmsDispatchData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderSiteData;
import com.yunyou.modules.bms.business.service.BmsDispatchDataService;
import com.yunyou.modules.bms.business.service.BmsDispatchOrderDataService;
import com.yunyou.modules.bms.calculate.BmsCalculateUtils;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.CalcSkuQtyType;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务数据-派车Service
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@Service
public class BmsDispatchBusinessService extends BaseService {
    @Autowired
    private BmsDispatchOrderDataService bmsDispatchOrderDataService;
    @Autowired
    private BmsDispatchDataService bmsDispatchDataService;
    @Autowired
    private BmsCarrierRouteService bmsCarrierRouteService;
    @Autowired
    private SettlementSkuService settlementSkuService;
    @Autowired
    private BmsCustomerService bmsCustomerService;
    @Autowired
    private AreaService areaService;

    /**
     * 查找原始业务数据
     *
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @return 原始业务数据
     */
    private List<BmsDispatchOrderData> findBusinessOrderData(List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        return bmsDispatchOrderDataService.findCalcData(includeParams, excludeParams);
    }

    /**
     * 查找原始业务数据
     *
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @return 原始业务数据
     */
    private List<BmsDispatchData> findBusinessData(List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        return bmsDispatchDataService.findCalcData(includeParams, excludeParams);
    }

    /**
     * 从原始业务数据中提取商品参数
     *
     * @param list  原始业务数据
     * @param orgId 机构ID
     * @return 商品参数
     */
    private List<BmsBusinessSkuParams> findSkuParams(List<BmsDispatchData> list, String orgId) {
        List<BmsBusinessSkuParams> rsList = Lists.newArrayList();
        for (BmsDispatchData o : list) {
            BigDecimal qty = o.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQty());
            BigDecimal qtyCs = o.getQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQtyCs());
            BigDecimal qtyPl = o.getQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQtyPl());
            BigDecimal theoryCsQty = BigDecimal.valueOf(settlementSkuService.convertQty(o.getOwnerCode(), o.getSkuCode(), orgId, "EA", qty.doubleValue(), "CS"));
            BigDecimal theoryPlQty = BigDecimal.valueOf(settlementSkuService.convertQty(o.getOwnerCode(), o.getSkuCode(), orgId, "EA", qty.doubleValue(), "PL"));
            BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
            BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
            rsList.add(new BmsBusinessSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), qty, qtyCs, qtyPl, theoryCsQty, theoryPlQty, weight, volume));
        }
        return rsList;
    }

    /**
     * 查找费用计算时业务数据
     *
     * @param outputObject  条款输出对象
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @param orgId         机构ID
     * @return 业务数据
     */
    public List<BmsCalcBusinessData> findForCalcDispatchOrder(String outputObject, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams, String orgId) {
        List<BmsDispatchOrderData> data = this.findBusinessOrderData(includeParams, excludeParams);

        List<BmsCalcBusinessData> rsList = Lists.newArrayList();
        switch (outputObject) {
            case BmsConstants.OUTPUT_OBJECT_001:
                data.forEach(o -> {
                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(Lists.newArrayList(o.getId()));
                    bmsCalcBusinessData.setOutputValue(BigDecimal.ONE);
                    rsList.add(bmsCalcBusinessData);
                });
                break;
            case BmsConstants.OUTPUT_OBJECT_007:
            case BmsConstants.OUTPUT_OBJECT_008:
                break;// 此处逻辑待定
            case BmsConstants.OUTPUT_OBJECT_009:
                data.forEach(o -> {
                    o.setSiteList(bmsDispatchOrderDataService.findSiteData(o.getId(), o.getOrgId()));

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(Lists.newArrayList(o.getId()));
                    bmsCalcBusinessData.setOutputValue(BigDecimal.valueOf(o.getSiteList().size()));
                    rsList.add(bmsCalcBusinessData);
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

                        BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                        bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                        bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                        if (startArea != null) {
                            bmsCalcBusinessData.setStartPlaceCode(startArea.getCode());
                        }
                        if (endArea != null) {
                            bmsCalcBusinessData.setEndPlaceCode(endArea.getCode());
                        }
                        bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                        Office office = UserUtils.getOffice(o.getOrgId());
                        bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                        bmsCalcBusinessData.setBusinessOrgName(office.getName());
                        bmsCalcBusinessData.setBusinessDataIds(Lists.newArrayList(o.getId()));
                        bmsCalcBusinessData.setOutputValue(bmsCarrierRoute.getMileage());
                        rsList.add(bmsCalcBusinessData);
                    }
                });
                break;
            default:
                break;
        }
        return rsList;
    }

    /**
     * 查找费用计算时业务数据
     *
     * @param outputObject  条款输出对象
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @param orgId         机构ID
     * @return 业务数据
     */
    public List<BmsCalcBusinessData> findForCalcDispatch(String outputObject, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams, String orgId) {
        List<BmsDispatchData> data = this.findBusinessData(includeParams, excludeParams);

        switch (outputObject) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_009:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);
                    List<String> collect = list.stream().map(BmsDispatchData::getConsigneeCode).collect(Collectors.toList());
                    collect.addAll(list.stream().map(BmsDispatchData::getShipCode).collect(Collectors.toList()));

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BigDecimal.valueOf(collect.stream().distinct().count()));
                    return bmsCalcBusinessData;
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

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    if (startArea != null) {
                        bmsCalcBusinessData.setStartPlaceCode(startArea.getCode());
                    }
                    if (endArea != null) {
                        bmsCalcBusinessData.setEndPlaceCode(endArea.getCode());
                    }
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(mileage);
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, orgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);
                    List<BmsBusinessSkuParams> bmsCalcSkuParams = this.findSkuParams(list, orgId);
                    BigDecimal weight = BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessData.setOutputValue(outputValue);
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            default:
                return Lists.newArrayList();
        }
    }

    /**
     * 查找费用计算时业务数据
     *
     * @param outputObject  条款输出对象
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @param orgId         机构ID
     * @return 业务数据
     */
    public List<BmsCalcBusinessData> findForCalcDispatchRegion(String outputObject, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams, String orgId) {
        List<BmsDispatchData> data = this.findBusinessData(includeParams, excludeParams);

        switch (outputObject) {
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcBusinessData.getSkuParams()));
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_009:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(BigDecimal.ONE);
                    return bmsCalcBusinessData;
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

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    if (startArea != null) {
                        bmsCalcBusinessData.setStartPlaceCode(startArea.getCode());
                    }
                    if (endArea != null) {
                        bmsCalcBusinessData.setEndPlaceCode(endArea.getCode());
                    }
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(this.findSkuParams(list, orgId));
                    bmsCalcBusinessData.setOutputValue(mileage);
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, orgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsDispatchData o = list.get(0);
                    List<BmsBusinessSkuParams> bmsCalcSkuParams = this.findSkuParams(list, orgId);
                    BigDecimal weight = BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessData bmsCalcBusinessData = new BmsCalcBusinessData();
                    bmsCalcBusinessData.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsCalcBusinessData.setCarTypeCode(o.getCarType());
                    bmsCalcBusinessData.setSkuCode(o.getSkuCode());
                    bmsCalcBusinessData.setSkuClass(o.getSkuClass());
                    bmsCalcBusinessData.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                    bmsCalcBusinessData.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsCalcBusinessData.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessData.setBusinessOrgName(office.getName());
                    bmsCalcBusinessData.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                    bmsCalcBusinessData.setSkuParams(bmsCalcSkuParams);
                    bmsCalcBusinessData.setOutputValue(outputValue);
                    return bmsCalcBusinessData;
                }).collect(Collectors.toList());
            default:
                return Lists.newArrayList();
        }
    }

    /**
     * 查找费用计算时业务数据
     *
     * @param outputObject  条款输出对象
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @param orgId         机构ID
     * @return 业务数据
     */
    public List<BmsCalcBusinessData> findForCalcDispatchQC(String outputObject, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams, String orgId) {
        List<BmsDispatchData> data = this.findBusinessData(includeParams, excludeParams);

        if (BmsConstants.OUTPUT_OBJECT_014.equals(outputObject)) {
            return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getConsigneeCode() + o.getQuarantineType() + o.getOrgId())).values().stream().map(list -> {
                BmsDispatchData o = list.get(0);

                BmsCalcBusinessData bmsCalcBusinessParams = new BmsCalcBusinessData();
                bmsCalcBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                bmsCalcBusinessParams.setCarTypeCode(o.getCarType());
                bmsCalcBusinessParams.setRegionCode(bmsCustomerService.getRegionCode(o.getConsigneeCode(), o.getOrgId()));
                bmsCalcBusinessParams.setBusinessOrgId(o.getOrgId());
                Office office = UserUtils.getOffice(o.getOrgId());
                bmsCalcBusinessParams.setBusinessOrgCode(office.getCode());
                bmsCalcBusinessParams.setBusinessOrgName(office.getName());
                bmsCalcBusinessParams.setBusinessDataIds(list.stream().map(BmsDispatchData::getId).collect(Collectors.toList()));
                bmsCalcBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                bmsCalcBusinessParams.setOutputValue(BigDecimal.valueOf(list.stream().map(BmsDispatchData::getQuarantineType).filter(StringUtils::isNotBlank).distinct().count()));
                return bmsCalcBusinessParams;
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

}
