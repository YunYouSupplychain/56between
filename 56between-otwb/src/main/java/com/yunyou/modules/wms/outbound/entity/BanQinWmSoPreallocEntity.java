package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;
import java.util.List;

/**
 * 描述：预配明细扩展Entity
 *
 * @auther: Jianhua on 2019/2/14
 */
public class BanQinWmSoPreallocEntity extends BanQinWmSoPrealloc {
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 商品快速录入码
    private String skuQuickCode;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 批次属性1(生产日期)
    private Date lotAtt01;
    // 批次属性2(失效日期)
    private Date lotAtt02;
    // 批次属性3(入库日期)
    private Date lotAtt03;
    // 批次属性4
    private String lotAtt04;
    // 批次属性5
    private String lotAtt05;
    // 批次属性6
    private String lotAtt06;
    // 批次属性7
    private String lotAtt07;
    // 批次属性8
    private String lotAtt08;
    // 批次属性9
    private String lotAtt09;
    // 批次属性10
    private String lotAtt10;
    // 批次属性11
    private String lotAtt11;
    // 批次属性12
    private String lotAtt12;
    
    List<String> waveNos;
    List<String> soNos;
    List<String> preallocIds;
    private String lineNos;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuQuickCode() {
        return skuQuickCode;
    }

    public void setSkuQuickCode(String skuQuickCode) {
        this.skuQuickCode = skuQuickCode;
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

    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    public List<String> getWaveNos() {
        return waveNos;
    }

    public void setWaveNos(List<String> waveNos) {
        this.waveNos = waveNos;
    }

    public List<String> getSoNos() {
        return soNos;
    }

    public void setSoNos(List<String> soNos) {
        this.soNos = soNos;
    }

    public List<String> getPreallocIds() {
        return preallocIds;
    }

    public void setPreallocIds(List<String> preallocIds) {
        this.preallocIds = preallocIds;
    }

    public String getLineNos() {
        return lineNos;
    }

    public void setLineNos(String lineNos) {
        this.lineNos = lineNos;
    }
}
