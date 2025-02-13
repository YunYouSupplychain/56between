package com.yunyou.modules.sys.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.TreeEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 机构Entity
 *
 * @author yunyou
 * @version 2016-05-15
 */
public class Office extends TreeEntity<Office> {

    private static final long serialVersionUID = 1L;
    // 归属区域
    private Area area;
    // 机构编码
    private String code;
    /**
     * 机构类型{@link com.yunyou.modules.sys.OfficeType}
     */
    private String type;
    // 联系地址
    private String address;
    // 邮政编码
    private String zipCode;
    // 负责人
    private String master;
    // 电话
    private String phone;
    // 传真
    private String fax;
    // 邮箱
    private String email;
    // 是否可用
    private String useable;

    /**
     * 查询条件 类型集合{@link com.yunyou.modules.sys.OfficeType}
     */
    private List<String> typeList = Lists.newArrayList();

    public Office() {
        super();
        this.setIdType(IDTYPE_UUID);
    }

    public Office(String id) {
        super(id);
    }

    @Override
    public Office getParent() {
        return parent;
    }

    @Override
    public void setParent(Office parent) {
        this.parent = parent;
    }

    @NotNull
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }
}