package com.yunyou.modules.bms.finance.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 结算模型Entity
 *
 * @author Jianhua Liu
 * @version 2019-06-13
 */
public class BmsSettleModel extends DataEntity<BmsSettleModel> {

    private static final long serialVersionUID = 1L;
    // 模型编码
    private String settleModelCode;
    // 结算对象编码
    private String settleObjectCode;
    // 负责人
    private String responsiblePerson;
    // 机构机构
    private String orgId;

    public BmsSettleModel() {
        super();
    }

    public BmsSettleModel(String id) {
        super(id);
    }

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}