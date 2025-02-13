package com.yunyou.modules.bms.calculate;

import com.yunyou.common.ResultMessage;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.yunyou.modules.bms.calculate.business.*;
import com.yunyou.modules.bms.calculate.contract.BmsCalcContractData;
import com.yunyou.modules.bms.calculate.contract.BmsGetContractDataService;
import com.yunyou.modules.bms.common.CalcMethod;
import com.yunyou.modules.bms.finance.entity.BmsChargeDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsChargeResultEntity;
import com.yunyou.modules.bms.finance.service.BmsChargeDetailService;
import com.yunyou.modules.bms.finance.service.BmsChargeResultService;
import com.yunyou.modules.bms.finance.service.BmsSettleModelService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BmsCalculateService extends BaseService {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BmsSettleModelService bmsSettleModelService;
    @Autowired
    private BmsContractService bmsContractService;
    @Autowired
    private BmsGetContractDataService bmsGetContractDataService;
    @Autowired
    private BmsInboundBusinessService bmsInboundBusinessService;
    @Autowired
    private BmsOutboundBusinessService bmsOutboundBusinessService;
    @Autowired
    private BmsInventoryBusinessService bmsInventoryBusinessService;
    @Autowired
    private BmsWaybillBusinessService bmsWaybillBusinessService;
    @Autowired
    private BmsDispatchBusinessService bmsDispatchBusinessService;
    @Autowired
    private BmsReturnBusinessService bmsReturnBusinessService;
    @Autowired
    private BmsExceptionBusinessService bmsExceptionBusinessService;
    @Autowired
    private BmsFixedBusinessService bmsFixedBusinessService;
    @Autowired
    private BmsChargeDetailService bmsChargeDetailService;
    @Autowired
    private BmsChargeResultService bmsChargeResultService;

    /**
     * 计算方式：模型结算、合同结算
     */
    private static final String C_M_MODEL = "1";
    private static final String C_M_CONTRACT = "2";

    /**
     * 费用计算按模型批量计算
     *
     * @param modelIds 模型ID
     * @param fmDate   结算日期从
     * @param toDate   结算日期到
     */
    public ResultMessage calcByModel(List<String> modelIds, Date fmDate, Date toDate) {
        ResultMessage message = new ResultMessage();

        // 结算批次号
        String settleLotNo = noService.getDocumentNo(GenNoType.BMS_BILL_NO.name());
        List<BmsChargeDetail> chargeDetails = Lists.newArrayList();
        for (String modelId : modelIds) {
            /*BmsSettleModel bmsSettleModel = bmsSettleModelService.get(modelId);
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
            }*/

            List<BmsCalcContractData> list = this.findContractData(fmDate, toDate, C_M_MODEL, modelId);
            for (BmsCalcContractData contractData : list) {
                try {
                    chargeDetails.addAll(this.calc(settleLotNo, contractData));
                } catch (Exception e) {
                    logger.error("费用计算异常", e);
                    message.setSuccess(false);
                    message.setMessage(e.getMessage());
                }
            }
        }
        chargeDetails.forEach(bmsChargeDetailService::save);
        // 将费用明细转化为费用结果
        List<BmsChargeResultEntity> chargeResults = BmsCalculateUtils.detailToResult(chargeDetails);
        chargeResults.forEach(bmsChargeResultService::save);
        return message;
    }

    /**
     * 费用计算按合同批量计算
     *
     * @param contactIds 合同ID
     * @param fmDate     结算日期从
     * @param toDate     结算日期到
     */
    public ResultMessage calcByContact(List<String> contactIds, Date fmDate, Date toDate) {
        ResultMessage message = new ResultMessage();

        // 结算批次号
        String settleLotNo = noService.getDocumentNo(GenNoType.BMS_BILL_NO.name());
        List<BmsChargeDetail> chargeDetails = Lists.newArrayList();
        for (String contactId : contactIds) {
            /*BmsContract bmsContract = bmsContractService.get(contactId);
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
            }*/

            List<BmsCalcContractData> list = this.findContractData(fmDate, toDate, C_M_CONTRACT, contactId);
            for (BmsCalcContractData contractData : list) {
                try {
                    chargeDetails.addAll(this.calc(settleLotNo, contractData));
                } catch (Exception e) {
                    logger.error("费用计算异常", e);
                    message.setSuccess(false);
                    message.setMessage(e.getMessage());
                }
            }
        }
        chargeDetails.forEach(bmsChargeDetailService::save);
        // 将费用明细转化为费用结果
        List<BmsChargeResultEntity> chargeResults = BmsCalculateUtils.detailToResult(chargeDetails);
        chargeResults.forEach(bmsChargeResultService::save);
        return message;
    }

    /**
     * 费用计算
     *
     * @param settleLotNo  结算批次号
     * @param contractData 合同数据
     * @return 费用结果
     */
    public List<BmsChargeDetail> calc(String settleLotNo, BmsCalcContractData contractData) {
        List<BmsCalcBusinessData> businessData = this.findBusinessData(contractData);
        List<BmsCalcData> calcData = this.findCalcData(settleLotNo, contractData, businessData);
        return BmsCalculateUtils.calc(calcData);
    }

    /**
     * 获取计算相关合同数据
     *
     * @param fmDate     结算日期从
     * @param toDate     结算日期到
     * @param calcMethod 计算方式
     * @param settleId   计算方式对应ID
     * @return 合同数据
     */
    private List<BmsCalcContractData> findContractData(Date fmDate, Date toDate, String calcMethod, String settleId) {
        switch (calcMethod) {
            case C_M_MODEL:
                return bmsGetContractDataService.findContractDataByModel(settleId, fmDate, toDate);
            case C_M_CONTRACT:
                return bmsGetContractDataService.findContractDataByContract(settleId, fmDate, toDate);
            default:
                return Lists.newArrayList();
        }
    }

    /**
     * 获取计算相关业务数据
     *
     * @param contractData 合同数据
     * @return 业务数据
     */
    private List<BmsCalcBusinessData> findBusinessData(BmsCalcContractData contractData) {
        String methodName = contractData.getMethodName();
        String outputObject = contractData.getOutputObjects();
        Date settleDateFm = contractData.getSettleDateFm();
        Date settleDateTo = contractData.getSettleDateTo();
        List<BmsCalcTermsParams> includeParams = contractData.getIncludeParams();
        List<BmsCalcTermsParams> excludeParams = contractData.getExcludeParams();
        String orgId = contractData.getOrgId();
        switch (CalcMethod.valueOf(methodName)) {
            case calcInbound:
                return bmsInboundBusinessService.findForCalcInbound(outputObject, includeParams, excludeParams, orgId);
            case calcOutbound:
                return bmsOutboundBusinessService.findForCalcOutbound(outputObject, includeParams, excludeParams, orgId);
            case calcInventory:
                return bmsInventoryBusinessService.findForCalcInventory(outputObject, includeParams, excludeParams, orgId);
            case calcWaybill:
                return bmsWaybillBusinessService.findForCalcOutbound(outputObject, includeParams, excludeParams, orgId);
            case calcDispatchOrder:
                return bmsDispatchBusinessService.findForCalcDispatchOrder(outputObject, includeParams, excludeParams, orgId);
            case calcDispatch:
                return bmsDispatchBusinessService.findForCalcDispatch(outputObject, includeParams, excludeParams, orgId);
            case calcDispatchForRegion:
                return bmsDispatchBusinessService.findForCalcDispatchRegion(outputObject, includeParams, excludeParams, orgId);
            case calcDispatchForQC:
                return bmsDispatchBusinessService.findForCalcDispatchQC(outputObject, includeParams, excludeParams, orgId);
            case calcReturn:
                return bmsReturnBusinessService.findForCalcReturn(outputObject, includeParams, excludeParams, orgId);
            case calcException:
                return bmsExceptionBusinessService.findForCalcException(outputObject, includeParams, excludeParams, orgId);
            case calcFixedDaily:
                return bmsFixedBusinessService.findForCalcFixed(outputObject, "Daily", settleDateFm, settleDateTo);
            case calcFixedMonthly:
                return bmsFixedBusinessService.findForCalcFixed(outputObject, "Monthly", settleDateFm, settleDateTo);
            case calcFixedDailyForBusinessOrg:
                return bmsFixedBusinessService.findForCalcFixed(outputObject, "Daily", settleDateFm, settleDateTo, includeParams, excludeParams);
            case calcFixedMonthlyForBusinessOrg:
                return bmsFixedBusinessService.findForCalcFixed(outputObject, "Monthly", settleDateFm, settleDateTo, includeParams, excludeParams);
            default:
                return Lists.newArrayList();
        }
    }

    /**
     * 将业务数据数转换为计算数据
     *
     * @param settleLotNo      结算批次号
     * @param contractData     合同数据
     * @param businessDataList 业务数据
     * @return 计算数据
     */
    private List<BmsCalcData> findCalcData(String settleLotNo, BmsCalcContractData contractData, List<BmsCalcBusinessData> businessDataList) {
        return businessDataList.stream().map(businessData -> {
            BmsCalcData bmsCalcData = new BmsCalcData();
            bmsCalcData.setSettleLotNo(settleLotNo);
            bmsCalcData.setContractData(contractData);
            bmsCalcData.setBusinessData(businessData);
            bmsCalcData.setFormulaParam001(bmsGetContractDataService.getFormulaParam001(contractData, businessData));
            bmsCalcData.setFormulaParam002(bmsGetContractDataService.getFormulaParam002(contractData, businessData));
            bmsCalcData.setFormulaParam003(bmsGetContractDataService.getFormulaParam003(contractData, businessData));
            bmsCalcData.setFormulaParam004(bmsGetContractDataService.getFormulaParam004(contractData, businessData));
            bmsCalcData.setFormulaParam005(bmsGetContractDataService.getFormulaParam005(contractData, businessData));
            bmsCalcData.setFormulaParam006(bmsGetContractDataService.getFormulaParam006(contractData));
            bmsCalcData.setFormulaParam007(bmsGetContractDataService.getFormulaParam007(contractData, businessData));
            bmsCalcData.setFormulaParam008(bmsGetContractDataService.getFormulaParam008(contractData));
            return bmsCalcData;
        }).collect(Collectors.toList());
    }
}
