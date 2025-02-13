package com.yunyou.modules.interfaces.kdBest.utils;


public class Param {
	private String serviceType;
	private String bizData;
	private String partnerKey;
	private String partnerID;
	private String sign;

    public String getServiceType()
    {
        return this.serviceType;
    }

    public void setServiceType(String value)
    {
        this.serviceType = value;
    }
    public String getBizData()
    {
        return this.bizData;
    }

    public void setBizData(String value)
    {
        this.bizData = value;
    }
    public String getPartnerKey()
    {
        return this.partnerKey;
    }

    public void setPartnerKey(String value)
    {
        this.partnerKey = value;
    }
    public String getPartnerID()
    {
        return this.partnerID;
    }

    public void setPartnerID(String value)
    {
        this.partnerID = value;
    }
    public String getSign()
    {
        return this.sign;
    }

    public void setSign(String value)
    {
        this.sign = value;
    }
}