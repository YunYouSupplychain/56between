package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.common.utils.collection.SetUtil;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLog;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLogEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存交易日志生成service
 * @author WMJ
 * @version 2020-04-14
 */
@Service
public class BanQinInvActLogCreateService {
    @Autowired
    private BanQinWmActLogService wmActLogService;

    @Transactional
    public void createActLog(Date beginTime, Date endTime, String orgId, boolean isReCalc) {
        // 获取前一天的订单数据按照操作时间排序
        List<BanQinWmActLogEntity> allData = findAllData(beginTime, endTime, orgId, isReCalc);
        Set<String> hasExists = SetUtil.newHashSet();
        for (BanQinWmActLogEntity entity : allData) {
            if (hasExists.contains(entity.getOrderNo())) continue;
            switch (entity.getOrderType()) {
                case "ASN":
                    createByAsn(entity, hasExists);
                    break;
                case "SO":
                    createBySo(entity, hasExists);
                    break;
                case "AD":
                    createByAd(entity, hasExists);
                    break;
                case "TF":
                    createByTf(entity, hasExists);
                    break;
            }
        }
    }

    /**
     * 查找前一天有修改过的订单数据并排序
     * @param beginTime
     * @param endTime
     * @return
     */
    private List<BanQinWmActLogEntity> findAllData(Date beginTime, Date endTime, String orgId, boolean isReCalc) {
        List<BanQinWmActLogEntity> result = ListUtil.newArrayList();

        BanQinWmActLogEntity condition = new BanQinWmActLogEntity();
        if (isReCalc) {
            condition.setOrderTimeFm(beginTime);
            condition.setOrderTimeTo(endTime);
        } else {
            condition.setBeginTime(beginTime);
            condition.setEndTime(endTime);
        }
        condition.setOrgId(orgId);
        // 入库数据
        result.addAll(wmActLogService.findAsnData(condition));
        // 出库数据
        result.addAll(checkSoData(wmActLogService.findSoData(condition)));
        // 调整数据
        result.addAll(wmActLogService.findAdData(condition));
        // 转移数据
        result.addAll(splitTfData(wmActLogService.findTfData(condition)));
        // 按照操作时间、单号、行号排序
        result.sort(Comparator.comparing(BanQinWmActLogEntity::getOpTime).thenComparing(BanQinWmActLogEntity::getOrderNo).thenComparing(BanQinWmActLogEntity::getStatus).thenComparing(BanQinWmActLogEntity::getLineNo));

        return result;
    }

    @Transactional
    public void createByAsn(BanQinWmActLogEntity entity, Set<String> hasExists) {
        // 先判断该批次所属订单是否存在, 存在,删除订单下批次所对应的opTime之后的记录并重新计算，不存在继续
        boolean isCancel = createByCancel(entity);
        if (isCancel) {
            BanQinWmActLogEntity asnCon = new BanQinWmActLogEntity();
            asnCon.setOrderNo(entity.getOrderNo());
            asnCon.setOrgId(entity.getOrgId());
            List<BanQinWmActLogEntity> asnData = wmActLogService.findAsnData(asnCon);
            List<BanQinWmActLogEntity> collect = asnData.stream().filter(s -> WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(s.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                collect.sort(Comparator.comparing(BanQinWmActLogEntity::getOpTime).thenComparing(BanQinWmActLogEntity::getLineNo));
                for (BanQinWmActLogEntity logEntity : collect) {
                    createByFinal(logEntity);
                }
            }
            hasExists.add(entity.getOrderNo());
        } else {
            // 判断是否是完全收货
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(entity.getStatus())) {
                createByFinal(entity);
            }
        }
    }

    @Transactional
    public void createBySo(BanQinWmActLogEntity entity, Set<String> hasExists) {
        boolean isCancel = createByCancel(entity);
        if (isCancel) {
            BanQinWmActLogEntity soCon = new BanQinWmActLogEntity();
            soCon.setOrderNo(entity.getOrderNo());
            soCon.setOrgId(entity.getOrgId());
            List<BanQinWmActLogEntity> soData = wmActLogService.findSoData(soCon);
            List<BanQinWmActLogEntity> collect = soData.stream().filter(s -> WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(s.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                collect.sort(Comparator.comparing(BanQinWmActLogEntity::getOpTime).thenComparing(BanQinWmActLogEntity::getLineNo));
                for (BanQinWmActLogEntity logEntity : collect) {
                    createByFinal(logEntity);
                }
            }
            hasExists.add(entity.getOrderNo());
        } else {
            // 判断是否是完全发货
            if (WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(entity.getStatus())) {
                createByFinal(entity);
            }
        }
    }

    @Transactional
    public void createByAd(BanQinWmActLogEntity entity, Set<String> hasExists) {
        // 先判断该批次所属订单是否存在, 存在,删除订单下批次所对应的opTime之后的记录并重新计算，不存在继续
        boolean isCancel = createByCancel(entity);
        if (isCancel) {
            BanQinWmActLogEntity adCon = new BanQinWmActLogEntity();
            adCon.setOrderNo(entity.getOrderNo());
            adCon.setOrgId(entity.getOrgId());
            List<BanQinWmActLogEntity> adData = wmActLogService.findAdData(adCon);
            List<BanQinWmActLogEntity> collect = adData.stream().filter(s -> WmsCodeMaster.AD_FULL_ADJUSTMENT.getCode().equals(s.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                collect.sort(Comparator.comparing(BanQinWmActLogEntity::getOpTime).thenComparing(BanQinWmActLogEntity::getLineNo));
                for (BanQinWmActLogEntity logEntity : collect) {
                    createByFinal(logEntity);
                }
            }
            hasExists.add(entity.getOrderNo());
        } else {
            // 判断是否是完全调整
            if (WmsCodeMaster.AD_FULL_ADJUSTMENT.getCode().equals(entity.getStatus())) {
                createByFinal(entity);
            }
        }
    }

    @Transactional
    public void createByTf(BanQinWmActLogEntity entity, Set<String> hasExists) {
        // 先判断该批次所属订单是否存在, 存在,删除订单下批次所对应的opTime之后的记录并重新计算，不存在继续
        boolean isCancel = createByCancel(entity);
        if (isCancel) {
            BanQinWmActLogEntity tfCon = new BanQinWmActLogEntity();
            tfCon.setOrderNo(entity.getOrderNo());
            tfCon.setOrgId(entity.getOrgId());
            List<BanQinWmActLogEntity> tfData = wmActLogService.findTfData(tfCon);
            List<BanQinWmActLogEntity> finalTfDate = splitTfData(tfData);
            List<BanQinWmActLogEntity> collect = finalTfDate.stream().filter(s -> WmsCodeMaster.TF_FULL_TRANSFER.getCode().equals(s.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                collect.sort(Comparator.comparing(BanQinWmActLogEntity::getOpTime).thenComparing(BanQinWmActLogEntity::getLineNo));
                for (BanQinWmActLogEntity logEntity : collect) {
                    createByFinal(logEntity);
                }
            }
            hasExists.add(entity.getOrderNo());
        } else {
            // 判断是否是完全转移
            if (WmsCodeMaster.TF_FULL_TRANSFER.getCode().equals(entity.getStatus())) {
                createByFinal(entity);
            }
        }
    }

    @Transactional
    public void createByFinal(BanQinWmActLogEntity entity) {
        // 判断批次是否存在
        List<BanQinWmActLog> lotList = wmActLogService.findByLotNum(entity.getLotNum(), entity.getOrgId());
        if (CollectionUtil.isEmpty(lotList)) {
            double qtyBegin = 0d;
            // 插入
            entity.setTranType(getTranType(entity.getQtyEaOp()));
            entity.setQtyEaBefore(qtyBegin);
            entity.setQtyEaAfter(qtyBegin + entity.getQtyEaOp());
            wmActLogService.save(entity);
        } else {
            lotList.sort(Comparator.comparing(BanQinWmActLog::getOpTime, Comparator.reverseOrder()).thenComparing(BanQinWmActLog::getOrderNo, Comparator.reverseOrder()).thenComparing(BanQinWmActLog::getLineNo, Comparator.reverseOrder()));
            // 判断是否是同一条记录
            long count = lotList.stream().filter(s -> s.getOrderNo().equals(entity.getOrderNo()) && s.getLineNo().equals(entity.getLineNo())).count();
            if (count > 0) return;
            // 判断当前批次的操作时间是否大于历史批次记录中最大的操作时间, 是,直接插入/否,删除重新计算
            BanQinWmActLog lastLog = lotList.get(0);
            if (entity.getOpTime().after(lastLog.getOpTime())) {
                entity.setTranType(getTranType(entity.getQtyEaOp()));
                entity.setQtyEaBefore(lastLog.getQtyEaAfter());
                entity.setQtyEaAfter(entity.getQtyEaBefore() + entity.getQtyEaOp());
                wmActLogService.save(entity);
                return;
            }
            // 找到当前这条应该插入的位置
            List<BanQinWmActLog> deleteList = ListUtil.newArrayList();
            for (BanQinWmActLog act : lotList) {
                if (act.getOpTime().after(entity.getOpTime())) {
                    deleteList.add(act);
                } else if (act.getOpTime().equals(entity.getOpTime())) {
                    if (act.getOrderNo().compareTo(entity.getOrderNo()) > 0) {
                        deleteList.add(act);
                    } else if (act.getOrderNo().compareTo(entity.getOrderNo()) == 0) {
                        if (act.getLineNo().compareTo(entity.getLineNo()) > 0) {
                            deleteList.add(act);
                        }
                    }
                }
            }
            if (CollectionUtil.isEmpty(deleteList)) {
                entity.setTranType(getTranType(entity.getQtyEaOp()));
                entity.setQtyEaBefore(lastLog.getQtyEaAfter());
                entity.setQtyEaAfter(entity.getQtyEaBefore() + entity.getQtyEaOp());
                wmActLogService.save(entity);
                return;
            }

            wmActLogService.deleteAll(deleteList);
            // 重新获取Id
            deleteList.forEach(k -> k.setId(null));
            // 按照操作时间升序
            deleteList.sort(Comparator.comparing(BanQinWmActLog::getOpTime).thenComparing(BanQinWmActLog::getOrderNo).thenComparing(BanQinWmActLog::getLineNo));
            // 获取第一条记录的期初
            double qtyBegin = deleteList.get(0).getQtyEaBefore();
            // 将当前这条插入到第一条
            entity.setTranType(getTranType(entity.getQtyEaOp()));
            deleteList.add(0, entity);
            // 重新计算期初期末
            for (BanQinWmActLog log : deleteList) {
                log.setQtyEaBefore(qtyBegin);
                log.setQtyEaAfter(log.getQtyEaBefore() + log.getQtyEaOp());
                qtyBegin = log.getQtyEaAfter();
            }
            wmActLogService.saveAll(deleteList);
        }
    }

    @Transactional
    public boolean createByCancel(BanQinWmActLogEntity entity) {
        boolean flag = false;
        List<BanQinWmActLog> orderList = wmActLogService.findByOrderNo(entity.getOrderNo(), entity.getOrgId());
        if (CollectionUtil.isNotEmpty(orderList)) {
            flag = true;
            orderList.sort(Comparator.comparing(BanQinWmActLog::getOpTime).thenComparing(BanQinWmActLog::getLineNo));
            for (BanQinWmActLog log : orderList) {
                List<BanQinWmActLog> currLotList = wmActLogService.findByLotNum(log.getLotNum(), log.getOrgId());
                // 找到当前这条应该插入的位置
                // 按照操作时间升序
                currLotList.sort(Comparator.comparing(BanQinWmActLog::getOpTime).thenComparing(BanQinWmActLog::getOrderNo).thenComparing(BanQinWmActLog::getLineNo));
                List<BanQinWmActLog> deleteList = ListUtil.newArrayList();
                for (BanQinWmActLog act : currLotList) {
                    if (act.getOpTime().after(log.getOpTime())) {
                        deleteList.add(act);
                    } else if (act.getOpTime().equals(log.getOpTime())) {
                        if (act.getOrderNo().compareTo(log.getOrderNo()) > 0) {
                            deleteList.add(act);
                        } else if (act.getOrderNo().compareTo(log.getOrderNo()) == 0) {
                            if (act.getLineNo().compareTo(log.getLineNo()) > 0) {
                                deleteList.add(act);
                            }
                        }
                    }
                }
                wmActLogService.deleteAll(deleteList);
                wmActLogService.delete(log);
                // 获取第一条记录的期初
                double qtyBegin = log.getQtyEaBefore();
                // 重新计算期初期末
                for (BanQinWmActLog actLog : deleteList) {
                    actLog.setQtyEaBefore(qtyBegin);
                    actLog.setQtyEaAfter(actLog.getQtyEaBefore() + actLog.getQtyEaOp());
                    actLog.setId(null);
                    qtyBegin = actLog.getQtyEaAfter();
                }
                wmActLogService.saveAll(deleteList);
            }
        }

        return flag;
    }

    private List<BanQinWmActLogEntity> checkSoData(List<BanQinWmActLogEntity> list) {
        List<BanQinWmActLogEntity> result = ListUtil.newArrayList();
        Map<String, List<BanQinWmActLogEntity>> collect = list.stream().collect(Collectors.groupingBy(BanQinWmActLogEntity::getOrderNo));
        collect.values().forEach(k -> {
            List<BanQinWmActLogEntity> logList = k.stream().filter(s -> "90".equals(s.getStatus())).collect(Collectors.toList());
            if (k.size() > logList.size()) {
                k.removeAll(logList);
                result.addAll(k);
            } else if (k.size() == logList.size()) {
                result.add(k.get(0));
            }
        });

        return result;
    }

    private List<BanQinWmActLogEntity> splitTfData(List<BanQinWmActLogEntity> list) {
        List<BanQinWmActLogEntity> result = ListUtil.newArrayList();
        for (BanQinWmActLogEntity entity : list) {
            result.add(entity);
            BanQinWmActLogEntity newEntity = new BanQinWmActLogEntity();
            BeanUtils.copyProperties(entity, newEntity);
            newEntity.setIsNewRecord(false);
            newEntity.setOwnerCode(entity.getToOwnerCode());
            newEntity.setSkuCode(entity.getToSkuCode());
            newEntity.setLotNum(entity.getToLotNum());
            newEntity.setQtyEaOp(entity.getToQtyEaOp());
            result.add(newEntity);
        }

        return result;
    }

    private String getTranType(double qty) {
        return qty > 0 ? "1" : "0";
    }

}
