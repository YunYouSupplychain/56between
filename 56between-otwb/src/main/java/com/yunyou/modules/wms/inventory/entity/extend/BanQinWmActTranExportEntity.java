package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTran;

import java.util.Date;

/**
 * 库存交易导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmActTranExportEntity extends BanQinWmActTran {

    @Override
    @ExcelField(title = "交易ID", align = 2, sort = 1)
    public String getTranId() {
        return super.getTranId();
    }

    @Override
    @ExcelField(title = "交易类型", align = 2, sort = 2, dictType = "SYS_WM_TRAN_TYPE")
    public String getTranType() {
        return super.getTranType();
    }

    @Override
    @ExcelField(title = "单据号", align = 2, sort = 3)
    public String getOrderNo() {
        return super.getOrderNo();
    }

    @Override
    @ExcelField(title = "单据行号", align = 2, sort = 4)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "操作人", align = 2, sort = 5)
    public String getTranOp() {
        return super.getTranOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "交易时间", align = 2, sort = 6)
    public Date getTranTime() {
        return super.getTranTime();
    }

    @Override
    @ExcelField(title = "源货主编码", align = 2, sort = 7)
    public String getFmOwner() {
        return super.getFmOwner();
    }

    @Override
    @ExcelField(title = "源货主名称", align = 2, sort = 8)
    public String getFmOwnerName() {
        return super.getFmOwnerName();
    }

    @Override
    @ExcelField(title = "源商品编码", align = 2, sort = 9)
    public String getFmSku() {
        return super.getFmSku();
    }

    @Override
    @ExcelField(title = "源商品名称", align = 2, sort = 10)
    public String getFmSkuName() {
        return super.getFmSkuName();
    }

    @Override
    @ExcelField(title = "源批次", align = 2, sort = 11)
    public String getFmLot() {
        return super.getFmLot();
    }

    @Override
    @ExcelField(title = "源库位", align = 2, sort = 12)
    public String getFmLoc() {
        return super.getFmLoc();
    }

    @Override
    @ExcelField(title = "源跟踪号", align = 2, sort = 13)
    public String getFmId() {
        return super.getFmId();
    }

    @Override
    @ExcelField(title = "源包装规格", align = 2, sort = 14)
    public String getFmPack() {
        return super.getFmPack();
    }

    @Override
    @ExcelField(title = "源单位", align = 2, sort = 15)
    public String getFmUom() {
        return super.getFmUom();
    }

    @Override
    @ExcelField(title = "源操作数量(增量)", align = 2, sort = 16)
    public Double getFmQtyUomOp() {
        return super.getFmQtyUomOp();
    }

    @Override
    @ExcelField(title = "源操作数量EA(增量)", align = 2, sort = 17)
    public Double getFmQtyEaOp() {
        return super.getFmQtyEaOp();
    }

    @Override
    @ExcelField(title = "源操作前库存数", align = 2, sort = 18)
    public Double getFmQtyEaBefore() {
        return super.getFmQtyEaBefore();
    }

    @Override
    @ExcelField(title = "源操作后库存数", align = 2, sort = 19)
    public Double getFmQtyEaAfter() {
        return super.getFmQtyEaAfter();
    }

    @Override
    @ExcelField(title = "目标货主编码", align = 2, sort = 20)
    public String getToOwner() {
        return super.getToOwner();
    }

    @Override
    @ExcelField(title = "目标货主名称", align = 2, sort = 21)
    public String getToOwnerName() {
        return super.getToOwnerName();
    }

    @Override
    @ExcelField(title = "目标商品编码", align = 2, sort = 22)
    public String getToSku() {
        return super.getToSku();
    }

    @Override
    @ExcelField(title = "目标商品名称", align = 2, sort = 23)
    public String getToSkuName() {
        return super.getToSkuName();
    }

    @Override
    @ExcelField(title = "目标批次", align = 2, sort = 24)
    public String getToLot() {
        return super.getToLot();
    }

    @Override
    @ExcelField(title = "目标库位", align = 2, sort = 25)
    public String getToLoc() {
        return super.getToLoc();
    }

    @Override
    @ExcelField(title = "目标跟踪号", align = 2, sort = 26)
    public String getToId() {
        return super.getToId();
    }

    @Override
    @ExcelField(title = "目标包装规格", align = 2, sort = 27)
    public String getToPack() {
        return super.getToPack();
    }

    @Override
    @ExcelField(title = "目标单位", align = 2, sort = 28)
    public String getToUom() {
        return super.getToUom();
    }

    @Override
    @ExcelField(title = "目标操作数量(增量)", align = 2, sort = 29)
    public Double getToQtyUomOp() {
        return super.getToQtyUomOp();
    }

    @Override
    @ExcelField(title = "目标操作数量EA(增量)", align = 2, sort = 30)
    public Double getToQtyEaOp() {
        return super.getToQtyEaOp();
    }

    @Override
    @ExcelField(title = "目标操作前库存数", align = 2, sort = 31)
    public Double getToQtyEaBefore() {
        return super.getToQtyEaBefore();
    }

    @Override
    @ExcelField(title = "目标操作后库存数", align = 2, sort = 32)
    public Double getToQtyEaAfter() {
        return super.getToQtyEaAfter();
    }

    @Override
    @ExcelField(title = "任务ID", align = 2, sort = 33)
    public String getTaskId() {
        return super.getTaskId();
    }

    @Override
    @ExcelField(title = "任务行号", align = 2, sort = 34)
    public String getTaskLineNo() {
        return super.getTaskLineNo();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "生产日期", align = 2, sort = 35)
    public Date getLotAtt01() {
        return super.getLotAtt01();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "失效日期", align = 2, sort = 36)
    public Date getLotAtt02() {
        return super.getLotAtt02();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "入库日期", align = 2, sort = 37)
    public Date getLotAtt03() {
        return super.getLotAtt03();
    }

    @Override
    @ExcelField(title = "批次属性4", align = 2, sort = 38)
    public String getLotAtt04() {
        return super.getLotAtt04();
    }

    @Override
    @ExcelField(title = "批次属性5", align = 2, sort = 39)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @Override
    @ExcelField(title = "批次属性6", align = 2, sort = 40)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @Override
    @ExcelField(title = "批次属性7", align = 2, sort = 41)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @Override
    @ExcelField(title = "批次属性8", align = 2, sort = 42)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @Override
    @ExcelField(title = "批次属性9", align = 2, sort = 43)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @Override
    @ExcelField(title = "批次属性10", align = 2, sort = 44)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @Override
    @ExcelField(title = "批次属性11", align = 2, sort = 45)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @Override
    @ExcelField(title = "批次属性12", align = 2, sort = 46)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "目标生产日期", align = 2, sort = 47)
    public Date getTlotAtt01() {
        return super.getTlotAtt01();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "目标失效日期", align = 2, sort = 48)
    public Date getTlotAtt02() {
        return super.getTlotAtt02();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "目标入库日期", align = 2, sort = 49)
    public Date getTlotAtt03() {
        return super.getTlotAtt03();
    }

    @Override
    @ExcelField(title = "目标批次属性4", align = 2, sort = 50)
    public String getTlotAtt04() {
        return super.getTlotAtt04();
    }

    @Override
    @ExcelField(title = "目标批次属性5", align = 2, sort = 51)
    public String getTlotAtt05() {
        return super.getTlotAtt05();
    }

    @Override
    @ExcelField(title = "目标批次属性6", align = 2, sort = 52)
    public String getTlotAtt06() {
        return super.getTlotAtt06();
    }

    @Override
    @ExcelField(title = "目标批次属性7", align = 2, sort = 53)
    public String getTlotAtt07() {
        return super.getTlotAtt07();
    }

    @Override
    @ExcelField(title = "目标批次属性8", align = 2, sort = 54)
    public String getTlotAtt08() {
        return super.getTlotAtt08();
    }

    @Override
    @ExcelField(title = "目标批次属性9", align = 2, sort = 55)
    public String getTlotAtt09() {
        return super.getTlotAtt09();
    }

    @Override
    @ExcelField(title = "目标批次属性10", align = 2, sort = 56)
    public String getTlotAtt10() {
        return super.getTlotAtt10();
    }

    @Override
    @ExcelField(title = "目标批次属性11", align = 2, sort = 57)
    public String getTlotAtt11() {
        return super.getTlotAtt11();
    }

    @Override
    @ExcelField(title = "目标批次属性12", align = 2, sort = 58)
    public String getTlotAtt12() {
        return super.getTlotAtt12();
    }

    @Override
    @ExcelField(title = "仓库", align = 2, sort = 59)
    public String getOrgName() {
        return super.getOrgName();
    }
}
