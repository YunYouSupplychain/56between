package com.yunyou.modules.oms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 订单补充数据Entity
 *
 * @author ZYF
 * @version 2021-05-30
 */
public class OmOrderAppendix extends DataEntity<OmOrderAppendix> {
    private static final long serialVersionUID = 1L;

    private String orderNo;         // 订单号
    private String orgId;// 机构ID
    private String customerNo;      // 客户订单号
    private String type;
    private String pushSystem;      // 下发系统
    private String pushOrgId;       // 下发机构

    private String def1;    // SMS：大润发门店调入单号           WMS：锅美-记账人编码
    private String def2;    // SMS：大润发接口自定义1            WMS：锅美-记账人名称
    private String def3;    // SMS：大润发接口自定义2            WMS：锅美-发货组织
    private String def4;    // SMS：手工单：文件导入类型         WMS：锅美-客户机构
    private String def5;    // SMS&WMS：大润发-真实门店编码           WMS：锅美-客户订单类型
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;
    private String def11;
    private String def12;
    private String def13;
    private String def14;
    private String def15;
    private String def16;
    private String def17;
    private String def18;
    private String def19;
    private String def20;
    private String def21;
    private String def22;
    private String def23;
    private String def24;
    private String def25;
    private String def26;
    private String def27;
    private String def28;
    private String def29;
    private String def30;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime1;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime2;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime3;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime4;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime5;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime6;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime7;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime8;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime9;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date defTime10;

    public OmOrderAppendix() {
        super();
    }

    public OmOrderAppendix(String id) {
        super(id);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPushSystem() {
        return pushSystem;
    }

    public void setPushSystem(String pushSystem) {
        this.pushSystem = pushSystem;
    }

    public String getPushOrgId() {
        return pushOrgId;
    }

    public void setPushOrgId(String pushOrgId) {
        this.pushOrgId = pushOrgId;
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

    public String getDef11() {
        return def11;
    }

    public void setDef11(String def11) {
        this.def11 = def11;
    }

    public String getDef12() {
        return def12;
    }

    public void setDef12(String def12) {
        this.def12 = def12;
    }

    public String getDef13() {
        return def13;
    }

    public void setDef13(String def13) {
        this.def13 = def13;
    }

    public String getDef14() {
        return def14;
    }

    public void setDef14(String def14) {
        this.def14 = def14;
    }

    public String getDef15() {
        return def15;
    }

    public void setDef15(String def15) {
        this.def15 = def15;
    }

    public String getDef16() {
        return def16;
    }

    public void setDef16(String def16) {
        this.def16 = def16;
    }

    public String getDef17() {
        return def17;
    }

    public void setDef17(String def17) {
        this.def17 = def17;
    }

    public String getDef18() {
        return def18;
    }

    public void setDef18(String def18) {
        this.def18 = def18;
    }

    public String getDef19() {
        return def19;
    }

    public void setDef19(String def19) {
        this.def19 = def19;
    }

    public String getDef20() {
        return def20;
    }

    public void setDef20(String def20) {
        this.def20 = def20;
    }

    public String getDef21() {
        return def21;
    }

    public void setDef21(String def21) {
        this.def21 = def21;
    }

    public String getDef22() {
        return def22;
    }

    public void setDef22(String def22) {
        this.def22 = def22;
    }

    public String getDef23() {
        return def23;
    }

    public void setDef23(String def23) {
        this.def23 = def23;
    }

    public String getDef24() {
        return def24;
    }

    public void setDef24(String def24) {
        this.def24 = def24;
    }

    public String getDef25() {
        return def25;
    }

    public void setDef25(String def25) {
        this.def25 = def25;
    }

    public String getDef26() {
        return def26;
    }

    public void setDef26(String def26) {
        this.def26 = def26;
    }

    public String getDef27() {
        return def27;
    }

    public void setDef27(String def27) {
        this.def27 = def27;
    }

    public String getDef28() {
        return def28;
    }

    public void setDef28(String def28) {
        this.def28 = def28;
    }

    public String getDef29() {
        return def29;
    }

    public void setDef29(String def29) {
        this.def29 = def29;
    }

    public String getDef30() {
        return def30;
    }

    public void setDef30(String def30) {
        this.def30 = def30;
    }

    public Date getDefTime1() {
        return defTime1;
    }

    public void setDefTime1(Date defTime1) {
        this.defTime1 = defTime1;
    }

    public Date getDefTime2() {
        return defTime2;
    }

    public void setDefTime2(Date defTime2) {
        this.defTime2 = defTime2;
    }

    public Date getDefTime3() {
        return defTime3;
    }

    public void setDefTime3(Date defTime3) {
        this.defTime3 = defTime3;
    }

    public Date getDefTime4() {
        return defTime4;
    }

    public void setDefTime4(Date defTime4) {
        this.defTime4 = defTime4;
    }

    public Date getDefTime5() {
        return defTime5;
    }

    public void setDefTime5(Date defTime5) {
        this.defTime5 = defTime5;
    }

    public Date getDefTime6() {
        return defTime6;
    }

    public void setDefTime6(Date defTime6) {
        this.defTime6 = defTime6;
    }

    public Date getDefTime7() {
        return defTime7;
    }

    public void setDefTime7(Date defTime7) {
        this.defTime7 = defTime7;
    }

    public Date getDefTime8() {
        return defTime8;
    }

    public void setDefTime8(Date defTime8) {
        this.defTime8 = defTime8;
    }

    public Date getDefTime9() {
        return defTime9;
    }

    public void setDefTime9(Date defTime9) {
        this.defTime9 = defTime9;
    }

    public Date getDefTime10() {
        return defTime10;
    }

    public void setDefTime10(Date defTime10) {
        this.defTime10 = defTime10;
    }
}