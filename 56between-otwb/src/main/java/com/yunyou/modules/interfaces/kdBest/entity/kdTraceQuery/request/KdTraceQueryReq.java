package com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.request;

import com.yunyou.modules.interfaces.kdBest.utils.BaseRequest;
import com.yunyou.modules.interfaces.kdBest.utils.BaseResponse;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response.KdTraceQueryRsp;

public class KdTraceQueryReq implements BaseRequest {
	private MailNos mailNos;

    public MailNos getMailNos() {
        return this.mailNos;
    }

    public void setMailNos(MailNos value) {
        this.mailNos = value;
    }

    public String obtainServiceType() {
        return "KD_TRACE_QUERY";
    }

    public BaseResponse makeResponse(String rsp, String format) {
        if ("xml".equalsIgnoreCase(format)) {
			return Parser.coverXml2Object(rsp, KdTraceQueryRsp.class);
		}
		return Parser.convertJson2Object(rsp, KdTraceQueryRsp.class);
    }

}
