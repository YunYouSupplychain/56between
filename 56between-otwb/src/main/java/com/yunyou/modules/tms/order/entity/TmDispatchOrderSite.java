package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 派车单配送点Entity
 *
 * @author liujianhua
 * @version 2020-03-11
 */
public class TmDispatchOrderSite extends DataEntity<TmDispatchOrderSite> {

    private static final long serialVersionUID = 1L;
    private String dispatchNo;        // 派车单号
    private Integer dispatchSeq;        // 配送顺序
    private String outletCode;        // 配送网点编码
    private String receiveShip;        // 提货(R)/送货(S)
    private String orgId;        // 机构ID
    private String baseOrgId;   // 基础数据机构ID

    public TmDispatchOrderSite() {
        super();
    }

    public TmDispatchOrderSite(String id) {
        super(id);
    }

    public TmDispatchOrderSite(String dispatchNo, String orgId) {
        this.dispatchNo = dispatchNo;
        this.orgId = orgId;
    }

    @ExcelField(title = "派车单号", align = 2, sort = 7)
    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    @ExcelField(title = "配送顺序", align = 2, sort = 8)
    public Integer getDispatchSeq() {
        return dispatchSeq;
    }

    public void setDispatchSeq(Integer dispatchSeq) {
        this.dispatchSeq = dispatchSeq;
    }

    @ExcelField(title = "配送网点编码", align = 2, sort = 9)
    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    @ExcelField(title = "收/发", dictType = "TMS_RECEIVE_SHIP", align = 2, sort = 10)
    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 11)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}