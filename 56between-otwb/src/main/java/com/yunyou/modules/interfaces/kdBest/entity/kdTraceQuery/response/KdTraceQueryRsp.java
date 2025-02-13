package com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response;

import com.yunyou.modules.interfaces.kdBest.utils.BaseResponse;

import java.util.List;

public class KdTraceQueryRsp implements BaseResponse {
	private boolean result;
	private String remark;
	private String errorCode;
	private String errorDescription;
	private List<TraceLogs> traceLogs;

    public boolean getResult() {
        return this.result;
    }

    public void setResult(boolean value)
    {
        this.result = value;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String value)
    {
        this.remark = value;
    }

    public String getErrorCode()
    {
        return this.errorCode;
    }

    public void setErrorCode(String value)
    {
        this.errorCode = value;
    }

    public String getErrorDescription()
    {
        return this.errorDescription;
    }

    public void setErrorDescription(String value)
    {
        this.errorDescription = value;
    }

    public List<TraceLogs>  getTraceLogs() {
        return this.traceLogs;
    }

    public void setTraceLogs(List<TraceLogs>  value)
    {
        this.traceLogs = value;
    }

}
