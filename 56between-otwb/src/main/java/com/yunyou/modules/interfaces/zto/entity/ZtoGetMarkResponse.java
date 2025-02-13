package com.yunyou.modules.interfaces.zto.entity;

/**
 * 中通获取集装地大头笔response
 * @author WMJ
 * @version 2020-05-07
 */
public class ZtoGetMarkResponse {
    private boolean status;
    private String statusCode;
    private String message;
    private Result result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        // 大头笔
        private String bagAddr;
        // 集包地
        private String mark;

        public String getBagAddr() {
            return bagAddr;
        }

        public void setBagAddr(String bagAddr) {
            this.bagAddr = bagAddr;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }
    }

}
