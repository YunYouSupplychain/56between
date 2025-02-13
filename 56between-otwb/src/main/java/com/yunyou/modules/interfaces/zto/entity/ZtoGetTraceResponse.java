package com.yunyou.modules.interfaces.zto.entity;

import java.util.List;

/**
 * 中通快递轨迹获取接口response
 * @author WMJ
 * @version 2020-05-08
 */
public class ZtoGetTraceResponse {
    private boolean status;
    private String msg;
    private List<Data> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        // 运单号
        private String billCode;
        // 路由信息列表
        private List<ZtoGetTraceInfoResponse> traces;

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public List<ZtoGetTraceInfoResponse> getTraces() {
            return traces;
        }

        public void setTraces(List<ZtoGetTraceInfoResponse> traces) {
            this.traces = traces;
        }
    }
}
