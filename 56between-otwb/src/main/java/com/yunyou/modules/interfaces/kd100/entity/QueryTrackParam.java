package com.yunyou.modules.interfaces.kd100.entity;

public class QueryTrackParam {
    /**
     * 查询的快递公司的编码，一律用小写字母
     */
    private String com;
    /**
     * 查询的快递单号， 单号的最大长度是32个字符
     */
    private String num;
    /**
     * 收件人或寄件人的手机号或固话
     */
    private String phone;
    /**
     * 出发地城市，省-市-区
     */
    private String from;
    /**
     * 目的地城市，省-市-区
     */
    private String to;
    /**
     * 添加此字段表示开通行政区域解析功能。0：关闭（默认），1：开通行政区域解析功能，2：开通行政解析功能并且返回出发、目的及当前城市信息
     */
    private String resultv2 = "0";
    /**
     * 返回数据格式。0：json（默认），1：xml，2：html，3：text
     */
    private String show = "0";
    /**
     * 返回结果排序方式。desc：降序（默认），asc：升序
     */
    private String order = "desc";

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getResultv2() {
        return resultv2;
    }

    public void setResultv2(String resultv2) {
        this.resultv2 = resultv2;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
