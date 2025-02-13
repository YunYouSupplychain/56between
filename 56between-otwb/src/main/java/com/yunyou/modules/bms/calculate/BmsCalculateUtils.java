package com.yunyou.modules.bms.calculate;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;
import com.yunyou.modules.bms.calculate.business.BmsBusinessSkuParams;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.common.CalcSkuQtyType;
import com.yunyou.modules.bms.finance.entity.BmsCalculateProcessNode;
import com.yunyou.modules.bms.finance.entity.BmsChargeDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsChargeResultEntity;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import org.apache.commons.text.CaseUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费用计算器工具类
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsCalculateUtils {

    /**
     * 将模型条款参数转换为SQL条件语句
     *
     * @param params 条款参数
     * @return SQL条件语句
     */
    public static String toWhereSql(List<BmsCalcTermsParams> params) {
        StringBuilder sql = new StringBuilder();
        if (CollectionUtil.isEmpty(params)) {
            return sql.toString();
        }
        for (BmsCalcTermsParams o : params) {
            if (!BmsConstants.YES.equals(o.getIsEnable()) || StringUtils.isBlank(o.getFieldValue())) {
                continue;
            }
            // 将机构编码转换为机构ID
            if ("org_id".equals(o.getField()) || o.getField().endsWith(".org_id")) {
                o.setFieldValue(Arrays.stream(o.getFieldValue().split(",")).map(orgCode -> {
                    Office office = UserUtils.getOfficeByCode(orgCode);
                    if (office != null) {
                        return office.getId();
                    } else {
                        return "";
                    }
                }).collect(Collectors.joining(",")));
            }
            // 日期类型字段皆为范围，以逗号隔开
            if (BmsConstants.TERMS_PARAM_TYPE_DATE.equals(o.getType())) {
                if (o.getFieldValue().contains(",")) {
                    String[] values = o.getFieldValue().split(",");
                    sql.append(" and ").append(o.getField()).append(" >= '").append(values[0]).append("'");
                    sql.append(" and ").append(o.getField()).append(" <= '").append(values[1]).append("'");
                } else {
                    sql.append(" and ").append(o.getField()).append(" >= '").append(o.getFieldValue()).append("'");
                }
            } else {
                sql.append(" and ").append(o.getField());
                if (o.getFieldValue().contains(",")) {
                    String[] values = o.getFieldValue().split(",");
                    sql.append(" in (");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            sql.append(",");
                        }
                        sql.append(" '").append(values[i]).append("'");
                    }
                    sql.append(")");
                } else {
                    sql.append(" = '").append(o.getFieldValue()).append("' ");
                }
            }
        }
        return sql.toString();
    }

    /**
     * 获取业务日期
     * <p> T必须是 {@link DataEntity} 的子类，且必须与根据各自处理方法名称要求提取的数据类型一致，否则就会发生类型转换异常</p>
     *
     * @param t           原始业务数据
     * @param termsParams 条款条件参数
     * @param <T>         实际业务数据类型
     * @return 业务日期
     */
    public static <T extends DataEntity<T>> Date getBusinessDate(T t, List<BmsCalcTermsParams> termsParams) {
        String fieldName = CollectionUtil.isEmpty(termsParams) ? "" : termsParams.stream().filter(o -> BmsConstants.YES.equals(o.getIsSettleDate())).map(BmsCalcTermsParams::getField).findFirst().orElse("");

        try {
            int index = fieldName.indexOf(".");
            String str = fieldName;
            if (index != -1) {
                str = str.substring(index);
            }
            Field field = t.getClass().getDeclaredField(CaseUtils.toCamelCase(str, false, '.', '_'));
            field.setAccessible(true);
            return (Date) field.get(t);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    /**
     * 计算与商品相关的量
     *
     * @param type   需计算商品数量类型
     * @param params 商品相关量业务参数
     * @return 商品相关的量
     */
    public static BigDecimal calcSkuQty(CalcSkuQtyType type, List<BmsBusinessSkuParams> params) {
        if (type == null || CollectionUtil.isEmpty(params)) {
            return BigDecimal.ZERO;
        }
        switch (type) {
            case QTY:
                return params.stream().map(BmsBusinessSkuParams::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case ACTUAL_CS_QTY:
                return params.stream().map(BmsBusinessSkuParams::getCsQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case ACTUAL_PL_QTY:
                return params.stream().map(BmsBusinessSkuParams::getPlQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case THEORY_CS_QTY:
                return params.stream().map(BmsBusinessSkuParams::getTheoryCsQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case THEORY_PL_QTY:
                return params.stream().map(BmsBusinessSkuParams::getTheoryPlQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            case WEIGHT:
                return params.stream().map(BmsBusinessSkuParams::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
            case VOLUME:
                return params.stream().map(BmsBusinessSkuParams::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * 费用计算
     *
     * @param calcData 计算数据
     * @return 费用结果
     */
    public static List<BmsChargeDetail> calc(List<BmsCalcData> calcData) {
        return calcData.stream().map(BmsCalculateUtils::calcResult).collect(Collectors.toList());
    }

    /**
     * 解析公式并计算
     *
     * @param calcData 计算数据
     */
    private static BigDecimal analyzeAndCalc(BmsCalcData calcData) {
        try {
            List<BmsCalculateProcessNode> nodes = Lists.newArrayList();
            // 解析公式计算结果
            Jep jep = new Jep();
            for (BmsBillFormulaParameter parameter : calcData.getContractData().getFormulaParameters()) {
                switch (parameter.getParameterValue()) {
                    case BmsConstants.FORMULA_PARAM_001:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam001().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam001().getCalcValue(), "条款输出值"));
                        break;
                    case BmsConstants.FORMULA_PARAM_002:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam002().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam002().getCalcValue(), "合同单价"));
                        break;
                    case BmsConstants.FORMULA_PARAM_003:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam003().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam003().getCalcValue(), "物流点数"));
                        break;
                    case BmsConstants.FORMULA_PARAM_004:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam004().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam004().getCalcValue(), "商品未税单价"));
                        break;
                    case BmsConstants.FORMULA_PARAM_005:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam005().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam005().getCalcValue(), "商品含税单价"));
                        break;
                    case BmsConstants.FORMULA_PARAM_006:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam006().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam006().getCalcValue(), "合同系数"));
                        break;
                    case BmsConstants.FORMULA_PARAM_007:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam007().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam007().getCalcValue(), "日历系数"));
                        break;
                    case BmsConstants.FORMULA_PARAM_008:
                        jep.addVariable(parameter.getParameterName(), calcData.getFormulaParam008().getCalcValue());

                        nodes.add(new BmsCalculateProcessNode(calcData.getFormulaParam008().getCalcValue(), "税率"));
                        break;
                    default:
                }
            }
            jep.parse(calcData.getContractData().getFormula());
            BigDecimal amount = BigDecimal.valueOf(jep.evaluateD());

            BmsCalculateProcessNode parentNode = new BmsCalculateProcessNode(amount, "费用");
            nodes.forEach(o -> o.setParent(parentNode));
            nodes.add(parentNode);
            return amount;
        } catch (JepException e) {
            throw new BmsException("公式【" + calcData.getContractData().getFormula() + "】解析计算失败，原因：" + e.getMessage());
        }
    }

    /**
     * 计算结果
     *
     * @param calcData 计算数据
     */
    private static BmsChargeDetail calcResult(BmsCalcData calcData) {
        BigDecimal amount = analyzeAndCalc(calcData);
        BigDecimal minAmount = calcData.getContractData().getMinAmount();
        BigDecimal maxAmount = calcData.getContractData().getMaxAmount();
        if (maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) > 0 && maxAmount.compareTo(amount) < 0) {
            amount = maxAmount;
        } else if (minAmount != null && minAmount.compareTo(BigDecimal.ZERO) > 0 && minAmount.compareTo(amount) > 0) {
            amount = minAmount;
        }

        BmsChargeDetail bmsChargeDetail = new BmsChargeDetail();
        bmsChargeDetail.setSettleLotNo(calcData.getSettleLotNo());
        bmsChargeDetail.setSettleDateFm(calcData.getContractData().getSettleDateFm());
        bmsChargeDetail.setSettleDateTo(calcData.getContractData().getSettleDateTo());
        bmsChargeDetail.setBusinessOrgCode(calcData.getBusinessData().getBusinessOrgCode());
        bmsChargeDetail.setBusinessOrgName(calcData.getBusinessData().getBusinessOrgName());
        bmsChargeDetail.setSettleCode(calcData.getContractData().getSettleObjectCode());
        bmsChargeDetail.setSettleName(calcData.getContractData().getSettleObjectName());
        bmsChargeDetail.setSysContractNo(calcData.getContractData().getSysContractNo());
        bmsChargeDetail.setContractNo(calcData.getContractData().getContractNo());
        bmsChargeDetail.setSubjectCode(calcData.getContractData().getBillSubjectCode());
        bmsChargeDetail.setSubjectName(calcData.getContractData().getBillSubjectName());
        bmsChargeDetail.setTermsCode(calcData.getContractData().getBillTermsCode());
        bmsChargeDetail.setTermsDesc(calcData.getContractData().getBillTermsDesc());
        bmsChargeDetail.setTermsOutput(calcData.getContractData().getOutputObjects());
        bmsChargeDetail.setSettleModelCode(calcData.getContractData().getSettleModelCode());
        bmsChargeDetail.setFormula(calcData.getContractData().getFormulaName());
        bmsChargeDetail.setArOrAp(calcData.getContractData().getArOrAp());
        bmsChargeDetail.setAmount(amount);
        bmsChargeDetail.setOrgId(calcData.getContractData().getOrgId());
        return bmsChargeDetail;
    }

    /**
     * 将费用明细按结算批次号、结算日期范围、业务机构编码、结算模型编码、结算对象编码、系统合同号、费用科目编码、计费条款编码、计费公式、应收应付、机构等维度转化为费用结果
     *
     * @param chargeDetails 费用明细
     */
    public static List<BmsChargeResultEntity> detailToResult(List<BmsChargeDetail> chargeDetails) {
        return chargeDetails.stream()
                .collect(Collectors.groupingBy(o -> o.getSettleLotNo() + o.getSettleDateFm() + o.getSettleDateTo() + o.getBusinessOrgCode()
                        + o.getSettleModelCode() + o.getSettleCode() + o.getSysContractNo() + o.getSubjectCode()
                        + o.getTermsCode() + o.getFormula() + o.getArOrAp() + o.getOrgId()))
                .values()
                .stream().map(list -> {
                    BmsChargeDetail detail = list.get(0);

                    BmsChargeResultEntity result = new BmsChargeResultEntity();
                    result.setSettleLotNo(detail.getSettleLotNo());
                    result.setSettleDateFm(detail.getSettleDateFm());
                    result.setSettleDateTo(detail.getSettleDateTo());
                    result.setBusinessOrgCode(detail.getBusinessOrgCode());
                    result.setBusinessOrgName(detail.getBusinessOrgName());
                    result.setSettleModelCode(detail.getSettleModelCode());
                    result.setSettleCode(detail.getSettleCode());
                    result.setSettleName(detail.getSettleName());
                    result.setSysContractNo(detail.getSysContractNo());
                    result.setContractNo(detail.getContractNo());
                    result.setSubjectCode(detail.getSubjectCode());
                    result.setSubjectName(detail.getSubjectName());
                    result.setTermsCode(detail.getTermsCode());
                    result.setTermsDesc(detail.getTermsDesc());
                    result.setTermsOutput(detail.getTermsOutput());
                    result.setFormula(detail.getFormula());
                    result.setArOrAp(detail.getArOrAp());
                    result.setAmount(list.stream().map(BmsChargeDetail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                    result.setOrgId(detail.getOrgId());
                    result.setChargeDetailIds(list.stream().map(BmsChargeDetail::getId).collect(Collectors.toList()));
                    return result;
                }).collect(Collectors.toList());
    }
}
