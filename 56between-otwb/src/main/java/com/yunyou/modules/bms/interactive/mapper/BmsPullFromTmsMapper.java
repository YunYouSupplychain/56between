package com.yunyou.modules.bms.interactive.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsDispatchData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderSiteData;
import com.yunyou.modules.bms.business.entity.BmsWaybillData;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 * <p>
 * create by Jianhua on 2019/7/17
 */
@MyBatisMapper
public interface BmsPullFromTmsMapper {

    List<BmsWaybillData> pullWaybillData(BmsPullDataCondition condition);

    List<BmsDispatchOrderData> pullDispatchOrderData(BmsPullDataCondition condition);

    List<BmsDispatchOrderSiteData> pullDispatchOrderStoreData(@Param("dispatchNo") String dispatchNo, @Param("orgId") String orgId);

    List<BmsDispatchData> pullDispatchData(BmsPullDataCondition condition);

}
