package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.crossDock.entity.BanQinWmAsnDetailReceiveQueryEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量删除越库任务(取消ASN与SO的绑定关系)
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockBatchRemoveAction {
    @Autowired
    protected BanQinCrossDockRemoveService crossDockRemoveService;

    /**
     * 删除越库任务
     */
    public ResultMessage crossDockRemoveByDirect(List<BanQinWmAsnDetailReceiveQueryEntity> wmAsnDetailReceiveQueryEntityList) {
        ResultMessage msg = new ResultMessage();
        // 选中记录中的分拨越库任务
        List<BanQinWmAsnDetailReceiveQueryEntity> indirectItems = new ArrayList<>();
        for (BanQinWmAsnDetailReceiveQueryEntity item : wmAsnDetailReceiveQueryEntityList) {
            try {
                // 分拨越库不能删除
                if (item.getCdType().equals(WmsCodeMaster.CD_TYPE_INDIRECT.getCode())) {
                    indirectItems.add(item);
                    continue;
                }
                if (!item.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
                    msg.addMessage(item.getAsnNo() + "不是创建状态不能操作");
                    msg.setSuccess(false);
                } else {
                    crossDockRemoveService.crossDockRemoveByDirect(item.getAsnNo(), item.getRcvLineNo(), item.getSoNo(), item.getSoLineNo(), item.getOrgId());
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (indirectItems.size() > 0) {
            msg.addMessage("选中记录中的分拨越库任务，不能操作");
            msg.setSuccess(false);
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 取消越库标记
     */
    public ResultMessage crossDockRemove(List<BanQinWmAsnDetailReceiveQueryEntity> wmAsnDetailReceiveQueryEntityList) {
        ResultMessage msg = new ResultMessage();
        // 选中记录中的直接越库任务
        List<BanQinWmAsnDetailReceiveQueryEntity> directItems = new ArrayList<>();
        for (BanQinWmAsnDetailReceiveQueryEntity item : wmAsnDetailReceiveQueryEntityList) {
            try {
                // 分拨越库不能删除
                if (item.getCdType().equals(WmsCodeMaster.CD_TYPE_DIRECT.getCode())) {
                    directItems.add(item);
                    continue;
                }
                if (!item.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
                    msg.addMessage(item.getAsnNo() + "不是创建状态不能操作");
                    msg.setSuccess(false);
                } else {
                    crossDockRemoveService.crossDockRemoveByIndirect(item.getOwnerCode(), item.getSkuCode(), item.getOrgId());
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (directItems.size() > 0) {
            msg.addMessage("选中记录中的直接越库任务，不能操作");
            msg.setSuccess(false);
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 取消越库标记（明细）
     */
    public ResultMessage cancelRemarkDetail(List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys, List<BanQinWmSoDetailEntity> soDetailEntitys) {
        ResultMessage msg = new ResultMessage();
        if (CollectionUtil.isNotEmpty(asnReceiveEntitys)) {
            for (BanQinWmAsnDetailReceiveEntity asnReceiveEntity : asnReceiveEntitys) {
                try {
                    crossDockRemoveService.crossDockRemoveAsn(asnReceiveEntity.getAsnNo(), asnReceiveEntity.getLineNo(), asnReceiveEntity.getOrgId());
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(soDetailEntitys)) {
            for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
                Double qtyOpEa = soDetailEntity.getQtyAllocEa() + soDetailEntity.getQtyPkEa() + soDetailEntity.getQtyShipEa();
                if (soDetailEntity.getQtySoEa() > qtyOpEa) {
                    // 如果订货数>已操作数，那么表示商品明细创建/部分状态，可取消标记/拆分取消
                    try {
                        crossDockRemoveService.crossDockRemoveSo(soDetailEntity.getSoNo(), soDetailEntity.getLineNo(), soDetailEntity.getQtySoEa() - qtyOpEa, soDetailEntity.getOrgId());
                    } catch (WarehouseException e) {
                        msg.addMessage(e.getMessage());
                        msg.setSuccess(false);
                    }
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }

}
