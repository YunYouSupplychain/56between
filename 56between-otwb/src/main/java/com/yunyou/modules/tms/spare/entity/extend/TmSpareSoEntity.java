package com.yunyou.modules.tms.spare.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.spare.entity.TmSpareSoHeader;

import java.util.Date;
import java.util.List;

public class TmSpareSoEntity extends TmSpareSoHeader {
    private static final long serialVersionUID = -7066239359683995868L;

    private List<TmSpareSoDetailEntity> tmSpareSoDetailList = Lists.newArrayList();
    private List<TmSpareSoScanInfoEntity> tmSpareSoScanInfoList = Lists.newArrayList();
    private Date orderTimeFm;
    private Date orderTimeTo;

    public TmSpareSoEntity() {
    }

    public TmSpareSoEntity(String id) {
        super(id);
    }

    public TmSpareSoEntity(String spareSoNo, String orgId) {
        super(spareSoNo, orgId);
    }

    public List<TmSpareSoDetailEntity> getTmSpareSoDetailList() {
        return tmSpareSoDetailList;
    }

    public void setTmSpareSoDetailList(List<TmSpareSoDetailEntity> tmSpareSoDetailList) {
        this.tmSpareSoDetailList = tmSpareSoDetailList;
    }

    public List<TmSpareSoScanInfoEntity> getTmSpareSoScanInfoList() {
        return tmSpareSoScanInfoList;
    }

    public void setTmSpareSoScanInfoList(List<TmSpareSoScanInfoEntity> tmSpareSoScanInfoList) {
        this.tmSpareSoScanInfoList = tmSpareSoScanInfoList;
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
