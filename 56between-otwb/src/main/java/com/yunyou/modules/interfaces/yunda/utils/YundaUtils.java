package com.yunyou.modules.interfaces.yunda.utils;

import com.google.common.collect.Maps;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.MD5Util;
import com.yunyou.common.utils.xml.XmlObjectUtils;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.request.YundaCreateOrderRequest;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.response.YundaCreateOrderResponse;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YundaUtils {
    public static final Map<String, String> ERROR_DESCRIPTION = Maps.newHashMap();

    static {
        ERROR_DESCRIPTION.put("s00", "未知错误");
        ERROR_DESCRIPTION.put("s01", "非法的合作商账户");
        ERROR_DESCRIPTION.put("s02", "非法的主机来源");
        ERROR_DESCRIPTION.put("s03", "非法的数据签名");
        ERROR_DESCRIPTION.put("s04", "非法的请求类型");
        ERROR_DESCRIPTION.put("s05", "非法的XML格式");
        ERROR_DESCRIPTION.put("s06", "非法的订单号");
        ERROR_DESCRIPTION.put("s07", "账户不具有访问本功能的权限");
        ERROR_DESCRIPTION.put("s08", "查询的运单没有经过指定账号进行订阅操作");
        ERROR_DESCRIPTION.put("s09", "当天下单量超出限额");
        ERROR_DESCRIPTION.put("s10", "当天查询量超出限额");
        ERROR_DESCRIPTION.put("s11", "无效的指令操作");
        ERROR_DESCRIPTION.put("s12", "没有指定有效的查询条件");
        ERROR_DESCRIPTION.put("s13", "非法运单号");
        ERROR_DESCRIPTION.put("s14", "数据格式错误");
        ERROR_DESCRIPTION.put("s15", "发件人手机错误");
        ERROR_DESCRIPTION.put("s16", "发件人电话错误");
        ERROR_DESCRIPTION.put("s17", "收件人手机错误");
        ERROR_DESCRIPTION.put("s18", "收件人电话错误");
        ERROR_DESCRIPTION.put("s51", "发件人信息不完整");
        ERROR_DESCRIPTION.put("s52", "收件人信息不完整");
        ERROR_DESCRIPTION.put("s71", "发件人所在地区服务已关闭");
        ERROR_DESCRIPTION.put("s72", "收件人所在地区服务已关闭");
        ERROR_DESCRIPTION.put("s91", "信息不完整");
        ERROR_DESCRIPTION.put("s97", "数据更新失败");
        ERROR_DESCRIPTION.put("s98", "数据保存失败");
        ERROR_DESCRIPTION.put("s99", "服务器错误");
        ERROR_DESCRIPTION.put("e03", "黑名单客户");
        ERROR_DESCRIPTION.put("e99", "其他原因");
    }

    public static String requestCover2Xml(List<YundaCreateOrderRequest> orders) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<").append("orders").append(">");
        for (YundaCreateOrderRequest order : orders) {
            sb.append("<").append("order").append(">");
            sb.append(XmlObjectUtils.object2XmlConvert(order));
            sb.append("</").append("order").append(">");
        }
        sb.append("</").append("orders").append(">");
        return sb.toString();
    }

    public static String responseCover2Xml(YundaCreateOrderResponse response) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<").append("responses").append(">");
        sb.append(XmlObjectUtils.object2XmlConvert(response));
        sb.append("</").append("responses").append(">");
        return sb.toString();
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

    public static String execute(String url, String partnerid, String password, String request, String xmlString) {
        String xmlDate = Base64.encodeBase64String(xmlString.getBytes());
        String validation = MD5Util.encrypt32(xmlDate + partnerid + password);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("partnerid", partnerid);
        parameters.put("request", request);
        parameters.put("xmldata", xmlDate);
        parameters.put("validation", validation);
        HttpClientUtil clientUtil = HttpClientUtil.getInstance();
        return clientUtil.sendHttpPost(url, paramsToQueryStringUrlencoded(parameters), false);
    }
}
