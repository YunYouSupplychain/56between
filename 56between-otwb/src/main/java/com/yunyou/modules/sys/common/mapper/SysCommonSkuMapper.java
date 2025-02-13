package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonSku;

import java.util.List;

/**
 * 商品信息MAPPER接口
 * @author WMJ
 * @version 2019-05-30
 */
@MyBatisMapper
public interface SysCommonSkuMapper extends BaseMapper<SysCommonSku> {

    List<SysCommonSku> findPage(SysCommonSku entity);

    List<SysCommonSku> findGrid(SysCommonSku entity);

    List<SysCommonSku> findSync(SysCommonSku entity);

    void batchInsert(List<SysCommonSku> list);
}