package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务对象信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmTransportObjMapper extends BaseMapper<TmTransportObj> {

    TmTransportObjEntity getEntity(TmTransportObj tmTransportObj);

    List<TmTransportObjEntity> findPage(TmTransportObj tmTransportObj);

    List<TmTransportObjEntity> findGrid(TmTransportObjEntity entity);

    List<TmTransportObjEntity> findSettleGrid(TmTransportObjEntity entity);

    List<TmTransportObjEntity> findOutletGrid(TmTransportObjEntity entity);

    void remove(@Param("transportObjCode") String transportObjCode, @Param("orgId") String orgId);

    void batchInsert(List<TmTransportObj> list);

}