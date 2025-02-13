package com.yunyou.modules.bms.calculate;

import com.yunyou.modules.bms.calculate.business.BmsCalcBusinessData;
import com.yunyou.modules.bms.calculate.contract.BmsCalcContractData;

/**
 * 费用计算数据
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsCalcData {

    /**
     * 结算批次号
     */
    private String settleLotNo;
    /**
     * 合同数据
     */
    private BmsCalcContractData contractData;
    /**
     * 业务数据
     */
    private BmsCalcBusinessData businessData;
    /**
     * 公式变量参数值
     */
    private BmsCalcProcessObj formulaParam001;
    private BmsCalcProcessObj formulaParam002;
    private BmsCalcProcessObj formulaParam003;
    private BmsCalcProcessObj formulaParam004;
    private BmsCalcProcessObj formulaParam005;
    private BmsCalcProcessObj formulaParam006;
    private BmsCalcProcessObj formulaParam007;
    private BmsCalcProcessObj formulaParam008;

    public String getSettleLotNo() {
        return settleLotNo;
    }

    public void setSettleLotNo(String settleLotNo) {
        this.settleLotNo = settleLotNo;
    }

    public BmsCalcContractData getContractData() {
        return contractData;
    }

    public void setContractData(BmsCalcContractData contractData) {
        this.contractData = contractData;
    }

    public BmsCalcBusinessData getBusinessData() {
        return businessData;
    }

    public void setBusinessData(BmsCalcBusinessData businessData) {
        this.businessData = businessData;
    }

    public BmsCalcProcessObj getFormulaParam001() {
        return formulaParam001;
    }

    public void setFormulaParam001(BmsCalcProcessObj formulaParam001) {
        this.formulaParam001 = formulaParam001;
    }

    public BmsCalcProcessObj getFormulaParam002() {
        return formulaParam002;
    }

    public void setFormulaParam002(BmsCalcProcessObj formulaParam002) {
        this.formulaParam002 = formulaParam002;
    }

    public BmsCalcProcessObj getFormulaParam003() {
        return formulaParam003;
    }

    public void setFormulaParam003(BmsCalcProcessObj formulaParam003) {
        this.formulaParam003 = formulaParam003;
    }

    public BmsCalcProcessObj getFormulaParam004() {
        return formulaParam004;
    }

    public void setFormulaParam004(BmsCalcProcessObj formulaParam004) {
        this.formulaParam004 = formulaParam004;
    }

    public BmsCalcProcessObj getFormulaParam005() {
        return formulaParam005;
    }

    public void setFormulaParam005(BmsCalcProcessObj formulaParam005) {
        this.formulaParam005 = formulaParam005;
    }

    public BmsCalcProcessObj getFormulaParam006() {
        return formulaParam006;
    }

    public void setFormulaParam006(BmsCalcProcessObj formulaParam006) {
        this.formulaParam006 = formulaParam006;
    }

    public BmsCalcProcessObj getFormulaParam007() {
        return formulaParam007;
    }

    public void setFormulaParam007(BmsCalcProcessObj formulaParam007) {
        this.formulaParam007 = formulaParam007;
    }

    public BmsCalcProcessObj getFormulaParam008() {
        return formulaParam008;
    }

    public void setFormulaParam008(BmsCalcProcessObj formulaParam008) {
        this.formulaParam008 = formulaParam008;
    }

}
