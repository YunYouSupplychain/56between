package com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.response;

import java.util.Date;

public class SfRouteQueryResponseData {

    private Date acceptTime;            // 路由节点发生的时间
    private String acceptAddress;       // 路由节点发生的地点
    private String remark;              // 路由节点具体描述
    /*
        路由节点操作码
        已收件状态的opCode:"50"、"51"、"54"
        运输中状态的opCode:"21"、"30"、"31"、"41"、"43"、"46"、"125"、"204"
        派送中状态的opCode:"34"、"44"、 "99"、"123"、"130"、"630"、"634"、"642"、"643"、"658"、"663"、"880"
        已签收状态的opCode:"80"、"8000"
        异常件状态的opCode:"70A"、"99A"、"631"、"8000A"、"8000B"、"8000C"、"8000E"、"70"、"33"、"77"
     */
    private String opCode;

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptAddress() {
        return acceptAddress;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }
}
