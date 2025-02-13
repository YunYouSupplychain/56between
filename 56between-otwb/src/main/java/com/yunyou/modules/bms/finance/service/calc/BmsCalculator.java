package com.yunyou.modules.bms.finance.service.calc;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;
import com.yunyou.modules.bms.basic.entity.BmsCalendar;
import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSteppedPrice;
import com.yunyou.modules.bms.basic.service.*;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.common.CalcSkuQtyType;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcBusinessParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcConditionParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcSkuParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalculateEntity;
import com.yunyou.modules.sys.utils.DictUtils;
import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BmsCalculator {
    private final Logger logger = LoggerFactory.getLogger(BmsCalculator.class);

    @Autowired
    private SettlementSkuService settlementSkuService;
    @Autowired
    private BmsCalendarService bmsCalendarService;
    @Autowired
    private BmsContractSkuPriceService bmsContractSkuPriceService;
    @Autowired
    private BmsContractStoragePriceService bmsContractStoragePriceService;
    @Autowired
    private BmsTransportPriceService bmsTransportPriceService;

    /**
     * 是否按阶梯价格 分段累加方式计算
     *
     * @param billModule       费用计算条件参数
     * @param contractDetailId 费用计算条件参数
     * @param orgId            费用计算条件参数
     * @param skuClass         费用计算条件参数
     * @param skuCode          费用计算条件参数
     * @param startPlaceCode   费用计算条件参数
     * @param endPlaceCode     费用计算条件参数
     * @param regionCode       费用计算条件参数
     * @param carTypeCode      费用计算条件参数
     */
    private boolean isStepAccumulationMethod(String billModule, String contractDetailId, String orgId, String skuClass, String skuCode,
                                             String startPlaceCode, String endPlaceCode, String regionCode, String carTypeCode) {
        BmsContractPrice bmsContractPrice = null;
        switch (billModule) {
            case BmsConstants.BILL_MODULE_01:// 仓储价格
                bmsContractPrice = bmsContractStoragePriceService.getContractPrice(contractDetailId, skuClass, skuCode, orgId);
                break;
            case BmsConstants.BILL_MODULE_02:// 运输价格
                bmsContractPrice = bmsTransportPriceService.getContractPrice(contractDetailId, startPlaceCode, endPlaceCode, regionCode, carTypeCode, orgId);
                break;
            default:
        }
        return bmsContractPrice != null && BmsConstants.YES.equals(bmsContractPrice.getIsUseStep()) && BmsConstants.YES.equals(bmsContractPrice.getIsAccumulationMethod());
    }

    /**
     * 获取公式参数 - 枚举Label
     *
     * @param value 公式参数值
     */
    private String getFormulaParamLabel(String value) {
        return DictUtils.getDictLabel(value, "BMS_BILL_FORMULA_PARAM", value);
    }

    private String missingDataDesc(String sysContractNo, String skuClass, String skuCode) {
        return "合同" + sysContractNo
                + (StringUtils.isNotBlank(skuClass) ? "品类编码" + skuClass : "")
                + (StringUtils.isNotBlank(skuCode) ? "商品编码" + skuCode : "");
    }

    private String missingDataDesc(String sysContractNo, String startPlaceCode, String endPlaceCode, String regionCode, String carTypeCode) {
        return "合同" + sysContractNo
                + (StringUtils.isNotBlank(startPlaceCode) ? "起点编码" + startPlaceCode : "")
                + (StringUtils.isNotBlank(endPlaceCode) ? "终点编码" + endPlaceCode : "")
                + (StringUtils.isNotBlank(regionCode) ? "区域编码" + regionCode : "")
                + (StringUtils.isNotBlank(carTypeCode) ? "车型编码" + carTypeCode : "");
    }

    /**
     * 获取公式参数值 - 条款输出对象
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 条款输出对象值
     */
    private BigDecimal getFormulaParam001(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_001)).appendProcessDataDesc(":").appendProcessDataDesc(businessParams.getOutputValue().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");

        // 如果是按分段累加方式计算，则此处条款输出对象值设为1，因在累加计算时已计算入阶梯价格内了，不再重复算入
        boolean stepAccumulationMethod = this.isStepAccumulationMethod(conditionParams.getBillModule(), conditionParams.getContractDetailId(), conditionParams.getOrgId(),
                businessParams.getSkuClass(), businessParams.getSkuCode(), businessParams.getStartPlaceCode(), businessParams.getEndPlaceCode(), businessParams.getRegionCode(), businessParams.getCarTypeCode());
        return stepAccumulationMethod ? BigDecimal.ONE : businessParams.getOutputValue();
    }

    /**
     * 获取公式参数值 - 合同单价
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 合同单价
     */
    private BigDecimal getFormulaParam002(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_002)).appendProcessDataDesc(":");

        BmsContractPrice bmsContractPrice = null;
        if (BmsConstants.BILL_MODULE_01.equals(conditionParams.getBillModule())) {
            // 仓储价格
            bmsContractPrice = bmsContractStoragePriceService.getContractPrice(conditionParams.getContractDetailId(), businessParams.getSkuClass(), businessParams.getSkuCode(), conditionParams.getOrgId());
        } else if (BmsConstants.BILL_MODULE_02.equals(conditionParams.getBillModule())) {
            // 运输价格
            bmsContractPrice = bmsTransportPriceService.getContractPrice(conditionParams.getContractDetailId(), businessParams.getStartPlaceCode(), businessParams.getEndPlaceCode(), businessParams.getRegionCode(), businessParams.getCarTypeCode(), conditionParams.getOrgId());
        }
        if (bmsContractPrice != null) {
            if (BmsConstants.YES.equals(bmsContractPrice.getIsUseStep()) && CollectionUtil.isNotEmpty(bmsContractPrice.getSteppedPrices())) {
                // 阶梯价格；计算方式一：分段累加
                if (BmsConstants.YES.equals(bmsContractPrice.getIsAccumulationMethod())) {
                    // 本计算公式中是否含有FORMULA_PARAM_001
                    boolean isContainsOutput = conditionParams.getFormulaParameters().stream().anyMatch(o -> BmsConstants.FORMULA_PARAM_001.equals(o.getParameterValue()));

                    BigDecimal price = null;
                    for (BmsContractSteppedPrice steppedPrice : bmsContractPrice.getSteppedPrices()) {
                        if (steppedPrice.getTo() == null || steppedPrice.getTo().compareTo(businessParams.getOutputValue()) > 0) {
                            if (businessParams.getOutputValue().compareTo(steppedPrice.getFm()) < 0) {
                                break;
                            }
                            if (price == null) {
                                price = BigDecimal.ZERO;
                            }
                            BigDecimal v = isContainsOutput ? businessParams.getOutputValue().subtract(steppedPrice.getFm()) : BigDecimal.ONE;
                            price = price.add(steppedPrice.getPrice().multiply(v));
                            calculateEntity.appendProcessDataDesc(steppedPrice.getFm().stripTrailingZeros().toPlainString()).appendProcessDataDesc(" ~ ").appendProcessDataDesc(businessParams.getOutputValue().stripTrailingZeros().toPlainString()).appendProcessDataDesc(" ").appendProcessDataDesc(steppedPrice.getPrice().stripTrailingZeros().toPlainString());
                            break;
                        }
                        if (price == null) {
                            price = BigDecimal.ZERO;
                        }
                        BigDecimal v = isContainsOutput ? steppedPrice.getTo().subtract(steppedPrice.getFm()) : BigDecimal.ONE;
                        price = price.add(steppedPrice.getPrice().multiply(v));
                        calculateEntity.appendProcessDataDesc(steppedPrice.getFm().stripTrailingZeros().toPlainString()).appendProcessDataDesc(" ~ ").appendProcessDataDesc(steppedPrice.getTo().stripTrailingZeros().toPlainString()).appendProcessDataDesc(" ").appendProcessDataDesc(steppedPrice.getPrice().stripTrailingZeros().toPlainString()).appendProcessDataDesc(",");
                    }
                    calculateEntity.appendProcessDataDesc("(分段累加)，");
                    return price;
                }
                // 计算方式二：范围段直取
                for (BmsContractSteppedPrice steppedPrice : bmsContractPrice.getSteppedPrices()) {
                    if (steppedPrice.getTo() == null && businessParams.getOutputValue().compareTo(steppedPrice.getFm()) >= 0) {
                        calculateEntity.appendProcessDataDesc(steppedPrice.getPrice().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
                        calculateEntity.setContractPrice(steppedPrice.getPrice());
                        return steppedPrice.getPrice();
                    }
                    if (steppedPrice.getTo().compareTo(businessParams.getOutputValue()) > 0 && businessParams.getOutputValue().compareTo(steppedPrice.getFm()) >= 0) {
                        calculateEntity.appendProcessDataDesc(steppedPrice.getPrice().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
                        calculateEntity.setContractPrice(steppedPrice.getPrice());
                        return steppedPrice.getPrice();
                    }
                }
            } else if (!BmsConstants.YES.equals(bmsContractPrice.getIsUseStep()) && bmsContractPrice.getPrice() != null) {
                calculateEntity.appendProcessDataDesc(bmsContractPrice.getPrice().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
                calculateEntity.setContractPrice(bmsContractPrice.getPrice());
                return bmsContractPrice.getPrice();
            }
        }

        calculateEntity.appendProcessDataDesc(null).appendProcessDataDesc("，");
        calculateEntity.setContractPrice(null);
        if (BmsConstants.BILL_MODULE_01.equals(conditionParams.getBillModule())) {
            calculateEntity.appendMissingDataDesc(this.missingDataDesc(conditionParams.getSysContractNo(), businessParams.getSkuClass(), businessParams.getSkuCode()))
                    .appendMissingDataDesc("未发现匹配合同单价");
        } else if (BmsConstants.BILL_MODULE_02.equals(conditionParams.getBillModule())) {
            calculateEntity.appendMissingDataDesc(this.missingDataDesc(conditionParams.getSysContractNo(), businessParams.getStartPlaceCode(), businessParams.getEndPlaceCode(), businessParams.getRegionCode(), businessParams.getCarTypeCode()))
                    .appendMissingDataDesc("未发现匹配合同单价");
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取公式参数值 - 物流点数
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 物流点数
     */
    private BigDecimal getFormulaParam003(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_003)).appendProcessDataDesc(":");

        BmsContractPrice bmsContractPrice = null;
        if (BmsConstants.BILL_MODULE_01.equals(conditionParams.getBillModule())) {
            // 仓储
            bmsContractPrice = bmsContractStoragePriceService.getContractPrice(conditionParams.getContractDetailId(), businessParams.getSkuClass(), businessParams.getSkuCode(), conditionParams.getOrgId());
        } else if (BmsConstants.BILL_MODULE_02.equals(conditionParams.getBillModule())) {
            // 运输
            bmsContractPrice = bmsTransportPriceService.getContractPrice(conditionParams.getContractDetailId(), businessParams.getStartPlaceCode(), businessParams.getEndPlaceCode(), businessParams.getRegionCode(), businessParams.getCarTypeCode(), conditionParams.getOrgId());
        }
        if (bmsContractPrice != null && bmsContractPrice.getLogisticsPoints() != null) {
            calculateEntity.appendProcessDataDesc(bmsContractPrice.getLogisticsPoints().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
            calculateEntity.setLogisticsPoints(bmsContractPrice.getLogisticsPoints());
            return bmsContractPrice.getLogisticsPoints();
        }

        calculateEntity.appendProcessDataDesc(null).appendProcessDataDesc("，");
        calculateEntity.setContractPrice(null);
        if (BmsConstants.BILL_MODULE_01.equals(conditionParams.getBillModule())) {
            calculateEntity.appendMissingDataDesc(this.missingDataDesc(conditionParams.getSysContractNo(), businessParams.getSkuClass(), businessParams.getSkuCode()))
                    .appendMissingDataDesc("未发现匹配物流点数");
        } else if (BmsConstants.BILL_MODULE_02.equals(conditionParams.getBillModule())) {
            calculateEntity.appendMissingDataDesc(this.missingDataDesc(conditionParams.getSysContractNo(), businessParams.getStartPlaceCode(), businessParams.getEndPlaceCode(), businessParams.getRegionCode(), businessParams.getCarTypeCode()))
                    .appendMissingDataDesc("未发现匹配物流点数");
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取公式参数值 - 商品未税单价
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 商品未税单价
     */
    private BigDecimal getFormulaParam004(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_004)).appendProcessDataDesc(":");

        BmsContractSkuPrice bmsContractSkuPrice = bmsContractSkuPriceService.getContractPrice(conditionParams.getSysContractNo(), businessParams.getSkuClass(), businessParams.getSkuCode(), conditionParams.getOrgId());
        if (bmsContractSkuPrice != null && bmsContractSkuPrice.getPrice() != null) {
            calculateEntity.appendProcessDataDesc(bmsContractSkuPrice.getPrice().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
            calculateEntity.setLogisticsPoints(bmsContractSkuPrice.getPrice());
            return bmsContractSkuPrice.getPrice();
        }

        calculateEntity.appendProcessDataDesc(null).appendProcessDataDesc("，");
        calculateEntity.setContractPrice(null);
        calculateEntity.appendMissingDataDesc(this.missingDataDesc(conditionParams.getSysContractNo(), businessParams.getSkuClass(), businessParams.getSkuCode()))
                .appendMissingDataDesc("未发现匹配商品未税单价");
        return BigDecimal.ZERO;
    }

    /**
     * 获取公式参数值 - 商品含税单价
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 商品含税单价
     */
    private BigDecimal getFormulaParam005(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_005)).appendProcessDataDesc(":");

        BmsContractSkuPrice bmsContractSkuPrice = bmsContractSkuPriceService.getContractPrice(conditionParams.getSysContractNo(), businessParams.getSkuClass(), businessParams.getSkuCode(), conditionParams.getOrgId());
        if (bmsContractSkuPrice != null && bmsContractSkuPrice.getTaxPrice() != null) {
            calculateEntity.appendProcessDataDesc(bmsContractSkuPrice.getTaxPrice().stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
            calculateEntity.setLogisticsPoints(bmsContractSkuPrice.getTaxPrice());
            return bmsContractSkuPrice.getTaxPrice();
        }

        calculateEntity.appendProcessDataDesc(null).appendProcessDataDesc("，");
        calculateEntity.setContractPrice(null);
        calculateEntity.appendMissingDataDesc(this.missingDataDesc(conditionParams.getSysContractNo(), businessParams.getSkuClass(), businessParams.getSkuCode()))
                .appendMissingDataDesc("未发现匹配商品含税单价");
        return BigDecimal.ZERO;
    }

    /**
     * 获取公式参数值 - 合同系数
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @return 合同系数
     */
    private BigDecimal getFormulaParam006(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_006)).appendProcessDataDesc(":");

        BigDecimal coefficient = conditionParams.getCoefficient() == null ? BigDecimal.ONE : conditionParams.getCoefficient();

        calculateEntity.appendProcessDataDesc(coefficient.stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
        return coefficient;
    }

    /**
     * 获取公式参数值 - 日历系数
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 日历系数
     */
    private BigDecimal getFormulaParam007(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_007)).appendProcessDataDesc(":");

        BmsCalendar calendar = bmsCalendarService.getByDate(businessParams.getBusinessDate(), conditionParams.getOrgId());
        BigDecimal coefficient = calendar == null || calendar.getCoefficient() != null ? BigDecimal.ONE : calendar.getCoefficient();

        calculateEntity.appendProcessDataDesc(coefficient.stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
        return coefficient;
    }

    /**
     * 获取公式参数值 - 税率
     *
     * @param calculateEntity 计算实体
     * @param conditionParams 费用计算条件参数
     * @return 税率
     */
    private BigDecimal getFormulaParam008(BmsCalculateEntity calculateEntity, BmsCalcConditionParams conditionParams) {
        calculateEntity.appendProcessDataDesc(getFormulaParamLabel(BmsConstants.FORMULA_PARAM_008)).appendProcessDataDesc(":");

        BigDecimal taxRate = conditionParams.getTaxRate() == null ? BigDecimal.ONE : conditionParams.getTaxRate();

        calculateEntity.appendProcessDataDesc(taxRate.stripTrailingZeros().toPlainString()).appendProcessDataDesc("，");
        return taxRate;
    }

    /**
     * 解析公式并计算
     *
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     */
    private BmsCalculateEntity analyzeAndCalculate(BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        BmsCalculateEntity bmsCalculateEntity = new BmsCalculateEntity();
        try {
            Jep jep = new Jep();// 计算结果
            for (BmsBillFormulaParameter parameter : conditionParams.getFormulaParameters()) {
                BigDecimal value;
                switch (parameter.getParameterValue()) {
                    case BmsConstants.FORMULA_PARAM_001:
                        value = this.getFormulaParam001(bmsCalculateEntity, conditionParams, businessParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_002:
                        value = this.getFormulaParam002(bmsCalculateEntity, conditionParams, businessParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_003:
                        value = this.getFormulaParam003(bmsCalculateEntity, conditionParams, businessParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_004:
                        value = getFormulaParam004(bmsCalculateEntity, conditionParams, businessParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_005:
                        value = getFormulaParam005(bmsCalculateEntity, conditionParams, businessParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_006:
                        value = getFormulaParam006(bmsCalculateEntity, conditionParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_007:
                        value = getFormulaParam007(bmsCalculateEntity, conditionParams, businessParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                    case BmsConstants.FORMULA_PARAM_008:
                        value = getFormulaParam008(bmsCalculateEntity, conditionParams);
                        jep.addVariable(parameter.getParameterName(), value);
                        break;
                }
            }
            jep.parse(conditionParams.getFormula());
            bmsCalculateEntity.setResult(BigDecimal.valueOf(jep.evaluateD()));
            return bmsCalculateEntity;
        } catch (JepException e) {
            logger.error("公式解析计算异常，公式：" + conditionParams.getFormula(), e);
            throw new BmsException("公式解析计算异常");
        }
    }

    /**
     * 计算计费量
     *
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @return 计费量
     */
    private BigDecimal calcQty(BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        return conditionParams.getFormulaParameters().stream().anyMatch(o -> BmsConstants.FORMULA_PARAM_001.equals(o.getParameterValue())) ? businessParams.getOutputValue() : BigDecimal.ONE;
    }

    /**
     * 计算与商品相关的量
     *
     * @param type   计算商品数量类型
     * @param params 计算商品相关量业务参数
     * @return 发生量
     */
    public BigDecimal calcSkuQty(CalcSkuQtyType type, List<BmsCalcSkuParams> params) {
        if (type == null || CollectionUtil.isEmpty(params)) {
            return BigDecimal.ZERO;
        }
        switch (type) {
            case QTY:
                return params.stream().map(BmsCalcSkuParams::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case ACTUAL_CS_QTY:
                return params.stream().map(BmsCalcSkuParams::getCsQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case ACTUAL_PL_QTY:
                return params.stream().map(BmsCalcSkuParams::getPlQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case THEORY_CS_QTY:
                return params.stream().collect(Collectors.groupingBy(o -> o.getOwnerCode() + o.getSkuCode() + o.getSettleOrgId())).values().stream().map(list -> BigDecimal.valueOf(settlementSkuService.convertQty(list.get(0).getOwnerCode(), list.get(0).getSkuCode(), list.get(0).getSettleOrgId(), "EA", list.stream().map(BmsCalcSkuParams::getQty).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue(), "CS"))).reduce(BigDecimal.ZERO, BigDecimal::add);
            case THEORY_PL_QTY:
                return params.stream().collect(Collectors.groupingBy(o -> o.getOwnerCode() + o.getSkuCode() + o.getSettleOrgId())).values().stream().map(list -> BigDecimal.valueOf(settlementSkuService.convertQty(list.get(0).getOwnerCode(), list.get(0).getSkuCode(), list.get(0).getSettleOrgId(), "EA", list.stream().map(BmsCalcSkuParams::getQty).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue(), "PL"))).reduce(BigDecimal.ZERO, BigDecimal::add);
            case WEIGHT:
                return params.stream().map(BmsCalcSkuParams::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
            case VOLUME:
                return params.stream().map(BmsCalcSkuParams::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);
            default:
        }
        return BigDecimal.ZERO;
    }

    /**
     * 计算条款发生量
     *
     * @param occurrence 条款发生量
     * @param params     业务参数
     * @return 发生量值
     */
    public BigDecimal calcOccurrenceQty(String occurrence, List<BmsCalcSkuParams> params) {
        if (BmsConstants.OCCURRENCE_QUANTITY_001.equals(occurrence)) {
            return this.calcSkuQty(CalcSkuQtyType.QTY, params);
        } else if (BmsConstants.OCCURRENCE_QUANTITY_002.equals(occurrence)) {
            return this.calcSkuQty(CalcSkuQtyType.ACTUAL_CS_QTY, params);
        } else if (BmsConstants.OCCURRENCE_QUANTITY_003.equals(occurrence)) {
            return this.calcSkuQty(CalcSkuQtyType.ACTUAL_PL_QTY, params);
        } else if (BmsConstants.OCCURRENCE_QUANTITY_004.equals(occurrence)) {
            return this.calcSkuQty(CalcSkuQtyType.THEORY_CS_QTY, params);
        } else if (BmsConstants.OCCURRENCE_QUANTITY_005.equals(occurrence)) {
            return this.calcSkuQty(CalcSkuQtyType.THEORY_PL_QTY, params);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 计算结果
     *
     * @param conditionParams 费用计算条件参数
     * @param businessParams  费用计算业务参数
     */
    public BmsCalculateEntity calcResult(BmsCalcConditionParams conditionParams, BmsCalcBusinessParams businessParams) {
        BmsCalculateEntity bmsCalculateEntity = this.analyzeAndCalculate(conditionParams, businessParams);

        BigDecimal minAmount = conditionParams.getMinAmount();
        BigDecimal maxAmount = conditionParams.getMaxAmount();
        if (StringUtils.isBlank(bmsCalculateEntity.getMissingDataDesc()) && maxAmount != null
                && maxAmount.compareTo(BigDecimal.ZERO) > 0 && maxAmount.compareTo(bmsCalculateEntity.getResult()) < 0) {
            bmsCalculateEntity.setResult(maxAmount);
        } else if (StringUtils.isBlank(bmsCalculateEntity.getMissingDataDesc()) && minAmount != null
                && minAmount.compareTo(BigDecimal.ZERO) > 0 && minAmount.compareTo(bmsCalculateEntity.getResult()) > 0) {
            bmsCalculateEntity.setResult(minAmount);
        }
        bmsCalculateEntity.setQty(this.calcQty(conditionParams, businessParams));

        if (minAmount != null) {
            bmsCalculateEntity.appendProcessDataDesc("最小金额:").appendProcessDataDesc(minAmount.stripTrailingZeros().toPlainString()).appendProcessDataDesc(" ");
        }
        if (maxAmount != null) {
            bmsCalculateEntity.appendProcessDataDesc("最大金额:").appendProcessDataDesc(maxAmount.stripTrailingZeros().toPlainString()).appendProcessDataDesc(" ");
        }
        return bmsCalculateEntity;
    }

}
