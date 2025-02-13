package com.yunyou.modules.oms.basic.entity;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @auther: Jianhua on 2019/5/6
 */
public class OmItemPriceEntity extends OmItemPrice {
    private static final long serialVersionUID = 2445683278772260113L;

    private String ownerName;
    private String customerName;    // 客户编码
    private String skuName;		    // 商品名称
    private String spec;		    // 规格

    private Date beginEffectiveTime;		// 开始 生效时间
    private Date endEffectiveTime;		// 结束 生效时间
    private Date beginExpirationTime;		// 开始 失效时间
    private Date endExpirationTime;		// 结束 失效时间

    private List<String> noSkuList = Lists.newArrayList();

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Date getBeginEffectiveTime() {
        return beginEffectiveTime;
    }

    public void setBeginEffectiveTime(Date beginEffectiveTime) {
        this.beginEffectiveTime = beginEffectiveTime;
    }

    public Date getEndEffectiveTime() {
        return endEffectiveTime;
    }

    public void setEndEffectiveTime(Date endEffectiveTime) {
        this.endEffectiveTime = endEffectiveTime;
    }

    public Date getBeginExpirationTime() {
        return beginExpirationTime;
    }

    public void setBeginExpirationTime(Date beginExpirationTime) {
        this.beginExpirationTime = beginExpirationTime;
    }

    public Date getEndExpirationTime() {
        return endExpirationTime;
    }

    public void setEndExpirationTime(Date endExpirationTime) {
        this.endExpirationTime = endExpirationTime;
    }

    public List<String> getNoSkuList() {
        return noSkuList;
    }

    public void setNoSkuList(List<String> noSkuList) {
        this.noSkuList = noSkuList;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
