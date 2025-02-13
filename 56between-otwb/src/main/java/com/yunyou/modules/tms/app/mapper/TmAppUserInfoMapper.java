package com.yunyou.modules.tms.app.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.app.entity.TmAppUserInfo;
import com.yunyou.modules.tms.app.entity.extend.TmAppUserInfoEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmDriverEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;

import java.util.List;

/**
 * app用户信息MAPPER接口
 *
 * @author zyf
 * @version 2020-06-29
 */
@MyBatisMapper
public interface TmAppUserInfoMapper extends BaseMapper<TmAppUserInfo> {

    TmAppUserInfoEntity getEntity(TmAppUserInfo tmAppUserInfo);

    List<TmAppUserInfoEntity> findPage(TmAppUserInfo tmAppUserInfo);

    List<TmTransportObjEntity> findTransportObjGrid(TmTransportObjEntity entity);

    List<TmDriverEntity> findDriverGrid(TmDriverEntity entity);
}