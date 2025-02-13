/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.yunyou.modules.interfaces.gps.g7.util;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.gps.g7.constant.Constants;
import com.yunyou.modules.interfaces.gps.g7.constant.HttpConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.yunyou.modules.interfaces.gps.constant.Constants.*;

/**
 * 签名工具
 */
public class SignUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 计算签名
     *
     * @param secret APP密钥
     * @param method HttpMethod
     * @return 签名后的字符串
     */
    public static String sign(String secret, String method, String path,
                              Map<String, String> headers,
                              Map<String, String> queries,
                              Map<String, String> bodies) {
        try {
            Mac hmacSha256 = Mac.getInstance(Constants.HMAC_SHA256);
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, Constants.HMAC_SHA256));

            String stringToSign = buildStringToSign(method, path, headers, queries, bodies);
            LOG.info("StringToSign:\n{}", stringToSign);

            byte[] bytes = hmacSha256.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String buildStringToSign(String method, String path,
                                            Map<String, String> headers,
                                            Map<String, String> queries,
                                            Map<String, String> bodies) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.toUpperCase()).append(LF);
        if (null != headers) {
            if (null != headers.get(HttpConstants.HTTP_HEADER_CONTENT_MD5)) {
                sb.append(headers.get(HttpConstants.HTTP_HEADER_CONTENT_MD5));
            }
            sb.append(LF);
            if (null != headers.get(HttpConstants.HTTP_HEADER_CONTENT_TYPE)) {
                sb.append(headers.get(HttpConstants.HTTP_HEADER_CONTENT_TYPE));
            }
            sb.append(LF);
            if (null != headers.get(HttpConstants.HTTP_HEADER_G7_TIMESTAMP)) {
                sb.append(headers.get(HttpConstants.HTTP_HEADER_G7_TIMESTAMP));
            }
        }
        sb.append(LF);
        sb.append(buildHeaders(headers));
        sb.append(buildResource(path, queries, bodies));
        return sb.toString();
    }

    /**
     * 构建待签名Path+Query+BODY
     *
     * @return 待签名
     */
    private static String buildResource(String path, Map<String, String> queries, Map<String, String> bodies) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(path)) {
            sb.append(path);
        }
        Map<String, String> sortMap = new TreeMap<>();
        if (null != queries) {
            for (Map.Entry<String, String> query : queries.entrySet()) {
                if (StringUtils.isNotBlank(query.getKey())) {
                    sortMap.put(query.getKey(), query.getValue());
                }
            }
        }
        if (null != bodies) {
            for (Map.Entry<String, String> body : bodies.entrySet()) {
                if (StringUtils.isNotBlank(body.getKey())) {
                    sortMap.put(body.getKey(), body.getValue());
                }
            }
        }
        StringBuilder sbParam = new StringBuilder();
        for (Map.Entry<String, String> item : sortMap.entrySet()) {
            if (StringUtils.isBlank(item.getKey())) {
                continue;
            }
            if (0 < sbParam.length()) {
                sbParam.append(SPE3_CONNECT);
            }
            sbParam.append(item.getKey()).append(SPE4_EQUAL);
            if (StringUtils.isNotBlank(item.getValue())) {
                sbParam.append(item.getValue());
            }
        }
        if (0 < sbParam.length()) {
            sb.append(SPE5_QUESTION);
            sb.append(sbParam);
        }
        return sb.toString();
    }

    /**
     * 构建待签名Http头
     *
     * @param headers 请求中所有的Http头
     * @return 待签名Http头
     */
    private static String buildHeaders(Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        if (null != headers) {
            Map<String, String> sortMap = new TreeMap<>();
            //对于header头值，转成小写了之后再排序
            for (Map.Entry<String, String> header : headers.entrySet()) {
                sortMap.put(header.getKey().toLowerCase(), header.getValue());
            }
            for (Map.Entry<String, String> header : sortMap.entrySet()) {
                if (isHeaderToSignByPrefix(header.getKey())) {
                    sb.append(header.getKey());
                    sb.append(SPE2_COLON);
                    if (StringUtils.isNotBlank(header.getValue())) {
                        sb.append(header.getValue());
                    }
                    sb.append(LF);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Http头是否参与签名
     */
    private static boolean isHeaderToSign(String headerName, List<String> signHeaderPrefixList) {
        if (StringUtils.isBlank(headerName)) {
            return false;
        }
        if (headerName.startsWith(HttpConstants.CA_HEADER_TO_SIGN_PREFIX_SYSTEM)) {
            return true;
        }
        if (null != signHeaderPrefixList) {
            for (String signHeaderPrefix : signHeaderPrefixList) {
                if (headerName.equalsIgnoreCase(signHeaderPrefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Http头是否参与签名 return
     */
    private static boolean isHeaderToSignByPrefix(String headerName) {
        if (StringUtils.isBlank(headerName)) {
            return false;
        }
        return headerName.toLowerCase().startsWith(HttpConstants.CA_HEADER_TO_SIGN_PREFIX_SYSTEM.toLowerCase());
    }
}
