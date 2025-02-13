package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysWmsSkuLoc;

/**
 * 商品拣货位entity
 */
public class SysWmsSkuLocEntity extends SysWmsSkuLoc {
    private static final long serialVersionUID = 1L;

	private String cdprDesc;
	private String paSeq;
	private String packCode;
	private String rotationRule;
	private String rotationType;
	private Double qtyAvail;
	private Double qty;
	private Double qtyPaIn;
	private Double qtyMvIn;
	private Double qtyRpIn;

    public String getCdprDesc() {
        return cdprDesc;
    }

    public void setCdprDesc(String cdprDesc) {
        this.cdprDesc = cdprDesc;
    }

    public String getPaSeq() {
        return paSeq;
    }

    public void setPaSeq(String paSeq) {
        this.paSeq = paSeq;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getRotationRule() {
        return rotationRule;
    }

    public void setRotationRule(String rotationRule) {
        this.rotationRule = rotationRule;
    }

    public String getRotationType() {
        return rotationType;
    }

    public void setRotationType(String rotationType) {
        this.rotationType = rotationType;
    }

    public Double getQtyAvail() {
        return qtyAvail;
    }

    public void setQtyAvail(Double qtyAvail) {
        this.qtyAvail = qtyAvail;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQtyPaIn() {
        return qtyPaIn;
    }

    public void setQtyPaIn(Double qtyPaIn) {
        this.qtyPaIn = qtyPaIn;
    }

    public Double getQtyMvIn() {
        return qtyMvIn;
    }

    public void setQtyMvIn(Double qtyMvIn) {
        this.qtyMvIn = qtyMvIn;
    }

    public Double getQtyRpIn() {
        return qtyRpIn;
    }

    public void setQtyRpIn(Double qtyRpIn) {
        this.qtyRpIn = qtyRpIn;
    }
}
