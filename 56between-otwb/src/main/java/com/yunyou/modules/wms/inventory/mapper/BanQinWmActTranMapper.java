package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTran;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存交易MAPPER接口
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmActTranMapper extends BaseMapper<BanQinWmActTran> {
    List<BanQinWmActTran> findPage(BanQinWmActTran banQinWmActTran);

    List<BanQinWmActTran> checkInitInvQuery(@Param("initInventory") String initInventory, @Param("orgId") String orgId);
}