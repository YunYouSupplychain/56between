package com.yunyou.modules.interfaces.sfExpress.utils;

import com.yunyou.common.utils.MD5Util;
import com.yunyou.modules.wms.common.entity.WarehouseException;

import java.util.Arrays;

public class Sign {
    /***
     * 加签
     * 算法思路：将appId, requestId, timestamp, appSecret（如果传了appSecret参数）四个字符串参数进行排序后再拼接得到的长字符串进行md5（32位）加密
     * 示例：appId：16517
     *        requestId：3c83a80c-51eb-4aa4-8f92-41b8f897c1ed
     *        timestamp：1565085864573
     *        appSecret：0d7fe66fea03b11c
     *        排序后得到的串：0d7fe66fea03b11c1565085864573165173c83a80c-51eb-4aa4-8f92-41b8f897c1ed
     *        md5加密后得到的sign：2aac97e9b9c9470a2a68953ecb9e258d
     * @author zhengfei
     * @date 2019-03-29 14:41
     */
    public static String sign(String appId, String requestId, String timestamp, String appSecret) {
        String[] arrs = new String[]{appId, requestId, timestamp, appSecret};
        Arrays.sort(arrs);

        StringBuilder sb = new StringBuilder();
        for (String str : arrs) {
            sb.append(str);
        }
        try {
            return MD5Util.encrypt32(sb.toString());
        } catch (Exception e) {
            throw new WarehouseException("加签异常");
        }
    }
}
