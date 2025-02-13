package com.yunyou.modules.wms.qc.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSku;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检单商品MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmQcSkuMapper extends BaseMapper<BanQinWmQcSku> {

    Integer getMaxLineNo(@Param("qcNo") String qcNo, @Param("orgId") String orgId);

    List<BanQinWmQcSku> findPctQuaIsNull(@Param("qcNo") String qcNo, @Param("orgId") String orgId);

    List<BanQinWmQcSku> findCanCancelDetail(@Param("qcNo") String qcNo, @Param("lineNos") String[] lineNo, @Param("status") String[] status, @Param("qcPhase") String qcPhase, @Param("orgId") String orgId);

    List<BanQinWmQcSku> findUnCancelAndClose(@Param("orderNo") String orderNo, @Param("orgId") String orgId);

    void removeByQcNoAndLineNo(@Param("qcNo") String qcNo, @Param("lineNos") String[] lineNos, @Param("orgId") String orgId);

    List<BanQinWmQcSkuEntity> findPage(BanQinWmQcSkuEntity entity);

    BanQinWmQcSku getWmQcPctQuaQuery(@Param("qcNo") String qcNo, @Param("qcLineNo") String qcLineNo, @Param("orgId") String orgId);
}