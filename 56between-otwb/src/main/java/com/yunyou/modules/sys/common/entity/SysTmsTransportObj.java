package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 业务对象信息Entity
 */
public class SysTmsTransportObj extends DataEntity<SysTmsTransportObj> {

    private static final long serialVersionUID = 1L;
    private String transportObjCode;// 业务对象编码
    private String transportObjName;// 业务对象名称
    private String transportObjShortName;// 业务对象简称
    private String contact;// 联系人
    private String phone;// 手机
    private String tel;// 电话
    private String fax;// 传真
    private String email;// 电子邮箱
    private String url;// 网址
    private String areaId;// 所属城市ID
    private String address;// 详细地址
    private String unCode;// 统一码
    private String signBy;// 指定签收人
    private String brand;// 品牌
    private String transportObjType;// 业务对象类型
    private String carrierMatchedOrgId;// 承运商对应机构ID
    private String outletMatchedOrgId;// 网点对应机构ID
    private String dataSet;// 数据套
    private String classification;// 分类
    private String routeId;// 路线(业务路线code)
    private Double repairPrice;// 维修工时单价
    private String settleCode;// 结算对象编码

    public SysTmsTransportObj() {
        super();
    }

    public SysTmsTransportObj(String id) {
        super(id);
    }

    public SysTmsTransportObj(String transportObjCode, String dataSet) {
        this.transportObjCode = transportObjCode;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "业务对象编码", align = 2, sort = 7)
    public String getTransportObjCode() {
        return transportObjCode;
    }

    public void setTransportObjCode(String transportObjCode) {
        this.transportObjCode = transportObjCode;
    }

    @ExcelField(title = "业务对象名称", align = 2, sort = 8)
    public String getTransportObjName() {
        return transportObjName;
    }

    public void setTransportObjName(String transportObjName) {
        this.transportObjName = transportObjName;
    }

    @ExcelField(title = "业务对象简称", align = 2, sort = 9)
    public String getTransportObjShortName() {
        return transportObjShortName;
    }

    public void setTransportObjShortName(String transportObjShortName) {
        this.transportObjShortName = transportObjShortName;
    }

    @ExcelField(title = "联系人", align = 2, sort = 10)
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @ExcelField(title = "手机", align = 2, sort = 11)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title = "电话", align = 2, sort = 12)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @ExcelField(title = "传真", align = 2, sort = 13)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @ExcelField(title = "电子邮箱", align = 2, sort = 14)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ExcelField(title = "网址", align = 2, sort = 15)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ExcelField(title = "所属城市ID", align = 2, sort = 16)
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @ExcelField(title = "详细地址", align = 2, sort = 17)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ExcelField(title = "统一码", align = 2, sort = 18)
    public String getUnCode() {
        return unCode;
    }

    public void setUnCode(String unCode) {
        this.unCode = unCode;
    }

    @ExcelField(title = "指定签收人", align = 2, sort = 19)
    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    @ExcelField(title = "品牌", dictType = "TMS_TRANSPORT_OBJ_BRAND", align = 2, sort = 20)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @ExcelField(title = "业务对象类型", dictType = "TMS_TRANSPORT_OBJ_TYPE", align = 2, sort = 21)
    public String getTransportObjType() {
        return transportObjType;
    }

    public void setTransportObjType(String transportObjType) {
        this.transportObjType = transportObjType;
    }

    @ExcelField(title = "承运商对应机构ID", align = 2, sort = 22)
    public String getCarrierMatchedOrgId() {
        return carrierMatchedOrgId;
    }

    public void setCarrierMatchedOrgId(String carrierMatchedOrgId) {
        this.carrierMatchedOrgId = carrierMatchedOrgId;
    }

    @ExcelField(title = "网点对应机构ID", align = 2, sort = 23)
    public String getOutletMatchedOrgId() {
        return outletMatchedOrgId;
    }

    public void setOutletMatchedOrgId(String outletMatchedOrgId) {
        this.outletMatchedOrgId = outletMatchedOrgId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
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

    public String getSettleCode() {
        return settleCode;
    }

    public void setSettleCode(String settleCode) {
        this.settleCode = settleCode;
    }
}