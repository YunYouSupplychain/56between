package com.yunyou.modules.bms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 运输价格档案清单
 */
public class BmsTransportPriceFileList extends DataEntity<BmsTransportPriceFileList> {
    private static final long serialVersionUID = -488503039172771474L;
    // 结算对象编码
    private String settleObjectCode;
    @ExcelField(title = "结算对象", type = 1)
    private String settleObjectName;
    @ExcelField(title = "车型", dictType = "TMS_CAR_TYPE", type = 1)
    private String carType;
    // 费用科目编码
    private String billSubjectCode;
    @ExcelField(title = "费用科目", type = 1)
    private String billSubjectName;
    @ExcelField(title = "阶梯范围从", type = 1)
    private Double fm;
    @ExcelField(title = "阶梯范围到", type = 1)
    private Double to;
    @ExcelField(title = "单价", type = 1)
    private Double price;
    // 机构ID
    private String orgId;

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public Double getFm() {
        return fm;
    }

    public void setFm(Double fm) {
        this.fm = fm;
    }

    public Double getTo() {
        return to;
    }

    public void setTo(Double to) {
        this.to = to;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
