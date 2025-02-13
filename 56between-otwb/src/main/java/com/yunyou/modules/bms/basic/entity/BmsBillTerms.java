package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 计费条款Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
public class BmsBillTerms extends DataEntity<BmsBillTerms> {

    private static final long serialVersionUID = 1L;
    // 计费条款代码
    private String billTermsCode;
    // 计费条款说明
    private String billTermsDesc;
    // 费用模块
    private String billModule;
    // 输出对象
    private String outputObjects;
    // 发生量
    private String occurrenceQuantity;
    // 处理方法名称
    private String methodName;
    // 使用行业
    private String useIndustry;

    public BmsBillTerms() {
        super();
    }

    public BmsBillTerms(String id) {
        super(id);
    }

    public String getBillTermsCode() {
        return billTermsCode;
    }

    public void setBillTermsCode(String billTermsCode) {
        this.billTermsCode = billTermsCode;
    }

    public String getBillTermsDesc() {
        return billTermsDesc;
    }

    public void setBillTermsDesc(String billTermsDesc) {
        this.billTermsDesc = billTermsDesc;
    }

    public String getBillModule() {
        return billModule;
    }

    public void setBillModule(String billModule) {
        this.billModule = billModule;
    }

    public String getOutputObjects() {
        return outputObjects;
    }

    public void setOutputObjects(String outputObjects) {
        this.outputObjects = outputObjects;
    }

    public String getOccurrenceQuantity() {
        return occurrenceQuantity;
    }

    public void setOccurrenceQuantity(String occurrenceQuantity) {
        this.occurrenceQuantity = occurrenceQuantity;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUseIndustry() {
        return useIndustry;
    }

    public void setUseIndustry(String useIndustry) {
        this.useIndustry = useIndustry;
    }
}