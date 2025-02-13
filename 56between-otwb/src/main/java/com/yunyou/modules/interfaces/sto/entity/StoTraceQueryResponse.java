package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 物流轨迹查询Response
 */
public class StoTraceQueryResponse implements Serializable {
    private static final long serialVersionUID = -8586548554030356114L;

    private String waybillNo;
    private List<CommonResultVO> trackList;

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public List<CommonResultVO> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<CommonResultVO> trackList) {
        this.trackList = trackList;
    }
}
