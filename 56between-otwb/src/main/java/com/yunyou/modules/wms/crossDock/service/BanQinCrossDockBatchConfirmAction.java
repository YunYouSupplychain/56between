package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.crossDock.entity.BanQinWmAsnDetailReceiveQueryEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量执行越库
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockBatchConfirmAction {
    @Autowired
    protected BanQinCrossDockConfirmService crossDockConfirmService;
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;

    /**
     * 批量执行越库，勾选主列表执行
     */
    public ResultMessage crossDockBatchConfirm(List<BanQinWmAsnDetailReceiveQueryEntity> wmAsnDetailReceiveQueryEntityList) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmAsnDetailReceiveQueryEntity item : wmAsnDetailReceiveQueryEntityList) {
            // 分拨越库
            if (StringUtils.isEmpty(item.getAsnNo()) || item.getCdType().equals(WmsCodeMaster.CD_TYPE_INDIRECT.getCode())) {
                List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntity = wmAsnDetailReceiveService.getEntityByCrossDock(item);
                for (BanQinWmAsnDetailReceiveEntity entity : wmAsnDetailReceiveEntity) {
                    try {
                        crossDockConfirmService.crossDockConfirmByCd(entity.getAsnNo(), entity.getLineNo(), entity.getOrgId());
                    } catch (WarehouseException e) {
                        msg.addMessage(e.getMessage());
                        msg.setSuccess(false);
                    }
                }
            } else {
                // 直接越库
                try {
                    crossDockConfirmService.crossDockConfirmByCd(item.getAsnNo(), item.getRcvLineNo(), item.getOrgId());
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 批量执行越库，明细勾选记录执行
     */
    public ResultMessage crossDockConfirmDetail(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntity, List<BanQinWmSoDetailEntity> wmSoDetailEntity) {
        ResultMessage msg = new ResultMessage();
        // 如果出库单商品明细未勾选，则直接调用按收货明细执行越库
        if (CollectionUtil.isEmpty(wmSoDetailEntity)) {
            for (BanQinWmAsnDetailReceiveEntity entity : wmAsnDetailReceiveEntity) {
                try {
                    crossDockConfirmService.crossDockConfirmByCd(entity.getAsnNo(), entity.getLineNo(), entity.getOrgId());
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        } else {
            // 如果出库单商品明细勾选，
            for (BanQinWmAsnDetailReceiveEntity entity : wmAsnDetailReceiveEntity) {
                try {
                    // 越库收货
                    // 如果完成质检，但是品质不为良品，那么不能越库
                    if (StringUtils.isNotEmpty(entity.getQcStatus()) && entity.getQcStatus().equals(WmsCodeMaster.QC_FULL_QC.getCode())
                            && (StringUtils.isEmpty(entity.getLotAtt04()) || !entity.getLotAtt04().equals(WmsConstants.YES))) {
                        msg.addMessage("[" + entity.getAsnNo() + "][" + entity.getLineNo() + "]质检越库的品质属性必须是良品");
                        continue;
                    }
                    if (entity.getCdType().equals(WmsCodeMaster.CD_TYPE_INDIRECT.getCode())) {
                        // 分拨越库时，则指定明细执行越库
                        // 二次查询，1、过滤数据已过期记录 2、循环操作时，过滤已被更新记录
                        StringBuilder soAndLineNos = new StringBuilder();
                        for (BanQinWmSoDetailEntity soDetail : wmSoDetailEntity) {
                            soAndLineNos.append("('").append(soDetail.getSoNo()).append("','").append(soDetail.getLineNo()).append("'),");
                        }
                        soAndLineNos.deleteCharAt(soAndLineNos.length() - 1);
                        BanQinWmSoDetailByCdQuery soCondition = new BanQinWmSoDetailByCdQuery();
                        soCondition.setOwnerCode(entity.getOwnerCode());
                        soCondition.setSkuCode(entity.getSkuCode());
                        soCondition.setSoAndLineNos(soAndLineNos.toString());
                        soCondition.setCdTypes(new String[] { WmsCodeMaster.CD_TYPE_INDIRECT.getCode() });
                        soCondition.setOrgId(entity.getOrgId());
                        wmSoDetailEntity = wmSoDetailService.getEntityByCdAndSku(soCondition);

                        // 需收货数
                        double qtyOpEa = 0D;
                        double qtyOpUom = 0D;
                        for (BanQinWmSoDetailEntity soDetail : wmSoDetailEntity) {
                            qtyOpEa += wmSoDetailService.getAvailableQtySoEa(soDetail);
                            qtyOpUom += soDetail.getQtySoUom() - soDetail.getQtyPreallocUom() - soDetail.getQtyAllocUom() - soDetail.getQtyPkUom() - soDetail.getQtyShipUom();
                        }
                        if (entity.getQtyPlanEa() > qtyOpEa) {
                            entity.setCurrentQtyRcvEa(qtyOpEa);// 当前收货数
                            entity.setCurrentQtyRcvUom(qtyOpUom);
                        }
                    }
                    crossDockConfirmService.crossDockByIndirect(entity, wmSoDetailEntity);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
        }
        return msg;
    }
}
