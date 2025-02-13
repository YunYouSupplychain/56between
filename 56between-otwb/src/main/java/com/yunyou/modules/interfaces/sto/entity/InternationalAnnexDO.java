package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 国际订单附属信息（国际订单必填）
 */
public class InternationalAnnexDO implements Serializable {
    private static final long serialVersionUID = 6328182914021533105L;
    // 国际业务类型（01-国际进口，02-国际保税，03-国际直邮)
    private String internationalProductType;
    // 是否报关，默认为否
    private boolean customsDeclaration = false;
    // 发件人所在国家，国际件为必填字段
    private String senderCountry;
    // 收件人所在国家，国际件为必填字段
    private String receiverCountry;

    public String getInternationalProductType() {
        return internationalProductType;
    }

    public void setInternationalProductType(String internationalProductType) {
        this.internationalProductType = internationalProductType;
    }

    public boolean isCustomsDeclaration() {
        return customsDeclaration;
    }

    public void setCustomsDeclaration(boolean customsDeclaration) {
        this.customsDeclaration = customsDeclaration;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }
}
