package com.yunyou.modules.interfaces.zto.entity;

/**
 * 中通路由信息response
 * @author WMJ
 * @version 2020-05-08
 */
public class ZtoGetTraceInfoResponse {
    // 路由详细描述
    private String desc;
    // 派件或收件员
    private String dispOrRecMan;
    // 派件或收件员编号
    private String dispOrRecManCode;
    // 派件或收件员电话
    private String dispOrRecManPhone;
    // 扫描网点是否中心("T" or "F")
    private String isCenter;
    // 上一站或下一站城市
    private String preOrNextCity;
    // 上一站或下一站省份
    private String preOrNextProv;
    // 上一站或下一站网点
    private String preOrNextSite;
    // 上一站或下一站网点编号
    private String preOrNextSiteCode;
    // 上一站或下一站网点联系方式
    private String preOrNextSitePhone;
    // 备注
    private String remark;
    // 扫描网点所在城市
    private String scanCity;
    // 扫描时间
    private String scanDate;
    // 扫描网点所在省份
    private String scanProv;
    // 扫描网点
    private String scanSite;
    // 扫描网点编号
    private String scanSiteCode;
    // 扫描网点联系方式
    private String scanSitePhone;
    // 扫描类型(1.收件/2.发件/3.到件/4.派件/5.签收/6.第三方签收/7.退件/8.退件签收/9.问题件/10.ARRIVAL [表示快件被第三方代理点接收]/11.SIGNED [表示收件人已从第三方代理点签收])
    private String scanType;
    // 签收人
    private String signMan;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDispOrRecMan() {
        return dispOrRecMan;
    }

    public void setDispOrRecMan(String dispOrRecMan) {
        this.dispOrRecMan = dispOrRecMan;
    }

    public String getDispOrRecManCode() {
        return dispOrRecManCode;
    }

    public void setDispOrRecManCode(String dispOrRecManCode) {
        this.dispOrRecManCode = dispOrRecManCode;
    }

    public String getDispOrRecManPhone() {
        return dispOrRecManPhone;
    }

    public void setDispOrRecManPhone(String dispOrRecManPhone) {
        this.dispOrRecManPhone = dispOrRecManPhone;
    }

    public String getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(String isCenter) {
        this.isCenter = isCenter;
    }

    public String getPreOrNextCity() {
        return preOrNextCity;
    }

    public void setPreOrNextCity(String preOrNextCity) {
        this.preOrNextCity = preOrNextCity;
    }

    public String getPreOrNextProv() {
        return preOrNextProv;
    }

    public void setPreOrNextProv(String preOrNextProv) {
        this.preOrNextProv = preOrNextProv;
    }

    public String getPreOrNextSite() {
        return preOrNextSite;
    }

    public void setPreOrNextSite(String preOrNextSite) {
        this.preOrNextSite = preOrNextSite;
    }

    public String getPreOrNextSiteCode() {
        return preOrNextSiteCode;
    }

    public void setPreOrNextSiteCode(String preOrNextSiteCode) {
        this.preOrNextSiteCode = preOrNextSiteCode;
    }

    public String getPreOrNextSitePhone() {
        return preOrNextSitePhone;
    }

    public void setPreOrNextSitePhone(String preOrNextSitePhone) {
        this.preOrNextSitePhone = preOrNextSitePhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScanCity() {
        return scanCity;
    }

    public void setScanCity(String scanCity) {
        this.scanCity = scanCity;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getScanProv() {
        return scanProv;
    }

    public void setScanProv(String scanProv) {
        this.scanProv = scanProv;
    }

    public String getScanSite() {
        return scanSite;
    }

    public void setScanSite(String scanSite) {
        this.scanSite = scanSite;
    }

    public String getScanSiteCode() {
        return scanSiteCode;
    }

    public void setScanSiteCode(String scanSiteCode) {
        this.scanSiteCode = scanSiteCode;
    }

    public String getScanSitePhone() {
        return scanSitePhone;
    }

    public void setScanSitePhone(String scanSitePhone) {
        this.scanSitePhone = scanSitePhone;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getSignMan() {
        return signMan;
    }

    public void setSignMan(String signMan) {
        this.signMan = signMan;
    }
}
