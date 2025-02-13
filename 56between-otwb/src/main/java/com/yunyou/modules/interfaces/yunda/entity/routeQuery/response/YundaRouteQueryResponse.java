package com.yunyou.modules.interfaces.yunda.entity.routeQuery.response;

import java.util.List;

public class YundaRouteQueryResponse {

    // 调用运单号
    private String mailno;
    // true成功，false失败
    private Boolean result;
    // 接口响应时间 格式 yyyy-MM-dd HH : mm : ss
    private String time;
    // 描述
    private String remark;
    /**
     *  运单状态
     * GOT	    已揽件
     * TRANSIT	运输中
     * SIGNED	已签收
     * RETURN	退回件
     * SIGNFAIL	异常签收
     */
    private String status;
    // 业务轨迹
    private List<YundaRouteQueryStepsResponse> steps;

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<YundaRouteQueryStepsResponse> getSteps() {
        return steps;
    }

    public void setSteps(List<YundaRouteQueryStepsResponse> steps) {
        this.steps = steps;
    }
}
