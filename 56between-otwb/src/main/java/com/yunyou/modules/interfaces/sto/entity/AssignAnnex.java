package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 指定网点揽收（非调度业务不传）
 */
public class AssignAnnex implements Serializable {
    private static final long serialVersionUID = 1447879336136969364L;
    // 指定取件的网点编号
    private String takeCompanyCode;
    // 指定取件的业务员编号（指定业务员时takeCompanyCode可传可不传，若传必须传正确，举例：寄件地址是上海，只能是指定上海业务员取件）
    private String takeUserCode;

    public String getTakeCompanyCode() {
        return takeCompanyCode;
    }

    public void setTakeCompanyCode(String takeCompanyCode) {
        this.takeCompanyCode = takeCompanyCode;
    }

    public String getTakeUserCode() {
        return takeUserCode;
    }

    public void setTakeUserCode(String takeUserCode) {
        this.takeUserCode = takeUserCode;
    }
}
