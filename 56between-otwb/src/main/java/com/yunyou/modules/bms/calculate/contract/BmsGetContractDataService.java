package com.yunyou.modules.bms.calculate.contract;

import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.basic.entity.BmsCalendar;
import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSteppedPrice;
import com.yunyou.modules.bms.basic.service.*;
import com.yunyou.modules.bms.calculate.BmsCalcProcessObj;
import com.yunyou.modules.bms.calculate.business.BmsCalcBusinessData;
import com.yunyou.modules.bms.calculate.contract.mapper.BmsGetContractDataMapper;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.sys.utils.DictUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 获取计算相关合同数据Service
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@Service
public class BmsGetContractDataService extends BaseService {
    @Autowired
    private BmsGetContractDataMapper mapper;
    @Autowired
    private BmsBillFormulaParameterService bmsBillFormulaParameterService;
    @Autowired
    private BmsContractSkuPriceService bmsContractSkuPriceService;
    @Autowired
    private BmsContractStoragePriceService bmsContractStoragePriceService;
    @Autowired
    private BmsTransportPriceService bmsTransportPriceService;
    @Autowired
    private BmsCalendarService bmsCalendarService;

    /**
     * 是否按阶梯价格 分段累加方式计算
     *
     * @param billModule       费用模块
     * @param contractDetailId 合同明细ID
     * @param orgId            机构ID
     * @param skuClass         品类
     * @param skuCode          商品编码
     * @param startPlaceCode   起始地编码
     * @param endPlaceCode     目的地编码
     * @param regionCode       区域编码
     * @param carTypeCode      车型
     */
    private boolean isStepAccumulationMethod(String billModule, String contractDetailId, String orgId, String skuClass, String skuCode,
                                             String startPlaceCode, String endPlaceCode, String regionCode, String carTypeCode) {
        BmsContractPrice bmsContractPrice = null;
        switch (billModule) {
            // 仓储价格
            case BmsConstants.BILL_MODULE_01:
                bmsContractPrice = bmsContractStoragePriceService.getContractPrice(contractDetailId, skuClass, skuCode, orgId);
                break;
            // 运输价格
            case BmsConstants.BILL_MODULE_02:
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

    /**
     * 费用计算按模型计算 - 合同数据
     *
     * @param modelId 结算模型ID
     * @param fmDate  结算日期从
     * @param toDate  结算日期到
     * @return 合同数据
     */
    public List<BmsCalcContractData> findContractDataByModel(String modelId, Date fmDate, Date toDate) {
        List<BmsCalcContractData> list = mapper.findContractDataByModel(modelId);
        for (BmsCalcContractData o : list) {
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
            List<BmsCalcTermsParams> includeParams = mapper.findByModelDetailIdAndIoE(o.getSettleModelDetailId(), BmsConstants.INCLUDE);
            List<BmsCalcTermsParams> excludeParams = mapper.findByModelDetailIdAndIoE(o.getSettleModelDetailId(), BmsConstants.EXCLUDE);
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
     * 费用计算按合同计算 - 合同数据
     *
     * @param contactId 合同ID
     * @param fmDate    结算日期从
     * @param toDate    结算日期到
     * @return 合同数据
     */
    public List<BmsCalcContractData> findContractDataByContract(String contactId, Date fmDate, Date toDate) {
        List<BmsCalcContractData> list = mapper.findContractDataByContract(contactId);
        for (BmsCalcContractData o : list) {
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
            List<BmsCalcTermsParams> includeParams = mapper.findByContractDetailIdAndIoE(o.getContractDetailId(), BmsConstants.INCLUDE);
            List<BmsCalcTermsParams> excludeParams = mapper.findByContractDetailIdAndIoE(o.getContractDetailId(), BmsConstants.EXCLUDE);
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
     * 获取公式参数值 - 条款输出对象
     *
     * @param contractData 费用计算条件参数
     * @param businessData 费用计算业务参数
     * @return 条款输出对象值
     */
    public BmsCalcProcessObj getFormulaParam001(BmsCalcContractData contractData, BmsCalcBusinessData businessData) {
        // 如果是按分段累加方式计算，则此处条款输出对象值设为1，因在累加计算时已计算入阶梯价格内了，不再重复算入
        boolean stepAccumulationMethod = this.isStepAccumulationMethod(contractData.getBillModule(), contractData.getContractDetailId(), contractData.getOrgId(),
                businessData.getSkuClass(), businessData.getSkuCode(), businessData.getStartPlaceCode(), businessData.getEndPlaceCode(), businessData.getRegionCode(), businessData.getCarTypeCode());
        BigDecimal calcValue = stepAccumulationMethod ? BigDecimal.ONE : businessData.getOutputValue();
        List<String> calcRelationIds = businessData.getBusinessDataIds();

        return new BmsCalcProcessObj("001", calcValue, businessData.getOutputValue(), calcRelationIds);
    }

    /**
     * 获取公式参数值 - 合同单价
     *
     * @param contractData 费用计算条件参数
     * @param businessData 费用计算业务参数
     * @return 合同单价
     */
    public BmsCalcProcessObj getFormulaParam002(BmsCalcContractData contractData, BmsCalcBusinessData businessData) {
        BmsContractPrice bmsContractPrice;
        if (BmsConstants.BILL_MODULE_01.equals(contractData.getBillModule())) {
            // 仓储价格
            bmsContractPrice = bmsContractStoragePriceService.getContractPrice(contractData.getContractDetailId(), businessData.getSkuClass(), businessData.getSkuCode(), contractData.getOrgId());
        } else if (BmsConstants.BILL_MODULE_02.equals(contractData.getBillModule())) {
            // 运输价格
            bmsContractPrice = bmsTransportPriceService.getContractPrice(contractData.getContractDetailId(), businessData.getStartPlaceCode(), businessData.getEndPlaceCode(), businessData.getRegionCode(), businessData.getCarTypeCode(), contractData.getOrgId());
        } else {
            bmsContractPrice = new BmsContractPrice();
        }
        // 阶梯价格；计算方式一：分段累加
        if (BmsConstants.YES.equals(bmsContractPrice.getIsAccumulationMethod())) {
            // 本计算公式中是否含有FORMULA_PARAM_001
            boolean isContainsOutput = contractData.getFormulaParameters().stream().anyMatch(o -> BmsConstants.FORMULA_PARAM_001.equals(o.getParameterValue()));

            List<String> calcRelationIds = Lists.newArrayList();
            BigDecimal price = null;
            for (BmsContractSteppedPrice steppedPrice : bmsContractPrice.getSteppedPrices()) {
                if (steppedPrice.getTo() == null || steppedPrice.getTo().compareTo(businessData.getOutputValue()) > 0) {
                    if (businessData.getOutputValue().compareTo(steppedPrice.getFm()) < 0) {
                        break;
                    }
                    if (price == null) {
                        price = BigDecimal.ZERO;
                    }
                    BigDecimal v = isContainsOutput ? businessData.getOutputValue().subtract(steppedPrice.getFm()) : BigDecimal.ONE;
                    price = price.add(steppedPrice.getPrice().multiply(v));
                    calcRelationIds.add(steppedPrice.getId());
                    break;
                }
                if (price == null) {
                    price = BigDecimal.ZERO;
                }
                BigDecimal v = isContainsOutput ? steppedPrice.getTo().subtract(steppedPrice.getFm()) : BigDecimal.ONE;
                price = price.add(steppedPrice.getPrice().multiply(v));
                calcRelationIds.add(steppedPrice.getId());
            }
            return new BmsCalcProcessObj("002", price, price, calcRelationIds);
        }
        // 计算方式二：范围段直取
        for (BmsContractSteppedPrice steppedPrice : bmsContractPrice.getSteppedPrices()) {
            // 是否在范围内
            boolean isWithinRange = (steppedPrice.getTo() == null || steppedPrice.getTo().compareTo(businessData.getOutputValue()) > 0) && businessData.getOutputValue().compareTo(steppedPrice.getFm()) >= 0;
            if (isWithinRange) {
                return new BmsCalcProcessObj("002", steppedPrice.getPrice(), steppedPrice.getPrice(), Lists.newArrayList(steppedPrice.getId()));
            }
        }
        return new BmsCalcProcessObj("002", bmsContractPrice.getPrice(), bmsContractPrice.getPrice(), Lists.newArrayList(bmsContractPrice.getId()));
    }

    /**
     * 获取公式参数值 - 物流点数
     *
     * @param contractData 费用计算条件参数
     * @param businessData 费用计算业务参数
     * @return 物流点数
     */
    public BmsCalcProcessObj getFormulaParam003(BmsCalcContractData contractData, BmsCalcBusinessData businessData) {
        BmsContractPrice bmsContractPrice;
        if (BmsConstants.BILL_MODULE_01.equals(contractData.getBillModule())) {
            // 仓储
            bmsContractPrice = bmsContractStoragePriceService.getContractPrice(contractData.getContractDetailId(), businessData.getSkuClass(), businessData.getSkuCode(), contractData.getOrgId());
        } else if (BmsConstants.BILL_MODULE_02.equals(contractData.getBillModule())) {
            // 运输
            bmsContractPrice = bmsTransportPriceService.getContractPrice(contractData.getContractDetailId(), businessData.getStartPlaceCode(), businessData.getEndPlaceCode(), businessData.getRegionCode(), businessData.getCarTypeCode(), contractData.getOrgId());
        } else {
            bmsContractPrice = new BmsContractPrice();
        }
        BigDecimal calcValue = bmsContractPrice.getLogisticsPoints() == null ? BigDecimal.ZERO : bmsContractPrice.getLogisticsPoints();
        List<String> calcRelationIds = Lists.newArrayList(bmsContractPrice.getId());

        return new BmsCalcProcessObj("003", calcValue, bmsContractPrice.getLogisticsPoints(), calcRelationIds);
    }

    /**
     * 获取公式参数值 - 商品未税单价
     *
     * @param contractData 费用计算条件参数
     * @param businessData 费用计算业务参数
     * @return 商品未税单价
     */
    public BmsCalcProcessObj getFormulaParam004(BmsCalcContractData contractData, BmsCalcBusinessData businessData) {
        BmsContractSkuPrice bmsContractSkuPrice = bmsContractSkuPriceService.getContractPrice(contractData.getSysContractNo(), businessData.getSkuClass(), businessData.getSkuCode(), contractData.getOrgId());
        BigDecimal calcValue = bmsContractSkuPrice.getPrice() == null ? BigDecimal.ZERO : bmsContractSkuPrice.getPrice();
        List<String> calcRelationIds = Lists.newArrayList(bmsContractSkuPrice.getId());

        return new BmsCalcProcessObj("004", calcValue, bmsContractSkuPrice.getTaxPrice(), calcRelationIds);
    }

    /**
     * 获取公式参数值 - 商品含税单价
     *
     * @param contractData 费用计算条件参数
     * @param businessData 费用计算业务参数
     * @return 商品含税单价
     */
    public BmsCalcProcessObj getFormulaParam005(BmsCalcContractData contractData, BmsCalcBusinessData businessData) {
        BmsContractSkuPrice bmsContractSkuPrice = bmsContractSkuPriceService.getContractPrice(contractData.getSysContractNo(), businessData.getSkuClass(), businessData.getSkuCode(), contractData.getOrgId());
        BigDecimal calcValue = bmsContractSkuPrice.getTaxPrice() == null ? BigDecimal.ZERO : bmsContractSkuPrice.getTaxPrice();
        List<String> calcRelationIds = Lists.newArrayList(bmsContractSkuPrice.getId());

        return new BmsCalcProcessObj("005", calcValue, bmsContractSkuPrice.getTaxPrice(), calcRelationIds);
    }

    /**
     * 获取公式参数值 - 合同系数
     *
     * @param contractData 费用计算条件参数
     * @return 合同系数
     */
    public BmsCalcProcessObj getFormulaParam006(BmsCalcContractData contractData) {
        BigDecimal calcValue = contractData.getCoefficient() == null ? BigDecimal.ONE : contractData.getCoefficient();
        List<String> calcRelationIds = Lists.newArrayList(contractData.getContractDetailId());

        return new BmsCalcProcessObj("006", calcValue, contractData.getCoefficient(), calcRelationIds);
    }

    /**
     * 获取公式参数值 - 日历系数
     *
     * @param contractData 费用计算条件参数
     * @param businessData 费用计算业务参数
     * @return 日历系数
     */
    public BmsCalcProcessObj getFormulaParam007(BmsCalcContractData contractData, BmsCalcBusinessData businessData) {
        BmsCalendar calendar = bmsCalendarService.getByDate(businessData.getBusinessDate(), contractData.getOrgId());
        BigDecimal calcValue = calendar.getCoefficient() != null ? BigDecimal.ONE : calendar.getCoefficient();
        List<String> calcRelationIds = Lists.newArrayList(calendar.getId());

        return new BmsCalcProcessObj("007", calcValue, calendar.getCoefficient(), calcRelationIds);
    }

    /**
     * 获取公式参数值 - 税率
     *
     * @param contractData 费用计算条件参数
     * @return 税率
     */
    public BmsCalcProcessObj getFormulaParam008(BmsCalcContractData contractData) {
        BigDecimal calcValue = contractData.getTaxRate() == null ? BigDecimal.ONE : contractData.getTaxRate();
        List<String> calcRelationIds = Lists.newArrayList(contractData.getContractDetailId());

        return new BmsCalcProcessObj("008", calcValue, contractData.getCoefficient(), calcRelationIds);
    }
}
