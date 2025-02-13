package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysWmsWeighMachineInfo;

/**
 * 称重设备表Entity
 */
public class SysWmsWeighMachineInfoEntity extends SysWmsWeighMachineInfo {

    private String orgName;// 机构名称
    private String dataSetName;// 数据套名称

    public SysWmsWeighMachineInfoEntity() {
        super();
    }

    public SysWmsWeighMachineInfoEntity(String id) {
        super(id);
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}