package com.yunyou.modules.wms.qc.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetail;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检单明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmQcDetailMapper extends BaseMapper<BanQinWmQcDetail> {

    Integer getMaxLineNo(@Param("qcNo") String qcNo, @Param("orgId") String orgId);

    void updateStatus(@Param("qcNo") String qcNo, @Param("qcLineNo") String qcLineNo, @Param("status") String status, @Param("orgId") String orgId);

    void updateQcQuaPaIdNull(@Param("paId") String paId, @Param("orgId") String orgId);

    void updateQcUnquaPaIdNull(@Param("paId") String paId, @Param("orgId") String orgId);

    void removeByQcNoAndQcLineNo(@Param("qcNo") String qcNo, @Param("qcLineNos") String[] qcLineNos, @Param("orgId") String orgId);

    List<BanQinWmQcDetailEntity> findPage(BanQinWmQcDetailEntity entity);

    List<BanQinWmQcDetailEntity> getWmQcDetailByQcRcvId(BanQinWmQcDetailEntity entity);

}