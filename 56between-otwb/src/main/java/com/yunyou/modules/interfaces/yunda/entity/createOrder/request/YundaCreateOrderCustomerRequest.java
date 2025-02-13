package com.yunyou.modules.interfaces.yunda.entity.createOrder.request;

public class YundaCreateOrderCustomerRequest {

    // 姓名
    private String name;
    // 公司名
    private String company;
    // 严格按照国家行政区划，省市区三级，逗号分隔。示例上海市,上海市,青浦区（cod订单必填）
    private String city;
    // 需要将省市区划信息加上，例如：上海市,上海市,青浦区盈港东路7766号
    private String address;
    // 固定电话
    private String phone;
    // 移动电话（固定电话或移动电话至少填一项）
    private String mobile;

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
}
