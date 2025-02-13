package com.yunyou.modules.sys.utils;

import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.config.CacheNames;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.sys.entity.DictType;
import com.yunyou.modules.sys.entity.DictValue;
import com.yunyou.modules.sys.service.DictTypeService;
import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author yunyou
 * @version 2016-5-29
 */
public class DictUtils {
    private static final DictTypeService dictTypeService = SpringContextHolder.getBean(DictTypeService.class);
    private static final RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

    public static String getDictLabel(String value, String type, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            for (DictValue dictValue : getDictList(type)) {
                if (value.equals(dictValue.getValue())) {
                    return dictValue.getLabel();
                }
            }
        }
        return defaultLabel;
    }

    public static String getDictLabels(String values, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
            List<String> valueList = Lists.newArrayList();
            for (String value : StringUtils.split(values, ",")) {
                valueList.add(getDictLabel(value, type, defaultValue));
            }
            return StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    public static String getDictValue(String label, String type, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
            for (DictValue dictValue : getDictList(type)) {
                if (label.equals(dictValue.getLabel())) {
                    return dictValue.getValue();
                }
            }
        }
        return defaultLabel;
    }

    public static List<DictValue> getDictList(String type) {
        @SuppressWarnings("unchecked")
        Map<String, List<DictValue>> dictMap = (Map<String, List<DictValue>>) redisUtils.get(CacheNames.SYS_CACHE_DICT_MAP);
        if (dictMap == null) {
            dictMap = Maps.newHashMap();
            for (DictType dictType : dictTypeService.findList(new DictType())) {
                List<DictValue> dictList = dictMap.get(dictType.getType());
                dictType = dictTypeService.get(dictType.getId());
                if (dictList != null) {
                    dictList.addAll(dictType.getDictValueList());
                } else {
                    dictMap.put(dictType.getType(), Lists.newArrayList(dictType.getDictValueList()));
                }
            }
            redisUtils.set(CacheNames.SYS_CACHE_DICT_MAP, dictMap);
        }
        List<DictValue> dictList = dictMap.get(type);
        if (dictList == null) {
            dictList = Lists.newArrayList();
        }
        return dictList;
    }

    /**
     * 反射根据对象和属性名获取属性值
     */
    public static Object getValue(Object obj, String filed) {
        try {
            Class clazz = obj.getClass();
            PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);
            Method method = pd.getReadMethod();
            return method.invoke(obj);
        } catch (SecurityException | IllegalArgumentException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
