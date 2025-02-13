package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmObjRoute;

public class TmObjRouteEntity extends TmObjRoute {
    private static final long serialVersionUID = 4250791648622839234L;

    private String carrierName;// 承运商名称
    private String startObjName;// 业务对象名称(起点)
    private String endObjName;// 业务对象名称(终点)

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getStartObjName() {
        return startObjName;
    }

    public void setStartObjName(String startObjName) {
        this.startObjName = startObjName;
    }

    public String getEndObjName() {
        return endObjName;
    }

    public void setEndObjName(String endObjName) {
        this.endObjName = endObjName;
    }
}
