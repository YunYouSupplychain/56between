package com.yunyou.modules.interfaces.kdBest.entity.kdInspectionSubmit.request;

import com.yunyou.modules.interfaces.kdBest.entity.kdInspectionSubmit.response.KdInspectionSubmitRsp;
import com.yunyou.modules.interfaces.kdBest.utils.BaseRequest;
import com.yunyou.modules.interfaces.kdBest.utils.BaseResponse;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;

public class KdInspectionSubmitReq implements BaseRequest {
	private String mailNo;
	private String sendManName;
	private String sendProvinceName;
	private String sendCityName;
	private String sendCountyName;
	private String acceptManName;
	private String acceptManPhone;
	private String acceptManAddress;
	private String acceptProvinceName;
	private String acceptCityName;
	private String acceptCountyName;

    public String getMailNo()
    {
        return this.mailNo;
    }

    public void setMailNo(String value)
    {
        this.mailNo = value;
    }

    public String getSendManName()
    {
        return this.sendManName;
    }

    public void setSendManName(String value)
    {
        this.sendManName = value;
    }

    public String getSendProvinceName()
    {
        return this.sendProvinceName;
    }

    public void setSendProvinceName(String value)
    {
        this.sendProvinceName = value;
    }

    public String getSendCityName()
    {
        return this.sendCityName;
    }

    public void setSendCityName(String value)
    {
        this.sendCityName = value;
    }

    public String getSendCountyName()
    {
        return this.sendCountyName;
    }

    public void setSendCountyName(String value)
    {
        this.sendCountyName = value;
    }

    public String getAcceptManName()
    {
        return this.acceptManName;
    }

    public void setAcceptManName(String value)
    {
        this.acceptManName = value;
    }

    public String getAcceptManPhone()
    {
        return this.acceptManPhone;
    }

    public void setAcceptManPhone(String value)
    {
        this.acceptManPhone = value;
    }

    public String getAcceptManAddress()
    {
        return this.acceptManAddress;
    }

    public void setAcceptManAddress(String value)
    {
        this.acceptManAddress = value;
    }

    public String getAcceptProvinceName()
    {
        return this.acceptProvinceName;
    }

    public void setAcceptProvinceName(String value)
    {
        this.acceptProvinceName = value;
    }

    public String getAcceptCityName()
    {
        return this.acceptCityName;
    }

    public void setAcceptCityName(String value)
    {
        this.acceptCityName = value;
    }

    public String getAcceptCountyName()
    {
        return this.acceptCountyName;
    }

    public void setAcceptCountyName(String value)
    {
        this.acceptCountyName = value;
    }

    public String obtainServiceType() {
        return "KD_INSPECTION_SUBMIT";
    }

    public BaseResponse makeResponse(String rsp, String format) {
        if ("xml".equalsIgnoreCase(format)) {
			return Parser.coverXml2Object(rsp, KdInspectionSubmitRsp.class);
		}
		return Parser.convertJson2Object(rsp, KdInspectionSubmitRsp.class);
    }

}
