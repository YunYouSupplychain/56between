package com.yunyou.core.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.modules.sys.entity.DataRule;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service基类
 *
 * @author yunyou
 * @version 2017-05-16
 */
@Transactional(readOnly = true)
public abstract class BaseService {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 数据范围过滤
     *
     * @param entity 当前过滤的实体类
     */
    public static void dataRuleFilter(BaseEntity<?> entity) {
        User user = UserUtils.getUser();
        entity.setCurrentUser(user);
        // 如果是超级管理员，则不过滤数据
        if (user.isAdmin()) {
            return;
        }
        // 数据范围过滤
        StringBuilder sqlString = new StringBuilder();
        List<DataRule> dataRuleList = UserUtils.getDataRuleList();
        for (DataRule dataRule : dataRuleList) {
            if (entity.getClass().getSimpleName().equals(dataRule.getClassName())) {
                sqlString.append(dataRule.getDataScopeSql());
            }
        }
        if (StringUtils.isNotBlank(entity.getDataScope())) {
            entity.setDataScope(entity.getDataScope() + " " + sqlString);
        } else {
            entity.setDataScope(sqlString.toString());
        }
    }

}
