package com.yunyou.modules.interfaces.yto.utils;

import com.google.common.collect.Maps;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.xml.XmlObjectUtils;
import com.yunyou.modules.interfaces.yto.entity.createOrder.request.YTOCreateOrderRequest;
import com.yunyou.modules.interfaces.yto.entity.createOrder.response.YTOCreateOrderResponseData;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class YTOUtils {
    public static final Map<String, String> ERROR_DESCRIPTION = Maps.newHashMap();

    static {
        ERROR_DESCRIPTION.put("200", "请求成功");
        ERROR_DESCRIPTION.put("S01", "订单报文不合法");
        ERROR_DESCRIPTION.put("S02", "数字签名不匹配");
        ERROR_DESCRIPTION.put("S03", "没有剩余单号");
        ERROR_DESCRIPTION.put("S04", "接口请求参数为空：logistics_interface, data_digest或clientId");
        ERROR_DESCRIPTION.put("S05", "唯品会专用");
        ERROR_DESCRIPTION.put("S06", "请求太快");
        ERROR_DESCRIPTION.put("S07", "url解码失败");
        ERROR_DESCRIPTION.put("S08", "订单号重复：订单号+客户编码+orderType全部重复则为重复");
        ERROR_DESCRIPTION.put("S09", "数据入库异常");
    }

    public static String requestCover2Xml(YTOCreateOrderRequest request) {
        return "<" + "RequestOrder" + ">" +
                XmlObjectUtils.object2XmlConvert(request) +
                "</" + "RequestOrder" + ">";
    }

    public static String responseCover2Xml(YTOCreateOrderResponseData response) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Response>" +
                XmlObjectUtils.object2XmlConvert(response) +
                "</Response>";
    }

    public static String paramsToQueryStringUrlencoded(Map<String, String> params) {
        return params.entrySet().stream().map(e -> {
            try {
                return e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                return e.getValue();
            }
        }).collect(Collectors.joining("&"));
    }

    public static String execute(String url, String clientID, String partnerID, String xmlString) {
        // 先MD5加密
        byte[] md5 = DigestUtils.md5(xmlString + partnerID);
        // 转换base64字符串
        String dataDigest = Base64.encodeBase64String(md5);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("logistics_interface", xmlString);
        parameters.put("data_digest", dataDigest);
        parameters.put("clientId", clientID);
        parameters.put("type", "offline");
        HttpClientUtil clientUtil = HttpClientUtil.getInstance();
        return clientUtil.sendHttpPost(url, paramsToQueryStringUrlencoded(parameters), false);
    }

    public static String traceQuery(String requestString, String url, String method, String v, String appKey, String userId, String secretKey) {
        String timestamp = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        // 签名
        String signStringA = "app_key" + appKey + "format" + "JSON" + "method" + method + "timestamp" + timestamp + "user_id" + userId + "v" + v;
        String signStringB = secretKey + signStringA;
        String signStringC = DigestUtils.md5Hex(signStringB);
        String signStringD = signStringC.toUpperCase();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "JSON");
        parameters.put("sign", signStringD);
        parameters.put("app_key", appKey);
        parameters.put("method", method);
        parameters.put("timestamp", timestamp);
        parameters.put("user_id", userId);
        parameters.put("v", v);
        parameters.put("param", requestString);
        parameters.put("Secret_Key", secretKey);

        HttpClientUtil clientUtil = HttpClientUtil.getInstance();
        return clientUtil.sendHttpPost(url, paramsToQueryStringUrlencoded(parameters), false);
    }
}
