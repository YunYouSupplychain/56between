package com.yunyou.modules.wms.report.entity;

import java.io.Serializable;

/**
 * 描述：出库交接清单
 *
 * @author Jianhua on 2020-1-13
 */
public class OutHandoverListLabel implements Serializable {

    private static final long serialVersionUID = -7031497529035365250L;
    private String handoverNo;  // 交接单号
    private String orgId;       // 机构ID
    private String orgName;     // 机构名称
    private String carrierCode; // 承运商编码
    private String carrierName; // 承运商名称
    private String lastPackTime;// 最后一单称重时间（发运时间）
    private Long orderNum;      // 订单数
    private Integer packNum;    // 包裹数
    private Integer count;      // 计数
    // 第一纵队
    private Long oneLineNo;     // 序号
    private String oneOrderNo;  // 订单号
    private String oneTrackingNo;// 快递单号
    // 第二纵队
    private Long twoLineNo;     // 序号
    private String twoOrderNo;  // 订单号
    private String twoTrackingNo;// 快递单号
    // 第三纵队
    private Long threeLineNo;     // 序号
    private String threeOrderNo;  // 订单号
    private String threeTrackingNo;// 快递单号

    public String getHandoverNo() {
        return handoverNo;
    }

    public void setHandoverNo(String handoverNo) {
        this.handoverNo = handoverNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getLastPackTime() {
        return lastPackTime;
    }

    public void setLastPackTime(String lastPackTime) {
        this.lastPackTime = lastPackTime;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getPackNum() {
        return packNum;
    }

    public void setPackNum(Integer packNum) {
        this.packNum = packNum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getOneLineNo() {
        return oneLineNo;
    }

    public void setOneLineNo(Long oneLineNo) {
        this.oneLineNo = oneLineNo;
    }

    public String getOneOrderNo() {
        return oneOrderNo;
    }

    public void setOneOrderNo(String oneOrderNo) {
        this.oneOrderNo = oneOrderNo;
    }

    public String getOneTrackingNo() {
        return oneTrackingNo;
    }

    public void setOneTrackingNo(String oneTrackingNo) {
        this.oneTrackingNo = oneTrackingNo;
    }

    public Long getTwoLineNo() {
        return twoLineNo;
    }

    public void setTwoLineNo(Long twoLineNo) {
        this.twoLineNo = twoLineNo;
    }

    public String getTwoOrderNo() {
        return twoOrderNo;
    }

    public void setTwoOrderNo(String twoOrderNo) {
        this.twoOrderNo = twoOrderNo;
    }

    public String getTwoTrackingNo() {
        return twoTrackingNo;
    }

    public void setTwoTrackingNo(String twoTrackingNo) {
        this.twoTrackingNo = twoTrackingNo;
    }

    public Long getThreeLineNo() {
        return threeLineNo;
    }

    public void setThreeLineNo(Long threeLineNo) {
        this.threeLineNo = threeLineNo;
    }

    public String getThreeOrderNo() {
        return threeOrderNo;
    }

    public void setThreeOrderNo(String threeOrderNo) {
        this.threeOrderNo = threeOrderNo;
    }

    public String getThreeTrackingNo() {
        return threeTrackingNo;
    }

    public void setThreeTrackingNo(String threeTrackingNo) {
        this.threeTrackingNo = threeTrackingNo;
    }
}
