package com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response;

public class RlsDetail {

    private String waybillNumber;           // 电子运单号
    private String sourceTransferCode;      // 原寄地中转场
    private String sourceCityCode;          // 原寄地城市代码
    private String sourceDeptCode;          // 原寄地网点代码
    private String sourceTeamCode;          // 原寄地单元区域
    private String destCityCode;            // 目的地城市代码
    private String destDeptCode;            // 目的地网点代码
    private String destDeptCodeMapping;     // 目的地网点代码映射码
    private String destTeamCode;            // 目的地单元区域
    private String destTeamCodeMapping;     // 目的地单元区域映射码
    private String destTransferCode;        // 目的地中转场
    /*
        打单时的路由标签信息
        如果是大网的路由标签，这里的值是目的地网点代码，
        如果是同城配的路由标签，这里的值是根据同城配的设置映射出来的值，不同的配置结果会不一样，不能根据-符号切分
            （如：上海同城配，可能是：集散点-目的地网点-接驳点，也有可能是目的地网点代码-集散点-接驳点）
     */
    private String destRouteLabel;          // 打单时的路由标签信息
    private String proName;                 // 产品名称
    private String cargoTypeCode;           // 快件内容 如：C816、SP601
    private String limitTypeCode;           // 时效代码 如：T4
    private String expressTypeCode;         // 产品类型 如：B1
    private String codingMapping;           // 入港映射码
    private String codingMappingOut;        // 出港映射码
    private String xbFlag;                  // XB标志 0:不需要打印XB 1:需要打印XB
    /*
        打印标志
        返回值总共有9位，每一位只有0和1两种，0表示按丰密运单默认的规则，1表示显示
        顺序如下，1：寄方姓名 2：寄方电话 3：寄方公司名 4：寄方地址 5：重量 6：收方姓名 7：收方电话 8：收方公司名 9：收方地址
        如 111110000 表示打印寄方姓名、寄方电话、寄方公司名、寄方地址和重量，而收方姓名、收方电话、收方公司名和收方地址按丰密运单默认规则
     */
    private String printFlag;
    private String twoDimensionCode;        // 二维码
    private String proCode;                 // 时效类型: 值为二维码中的K4
    private String printIcon;               // 打印图标
    private String abFlag;                  // AB标
    private String errMsg;                  // 查询出现异常时返回信息
    private String destPortCode;            // 目的地口岸代码
    private String destCountry;             // 目的国别
    private String destPostCode;            // 目的地邮编
    private String goodsValueTotal;         // 总价值(保留两位小数，数字类型，可补位)
    private String currencySymbol;          // 币种
    private String goodsNumber;             // 件数

    private String proIcon;
    private String fileIcon;
    private String fbaIcon;
    private String icsmIcon;

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public String getSourceTransferCode() {
        return sourceTransferCode;
    }

    public void setSourceTransferCode(String sourceTransferCode) {
        this.sourceTransferCode = sourceTransferCode;
    }

    public String getSourceCityCode() {
        return sourceCityCode;
    }

    public void setSourceCityCode(String sourceCityCode) {
        this.sourceCityCode = sourceCityCode;
    }

    public String getSourceDeptCode() {
        return sourceDeptCode;
    }

    public void setSourceDeptCode(String sourceDeptCode) {
        this.sourceDeptCode = sourceDeptCode;
    }

    public String getSourceTeamCode() {
        return sourceTeamCode;
    }

    public void setSourceTeamCode(String sourceTeamCode) {
        this.sourceTeamCode = sourceTeamCode;
    }

    public String getDestCityCode() {
        return destCityCode;
    }

    public void setDestCityCode(String destCityCode) {
        this.destCityCode = destCityCode;
    }

    public String getDestDeptCode() {
        return destDeptCode;
    }

    public void setDestDeptCode(String destDeptCode) {
        this.destDeptCode = destDeptCode;
    }

    public String getDestDeptCodeMapping() {
        return destDeptCodeMapping;
    }

    public void setDestDeptCodeMapping(String destDeptCodeMapping) {
        this.destDeptCodeMapping = destDeptCodeMapping;
    }

    public String getDestTeamCode() {
        return destTeamCode;
    }

    public void setDestTeamCode(String destTeamCode) {
        this.destTeamCode = destTeamCode;
    }

    public String getDestTeamCodeMapping() {
        return destTeamCodeMapping;
    }

    public void setDestTeamCodeMapping(String destTeamCodeMapping) {
        this.destTeamCodeMapping = destTeamCodeMapping;
    }

    public String getDestTransferCode() {
        return destTransferCode;
    }

    public void setDestTransferCode(String destTransferCode) {
        this.destTransferCode = destTransferCode;
    }

    public String getDestRouteLabel() {
        return destRouteLabel;
    }

    public void setDestRouteLabel(String destRouteLabel) {
        this.destRouteLabel = destRouteLabel;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getCargoTypeCode() {
        return cargoTypeCode;
    }

    public void setCargoTypeCode(String cargoTypeCode) {
        this.cargoTypeCode = cargoTypeCode;
    }

    public String getLimitTypeCode() {
        return limitTypeCode;
    }

    public void setLimitTypeCode(String limitTypeCode) {
        this.limitTypeCode = limitTypeCode;
    }

    public String getExpressTypeCode() {
        return expressTypeCode;
    }

    public void setExpressTypeCode(String expressTypeCode) {
        this.expressTypeCode = expressTypeCode;
    }

    public String getCodingMapping() {
        return codingMapping;
    }

    public void setCodingMapping(String codingMapping) {
        this.codingMapping = codingMapping;
    }

    public String getCodingMappingOut() {
        return codingMappingOut;
    }

    public void setCodingMappingOut(String codingMappingOut) {
        this.codingMappingOut = codingMappingOut;
    }

    public String getXbFlag() {
        return xbFlag;
    }

    public void setXbFlag(String xbFlag) {
        this.xbFlag = xbFlag;
    }

    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public String getTwoDimensionCode() {
        return twoDimensionCode;
    }

    public void setTwoDimensionCode(String twoDimensionCode) {
        this.twoDimensionCode = twoDimensionCode;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getPrintIcon() {
        return printIcon;
    }

    public void setPrintIcon(String printIcon) {
        this.printIcon = printIcon;
    }

    public String getAbFlag() {
        return abFlag;
    }

    public void setAbFlag(String abFlag) {
        this.abFlag = abFlag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getDestPortCode() {
        return destPortCode;
    }

    public void setDestPortCode(String destPortCode) {
        this.destPortCode = destPortCode;
    }

    public String getDestCountry() {
        return destCountry;
    }

    public void setDestCountry(String destCountry) {
        this.destCountry = destCountry;
    }

    public String getDestPostCode() {
        return destPostCode;
    }

    public void setDestPostCode(String destPostCode) {
        this.destPostCode = destPostCode;
    }

    public String getGoodsValueTotal() {
        return goodsValueTotal;
    }

    public void setGoodsValueTotal(String goodsValueTotal) {
        this.goodsValueTotal = goodsValueTotal;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getProIcon() {
        return proIcon;
    }

    public void setProIcon(String proIcon) {
        this.proIcon = proIcon;
    }

    public String getFileIcon() {
        return fileIcon;
    }

    public void setFileIcon(String fileIcon) {
        this.fileIcon = fileIcon;
    }

    public String getFbaIcon() {
        return fbaIcon;
    }

    public void setFbaIcon(String fbaIcon) {
        this.fbaIcon = fbaIcon;
    }

    public String getIcsmIcon() {
        return icsmIcon;
    }

    public void setIcsmIcon(String icsmIcon) {
        this.icsmIcon = icsmIcon;
    }
}
