package com.yunyou.modules.bms.calculate.business;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.basic.service.SettlementSkuService;
import com.yunyou.modules.bms.business.entity.BmsInboundData;
import com.yunyou.modules.bms.business.service.BmsInboundDataService;
import com.yunyou.modules.bms.calculate.BmsCalculateUtils;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.CalcSkuQtyType;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务数据-入库Service
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@Service
public class BmsInboundBusinessService extends BaseService {
    @Autowired
    private BmsInboundDataService bmsInboundDataService;
    @Autowired
    private SettlementSkuService settlementSkuService;

    /**
     * 查找原始业务数据
     *
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @return 原始业务数据
     */
    private List<BmsInboundData> findBusinessData(List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        return bmsInboundDataService.findCalcData(includeParams, excludeParams);
    }

    /**
     * 从原始业务数据中提取商品参数
     *
     * @param list  原始业务数据
     * @param orgId 机构ID
     * @return 商品参数
     */
    private List<BmsBusinessSkuParams> findSkuParams(List<BmsInboundData> list, String orgId) {
        List<BmsBusinessSkuParams> rsList = Lists.newArrayList();
        for (BmsInboundData o : list) {
            BigDecimal qty = o.getReceiptQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQty());
            BigDecimal qtyCs = o.getReceiptQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQtyCs());
            BigDecimal qtyPl = o.getReceiptQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQtyPl());
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
    public List<BmsCalcBusinessData> findForCalcInbound(String outputObject, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams, String orgId) {
        List<BmsInboundData> data = this.findBusinessData(includeParams, excludeParams);

        switch (outputObject) {
            case BmsConstants.OUTPUT_OBJECT_001:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setOutputValue(BigDecimal.ONE);
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_002:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.QTY, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_003:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_004:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_005:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_006:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_007:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_008:
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(this.findSkuParams(list, orgId));
                    bmsBusinessParams.setOutputValue(BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.VOLUME, bmsBusinessParams.getSkuParams()));
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            case BmsConstants.OUTPUT_OBJECT_013:
                // BMS参数：重泡比比例
                final String HEAVY_BUBBLE_RATIO = SysControlParamsUtils.getValue(SysParamConstants.HEAVY_BUBBLE_RATIO, orgId);
                final BigDecimal heavyBubbleRatio = StringUtils.isNotBlank(HEAVY_BUBBLE_RATIO) ? new BigDecimal(HEAVY_BUBBLE_RATIO) : BigDecimal.ONE;
                return data.stream().collect(Collectors.groupingBy(o -> o.getOrderNo() + o.getOwnerCode() + o.getSkuCode() + o.getSkuClass() + o.getOrgId())).values().stream().map(list -> {
                    BmsInboundData o = list.get(0);
                    List<BmsBusinessSkuParams> bmsCalcSkuParams = this.findSkuParams(list, orgId);
                    BigDecimal weight = BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.WEIGHT, bmsCalcSkuParams);
                    BigDecimal volume = BmsCalculateUtils.calcSkuQty(CalcSkuQtyType.VOLUME, bmsCalcSkuParams);
                    BigDecimal multiply = volume.multiply(heavyBubbleRatio);
                    BigDecimal outputValue = multiply.compareTo(weight) > 0 ? multiply : weight;

                    BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                    bmsBusinessParams.setBusinessDate(BmsCalculateUtils.getBusinessDate(o, includeParams));
                    bmsBusinessParams.setSkuCode(o.getSkuCode());
                    bmsBusinessParams.setSkuClass(o.getSkuClass());
                    bmsBusinessParams.setBusinessOrgId(o.getOrgId());
                    Office office = UserUtils.getOffice(o.getOrgId());
                    bmsBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsBusinessParams.setBusinessOrgName(office.getName());
                    bmsBusinessParams.setBusinessDataIds(list.stream().map(BmsInboundData::getId).collect(Collectors.toList()));
                    bmsBusinessParams.setSkuParams(bmsCalcSkuParams);
                    bmsBusinessParams.setOutputValue(outputValue);
                    return bmsBusinessParams;
                }).collect(Collectors.toList());
            default:
                return Lists.newArrayList();
        }
    }
}
