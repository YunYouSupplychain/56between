package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 分配规则Entity
 */
public class SysWmsRuleAllocDetail extends DataEntity<SysWmsRuleAllocDetail> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "规则编码不能为空")
    private String ruleCode;// 规则编码
    @NotNull(message = "行号不能为空")
    private String lineNo;// 行号
    @NotNull(message = "包装单位不能为空")
    private String uom;// 包装单位（PL、CS、IP、EA、OT）
    private String locUseType;// 库位使用类型（RS、CS、EA、PC、QC、ST、SS、KT）
    private String skuLocType;// 商品拣货位类型（CS、EA、PC）
    private String isClearFirst;// 是否清仓优先
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsRuleAllocDetail() {
        super();
    }

    public SysWmsRuleAllocDetail(String id) {
        super(id);
    }

    public SysWmsRuleAllocDetail(String headerId, String dataSet) {
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getLocUseType() {
        return locUseType;
    }

    public void setLocUseType(String locUseType) {
        this.locUseType = locUseType;
    }

    public String getSkuLocType() {
        return skuLocType;
    }

    public void setSkuLocType(String skuLocType) {
        this.skuLocType = skuLocType;
    }

    public String getIsClearFirst() {
        return isClearFirst;
    }

    public void setIsClearFirst(String isClearFirst) {
        this.isClearFirst = isClearFirst;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}