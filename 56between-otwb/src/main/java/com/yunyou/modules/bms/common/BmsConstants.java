package com.yunyou.modules.bms.common;

/**
 * 描述：BMS常量类
 *
 * @author Jianhua
 * @version 2019/6/24
 */
public class BmsConstants {

    public static final String YES = "Y";
    public static final String NO = "N";

    /**
     * 费用模块
     */
    public static final String BILL_MODULE_01 = "01";// 仓储
    public static final String BILL_MODULE_02 = "02";// 运输
    /**
     * 应收应付
     */
    public static final String RECEIVABLE = "01";// 应收
    public static final String PAYABLE = "02";// 应付
    /**
     * 条款输出对象
     */
    public static final String OUTPUT_OBJECT_001 = "001";// 单据数量
    public static final String OUTPUT_OBJECT_002 = "002";// 商品数量
    public static final String OUTPUT_OBJECT_003 = "003";// 商品数量(实际箱)
    public static final String OUTPUT_OBJECT_004 = "004";// 商品数量(实际托)
    public static final String OUTPUT_OBJECT_005 = "005";// 商品数量(理论箱)
    public static final String OUTPUT_OBJECT_006 = "006";// 商品数量(理论托)
    public static final String OUTPUT_OBJECT_007 = "007";// 重量
    public static final String OUTPUT_OBJECT_008 = "008";// 体积
    public static final String OUTPUT_OBJECT_009 = "009";// 网点数
    public static final String OUTPUT_OBJECT_010 = "010";// 里程数
    public static final String OUTPUT_OBJECT_011 = "011";// 库位数
    public static final String OUTPUT_OBJECT_012 = "012";// 托盘数
    public static final String OUTPUT_OBJECT_013 = "013";// 重泡比
    public static final String OUTPUT_OBJECT_014 = "014";// 检疫证
    public static final String OUTPUT_OBJECT_999 = "999";// 无
    /**
     * 条款发生量
     */
    public static final String OCCURRENCE_QUANTITY_001 = "001";// 件数量
    public static final String OCCURRENCE_QUANTITY_002 = "002";// 实际箱数量
    public static final String OCCURRENCE_QUANTITY_003 = "003";// 实际托数量
    public static final String OCCURRENCE_QUANTITY_004 = "004";// 理论箱数量
    public static final String OCCURRENCE_QUANTITY_005 = "005";// 理论托数量
    /**
     * 条款参数格式
     */
    public static final String TERMS_PARAM_TYPE_SELECT = "SELECT";// 下拉框
    public static final String TERMS_PARAM_TYPE_DATE = "DATE";    // 日期
    public static final String TERMS_PARAM_TYPE_TEXT = "TEXT";    // 文本
    /**
     * 手工费条款编码
     */
    public static final String MANUAL_FEE_TERM_CODE = "MFT999999";
    /**
     * 公式参数
     */
    public static final String FORMULA_PARAM_001 = "001";// 条款输出对象
    public static final String FORMULA_PARAM_002 = "002";// 合同单价
    public static final String FORMULA_PARAM_003 = "003";// 物流点数
    public static final String FORMULA_PARAM_004 = "004";// 商品未税单价
    public static final String FORMULA_PARAM_005 = "005";// 商品含税单价
    public static final String FORMULA_PARAM_006 = "006";// 合同系数
    public static final String FORMULA_PARAM_007 = "007";// 日历系数
    public static final String FORMULA_PARAM_008 = "008";// 税率
    /**
     * 合同状态
     */
    public static final String CONTRACT_NEW = "00";// 新建
    public static final String CONTRACT_VALID = "10";// 生效
    public static final String CONTRACT_INVALID = "90";// 失效

    public static final String INCLUDE = "0";// 包含
    public static final String EXCLUDE = "1";// 排除
    /**
     * 账单状态
     */
    public static final String BILL_STATUS_01 = "01";// 待确认
    public static final String BILL_STATUS_02 = "02";// 已生成账单
    public static final String BILL_STATUS_03 = "03";// 已核销
    /**
     * 业务数据类型
     */
    public static final String BUSINESS_DATA_TYPE_INBOUND = "01";
    public static final String BUSINESS_DATA_TYPE_OUTBOUND = "02";
    public static final String BUSINESS_DATA_TYPE_INVENTORY = "03";
    public static final String BUSINESS_DATA_TYPE_WAYBILL = "04";
    public static final String BUSINESS_DATA_TYPE_DISPATCH_ORDER = "05";
    public static final String BUSINESS_DATA_TYPE_DISPATCH_DATA = "06";
    public static final String BUSINESS_DATA_TYPE_EXCEPTION = "07";
    public static final String BUSINESS_DATA_TYPE_RETURN = "08";
    /**
     * 业务数据来源
     */
    public static final String BUSINESS_DATA_SOURCE_INTERFACE = "INT";
    public static final String BUSINESS_DATA_SOURCE_IMPORT = "IMP";

    public static final Integer BILL_RETAIN_DECIMAL = 2;// 费用结果保留小数位

}
