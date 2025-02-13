package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsContractCostItem;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractCostItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同费用项目MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsContractCostItemMapper extends BaseMapper<BmsContractCostItem> {

    BmsContractCostItemEntity getEntity(String id);

    List<BmsContractCostItemEntity> findByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    BmsContractCostItem getOnly(@Param("sysContractNo") String sysContractNo, @Param("billSubjectCode") String billSubjectCode, @Param("billTermsCode") String billTermsCode, @Param("orgId") String orgId);

    void deleteByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    int isCited(@Param("id") String id, @Param("orgId") String orgId);
}