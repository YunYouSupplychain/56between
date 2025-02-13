package com.yunyou.modules.bms.finance.service.calc;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.bms.basic.service.BmsBillFormulaParameterService;
import com.yunyou.modules.bms.business.entity.*;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.CalcMethod;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcConditionParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcSkuParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.bms.finance.mapper.BmsCalculateCostMapper;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.google.common.collect.Lists;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计算前准备Service
 */
@Service
public class BmsPrepareService {

    @Autowired
    private BmsCalculateCostMapper mapper;
    @Autowired
    private BmsBillFormulaParameterService bmsBillFormulaParameterService;
    @Autowired
    private OfficeService officeService;

    /**
     * 获取费用计算条件参数
     *
     * @param type   类型(1:结算模型 2:合同)
     * @param id     结算模型ID / 合同ID
     * @param fmDate 结算日期从
     * @param toDate 结算日期到
     * @return 费用计算条件参数
     */
    protected List<BmsCalcConditionParams> getConditionParams(String type, String id, Date fmDate, Date toDate) {
        List<BmsCalcConditionParams> list;

        // 不同的type对应着不同的获取、处理方式
        if ("1".equals(type)) {
            // 按结算模型根据id获取到基本条件参数
            list = mapper.findConditionParamsByModel(id);
        } else if ("2".equals(type)) {
            // 按合同根据id获取到基本条件参数
            list = mapper.findConditionParamsByContract(id);
        } else {
            return Lists.newArrayList();
        }

        for (BmsCalcConditionParams o : list) {
            // 将参数中的合同有效期时间范围与结算日期时间范围做交集处理，计算出实际有效的结算日期范围数据
            Date effectiveDateFm = o.getEffectiveDateFm();
            Date effectiveDateTo = o.getEffectiveDateTo();
            if (effectiveDateFm.after(toDate) || effectiveDateTo.before(fmDate)) {
                continue;
            }
            if (fmDate.before(effectiveDateFm)) {
                o.setSettleDateFm(effectiveDateFm);
            } else {
                o.setSettleDateFm(fmDate);
            }
            if (!toDate.before(effectiveDateTo)) {
                o.setSettleDateTo(effectiveDateTo);
            } else {
                o.setSettleDateTo(toDate);
            }

            // 补充计算条款参数并对条款参数中参数类型为结算日期的字段赋值
            List<BmsCalcTermsParams> includeParams;
            List<BmsCalcTermsParams> excludeParams;
            if ("1".equals(type)) {
                includeParams = mapper.findByModelDetailIdAndIoE(o.getDetailId(), BmsConstants.INCLUDE);
                excludeParams = mapper.findByModelDetailIdAndIoE(o.getDetailId(), BmsConstants.EXCLUDE);
            } else {
                includeParams = mapper.findByContractDetailIdAndIoE(o.getDetailId(), BmsConstants.INCLUDE);
                excludeParams = mapper.findByContractDetailIdAndIoE(o.getDetailId(), BmsConstants.EXCLUDE);
            }
            for (BmsCalcTermsParams params : includeParams) {
                if (BmsConstants.YES.equals(params.getIsSettleDate())) {
                    String fmDateStr = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, DateUtil.beginOfDate(o.getSettleDateFm()));
                    String toDateStr = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, DateUtil.endOfDate(o.getSettleDateTo()));
                    params.setFieldValue(fmDateStr + "," + toDateStr);
                    break;
                }
            }
            o.setIncludeParams(includeParams);
            o.setExcludeParams(excludeParams);

            // 补充计算公式参数
            o.setFormulaParameters(bmsBillFormulaParameterService.findByFormulaCode(o.getFormulaCode()));
        }
        return list;
    }

    /**
     * 将模型条款参数转换为SQL条件语句
     *
     * @param params 参数
     * @return SQL条件语句
     */
    private String toWhereSql(List<BmsCalcTermsParams> params) {
        StringBuilder sql = new StringBuilder();
        if (CollectionUtil.isNotEmpty(params)) {
            for (BmsCalcTermsParams o : params) {
                if (!BmsConstants.YES.equals(o.getIsEnable()) || StringUtils.isBlank(o.getFieldValue())) {
                    continue;
                }
                // 将机构编码转换为机构ID
                if ("org_id".equals(o.getField())) {
                    o.setFieldValue(Arrays.stream(o.getFieldValue().split(",")).map(orgCode -> {
                        Office office = officeService.getByCode(orgCode);
                        if (office != null) {
                            return office.getId();
                        } else {
                            return "";
                        }
                    }).collect(Collectors.joining(",")));
                }
                if (BmsConstants.TERMS_PARAM_TYPE_DATE.equals(o.getType())) {
                    // 日期类型字段皆为范围，以逗号隔开
                    String[] values = o.getFieldValue().split(",");
                    sql.append(" and ").append(o.getField()).append(" >= '").append(values[0]).append("'");
                    sql.append(" and ").append(o.getField()).append(" <= '").append(values[1]).append("'");
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
        }
        return sql.toString();
    }

    /**
     * 获取业务数据
     * <p> T必须是 {@link DataEntity} 的子类，且必须与根据各自处理方法名称要求提取的数据类型一致，否则就会发生类型转换异常</p>
     *
     * @param methodName    处理方法名称
     * @param includeParams 业务数据过滤参数(包含)
     * @param excludeParams 业务数据过滤参数(排除)
     * @param <T>           实际业务数据类型
     * @return 业务数据
     * @throws ClassCastException 类型转换异常
     */
    protected <T extends DataEntity<T>> List<T> getBusinessData(String methodName, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {

        List<T> rsList = Lists.newArrayList();
        String includeSql = toWhereSql(includeParams);
        if (StringUtils.isBlank(includeSql)) {
            return rsList;
        }
        String excludeSql = toWhereSql(excludeParams);
        List<DataEntity<T>> includeData = Lists.newArrayList();
        List<DataEntity<T>> excludeData = Lists.newArrayList();
        switch (CalcMethod.valueOf(methodName)) {
            case calcInbound:
                includeData = mapper.findInboundData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findInboundData(excludeSql);
                }
                break;
            case calcOutbound:
                includeData = mapper.findOutboundData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findOutboundData(excludeSql);
                }
                break;
            case calcInventory:
                includeData = mapper.findInventoryData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findInventoryData(excludeSql);
                }
                break;
            case calcDispatchOrder:
                includeData = mapper.findDispatchOrderData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findDispatchOrderData(excludeSql);
                }
                break;
            case calcDispatch:
                includeData = mapper.findDispatchData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findDispatchData(excludeSql);
                }
                break;
            case calcReturn:
                includeData = mapper.findReturnData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findReturnData(excludeSql);
                }
                break;
            case calcException:
                includeData = mapper.findExceptionData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findExceptionData(excludeSql);
                }
                break;
            case calcWaybill:
                includeData = mapper.findWaybillData(includeSql);
                if (StringUtils.isNotBlank(excludeSql)) {
                    excludeData = mapper.findWaybillData(excludeSql);
                }
                break;
            default:
                break;
        }
        if (CollectionUtil.isEmpty(includeData)) {
            return rsList;
        }
        List<String> excludeIds = excludeData.stream().map(DataEntity::getId).collect(Collectors.toList());
        List<DataEntity<T>> list = includeData.stream().filter(o -> !excludeIds.contains(o.getId())).collect(Collectors.toList());

        for (DataEntity<T> entity : list) {
            @SuppressWarnings("unchecked") T t = (T) entity;
            rsList.add(t);
        }
        return rsList;
    }

    /**
     * 获取业务日期
     * <p> T必须是 {@link DataEntity} 的子类，且必须与根据各自处理方法名称要求提取的数据类型一致，否则就会发生类型转换异常</p>
     *
     * @param t         业务数据
     * @param fieldName 结算日期字段名称
     * @param <T>       实际业务数据类型
     * @return 业务日期
     */
    protected <T extends DataEntity<T>> Date getBusinessDate(T t, String fieldName) {
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
     * 获取计费条款参数中的结算日期字段
     *
     * @param params 计费条款参数
     * @return 结算日期字段
     */
    protected String getSettleDataField(List<BmsCalcTermsParams> params) {
        return CollectionUtil.isEmpty(params) ? "" : params.stream().filter(o -> BmsConstants.YES.equals(o.getIsSettleDate())).map(BmsCalcTermsParams::getField).findFirst().orElse("");
    }

    /**
     * 提取业务数据中商品相关数据
     *
     * @param list 业务数据
     * @param <T>  业务数据类型
     * @return 商品相关业务参数
     */
    protected <T extends DataEntity<T>> List<BmsCalcSkuParams> extractSkuParams(List<T> list, String settleOrgId) {
        if (CollectionUtil.isEmpty(list)) {
            return Lists.newArrayList();
        }

        List<BmsCalcSkuParams> rsList = Lists.newArrayList();
        for (T t : list) {
            if (t instanceof BmsInboundData) {
                BmsInboundData o = (BmsInboundData) t;
                BigDecimal qty = o.getReceiptQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQty());
                BigDecimal qtyCs = o.getReceiptQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQtyCs());
                BigDecimal qtyPl = o.getReceiptQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            } else if (t instanceof BmsOutboundData) {
                BmsOutboundData o = (BmsOutboundData) t;
                BigDecimal qty = o.getShipQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getShipQty());
                BigDecimal qtyCs = o.getShipQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getShipQtyCs());
                BigDecimal qtyPl = o.getShipQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getShipQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            } else if (t instanceof BmsInventoryData) {
                BmsInventoryData o = (BmsInventoryData) t;
                BigDecimal qty = o.getEndQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getEndQty());
                BigDecimal qtyCs = o.getEndQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getEndQtyCs());
                BigDecimal qtyPl = o.getEndQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getEndQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            } else if (t instanceof BmsDispatchData) {
                BmsDispatchData o = (BmsDispatchData) t;
                BigDecimal qty = o.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQty());
                BigDecimal qtyCs = o.getQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQtyCs());
                BigDecimal qtyPl = o.getQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            } else if (t instanceof BmsReturnData) {
                BmsReturnData o = (BmsReturnData) t;
                BigDecimal qty = o.getReceiptQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQty());
                BigDecimal qtyCs = o.getReceiptQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQtyCs());
                BigDecimal qtyPl = o.getReceiptQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getReceiptQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            } else if (t instanceof BmsExceptionData) {
                BmsExceptionData o = (BmsExceptionData) t;
                BigDecimal qty = o.getExceptionQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getExceptionQty());
                BigDecimal qtyCs = o.getExceptionQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getExceptionQtyCs());
                BigDecimal qtyPl = o.getExceptionQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getExceptionQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            } else if (t instanceof BmsWaybillData) {
                BmsWaybillData o = (BmsWaybillData) t;
                BigDecimal qty = o.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQty());
                BigDecimal qtyCs = o.getQtyCs() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQtyCs());
                BigDecimal qtyPl = o.getQtyPl() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getQtyPl());
                BigDecimal weight = o.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getWeight());
                BigDecimal volume = o.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(o.getVolume());
                rsList.add(new BmsCalcSkuParams(o.getOwnerCode(), o.getSkuCode(), o.getSkuClass(), settleOrgId, qty, qtyCs, qtyPl, weight, volume));
            }
        }
        return rsList;
    }
}
