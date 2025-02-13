package com.yunyou.modules.interfaces.yto.entity.createOrder.response;

public class YTOCreateOrderResponseDistributeInfo {

    // 三段码
    private String shortAddress;
    // 末端网点代码
    private String consigneeBranchCode;
    // 集包地中心代码
    private String packageCenterCode;
    // 集包地中心名称
    private String packageCenterName;
    //
    private String printKeyWord;

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public String getConsigneeBranchCode() {
        return consigneeBranchCode;
    }

    public void setConsigneeBranchCode(String consigneeBranchCode) {
        this.consigneeBranchCode = consigneeBranchCode;
    }

    public String getPackageCenterCode() {
        return packageCenterCode;
    }

    public void setPackageCenterCode(String packageCenterCode) {
        this.packageCenterCode = packageCenterCode;
    }

    public String getPackageCenterName() {
        return packageCenterName;
    }

    public void setPackageCenterName(String packageCenterName) {
        this.packageCenterName = packageCenterName;
    }

    public String getPrintKeyWord() {
        return printKeyWord;
    }

    public void setPrintKeyWord(String printKeyWord) {
        this.printKeyWord = printKeyWord;
    }
}
