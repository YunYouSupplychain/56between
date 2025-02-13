package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhQcItemDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检项MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhQcItemDetailMapper extends BaseMapper<BanQinCdWhQcItemDetail> {
	void deleteByHeaderId(String headerId);

	List<BanQinCdWhQcItemDetail> findPage(BanQinCdWhQcItemDetail detail);

	void remove(@Param("itemGroupCode") String itemGroupCode, @Param("orgId") String orgId);
}