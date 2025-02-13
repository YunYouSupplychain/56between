package com.yunyou.modules.bms.finance.entity;

import com.yunyou.core.persistence.TreeEntity;

import java.math.BigDecimal;

/**
 * 计算过程节点Entity
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsCalculateProcessNode extends TreeEntity<BmsCalculateProcessNode> {

    private BigDecimal value;

    public BmsCalculateProcessNode(){
        super();
        this.setIdType(IDTYPE_AUTO);
        this.sort = 0;
    }

    public BmsCalculateProcessNode(String id){
        super(id);
    }

    public BmsCalculateProcessNode(BigDecimal value, String remarks) {
        this.value = value;
        this.remarks = remarks;
    }

    @Override
    public BmsCalculateProcessNode getParent() {
        return parent;
    }

    @Override
    public void setParent(BmsCalculateProcessNode parent) {
        this.parent = parent;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
