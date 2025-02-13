package com.yunyou.modules.tms.order.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 派车单标签Entity
 *
 * @author liujianhua
 * @version 2020-03-11
 */
public class TmDispatchOrderLabel extends DataEntity<TmDispatchOrderLabel> {

    private static final long serialVersionUID = 1L;
    private String labelNo;     // 标签号
    private String dispatchNo;  // 派车单号
    private String transportNo; // 运输订单号
    private String customerNo;  // 客户订单号
    private String receiveShip; // 提货(R) or 送货(S)
    private String orgId;       // 机构ID
    private String baseOrgId;   // 基础数据机构ID
    private String dispatchSiteOutletCode; // 配送网点编码
    private String status;  // 状态
    private String cabin;   // 舱室
    private String ownerCode;   // 货主编码
    private String skuCode;     // 商品编码
    private Double qty;     // 数量
    private Double weight;  // 重量
    private Double cubic;   // 体积
    private String storeDispatchNo; // 门店派车单号

    private String def1; // 栈板号
    private String def2; // 框号
    private String def3;
    private String def4;
    private String def5;
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;

    public TmDispatchOrderLabel() {
        super();
    }

    public TmDispatchOrderLabel(String id) {
        super(id);
    }

    public TmDispatchOrderLabel(String dispatchNo, String orgId) {
        this.dispatchNo = dispatchNo;
        this.orgId = orgId;
    }

    public TmDispatchOrderLabel(String dispatchNo, String labelNo, String orgId) {
        this.dispatchNo = dispatchNo;
        this.labelNo = labelNo;
        this.orgId = orgId;
    }

    @ExcelField(title = "标签号", align = 2, sort = 7)
    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    @ExcelField(title = "派车单号", align = 2, sort = 8)
    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    @ExcelField(title = "运输订单号", align = 2, sort = 9)
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    @ExcelField(title = "客户订单号", align = 2, sort = 10)
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @ExcelField(title = "提货/送货", dictType = "", align = 2, sort = 11)
    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 12)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getDispatchSiteOutletCode() {
        return dispatchSiteOutletCode;
    }

    public void setDispatchSiteOutletCode(String dispatchSiteOutletCode) {
        this.dispatchSiteOutletCode = dispatchSiteOutletCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
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

    public String getStoreDispatchNo() {
        return storeDispatchNo;
    }

    public void setStoreDispatchNo(String storeDispatchNo) {
        this.storeDispatchNo = storeDispatchNo;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }
}