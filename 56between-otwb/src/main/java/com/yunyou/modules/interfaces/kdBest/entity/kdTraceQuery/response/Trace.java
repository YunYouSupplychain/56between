package com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response;

public class Trace {
	private String acceptTime;
	private String acceptAddress;
	private String scanType;
	private String remark;

    public String getAcceptTime()
    {
        return this.acceptTime;
    }

    public void setAcceptTime(String value)
    {
        this.acceptTime = value;
    }

    public String getAcceptAddress()
    {
        return this.acceptAddress;
    }

    public void setAcceptAddress(String value)
    {
        this.acceptAddress = value;
    }

    public String getScanType()
    {
        return this.scanType;
    }

    public void setScanType(String value)
    {
        this.scanType = value;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String value)
    {
        this.remark = value;
    }

}
