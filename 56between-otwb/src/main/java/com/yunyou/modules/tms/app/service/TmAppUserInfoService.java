package com.yunyou.modules.tms.app.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.app.entity.TmAppUserInfo;
import com.yunyou.modules.tms.app.entity.extend.TmAppUserInfoEntity;
import com.yunyou.modules.tms.app.mapper.TmAppUserInfoMapper;
import com.yunyou.modules.tms.basic.entity.extend.TmDriverEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.applet.AppConstants;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * app用户信息Service
 *
 * @author zyf
 * @version 2020-06-29
 */
@Service
@Transactional(readOnly = true)
public class TmAppUserInfoService extends CrudService<TmAppUserInfoMapper, TmAppUserInfo> {

    public void saveValidator(TmAppUserInfo tmAppUserInfo) {
        if (StringUtils.isBlank(tmAppUserInfo.getLoginName())) {
            throw new TmsException("登录名不能为空");
        }
        if (StringUtils.isBlank(tmAppUserInfo.getName())) {
            throw new TmsException("姓名不能为空");
        }
        if (StringUtils.isBlank(tmAppUserInfo.getPassword())) {
            throw new TmsException("密码不能为空");
        }
        TmAppUserInfo checkExist = new TmAppUserInfo();
        checkExist.setLoginName(tmAppUserInfo.getLoginName());
        List<TmAppUserInfo> list = findList(checkExist);
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmAppUserInfo.getId()))) {
                throw new TmsException("用户[" + tmAppUserInfo.getLoginName() + "]已存在");
            }
        }
    }

    public TmAppUserInfoEntity getEntity(String id) {
        return mapper.getEntity(new TmAppUserInfo(id));
    }

    @SuppressWarnings("unchecked")
    public Page<TmAppUserInfoEntity> findPage(Page page, TmAppUserInfo tmAppUserInfo) {
        dataRuleFilter(tmAppUserInfo);
        tmAppUserInfo.setPage(page);
        List<TmAppUserInfoEntity> list = mapper.findPage(tmAppUserInfo);
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportObjEntity> findTransportObjGrid(Page page, TmTransportObjEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findTransportObjGrid(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDriverEntity> findDriverGrid(Page page, TmDriverEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findDriverGrid(entity));
        return page;
    }

    public TmAppUserInfoEntity getByType(String code, String loginType) {
        TmAppUserInfo condition = new TmAppUserInfo();
        if (AppConstants.LOGIN_TYPE_NAME.equals(loginType)) {
            condition.setLoginName(code);
        } else if (AppConstants.LOGIN_TYPE_WX.equals(loginType)){
            condition.setDef1(code);
        } else {
            return null;
        }
        List<TmAppUserInfoEntity> entityList = mapper.findPage(condition);
        if (CollectionUtil.isNotEmpty(entityList)) {
            return entityList.get(0);
        } else {
            return null;
        }
    }
}