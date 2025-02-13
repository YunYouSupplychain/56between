package com.yunyou.modules.sys.utils;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.common.entity.SysCommonDataSet;
import com.yunyou.modules.sys.common.entity.extend.SysDataSetOrgRelationEntity;
import com.yunyou.modules.sys.common.service.SysCommonDataSetService;
import com.yunyou.modules.sys.common.service.SysDataSetOrgRelationService;
import com.yunyou.modules.sys.entity.User;

/**
 * 数据套工具类
 */
public class SysDataSetUtils {
    private static final SysDataSetOrgRelationService sysDataSetOrgRelationService = SpringContextHolder.getBean(SysDataSetOrgRelationService.class);
    private static final SysCommonDataSetService sysCommonDataSetService = SpringContextHolder.getBean(SysCommonDataSetService.class);
    private static final String USER_DATA_SET_PREFIX = "ds_";

    /**
     * 获取用户当前操作数据套，仅支持已登陆用户使用
     */
    public static SysCommonDataSet getUserDataSet() {
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(user.getId())) {
            throw new RuntimeException("用户未登录");
        }
        SysCommonDataSet cache = (SysCommonDataSet) UserUtils.getCache(USER_DATA_SET_PREFIX + user.getId());
        if (cache != null) {
            return cache;
        }
        if (user.getOffice() == null || StringUtils.isBlank(user.getOffice().getId())) {
            SysCommonDataSet aDefault = sysCommonDataSetService.getDefault();
            pushUserDataSetCache(aDefault);
            return aDefault;
        }
        SysDataSetOrgRelationEntity relation = sysDataSetOrgRelationService.getEntityByOrgId(user.getOffice().getId());
        if (relation == null) {
            SysCommonDataSet aDefault = sysCommonDataSetService.getDefault();
            pushUserDataSetCache(aDefault);
            return aDefault;
        }
        SysCommonDataSet sysCommonDataSet = sysCommonDataSetService.getByCode(relation.getDataSet());
        if (sysCommonDataSet == null) {
            SysCommonDataSet aDefault = sysCommonDataSetService.getDefault();
            pushUserDataSetCache(aDefault);
            return aDefault;
        }
        pushUserDataSetCache(sysCommonDataSet);
        return sysCommonDataSet;
    }

    /**
     * 仅允许用户在初始化和切换默认数据套时调用
     */
    public static void pushUserDataSetCache(SysCommonDataSet sysCommonDataSet) {
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(user.getId())) {
            throw new RuntimeException("用户未登录");
        }
        UserUtils.putCache(USER_DATA_SET_PREFIX + user.getId(), sysCommonDataSet);
    }
}
