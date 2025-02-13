package com.yunyou.modules.interfaces.yunda.entity.createOrder.response;

public class YundaCreateOrderResponseData {

    private String order_serial_no;
    private String mail_no;
    private String pdf_info;
    private String status;
    private String msg;

    public String getOrder_serial_no() {
        return order_serial_no;
    }

    public void setOrder_serial_no(String order_serial_no) {
        this.order_serial_no = order_serial_no;
    }

    public String getMail_no() {
        return mail_no;
    }

    public void setMail_no(String mail_no) {
        this.mail_no = mail_no;
    }

    public String getPdf_info() {
        return pdf_info;
    }

    public void setPdf_info(String pdf_info) {
        this.pdf_info = pdf_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
