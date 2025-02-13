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
package com.yunyou.modules.interfaces.gps;

import com.yunyou.modules.interfaces.gps.constant.Method;

import java.util.Map;

/**
 * Request
 */
public class Request {

    /**
     * （必选）请求方法
     */
    private Method method;
    /**
     * （必选）Host
     */
    private String host;
    /**
     * （必选）Path
     */
    private String path;
    /**
     * （必选)APP KEY
     */
    private String accessId;
    /**
     * （必选）APP密钥
     */
    private String secretKey;
    /**
     * （必选）超时时间,单位毫秒
     */
    private int timeout;
    /**
     * （可选） HTTP头
     */
    private Map<String, String> headers;
    /**
     * （可选） Querys
     */
    private Map<String, String> queries;
    /**
     * （可选）表单参数
     */
    private Map<String, String> bodies;
    /**
     * （可选）字符串Body体
     */
    private String JsonStrBody;
    /**
     * （可选）字节数组类型Body体
     */
    private byte[] bytesBody;

    public Request() {
    }

    public Request(Method method, String host, String path, String accessId, String secretKey, int timeout) {
        this.method = method;
        this.host = host;
        this.path = path;
        this.accessId = accessId;
        this.secretKey = secretKey;
        this.timeout = timeout;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public Map<String, String> getBodies() {
        return bodies;
    }

    public void setBodies(Map<String, String> bodies) {
        this.bodies = bodies;
    }

    public String getJsonStrBody() {
        return JsonStrBody;
    }

    public void setJsonStrBody(String jsonStrBody) {
        this.JsonStrBody = jsonStrBody;
    }

    public byte[] getBytesBody() {
        return bytesBody;
    }

    public void setBytesBody(byte[] bytesBody) {
        this.bytesBody = bytesBody;
    }

}
