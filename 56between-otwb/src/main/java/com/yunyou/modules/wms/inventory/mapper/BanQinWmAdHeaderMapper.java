package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdHeaderEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintAdOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调整单MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmAdHeaderMapper extends BaseMapper<BanQinWmAdHeader> {

    BanQinWmAdHeaderEntity getEntity(String id);

    List<BanQinWmAdHeaderEntity> findPage(BanQinWmAdHeaderEntity banQinWmAdHeaderEntity);

    List<PrintAdOrder> getAdOrder(@Param("ids") String[] ids);
}