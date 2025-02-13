package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeaderEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmWvHeaderMapper;
import com.yunyou.modules.wms.report.entity.PackingListLabel;
import com.yunyou.modules.wms.report.entity.WaveCombinePickingLabel;
import com.yunyou.modules.wms.report.entity.WaveSortingLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 波次单Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmWvHeaderService extends CrudService<BanQinWmWvHeaderMapper, BanQinWmWvHeader> {
    @Autowired
    private BanQinWmSoHeaderService banQinWmSoHeaderService;

    public BanQinWmWvHeader get(String id) {
        return super.get(id);
    }

    public BanQinWmWvHeaderEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public List<BanQinWmWvHeader> findList(BanQinWmWvHeader banQinWmWvHeader) {
        return super.findList(banQinWmWvHeader);
    }

    public Page<BanQinWmWvHeaderEntity> findPage(Page page, BanQinWmWvHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmWvHeader banQinWmWvHeader) {
        super.save(banQinWmWvHeader);
    }

    @Transactional
    public void delete(BanQinWmWvHeader banQinWmWvHeader) {
        super.delete(banQinWmWvHeader);
    }

    public BanQinWmWvHeader findFirst(BanQinWmWvHeader example) {
        List<BanQinWmWvHeader> list = mapper.findList(example);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public BanQinWmWvHeader findByWaveNo(String waveNo, String orgId) {
        BanQinWmWvHeader example = new BanQinWmWvHeader();
        example.setWaveNo(waveNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    public List<BanQinWmWvHeader> findBySoNo(String soNo, String orgId) {
        return mapper.findBySoNo(soNo, orgId);
    }

    /**
     * 描述： 查询波次单Entity
     * @param waveNo 波次单号
     * @param orgId
     */
    public BanQinWmWvEntity findEntityByWaveNo(String waveNo, String orgId) {
        BanQinWmWvEntity entity = null;

        BanQinWmWvHeader wmWvHeader = this.findByWaveNo(waveNo, orgId);
        if (wmWvHeader != null) {
            entity = (BanQinWmWvEntity) wmWvHeader;
        }
        return entity;
    }

    /**
     * 描述： 查询波次单Entity
     * @param soNo  出库单号
     * @param orgId
     */
    public List<BanQinWmWvEntity> findEntityBySoNo(String soNo, String orgId) {
        List<BanQinWmWvEntity> entities = Lists.newArrayList();
        List<BanQinWmWvHeader> wmWvHeaders = this.findBySoNo(soNo, orgId);
        if (CollectionUtil.isNotEmpty(wmWvHeaders)) {
            for (BanQinWmWvHeader wmWvHeader : wmWvHeaders) {
                BanQinWmWvEntity entity = (BanQinWmWvEntity) wmWvHeader;
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * 描述： 查询波次单Entity
     * @param soIds 出库单ID
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmWvEntity> findEntityBySo(List<String> soIds) {
        List<BanQinWmWvEntity> entities = Lists.newArrayList();
        for (String soId : soIds) {
            BanQinWmSoHeader wmSoHeader = banQinWmSoHeaderService.get(soId);
            List<BanQinWmWvEntity> wmWvEntities = this.findEntityBySoNo(wmSoHeader.getSoNo(), wmSoHeader.getOrgId());
            if (CollectionUtil.isNotEmpty(wmWvEntities) && wmWvEntities.size() > 0) {
                entities.addAll(wmWvEntities);
            }
        }
        // 过滤重复的波次单Entity
        if (entities.size() > 0) {
            return entities.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BanQinWmWvEntity::getWaveNo))), ArrayList::new));
        }
        return entities;
    }

    @Transactional
    public BanQinWmWvHeader saveWvHeader(BanQinWmWvHeader wvHeader) {
        this.save(wvHeader);
        return wvHeader;
    }

    @Transactional
    public void updateWvHeaderStatusByWave(List<String> waveNos, String userId, String orgId) {
        mapper.updateWvHeaderStatusByWave(waveNos, userId, orgId);
    }

    @Transactional
    public void updateWvHeaderStatusBySo(List<String> soNos, String userId, String orgId) {
        mapper.updateWvHeaderStatusBySo(soNos, userId, orgId);
    }

    public List<WaveSortingLabel> getWaveSorting(List<String> waveIds) {
        return mapper.getWaveSorting(waveIds);
    }

    @Transactional
    public List<PackingListLabel> getPacingList(List<String> waveIds) {
        List<PackingListLabel> packingList = mapper.getPackingList(waveIds);
        if (CollectionUtil.isNotEmpty(packingList)) {
            List<String> collect = packingList.stream().map(PackingListLabel::getWaveNo).distinct().collect(Collectors.toList());
            String orgId = packingList.get(0).getOrgId();
            for (String waveNo : collect) {
                mapper.updatePrint(waveNo, orgId);
            }
        }
        return packingList;
    }

    public List<WaveCombinePickingLabel> getWaveCombinePicking(List<String> waveIds) {
        List<WaveCombinePickingLabel> list = mapper.getWaveCombinePicking(waveIds);
        Map<String, List<WaveCombinePickingLabel>> collect = list.stream().collect(Collectors.groupingBy(s -> s.getWaveNo() + s.getZoneCode() + s.getLocCode() + s.getTraceId() + s.getSkuCode() + s.getLotNum() + s.getToLoc() + s.getToId()));
        List<WaveCombinePickingLabel> result = Lists.newArrayList();
        collect.forEach((k, v) -> {
            double boxSum = v.stream().mapToDouble(WaveCombinePickingLabel::getQtyBox).sum();
            double sum = v.stream().mapToDouble(WaveCombinePickingLabel::getQtyEa).sum();
            v.forEach(w -> w.setQtyBox(boxSum));
            v.forEach(w -> w.setQtyEa(sum));
            result.add(v.get(0));
        });
        return result.stream().sorted(Comparator.comparing(WaveCombinePickingLabel::getWaveNo)
                    .thenComparing(WaveCombinePickingLabel::getPickNo, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(WaveCombinePickingLabel::getPkSeq, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
    }

    @Transactional
    public void updateGetWayBillFlag(String waveNo, String orgId) {
        mapper.updateGetWayBillFlag(waveNo, orgId);
    }

    @Transactional
    public void updatePrintWayBillFlag(String waveNo, String orgId) {
        mapper.updatePrintWayBillFlag(waveNo, orgId);
    }

}