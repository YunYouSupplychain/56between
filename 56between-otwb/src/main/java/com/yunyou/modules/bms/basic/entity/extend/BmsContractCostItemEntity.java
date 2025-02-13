package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsContractCostItem;
import com.yunyou.modules.bms.basic.entity.BmsContractStoragePrice;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 描述：合同费用明细扩展实体
 *
 * @author Jianhua
 * @version 2019/7/3
 */
public class BmsContractCostItemEntity extends BmsContractCostItem {

    private static final long serialVersionUID = -2009915060314764056L;
    /**
     * 结算对象编码
     */
    private String settleObjectCode;
    /**
     * 费用科目名称
     */
    private String billSubjectName;
    /**
     * 原始费用科目编码
     */
    private String originalBillSubjectCode;
    /**
     * 计费条款说明
     */
    private String billTermsDesc;
    /**
     * 计费公式名称
     */
    private String formulaName;
    /**
     * 运输价格体系名称
     */
    private String transportGroupName;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 仓储价格
     */
    private List<BmsContractStoragePrice> storagePrices = Lists.newArrayList();
    /**
     * 明细条款包含参数
     */
    private List<BmsContractDetailTermsParamsEntity> includeParams;
    /**
     * 明细排除参数
     */
    private List<BmsContractDetailTermsParamsEntity> excludeParams;

    public BmsContractCostItemEntity() {
    }

    public BmsContractCostItemEntity(String id) {
        super(id);
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public String getOriginalBillSubjectCode() {
        return originalBillSubjectCode;
    }

    public void setOriginalBillSubjectCode(String originalBillSubjectCode) {
        this.originalBillSubjectCode = originalBillSubjectCode;
    }

    public String getBillTermsDesc() {
        return billTermsDesc;
    }

    public void setBillTermsDesc(String billTermsDesc) {
        this.billTermsDesc = billTermsDesc;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getTransportGroupName() {
        return transportGroupName;
    }

    public void setTransportGroupName(String transportGroupName) {
        this.transportGroupName = transportGroupName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<BmsContractStoragePrice> getStoragePrices() {
        return storagePrices;
    }

    public void setStoragePrices(List<BmsContractStoragePrice> storagePrices) {
        this.storagePrices = storagePrices;
    }

    public List<BmsContractDetailTermsParamsEntity> getIncludeParams() {
        return includeParams;
    }

    public void setIncludeParams(List<BmsContractDetailTermsParamsEntity> includeParams) {
        this.includeParams = includeParams;
    }

    public List<BmsContractDetailTermsParamsEntity> getExcludeParams() {
        return excludeParams;
    }

    public void setExcludeParams(List<BmsContractDetailTermsParamsEntity> excludeParams) {
        this.excludeParams = excludeParams;
    }
}
