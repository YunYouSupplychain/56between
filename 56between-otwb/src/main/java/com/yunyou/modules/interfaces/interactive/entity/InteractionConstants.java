package com.yunyou.modules.interfaces.interactive.entity;

import com.yunyou.modules.sys.entity.User;

public class InteractionConstants {

    public static final String SYN_SUCCESS = "Y";
    public static final String SYN_FALSE = "N";

    /****************************** SMS_BMS_SYNCHRO ****************************************/

    public static final String SYN_CUSTOMER_TIMER_FLAG = "SYN_SETTLEMENT_CUSTOMER_TIMER_FLAG";
    public static final String SYN_SKU_TIMER_FLAG = "SYN_SETTLEMENT_SKU_TIMER_FLAG";
    public static final String SYN_TYPE_CUSTOMER = "SYN_SETTLEMENT_CUSTOMER";
    public static final String SYN_TYPE_SKU = "SYN_SETTLEMENT_SKU";

    public static final User SYN_USER = new User("1");

    public static final String SYN_SKU_PRICE_TYPE = "1";

    public static final String BUSINESS_TYPE_ZT = "10";           // 业务类型 直通
    public static final String BUSINESS_TYPE_CC = "20";           // 业务类型 存储

    /****************************** SMS_WMS_SYNCHRO ****************************************/

    public static final String UOM_DEFAULT = "EA";

    /****************************** SMS_DAY_INVENTORY_BOX_SAVE ****************************************/

    public static final String SYN_DAY_INVENTORY_BOX_SAVE = "SYN_DAY_INVENTORY_BOX_SAVE";

    /*********************************** WMS_LD_TO_CHECK_ORDER **********************************/

    public static final String WMS_LD_TO_CHECK_ORDER = "WMS_LD_TO_CHECK_ORDER";
}