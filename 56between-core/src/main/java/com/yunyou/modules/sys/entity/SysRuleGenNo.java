package com.yunyou.modules.sys.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.sys.GenNoType;

/**
 * 编号生成规则Entity
 *
 * @author 张庆生
 * @version 2017-10-17
 */
public class SysRuleGenNo extends DataEntity<SysRuleGenNo> {

    private static final long serialVersionUID = 1L;
    /**
     * 编号类型{@link GenNoType}
     */
    private String billType;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 时间戳
     */
    private String stamp;
    /**
     * 规则清空周期
     */
    private String clearCycle;
    /**
     * 流水长度
     */
    private Integer serialNoMaxLength;
    /**
     * 当前流水号
     */
    private Integer currentSerialNo;

    public SysRuleGenNo() {
        super();
        this.setIdType(IDTYPE_AUTO);
    }

    public SysRuleGenNo(String id) {
        super(id);
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getClearCycle() {
        return clearCycle;
    }

    public void setClearCycle(String clearCycle) {
        this.clearCycle = clearCycle;
    }

    public Integer getSerialNoMaxLength() {
        return serialNoMaxLength;
    }

    public void setSerialNoMaxLength(Integer serialNoMaxLength) {
        this.serialNoMaxLength = serialNoMaxLength;
    }

    public Integer getCurrentSerialNo() {
        return currentSerialNo;
    }

    public void setCurrentSerialNo(Integer currentSerialNo) {
        this.currentSerialNo = currentSerialNo;
    }
}