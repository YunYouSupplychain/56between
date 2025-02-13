package com.yunyou.modules.bms.finance.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.bms.finance.entity.BmsSettleModel;

import java.util.Date;
import java.util.List;

/**
 * 描述：结算模型扩展实体
 *
 * @author Jianhua
 * @version 2019/6/13
 */
public class BmsSettleModelEntity extends BmsSettleModel {

    private static final long serialVersionUID = 284520427900430936L;
    // 模型明细
    private List<BmsSettleModelDetailEntity> detailList = Lists.newArrayList();
    // 结算对象名称
    private String settleObjectName;
    // 机构名称
    private String orgName;
    // 结算时间从
    private Date fmDate;
    // 结算时间到
    private Date toDate;

    public List<BmsSettleModelDetailEntity> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<BmsSettleModelDetailEntity> detailList) {
        this.detailList = detailList;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getFmDate() {
        return fmDate;
    }

    public void setFmDate(Date fmDate) {
        this.fmDate = fmDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
