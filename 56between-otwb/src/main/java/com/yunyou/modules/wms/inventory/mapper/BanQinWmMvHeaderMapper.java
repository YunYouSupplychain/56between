package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmMvHeader;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintMvOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存移动单Mapper
 */
@MyBatisMapper
public interface BanQinWmMvHeaderMapper extends BaseMapper<BanQinWmMvHeader> {

    BanQinWmMvEntity getEntity(String id);

    List<BanQinWmMvEntity> findPage(BanQinWmMvEntity qEntity);

    BanQinWmMvHeader getByNo(@Param("mvNo") String mvNo, @Param("orgId") String orgId);

    List<PrintMvOrder> getMvOrder(@Param("ids") String[] ids);
}