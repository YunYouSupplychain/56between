package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhQcItemHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检项MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhQcItemHeaderMapper extends BaseMapper<BanQinCdWhQcItemHeader> {
    List<BanQinCdWhQcItemHeader> findPage(BanQinCdWhQcItemHeader cdWhQcItemHeader);

    BanQinCdWhQcItemHeader getByCode(@Param("itemGroupCode") String itemGroupCode, @Param("orgId") String orgId);

    void remove(@Param("itemGroupCode") String itemGroupCode, @Param("orgId") String orgId);
}