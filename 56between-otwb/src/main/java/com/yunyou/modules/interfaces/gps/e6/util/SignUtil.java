package com.yunyou.modules.interfaces.gps.e6.util;

import com.yunyou.common.utils.MD5Util;
import com.yunyou.modules.interfaces.gps.e6.constant.Constants;

import java.util.Map;

public class SignUtil {

    public static String sign(Map<String, String> params) {
        StringBuilder str = new StringBuilder(Constants.SECRET_KEY);
        params.keySet().stream().sorted().forEach(key -> str.append(key).append(params.get(key)));
        str.append(Constants.SECRET_KEY);
        return MD5Util.encrypt32(str.toString()).toUpperCase();
    }
}
