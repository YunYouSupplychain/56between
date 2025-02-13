package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverGenCondition;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmOutHandoverHeaderEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmOutHandoverMapper;
import com.yunyou.modules.wms.report.entity.OutHandoverListLabel;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

/**
 * 描述：交接单Service
 *
 * @author Jianhua on 2020-2-6
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmOutHandoverService extends CrudService<BanQinWmOutHandoverMapper, BanQinWmOutHandoverHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinEbCustomerService banQinEbCustomerService;

    public Page<BanQinWmOutHandoverHeaderEntity> findPage(Page page, BanQinWmOutHandoverHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<BanQinWmOutHandoverDetail> findDetailPage(Page<BanQinWmOutHandoverDetail> page, BanQinWmOutHandoverDetail banQinWmOutHandoverDetail) {
        banQinWmOutHandoverDetail.setPage(page);
        page.setList(mapper.findDetailList(banQinWmOutHandoverDetail));
        return page;
    }

    /**
     * 描述：据分配ID查询交接明细单根
     *
     * @author Jianhua on 2020-2-9
     */
    public BanQinWmOutHandoverDetail getDetailByAllocId(String allocId, String orgId) {
        if (StringUtils.isBlank(allocId) || StringUtils.isBlank(orgId)) return null;

        BanQinWmOutHandoverDetail detail = new BanQinWmOutHandoverDetail();
        detail.setAllocId(allocId);
        detail.setOrgId(orgId);
        List<BanQinWmOutHandoverDetail> detailList = mapper.findDetailList(detail);
        if (CollectionUtil.isNotEmpty(detailList) && detailList.size() == 1) {
            return detailList.get(0);
        }
        return null;
    }

    @Transactional
    public void save(BanQinWmOutHandoverHeader entity) {
        super.save(entity);
    }

    /**
     * 描述：保存交接单明细
     *
     * @author Jianhua on 2020-2-6
     */
    @Transactional
    public void saveDetail(BanQinWmOutHandoverDetail detail) {
        if (StringUtils.isBlank(detail.getHandoverNo())) {
            throw new WarehouseException("交接单号为空");
        }
        if (BanQinWmOutHandoverDetail.DEL_FLAG_NORMAL.equals(detail.getDelFlag())) {
            if (StringUtils.isEmpty(detail.getId())) {
                detail.preInsert();
                mapper.insertDetail(detail);
            } else {
                detail.preUpdate();
                mapper.updateDetail(detail);
            }
        } else {
            mapper.deleteDetail(detail);
        }
    }

    /**
     * 描述：根据ID查询交接单
     *
     * @author Jianhua on 2020-2-6
     */
    public BanQinWmOutHandoverHeaderEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：根据ID删除交接单
     *
     * @author Jianhua on 2020-2-6
     */
    @Transactional
    public void deleteEntity(String id) {
        BanQinWmOutHandoverHeader wmOutHandoverHeader = mapper.get(id);
        if (wmOutHandoverHeader != null) {
            // 删除交接单头
            mapper.delete(wmOutHandoverHeader);
            // 删除交接单明细
            mapper.deleteDetail(new BanQinWmOutHandoverDetail(wmOutHandoverHeader.getHandoverNo(), wmOutHandoverHeader.getOrgId()));
        }
    }

    /**
     * 描述：根据单号删除交接单
     *
     * @author Jianhua on 2020-2-6
     */
    @Transactional
    public void deleteEntity(String handoverNo, String orgId) {
        // 删除交接单头
        mapper.deleteByNo(handoverNo, orgId);
        // 删除交接单明细
        mapper.deleteDetail(new BanQinWmOutHandoverDetail(handoverNo, orgId));
    }

    /**
     * 描述：根据ID删除交接单明细
     *
     * @author Jianhua on 2020-2-6
     */
    @Transactional
    public void deleteDetail(String id) {
        mapper.deleteDetail(new BanQinWmOutHandoverDetail(id));
    }

    /**
     * 描述：生成交接单，根据条件查询已发运的分配明细记录作为交接明细记录
     * 如果在生成交接单后再取消发运，需要手工在交接单删除，取消发运不会自动删除对应的交接记录
     *
     * @author Jianhua on 2020-2-6
     */
    @Transactional
    public void genHandoverOrder(BanQinWmOutHandoverGenCondition condition) {
        List<BanQinWmOutHandoverDetail> list = mapper.getMeetHandoverData(condition);

        List<BanQinWmOutHandoverDetail> needList = Lists.newArrayList();
        for (BanQinWmOutHandoverDetail entity : list) {
            // 剔除已生成了交接单的记录(根据分配ID号，所有的交接单明细都来自已发运的分配明细)
            /*BanQinWmOutHandoverDetail detail = this.getDetailByAllocId(entity.getAllocId(), entity.getOrgId());
            if (detail != null) {
                continue;
            }*/
            needList.add(entity);
        }
        if (CollectionUtil.isEmpty(needList)) {
            throw new WarehouseException("没有发现符合交接的数据，无法生成");
        }

        // 查询承运商信息
        BanQinEbCustomer carrier = banQinEbCustomerService.find(condition.getCarrierCode(), "CARRIER", condition.getOrgId());

        // 交接单号
        String handoverNo = noService.getDocumentNo(GenNoType.WM_HANDOVER_NO.name());
        // 交接单头
        BanQinWmOutHandoverHeader wmOutHandoverHeader = new BanQinWmOutHandoverHeaderEntity();
        wmOutHandoverHeader.setHandoverNo(handoverNo);
        wmOutHandoverHeader.setShipTimeFm(condition.getShipTimeFm());
        wmOutHandoverHeader.setShipTimeTo(condition.getShipTimeTo());
        if (carrier != null) {
            wmOutHandoverHeader.setCarrierCode(carrier.getEbcuCustomerNo());
            wmOutHandoverHeader.setCarrierName(carrier.getEbcuNameCn());
        }
        wmOutHandoverHeader.setHandoverTime(condition.getHandoverTime());
        wmOutHandoverHeader.setHandoverOp(condition.getHandoverOp());
        wmOutHandoverHeader.setOrgId(condition.getOrgId());
        this.save(wmOutHandoverHeader);

        // 交接单明细
        for (BanQinWmOutHandoverDetail detail : needList) {
            detail.setHandoverNo(handoverNo);
            this.saveDetail(detail);
        }
    }

    /**
     * 描述：打印交接清单
     *
     * @author Jianhua on 2020-2-10
     */
    public List<OutHandoverListLabel> getOutHandoverList(String id) {
        List<OutHandoverListLabel> rsList = Lists.newArrayList();

        BanQinWmOutHandoverHeader wmOutHandoverHeader = this.get(id);
        if (wmOutHandoverHeader != null) {
            int count = 1;
            // 查询该交接单是该交接日期内与该承运商的第几次交接
            List<String> nos = mapper.findNoByCarrierAndHandoverTime(wmOutHandoverHeader.getCarrierCode(), DateUtil.beginOfDate(wmOutHandoverHeader.getHandoverTime()), DateUtil.endOfDate(wmOutHandoverHeader.getHandoverTime()), wmOutHandoverHeader.getOrgId());
            for (int i = 0; i < nos.size(); i++) {
                if (wmOutHandoverHeader.getHandoverNo().equals(nos.get(i))) {
                    count = i + 1;
                    break;
                }
            }

            List<BanQinWmOutHandoverDetail> detailList = mapper.findDetailList(new BanQinWmOutHandoverDetail(wmOutHandoverHeader.getHandoverNo(), wmOutHandoverHeader.getOrgId()));
            if (CollectionUtil.isNotEmpty(detailList)) {
                // 去除订单号与快递单号为空的数据，再按订单号+快递单号去重
                detailList = detailList.stream().filter(o -> StringUtils.isNotBlank(o.getCustomerOrderNo()) && StringUtils.isNotBlank(o.getTrackingNo()))
                        .collect(Collectors.collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCustomerOrderNo() + o.getTrackingNo()))), ArrayList::new));

                long lineNo = 1;
                Date lastPackTime = detailList.stream().filter(o -> o.getShipTime() != null).map(BanQinWmOutHandoverDetail::getShipTime).max(Comparator.comparingLong(Date::getTime)).get();
                long orderNum = detailList.stream().map(BanQinWmOutHandoverDetail::getCustomerOrderNo).distinct().count();
                // 分成三纵队
                int size = detailList.size();
                detailList.sort(Comparator.comparing(BanQinWmOutHandoverDetail::getCustomerOrderNo).thenComparingLong(o -> o.getShipTime().getTime()));
                for (int i = 0; i < size; i += 3) {
                    BanQinWmOutHandoverDetail one = detailList.get(i);

                    OutHandoverListLabel outHandoverListLabel = new OutHandoverListLabel();
                    outHandoverListLabel.setOrgId(one.getOrgId());
                    outHandoverListLabel.setOrgName(one.getOrgName());
                    outHandoverListLabel.setCarrierCode(one.getCarrierCode());
                    outHandoverListLabel.setCarrierName(one.getCarrierName());
                    outHandoverListLabel.setLastPackTime(DateUtils.formatDate(lastPackTime, "yyyy-MM-dd HH:mm:ss"));
                    outHandoverListLabel.setOrderNum(orderNum);
                    outHandoverListLabel.setPackNum(size);
                    outHandoverListLabel.setCount(count);

                    outHandoverListLabel.setOneLineNo(lineNo++);
                    outHandoverListLabel.setOneOrderNo(one.getCustomerOrderNo());
                    outHandoverListLabel.setOneTrackingNo(one.getTrackingNo());
                    if (i + 1 < size) {
                        BanQinWmOutHandoverDetail two = detailList.get(i + 1);
                        outHandoverListLabel.setTwoLineNo(lineNo++);
                        outHandoverListLabel.setTwoOrderNo(two.getCustomerOrderNo());
                        outHandoverListLabel.setTwoTrackingNo(two.getTrackingNo());
                    }
                    if (i + 2 < size) {
                        BanQinWmOutHandoverDetail three = detailList.get(i + 2);
                        outHandoverListLabel.setThreeLineNo(lineNo++);
                        outHandoverListLabel.setThreeOrderNo(three.getCustomerOrderNo());
                        outHandoverListLabel.setThreeTrackingNo(three.getTrackingNo());
                    }
                    rsList.add(outHandoverListLabel);
                }
            }
        }
        return rsList;
    }
}
