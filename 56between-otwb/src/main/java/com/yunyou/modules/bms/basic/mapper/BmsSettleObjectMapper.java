package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsSettleObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算对象MAPPER接口
 *
 * @author zqs
 * @version 2018-06-15
 */
@MyBatisMapper
public interface BmsSettleObjectMapper extends BaseMapper<BmsSettleObject> {

    List<BmsSettleObject> findPage(BmsSettleObject entity);

    List<BmsSettleObject> findGrid(BmsSettleObject entity);

    BmsSettleObject getByCode(@Param("code") String code, @Param("orgId") String orgId);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}