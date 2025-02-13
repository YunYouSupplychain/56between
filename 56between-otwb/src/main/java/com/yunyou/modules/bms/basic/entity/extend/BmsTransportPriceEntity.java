package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsTransportPrice;

/**
 * 描述：运输价格扩展实体
 *
 * @author liujianhua
 * @version 2019-11-14
 */
public class BmsTransportPriceEntity extends BmsTransportPrice {

    private static final long serialVersionUID = -2472409718928891385L;
    /**
     * 起点名称
     */
    private String startPlaceName;
    /**
     * 终点名称
     */
    private String endPlaceName;
    /**
     * 区域名称
     */
    private String regionName;
    /**
     * 车型
     */
    private String carType;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 运输价格体系编码
     */
    private String transportGroupCode;

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTransportGroupCode() {
        return transportGroupCode;
    }

    public void setTransportGroupCode(String transportGroupCode) {
        this.transportGroupCode = transportGroupCode;
    }
}
