package com.yunyou.modules.bms.finance.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 描述：结算模型明细实体
 * <p>
 *
 * @author Jianhua
 * @version 2019-11-6
 */
public class BmsSettleModelDetail extends DataEntity<BmsSettleModelDetail> {

    private static final long serialVersionUID = 1L;
    // 模型编码
    private String settleModelCode;
    // 合同明细ID
    private String contractCostItemId;
    // 机构ID
    private String orgId;

    public BmsSettleModelDetail() {
        super();
    }

    public BmsSettleModelDetail(String id) {
        super(id);
    }

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
    }

    public String getContractCostItemId() {
        return contractCostItemId;
    }

    public void setContractCostItemId(String contractCostItemId) {
        this.contractCostItemId = contractCostItemId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}