package com.yunyou.modules.interfaces.yto.entity.createOrder.request;

public class YTOCreateOrderSenderRequest {

    // 用户姓名     **必填
    private String name;
    // 用户邮编
    private Integer postCode;
    // 用户电话，包括区号、电话号码及分机号，中间用“-”分隔
    private String phone;
    // 用户移动电话， 手机和电话至少填一项
    private String mobile;
    // 省份       **必填
    private String prov;
    // 城市与区县， 城市与区县用英文逗号隔开      **必填
    private String city;
    // 详细地址（注：不包含省市区）   **必填
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
