package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 车辆配件Entity
 */
public class SysTmsVehiclePart extends DataEntity<SysTmsVehiclePart> {

    private static final long serialVersionUID = 1L;
    private String carNo;// 车牌号
    private String partNo;// 配件编号
    private String partName;// 配件名称
    private Long partNumber;// 配件数量
    private String dataSet;// 数据套

    public SysTmsVehiclePart() {
        super();
    }

    public SysTmsVehiclePart(String id) {
        super(id);
    }

    public SysTmsVehiclePart(String carNo, String dataSet) {
        this.carNo = carNo;
        this.dataSet = dataSet;
    }

    public SysTmsVehiclePart(String carNo, String partNo, String dataSet) {
        this.carNo = carNo;
        this.partNo = partNo;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "车牌号", align = 2, sort = 7)
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    @ExcelField(title = "配件编号", align = 2, sort = 8)
    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    @ExcelField(title = "配件名称", align = 2, sort = 9)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @ExcelField(title = "配件数量", align = 2, sort = 10)
    public Long getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Long partNumber) {
        this.partNumber = partNumber;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}