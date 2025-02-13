package com.yunyou.modules.sys.utils;

import com.yunyou.common.config.CacheNames;
import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.modules.sys.entity.SysControlParams;
import com.yunyou.modules.sys.service.SysControlParamsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统数据字典工具类
 *
 * @author WMJ
 * @version 2020-01-15
 */
public class SysControlParamsUtils {
    private static final SysControlParamsService sysControlParamsService = SpringContextHolder.getBean(SysControlParamsService.class);
    private static final RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

    public static String getValue(String paramCode) {
        return getValue(paramCode, null);
    }

    public static String getValue(String paramCode, String orgId) {
        if (StringUtils.isBlank(paramCode)) {
            return null;
        }
        String result = null;
        @SuppressWarnings("unchecked")
        Map<String, String> paramMap = (Map<String, String>) redisUtils.get(CacheNames.SYS_CACHE_CONTROL_PARAMS_MAP);
        if (null == paramMap) {
            List<SysControlParams> list = sysControlParamsService.findList(new SysControlParams());
            if (CollectionUtil.isNotEmpty(list)) {
                setCacheParamsMap(list);
                result = getParamValue(list, paramCode, orgId);
                if (StringUtils.isBlank(result) && StringUtils.isNotBlank(orgId)) {
                    result = getParamValue(list, paramCode, null);
                }
            }
        } else {
            result = paramMap.get(paramCode + "@" + StringUtils.stripToEmpty(orgId));
            if (StringUtils.isBlank(result) && StringUtils.isNotBlank(orgId)) {
                result = paramMap.get(paramCode + "@" + StringUtils.stripToEmpty(null));
            }
        }
        return result;
    }

    private static void setCacheParamsMap(List<SysControlParams> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, String> map = MapUtil.newHashMap();
            for (SysControlParams params : list) {
                map.put(params.getCode() + "@" + StringUtils.stripToEmpty(params.getOrgId()), params.getValue());
            }
            redisUtils.set(CacheNames.SYS_CACHE_CONTROL_PARAMS_MAP, map);
        }
    }

    private static String getParamValue(List<SysControlParams> list, String paramCode, String orgId) {
        String paramValue = "";
        if (StringUtils.isBlank(paramCode)) return paramValue;
        Optional<String> first = list.stream()
                .filter(s -> paramCode.equals(s.getCode()) && StringUtils.stripToEmpty(orgId).equals(StringUtils.stripToEmpty(s.getOrgId())))
                .map(SysControlParams::getValue).findFirst();
        if (first.isPresent()) {
            paramValue = first.get();
        }
        return paramValue;
    }

}