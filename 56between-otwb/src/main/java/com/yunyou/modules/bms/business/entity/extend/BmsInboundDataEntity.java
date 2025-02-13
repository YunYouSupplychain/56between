package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsInboundData;

import java.util.Date;

/**
 * 描述：入库数据扩展实体
 *
 * @author Jianhua
 * @version 2019/6/24
 */
public class BmsInboundDataEntity extends BmsInboundData {

    private static final long serialVersionUID = 5378484548785758440L;
    private String orgName;         // 机构名称
    private Double sumInQty;        // 合计入库总数量
    private Double sumInQtyCs;      // 合计入库总箱数
    private Double sumInQtyPl;      // 合计入库总托数
    private String skuClassName;

    /*额外查询条件*/
    private Date orderDateFm;
    private Date orderDateTo;
    private Date receiveTimeFm;
    private Date receiveTimeTo;

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Double getSumInQty() {
        return sumInQty;
    }

    public void setSumInQty(Double sumInQty) {
        this.sumInQty = sumInQty;
    }

    public Double getSumInQtyCs() {
        return sumInQtyCs;
    }

    public void setSumInQtyCs(Double sumInQtyCs) {
        this.sumInQtyCs = sumInQtyCs;
    }

    public Double getSumInQtyPl() {
        return sumInQtyPl;
    }

    public void setSumInQtyPl(Double sumInQtyPl) {
        this.sumInQtyPl = sumInQtyPl;
    }

    public Date getOrderDateFm() {
        return orderDateFm;
    }

    public void setOrderDateFm(Date orderDateFm) {
        this.orderDateFm = orderDateFm;
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }

    public Date getReceiveTimeFm() {
        return receiveTimeFm;
    }

    public void setReceiveTimeFm(Date receiveTimeFm) {
        this.receiveTimeFm = receiveTimeFm;
    }

    public Date getReceiveTimeTo() {
        return receiveTimeTo;
    }

    public void setReceiveTimeTo(Date receiveTimeTo) {
        this.receiveTimeTo = receiveTimeTo;
    }
}
