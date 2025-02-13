package com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.response;

import com.yunyou.modules.interfaces.kdBest.utils.BaseResponse;

import java.util.List;

public class KdWaybillApplyNotifyRsp implements BaseResponse {
	private String errorCode;
	private String errorDescription;
	private boolean result;
	private List<EDIPrintDetailList> EDIPrintDetailList;

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

    public boolean getResult()
    {
        return this.result;
    }

    public void setResult(boolean value)
    {
        this.result = value;
    }

    public List<EDIPrintDetailList>  getEDIPrintDetailList()
    {
        return this.EDIPrintDetailList;
    }

    public void setEDIPrintDetailList(List<EDIPrintDetailList>  value)
    {
        this.EDIPrintDetailList = value;
    }

}
