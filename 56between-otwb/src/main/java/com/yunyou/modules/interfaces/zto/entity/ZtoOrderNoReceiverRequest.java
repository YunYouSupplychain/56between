package com.yunyou.modules.interfaces.zto.entity;

/**
 * 中通面单号获取接口参数内容体
 * @author WMJ
 * @version 2020-05-07
 */
public class ZtoOrderNoReceiverRequest {
    // 收件人在合作商平台中的ID号
    private String id;
    // 收件人姓名 必填
    private String name;
    // 收件公司名
    private String company;
    // 收件人手机号码 至少其中之一
    private String mobile;
    // 收件人电话号码 至少其中之一
    private String phone;
    // 收件人区域ID，如提供区域ID，请参考中通速递提供的国家行政区划代码
    private String area;
    // 收件人所在城市，必须逐级指定，用英文半角逗号分隔，目前至少需要指定到区县级，如能往下精确更好，如“上海市,上海市,青浦区,华新镇,华志路,123号” 必填
    private String city;
    // 收件人路名门牌等地址信息 必填
    private String address;
    // 收件人邮政编码
    private String zipcode;
    // 收件人电子邮件
    private String email;
    // 收件人即时通讯工具
    private String im;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }
}
