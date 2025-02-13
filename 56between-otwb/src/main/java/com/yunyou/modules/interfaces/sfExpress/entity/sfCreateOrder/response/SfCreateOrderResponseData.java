package com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response;

import java.io.Serializable;
import java.util.List;

public class SfCreateOrderResponseData implements Serializable {

    private static final long serialVersionUID = 1905025663260745685L;
    private String contractNumber;
    private String orderNumber;             // 订单号
    /*
        顺丰运单号
        (一个订单只能有一个母单号，如果是子母单的情况，以半角逗号分隔，主单号在第一个位置)
    */
    private String mailNumber;
    private String returnTrackingNumber;    // 顺丰签回单服务运单号
    private String originCode;              // 原寄地区域代码
    private String destCode;                // 原目的地区域代码
    /*
        筛单结果： 1：人工确认 2：可收派 3：不可以收派
     */
    private String filterResult;
    /*
        如果filter_result=3时为必填，
        不可以收派的原因代码： 1：收方超范围 2：派方超范围 3：其它原因
     */
    private String remark;
    private String agentMailNumber;         // 代理单号
    private String mappingMark;             // 地址映射码
    private String url;                     // 二维码URL
    private String paymentLink;             // 用于第三方支付运费的 URL
    private List<RlsInfo> rlsInfo;                 // 运单详细信息

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
    }

    public String getReturnTrackingNumber() {
        return returnTrackingNumber;
    }

    public void setReturnTrackingNumber(String returnTrackingNumber) {
        this.returnTrackingNumber = returnTrackingNumber;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestCode() {
        return destCode;
    }

    public void setDestCode(String destCode) {
        this.destCode = destCode;
    }

    public String getFilterResult() {
        return filterResult;
    }

    public void setFilterResult(String filterResult) {
        this.filterResult = filterResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAgentMailNumber() {
        return agentMailNumber;
    }

    public void setAgentMailNumber(String agentMailNumber) {
        this.agentMailNumber = agentMailNumber;
    }

    public String getMappingMark() {
        return mappingMark;
    }

    public void setMappingMark(String mappingMark) {
        this.mappingMark = mappingMark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public List<RlsInfo> getRlsInfo() {
        return rlsInfo;
    }

    public void setRlsInfo(List<RlsInfo> rlsInfo) {
        this.rlsInfo = rlsInfo;
    }
}
