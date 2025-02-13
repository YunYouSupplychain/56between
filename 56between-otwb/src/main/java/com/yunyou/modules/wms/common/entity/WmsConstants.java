package com.yunyou.modules.wms.common.entity;

/**
 * 描述： WMS常量定义类
 *
 * @author Jianhua on 2019/1/26
 */
public class WmsConstants {

    /**
     * 通用常量
     */
    public static final String OK = "OK";
    public static final String OPERATE_SUCCESS = "操作成功";
    public static final Long DELETE_NO = 0l;
    public static final Long DELETE_YES = 1l;
    /**
     * 公共标识
     */
    public static final String YES = "Y";
    public static final String NO = "N";

    /**
     * 日期格式
     */
    public static final String DATE = "DATE";
    /**
     * 日期格式无时间
     */
    public static final String DateField = "DateField";
    /**
     * 下拉框
     */
    public static final String ComboBox = "ComboBox";
    /***************************************************************************
     * 文件导入 start
     **************************************************************************/
    /**
     * 上传文件入参type
     */
    public static final String FILE_TYPE = "fileType";
    /**
     * PO(按箱)导入
     */
    public static final String UPLOAD_PO_BOX = "UPLOAD_PO_BOX";
    /**
     * ASN明细导入
     */
    public static final String UPLOAD_ASN = "UPLOAD_ASN";
    /**
     * ASN明细导入
     */
    public static final String UPLOAD_ASN_HEADER = "UPLOAD_ASN_HEADER";
    /**
     * 入库序列号导入
     */
    public static final String UPLOAD_ASN_SERIAL = "UPLOAD_ASN_SERIAL";
    /**
     * 发运订单so明细导入
     */
    public static final String UPLOAD_SO = "UPLOAD_SO";
    /**
     * 发运订单so单头和明细导入
     */
    public static final String UPLOAD_SO_HEADER = "UPLOAD_SO_HEADER";
    /**
     * SKU导入
     */
    public static final String UPLOAD_SKU = "UPLOAD_SKU";
    /**
     * 库位导入
     */
    public static final String UPLOAD_LOC = "UPLOAD_LOC";
    /**
     * 库存导入
     */
    public static final String UPLOAD_INV_NEW = "UPLOAD_INV_NEW";
    /**
     * 库存导入
     */
    public static final String UPLOAD_INV_ADD = "UPLOAD_INV_ADD";

    /***************************************************************************
     * 文件导入 end
     **************************************************************************/

    /**
     * 跟踪号默认标志 *
     */
    public static final String TRACE_ID = "*";

    /** **********************包装单位 Start**************************** */
    /**
     * 包装单位-EA
     */
    public static final String UOM_EA = "EA";
    /**
     * 包装单位-件
     */
    public static final String UOM_EA_DESC = "件";
    /**
     * 包装单位-IP
     */
    public static final String UOM_IP = "IP";
    /**
     * 包装单位-内包装
     */
    public static final String UOM_IP_DESC = "内包装";
    /**
     * 包装单位-CS
     */
    public static final String UOM_CS = "CS";
    /**
     * 包装单位-箱
     */
    public static final String UOM_CS_DESC = "箱";
    /**
     * 包装单位-PL
     */
    public static final String UOM_PL = "PL";
    /**
     * 包装单位-托盘
     */
    public static final String UOM_PL_DESC = "托盘";
    /**
     * 包装单位-OT
     */
    public static final String UOM_OT = "OT";
    /**
     * 包装单位-大包装
     */
    public static final String UOM_OT_DESC = "大包装";

    /** **********************包装单位 End**************************** */

    /** **********************装车单 Start**************************** */

    /**
     * 查询完全拣货的出库明细（LOAD_ALLOW_STATUS_PK）
     */
    public static final String LOAD_ALLOW_STATUS_PK = "PK";
    /**
     * 查询完全发运的出库明细（LOAD_ALLOW_STATUS_SP）
     */
    public static final String LOAD_ALLOW_STATUS_SP = "SP";

    /** **********************装车单 End**************************** */

    /**
     * 数据字典类型为可用状态
     */
    public static final String CODEMASTER_ENABLE_TYPE_VALUE = "1";
    /**
     * 数据字典类型为批次属性（SYS_WM_LOT_ATT）
     */
    public static final String SYS_WM_LOT_ATT = "SYS_WM_LOT_ATT";
    /**
     * 数据字典类型为包装单位（ORG_WARE_UNIT） -- 平台级别，与LMDM共用
     */
    public static final String ORG_WARE_UNIT = "ORG_WARE_UNIT";

    /** ********************* 批次属性控制状态 Start************************ */
    /**
     * 必选-R
     */
    public static final String R = "R";
    /**
     * 可选-O
     */
    public static final String O = "O";
    /**
     * 禁用-F
     */
    public static final String F = "F";

    /** **********************批次属性控制状态 End**************************** */

    /** **********************12个批次属性 Start**************************** */
    /**
     * 批次属性01
     */
    public static final String LOT_ATT01 = "LOT_ATT01";

    /**
     * 批次属性01-生产日期
     */
    public static final String LOT_ATT01_TITLE = "生产日期";

    /**
     * 批次属性02
     */
    public static final String LOT_ATT02 = "LOT_ATT02";

    /**
     * 批次属性02-失效日期
     */
    public static final String LOT_ATT02_TITLE = "失效日期";

    /**
     * 批次属性03
     */
    public static final String LOT_ATT03 = "LOT_ATT03";

    /**
     * 批次属性03-入库日期
     */
    public static final String LOT_ATT03_TITLE = "入库日期";

    /**
     * 批次属性04
     */
    public static final String LOT_ATT04 = "LOT_ATT04";

    /**
     * 批次属性04
     */
    public static final String LOT_ATT04_TITLE = "批次属性04";

    /**
     * 批次属性05
     */
    public static final String LOT_ATT05 = "LOT_ATT05";

    /**
     * 批次属性05
     */
    public static final String LOT_ATT05_TITLE = "批次属性05";

    /**
     * 批次属性06
     */
    public static final String LOT_ATT06 = "LOT_ATT06";

    /**
     * 批次属性06
     */
    public static final String LOT_ATT06_TITLE = "批次属性06";

    /**
     * 批次属性07
     */
    public static final String LOT_ATT07 = "LOT_ATT07";

    /**
     * 批次属性07
     */
    public static final String LOT_ATT07_TITLE = "批次属性07";

    /**
     * 批次属性08
     */
    public static final String LOT_ATT08 = "LOT_ATT08";

    /**
     * 批次属性08
     */
    public static final String LOT_ATT08_TITLE = "批次属性08";

    /**
     * 批次属性09
     */
    public static final String LOT_ATT09 = "LOT_ATT09";

    /**
     * 批次属性09
     */
    public static final String LOT_ATT09_TITLE = "批次属性09";

    /**
     * 批次属性10
     */
    public static final String LOT_ATT10 = "LOT_ATT10";

    /**
     * 批次属性10
     */
    public static final String LOT_ATT10_TITLE = "批次属性10";

    /**
     * 批次属性11
     */
    public static final String LOT_ATT11 = "LOT_ATT11";

    /**
     * 批次属性11
     */
    public static final String LOT_ATT11_TITLE = "批次属性11";

    /**
     * 批次属性12
     */
    public static final String LOT_ATT12 = "LOT_ATT12";

    /**
     * 批次属性12
     */
    public static final String LOT_ATT12_TITLE = "批次属性12";

    /** **********************12个批次属性 End**************************** */
    /**
     * 标准批次属性编码
     */
    public static final String LOT_CODE_STANDARD = "STANDARD";

    public static final String AD = "AD";

    public static final String TF = "TF";

    public static final String MV = "MV";

    /**
     * 库位-过渡库位
     */
    public static final String STAGE = "STAGE";

    /**
     * 库位-理货站
     */
    public static final String SORTATION = "SORTATION";
    /**
     * 库位-加工台
     */
    public static final String WORKBENCH = "WORKBENCH";
    /**
     * 库位-越库库位
     */
    public static final String CROSSDOCK = "CROSSDOCK";

    /**
     * 库区
     */
    public static final String ZONE = "ZONE";

    /**
     * 区域
     */
    public static final String AREA = "AREA";

    /**
     * 初始化-STANDARD
     */
    public static final String STANDARD = "STANDARD";

    /**
     * 默认包装规格-1/10/100
     */
    public static final String PACKAGE_FORMAT = "1/10/100";

    /**
     * 报表模板名称 - 打印装箱清单
     */
    public static final String TEMPLATE_PACK_LIST = "装箱清单";
    /**
     * 报表模板名称 - 打印装箱标签
     */
    public static final String TEMPLATE_PACK_LABLE = "拣货箱标签";
    /**
     * 报表按扭名称 - 打印装箱清单
     */
    public static final String BUTTON_PACK_LIST = "打印装箱清单";
    /**
     * 报表按扭名称 - 打印装箱标签
     */
    public static final String BUTTON_PACK_LABLE = "打印装箱标签";

    /**
     * 打印方式-前台打印
     */
    public static final String PRINT_TYPE_01 = "01";
    /**
     * 打印方式-后台打印
     */
    public static final String PRINT_TYPE_02 = "02";
    /**
     * 库存初始化
     */
    public static final String INNIT_INVENTORY = "BEGIN";
    /**
     * 库位批量修改控制参数
     */
    public static final String SYS_WM_LOC_FIELD = "SYS_WM_LOC_FIELD";
    /**
     * 商品批量修改控制参数
     */
    public static final String SYS_WM_SKU_FIELD = "SYS_WM_SKU_FIELD";

    /**
     * 出库分配明细 拣货操作
     */
    public static final String ALLOC_PK = "PK";
    /**
     * 出库分配明细 发货操作
     */
    public static final String ALLOC_SP = "SP";
    /** *******************时间控制，使用标记****************************************** */
    /**
     * 不可用时段
     */
    public static final String TIME_STATUS_UNABLE = "UNABLE";

    /**
     * 预约时间段
     */
    public static final String TIME_STATUS_APPOINT = "APPOINT";
    /**
     * 实际使用时间段
     */
    public static final String TIME_STATUS_USED = "USED";

    public static final String DATA_HAS_EXPIRED = "数据已过期";

    public static final String NULL_STRING = "";

    /** *********************************承运商类型关系****************************** */
    /**
     * 百世快递
     */
    public static final String CARRIER_TYPE_800BEST = "KD_BEST";
    /**
     * 顺丰快递
     */
    public static final String CARRIER_TYPE_SF_EXPRESS = "SF-EXPRESS";
    /**
     * 自提
     */
    public static final String CARRIER_TYPE_SELF = "SELF";
    /**
     * 百世快递2
     */
    public static final String CARRIER_TYPE_800BEST_2 = "KD_BEST2";

    public static final String EXPRESS_TYPE_HTKY = "HTKY";
    public static final String EXPRESS_TYPE_SFEXPRESS = "SFEXPRESS";
    public static final String EXPRESS_TYPE_ZT = "ZT";
    public static final String EXPRESS_TYPE_ZTO = "ZTO";
    public static final String EXPRESS_TYPE_YUNDA = "YUNDA";
    public static final String EXPRESS_TYPE_STO = "STO";
    public static final String EXPRESS_TYPE_YTO = "YTO";
    public static final String EXPRESS_TYPE_KD100 = "KD100";

    public static final String MAIl_BS_1 = "BS1";
    public static final String MAIl_BS_2 = "BS2";
    public static final String MAIl_SF = "SF";
    public static final String MAIl_ZT = "ZT";
    public static final String MAIl_ZTO = "ZTO";
    public static final String MAIl_YUNDA = "YUNDA";
    public static final String MAIl_STO = "STO";
    public static final String MAIl_YTO = "YTO";

    public static final String ZERO = "0";
    public static final String ONE = "1";

    public static final String EDI_FLAG_00 = "00";// 未反馈
    public static final String EDI_FLAG_01 = "01";// 反馈失败
    public static final String EDI_FLAG_02 = "02";// 反馈成功
    public static final String EDI_FLAG_10 = "10";// 未反馈(用于快递单号更新反馈)
    public static final String EDI_FLAG_11 = "11";// 反馈失败(用于快递单号更新反馈)
    public static final String EDI_FLAG_12 = "12";// 反馈成功(用于快递单号更新反馈)
    public static final String EDI_FLAG_90 = "90";// 不反馈

}
