package com.yunyou.modules.bms.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 描述：派车单配送点数据
 */
public class BmsDispatchOrderSiteData extends DataEntity<BmsDispatchOrderSiteData> {

    private static final long serialVersionUID = -3482234582586951526L;
    // 派车单ID
    private String headerId;
    // 派车单号
    private String orderNo;
    // 配送顺序
    private String dispatchSeq;
    // 网点编码
    private String outletCode;
    // 网点名称
    private String outletName;
    // 网点所属城市ID
    private String areaId;
    // 机构
    private String orgId;
    // 数据来源
    private String dataSources;

    // 门店派车单号
    private String storeDispatchNo;
    // 门店派车单号dsp
    private String storeDispatchNoDsp;
    // 托盘数
    private Double palletCount;
    // 签收状态 0未签收 1已签收
    private String signStatus;
    // 签收人
    private String signPerson;
    // 签收时间
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date signTime;
    // 地址
    private String address;

    public BmsDispatchOrderSiteData() {
    }

    public BmsDispatchOrderSiteData(String id) {
        super(id);
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDispatchSeq() {
        return dispatchSeq;
    }

    public void setDispatchSeq(String dispatchSeq) {
        this.dispatchSeq = dispatchSeq;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public String getStoreDispatchNo() {
        return storeDispatchNo;
    }

    public void setStoreDispatchNo(String storeDispatchNo) {
        this.storeDispatchNo = storeDispatchNo;
    }

    public String getStoreDispatchNoDsp() {
        return storeDispatchNoDsp;
    }

    public void setStoreDispatchNoDsp(String storeDispatchNoDsp) {
        this.storeDispatchNoDsp = storeDispatchNoDsp;
    }

    public Double getPalletCount() {
        return palletCount;
    }

    public void setPalletCount(Double palletCount) {
        this.palletCount = palletCount;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignPerson() {
        return signPerson;
    }

    public void setSignPerson(String signPerson) {
        this.signPerson = signPerson;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
