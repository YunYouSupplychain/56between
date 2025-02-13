package com.yunyou.modules.sys;

/**
 * 生成编号类型
 */
public enum GenNoType {
    /**
     * 采购单号 {PARAMS(PO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    OM_PO_NO,
    /**
     * 销售单号 {PARAMS(SO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    OM_SALE_NO,
    /**
     * 供应链订单号 {PARAMS(CO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    OM_CHAIN_NO,
    /**
     * 作业任务号 {PARAMS(TK)}{DATE(yyyyMMdd)}{NO(6)}
     */
    OM_TASK_NO,
    /**
     * 业务服务组编码 {DATE(yyyyMMdd)}{NO(6)}
     */
    OM_BUSINESS_GROUP_NO,
    /**
     * 调拨单号 {PARAMS(RO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    OM_REQUISITION_NO,

    /**
     * 派车单号 {PARAMS(D)}{DATE(yyyyMMdd)}{NO(6)}
     */
    ET_DISPATCH_NO,
    /**
     * ASN单号 {PARAMS(ASN)}{DATE(yyyyMMdd)}{NO(6)}
     */
    ET_ASN_NO,
    /**
     * 采购生成ASN单号 {PARAMS(ASNPO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    ET_ASN_PO_NO,
    /**
     * 采购汇总单号 {PARAMS(POS)}{DATE(yyyyMMdd)}{NO(6)}
     */
    ET_PO_SUM_NO,
    /**
     * 总收货订单单号 {PARAMS(TOR)}{DATE(yyyyMMdd)}{NO(6)}
     */
    ET_TOTAL_RECEIVE_NO,
    /**
     * 档口编码 {PARAMS(DKZ)}{NO(6)}
     */
    ET_STALLS_NO,
    /**
     * 验收单打印流水 {DATE(yyyyMMdd)}{NO(6)}
     */
    ET_CHECK_ORDER_SERIAL_NO,
    /**
     * 发货交接流水号 {DATE(yyyyMMdd)}{NO(6)}
     */
    ET_HANDOVER_SERIAL_NO,
    /**
     * 下发批次号 {PARAMS(ISB)}{DATE(yyyyMMdd)}{NO(6)}
     */
    ET_ISSUE_BATCH_NO,
    /**
     * 发货反馈 {NO(6)}
     */
    SHIP_RETURN,
    /**
     * 收获反馈 {NO(6)}
     */
    RECEIVE_RETURN,
    /**
     * 退货单号 {PARAMS(RO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WMS_RETURN_ORDER,
    /**
     * 派车异常单号 {PARAMS(DE)}{DATE(yyyyMMdd)}{NO(6)}
     */
    DISPATCH_EXCEPTION,

    /**
     * 采购单号 {PARAMS(PO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_PO_NO,
    /**
     * 预收货通知单号 {PARAMS(ASN)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_ASN_NO,
    /**
     * 预收货通知单凭证号 {PARAMS(VO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_VOUCHER_NO,
    /**
     * 质检单号 {PARAMS(QC)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_QC_NO,
    /**
     * 销售单号 {PARAMS(SALE)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_SALE_NO,
    /**
     * 波次单号 {PARAMS(WV)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_WAVE_NO,
    /**
     * 出货单号 {PARAMS(SO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_SO_NO,
    /**
     * 预配记录ID {PARAMS(PRE)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_PREALLOC_ID,
    /**
     * 分配记录ID {PARAMS(ACT)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_ALLOC_ID,
    /**
     * 拣货单号 {PARAMS(PK)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_PK_NO,
    /**
     * 调整单号 {PARAMS(AD)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_AD_NO,
    /**
     * 转移单号 {PARAMS(TF)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_TF_NO,
    /**
     * 盘点单号 {PARAMS(CC)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_COUNT_NO,
    /**
     * 序列号盘点单号 {PARAMS(SC)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_SERIAL_COUNT_NO,
    /**
     * 冻结单号 {PARAMS(HD)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_HOLD_ID,
    /**
     * 库存交易ID {PARAMS(TRA)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_TRAN_ID,
    /**
     * 批次号 {PARAMS(LOT)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_LOT_NUM,
    /**
     * 跟踪号 {PARAMS(TRA)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_TRACE_ID,
    /**
     * 上架任务ID {PARAMS(PA)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_PA_ID,
    /**
     * 补货任务ID {PARAMS(RP)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_RP_ID,
    /**
     * 盘点任务ID {PARAMS(CNT)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_COUNT_ID,
    /**
     * 装车单号 {PARAMS(LD)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_LD_NO,
    /**
     * 加工单 {PARAMS(KIT)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_KIT_NO,
    /**
     * 加工任务ID {PARAMS(KT)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_KIT_ID,
    /**
     * 序列号库存交易ID {PARAMS(STN)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_SERIAL_TRAN_ID,
    /**
     * 越库收货明细号 {PARAMS(CDR)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_CD_RCV_ID,
    /**
     * 月台预约号 {PARAMS(APP)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_APPOINT_ID,
    /**
     * 质检收货明细号 {PARAMS(QCR)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_QC_RCV_ID,
    /**
     * 交接单号 {PARAMS(HO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_HANDOVER_NO,
    /**
     * 拣货单号 {PARAMS(PIC)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_PICK_NO,
    /**
     * 移动单号 {PARAMS(MV)}{DATE(yyyyMMdd)}{NO(6)}
     */
    WM_MV_NO,

    /**
     * 导入批次号 {PARAMS(TIM)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_EXPRESS_IMPORT_NO,
    /**
     * 运输单号 {TM_TRANSPORT_NO(TO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_TRANSPORT_NO,
    /**
     * 派车单号 {TM_DISPATCH_NO(DO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_DISPATCH_NO,
    /**
     * 交接单号 {TM_HANDOVER_NO(HO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_HANDOVER_NO,
    /**
     * 需求计划单号 {TM_DEMAND_PLAN_NO(MO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_DEMAND_PLAN_NO,
    /**
     * 调度计划单号 {TM_DISPATCH_PLAN_NO(PO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_DISPATCH_PLAN_NO,
    /**
     * 维修工单号 {TM_REPAIR_NO(RO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_REPAIR_NO,
    /**
     * 异常处理单号 {TM_EXCEPTION_NO(EXO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_EXCEPTION_NO,
    /**
     * 备件入库单号 {TM_SPARE_ASN_NO(ASN)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_SPARE_ASN_NO,
    /**
     * 备件出库单号 {TM_SPARE_SO_NO(SO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    TM_SPARE_SO_NO,
    /**
     * 车型编号 {TM_VEHICLE_TYPE_NO}{{NO(3)}
     */
    TM_VEHICLE_TYPE_NO,

    /**
     * 合同号 {PARAMS(CO)}{DATE(yyyyMMdd)}{NO(6)}
     */
    SYS_CONTRACT_NO,
    /**
     * 费用单号 {PARAMS(B)}{DATE(yyyyMMdd)}{NO(6)}
     */
    BMS_BILL_NO,
    /**
     * 结算模型编号 {PARAMS(SM)}{DATE(yyyyMMdd)}{NO(6)}
     */
    BMS_SETTLEMENT_MODEL_NO,
    /**
     * 运输价格体系编号 {DATE(yyyyMMdd)}{NO(6)}
     */
    BMS_TRANSPORT_GROUP,
    /**
     * 账单号 {PARAMS(CN)}{DATE(yyyyMMdd)}{NO(6)}
     */
    BMS_CONFIRM_NO
}