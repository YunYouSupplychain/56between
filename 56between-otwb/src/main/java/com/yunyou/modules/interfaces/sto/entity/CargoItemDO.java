package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 小包信息（国际专用）
 */
public class CargoItemDO implements Serializable {
    private static final long serialVersionUID = -8359057560253906187L;
    // 小包号
    private String serialNumber;
    // 关联单号
    private String referenceNumber;
    // 商品ID
    private String productId;
    // 名称
    private String name;
    // 数量
    private Integer qty;
    // 单价
    private Double unitPrice;
    // 总价
    private Double amount;
    // 币种
    private String currency;
    // 重量(kg)
    private Double weight;
    // 备注
    private String remark;
}
