package com.yunyou.modules.interfaces.kdBest.utils;

import com.yunyou.common.http.HttpClientUtil;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

public class Client {

    private String url;
    private String partnerKey;
    private String partnerID;
    private String messageFormat;
    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    public Client(String url, String partnerKey, String partnerID, String messageFormat){
        this.url = url;
        this.messageFormat = messageFormat;
        this.partnerKey = partnerKey;
        this.partnerID = partnerID;

    }

    public <T extends BaseResponse> T executed(BaseRequest baseRequest) {
        String response = "";
        Map<String, String> params = new HashMap<>();
        Param param = new Param();
		param.setServiceType(baseRequest.obtainServiceType());
        param.setBizData(makeBizData(baseRequest));
		param.setPartnerKey(partnerKey);
		param.setPartnerID(partnerID);

		params.put("serviceType", param.getServiceType());
		params.put("bizData", param.getBizData());
		params.put("partnerID", param.getPartnerID());
		params.put("sign", Sign.makeSign(param));
        try {
            response = httpClientUtil.sendPost(url, params);
        } catch (Exception e) {

        }
        return response == null ? null : (T) baseRequest.makeResponse(response, messageFormat);
    }

    private String makeBizData(BaseRequest baseRequest) {
        if("xml".equalsIgnoreCase(this.messageFormat)) {
            return HtmlUtils.htmlUnescape(Parser.coverObject2Xml(baseRequest));
        }
        return HtmlUtils.htmlUnescape(Parser.convertObject2Json(baseRequest));
    }
}