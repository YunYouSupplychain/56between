package com.yunyou.core.initial;

import com.google.common.collect.Maps;
import com.yunyou.common.config.CacheNames;
import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.entity.DictType;
import com.yunyou.modules.sys.entity.DictValue;
import com.yunyou.modules.sys.entity.SysControlParams;
import com.yunyou.modules.sys.service.DictTypeService;
import com.yunyou.modules.sys.service.SysControlParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Spring容器启动后初始化
 *
 * @author yunyou
 * @since 2023/3/28 11:04
 */
@Component
public class InitialCacheDataListener implements SmartApplicationListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return aClass == ContextRefreshedEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        ApplicationContext applicationContext = ((ContextRefreshedEvent) applicationEvent).getApplicationContext();
        // 仅Spring容器（根容器）初始化以下内容
        if (applicationContext.getParent() == null) {
            try {
                initControlParams(applicationContext);
                initDict(applicationContext);
            } catch (Exception ex) {
                logger.error("Redis初始化加载数据失败", ex);
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private void initControlParams(ApplicationContext applicationContext) {
        RedisUtils redisUtils = applicationContext.getBean(RedisUtils.class);
        SysControlParamsService sysControlParamsService = applicationContext.getBean(SysControlParamsService.class);

        Map<String, String> map = Maps.newHashMap();
        List<SysControlParams> list = sysControlParamsService.findList(new SysControlParams());
        for (SysControlParams params : list) {
            map.put(params.getCode() + "@" + StringUtils.stripToEmpty(params.getOrgId()), params.getValue());
        }
        redisUtils.set(CacheNames.SYS_CACHE_CONTROL_PARAMS_MAP, map);
    }

    private void initDict(ApplicationContext applicationContext) {
        RedisUtils redisUtils = applicationContext.getBean(RedisUtils.class);
        DictTypeService dictTypeService = applicationContext.getBean(DictTypeService.class);

        Map<String, List<DictValue>> map = Maps.newHashMap();
        List<DictType> dictTypeList = dictTypeService.findList(new DictType());
        for (DictType dictType : dictTypeList) {
            dictType = dictTypeService.get(dictType.getId());
            map.put(dictType.getType(), dictType.getDictValueList());
        }
        redisUtils.set(CacheNames.SYS_CACHE_DICT_MAP, map);
    }
}
