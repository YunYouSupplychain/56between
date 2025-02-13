package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 运输订单明细信息Entity
 *
 * @author liujianhua
 * @version 2020-03-04
 */
public class  TmTransportOrderSku extends DataEntity<TmTransportOrderSku> {

    private static final long serialVersionUID = 1L;
    private String transportNo;// 运输单号
    private String lineNo;// 行号
    private String ownerCode;// 货主编码
    private String skuCode;// 物料编码
    private Double qty;// 数量
    private Double weight;// 重量
    private Double cubic;// 体积
    private String orgId;// 机构ID
    private String baseOrgId;// 基础数据机构ID
    private String lotInfo; // 批次信息
    private Double eaQuantity; // 件-换算比例
    private Double ipQuantity; // 小包装-换算比例
    private Double csQuantity; // 箱-换算比例
    private Double plQuantity; // 托-换算比例
    private Double otQuantity; // 大包装-换算比例
    // 自定义
    private String def1; // 栈板号
    private String def2; // 框号
    private String def3; // 温层
    private String def4; // 科别
    private String def5; // 品类
    private String def6; // 发货箱数
    private String def7; // 包装单位
    private String def8; // 订单数量
    private String def9;
    private String def10;// 皮重

    public TmTransportOrderSku() {
        super();
    }

    public TmTransportOrderSku(String id) {
        super(id);
    }

    public TmTransportOrderSku(String transportNo, String orgId) {
        this.transportNo = transportNo;
        this.orgId = orgId;
    }

    public TmTransportOrderSku(String transportNo, String lineNo, String orgId) {
        this.transportNo = transportNo;
        this.lineNo = lineNo;
        this.orgId = orgId;
    }

    @ExcelField(title = "运输单号", align = 2, sort = 7)
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    @ExcelField(title = "物料编码", align = 2, sort = 8)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "数量", align = 2, sort = 9)
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    @ExcelField(title = "重量", align = 2, sort = 10)
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @ExcelField(title = "体积", align = 2, sort = 11)
    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 12)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }

    public String getLotInfo() {
        return lotInfo;
    }

    public void setLotInfo(String lotInfo) {
        this.lotInfo = lotInfo;
    }

    public Double getEaQuantity() {
        return eaQuantity;
    }

    public void setEaQuantity(Double eaQuantity) {
        this.eaQuantity = eaQuantity;
    }

    public Double getIpQuantity() {
        return ipQuantity;
    }

    public void setIpQuantity(Double ipQuantity) {
        this.ipQuantity = ipQuantity;
    }

    public Double getCsQuantity() {
        return csQuantity;
    }

    public void setCsQuantity(Double csQuantity) {
        this.csQuantity = csQuantity;
    }

    public Double getPlQuantity() {
        return plQuantity;
    }

    public void setPlQuantity(Double plQuantity) {
        this.plQuantity = plQuantity;
    }

    public Double getOtQuantity() {
        return otQuantity;
    }

    public void setOtQuantity(Double otQuantity) {
        this.otQuantity = otQuantity;
    }
}