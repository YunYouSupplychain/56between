package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsContractMapper extends BaseMapper<BmsContract> {

    BmsContractEntity getEntity(String id);

    List<BmsContract> findPage(BmsContract entity);

    BmsContract getByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    int isCited(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    void updateInvalidContractStatus();

}