package com.yunyou.modules.interfaces.kd100;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.kd100.entity.*;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.google.common.base.Splitter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liujianhua
 */
@Service
public class Kd100InterfaceService {
    @Autowired
    private InterfaceLogService interfaceLogService;

    public PrintBaseResp<PrintImgData> createOrder(PrintImgParam entity, String urls, String params) {
        if (StringUtils.isBlank(urls) || StringUtils.isBlank(params)) {
            throw new WarehouseException("接口参数未维护！");
        }
        String[] urlArray = urls.split(";", -1);
        if (urlArray.length < 1 || StringUtils.isBlank(urlArray[0])) {
            throw new WarehouseException("接口参数获取面单url未维护！");
        }
        String url = urlArray[0];
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String key = split.get("key");// 授权信息key
        if (StringUtils.isBlank(key)) {
            throw new WarehouseException("接口参数[key]未维护！");
        }
        String secret = split.get("secret");// 授权信息secret
        if (StringUtils.isBlank(secret)) {
            throw new WarehouseException("接口参数[secret]未维护！");
        }
        String tempId = split.get("tempid");// 面单模板配置信息-编码
        if (StringUtils.isBlank(tempId)) {
            throw new WarehouseException("接口参数[tempid]未维护！");
        }
        String partnerId = split.get("partnerId");// 电子面单客户账户或月结账号
        String partnerKey = split.get("partnerKey");// 电子面单密码
        String partnerSecret = split.get("partnerSecret");// 电子面单密钥
        String partnerName = split.get("partnerName");// 电子面单客户账户名称
        String net = split.get("net");// 收件网点名称
        String code = split.get("code");// 电子面单承载编号
        String checkMan = split.get("checkMan");// 电子面单承载快递员名

        entity.setPartnerId(partnerId);
        entity.setPartnerKey(partnerKey);
        entity.setPartnerSecret(partnerSecret);
        entity.setPartnerName(partnerName);
        entity.setNet(net);
        entity.setCode(code);
        entity.setCheckMan(checkMan);
        entity.setTempid(tempId);

        String param = JSONObject.toJSONString(entity);
        String t = System.currentTimeMillis() + "";
        String sign = SignUtils.printSign(param, t, key, secret);

        PrintReq request = new PrintReq();
        request.setKey(key);
        request.setMethod(ApiInfoConstant.ELECTRONIC_ORDER_PIC_METHOD);
        request.setSign(sign);
        request.setParam(param);
        request.setT(t);
        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.KD100_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, entity.getOrderId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_PRE);

        try {
            String responseStr = HttpClientUtil.getInstance().sendHttpPost(url, ObjectToMapUtils.objectToMap(request));
            if (StringUtils.isNotBlank(responseStr)) {
                PrintBaseResp<PrintImgData> response = JSONObject.parseObject(responseStr, new TypeReference<PrintBaseResp<PrintImgData>>() {
                });
                if (!"200".equals(response.getReturnCode())) {
                    saveLog(InterfaceConstant.KD100_CREATE_ORDER, InterfaceConstant.STATUS_N, response.getMessage(), requestString, responseStr, entity.getOrderId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                    return null;
                }
                // 保存日志时简化response，imgBase64太大
                PrintBaseResp<PrintImgData> simplifyResponse = JSONObject.parseObject(responseStr, new TypeReference<PrintBaseResp<PrintImgData>>() {
                });
                simplifyResponse.getData().setImgBase64(null);
                String simplifyResponseStr = JSONObject.toJSONString(simplifyResponse);

                saveLog(InterfaceConstant.KD100_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, simplifyResponseStr, entity.getOrderId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                return response;
            } else {
                saveLog(InterfaceConstant.KD100_CREATE_ORDER, InterfaceConstant.STATUS_N, "反馈信息为空", requestString, responseStr, entity.getOrderId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                return null;
            }
        } catch (Exception e) {
            saveLog(InterfaceConstant.KD100_CREATE_ORDER, InterfaceConstant.STATUS_N, "接口请求失败", requestString, null, entity.getOrderId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
    }

    public PrintBaseResp<PrintImgData> replay(CloudPrintOldParam entity, String urls, String params) {
        if (StringUtils.isBlank(urls) || StringUtils.isBlank(params)) {
            throw new WarehouseException("接口参数未维护！");
        }
        String[] urlArray = urls.split(";", -1);
        if (urlArray.length < 1 || StringUtils.isBlank(urlArray[0])) {
            throw new WarehouseException("接口参数获取面单url未维护！");
        }
        String url = urlArray[0];
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String key = split.get("key");// 授权信息key
        if (StringUtils.isBlank(key)) {
            throw new WarehouseException("接口参数[key]未维护！");
        }
        String secret = split.get("secret");// 授权信息secret
        if (StringUtils.isBlank(secret)) {
            throw new WarehouseException("接口参数[secret]未维护！");
        }
        String param = JSONObject.toJSONString(entity);
        String t = System.currentTimeMillis() + "";
        String sign = SignUtils.printSign(param, t, key, secret);

        PrintReq request = new PrintReq();
        request.setKey(key);
        request.setMethod(ApiInfoConstant.CLOUD_PRINT_OLD_METHOD);
        request.setSign(sign);
        request.setParam(param);
        request.setT(t);
        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.KD100_MAIL_REPLAY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, entity.getTaskId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_PRE);

        try {
            String responseStr = HttpClientUtil.getInstance().sendHttpPost(url, ObjectToMapUtils.objectToMap(request));
            if (StringUtils.isNotBlank(responseStr)) {
                PrintBaseResp<PrintImgData> response = JSONObject.parseObject(responseStr, new TypeReference<PrintBaseResp<PrintImgData>>() {
                });
                if (!"200".equals(response.getReturnCode())) {
                    saveLog(InterfaceConstant.KD100_MAIL_REPLAY, InterfaceConstant.STATUS_N, response.getMessage(), requestString, responseStr, entity.getTaskId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                    return null;
                }
                // 保存日志时简化response，imgBase64太大
                PrintBaseResp<PrintImgData> simplifyResponse = JSONObject.parseObject(responseStr, new TypeReference<PrintBaseResp<PrintImgData>>() {
                });
                simplifyResponse.getData().setImgBase64(null);
                String simplifyResponseStr = JSONObject.toJSONString(simplifyResponse);

                saveLog(InterfaceConstant.KD100_MAIL_REPLAY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, simplifyResponseStr, entity.getTaskId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                return response;
            } else {
                saveLog(InterfaceConstant.KD100_MAIL_REPLAY, InterfaceConstant.STATUS_N, "反馈信息为空", requestString, responseStr, entity.getTaskId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                return null;
            }
        } catch (Exception e) {
            saveLog(InterfaceConstant.KD100_MAIL_REPLAY, InterfaceConstant.STATUS_N, "接口请求失败", requestString, null, entity.getTaskId(), InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
    }

    public QueryTrackResp routeQuery(String carrierCode, String mailNo, String urls, String params) {
        if (StringUtils.isBlank(urls) || StringUtils.isBlank(params)) {
            throw new WarehouseException("接口参数未维护！");
        }
        String[] urlArray = urls.split(";", -1);
        if (urlArray.length < 2 || StringUtils.isBlank(urlArray[1])) {
            throw new WarehouseException("接口参数路由查询url未维护！");
        }
        String url = urlArray[1];
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String key = split.get("key");// 授权信息key
        if (StringUtils.isBlank(key)) {
            throw new WarehouseException("接口参数[key]未维护！");
        }
        String customer = split.get("customer");// 授权信息customer
        if (StringUtils.isBlank(customer)) {
            throw new WarehouseException("接口参数[customer]未维护！");
        }
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(carrierCode);
        queryTrackParam.setNum(mailNo);
        queryTrackParam.setResultv2("1");
        String param = JSONObject.toJSONString(queryTrackParam);

        QueryTrackReq request = new QueryTrackReq();
        request.setCustomer(customer);
        request.setParam(param);
        request.setSign(SignUtils.querySign(param, key, customer));
        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.KD100_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, mailNo, InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_PRE);

        try {
            String res = HttpClientUtil.getInstance().sendHttpPost(url, ObjectToMapUtils.objectToMap(request));
            if (StringUtils.isNotBlank(res)) {
                QueryTrackResp resp = JSONObject.parseObject(res, QueryTrackResp.class);
                if (resp == null || StringUtils.isBlank(resp.getState())) {
                    saveLog(InterfaceConstant.KD100_ROUTE_QUERY, InterfaceConstant.STATUS_N, JSONObject.parseObject(res).getString("message"), requestString, res, mailNo, InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                    return null;
                }
                saveLog(InterfaceConstant.KD100_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, res, mailNo, InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                return resp;
            } else {
                saveLog(InterfaceConstant.KD100_ROUTE_QUERY, InterfaceConstant.STATUS_N, "反馈信息为空", requestString, res, mailNo, InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                return null;
            }
        } catch (Exception e) {
            saveLog(InterfaceConstant.KD100_ROUTE_QUERY, InterfaceConstant.STATUS_N, "接口请求失败", requestString, null, mailNo, InterfaceConstant.KD100_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
    }

    protected void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, user, handleDir)).start();
    }

    /**
     * 处理返回的base64字符串（返回的是一个字符串json数组，多个子单时会有多个）
     *
     * @param imgBase64 base64 json字符串数组
     */
    public List<String> getBase64Img(String imgBase64) {
        return JSONObject.parseArray(imgBase64, String.class).stream().map(o -> {
            String base64 = null;
            try {
                BufferedImage source = ImageIO.read(new ByteArrayInputStream(Base64Utils.decode(o.getBytes(StandardCharsets.UTF_8))));

                int targetW = 300, targetH = 513;
                BufferedImage target = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
                target.getGraphics().drawImage(source.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH), 0, 0, null);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(target, "png", stream);

                base64 = Base64.encodeBase64String(stream.toByteArray());
            } catch (IOException ignored) {
            }
            return base64;
        }).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

}
