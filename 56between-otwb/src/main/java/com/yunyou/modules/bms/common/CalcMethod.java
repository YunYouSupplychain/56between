package com.yunyou.modules.bms.common;

/**
 * 费用计算处理方法名称
 *
 * @author liujianhua
 * <p>
 * TODO 在增加与业务数据相关的处理方法时，需要同步更新getDefaultFieldValue方法
 * @see com.yunyou.modules.bms.finance.service.BmsSettleModelDetailService
 *      getDefaultFieldValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
public enum CalcMethod {
    /**
     * 用入库数据计算费用
     */
    calcInbound,
    /**
     * 用出库数据计算费用
     */
    calcOutbound,
    /**
     * 用库存数据计算费用
     */
    calcInventory,
    /**
     * 用运输单数据计算费用
     */
    calcWaybill,
    /**
     * 用派车单数据计算费用
     */
    calcDispatchOrder,
    /**
     * 用派车配载数据计算费用
     */
    calcDispatch,
    /**
     * 用退货数据计算费用
     */
    calcReturn,
    /**
     * 用异常数据计算费用
     */
    calcException,
    /**
     * 每日出一笔合同固定费用，无任何逻辑
     */
    calcFixedDaily,
    /**
     * 每月出一笔合同固定费用，无任何逻辑
     */
    calcFixedMonthly,
    /**
     * 每日出一笔合同固定费用（以输入业务机构为统计维度）
     */
    calcFixedDailyForBusinessOrg,
    /**
     * 每月出一笔合同固定费用（以输入业务机构为统计维度）
     */
    calcFixedMonthlyForBusinessOrg,

    /**
     * 用派车配载数据计算费用（统计维度：标准维度+收货方）
     */
    calcDispatchForRegion,
    /**
     * 用派车配载数据计算费用（统计维度：收货方+检疫类型）
     */
    calcDispatchForQC
}