package com.yunyou.modules.tms.applet.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class TmAppQueryRequest implements Serializable {

    private static final long serialVersionUID = -2539186988557351186L;
    private String userId;
    private String searchKey;           // 查询key
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date searchDateFm;          // 查询key
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date searchDateTo;          // 查询key
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Date getSearchDateFm() {
        return searchDateFm;
    }

    public void setSearchDateFm(Date searchDateFm) {
        this.searchDateFm = searchDateFm;
    }

    public Date getSearchDateTo() {
        return searchDateTo;
    }

    public void setSearchDateTo(Date searchDateTo) {
        this.searchDateTo = searchDateTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}