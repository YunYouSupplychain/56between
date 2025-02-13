package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.SysRuleGenNo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 编号生成规则MAPPER接口
 *
 * @author 张庆生
 * @version 2017-10-17
 */
@MyBatisMapper
public interface SysRuleGenNoMapper extends BaseMapper<SysRuleGenNo> {

    SysRuleGenNo getByType(@Param("billType") String billType);

    void updateByType(@Param("billType") String billType, @Param("currentSerialNo") Integer currentSerialNo);

    List<SysRuleGenNo> findPage(SysRuleGenNo entity);
}