package com.yunyou.modules.interfaces.yunda.entity.routeQuery.response;

import java.io.Serializable;
import java.util.Date;

public class YundaRouteQueryStepsResponse implements Serializable {

    private static final long serialVersionUID = -8536045605767100327L;
    // 轨迹产生时间 格式 yyyy-MM-dd HH : mm : ss
    private Date time;
    /**
     *  当前状态
     * GOT	已揽件
     * TRANSIT	运输中
     * SIGNED	已签收
     * RETURN	退回件
     * SIGNFAIL	异常签收
     */
    private String status;
    /**
     * 节点状态
     * ACCEPT	    收件扫描
     * GOT	        揽件扫描
     * ARRIVAL	    入中转
     * DEPARTURE	出中转
     * SENT	        派件中
     * INBOUND	    第三方代收入库
     * SIGNED	    已签收
     * OUTBOUND	    第三方代收快递员取出
     * SIGNFAIL	    签收失败
     * RETURN	    退回件
     * ISSUE	    问题件
     * REJECTION    拒收
     * OTHER	    其他
     * OVERSEA_IN	入库扫描
     * OVERSEA_OUT	出库扫描
     * CLEARANCE_START	    清关开始
     * CLEARANCE_FINISH	    清关结束
     * CLEARANCE_FAIL	    清关失败
     * OVERSEA_ARRIVAL	    干线到达
     * OVERSEA_DEPARTURE	干线离开
     * TRANSFER	            转单
     */
    private String action;
    // 当前城市
    private String city;
    // 下级站点城市
    private String next_city;
    // 轨迹发生站点编码
    private String station;
    // 轨迹发生站点名称
    private String station_name;
    // 轨迹发生站点类型（1是网点，2是分拨中心）
    private String station_type;
    // 下级站点编码
    private String next;
    // 下级站点名称
    private String next_name;
    // 下级站点类型 （1是网点，2是分拨中心）
    private String next_type;
    // 轨迹描述信息
    private String description;
    // 快递员电话
    private String phone;
    // 签收人
    private String signer;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNext_city() {
        return next_city;
    }

    public void setNext_city(String next_city) {
        this.next_city = next_city;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_type() {
        return station_type;
    }

    public void setStation_type(String station_type) {
        this.station_type = station_type;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getNext_name() {
        return next_name;
    }

    public void setNext_name(String next_name) {
        this.next_name = next_name;
    }

    public String getNext_type() {
        return next_type;
    }

    public void setNext_type(String next_type) {
        this.next_type = next_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }
}
