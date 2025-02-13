package com.yunyou.core.action;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.modules.sys.entity.DataRule;
import com.yunyou.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Action基类
 */
public abstract class BaseAction {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 数据范围过滤
     *
     * @param entity 当前过滤的实体类
     */
    public static void dataRuleFilter(BaseEntity<?> entity) {
        entity.setCurrentUser(UserUtils.getUser());
        List<DataRule> dataRuleList = UserUtils.getDataRuleList();
        // 如果是超级管理员，则不过滤数据
        if (dataRuleList.size() == 0) {
            return;
        }
        // 数据范围
        StringBuilder sqlString = new StringBuilder();
        for (DataRule dataRule : dataRuleList) {
            if (entity.getClass().getSimpleName().equals(dataRule.getClassName())) {
                sqlString.append(dataRule.getDataScopeSql());
            }
        }
        if (StringUtils.isNotEmpty(entity.getDataScope())) {
            entity.setDataScope(entity.getDataScope()  + " " + sqlString.toString());
        } else {
            entity.setDataScope(sqlString.toString());
        }
    }
}
