package com.yunyou.modules.bms.finance.service.calc;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.common.CalcMethod;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.BmsSettleModel;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcBusinessParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcConditionParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalculateEntity;
import com.yunyou.modules.bms.finance.service.BmsBillDetailService;
import com.yunyou.modules.bms.finance.service.BmsSettleModelService;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：费用计算
 */
@Service
public class BmsCalculatorService {

    private final Logger logger = LoggerFactory.getLogger(BmsCalculatorService.class);
    @Autowired
    private BmsSettleModelService bmsSettleModelService;
    @Autowired
    private BmsContractService bmsContractService;
    @Autowired
    private BmsPrepareServiceParamService bmsPrepareParamService;
    @Autowired
    private BmsCalculator bmsCalculator;
    @Autowired
    private BmsBillDetailService bmsBillDetailService;

    /**
     * 费用计算按模型批量计算
     *
     * @param modelIds 模型ID
     * @param fmDate   结算日期从
     * @param toDate   结算日期到
     */
    public ResultMessage batchCalcByModel(List<String> modelIds, Date fmDate, Date toDate) {
        ResultMessage message = new ResultMessage();

        List<BmsBillDetail> rsList = Lists.newArrayList();
        for (String modelId : modelIds) {
            BmsSettleModel bmsSettleModel = bmsSettleModelService.get(modelId);
            // BMS参数：计算费用时是否先删除其“新建状态”历史费用记录(Y：是，N：否)
            final String DELETE_HISTORY_NEW_BILL_RECORD = SysControlParamsUtils.getValue(SysParamConstants.DELETE_HISTORY_NEW_BILL_RECORD, bmsSettleModel.getOrgId());
            if (BmsConstants.YES.equals(DELETE_HISTORY_NEW_BILL_RECORD)) {
                try {
                    bmsBillDetailService.deleteHistoryFeeByModel(bmsSettleModel.getSettleModelCode(), fmDate, toDate, bmsSettleModel.getOrgId());
                } catch (BmsException e) {
                    message.setSuccess(false);
                    message.addMessage("结算模型[" + bmsSettleModel.getSettleModelCode() + "]在此日期范围内存在已生成账单的费用，不能再次计算！<br>");
                    continue;
                }
            }

            List<BmsCalcConditionParams> list = bmsPrepareParamService.getConditionParams("1", modelId, fmDate, toDate);
            for (BmsCalcConditionParams o : list) {
                try {
                    rsList.addAll(this.calc(o));
                } catch (Exception e) {
                    logger.error("费用计算异常", e);
                    message.setSuccess(false);
                    message.setMessage(e.getMessage());
                }
            }
        }
        bmsBillDetailService.saveCalculateResult(rsList);
        return message;
    }

    /**
     * 费用计算按合同批量计算
     *
     * @param contactIds 合同ID
     * @param fmDate     结算日期从
     * @param toDate     结算日期到
     */
    public ResultMessage batchCalcByContact(List<String> contactIds, Date fmDate, Date toDate) {
        ResultMessage message = new ResultMessage();

        List<BmsBillDetail> rsList = Lists.newArrayList();
        for (String contactId : contactIds) {
            BmsContract bmsContract = bmsContractService.get(contactId);
            // BMS参数：计算费用时是否先删除其“新建状态”历史费用记录(Y：是，N：否)
            final String DELETE_HISTORY_NEW_BILL_RECORD = SysControlParamsUtils.getValue(SysParamConstants.DELETE_HISTORY_NEW_BILL_RECORD, bmsContract.getOrgId());
            if (BmsConstants.YES.equals(DELETE_HISTORY_NEW_BILL_RECORD)) {
                try {
                    bmsBillDetailService.deleteHistoryFeeByContact(bmsContract.getSysContractNo(), fmDate, toDate, bmsContract.getOrgId());
                } catch (BmsException e) {
                    message.setSuccess(false);
                    message.addMessage("合同[" + bmsContract.getSysContractNo() + "]在此日期范围内存在已生成账单的费用，不能再次计算！<br>");
                    continue;
                }
            }

            List<BmsCalcConditionParams> list = bmsPrepareParamService.getConditionParams("2", contactId, fmDate, toDate);
            for (BmsCalcConditionParams o : list) {
                try {
                    rsList.addAll(this.calc(o));
                } catch (Exception e) {
                    logger.error("费用计算异常", e);
                    message.setSuccess(false);
                    message.setMessage(e.getMessage());
                }
            }
        }
        bmsBillDetailService.saveCalculateResult(rsList);
        return message;
    }

    /**
     * 费用计算
     *
     * @param entity 费用计算条件参数
     * @return 费用结果
     */
    public List<BmsBillDetail> calc(BmsCalcConditionParams entity) {
        switch (CalcMethod.valueOf(entity.getMethodName())) {
            /*标准逻辑*/
            case calcInbound:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromInbound(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcOutbound:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromOutbound(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcInventory:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromInventory(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcDispatchOrder:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromDispatchOrder(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcDispatch:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromDispatch(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcReturn:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromReturn(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcException:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromException(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcWaybill:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromWaybill(entity, bmsPrepareParamService.getBusinessData(entity.getMethodName(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcFixedDaily:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromFixed(entity, "Daily"));
            case calcFixedMonthly:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromFixed(entity, "Monthly"));
            /*定制逻辑*/
            case calcDispatchForRegion:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromDispatchForRegion(entity, bmsPrepareParamService.getBusinessData(CalcMethod.calcDispatch.name(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcDispatchForQC:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromDispatchForQC(entity, bmsPrepareParamService.getBusinessData(CalcMethod.calcDispatch.name(), entity.getIncludeParams(), entity.getExcludeParams())));
            case calcFixedDailyForBusinessOrg:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromFixedForBusinessOrg(entity, "Daily"));
            case calcFixedMonthlyForBusinessOrg:
                return calc(entity, bmsPrepareParamService.getGroupBizParamsFromFixedForBusinessOrg(entity, "Monthly"));
            default:
                break;
        }
        return Lists.newArrayList();
    }

    /**
     * 费用计算
     *
     * @param entity 费用计算条件参数
     * @param data   费用计算业务参数
     * @return 费用结果
     */
    private List<BmsBillDetail> calc(BmsCalcConditionParams entity, List<BmsCalcBusinessParams> data) {
        return data.stream().map(o -> getCalculateResult(entity, o)).collect(Collectors.toList());
    }

    /**
     * 将数据转换为费用结果输出
     *
     * @param entity          费用计算条件参数
     * @param businessParams  费用计算业务参数
     * @param calculateEntity 费用计算实体
     * @return 费用结果
     */
    private BmsBillDetail convertResult(BmsCalcConditionParams entity, BmsCalcBusinessParams businessParams, BmsCalculateEntity calculateEntity) {
        BmsBillDetail result = new BmsBillDetail();
        result.setStatus(BmsConstants.BILL_STATUS_01);
        result.setDateFm(entity.getSettleDateFm());
        result.setDateTo(entity.getSettleDateTo());
        result.setSettleModelCode(entity.getSettleModelCode());
        result.setSettleObjectCode(entity.getSettleObjectCode());
        result.setSettleObjectName(entity.getSettleObjectName());
        result.setSysContractNo(entity.getSysContractNo());
        result.setContractNo(entity.getContractNo());
        result.setSubcontractNo(entity.getSubContractNo());
        result.setBillModule(entity.getBillModule());
        result.setBillSubjectCode(entity.getBillSubjectCode());
        result.setBillSubjectName(entity.getBillSubjectName());
        result.setBillCategory(entity.getBillCategory());
        result.setBillTermsCode(entity.getBillTermsCode());
        result.setBillTermsDesc(entity.getBillTermsDesc());
        result.setReceivablePayable(entity.getReceivablePayable());
        result.setFormulaName(entity.getFormulaName());

        result.setWarehouseCode(businessParams.getOrgCode());
        result.setWarehouseName(businessParams.getOrgName());
        result.setBusinessDate(businessParams.getBusinessDate());
        result.setSysOrderNo(businessParams.getOrderNo());
        result.setCustomerOrderNo(businessParams.getCustomerNo());
        result.setSupplierCode(businessParams.getSupplierCode());
        result.setSupplierName(businessParams.getSupplierName());
        result.setOwnerCode(businessParams.getOwnerCode());
        result.setOwnerName(businessParams.getOwnerName());
        result.setSkuCode(businessParams.getSkuCode());
        result.setSkuName(businessParams.getSkuName());
        result.setQuarantineType(businessParams.getQuarantineType());
        result.setConsigneeCode(businessParams.getConsigneeCode());
        result.setConsigneeName(businessParams.getConsigneeName());
        result.setCarrierCode(businessParams.getCarrierCode());
        result.setCarrierName(businessParams.getCarrierName());
        result.setCarType(businessParams.getCarTypeCode());
        result.setCarNo(businessParams.getVehicleNo());
        result.setRoute((StringUtils.isNotBlank(businessParams.getStartPlaceName()) ? businessParams.getStartPlaceName() : "")
                + " / " + (StringUtils.isNotBlank(businessParams.getEndPlaceName()) ? businessParams.getEndPlaceName() : ""));

        result.setTaxPrice(calculateEntity.getSkuTaxPrice());
        result.setPrice(calculateEntity.getSkuExcludeTaxPrice());
        result.setContractPrice(calculateEntity.getContractPrice());
        result.setLogisticsPoints(calculateEntity.getLogisticsPoints());
        result.setFormulaParamsDesc(calculateEntity.getCalcProcessDataDesc());
        result.setRemarks(calculateEntity.getMissingDataDesc());

        result.setOccurrenceQty(businessParams.getOccurrenceQty());
        result.setBillQty(calculateEntity.getQty());
        result.setBillStandard(calculateEntity.getPrice());
        result.setCost(calculateEntity.getResult());
        result.setOrgId(entity.getOrgId());
        return result;
    }

    /**
     * 得出最终费用结果
     *
     * @param entity         费用计算条件参数
     * @param businessParams 费用计算业务参数
     * @return 费用结果
     */
    private BmsBillDetail getCalculateResult(BmsCalcConditionParams entity, BmsCalcBusinessParams businessParams) {
        return convertResult(entity, businessParams, bmsCalculator.calcResult(entity, businessParams));
    }

}