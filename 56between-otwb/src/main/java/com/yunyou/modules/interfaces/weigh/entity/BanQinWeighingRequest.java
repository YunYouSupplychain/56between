package com.yunyou.modules.interfaces.weigh.entity;


import java.util.Date;

public class BanQinWeighingRequest {

    private String ID;      // 条码
    private String SN;      // 设备序列号
    private String ORDER;   // 订单编号
    private Double WEI;     // 重量
    private Double WEI_PI;  // 皮重
    private Date TIME;      // 时间

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public Double getWEI() {
        return WEI;
    }

    public void setWEI(Double WEI) {
        this.WEI = WEI;
    }

    public Double getWEI_PI() {
        return WEI_PI;
    }

    public void setWEI_PI(Double WEI_PI) {
        this.WEI_PI = WEI_PI;
    }

    public Date getTIME() {
        return TIME;
    }

    public void setTIME(Date TIME) {
        this.TIME = TIME;
    }

    public String getORDER() {
        return ORDER;
    }

    public void setORDER(String ORDER) {
        this.ORDER = ORDER;
    }
}
