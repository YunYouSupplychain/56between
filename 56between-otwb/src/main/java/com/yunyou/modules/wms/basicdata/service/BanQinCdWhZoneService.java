package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhZone;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhZoneMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库区Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhZoneService extends CrudService<BanQinCdWhZoneMapper, BanQinCdWhZone> {

    public Page<BanQinCdWhZone> findPage(Page<BanQinCdWhZone> page, BanQinCdWhZone banQinCdWhZone) {
        dataRuleFilter(banQinCdWhZone);
        banQinCdWhZone.setPage(page);
        page.setList(mapper.findPage(banQinCdWhZone));
        return page;
    }

    public BanQinCdWhZone getByCode(String zoneCode, String orgId) {
        BanQinCdWhZone con = new BanQinCdWhZone();
        con.setZoneCode(zoneCode);
        con.setOrgId(orgId);
        List<BanQinCdWhZone> list = this.findList(con);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Transactional
    public void remove(String zoneCode, String orgId) {
        mapper.remove(zoneCode, orgId);
    }

    @Transactional
    public void importFile(List<BanQinCdWhZone> importList, String orgId) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            BanQinCdWhZone data = importList.get(i);
            if (StringUtils.isBlank(data.getZoneCode())) {
                throw new GlobalException("第" + (i + 1) + "行，库区编码不能为空！");
            }
            if (StringUtils.isBlank(data.getZoneName())) {
                throw new GlobalException("第" + (i + 1) + "行，库区名称不能为空！");
            }
            if (StringUtils.isBlank(data.getType())) {
                throw new GlobalException("第" + (i + 1) + "行，库区类型不能为空！");
            }
            if (StringUtils.isBlank(data.getAreaCode())) {
                throw new GlobalException("第" + (i + 1) + "行，区域编码不能为空！");
            }
            BanQinCdWhZone cdWhZone = this.getByCode(data.getZoneCode(), orgId);
            if (cdWhZone != null) {
                throw new GlobalException("第" + (i + 1) + "行，库区已存在！");
            }
            data.setOrgId(orgId);
            this.save(data);
        }
    }
}