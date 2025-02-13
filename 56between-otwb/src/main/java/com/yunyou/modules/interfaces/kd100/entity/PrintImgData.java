package com.yunyou.modules.interfaces.kd100.entity;

public class PrintImgData {

    private String taskId;

    private String eOrder;

    private String kuaidinum;

    private String kuaidicom;

    private String imgBase64;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String geteOrder() {
        return eOrder;
    }

    public void seteOrder(String eOrder) {
        this.eOrder = eOrder;
    }

    public String getKuaidinum() {
        return kuaidinum;
    }

    public void setKuaidinum(String kuaidinum) {
        this.kuaidinum = kuaidinum;
    }

    public String getKuaidicom() {
        return kuaidicom;
    }

    public void setKuaidicom(String kuaidicom) {
        this.kuaidicom = kuaidicom;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
}
