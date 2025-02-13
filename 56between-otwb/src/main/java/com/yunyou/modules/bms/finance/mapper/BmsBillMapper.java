package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.BmsBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用账单MAPPER接口
 */
@MyBatisMapper
public interface BmsBillMapper extends BaseMapper<BmsBill> {

    BmsBill getByNo(@Param("confirmNo") String confirmNo, @Param("orgId") String orgId);

    List<BmsBill> findPage(BmsBill qEntity);
}