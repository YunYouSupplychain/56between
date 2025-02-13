package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinReceivingOperationService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 执行越库任务
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockConfirmService {
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinReceivingOperationService receivingOperationService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    protected BanQinOutboundManualAllocService outboundManualAllocService;
    @Autowired
    protected BanQinOutboundPickingService outboundPickingService;
    @Autowired
    protected BanQinOutboundShipmentService outboundShipmentService;
    @Autowired
    protected BanQinWmsCommonService wmsCommonService;
    @Autowired
    protected BanQinOutboundDuplicateService outboundDuplicateService;
    @Autowired
    protected BanQinWmAsnDetailService wmAsnDetailService;
    @Autowired
    protected SynchronizedNoService noService;
    @Autowired
    protected BanQinCrossDockCreateTaskService crossDockCreateTaskService;

    /**
     * 执行越库任务 (越库任务界面)
     */
    @Transactional
    public ResultMessage crossDockConfirmByCd(String asnNo, String rcvLineNo, String orgId) throws WarehouseException {
        // 获取asn收货明细
        BanQinWmAsnDetailReceiveEntity asnReceiveEntity = wmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(asnNo, rcvLineNo, orgId);
        if (!asnReceiveEntity.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
            throw new WarehouseException("[" + asnNo + "][" + rcvLineNo + "]不是创建状态，不能操作");
        }
        // 如果完成质检，但是品质不为良品，那么不能越库
        if (StringUtils.isNotEmpty(asnReceiveEntity.getQcStatus()) && asnReceiveEntity.getQcStatus().equals(WmsCodeMaster.QC_FULL_QC.getCode())
                && (StringUtils.isEmpty(asnReceiveEntity.getLotAtt04()) || !asnReceiveEntity.getLotAtt04().equals(WmsConstants.YES))) {
            throw new WarehouseException("[" + asnNo + "][" + rcvLineNo + "]质检越库的品质属性必须是良品");
        }
        // 按收货明细执行越库
        return crossDockConfirmByAsn(asnReceiveEntity);
    }

    /**
     * 预收货通知 收货确认
     */
    @Transactional
    public ResultMessage crossDockConfirmByAsn(BanQinWmAsnDetailReceiveEntity asnReceiveEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        if (asnReceiveEntity.getCdType().equals(WmsCodeMaster.CD_TYPE_DIRECT.getCode())) {
            // 直接越库 收货明细与SO商品明细 1-1
            // 获取SO商品明细
            BanQinWmSoDetailEntity soDetailEntity = wmSoDetailService.findEntityBySoNoAndLineNo(asnReceiveEntity.getSoNo(), asnReceiveEntity.getSoLineNo(), asnReceiveEntity.getOrgId());
            if (StringUtils.isEmpty(soDetailEntity.getId())) {
                throw new WarehouseException("查询不到出库单[" + asnReceiveEntity.getSoNo() + "]行[" + asnReceiveEntity.getSoLineNo() + "]");
            }
            // 执行越库
            crossDockByDirect(asnReceiveEntity, soDetailEntity);
        } else if (asnReceiveEntity.getCdType().equals(WmsCodeMaster.CD_TYPE_INDIRECT.getCode())) {
            // 分拨越库 存在未分配有越库标记的记录都可以进行越库分配,按预计出货时间到升序、优先级升序、订单创建时间升序 收货明细与SO商品明细
            BanQinWmSoDetailByCdQuery soCondition = new BanQinWmSoDetailByCdQuery();
            soCondition.setOwnerCode(asnReceiveEntity.getOwnerCode());
            soCondition.setSkuCode(asnReceiveEntity.getSkuCode());
            soCondition.setCdTypes(new String[] { WmsCodeMaster.CD_TYPE_INDIRECT.getCode() });
            soCondition.setOrgId(asnReceiveEntity.getOrgId());
            List<BanQinWmSoDetailEntity> soDetailEntitys = wmSoDetailService.getEntityByCdAndSku(soCondition);
            if (soDetailEntitys.size() == 0) {
                throw new WarehouseException("货主[" + asnReceiveEntity.getOwnerCode() + "]商品[" + asnReceiveEntity.getSkuCode() + "]没有可分拨越库的出库单明细");
            }
            // 需收货数
            double qtyOpEa = 0D;
            double qtyOpUom = 0D;
            for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
                qtyOpEa += wmSoDetailService.getAvailableQtySoEa(soDetailEntity);
                qtyOpUom += soDetailEntity.getQtySoUom() - soDetailEntity.getQtyPreallocUom() - soDetailEntity.getQtyAllocUom() - soDetailEntity.getQtyPkUom() - soDetailEntity.getQtyShipUom();
            }
            if (asnReceiveEntity.getQtyPlanEa() > qtyOpEa) {
                asnReceiveEntity.setCurrentQtyRcvEa(qtyOpEa);// 当前收货数
                asnReceiveEntity.setCurrentQtyRcvUom(qtyOpUom);
            }
            // 执行越库
            crossDockByIndirect(asnReceiveEntity, soDetailEntitys);
        } else {
            throw new WarehouseException("[" + asnReceiveEntity.getAsnNo() + "][", asnReceiveEntity.getLineNo() + "]不属于越库，不能操作");
        }

        return msg;
    }

    /**
     * 直接越库 越库收货+分配/拣货/发货 (预收货通知界面，收货确认)
     */
    @Transactional
    public ResultMessage crossDockByDirect(BanQinWmAsnDetailReceiveEntity asnReceiveEntity, BanQinWmSoDetailEntity soDetailEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 入库状态校验
        if (!asnReceiveEntity.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
            throw new WarehouseException("[" + asnReceiveEntity.getAsnNo() + "][" + asnReceiveEntity.getLineNo() + "]不是创建状态，不能操作");
        }
        // 出库状态校验
        double qtyOp = soDetailEntity.getQtySoEa() - soDetailEntity.getQtyAllocEa() - soDetailEntity.getQtyPkEa() - soDetailEntity.getQtyShipEa();
        if (qtyOp == 0) {
            throw new WarehouseException("[" + soDetailEntity.getSoNo() + "][" + soDetailEntity.getLineNo() + "]非创建、非部分操作状态，不能操作");
        }
        // 收货，默认收货到CROSSDOCK
        asnReceiveEntity = receive(asnReceiveEntity);
        // 出库手工分配->拣货确认 分配数=收货数
        BanQinWmSoAllocEntity wmSoAllocEntity = manualAlloc(asnReceiveEntity, soDetailEntity, asnReceiveEntity.getQtyRcvEa(), WmsCodeMaster.CD_TYPE_DIRECT.getCode());
        // 如果是直接越库
        if (wmSoAllocEntity.getCdOutStep().equals(WmsCodeMaster.RCV_ALLOC_PK.getCode())) {
            // 拣货确认
            wmSoAllocEntity.setQtyPkEa(wmSoAllocEntity.getQtyEa());// 拣货数
            wmSoAllocEntity.setQtyPkUom(wmSoAllocEntity.getQtyUom());
            wmSoAllocEntity.setToLoc(WmsConstants.CROSSDOCK);// 目标拣货库位默认为CROSSDOCK
            wmSoAllocEntity.setToId(wmSoAllocEntity.getTraceId());// 目标跟踪号=收货跟踪号=分配跟踪号
            wmSoAllocEntity.setCdOutStep(WmsCodeMaster.RCV_ALLOC_PK.getCode());// 越库步骤
            // 收货+分配+拣货
            outboundPickingService.outboundPicking(wmSoAllocEntity);
        } else if (wmSoAllocEntity.getCdOutStep().equals(WmsCodeMaster.RCV_ALLOC_PK_SP.getCode())) {
            // 拣货确认
            wmSoAllocEntity.setQtyPkEa(wmSoAllocEntity.getQtyEa());// 拣货数
            wmSoAllocEntity.setQtyPkUom(wmSoAllocEntity.getQtyUom());
            wmSoAllocEntity.setToLoc(WmsConstants.CROSSDOCK);// 目标拣货库位默认为CROSSDOCK
            wmSoAllocEntity.setToId(wmSoAllocEntity.getTraceId());// 目标跟踪号=收货跟踪号=分配跟踪号
            wmSoAllocEntity.setCdOutStep(WmsCodeMaster.RCV_ALLOC_PK_SP.getCode());// 越库步骤
            // =
            // 收货+分配+拣货+发货
            outboundPickingService.outboundPicking(wmSoAllocEntity);
            // 发货确认
            outboundShipmentService.outboundShipment(wmSoAllocEntity);
        }
        return msg;
    }

    /**
     * 分拨越库 越库收货+分配 (预收货通知，收货确认)
     */
    @Transactional
    public ResultMessage crossDockByIndirect(BanQinWmAsnDetailReceiveEntity asnReceiveEntity, List<BanQinWmSoDetailEntity> soDetailEntitys) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 入库状态校验
        if (!asnReceiveEntity.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
            throw new WarehouseException("[" + asnReceiveEntity.getAsnNo() + "][" + asnReceiveEntity.getLineNo() + "]不是创建状态，不能操作");
        }
        // 收货，默认收货到CROSSDOCK
        asnReceiveEntity = receive(asnReceiveEntity);
        Double qtyRcvEa = asnReceiveEntity.getQtyRcvEa();// 收货数
        // 循环出库单商品行
        for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
            if (qtyRcvEa == 0) {
                break;
            }
            double qtyAlloc = soDetailEntity.getQtySoEa() - soDetailEntity.getQtyPreallocEa() - soDetailEntity.getQtyAllocEa() - soDetailEntity.getQtyPkEa() - soDetailEntity.getQtyShipEa();
            // 可进行分配并且分拨标记
            if (qtyAlloc > 0D && WmsCodeMaster.CD_TYPE_INDIRECT.getCode().equals(soDetailEntity.getCdType())) {
                double qtyOpEa = 0D;// 可操作数
                // 如果收货数>=分配数
                if (qtyRcvEa >= qtyAlloc) {
                    qtyOpEa = qtyAlloc;
                } else {
                    qtyOpEa = qtyRcvEa;
                }
                // 出库手工分配->拣货确认
                manualAlloc(asnReceiveEntity, soDetailEntity, qtyOpEa, WmsCodeMaster.CD_TYPE_INDIRECT.getCode());
                qtyRcvEa -= qtyOpEa;
            }

        }
        return msg;
    }

    /**
     * 收货确认
     */
    @Transactional
    public BanQinWmAsnDetailReceiveEntity receive(BanQinWmAsnDetailReceiveEntity asnReceiveEntity) throws WarehouseException {
        // 收货，默认收货到CROSSDOCK
        asnReceiveEntity.setActionCode(ActionCode.RECEIVING);
        asnReceiveEntity.setToLoc(WmsConstants.CROSSDOCK);// 收货库位：默认越库库位
        // 返回参数
        ResultMessage msg = receivingOperationService.inboundReceiving(asnReceiveEntity);
        // 收货完成后的收货明细记录
        return (BanQinWmAsnDetailReceiveEntity) msg.getData();
    }

    /**
     * 手工分配
     */
    @Transactional
    public BanQinWmSoAllocEntity manualAlloc(BanQinWmAsnDetailReceiveEntity asnReceiveEntity, BanQinWmSoDetailEntity soDetailEntity, Double qtyOpEa, String cdType) throws WarehouseException {
        BanQinWmSoAllocEntity wmSoAllocEntity = new BanQinWmSoAllocEntity();
        wmSoAllocEntity.setSoNo(soDetailEntity.getSoNo());// SO
        wmSoAllocEntity.setLineNo(soDetailEntity.getLineNo());// SO行号
        wmSoAllocEntity.setOwnerCode(soDetailEntity.getOwnerCode());
        wmSoAllocEntity.setSkuCode(soDetailEntity.getSkuCode());
        wmSoAllocEntity.setPackCode(soDetailEntity.getPackCode());// 包装编码
        wmSoAllocEntity.setUom(soDetailEntity.getUom());// 包装单位
        wmSoAllocEntity.setUomQty(soDetailEntity.getUomQty());// 包装换算数量
        wmSoAllocEntity.setLotNum(asnReceiveEntity.getLotNum());// 批次号
        wmSoAllocEntity.setLocCode(asnReceiveEntity.getToLoc());// 源库位：收货库位=CROSSDOCK
        wmSoAllocEntity.setTraceId(asnReceiveEntity.getToId());// 跟踪号
        wmSoAllocEntity.setOrgId(soDetailEntity.getOrgId());
        String toLoc = SysControlParamsUtils.getValue(ControlParamCode.ALLOC_DEF_LOC.getCode(), soDetailEntity.getOrgId());
        wmSoAllocEntity.setToLoc(toLoc);// 目标拣货库位：SS,控制参数
        // 目标跟踪号
        String toId = null;
        // 分配参数：分配时是否自动产生跟踪号（Y：自动产生；N：默认为*）
        String allocTraceId = SysControlParamsUtils.getValue(ControlParamCode.ALLOC_TRACE_ID.getCode(), soDetailEntity.getOrgId());
        if (WmsConstants.YES.equals(allocTraceId)) {
            toId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
        } else {
            toId = WmsConstants.TRACE_ID;
        }
        wmSoAllocEntity.setToId(toId);// 目标跟踪号
        // wmSoAllocEntity.setToId(asnReceiveEntity.getToId());//目标跟踪号 = 收货跟踪号
        wmSoAllocEntity.setStatus(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode());// 完全分配
        wmSoAllocEntity.setQtyEa(qtyOpEa);// 分配数=收货数
        wmSoAllocEntity.setQtyUom(qtyOpEa / wmSoAllocEntity.getUomQty());
        // 越库绑定与ASN关系，ASN_NO RCV_LINE_NO
        wmSoAllocEntity.setAsnNo(asnReceiveEntity.getAsnNo());// ASN单号
        wmSoAllocEntity.setAsnLineNo(asnReceiveEntity.getAsnLineNo());// ASN商品行号
        wmSoAllocEntity.setRcvLineNo(asnReceiveEntity.getLineNo());// 收货行号
        wmSoAllocEntity.setCdType(cdType);// 越库类型
        if (cdType.equals(WmsCodeMaster.CD_TYPE_DIRECT.getCode())) {
            // 如果是直接越库
            String rcvCdOutStatus = SysControlParamsUtils.getValue(ControlParamCode.RCV_CD_OUT_STATUS.getCode(), soDetailEntity.getOrgId());
            if (StringUtils.isNotEmpty(rcvCdOutStatus) && rcvCdOutStatus.equals(WmsConstants.ALLOC_PK)) {
                wmSoAllocEntity.setCdOutStep(WmsCodeMaster.RCV_ALLOC_PK.getCode());// 越库步骤
            } else if (StringUtils.isNotEmpty(rcvCdOutStatus) && rcvCdOutStatus.equals(WmsConstants.ALLOC_SP)) {
                wmSoAllocEntity.setCdOutStep(WmsCodeMaster.RCV_ALLOC_PK_SP.getCode());// 越库步骤
            } else {
                wmSoAllocEntity.setCdOutStep(WmsCodeMaster.RCV_ALLOC.getCode());// 越库步骤
            }
        } else {
            wmSoAllocEntity.setCdOutStep(WmsCodeMaster.RCV_ALLOC.getCode());// 越库步骤
        }

        // 调用手工分配
        wmSoAllocEntity = outboundManualAllocService.outboundManualAlloc(wmSoAllocEntity);
        return wmSoAllocEntity;
    }

}
