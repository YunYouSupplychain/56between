package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverGenCondition;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverHeaderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 描述：交接单Mapper
 *
 * @author Jianhua on 2020-2-6
 */
@MyBatisMapper
public interface BanQinWmOutHandoverMapper extends BaseMapper<BanQinWmOutHandoverHeader> {

    List<BanQinWmOutHandoverHeaderEntity> findPage(BanQinWmOutHandoverHeaderEntity entity);

    BanQinWmOutHandoverHeaderEntity getEntity(String id);

    BanQinWmOutHandoverHeader getByNo(@Param("handoverNo") String handoverNo, @Param("orgId") String orgId);

    void deleteByNo(@Param("handoverNo") String handoverNo, @Param("orgId") String orgId);

    List<BanQinWmOutHandoverDetail> findDetailList(BanQinWmOutHandoverDetail banQinWmOutHandoverDetail);

    List<BanQinWmOutHandoverDetail> getMeetHandoverData(BanQinWmOutHandoverGenCondition condition);

    void insertDetail(BanQinWmOutHandoverDetail detail);

    void updateDetail(BanQinWmOutHandoverDetail detail);

    void deleteDetail(BanQinWmOutHandoverDetail detail);

    List<String> findNoByCarrierAndHandoverTime(@Param("carrierCode") String carrierCode,
                                                @Param("handoverTimeFm") Date handoverTimeFm,
                                                @Param("handoverTimeTo") Date handoverTimeTo,
                                                @Param("orgId") String orgId);
}
