package com.yunyou.modules.interfaces.kdBest.entity.kdInspectionSubmit.response;

import com.yunyou.modules.interfaces.kdBest.utils.BaseResponse;

public class KdInspectionSubmitRsp implements BaseResponse {
	private boolean result;
	private String remark;

    public boolean getResult()
    {
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


}
