package com.yunyou.modules.sys;

public enum AnnexType {
    /**
     * WMS商品附件
     */
    WMS_SKU(1),
    /**
     * WMS入库单附件
     */
    WMS_ASN(2),
    /**
     * WMS出库单附件
     */
    WMS_SO(3);

    private Integer code;

    AnnexType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
