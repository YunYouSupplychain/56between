package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeaderEntity;
import com.yunyou.modules.wms.report.entity.PackingListLabel;
import com.yunyou.modules.wms.report.entity.WaveCombinePickingLabel;
import com.yunyou.modules.wms.report.entity.WaveSortingLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 波次单MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmWvHeaderMapper extends BaseMapper<BanQinWmWvHeader> {

    BanQinWmWvHeaderEntity getEntity(String id);

    List<BanQinWmWvHeaderEntity> findPage(BanQinWmWvHeaderEntity entity);

    List<BanQinWmWvHeader> findBySoNo(@Param("soNo") String soNo, @Param("orgId") String orgId);
    
    void updateWvHeaderStatusByWave(@Param("waveNos") List<String> waveNos, @Param("userId") String userId, @Param("orgId") String orgId);
    
    void updateWvHeaderStatusBySo(@Param("soNos") List<String> soNos, @Param("userId") String userId, @Param("orgId") String orgId);

    List<WaveSortingLabel> getWaveSorting(List<String> list);

    List<PackingListLabel> getPackingList(List<String> list);

    List<WaveCombinePickingLabel> getWaveCombinePicking(List<String> list);

    void updatePrint(@Param("waveNo")String waveNo, @Param("orgId")String orgId);

    void updateGetWayBillFlag(@Param("waveNo")String waveNo, @Param("orgId")String orgId);

    void updatePrintWayBillFlag(@Param("waveNo")String waveNo, @Param("orgId")String orgId);
}