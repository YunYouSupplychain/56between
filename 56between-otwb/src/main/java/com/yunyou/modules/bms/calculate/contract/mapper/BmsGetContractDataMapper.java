package com.yunyou.modules.bms.calculate.contract.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.calculate.contract.BmsCalcContractData;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取计算相关合同数据Mapper
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@MyBatisMapper
public interface BmsGetContractDataMapper {

    /**
     * 查找费用计算时合同数据
     *
     * @param modelId 结算模型ID
     * @return 合同数据
     */
    List<BmsCalcContractData> findContractDataByModel(@Param("modelId") String modelId);

    /**
     * 查找费用计算时合同数据
     *
     * @param contactId 合同ID
     * @return 合同数据
     */
    List<BmsCalcContractData> findContractDataByContract(@Param("contactId") String contactId);

    /**
     * 查找费用计算时所需的条款参数数据
     *
     * @param modelDetailId    结算模型明细ID
     * @param includeOrExclude 包含 或 排除
     * @return 条款参数数据
     */
    List<BmsCalcTermsParams> findByModelDetailIdAndIoE(@Param("modelDetailId") String modelDetailId,
                                                       @Param("includeOrExclude") String includeOrExclude);

    /**
     * 查找费用计算时所需的条款参数数据
     *
     * @param contractDetailId 合同明细ID
     * @param includeOrExclude 包含 或 排除
     * @return 条款参数数据
     */
    List<BmsCalcTermsParams> findByContractDetailIdAndIoE(@Param("contractDetailId") String contractDetailId,
                                                          @Param("includeOrExclude") String includeOrExclude);

}
