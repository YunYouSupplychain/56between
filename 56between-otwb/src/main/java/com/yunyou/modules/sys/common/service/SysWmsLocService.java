package com.yunyou.modules.sys.common.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsLoc;
import com.yunyou.modules.sys.common.entity.SysWmsZone;
import com.yunyou.modules.sys.common.mapper.SysWmsLocMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 库位Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsLocService extends CrudService<SysWmsLocMapper, SysWmsLoc> {
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;
    @Autowired
    private SysWmsZoneService sysWmsZoneService;

    @Override
    public Page<SysWmsLoc> findPage(Page<SysWmsLoc> page, SysWmsLoc entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsLoc> findGrid(Page<SysWmsLoc> page, SysWmsLoc entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysWmsLoc getByCode(String locCode, String dataSet) {
        return mapper.getByCode(locCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsLoc entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsLoc entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeLocation(entity.getLocCode(), entity.getDataSet());
        }
    }

    /**
     * 生成库位
     */
    public ResultMessage locGeneration(String locCode, List<SysWmsLoc> locList, int sumLength, String dataSet) {
        ResultMessage msg = new ResultMessage();
        // 不可用库位编码数量
        int errorNum = 0;
        // 待生成库位
        List<String> listCode = locList.stream().map(SysWmsLoc::getLocCode).collect(Collectors.toList());
        // 不重复的库位
        List<String> existList = Lists.newArrayList();
        List<SysWmsLoc> items = mapper.getExistLoc(listCode, dataSet);
        for (SysWmsLoc item : items) {
            existList.add(item.getLocCode());
        }
        listCode = listCode.stream().filter(loc -> !existList.contains(loc)).collect(Collectors.toList());
        // 可以生成的库位编码列表
        List<SysWmsLoc> results = Lists.newArrayList();
        // 模板库位
        SysWmsLoc entity = this.getByCode(locCode, dataSet);
        // 将生成的库位编码做唯一性校验
        for (int i = locList.size() - sumLength; i < locList.size(); i++) {
            SysWmsLoc locEntity = locList.get(i);
            SysWmsLoc modelEntity = new SysWmsLoc();
            BeanUtils.copyProperties(entity, modelEntity);
            modelEntity.setId(null);
            modelEntity.setLocCode(locEntity.getLocCode());
            modelEntity.setLane(locEntity.getLane());
            modelEntity.setSeq(locEntity.getSeq());
            modelEntity.setFloor(locEntity.getFloor());
            modelEntity.setPaSeq(locEntity.getLocCode());
            modelEntity.setPkSeq(locEntity.getLocCode());
            modelEntity.setDataSet(dataSet);
            if (!listCode.contains(locEntity.getLocCode())) {
                modelEntity.setCreateStatus("库位编码" + locEntity.getLocCode() + "已存在");
                errorNum++;
            }
            results.add(modelEntity);
        }
        msg.addMessage("共生成" + sumLength + "个库位，其中有" + errorNum + "个重复库位");
        msg.setSuccess(false);
        msg.setData(results);
        return msg;
    }

    /**
     * 保存生成的库位信息
     */
    @Transactional
    public ResultMessage confirm(List<SysWmsLoc> entities, String dataSet) {
        List<SysWmsLoc> items = mapper.getExistLoc(entities.stream().map(SysWmsLoc::getLocCode).collect(Collectors.toList()), dataSet);
        List<String> existList = items.stream().map(SysWmsLoc::getLocCode).collect(Collectors.toList());// 已存在的库位

        StringBuilder str = new StringBuilder();
        for (SysWmsLoc entity : entities) {
            if (existList.contains(entity.getLocCode())) {
                if (StringUtils.isBlank(str)) {
                    str.append(entity.getLocCode());
                } else {
                    str.append("、").append(entity.getLocCode());
                }
                continue;
            }
            SysWmsLoc example = new SysWmsLoc();
            BeanUtils.copyProperties(entity, example);
            example.setId(IdGen.uuid());
            example.setIsNewRecord(true);
            this.save(example);
        }
        ResultMessage message = new ResultMessage("成功保存" + (entities.size() - items.size()));
        if (StringUtils.isNotBlank(str)) {
            message.addMessage("\n其中" + str.toString() + "已存在");
        }
        return message;
    }

    @Transactional
    public void importFile(List<SysWmsLoc> importList, String dataSet) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            SysWmsLoc data = importList.get(i);
            if (StringUtils.isBlank(data.getLocCode())) {
                throw new GlobalException("第" + (i + 1) + "行，库位编码不能为空！");
            }
            if (StringUtils.isBlank(data.getZoneCode())) {
                throw new GlobalException("第" + (i + 1) + "行，库区编码不能为空！");
            }
            if (StringUtils.isBlank(data.getLocUseType())) {
                throw new GlobalException("第" + (i + 1) + "行，库位使用类型不能为空！");
            }
            if (StringUtils.isBlank(data.getPaSeq())) {
                throw new GlobalException("第" + (i + 1) + "行，上架顺序不能为空！");
            }
            if (StringUtils.isBlank(data.getPkSeq())) {
                throw new GlobalException("第" + (i + 1) + "行，拣货顺序不能为空！");
            }
            SysWmsZone sysWmsZone = sysWmsZoneService.getByCode(data.getZoneCode(), dataSet);
            if (sysWmsZone == null) {
                throw new GlobalException("第" + (i + 1) + "行，库区不存在！");
            }
            SysWmsLoc sysWmsLoc = getByCode(data.getLocCode(), dataSet);
            if (sysWmsLoc != null) {
                throw new GlobalException("第" + (i + 1) + "行，库位已存在！");
            }
            if (StringUtils.isBlank(data.getIsMixSku())) {
                data.setIsMixSku(Global.Y);
            }
            if (StringUtils.isBlank(data.getIsMixLot())) {
                data.setIsMixLot(Global.Y);
            }
            if (StringUtils.isBlank(data.getIsLoseId())) {
                data.setIsLoseId(Global.Y);
            }
            if (StringUtils.isBlank(data.getStatus())) {
                data.setStatus("00");
            }
            data.setIsEnable(Global.Y);
            data.setZoneName(sysWmsZone.getZoneName());
            data.setDataSet(dataSet);
            this.save(data);
        }
    }
}