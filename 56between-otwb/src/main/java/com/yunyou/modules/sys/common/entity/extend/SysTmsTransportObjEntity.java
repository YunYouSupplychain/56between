package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysTmsTransportObj;

public class SysTmsTransportObjEntity extends SysTmsTransportObj {
    private static final long serialVersionUID = 773928936668397429L;

    private String area;// 所属城市
    private String carrierMatchedOrg;// 承运商对应机构
    private String outletMatchedOrg;// 承运商对应机构
    private String route;// 路线全名称
    private String dataSetName;// 数据套
    private String settleName;// 结算对象名称

    public SysTmsTransportObjEntity() {
    }

    public SysTmsTransportObjEntity(String id) {
        super(id);
    }

    public SysTmsTransportObjEntity(String transportObjCode, String dataSet) {
        super(transportObjCode, dataSet);
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCarrierMatchedOrg() {
        return carrierMatchedOrg;
    }

    public void setCarrierMatchedOrg(String carrierMatchedOrg) {
        this.carrierMatchedOrg = carrierMatchedOrg;
    }

    public String getOutletMatchedOrg() {
        return outletMatchedOrg;
    }

    public void setOutletMatchedOrg(String outletMatchedOrg) {
        this.outletMatchedOrg = outletMatchedOrg;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }
}
