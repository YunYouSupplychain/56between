package com.yunyou.modules.wms.report.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.report.entity.BanQinWmRepZoneUseRate;
import com.yunyou.modules.wms.report.mapper.BanQinWmRepZoneUseRateMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanQinWmRepZoneUseRateService extends CrudService<BanQinWmRepZoneUseRateMapper, BanQinWmRepZoneUseRate> {

    @Override
    public Page<BanQinWmRepZoneUseRate> findPage(Page<BanQinWmRepZoneUseRate> page, BanQinWmRepZoneUseRate entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("cwz.org_id", entity.getOrgId()));
        entity.setPage(page);
        List<BanQinWmRepZoneUseRate> list = mapper.findPage(entity);
        for (BanQinWmRepZoneUseRate o : list) {
            Integer total = o.getTotal();
            Integer useQty = mapper.getUseQty(o.getZoneCode(), o.getOrgId());
            o.setUse(useQty);
            o.setSpare(total - useQty);
            if (total != 0) {
                o.setRate((useQty * 100 / total) + "%");
            }
        }
        page.setList(list);
        return page;
    }
}
