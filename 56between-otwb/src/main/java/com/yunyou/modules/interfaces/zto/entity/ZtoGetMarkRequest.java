package com.yunyou.modules.interfaces.zto.entity;

/**
 * 中通获取集装地大头笔参数
 * @author WMJ
 * @version 2020-05-07
 */
public class ZtoGetMarkRequest {
    // 唯一标示，由调用方生成，建议使用运单号，如无运单号，也可以使用GUID/UUID 必填
    private String unionCode;
    // 发件省份 必填
    private String send_province;
    // 发件城市 必填
    private String send_city;
    // 发件区县 必填
    private String send_district;
    // 收件省份 必填
    private String receive_province;
    // 收件城市 必填
    private String receive_city;
    // 收件区县 必填
    private String receive_district;
    // 收件详细地址 必填
    private String receive_address;

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getSend_province() {
        return send_province;
    }

    public void setSend_province(String send_province) {
        this.send_province = send_province;
    }

    public String getSend_city() {
        return send_city;
    }

    public void setSend_city(String send_city) {
        this.send_city = send_city;
    }

    public String getSend_district() {
        return send_district;
    }

    public void setSend_district(String send_district) {
        this.send_district = send_district;
    }

    public String getReceive_province() {
        return receive_province;
    }

    public void setReceive_province(String receive_province) {
        this.receive_province = receive_province;
    }

    public String getReceive_city() {
        return receive_city;
    }

    public void setReceive_city(String receive_city) {
        this.receive_city = receive_city;
    }

    public String getReceive_district() {
        return receive_district;
    }

    public void setReceive_district(String receive_district) {
        this.receive_district = receive_district;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }
}
