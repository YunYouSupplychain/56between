package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.List;

public class WMSRF_SC_QueryLot_Response implements Serializable {
    // 批次属性1(生产日期)
    private String lotAtt01;
    // 批次属性2(失效日期)
    private String lotAtt02;
    // 批次属性3(入库日期)
    private String lotAtt03;
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
    // 批次属性配置
    private List<WMSRF_CF_ProductionLotConfig> lotConfigs;

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public void setLotAtt03(String lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    public void setLotConfigs(List<WMSRF_CF_ProductionLotConfig> lotConfigs) {
        this.lotConfigs = lotConfigs;
    }
}
