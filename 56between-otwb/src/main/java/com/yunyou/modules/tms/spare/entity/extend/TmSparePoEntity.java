package com.yunyou.modules.tms.spare.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.spare.entity.TmSparePoHeader;

import java.util.Date;
import java.util.List;

public class TmSparePoEntity extends TmSparePoHeader {
    private static final long serialVersionUID = -7522436221972134089L;

    private List<TmSparePoDetailEntity> tmSparePoDetailList = Lists.newArrayList();
    private List<TmSparePoScanInfoEntity> tmSparePoScanInfoList = Lists.newArrayList();
    private Date orderTimeFm;
    private Date orderTimeTo;

    public TmSparePoEntity() {
    }

    public TmSparePoEntity(String id) {
        super(id);
    }

    public TmSparePoEntity(String sparePoNo, String orgId) {
        super(sparePoNo, orgId);
    }

    public List<TmSparePoDetailEntity> getTmSparePoDetailList() {
        return tmSparePoDetailList;
    }

    public void setTmSparePoDetailList(List<TmSparePoDetailEntity> tmSparePoDetailList) {
        this.tmSparePoDetailList = tmSparePoDetailList;
    }

    public List<TmSparePoScanInfoEntity> getTmSparePoScanInfoList() {
        return tmSparePoScanInfoList;
    }

    public void setTmSparePoScanInfoList(List<TmSparePoScanInfoEntity> tmSparePoScanInfoList) {
        this.tmSparePoScanInfoList = tmSparePoScanInfoList;
    }

    public Date getOrderTimeFm() {
        return orderTimeFm;
    }

    public void setOrderTimeFm(Date orderTimeFm) {
        this.orderTimeFm = orderTimeFm;
    }

    public Date getOrderTimeTo() {
        return orderTimeTo;
    }

    public void setOrderTimeTo(Date orderTimeTo) {
        this.orderTimeTo = orderTimeTo;
    }
}
