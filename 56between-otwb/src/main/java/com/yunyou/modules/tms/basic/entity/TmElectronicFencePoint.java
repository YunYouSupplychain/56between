package com.yunyou.modules.tms.basic.entity;

/**
 * 电子围栏坐标点
 *
 * @see TmElectronicFence
 */
public class TmElectronicFencePoint {
    // 主键
    private Integer id;
    // 电子围栏编码
    private String fenceCode;
    // 坐标点经度
    private Double longitude;
    // 坐标点纬度
    private Double latitude;
    // 机构ID
    private String orgId;

    public TmElectronicFencePoint() {
    }

    public TmElectronicFencePoint(String fenceCode, String orgId) {
        this.fenceCode = fenceCode;
        this.orgId = orgId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFenceCode() {
        return fenceCode;
    }

    public void setFenceCode(String fenceCode) {
        this.fenceCode = fenceCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
