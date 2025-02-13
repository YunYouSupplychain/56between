package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.kit.entity.BanQinWmKitStep;

/**
 * 描述：加工工序Entity
 * <p>
 * create by Jianhua on 2019/8/21
 */
public class BanQinWmKitStepEntity extends BanQinWmKitStep {
    private static final long serialVersionUID = -4120486042485293564L;
    // 操作数
    private Double qtyOpEa = 0D;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 包装单位换算数量
    private Long uomQty = 1L;
    // 加工人名称
    private String kitOpName;

    public Double getQtyOpEa() {
        return qtyOpEa;
    }

    public void setQtyOpEa(Double qtyOpEa) {
        this.qtyOpEa = qtyOpEa;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Long getUomQty() {
        return uomQty;
    }

    public void setUomQty(Long uomQty) {
        this.uomQty = uomQty;
    }

    public String getKitOpName() {
        return kitOpName;
    }

    public void setKitOpName(String kitOpName) {
        this.kitOpName = kitOpName;
    }
}
