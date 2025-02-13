package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsTransportObj;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务对象信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsTransportObjMapper extends BaseMapper<SysTmsTransportObj> {

    SysTmsTransportObjEntity getEntity(SysTmsTransportObj tmTransportObj);

    List<SysTmsTransportObjEntity> findPage(SysTmsTransportObjEntity entity);

    List<SysTmsTransportObjEntity> findGrid(SysTmsTransportObjEntity entity);

    List<SysTmsTransportObjEntity> findSettleGrid(SysTmsTransportObjEntity entity);

    void batchInsert(List<SysTmsTransportObj> list);

    void remove(@Param("customerNo") String customerNo, @Param("dataSet") String dataSet);
}