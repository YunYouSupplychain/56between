package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 描述：阶梯价格
 *
 * @author liujianhua
 * @version 2019-11-14
 */
public class BmsContractStorageSteppedPrice extends DataEntity<BmsContractStorageSteppedPrice> {

    private static final long serialVersionUID = 1L;
    // 主表外键(仓储价格ID)
    private String fkId;
    // 从
    private BigDecimal fm;
    // 到
    private BigDecimal to;
    // 单价
    private BigDecimal price;
    // 机构ID
    private String orgId;

    public BmsContractStorageSteppedPrice() {
        super();
    }

    public BmsContractStorageSteppedPrice(String id) {
        super(id);
    }

    public String getFkId() {
        return fkId;
    }

    public void setFkId(String fkId) {
        this.fkId = fkId;
    }

    public BigDecimal getFm() {
        return fm;
    }

    public void setFm(BigDecimal fm) {
        this.fm = fm;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}