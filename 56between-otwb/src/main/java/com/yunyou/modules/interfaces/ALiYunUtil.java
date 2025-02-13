package com.yunyou.modules.interfaces;

import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.aliyun.teaopenapi.models.Config;

/**
 * 阿里云接口工具类
 *
 * @author liujianhua
 */
public class ALiYunUtil {

    /**
     * 阿里云语音通知
     *
     * @param accessKeyId     key
     * @param accessKeySecret 密钥
     * @param request         自定义参数
     */
    public static SingleCallByTtsResponse singleCallByTts(String accessKeyId, String accessKeySecret, SingleCallByTtsRequest request) throws Exception {
        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret).setEndpoint("dyvmsapi.aliyuncs.com");
        return new com.aliyun.dyvmsapi20170525.Client(config).singleCallByTts(request);
    }

}