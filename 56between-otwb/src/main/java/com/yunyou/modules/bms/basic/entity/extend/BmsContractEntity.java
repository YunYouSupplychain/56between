package com.yunyou.modules.bms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;

import java.util.Date;
import java.util.List;

/**
 * 描述：合同扩展扩展实体
 *
 * @author Jianhua
 * @version 2019/5/28
 */
public class BmsContractEntity extends BmsContract {

    private static final long serialVersionUID = 8179010187109916939L;
    private String settleObjectName;// 结算对象名称
    private String orgName;// 机构名称
    private List<BmsContractCostItemEntity> costItems = Lists.newArrayList();
    private List<BmsContractSkuPrice> skuPriceList = Lists.newArrayList();
    // 结算时间从
    private Date fmDate;
    // 结算时间到
    private Date toDate;

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

    public List<BmsContractCostItemEntity> getCostItems() {
        return costItems;
    }

    public void setCostItems(List<BmsContractCostItemEntity> costItems) {
        this.costItems = costItems;
    }

    public List<BmsContractSkuPrice> getSkuPriceList() {
        return skuPriceList;
    }

    public void setSkuPriceList(List<BmsContractSkuPrice> skuPriceList) {
        this.skuPriceList = skuPriceList;
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
