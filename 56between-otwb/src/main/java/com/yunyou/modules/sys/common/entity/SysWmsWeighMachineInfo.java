package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 称重设备表Entity
 */
public class SysWmsWeighMachineInfo extends DataEntity<SysWmsWeighMachineInfo> {

    private static final long serialVersionUID = 1L;
    private String machineNo;// 设备编码
    private String orgId;// 机构ID
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称

    public SysWmsWeighMachineInfo() {
        super();
    }

    public SysWmsWeighMachineInfo(String id) {
        super(id);
    }

    public SysWmsWeighMachineInfo(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}