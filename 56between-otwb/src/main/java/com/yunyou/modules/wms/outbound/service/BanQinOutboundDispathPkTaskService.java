package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.outbound.entity.BanQinDispatchPkRuleEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分派拣货
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundDispathPkTaskService {
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 分派拣货，生成拣货单号
     *
     * @param waveNos
     * @param ruleEntity
     * @return
     */
    @Transactional
    public ResultMessage outboundDispathPkTask(List<String> waveNos, BanQinDispatchPkRuleEntity ruleEntity, String orgId) {
        ResultMessage msg = new ResultMessage();

        String isByZone = ruleEntity.getIsByZone();
        String isPlTask = ruleEntity.getIsPlTask();
        String isCsTask = ruleEntity.getIsCsTask();
        String isEaTask = ruleEntity.getIsEaTask();
        Double plLimit = ruleEntity.getPlLimit();
        Double csLimit = ruleEntity.getCsLimit();
        Double eaLimit = ruleEntity.getEaLimit();
        Double plFloat = ruleEntity.getPlFloat();
        Double csFloat = ruleEntity.getCsFloat();
        Double eaFloat = ruleEntity.getEaFloat();

        if (WmsConstants.NO.equals(isByZone) && WmsConstants.NO.equals(isPlTask) && WmsConstants.NO.equals(isCsTask) && WmsConstants.NO.equals(isEaTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, null, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(items, null, null);
            this.updatePkNoById(map);
        } else if (WmsConstants.YES.equals(isByZone) && WmsConstants.NO.equals(isPlTask) && WmsConstants.NO.equals(isCsTask) && WmsConstants.NO.equals(isEaTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, null, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> areaMap = this.groupByZoneCode(items);
            for (String key : areaMap.keySet()) {
                Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(areaMap.get(key), null, null);
                this.updatePkNoById(map);
            }
        }
        if (WmsConstants.YES.equals(isByZone) && WmsConstants.YES.equals(isPlTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, WmsConstants.UOM_PL, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> areaPlMap = this.groupByZoneCode(items);
            for (String key : areaPlMap.keySet()) {
                Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(areaPlMap.get(key), plLimit, plFloat);
                this.updatePkNoById(map);
            }
        } else if (WmsConstants.YES.equals(isPlTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, WmsConstants.UOM_PL, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(items, plLimit, plFloat);
            this.updatePkNoById(map);
        }
        if (WmsConstants.YES.equals(isByZone) && WmsConstants.YES.equals(isCsTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, WmsConstants.UOM_CS, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> areaCsMap = this.groupByZoneCode(items);
            for (String key : areaCsMap.keySet()) {
                Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(areaCsMap.get(key), csLimit, csFloat);
                this.updatePkNoById(map);
            }
        } else if (WmsConstants.YES.equals(isCsTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, WmsConstants.UOM_CS, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(items, csLimit, csFloat);
            this.updatePkNoById(map);
        }
        if (WmsConstants.YES.equals(isByZone) && WmsConstants.YES.equals(isEaTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, WmsConstants.UOM_EA, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> areaEaMap = this.groupByZoneCode(items);
            for (String key : areaEaMap.keySet()) {
                Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(areaEaMap.get(key), eaLimit, eaFloat);
                this.updatePkNoById(map);
            }

        } else if (WmsConstants.YES.equals(isEaTask)) {
            List<BanQinWmSoAllocEntity> items = this.getWmSoAllocItemsByWaveNos(waveNos, WmsConstants.UOM_EA, orgId);
            Map<String, List<BanQinWmSoAllocEntity>> map = this.groupByLimit(items, eaLimit, eaFloat);
            this.updatePkNoById(map);
        }
        return msg;
    }

    /**
     * 根据库区分组
     *
     * @param items
     * @return
     */
    public Map<String, List<BanQinWmSoAllocEntity>> groupByZoneCode(List<BanQinWmSoAllocEntity> items) {
        return items.stream().collect(Collectors.groupingBy(BanQinWmSoAllocEntity::getZoneCode));
    }

    /**
     * 根据波次单号获取分配记录
     *
     * @param waveNos
     * @param uom
     * @return
     */
    public List<BanQinWmSoAllocEntity> getWmSoAllocItemsByWaveNos(List<String> waveNos, String uom, String orgId) {
        return wmSoAllocService.findByWaveNos(waveNos, uom, orgId);
    }

    /**
     * 按上限分组
     *
     * @param limitQty
     * @param floatQty
     * @return
     */
    protected Map<String, List<BanQinWmSoAllocEntity>> groupByLimit(List<BanQinWmSoAllocEntity> items, Double limitQty, Double floatQty) {
        Map<String, List<BanQinWmSoAllocEntity>> pkMap = new HashMap<>();
        if (limitQty != null && limitQty != 0.0) {
            int size = items.size();
            Double zs = size / limitQty;
            Double ys = size % limitQty;
            Double fds = null;
            if (floatQty != null && floatQty != 0.0) {
                fds = limitQty * floatQty / 100;
            }
            String pkNo = null;
            for (int i = 0; i < zs.intValue(); i++) {
                pkNo = noService.getDocumentNo(GenNoType.WM_PK_NO.name());
                List<BanQinWmSoAllocEntity> pks = new ArrayList<>();
                pkMap.put(pkNo, pks);
                for (int j = 0; j < limitQty; j++) {
                    pks.add(items.get(i * limitQty.intValue() + j));
                }
            }
            if (ys != 0.0) {
                if (fds != null && ys <= fds) {
                    if (pkNo != null) {
                        List<BanQinWmSoAllocEntity> pks = pkMap.get(pkNo);
                        for (int j = 0; j < ys; j++) {
                            pks.add(items.get(zs.intValue() * limitQty.intValue() + j));
                        }
                    } else {
                        pkNo = noService.getDocumentNo(GenNoType.WM_PK_NO.name());
                        List<BanQinWmSoAllocEntity> pks = new ArrayList<>();
                        pkMap.put(pkNo, pks);
                        for (int j = 0; j < ys; j++) {
                            pks.add(items.get(zs.intValue() * limitQty.intValue() + j));
                        }
                    }
                } else {
                    pkNo = noService.getDocumentNo(GenNoType.WM_PK_NO.name());
                    List<BanQinWmSoAllocEntity> pks = new ArrayList<>();
                    pkMap.put(pkNo, pks);
                    for (int j = 0; j < ys; j++) {
                        pks.add(items.get(zs.intValue() * limitQty.intValue() + j));
                    }
                }
            }
        } else {
            String pkNo = noService.getDocumentNo(GenNoType.WM_PK_NO.name());
            pkMap.put(pkNo, items);
        }
        return pkMap;
    }

    /**
     * 更新分配明细的拣货单号
     *
     * @param map
     */
    @Transactional
    protected void updatePkNoById(Map<String, List<BanQinWmSoAllocEntity>> map) {
        for (String key : map.keySet()) {
            List<BanQinWmSoAllocEntity> items = map.get(key);
            for (BanQinWmSoAllocEntity item : items) {
                jdbcTemplate.update("update wm_so_alloc set pick_no ='" + key + "' where id ='" + item.getId() + "'");
            }
        }
    }
}