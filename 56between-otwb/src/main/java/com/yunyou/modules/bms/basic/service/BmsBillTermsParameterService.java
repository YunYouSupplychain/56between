package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsBillTermsParameter;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractDetailTermsParamsEntity;
import com.yunyou.modules.bms.basic.mapper.BmsBillTermsParameterMapper;
import com.yunyou.modules.bms.common.CalcMethod;
import com.yunyou.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计费条款参数Service
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Service
@Transactional(readOnly = true)
public class BmsBillTermsParameterService extends CrudService<BmsBillTermsParameterMapper, BmsBillTermsParameter> {

    public List<BmsBillTermsParameter> findByBillTermsCode(String billTermsCode) {
        return mapper.findByBillTermsCode(billTermsCode);
    }

    @Transactional
    public void remove(String billTermsCode) {
        mapper.remove(billTermsCode);
    }

    public List<BmsContractDetailTermsParamsEntity> getTermsParams(String billTermsCode, String settleObjectCode, String type) {
        List<BmsContractDetailTermsParamsEntity> list = mapper.getTermsParams(billTermsCode);
        for (BmsContractDetailTermsParamsEntity entity : list) {
            entity.setFieldValue(getDefaultFieldValue(entity.getMethodName(), settleObjectCode, type, entity.getField(), entity.getFieldValue()));
            if (StringUtils.isNotBlank(entity.getFieldOption())) {
                entity.setDictValueList(DictUtils.getDictList(entity.getFieldOption()));
            }
        }
        return list;
    }

    /**
     * 获取默认字段值
     *
     * @param methodName        处理方法名称
     * @param settleObjectCode  结算对象编码
     * @param type              客户类型
     * @param field             字段
     * @param defaultFieldValue 条款字段默认值
     */
    private String getDefaultFieldValue(String methodName, String settleObjectCode, String type, String field, String defaultFieldValue) {
        if (StringUtils.isNotBlank(defaultFieldValue)) {
            return defaultFieldValue;
        }
        String fieldValue = "";
        switch (CalcMethod.valueOf(methodName)) {
            case calcInbound:
                if ((field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcOutbound:
                if ((field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))
                        || field.endsWith("customer_code")) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcInventory:
                if ((field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcWaybill:
                if ((field.endsWith("carrier_code") && type.contains(CustomerType.CARRIER.getCode()))
                        || (field.endsWith("principal_code") && type.contains(CustomerType.CUSTOMER.getCode()))
                        || (field.endsWith("ship_code") && type.contains(CustomerType.SHIPPER.getCode()))
                        || (field.endsWith("consignee_code") && type.contains(CustomerType.CONSIGNEE.getCode()))
                        || (field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcDispatchOrder:
                if ((field.endsWith("carrier_code") && type.contains(CustomerType.CARRIER.getCode()))) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcDispatch:
                if ((field.endsWith("carrier_code") && type.contains(CustomerType.CARRIER.getCode()))
                        || (field.endsWith("principal_code") && type.contains(CustomerType.CUSTOMER.getCode()))
                        || (field.endsWith("ship_code") && type.contains(CustomerType.SHIPPER.getCode()))
                        || (field.endsWith("consignee_code") && type.contains(CustomerType.CONSIGNEE.getCode()))
                        || (field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcReturn:
                if ((field.endsWith("carrier_code") && type.contains(CustomerType.CARRIER.getCode()))
                        || (field.endsWith("consignee_code") && type.contains(CustomerType.CONSIGNEE.getCode()))
                        || (field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))) {
                    fieldValue = settleObjectCode;
                }
                break;
            case calcException:
                if ((field.endsWith("carrier_code") && type.contains(CustomerType.CARRIER.getCode()))
                        || (field.endsWith("principal_code") && type.contains(CustomerType.CUSTOMER.getCode()))
                        || (field.endsWith("owner_code") && type.contains(CustomerType.OWNER.getCode()))
                        || (field.endsWith("supplier_code") && type.contains(CustomerType.SUPPLIER.getCode()))
                        || field.endsWith("customer_code")) {
                    fieldValue = settleObjectCode;
                }
                break;
            default:
                break;
        }
        return fieldValue;
    }
}