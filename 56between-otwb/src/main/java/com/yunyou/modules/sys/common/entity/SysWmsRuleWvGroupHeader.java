package com.yunyou.modules.sys.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 波次规则组Entity
 */
public class SysWmsRuleWvGroupHeader extends DataEntity<SysWmsRuleWvGroupHeader> {

    private static final long serialVersionUID = 1L;
    private String groupCode;// 规则组编码
    private String groupName;// 规则组名称
    @JsonFormat(pattern = "HH:mm:ss")
    private Date orderDateFm;// 订单时间从
    @JsonFormat(pattern = "HH:mm:ss")
    private Date orderDateTo;// 订单时间到
    private String ownerCode;// 货主编码
    private String skuCode;// 商品编码
    private String ownerName;// 货主名称
    private String skuName;// 商品名称
    private String addrArea;// 三级地址
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称
    private List<SysWmsRuleWvGroupDetail> wvGroupDetailList = Lists.newArrayList();

    public SysWmsRuleWvGroupHeader() {
        super();
    }

    public SysWmsRuleWvGroupHeader(String id) {
        super(id);
    }

    public SysWmsRuleWvGroupHeader(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getOrderDateFm() {
        return orderDateFm;
    }

    public void setOrderDateFm(Date orderDateFm) {
        this.orderDateFm = orderDateFm;
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getAddrArea() {
        return addrArea;
    }

    public void setAddrArea(String addrArea) {
        this.addrArea = addrArea;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public List<SysWmsRuleWvGroupDetail> getWvGroupDetailList() {
        return wvGroupDetailList;
    }

    public void setWvGroupDetailList(List<SysWmsRuleWvGroupDetail> wvGroupDetailList) {
        this.wvGroupDetailList = wvGroupDetailList;
    }
}