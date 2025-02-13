package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库单明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmAsnDetailMapper extends BaseMapper<BanQinWmAsnDetail> {
    
    List<BanQinWmAsnDetailEntity> findPage(BanQinWmAsnDetailEntity banQinWmAsnDetail);

    void updateLogisticNo(@Param("asnNo") String asnNo, @Param("logisticNo") String logisticNo, @Param("orgId") String orgId);

    List<BanQinWmAsnDetail> getPalletizeDetailByHeadId(@Param("headId") String headId, @Param("isPalletize") String isPalletize);

    BanQinWmAsnDetailEntity getByAsnNoAndLineNo(@Param("asnNo") String asnNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    Integer getMaxLineNo(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    List<BanQinWmAsnDetail> getByAsnNoAndLineNos(@Param("asnNo") String asnNo, @Param("lineNos") String[] lineNos, @Param("orgId") String orgId);

    List<String> getCheckQcLine(@Param("asnId") String asnId, @Param("lineNos") String[] lineNos, @Param("qcStatus") String qcStatus);

    List<String> checkAsnDetailStatusQuery(@Param("asnId") String asnId, @Param("asnStatus") String[] asnStatus, @Param("lineNos") String[] lineNos,
                                           @Param("lineStatus") String[] lineStatus, @Param("auditStatus") String[] auditStatus, @Param("holdStatus") String[] holdStatus,
                                           @Param("isPalletize") String isPalletize, @Param("isCrossDock") String isCrossDock, @Param("isArrangeLoc") String isArrangeLoc);
}