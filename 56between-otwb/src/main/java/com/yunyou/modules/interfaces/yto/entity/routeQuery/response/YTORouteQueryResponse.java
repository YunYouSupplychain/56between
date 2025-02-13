package com.yunyou.modules.interfaces.yto.entity.routeQuery.response;

public class YTORouteQueryResponse {

    private String waybill_No;
    private String upload_Time;
    private String processInfo;

    public String getWaybill_No() {
        return waybill_No;
    }

    public void setWaybill_No(String waybill_No) {
        this.waybill_No = waybill_No;
    }

    public String getUpload_Time() {
        return upload_Time;
    }

    public void setUpload_Time(String upload_Time) {
        this.upload_Time = upload_Time;
    }

    public String getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(String processInfo) {
        this.processInfo = processInfo;
    }
}
