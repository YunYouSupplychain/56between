package com.yunyou.modules.wms.qc.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcEntity;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检单MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmQcHeaderMapper extends BaseMapper<BanQinWmQcHeader> {

    void updateStatus(@Param("qcNo") String qcNo, @Param("orgId") String orgId);

    BanQinWmQcEntity getEntity(String id);

    List<BanQinWmQcHeader> getByIds(@Param("qcIds") String[] qcIds);

    List<BanQinWmQcEntity> findPage(BanQinWmQcEntity entity);

    List<String> checkQcStatusQuery(@Param("qcIds") String[] qcIds, @Param("status") String[] status, @Param("auditStatus") String[] auditStatus, @Param("isQcSuggest") String isQcSuggest);
}