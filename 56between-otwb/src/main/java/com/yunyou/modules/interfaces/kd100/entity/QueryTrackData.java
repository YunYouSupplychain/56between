package com.yunyou.modules.interfaces.kd100.entity;

public class QueryTrackData {
    /**
     * 时间，原始格式
     */
    private String time;
    /**
     * 物流轨迹节点内容
     */
    private String context;
    /**
     * 格式化后时间
     */
    private String ftime;
    /**
     * 行政区域的编码
     */
    private String areaCode;
    /**
     * 行政区域的名称
     */
    private String areaName;
    /**
     * 签收状态 (0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投)
     */
    private String status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
