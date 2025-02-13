package com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.request;

import com.yunyou.modules.interfaces.kdBest.utils.BaseRequest;
import com.yunyou.modules.interfaces.kdBest.utils.BaseResponse;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.response.KdWaybillApplyNotifyRsp;

import java.util.List;

public class KdWaybillApplyNotifyReq implements BaseRequest {
	private boolean deliveryConfirm;
	private List<EDIPrintDetailList> EDIPrintDetailList;
	private String storeCode;
	private String msgId;
	private Auth auth;

    public boolean getDeliveryConfirm()
    {
        return this.deliveryConfirm;
    }

    public void setDeliveryConfirm(boolean value)
    {
        this.deliveryConfirm = value;
    }

    public List<EDIPrintDetailList>  getEDIPrintDetailList()
    {
        return this.EDIPrintDetailList;
    }

    public void setEDIPrintDetailList(List<EDIPrintDetailList>  value)
    {
        this.EDIPrintDetailList = value;
    }
    public String getStoreCode()
    {
        return this.storeCode;
    }

    public void setStoreCode(String value)
    {
        this.storeCode = value;
    }

    public String getMsgId()
    {
        return this.msgId;
    }

    public void setMsgId(String value)
    {
        this.msgId = value;
    }

    public Auth getAuth()
    {
        return this.auth;
    }

    public void setAuth(Auth value)
    {
        this.auth = value;
    }

    public String obtainServiceType() {
        return "KD_WAYBILL_APPLY_NOTIFY";
    }

    public BaseResponse makeResponse(String rsp, String format) {
        if ("xml".equalsIgnoreCase(format)) {
			return Parser.coverXml2Object(rsp, KdWaybillApplyNotifyRsp.class);
		}
		return Parser.convertJson2Object(rsp, KdWaybillApplyNotifyRsp.class);
    }

}
