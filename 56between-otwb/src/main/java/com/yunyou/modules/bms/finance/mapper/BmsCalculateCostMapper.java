package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcConditionParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 计算费用Mapper
 * 该Mapper中提供了费用计算时涉及到的数据查询方法，提取计算所用的配置数据、业务数据等
 *
 * @author liujianhua
 * @date 2022/6/21
 */
@MyBatisMapper
public interface BmsCalculateCostMapper {

    /**
     * 查找费用计算时所需的条件参数数据
     *
     * @param modelId 结算模型ID
     * @return 条件参数数据
     */
    List<BmsCalcConditionParams> findConditionParamsByModel(@Param("modelId") String modelId);

    /**
     * 查找费用计算时所需的条件参数数据
     *
     * @param contractId 合同ID
     * @return 条件参数数据
     */
    List<BmsCalcConditionParams> findConditionParamsByContract(@Param("contractId") String contractId);

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

    /**
     * 查找费用计算时所需的入库业务数据
     *
     * @param dataScope 过滤条件
     * @return 入库业务数据
     */
    <T> List<DataEntity<T>> findInboundData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的出库业务数据
     *
     * @param dataScope 过滤条件
     * @return 出库业务数据
     */
    <T> List<DataEntity<T>> findOutboundData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的库存业务数据
     *
     * @param dataScope 过滤条件
     * @return 库存业务数据
     */
    <T> List<DataEntity<T>> findInventoryData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的派车单业务数据
     *
     * @param dataScope 过滤条件
     * @return 派车单业务数据
     */
    <T> List<DataEntity<T>> findDispatchOrderData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的派车配载业务数据
     *
     * @param dataScope 过滤条件
     * @return 派车配载业务数据
     */
    <T> List<DataEntity<T>> findDispatchData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的退货业务数据
     *
     * @param dataScope 过滤条件
     * @return 退货业务数据
     */
    <T> List<DataEntity<T>> findReturnData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的异常业务数据
     *
     * @param dataScope 过滤条件
     * @return 异常业务数据
     */
    <T> List<DataEntity<T>> findExceptionData(@Param("dataScope") String dataScope);

    /**
     * 查找费用计算时所需的运单业务数据
     *
     * @param dataScope 过滤条件
     * @return 运单业务数据
     */
    <T> List<DataEntity<T>> findWaybillData(@Param("dataScope") String dataScope);
}
