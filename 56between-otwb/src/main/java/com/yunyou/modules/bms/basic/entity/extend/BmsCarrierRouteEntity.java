package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;

/**
 * 描述：承运商路由扩展实体
 *
 * @author Jianhua
 * @version 2019/7/23
 */
public class BmsCarrierRouteEntity extends BmsCarrierRoute {

    private static final long serialVersionUID = -7180368410122481251L;
    private String carrierName;// 承运商名称
    private String startAreaName;
    private String endAreaName;
    private String orgName;// 机构名称

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getStartAreaName() {
        return startAreaName;
    }

    public void setStartAreaName(String startAreaName) {
        this.startAreaName = startAreaName;
    }

    public String getEndAreaName() {
        return endAreaName;
    }

    public void setEndAreaName(String endAreaName) {
        this.endAreaName = endAreaName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
