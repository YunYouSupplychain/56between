package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 出库管理复制方法类
 *
 * @author WMJ
 * @version 2019/02/22
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundDuplicateService {
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 出库单商品明细
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    // 销售订单
    @Autowired
    protected BanQinWmSaleHeaderService wmSaleHeaderService;
    // 销售订单商品明细
    @Autowired
    protected BanQinWmSaleDetailService wmSaleDetailService;

    /**
     * 复制出库单
     * @param soNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage duplicateSoEntity(String soNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 1、复制出库单
        BanQinWmSoEntity soEntity = wmSoHeaderService.findEntityBySoNo(soNo, orgId);
        soEntity.setId(null);
        soEntity.setSoNo(null);
        soEntity.setRecVer(0);
        soEntity.setLogisticNo(null);// 物流单号不复制
        soEntity.setLdStatus(null);// 装车状态不复制
        // 保存
        BanQinWmSoHeader newSoHeader = wmSoHeaderService.saveSoHeader(soEntity);
        // 2、复制出库单明细
        // 出库单明细获取
        List<BanQinWmSoDetailEntity> detailEntityList = wmSoDetailService.findEntityBySoNo(soNo, orgId);
        // 循环
        for (BanQinWmSoDetailEntity detailEntity : detailEntityList) {
            detailEntity.setId(null);
            detailEntity.setRecVer(0);
            detailEntity.setSoNo(newSoHeader.getSoNo());
            detailEntity.setLogisticNo(null);// 物流单号不复制
            detailEntity.setLogisticLineNo(null);// 物流单号不复制
            detailEntity.setLdStatus(null);// 装车状态不复制
            detailEntity.setSaleLineNo(null);
            detailEntity.setSaleNo(null);
            detailEntity.setCdType(null);// 越库类型
            detailEntity.setOldLineNo(null);// 原行号
            // 数量重置为0
            detailEntity.setQtyPreallocEa(0D);// 预配数量
            detailEntity.setQtyAllocEa(0D);// 分配数量
            detailEntity.setQtyPkEa(0D);// 拣货数量
            detailEntity.setQtyShipEa(0D);// 发货数量
            detailEntity.setHeadId(newSoHeader.getId());
            // 复制出库单明细
            wmSoDetailService.saveSoDetail(detailEntity);
        }
        
        msg.setSuccess(true);
        msg.setMessage("操作成功，新单号为：" + newSoHeader.getSoNo());
        msg.setData(newSoHeader);
        return msg;
    }

    /**
     * 复制出库单明细
     * @param soNo
     * @param lineNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage duplicateSoDetailEntity(String soNo, String lineNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 1、复制出库单明细
        BanQinWmSoDetailEntity soDetailEntity = wmSoDetailService.findEntityBySoNoAndLineNo(soNo, lineNo, orgId);
        soDetailEntity.setId(null);
        soDetailEntity.setRecVer(0);
        soDetailEntity.setLdStatus(null);// 装车状态不复制
        soDetailEntity.setSaleLineNo(null);
        soDetailEntity.setSaleNo(null);
        soDetailEntity.setCdType(null);// 越库类型
        soDetailEntity.setOldLineNo(null);// 原行号
        // 数量重置为0
        soDetailEntity.setQtyPreallocEa(0D);// 预配数量
        soDetailEntity.setQtyAllocEa(0D);// 分配数量
        soDetailEntity.setQtyPkEa(0D);// 拣货数量
        soDetailEntity.setQtyShipEa(0D);// 发货数量
        // 保存
        wmSoDetailService.saveSoDetail(soDetailEntity);

        msg.setSuccess(true);
        msg.setMessage("操作成功");
        return msg;
    }

    /**
     * 复制销售订单
     * @param saleNo
     * @return
     */
    @Transactional
    public ResultMessage duplicateSale(String saleNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSaleEntity wmSaleEntity = wmSaleHeaderService.getEntityBySaleNo(saleNo, orgId);
        String newSaleNo = "";
        if (null != wmSaleEntity) {
            // 复制PO单头
            BanQinWmSaleHeader model = new BanQinWmSaleHeader();
            BeanUtils.copyProperties(wmSaleEntity, model);
            model.setId(IdGen.uuid());
            model.setLogisticNo(null);
            msg = wmSaleHeaderService.saveSaleHeader(model);
            model = (BanQinWmSaleHeader) msg.getData();
            newSaleNo = model.getSaleNo();
            if (msg.isSuccess()) {
                // 复制PO明细
                List<BanQinWmSaleDetailEntity> WmSaleDetailList = wmSaleEntity.getWmSaleDetailList();
                for (BanQinWmSaleDetailEntity wmSaleDetailEntity : WmSaleDetailList) {
                    BanQinWmSaleDetail wmSaleDetailModel = new BanQinWmSaleDetail();
                    BeanUtils.copyProperties(wmSaleDetailEntity, wmSaleDetailModel);
                    wmSaleDetailModel.setId(IdGen.uuid());
                    wmSaleDetailModel.setSaleNo(newSaleNo);
                    wmSaleDetailModel.setLogisticLineNo(null);
                    wmSaleDetailModel.setLogisticNo(null);
                    wmSaleDetailService.saveSaleDetail(wmSaleDetailModel);
                }
            }
        }
        msg.setData(wmSaleHeaderService.getEntityBySaleNo(newSaleNo, orgId));
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }
}