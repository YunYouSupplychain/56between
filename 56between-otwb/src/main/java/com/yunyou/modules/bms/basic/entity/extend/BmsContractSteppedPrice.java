package com.yunyou.modules.bms.basic.entity.extend;

import java.math.BigDecimal;

/**
 * 合同阶梯价格
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsContractSteppedPrice {

    /**
     * 主键
     */
    private String id;
    /**
     * 主表外键
     */
    private String fkId;
    /**
     * 从
     */
    private BigDecimal fm;
    /**
     * 到（不含当前值）
     */
    private BigDecimal to;
    /**
     * 单价
     */
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
