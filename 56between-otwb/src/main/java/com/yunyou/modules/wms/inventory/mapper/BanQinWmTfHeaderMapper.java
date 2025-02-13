package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfHeaderEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintTfOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 转移单MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmTfHeaderMapper extends BaseMapper<BanQinWmTfHeader> {
	
    BanQinWmTfHeaderEntity getEntity(String id);
    
    List<BanQinWmTfHeaderEntity> findPage(BanQinWmTfHeaderEntity banQinWmTfHeaderEntity);

    List<PrintTfOrder> getTfOrder(@Param("ids") String[] ids);
}