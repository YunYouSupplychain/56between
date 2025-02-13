package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppLoginRequest implements Serializable {

    private String code;

    private String password;

    private String loginType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}