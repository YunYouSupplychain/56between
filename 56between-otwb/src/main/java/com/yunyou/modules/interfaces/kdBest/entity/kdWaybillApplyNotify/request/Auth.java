package com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.request;

public class Auth {
	private String username;
	private String pass;

    public Auth(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String value)
    {
        this.username = value;
    }

    public String getPass()
    {
        return this.pass;
    }

    public void setPass(String value)
    {
        this.pass = value;
    }

}
