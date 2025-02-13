package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 包裹信息
 */
public class CargoDO implements Serializable {
    private static final long serialVersionUID = 2193465326990375077L;
    // 带电标识 （10/未知 20/带电 30/不带电）
    private String battery;
    // 物品类型（大件、小件、扁平件\文件）
    private String goodsType;
    // 物品名称
    private String goodsName;
    // 物品数量
    private Integer goodsCount;
    // 长（cm）
    private Double spaceX;
    // 宽(cm)
    private Double spaceY;
    // 高(cm)
    private Double spaceZ;
    // 重量(kg)
    private Double weight;
    // 商品金额
    private String goodsAmount;
    // 小包信息（国际专用）
    private List<CargoItemDO> cargoItemList;

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Double getSpaceX() {
        return spaceX;
    }

    public void setSpaceX(Double spaceX) {
        this.spaceX = spaceX;
    }

    public Double getSpaceY() {
        return spaceY;
    }

    public void setSpaceY(Double spaceY) {
        this.spaceY = spaceY;
    }

    public Double getSpaceZ() {
        return spaceZ;
    }

    public void setSpaceZ(Double spaceZ) {
        this.spaceZ = spaceZ;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public List<CargoItemDO> getCargoItemList() {
        return cargoItemList;
    }

    public void setCargoItemList(List<CargoItemDO> cargoItemList) {
        this.cargoItemList = cargoItemList;
    }
}
