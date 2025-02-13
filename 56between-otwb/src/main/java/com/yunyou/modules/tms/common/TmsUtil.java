package com.yunyou.modules.tms.common;

import com.yunyou.common.utils.StringUtils;

public class TmsUtil {

    public static String getMessage(String message) {
        return StringUtils.isNotBlank(message) ? message.replace(TmsConstants.DATA_HAS_EXPIRED, TmsConstants.NULL_STRING) : "";
    }
}
