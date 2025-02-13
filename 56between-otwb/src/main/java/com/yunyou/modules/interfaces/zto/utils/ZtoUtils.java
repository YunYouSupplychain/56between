package com.yunyou.modules.interfaces.zto.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 中通接口发送请求工具类
 * @author WMJ
 * @version 2020-05-07
 */
public class ZtoUtils {

    public static String execute(String companyId, String key, String msgType, String data, String urlString) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("data", data);
        parameters.put("company_id", companyId);
        parameters.put("msg_type", msgType);
        String result = null;

        try {
            String strToDigest = paramsToQueryString(parameters) + key;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strToDigest.getBytes(StandardCharsets.UTF_8));
            String dataDigest = Base64.getEncoder().encodeToString(md.digest());

            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("x-datadigest", dataDigest);
            con.setRequestProperty("x-companyid", companyId);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(paramsToQueryStringUrlencoded(parameters).getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            result = content.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String paramsToQueryString(Map<String, String> params) {
        return params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
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

}
