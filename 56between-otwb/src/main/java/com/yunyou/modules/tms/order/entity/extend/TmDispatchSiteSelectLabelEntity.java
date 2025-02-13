package com.yunyou.modules.tms.order.entity.extend;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.DataEntity;

public class TmDispatchSiteSelectLabelEntity extends DataEntity<TmDispatchSiteSelectLabelEntity> {
    private static final long serialVersionUID = -5474558166829864797L;

    private String receiveShip;     // 提货(R) or 送货(S)
    private String dispatchSiteOutletCode;// 配送网点编码
    private String dispatchNo;      // 派车单号
    private String transportNo;     // 运输单号
    private String customerNo;      // 客户单号
    private String labelNo;         // 标签号
    private String principalCode;   // 委托方编码
    private String principalName;   // 委托方名称
    private String customerCode;    // 客户编码
    private String customerName;    // 客户名称
    private String skuCode;         // 商品编码
    private String skuName;         // 商品名称
    private String nowOutletCode;   // 货物当前所在网点编码
    private String nowOutletName;   // 货物当前所在网点名称
    private String nowCityId;       // 货物当前所在网点城市ID
    private String nowCity;         // 货物当前所在网点城市
    private String nextOutletCode;  // 货物下一站送往网点编码
    private String nextOutletName;  // 货物下一站送往网点名称
    private String nextCityId;      // 货物下一站送往网点城市ID
    private String nextCity;        // 货物下一站送往网点城市
    private String baseOrgId;       // 基础数据机构ID
    private String orgId;           // 机构ID
    private Double qty;             // 数量
    private Double weight;          // 重量
    private Double cubic;           // 体积

    /*查询条件*/
    private String dataType;            // 配载页签
    private String rcvOutletCodes;      // 派车单提货点
    private List<String> rcvOutletCodeList = Lists.newArrayList();
    private Date transportOrderTimeFm;  // 运输订单时间从
    private Date transportOrderTimeTo;  // 运输订单时间到
    private String preAllocDispatchNo;  // 预配载派车单号
    private String carrierCode;         // 承运商编码
    private String consigneeCode;   // 收货方编码
    private String orderType;       // 运输订单类型

    public TmDispatchSiteSelectLabelEntity() {
        super();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public Date getTransportOrderTimeFm() {
        return transportOrderTimeFm;
    }

    public void setTransportOrderTimeFm(Date transportOrderTimeFm) {
        this.transportOrderTimeFm = transportOrderTimeFm;
    }

    public Date getTransportOrderTimeTo() {
        return transportOrderTimeTo;
    }

    public void setTransportOrderTimeTo(Date transportOrderTimeTo) {
        this.transportOrderTimeTo = transportOrderTimeTo;
    }

    public String getPreAllocDispatchNo() {
        return preAllocDispatchNo;
    }

    public void setPreAllocDispatchNo(String preAllocDispatchNo) {
        this.preAllocDispatchNo = preAllocDispatchNo;
    }

    public String getPrincipalCode() {
        return principalCode;
    }

    public void setPrincipalCode(String principalCode) {
        this.principalCode = principalCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getNowCityId() {
        return nowCityId;
    }

    public void setNowCityId(String nowCityId) {
        this.nowCityId = nowCityId;
    }

    public String getNowCity() {
        return nowCity;
    }

    public void setNowCity(String nowCity) {
        this.nowCity = nowCity;
    }

    public String getNextCityId() {
        return nextCityId;
    }

    public void setNextCityId(String nextCityId) {
        this.nextCityId = nextCityId;
    }

    public String getNextCity() {
        return nextCity;
    }

    public void setNextCity(String nextCity) {
        this.nextCity = nextCity;
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

    public String getNowOutletCode() {
        return nowOutletCode;
    }

    public void setNowOutletCode(String nowOutletCode) {
        this.nowOutletCode = nowOutletCode;
    }

    public String getNowOutletName() {
        return nowOutletName;
    }

    public void setNowOutletName(String nowOutletName) {
        this.nowOutletName = nowOutletName;
    }

    public String getNextOutletCode() {
        return nextOutletCode;
    }

    public void setNextOutletCode(String nextOutletCode) {
        this.nextOutletCode = nextOutletCode;
    }

    public String getNextOutletName() {
        return nextOutletName;
    }

    public void setNextOutletName(String nextOutletName) {
        this.nextOutletName = nextOutletName;
    }

    public String getRcvOutletCodes() {
        return rcvOutletCodes;
    }

    public void setRcvOutletCodes(String rcvOutletCodes) {
        this.rcvOutletCodes = rcvOutletCodes;
        if (StringUtils.isNotBlank(rcvOutletCodes)) {
            if (rcvOutletCodeList == null) {
                rcvOutletCodeList = Lists.newArrayList();
            }
            rcvOutletCodeList.addAll(Arrays.asList(rcvOutletCodes.split(",")));
        }
    }

    public List<String> getRcvOutletCodeList() {
        return rcvOutletCodeList;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getDispatchSiteOutletCode() {
        return dispatchSiteOutletCode;
    }

    public void setDispatchSiteOutletCode(String dispatchSiteOutletCode) {
        this.dispatchSiteOutletCode = dispatchSiteOutletCode;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
