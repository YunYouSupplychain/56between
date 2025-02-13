package com.yunyou.modules.print.entity;

public class SfExpressBillPrintEntity {

    /* logo头部 */
    private String printNum;    // 打印次数

    /* 条码区 */
    private String proCode;     // 时效类型（值应与图片名称相同）
    private String mailNo;      // 运单号
    /* 条码区一票多件字段 */
    private String noType;      // 子母单类型（母单号/子单号）
    private String numSign;     // 件数标识

    /* 收方信息 */
    private String destRouteLabel;      // 目的地路由信息（若为空则为目的地城市代码）
    private String receiveMan;          // 收件人名称
    private String receiveManPhone;     // 收件人电话
    private String receiveManAddress;   // 收件人地址
    private String destTeamCode;        // 单元区域编码（可为空）
    private String codFlag;             // 代收货款标识（如有代收货款业务则为1）

    /* 主运单区其他信息 */
    /* 付款方式
         1、寄付月结：显示寄付月结，不显示运费
         2、寄付转第三方：显示寄付转第三方，不显示运费
         3、寄付现结：按实际运费显示
         4、到付：按实际需收取费用显示总计费用（含到付增值服务费，但不含COD费用)
    */
    private String payWay;
    private String codingMapping;       // 进港信息-进港映射码（可为空）
    private String twoDimensionCode;    // 二维码-中转分拣用二维码
    private String abFlag;              // A/B标（可为空）
    private String codingMappingOut;    // 出港信息-出港映射码（可为空）

    /* 寄方信息 */
    private String sendMan;             // 寄件人名称
    private String sendManPhone;        // 寄件人电话
    private String sendManCompany;      // 寄件人公司
    private String sendManAddress;      // 寄件人地址

    /* 附加信息 自定义区 */
    private String addedService;        // 增值服务
    private String cargo;               // 托寄物
    private String cargoQty;            // 件数
    private String billingWeight;       // 计费重量
    private String actualWeight;        // 实际重量
    private String totalAmount;         // 费用合计
    private String remarks;             // 备注
    private String customerOrderNo;     // 客户订单号

    /* 图标区域 */
    private String printIcon1;          // 贴纸信息1
    private String printIcon2;          // 贴纸信息2
    private String podFlag;             //

    private String label;               // 订单/波次

    public String getPrintNum() {
        return printNum;
    }

    public void setPrintNum(String printNum) {
        this.printNum = printNum;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getNoType() {
        return noType;
    }

    public void setNoType(String noType) {
        this.noType = noType;
    }

    public String getNumSign() {
        return numSign;
    }

    public void setNumSign(String numSign) {
        this.numSign = numSign;
    }

    public String getDestRouteLabel() {
        return destRouteLabel;
    }

    public void setDestRouteLabel(String destRouteLabel) {
        this.destRouteLabel = destRouteLabel;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getReceiveManPhone() {
        return receiveManPhone;
    }

    public void setReceiveManPhone(String receiveManPhone) {
        this.receiveManPhone = receiveManPhone;
    }

    public String getReceiveManAddress() {
        return receiveManAddress;
    }

    public void setReceiveManAddress(String receiveManAddress) {
        this.receiveManAddress = receiveManAddress;
    }

    public String getDestTeamCode() {
        return destTeamCode;
    }

    public void setDestTeamCode(String destTeamCode) {
        this.destTeamCode = destTeamCode;
    }

    public String getCodFlag() {
        return codFlag;
    }

    public void setCodFlag(String codFlag) {
        this.codFlag = codFlag;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getCodingMapping() {
        return codingMapping;
    }

    public void setCodingMapping(String codingMapping) {
        this.codingMapping = codingMapping;
    }

    public String getTwoDimensionCode() {
        return twoDimensionCode;
    }

    public void setTwoDimensionCode(String twoDimensionCode) {
        this.twoDimensionCode = twoDimensionCode;
    }

    public String getAbFlag() {
        return abFlag;
    }

    public void setAbFlag(String abFlag) {
        this.abFlag = abFlag;
    }

    public String getCodingMappingOut() {
        return codingMappingOut;
    }

    public void setCodingMappingOut(String codingMappingOut) {
        this.codingMappingOut = codingMappingOut;
    }

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
    }

    public String getSendManPhone() {
        return sendManPhone;
    }

    public void setSendManPhone(String sendManPhone) {
        this.sendManPhone = sendManPhone;
    }

    public String getSendManCompany() {
        return sendManCompany;
    }

    public void setSendManCompany(String sendManCompany) {
        this.sendManCompany = sendManCompany;
    }

    public String getSendManAddress() {
        return sendManAddress;
    }

    public void setSendManAddress(String sendManAddress) {
        this.sendManAddress = sendManAddress;
    }

    public String getAddedService() {
        return addedService;
    }

    public void setAddedService(String addedService) {
        this.addedService = addedService;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCargoQty() {
        return cargoQty;
    }

    public void setCargoQty(String cargoQty) {
        this.cargoQty = cargoQty;
    }

    public String getBillingWeight() {
        return billingWeight;
    }

    public void setBillingWeight(String billingWeight) {
        this.billingWeight = billingWeight;
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPrintIcon1() {
        return printIcon1;
    }

    public void setPrintIcon1(String printIcon1) {
        this.printIcon1 = printIcon1;
    }

    public String getPrintIcon2() {
        return printIcon2;
    }

    public void setPrintIcon2(String printIcon2) {
        this.printIcon2 = printIcon2;
    }

    public String getPodFlag() {
        return podFlag;
    }

    public void setPodFlag(String podFlag) {
        this.podFlag = podFlag;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
