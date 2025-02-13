package com.yunyou.modules.tms.authority;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;

import java.util.List;
import java.util.stream.Collectors;

public class TmAuthorityRule {
    private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
    private static TmTransportObjService tmTransportObjService = SpringContextHolder.getBean(TmTransportObjService.class);

    public static String dataRule(String tableName, String orgId) {
        Office office = officeService.get(orgId);
        if (office != null) {
            if (OfficeType.ORG_CENTER.getValue().equals(office.getType())) {
                return "AND EXISTS(SELECT 1 FROM tm_authority_data_org_center where table_name = '" + tableName + "' AND business_id = a.id AND relation_id = '" + orgId + "')";
            } else if (OfficeType.DISPATCH_CENTER.getValue().equals(office.getType())) {
                return "AND EXISTS(SELECT 1 FROM tm_authority_data_dispatch_center where table_name = '" + tableName + "' AND business_id = a.id AND relation_id = '" + orgId + "')";
            } else if (OfficeType.OUTLET.getValue().equals(office.getType()) || OfficeType.WAREHOUSE.getValue().equals(office.getType())) {
                Office organizationCenter = UserUtils.getOrgCenter(orgId);
                if (StringUtils.isNotBlank(organizationCenter.getId())) {
                    List<TmTransportObj> list = tmTransportObjService.findOutletMatchObjByOrgId(orgId, organizationCenter.getId());
                    if (CollectionUtil.isNotEmpty(list)) {
                        return "AND EXISTS(SELECT 1 FROM tm_authority_data_outlet where table_name = '" + tableName + "' AND business_id = a.id AND relation_id IN ('" + list.stream().map(TmTransportObj::getId).collect(Collectors.joining("','")) + "'))";
                    }
                }
            } else if (OfficeType.CARRIER.getValue().equals(office.getType())) {
                Office organizationCenter = UserUtils.getOrgCenter(orgId);
                if (StringUtils.isNotBlank(organizationCenter.getId())) {
                    List<TmTransportObj> list = tmTransportObjService.findCarrierMatchObjByOrgId(orgId, organizationCenter.getId());
                    if (CollectionUtil.isNotEmpty(list)) {
                        return "AND EXISTS(SELECT 1 FROM tm_authority_data_carrier where table_name = '" + tableName + "' AND business_id = a.id AND relation_id IN ('" + list.stream().map(TmTransportObj::getId).collect(Collectors.joining("','")) + "'))";
                    }
                }
            }
        }
        return "AND 1=2";
        /*if (TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue().equals(tableName)) {
            return GlobalDataRule.getDataRuleSql("a.base_org_id", orgId);
        }
        return GlobalDataRule.getDataRuleSql("a.org_id", orgId);*/
    }
}
