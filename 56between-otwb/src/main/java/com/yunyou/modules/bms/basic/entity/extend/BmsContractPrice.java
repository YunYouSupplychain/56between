package com.yunyou.modules.bms.basic.entity.extend;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class BmsContractPrice {

    private String id;
    // 单价
    private BigDecimal price;
    // 物流点数
    private BigDecimal logisticsPoints;
    // 是否启用阶梯价格
    private String isUseStep;
    // 是否阶梯分段累加方式计算
    private String isAccumulationMethod;
    // 阶梯价格
    private List<BmsContractSteppedPrice> steppedPrices = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLogisticsPoints() {
        return logisticsPoints;
    }

    public void setLogisticsPoints(BigDecimal logisticsPoints) {
        this.logisticsPoints = logisticsPoints;
    }

    public String getIsUseStep() {
        return isUseStep;
    }

    public void setIsUseStep(String isUseStep) {
        this.isUseStep = isUseStep;
    }

    public String getIsAccumulationMethod() {
        return isAccumulationMethod;
    }

    public void setIsAccumulationMethod(String isAccumulationMethod) {
        this.isAccumulationMethod = isAccumulationMethod;
    }

    public List<BmsContractSteppedPrice> getSteppedPrices() {
        return steppedPrices;
    }

    public void setSteppedPrices(List<BmsContractSteppedPrice> steppedPrices) {
        this.steppedPrices = steppedPrices;
    }
}
