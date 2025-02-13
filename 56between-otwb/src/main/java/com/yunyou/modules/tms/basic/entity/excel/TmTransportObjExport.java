package com.yunyou.modules.tms.basic.entity.excel;

import java.io.Serializable;

import com.yunyou.common.utils.excel.annotation.ExcelField;

public class TmTransportObjExport implements Serializable {
    private static final long serialVersionUID = -2525726270113143424L;

    private String areaId;

    @ExcelField(title = "业务对象编码", type = 1, sort = 1)
    private String transportObjCode;
    @ExcelField(title = "业务对象名称", type = 1, sort = 2)
    private String transportObjName;
    @ExcelField(title = "业务对象简称", type = 1, sort = 2)
    private String transportObjShortName;
    @ExcelField(title = "业务对象类型", dictType = "TMS_TRANSPORT_OBJ_TYPE", type = 1, sort = 2)
    private String transportObjType;
    @ExcelField(title = "联系人", type = 1, sort = 2)
    private String contact;
    @ExcelField(title = "手机", type = 1, sort = 2)
    private String phone;
    @ExcelField(title = "电话", type = 1, sort = 2)
    private String tel;
    @ExcelField(title = "传真", type = 1, sort = 2)
    private String fax;
    @ExcelField(title = "电子邮箱", type = 1, sort = 2)
    private String email;
    @ExcelField(title = "网址", type = 1, sort = 2)
    private String url;
    @ExcelField(title = "所属城市", type = 1, sort = 2)
    private String area;
    @ExcelField(title = "详细地址", type = 1, sort = 2)
    private String address;
    @ExcelField(title = "统一码", type = 1, sort = 2)
    private String unCode;
    @ExcelField(title = "指定签收人", type = 1, sort = 2)
    private String signBy;
    @ExcelField(title = "品牌", dictType = "TMS_TRANSPORT_OBJ_BRAND", type = 1, sort = 2)
    private String brand;
    @ExcelField(title = "分类", dictType = "TMS_TRANSPORT_OBJ_CLASSIFICATION", type = 1, sort = 2)
    private String classification;
    @ExcelField(title = "路线", type = 1, sort = 2)
    private String route;
    @ExcelField(title = "维修工时单价", type = 1, sort = 2)
    private Double repairPrice;
    @ExcelField(title = "承运商对应机构", type = 1, sort = 2)
    private String carrierMatchedOrg;
    @ExcelField(title = "网点对应机构", type = 1, sort = 2)
    private String outletMatchedOrg;
    @ExcelField(title = "机构", type = 1, sort = 2)
    private String orgName;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Double getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(Double repairPrice) {
        this.repairPrice = repairPrice;
    }

    public String getCarrierMatchedOrg() {
        return carrierMatchedOrg;
    }

    public void setCarrierMatchedOrg(String carrierMatchedOrg) {
        this.carrierMatchedOrg = carrierMatchedOrg;
    }

    public String getOutletMatchedOrg() {
        return outletMatchedOrg;
    }

    public void setOutletMatchedOrg(String outletMatchedOrg) {
        this.outletMatchedOrg = outletMatchedOrg;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
