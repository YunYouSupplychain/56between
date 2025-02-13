package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 库存周转规则Entity
 */
public class SysWmsRuleRotationDetail extends DataEntity<SysWmsRuleRotationDetail> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "规则编码不能为空")
    private String ruleCode;// 规则编码
    @NotNull(message = "行号不能为空")
    private String lineNo;// 行号
    @NotNull(message = "批次属性不能为空")
    private String lotAtt;// 批次属性
    @NotNull(message = "排序方式不能为空")
    private String orderBy;// 排序方式（A升序，D降序，E精确匹配）
    private String headerId;// 表头Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String lotAttName;

    public SysWmsRuleRotationDetail() {
        super();
    }

    public SysWmsRuleRotationDetail(String id) {
        super(id);
    }

    public SysWmsRuleRotationDetail(String headerId, String dataSet) {
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

    public String getLotAtt() {
        return lotAtt;
    }

    public void setLotAtt(String lotAtt) {
        this.lotAtt = lotAtt;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    public String getLotAttName() {
        return lotAttName;
    }

    public void setLotAttName(String lotAttName) {
        this.lotAttName = lotAttName;
    }
}