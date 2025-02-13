package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhSkuMapper extends BaseMapper<BanQinCdWhSku> {
	BanQinCdWhSkuEntity getEntity(String id);
	
	List<BanQinCdWhSkuEntity> findPage(BanQinCdWhSkuEntity banQinCdWhSkuEntity);

	List<BanQinCdWhSku> getExistSku(@Param("ownerSkuList") String ownerSkuList, @Param("orgId") String orgId);

	void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<BanQinCdWhSku> subList);
}