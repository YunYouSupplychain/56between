package com.yunyou.modules.sys;

/**
 * 描述：系统参数常量
 * <p>
 * create by Jianhua on 2019/10/22
 */
public class SysParamConstants {
    // COMMON参数：平台数据保存并下发至机构(Y:是 N:否)
    public static final String IS_SAVING_PUSH = "IS_SAVING_PUSH";
    // COMMON参数：下发TMS时自动审核(Y:是 N:否)
    public static final String PUSH_TO_TMS_AUDIT = "PUSH_TO_TMS_AUDIT";
    // COMMON参数：阿里云服务器所在区
    public static final String ALIYUN_REGION_ID = "ALIYUN_REGION_ID";
    // COMMON参数：阿里云accessKeyId
    public static final String ALIYUN_ACCESS_KEY_ID = "ALIYUN_ACCESS_KEY_ID";
    // COMMON参数：阿里云accessKeySecret
    public static final String ALIYUN_ACCESS_KEY_SECRET = "ALIYUN_ACCESS_KEY_SECRET";
    // COMMON参数：阿里云语音通知公共模板
    public static final String ALIYUN_ANNOUNCEMENT_TEMPLATE = "ALIYUN_ANNOUNCEMENT_TEMPLATE";

    // SYS参数：PORTAL平台接口地址
    public static final String EDI_URL = "EDI_URL";

    // OMS参数：订单生成任务时忽略库存校验(Y:是 N:否)
    public static final String CHAIN_GEN_TASK_IGNORE_CHECK_INV = "CHAIN_GEN_TASK_IGNORE_CHECK_INV";
    // OMS参数：是否同时合并同批次运输任务(Y:是 N:否)
    public static final String IS_MERGE_BATCH_TRANS_TASK = "IS_MERGE_BATCH_TRANS_TASK";
    // OMS参数：是否同时下发同批次运输任务(Y:是 N:否)
    public static final String IS_SEND_BATCH_TRANS_TASK = "IS_SEND_BATCH_TRANS_TASK";
    // OMS参数：是否校收货人省市区三级地址(Y:是 N:否)
    public static final String IS_CHECK_THREE_LEVEL_ADDR = "IS_CHECK_THREE_LEVEL_ADDR";
    // OMS参数：调拨单生成任务时是否生成运输任务(Y:是 N:否)
    public static final String REQUISITION_IS_GEN_TRANSPORT_TASK = "REQUISITION_IS_GEN_TRANSPORT_TASK";
    // OMS参数：是否下发运输任务至运输计划订单(Y:是 N:否)
    public static final String IS_PUSH_PRE_TRANSPORT_ORDER = "IS_PUSH_PRE_TRANSPORT_ORDER";

    // TMS参数：运输订单审核时自动生成默认标签，取消审核时则删除标签(Y:是 N:否)
    public static final String AUTO_GEN_DEFAULT_LABEL = "AUTO_GEN_DEFAULT_LABEL";
    // TMS参数：运输订单自动生成默认标签(Y：按明细生成，N：按订单生成)
    public static final String GEN_DEFAULT_LABEL_BY_TRANSPORT_DETAIL = "GEN_DEFAULT_LABEL_BY_TRANSPORT_DETAIL";
    // TMS参数：运输订单添加标签时揽收网点自动收货(Y:是 N:否)
    public static final String ADD_LABEL_OUTLET_AUTO_RECEIVE = "ADD_LABEL_OUTLET_AUTO_RECEIVE";
    // TMS参数：派车单发车前网点自动发货(Y:是 N:否)
    public static final String DEPART_OUTLET_AUTO_SHIP = "DEPART_OUTLET_AUTO_SHIP";
    // TMS参数：交接时网点自动收、发(Y:是 N:否)
    public static final String HANDOVER_OUTLET_AUTO_R_S = "HANDOVER_OUTLET_AUTO_R_S";
    // TMS参数：网络站点配送模式(Y:是 N:否)
    public static final String SITE_DISPATCH_MODE = "SITE_DISPATCH_MODE";
    // TMS参数：派车单按商品统计重量、体积（Y：是 N：否） 注：按商品统计指的是取商品基础数据的毛重*数量，体积同上；N：否，默认按标签统计
    public static final String DISPATCH_TOTAL_BY_SKU = "DISPATCH_TOTAL_BY_SKU";
    // TMS参数：承运商服务范围模式(派车单选择配送点时仅能选择承运商自身服务范围内的网点)
    public static final String IS_CARRIER_SERVICE_SCOPE_PATTERN = "IS_CARRIER_SERVICE_SCOPE_PATTERN";
    // TMS参数：地图授权码
    public static final String MAP_WEB_SERVER_AK = "MAP_WEB_SERVER_AK";
    public static final String MAP_WEB_CLIENT_AK = "MAP_WEB_CLIENT_AK";
    // TMS参数：温度预警呼叫通知(Y:是 N:否)
    public static final String IS_TEMPERATURE_WARNING_SMART_CALL = "IS_TEMPERATURE_WARNING_SMART_CALL";

    // BMS参数：计算费用时是否先删除其“新建状态”历史费用记录(Y：是，N：否)
    public static final String DELETE_HISTORY_NEW_BILL_RECORD = "DELETE_HISTORY_NEW_BILL_RECORD";
    // BMS参数：重泡比比例
    public static final String HEAVY_BUBBLE_RATIO = "HEAVY_BUBBLE_RATIO";
}
