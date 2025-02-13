package com.yunyou.modules.oms.common;

/**
 * 描述：OMS常量类
 *
 * @auther Jianhua on 2019/5/5
 */
public class OmsConstants {

    /*未关联客户的商品价格作为默认商品价格，客户默认为无*/
    public static final String OMS_DEFAULT_PRICE_CUSTOMER = "无";
    /*Y/N*/
    public static final String OMS_Y = "Y";
    public static final String OMS_N = "N";

    /*采购订单状态*/
    public static final String OMS_PO_STATUS_10 = "10";// 新建
    public static final String OMS_PO_STATUS_20 = "20";// 确认
    public static final String OMS_PO_STATUS_30 = "30";// 审核
    public static final String OMS_PO_STATUS_40 = "40";// 已生成供应链订单
    public static final String OMS_PO_STATUS_99 = "99";// 完成
    public static final String OMS_PO_STATUS_90 = "90";// 取消
    /*采购订单类型*/
    public static final String OMS_PO_TYPE_01 = "1";    // 正常采购
    public static final String OMS_PO_TYPE_02 = "2";    // 采购退货

    /*销售订单状态*/
    public static final String OMS_SO_STATUS_10 = "10";// 新建
    public static final String OMS_SO_STATUS_20 = "20";// 确认
    public static final String OMS_SO_STATUS_30 = "30";// 审核
    public static final String OMS_SO_STATUS_40 = "40";// 已生成供应链订单
    public static final String OMS_SO_STATUS_99 = "99";// 完成
    public static final String OMS_SO_STATUS_90 = "90";// 取消
    /*销售订单类型*/
    public static final String OMS_SO_TYPE_01 = "1";    // 正常销售
    public static final String OMS_SO_TYPE_02 = "2";    // 销售退货
    public static final String OMS_SO_TYPE_03 = "3";    // 折扣销售
    /*供应链订单状态*/
    public static final String OMS_CO_STATUS_00 = "00";// 新建
    public static final String OMS_CO_STATUS_30 = "30";// 审核
    public static final String OMS_CO_STATUS_35 = "35";// 部分生成任务
    public static final String OMS_CO_STATUS_40 = "40";// 完全生成任务
    public static final String OMS_CO_STATUS_90 = "90";// 取消
    /*业务订单类型*/
    public static final String OMS_BUSINESS_ORDER_TYPE_01 = "1";    //正常采购
    public static final String OMS_BUSINESS_ORDER_TYPE_02 = "2";    //正常销售
    public static final String OMS_BUSINESS_ORDER_TYPE_03 = "3";    //销售退货
    public static final String OMS_BUSINESS_ORDER_TYPE_04 = "4";    //采购退货
    public static final String OMS_BUSINESS_ORDER_TYPE_05 = "5";    //调拨入库
    public static final String OMS_BUSINESS_ORDER_TYPE_06 = "6";    //领用
    public static final String OMS_BUSINESS_ORDER_TYPE_07 = "7";    //赠送
    public static final String OMS_BUSINESS_ORDER_TYPE_08 = "8";    //折扣销售
    public static final String OMS_BUSINESS_ORDER_TYPE_09 = "9";    //调拨出库
    public static final String OMS_BUSINESS_ORDER_TYPE_10 = "10";    //调整入库
    public static final String OMS_BUSINESS_ORDER_TYPE_11 = "11";    //调整出库
    public static final String OMS_BUSINESS_ORDER_TYPE_12 = "12";    //盘点入库
    public static final String OMS_BUSINESS_ORDER_TYPE_13 = "13";    //盘点出库
    /*供应链订单来源订单类型*/
    public static final String OMS_SOURCE_TYPE_PO = "PO";// 采购订单
    public static final String OMS_SOURCE_TYPE_SO = "SO";// 销售订单
    /*价格类型*/
    public static final String OMS_PRINT_TYPE_P = "P"; //采购价格
    public static final String OMS_PRINT_TYPE_S = "S"; //销售价格

    /*任务状态*/
    public static final String OMS_TASK_STATUS_20 = "20";// 确认
    public static final String OMS_TASK_STATUS_25 = "25";// 部分分配
    public static final String OMS_TASK_STATUS_30 = "30";// 已分配
    public static final String OMS_TASK_STATUS_40 = "40";// 已下发
    public static final String OMS_TASK_STATUS_90 = "90";// 取消
    /*任务类型*/
    public static final String OMS_TASK_TYPE_01 = "1";// 出库
    public static final String OMS_TASK_TYPE_02 = "2";// 入库
    public static final String OMS_TASK_TYPE_03 = "3";// 运输
    /*任务分配状态*/
    public static final String OMS_TK_ALLOC_STATUS_00 = "00";// 未分配
    public static final String OMS_TK_ALLOC_STATUS_10 = "10";// 已分配
    public static final String OMS_TK_ALLOC_STATUS_90 = "90";// 不分配

    public static final String OMS_PACKAGE_EA = "EA";   //包装规格-ea

    /*运输方式*/
    public static final String OMS_TRANSPORT_MODE_01 = "1";// 铁路
    public static final String OMS_TRANSPORT_MODE_02 = "2";// 航空
    public static final String OMS_TRANSPORT_MODE_03 = "3";// 陆运
    public static final String OMS_TRANSPORT_MODE_04 = "4";// 快递
    public static final String OMS_TRANSPORT_MODE_05 = "5";// 船运

    /*拦截状态*/
    public static final String OMS_INTERCEPT_STATUS_00 = "00";// 无拦截
    public static final String OMS_INTERCEPT_STATUS_10 = "10";// 拦截
    public static final String OMS_INTERCEPT_STATUS_90 = "90";// 拦截失败
    public static final String OMS_INTERCEPT_STATUS_99 = "99";// 拦截成功

    /*操作类型*/
    public static final String OMS_OP_TYPE_ALLOC = "ALLOC";     // 分配
    public static final String OMS_OP_TYPE_UNALLOC = "UNALLOC"; // 取消分配
    public static final String OMS_OP_TYPE_INTERCEPT = "INTERCEPT";// 拦截
    public static final String OMS_OP_TYPE_SHIP = "SHIP"; // 发运

    /*任务来源*/
    public static final String TASK_SOURCE_CO = "CO";// 供应链订单
    public static final String TASK_SOURCE_RO = "RO";// 调拨单

    /*调拨单状态*/
    public static final String OMS_RO_STATUS_10 = "10";// 新建
    public static final String OMS_RO_STATUS_20 = "20";// 已审核
    public static final String OMS_RO_STATUS_35 = "35";// 部分生成任务
    public static final String OMS_RO_STATUS_40 = "40";// 全部生成任务
    public static final String OMS_RO_STATUS_90 = "90";// 取消
    public static final String OMS_RO_STATUS_99 = "99";// 关闭

    /*文件导入类型*/
    public static final String IMPORT_TYPE_DC_RT = "DC_RT"; // DC冷藏
    public static final String IMPORT_TYPE_DC_FT = "DC_FT"; // DC冷冻
    public static final String IMPORT_TYPE_DC_V = "DC_V"; // DC蔬菜
    public static final String IMPORT_TYPE_DC_F = "DC_F"; // DC水果
    public static final String IMPORT_TYPE_DC_RF = "DC_RF"; // DC-RF
    public static final String IMPORT_TYPE_DC_INT = "DC_INT"; // DC接口
    public static final String IMPORT_TYPE_WMS_ASN = "WMS_ASN"; // 仓储入库
    public static final String IMPORT_TYPE_WMS_SO = "WMS_SO"; // 仓储出库

    public static final String HANDLE_STATUS_N = "N";// 待处理
    public static final String HANDLE_STATUS_Y = "Y";// 处理成功
    public static final String HANDLE_STATUS_E = "E";// 处理异常

}
