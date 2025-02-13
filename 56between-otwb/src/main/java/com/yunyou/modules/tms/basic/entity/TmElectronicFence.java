package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 电子围栏
 */
public class TmElectronicFence extends DataEntity<TmElectronicFence> {

    private static final long serialVersionUID = -593982102205583770L;
    private String fenceCode;
    @NotNull(message = "电子围栏名称不能为空")
    private String fenceName;
    /**
     * 电子围栏类型 {@link com.yunyou.modules.tms.common.ElectronicFenceType}
     */
    @NotNull(message = "电子围栏类型不能为空")
    private String fenceType;
    // 电子围栏省份
    private String fenceProvince;
    // 电子围栏城市
    private String fenceCity;
    // 电子围栏区
    private String fenceDistrict;
    // 电子围栏地址
    private String fenceAddress;
    // 机构ID
    @NotNull(message = "机构不能为空")
    private String orgId;
    // 中心点经度
    private Double centerLongitude;
    // 中心点纬度
    private Double centerLatitude;
    // 圆形半径（米）
    private Long radius;
    // 电子围栏坐标点
    private List<TmElectronicFencePoint> pointList;

    // 扩展字段
    private String orgName;

    public TmElectronicFence() {
        super();
    }

    public TmElectronicFence(String id) {
        super(id);
    }

    public TmElectronicFence(String fenceCode, String orgId) {
        this.fenceCode = fenceCode;
        this.orgId = orgId;
    }

    public String getFenceCode() {
        return fenceCode;
    }

    public void setFenceCode(String fenceCode) {
        this.fenceCode = fenceCode;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceType() {
        return fenceType;
    }

    public void setFenceType(String fenceType) {
        this.fenceType = fenceType;
    }

    public String getFenceProvince() {
        return fenceProvince;
    }

    public void setFenceProvince(String fenceProvince) {
        this.fenceProvince = fenceProvince;
    }

    public String getFenceCity() {
        return fenceCity;
    }

    public void setFenceCity(String fenceCity) {
        this.fenceCity = fenceCity;
    }

    public String getFenceDistrict() {
        return fenceDistrict;
    }

    public void setFenceDistrict(String fenceDistrict) {
        this.fenceDistrict = fenceDistrict;
    }

    public String getFenceAddress() {
        return fenceAddress;
    }

    public void setFenceAddress(String fenceAddress) {
        this.fenceAddress = fenceAddress;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public Long getRadius() {
        return radius;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    public List<TmElectronicFencePoint> getPointList() {
        return pointList;
    }

    public void setPointList(List<TmElectronicFencePoint> pointList) {
        this.pointList = pointList;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
