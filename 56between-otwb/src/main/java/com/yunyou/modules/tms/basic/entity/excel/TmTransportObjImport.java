package com.yunyou.modules.tms.basic.entity.excel;

import java.io.Serializable;

import com.yunyou.common.utils.excel.annotation.ExcelField;

public class TmTransportObjImport implements Serializable {
    private static final long serialVersionUID = 8772349650527752220L;

    @ExcelField(title = "业务对象编码", sort = 1)
    private String transportObjCode;
    @ExcelField(title = "业务对象名称", sort = 2)
    private String transportObjName;
    @ExcelField(title = "业务对象简称", sort = 3)
    private String transportObjShortName;
    @ExcelField(title = "业务对象类型", sort = 4)
    private String transportObjType;
    @ExcelField(title = "联系人", sort = 5)
    private String contact;
    @ExcelField(title = "手机", sort = 6)
    private String phone;
    @ExcelField(title = "电话", sort = 7)
    private String tel;
    @ExcelField(title = "传真", sort = 8)
    private String fax;
    @ExcelField(title = "电子邮箱", sort = 9)
    private String email;
    @ExcelField(title = "网址", sort = 10)
    private String url;
    @ExcelField(title = "所属城市", sort = 11)
    private String areaId;
    @ExcelField(title = "详细地址", sort = 12)
    private String address;
    @ExcelField(title = "统一码", sort = 13)
    private String unCode;
    @ExcelField(title = "指定签收人", sort = 14)
    private String signBy;
    @ExcelField(title = "品牌", sort = 15)
    private String brand;
    @ExcelField(title = "分类", sort = 16)
    private String classification;
    @ExcelField(title = "业务路线编码", sort = 17)
    private String routeId;
    @ExcelField(title = "维修工时单价", sort = 18)
    private Double repairPrice;
    @ExcelField(title = "承运商对应机构编码", sort = 19)
    private String carrierMatchedOrgCode;
    @ExcelField(title = "网点对应机构编码", sort = 20)
    private String outletMatchedOrgCode;
    @ExcelField(title = "机构编码", sort = 21)
    private String orgCode;

    public String getTransportObjCode() {
        return transportObjCode;
    }

    public void setTransportObjCode(String transportObjCode) {
        this.transportObjCode = transportObjCode;
    }

    public String getTransportObjName() {
        return transportObjName;
    }

    public void setTransportObjName(String transportObjName) {
        this.transportObjName = transportObjName;
    }

    public String getTransportObjShortName() {
        return transportObjShortName;
    }

    public void setTransportObjShortName(String transportObjShortName) {
        this.transportObjShortName = transportObjShortName;
    }

    public String getTransportObjType() {
        return transportObjType;
    }

    public void setTransportObjType(String transportObjType) {
        this.transportObjType = transportObjType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnCode() {
        return unCode;
    }

    public void setUnCode(String unCode) {
        this.unCode = unCode;
    }

    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Double getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(Double repairPrice) {
        this.repairPrice = repairPrice;
    }

    public String getCarrierMatchedOrgCode() {
        return carrierMatchedOrgCode;
    }

    public void setCarrierMatchedOrgCode(String carrierMatchedOrgCode) {
        this.carrierMatchedOrgCode = carrierMatchedOrgCode;
    }

    public String getOutletMatchedOrgCode() {
        return outletMatchedOrgCode;
    }

    public void setOutletMatchedOrgCode(String outletMatchedOrgCode) {
        this.outletMatchedOrgCode = outletMatchedOrgCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
