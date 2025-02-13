package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 标签履历
 *
 * @author 刘剑华
 * @since 2023/11/16 13:54
 */
public class TmLabelLog extends DataEntity<TmLabelLog> {

    // 操作时间
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date opTime;
    // 操作类型
    private String opType;
    // 标签号
    private String labelNo;
    // 运输单号
    private String transportNo;
    // 行号
    private String lineNo;
    // 派车单号
    private String dispatchNo;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;
    // 当前网点编码
    private String nowOutletCode;
    // 上一站网点编码
    private String preOutletCode;
    // 下一站网点编码
    private String nextOutletCode;
    // 数量
    private BigDecimal qty;
    // 重量
    private BigDecimal weight;
    // 体积
    private BigDecimal volume;
    // 组织中心机构ID
    private String baseOrgId;
    // 机构ID
    private String orgId;

    public TmLabelLog() {
    }

    public TmLabelLog(String id) {
        super(id);
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getNowOutletCode() {
        return nowOutletCode;
    }

    public void setNowOutletCode(String nowOutletCode) {
        this.nowOutletCode = nowOutletCode;
    }

    public String getPreOutletCode() {
        return preOutletCode;
    }

    public void setPreOutletCode(String preOutletCode) {
        this.preOutletCode = preOutletCode;
    }

    public String getNextOutletCode() {
        return nextOutletCode;
    }

    public void setNextOutletCode(String nextOutletCode) {
        this.nextOutletCode = nextOutletCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
