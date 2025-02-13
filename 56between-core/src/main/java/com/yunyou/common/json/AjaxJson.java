package com.yunyou.common.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunyou.core.mapper.JsonMapper;

import java.util.LinkedHashMap;

/**
 * $.ajax后需要接受的JSON
 *
 * @author yunyou
 */
public class AjaxJson {
    /**
     * 是否成功
     */
    private boolean success = true;
    /**
     * 错误代码
     */
    private String errorCode = "-1";
    /**
     * 提示信息
     */
    private String msg = "操作成功";
    /**
     * 封装json的map
     */
    private LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    /**
     * 处理数量1
     */
    private Integer handleQty1;
    /**
     * 处理数量2
     */
    private Integer handleQty2;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LinkedHashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(LinkedHashMap<String, Object> body) {
        this.body = body;
    }

    public Integer getHandleQty1() {
        return handleQty1;
    }

    public void setHandleQty1(Integer handleQty1) {
        this.handleQty1 = handleQty1;
    }

    public Integer getHandleQty2() {
        return handleQty2;
    }

    public void setHandleQty2(Integer handleQty2) {
        this.handleQty2 = handleQty2;
    }

    @JsonIgnore
    public String getJsonStr() {//返回json字符串数组，将访问msg和key的方式统一化，都使用data.key的方式直接访问。
        return JsonMapper.toJsonString(this);
    }

    public void put(String key, Object value) {
        //向json中添加属性，在js中访问，请调用data.map.key
        body.put(key, value);
    }

    public void remove(String key) {
        body.remove(key);
    }
}
