package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.crossDock.entity.*;
import com.yunyou.modules.wms.crossDock.mapper.BanQinWmCrossDockMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnReceiveByCdQuery;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 越库
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinWmCrossDockListService {
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    private BanQinWmCrossDockMapper wmCrossDockMapper;

    /**
     * 越库入库单、出库单列表查询
     */
    public ResultMessage getCrossDockDetailByQuery(BanQinWmCrossDockEntity item) {
        ResultMessage msg = new ResultMessage();
        if (null == item) {
            msg.setSuccess(false);
            return msg;
        }
        BanQinWmCrossDockDetailEntity entity = new BanQinWmCrossDockDetailEntity();

        BanQinWmAsnReceiveByCdQuery asnCondition = new BanQinWmAsnReceiveByCdQuery();
        BeanUtils.copyProperties(item, asnCondition);
        asnCondition.setOwnerCode(item.getOwnerCode());
        asnCondition.setSkuCode(item.getSkuCode());
        asnCondition.setStatus(WmsCodeMaster.ASN_NEW.getCode());// 创建状态
        asnCondition.setIsCd(WmsConstants.YES);// 未越库
        asnCondition.setOrgId(item.getOrgId());
        entity.setWmAsnDetailReceiveEntity(wmAsnDetailReceiveService.getEntityByCdAndSku(asnCondition));

        BanQinWmSoDetailByCdQuery soCondition = new BanQinWmSoDetailByCdQuery();
        BeanUtils.copyProperties(item, soCondition);
        soCondition.setOwnerCode(item.getOwnerCode());
        soCondition.setSkuCode(item.getSkuCode());
        soCondition.setStatus(WmsCodeMaster.SO_NEW.getCode());// 创建状态
        soCondition.setIsCd(WmsConstants.YES);// 未越库
        soCondition.setOrgId(item.getOrgId());
        entity.setWmSoDetailEntity(wmSoDetailService.getEntityByCdAndSku(soCondition));

        msg.setSuccess(true);
        msg.setData(entity);
        return msg;
    }

    /**
     * 校验是否有asn条件
     */
    protected Boolean checkNullGetCdAsnDetailCondition(BanQinGetCrossDockAsnDetaiCountQueryEntity asnCon) {
        if (StringUtils.isEmpty(asnCon.getAsnNo()) && (null == asnCon.getAsnTypes() || asnCon.getAsnTypes().length == 0) && asnCon.getFmEtaFm() == null && asnCon.getFmEtaTo() == null && asnCon.getToEtaFm() == null
                && asnCon.getToEtaTo() == null) {
            return true;
        }
        return false;
    }

    /**
     * 校验是否有so条件
     */
    protected Boolean checkNullGetCdSoDetailCondition(BanQinGetCrossDockSoDetailCountQueryEntity soCon) {
        if (StringUtils.isEmpty(soCon.getSoNo()) && (null == soCon.getSoTypes() || soCon.getSoTypes().length == 0) && soCon.getFmEtdFm() == null && soCon.getFmEtdTo() == null && soCon.getToEtdFm() == null
                && soCon.getToEtdTo() == null && soCon.getSoLineNum() == null && soCon.getQtySoEa() == null) {
            return true;
        }
        return false;
    }

    /**
     * 高级查询
     */
    public Page<BanQinWmCrossDockEntity> getEntityByQueryInfo(Page page, BanQinGetCrossDockQueryEntity condition) {
        // 分组处理，货主+商品
        Map<String, BanQinWmCrossDockEntity> cdMaps = new HashMap<>();
        // 结果集
        List<BanQinWmCrossDockEntity> entityList = new ArrayList<>();
        // 查询入库单
        BanQinGetCrossDockAsnDetaiCountQueryEntity asnCon = new BanQinGetCrossDockAsnDetaiCountQueryEntity();
        BeanUtils.copyProperties(condition, asnCon);
        List<BanQinGetCrossDockAsnDetaiCountQueryEntity> asnItems = wmCrossDockMapper.getCrossDockAsnDetailCountQuery(asnCon);

        // 查询出库单
        BanQinGetCrossDockSoDetailCountQueryEntity soCon = new BanQinGetCrossDockSoDetailCountQueryEntity();
        BeanUtils.copyProperties(condition, soCon);
        List<BanQinGetCrossDockSoDetailCountQueryEntity> soItems = wmCrossDockMapper.getCrossDockSoDetailCountQuery(soCon);
        // 如果都有条件，则取交集
        if (!checkNullGetCdAsnDetailCondition(asnCon) && !checkNullGetCdSoDetailCondition(soCon)) {
            for (BanQinGetCrossDockAsnDetaiCountQueryEntity item : asnItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 不包含，写入key
                if (!cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
                    BeanUtils.copyProperties(item, entity);
                    cdMaps.put(key, entity);
                }
            }
            for (BanQinGetCrossDockSoDetailCountQueryEntity item : soItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 包含，写入key
                if (cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
                    BeanUtils.copyProperties(item, entity);
                    entityList.add(entity);
                }
            }
        } else if (!checkNullGetCdAsnDetailCondition(asnCon) && checkNullGetCdSoDetailCondition(soCon)) {
            // 如果有asn条件，无so条件，取ASN结果集
            for (BanQinGetCrossDockAsnDetaiCountQueryEntity item : asnItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 不包含，写入key
                if (!cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
                    BeanUtils.copyProperties(item, entity);
                    entityList.add(entity);
                    cdMaps.put(key, entity);
                }
            }
            for (BanQinGetCrossDockSoDetailCountQueryEntity item : soItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 包含，写入SO值
                if (cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = cdMaps.get(key);
                    BeanUtils.copyProperties(entity, item);
                }
            }
        } else if (checkNullGetCdAsnDetailCondition(asnCon) && !checkNullGetCdSoDetailCondition(soCon)) {
            // 如果无asn条件，有so条件，取SO结果集
            for (BanQinGetCrossDockSoDetailCountQueryEntity item : soItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 不包含，写入key
                if (!cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
                    BeanUtils.copyProperties(item, entity);
                    entityList.add(entity);
                    cdMaps.put(key, entity);
                }
            }
            for (BanQinGetCrossDockAsnDetaiCountQueryEntity item : asnItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 包含，写入SO值
                if (cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = cdMaps.get(key);
                    BeanUtils.copyProperties(item, entity);
                }
            }
        } else if (checkNullGetCdAsnDetailCondition(asnCon) && checkNullGetCdSoDetailCondition(soCon)) {
            // 都没有条件，取全集
            for (BanQinGetCrossDockAsnDetaiCountQueryEntity item : asnItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 不包含，写入key
                if (!cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
                    BeanUtils.copyProperties(item, entity);
                    entity.setSoNum(0L);// 出库订单数
                    entity.setQtySoEa(0D);// 出库订货数
                    entityList.add(entity);
                    cdMaps.put(key, entity);
                }
            }
            for (BanQinGetCrossDockSoDetailCountQueryEntity item : soItems) {
                String ownerCode = item.getOwnerCode();// 货主
                String skuCode = item.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 包含，写入SO值
                if (cdMaps.containsKey(key)) {
                    BanQinWmCrossDockEntity entity = cdMaps.get(key);
                    BeanUtils.copyProperties(item, entity);
                } else {
                    BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
                    BeanUtils.copyProperties(item, entity);
                    entity.setAsnNum(0L);// 入库订单数
                    entity.setQtyPlanEa(0D);// 入库订货数
                    entityList.add(entity);
                }
            }
        }

        page.setCount(entityList.size());
        page.setList(entityList);
        return page;
    }

    /**
     *
     * 明细操作完成获取头信息
     */
    public BanQinWmCrossDockEntity getEntity(BanQinWmCrossDockEntity wmCrossDockEntity) {
        BanQinWmCrossDockEntity entity = new BanQinWmCrossDockEntity();
        BanQinGetCrossDockQueryEntity con = new BanQinGetCrossDockQueryEntity();
        String[] skuCode = new String[1];
        skuCode[0] = wmCrossDockEntity.getSkuCode();
        con.setSkuCodes(skuCode);

        String[] ownerCode = new String[1];
        ownerCode[0] = wmCrossDockEntity.getOwnerCode();
        con.setOwnerCodes(ownerCode);
        List<BanQinGetCrossDockQueryEntity> item = wmCrossDockMapper.getCrossDockQuery(con);
        if (CollectionUtil.isNotEmpty(item)) {
            BeanUtils.copyProperties(item.get(0), entity);
        }
        return entity;
    }

    /**
     * 根据asnno 和 lineno查询收货明细
     */
    public BanQinWmAsnDetailReceive getWmAsnReceiveEntity(String asnNo, String lineNo, String orgId) {
        BanQinWmAsnDetailReceive item = null;
        BanQinWmAsnDetailReceive condition = new BanQinWmAsnDetailReceive();
        condition.setAsnNo(asnNo);
        condition.setLineNo(lineNo);
        condition.setOrgId(orgId);
        List<BanQinWmAsnDetailReceive> items = wmAsnDetailReceiveService.findList(condition);
        if (items != null && items.size() > 0) {
            item = items.get(0);
        }
        return item;
    }

    /**
     * 越库任务高级查询
     */
    public Page<BanQinWmAsnDetailReceiveQueryEntity> getTaskItemByQueryInfo(Page page, BanQinWmTaskCdByDirectQueryEntity condition) {
        // 执行查询
        List<BanQinWmAsnDetailReceiveQueryEntity> items = new ArrayList<>();
        // 1、分拨越库，创建
        items = getTaskCdNewByIndirect(condition, items);
        // 2、分拨越库，完全收货
        BanQinWmTaskCdByIndirectQueryEntity fullIndirectCon = new BanQinWmTaskCdByIndirectQueryEntity();
        BeanUtils.copyProperties(condition, fullIndirectCon);
        fullIndirectCon.setCdType(WmsCodeMaster.CD_TYPE_INDIRECT.getCode());
        fullIndirectCon.setIndirectStatus(WmsCodeMaster.ASN_FULL_RECEIVING.getCode());// 完全越库
        List<BanQinWmTaskCdByIndirectQueryEntity> fullIndirectItems = wmCrossDockMapper.wmTaskCdByIndirectQuery(fullIndirectCon);
        items.addAll((List<BanQinWmAsnDetailReceiveQueryEntity>) convertCollection(fullIndirectItems, BanQinWmAsnDetailReceiveQueryEntity.class));
        // 3、直接越库，创建/完全收货
        condition.setCdType(WmsCodeMaster.CD_TYPE_DIRECT.getCode());
        List<BanQinWmTaskCdByDirectQueryEntity> directItems = wmCrossDockMapper.wmTaskCdByDirectQuery(condition);
        items.addAll((List<BanQinWmAsnDetailReceiveQueryEntity>) convertCollection(directItems, BanQinWmAsnDetailReceiveQueryEntity.class));

        // 显示记录行,分页数据
        page.setCount(items.size());
        page.setList(items);
        return page;
    }

    public Collection convertCollection(Collection origList, Class clazz) {
        if (origList == null)
            return null;
        Collection destList = (Collection)instance(origList.getClass());
        Object dest;
        for (Iterator iter = origList.iterator(); iter.hasNext(); destList.add(dest)) {
            Object orig = iter.next();
            dest = instance(clazz);
            BeanUtils.copyProperties(orig, dest);
        }

        return destList;
    }

    public Object instance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取分拨越库，创建记录
     */
    protected List<BanQinWmAsnDetailReceiveQueryEntity> getTaskCdNewByIndirect(BanQinWmTaskCdByDirectQueryEntity condition, List<BanQinWmAsnDetailReceiveQueryEntity> items) {
        if (StringUtils.isNotEmpty(condition.getStatus()) && condition.getStatuss().length > 0 && condition.getStatuss()[0].equals(WmsCodeMaster.ASN_FULL_RECEIVING.getCode())) {
            return items;
        }
        // 收货明细
        BanQinWmTaskCdByIndirectQueryEntity fullIndirectCon = new BanQinWmTaskCdByIndirectQueryEntity();
        BeanUtils.copyProperties(condition, fullIndirectCon);
        // 清空出库条件
        fullIndirectCon.setSoNo(null);
        fullIndirectCon.setFmEtdFm(null);
        fullIndirectCon.setFmEtdTo(null);
        fullIndirectCon.setToEtdFm(null);
        fullIndirectCon.setToEtdTo(null);
        // 分组处理，货主+商品
        Map<String, BanQinWmAsnDetailReceiveQueryEntity> rcvMaps = new HashMap<>();
        // 入库收货明细
        fullIndirectCon.setCdType(WmsCodeMaster.CD_TYPE_INDIRECT.getCode());
        fullIndirectCon.setIndirectStatus(WmsCodeMaster.ASN_NEW.getCode());
        List<BanQinWmTaskCdByIndirectQueryEntity> newIndirectItems = wmCrossDockMapper.wmTaskCdByIndirectQuery(fullIndirectCon);

        // 发运订单
        BanQinWmTaskCdSoDetailByIndirectQueryEntity soDetailCon = new BanQinWmTaskCdSoDetailByIndirectQueryEntity();
        BeanUtils.copyProperties(condition, soDetailCon);
        List<BanQinWmTaskCdSoDetailByIndirectQueryEntity> soDetailItems = wmCrossDockMapper.wmTaskCdSoDetailByIndirectQuery(soDetailCon);

        // 如果入库单和出库单都有条件，那么分拨越库任务结果由两者取重复
        if ((StringUtils.isNotEmpty(condition.getAsnNo()) || condition.getFmEtaFm() != null || condition.getFmEtaTo() != null || condition.getToEtaFm() != null || condition.getToEtaTo() != null)
                && (StringUtils.isNotEmpty(condition.getSoNo()) || condition.getFmEtdFm() != null || condition.getToEtdFm() != null || condition.getToEtdFm() != null || condition.getToEtdTo() != null)) {
            if (newIndirectItems.size() == 0 || soDetailItems.size() == 0) {
                return items;
            }
            // 收货明细 循环写入,获取入库单所有货主+商品
            for (BanQinWmTaskCdByIndirectQueryEntity newIndirectItem : newIndirectItems) {
                String ownerCode = newIndirectItem.getOwnerCode();// 货主
                String skuCode = newIndirectItem.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                // 不包含，写入key
                if (!rcvMaps.containsKey(key)) {
                    rcvMaps.put(key, null);
                }
            }
            // 出库商品明细行，排除非重复值
            for (BanQinWmTaskCdSoDetailByIndirectQueryEntity soDetailItem : soDetailItems) {
                // Map的Key值
                String key = soDetailItem.getOwnerCode() + soDetailItem.getSkuCode();// 货主+商品
                // 包含写入items
                if (rcvMaps.containsKey(key)) {
                    if (rcvMaps.get(key) == null) {
                        BanQinWmAsnDetailReceiveQueryEntity item = new BanQinWmAsnDetailReceiveQueryEntity();
                        item.setOwnerCode(soDetailItem.getOwnerCode());// 货主
                        item.setOwnerName(soDetailItem.getOwnerName());
                        item.setSkuCode(soDetailItem.getSkuCode());// 商品
                        item.setSkuName(soDetailItem.getSkuName());
                        item.setStatus(WmsCodeMaster.ASN_NEW.getCode());// 状态
                        item.setCdType(soDetailItem.getCdType());// 越库类型
                        item.setOrgId(soDetailItem.getOrgId());
                        rcvMaps.remove(key);
                        rcvMaps.put(key, item);
                        items.add(item);
                    }
                }
            }
        }
        // 如果入库单有条件，那么分拨越库任务结果由收货明细获取
        else if (StringUtils.isNotEmpty(condition.getAsnNo()) || condition.getFmEtaFm() != null || condition.getFmEtaTo() != null || condition.getToEtaFm() != null || condition.getToEtaTo() != null) {
            if (newIndirectItems.size() == 0) {
                return items;
            }
            // 收货明细 循环写入
            for (BanQinWmTaskCdByIndirectQueryEntity newIndirectItem : newIndirectItems) {
                String ownerCode = newIndirectItem.getOwnerCode();// 货主
                String skuCode = newIndirectItem.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                if (!rcvMaps.containsKey(key)) {
                    BanQinWmAsnDetailReceiveQueryEntity item = new BanQinWmAsnDetailReceiveQueryEntity();
                    item.setOwnerCode(ownerCode);// 货主
                    item.setOwnerName(newIndirectItem.getOwnerName());
                    item.setSkuCode(skuCode);// 商品
                    item.setSkuName(newIndirectItem.getSkuName());
                    item.setStatus(newIndirectItem.getStatus());// 状态
                    item.setCdType(newIndirectItem.getCdType());// 越库类型
                    item.setOrgId(newIndirectItem.getOrgId());
                    rcvMaps.put(key, item);
                    // 写入结果集
                    items.add(item);
                }
            }
        }
        // 如果出库单有条件，那么分拨越库任务结果由出库单商品明细获取
        else if (StringUtils.isNotEmpty(condition.getSoNo()) || condition.getFmEtdFm() != null || condition.getToEtdFm() != null || condition.getToEtdFm() != null || condition.getToEtdTo() != null) {
            if (soDetailItems.size() == 0) {
                return items;
            }
            // 出库商品明细行
            for (BanQinWmTaskCdSoDetailByIndirectQueryEntity soDetailItem : soDetailItems) {
                // Map的Key值
                String key = soDetailItem.getOwnerCode() + soDetailItem.getSkuCode();// 货主+商品
                if (!rcvMaps.containsKey(key)) {
                    BanQinWmAsnDetailReceiveQueryEntity item = new BanQinWmAsnDetailReceiveQueryEntity();
                    item.setOwnerCode(soDetailItem.getOwnerCode());// 货主
                    item.setOwnerName(soDetailItem.getOwnerName());
                    item.setSkuCode(soDetailItem.getSkuCode());// 商品
                    item.setSkuName(soDetailItem.getSkuName());
                    item.setStatus(WmsCodeMaster.ASN_NEW.getCode());// 状态
                    item.setCdType(soDetailItem.getCdType());// 越库类型
                    item.setOrgId(soDetailItem.getOrgId());
                    rcvMaps.put(key, item);
                    // 写入结果集
                    items.add(item);
                }
            }
        } else {
            // 收货明细 循环写入
            for (BanQinWmTaskCdByIndirectQueryEntity newIndirectItem : newIndirectItems) {
                String ownerCode = newIndirectItem.getOwnerCode();// 货主
                String skuCode = newIndirectItem.getSkuCode();// 商品
                // Map的Key值
                String key = ownerCode + skuCode;// 货主+商品
                if (!rcvMaps.containsKey(key)) {
                    BanQinWmAsnDetailReceiveQueryEntity item = new BanQinWmAsnDetailReceiveQueryEntity();
                    item.setOwnerCode(ownerCode);// 货主
                    item.setOwnerName(newIndirectItem.getOwnerName());
                    item.setSkuCode(skuCode);// 商品
                    item.setSkuName(newIndirectItem.getSkuName());
                    item.setStatus(newIndirectItem.getStatus());// 状态
                    item.setCdType(newIndirectItem.getCdType());// 越库类型
                    item.setOrgId(newIndirectItem.getOrgId());
                    rcvMaps.put(key, item);
                    // 写入结果集
                    items.add(item);
                }
            }
            // 出库商品明细行
            for (BanQinWmTaskCdSoDetailByIndirectQueryEntity soDetailItem : soDetailItems) {
                // Map的Key值
                String key = soDetailItem.getOwnerCode() + soDetailItem.getSkuCode();// 货主+商品
                if (!rcvMaps.containsKey(key)) {
                    BanQinWmAsnDetailReceiveQueryEntity item = new BanQinWmAsnDetailReceiveQueryEntity();
                    item.setOwnerCode(soDetailItem.getOwnerCode());// 货主
                    item.setOwnerName(soDetailItem.getOwnerName());
                    item.setSkuCode(soDetailItem.getSkuCode());// 商品
                    item.setSkuName(soDetailItem.getSkuName());
                    item.setStatus(WmsCodeMaster.ASN_NEW.getCode());// 状态
                    item.setCdType(soDetailItem.getCdType());// 越库类型
                    item.setOrgId(soDetailItem.getOrgId());
                    rcvMaps.put(key, item);
                    // 写入结果集
                    items.add(item);
                }
            }
        }
        return items;
    }
}
