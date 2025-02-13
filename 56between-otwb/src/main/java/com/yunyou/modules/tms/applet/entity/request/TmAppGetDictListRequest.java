package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppGetDictListRequest implements Serializable {

    private String dictType;              // 类型
    private String userId;

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}