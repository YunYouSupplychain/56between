package com.yunyou.modules.interfaces.edi.utils;

import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;

public class EdiConstant {

    public static final String EDI_TYPE_ASN = "ASN";
    public static final String EDI_TYPE_SO = "SO";
    public static final String EDI_TYPE_INV = "INV";
    public static final String EDI_TYPE_PLAN_INV = "PLAN_INV";
    public static final String EDI_TYPE_DO = "DO";

    public static String EDI_URL = SysControlParamsUtils.getValue(SysParamConstants.EDI_URL);
}