package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmDispatchOrderSite;

/**
 * 派车单配送点Entity
 *
 * @author zyf
 * @version 2020-03-18
 */
public class TmDispatchOrderSiteEntity extends TmDispatchOrderSite {
    private static final long serialVersionUID = -406576789994878075L;

    private String outletName;        // 配送网点名称
    private String outletContact;     // 配送网点联系人
    private String outletPhone;       // 配送网点联系电话
    private String outletAddress;     // 配送网点地址
    private String outletType;        // 配送网点类型
    private Long labelQty = 0L;  // 标签数量
    private Long transportOrderQty = 0L;// 运输订单数量

    public TmDispatchOrderSiteEntity() {
        super();
    }

    public TmDispatchOrderSiteEntity(String id) {
        super(id);
    }

    public TmDispatchOrderSiteEntity(String dispatchNo, String orgId) {
        super(dispatchNo, orgId);
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletContact() {
        return outletContact;
    }

    public void setOutletContact(String outletContact) {
        this.outletContact = outletContact;
    }

    public String getOutletPhone() {
        return outletPhone;
    }

    public void setOutletPhone(String outletPhone) {
        this.outletPhone = outletPhone;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public Long getLabelQty() {
        return labelQty;
    }

    public void setLabelQty(Long labelQty) {
        this.labelQty = labelQty;
    }

    public Long getTransportOrderQty() {
        return transportOrderQty;
    }

    public void setTransportOrderQty(Long transportOrderQty) {
        this.transportOrderQty = transportOrderQty;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }
}