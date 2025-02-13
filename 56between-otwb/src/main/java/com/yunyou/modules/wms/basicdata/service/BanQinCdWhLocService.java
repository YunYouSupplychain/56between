package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhZone;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhLocMapper;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 库位Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhLocService extends CrudService<BanQinCdWhLocMapper, BanQinCdWhLoc> {
    @Autowired
    private BanQinWmsCommonService wmCommon;
    @Autowired
    private BanQinCdWhZoneService cdWhZoneService;

    public Page<BanQinCdWhLoc> findPage(Page<BanQinCdWhLoc> page, BanQinCdWhLoc banQinCdWhLoc) {
        dataRuleFilter(banQinCdWhLoc);
        banQinCdWhLoc.setPage(page);
        page.setList(mapper.findPage(banQinCdWhLoc));
        return page;
    }

    public BanQinCdWhLoc findFirst(BanQinCdWhLoc banQinCdWhLoc) {
        List<BanQinCdWhLoc> list = this.findList(banQinCdWhLoc);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public BanQinCdWhLoc findByLocCode(String locCode, String orgId) {
        BanQinCdWhLoc cdWhLoc = new BanQinCdWhLoc();
        cdWhLoc.setLocCode(locCode);
        cdWhLoc.setOrgId(orgId);
        return findFirst(cdWhLoc);
    }

    /**
     * 库位冻结
     */
    @Transactional
    public ResultMessage holdLoc(String locCode, String status, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinCdWhLoc cdWhLocEntity = findByLocCode(locCode, orgId);
        if (null == cdWhLocEntity) {
            msg.setSuccess(false);
            msg.addMessage("库位不存在");
            return msg;
        } else {
            cdWhLocEntity.setStatus(status);
            BanQinCdWhLoc model = new BanQinCdWhLoc();
            BeanUtils.copyProperties(cdWhLocEntity, model);
            this.save(model);
            return msg;
        }
    }

    public List<BanQinCdWhLoc> getExistLoc(List<String> locCodeList, String orgId) {
        return mapper.getExistLoc(locCodeList, orgId);
    }

    /**
     * 生成库位
     */
    public ResultMessage locGeneration(String locCode, List<BanQinCdWhLoc> locList, int sumLength) {
        ResultMessage msg = new ResultMessage();
        // 不可用库位编码数量
        int errorNum = 0;
        // 待生成库位
        List<String> listCode = Lists.newArrayList();
        String orgId = CollectionUtil.isNotEmpty(locList) ? locList.get(0).getOrgId() : "";
        for (BanQinCdWhLoc loc : locList) {
            listCode.add(loc.getLocCode());
        }
        // 不重复的库位
        listCode = (List<String>) validateLocation(listCode, orgId).getData();
        // 可以生成的库位编码列表
        List<BanQinCdWhLoc> results = Lists.newArrayList();
        // 模板库位
        BanQinCdWhLoc entity = findByLocCode(locCode, orgId);
        // 将生成的库位编码做唯一性校验
        for (int i = locList.size() - sumLength; i < locList.size(); i++) {
            BanQinCdWhLoc locEntity = locList.get(i);
            BanQinCdWhLoc modelEntity = new BanQinCdWhLoc();
            BeanUtils.copyProperties(entity, modelEntity);
            modelEntity.setId(null);
            modelEntity.setLocCode(locEntity.getLocCode());
            modelEntity.setLane(locEntity.getLane());
            modelEntity.setSeq(locEntity.getSeq());
            modelEntity.setFloor(locEntity.getFloor());
            modelEntity.setPaSeq(locEntity.getLocCode());
            modelEntity.setPkSeq(locEntity.getLocCode());
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
     * 判断是否存在数据中
     *
     * @return date 不存在的可保存数据， msg：提示重复信息
     */
    private ResultMessage validateLocation(List<String> locationList, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的单号
        List<String> returnList = Lists.newArrayList();
        List<BanQinCdWhLoc> items = getExistLoc(locationList, orgId);
        for (BanQinCdWhLoc item : items) {
            returnList.add(item.getLocCode());
        }
        // 不符合条件的单号，提示
        Object[] minusNos = wmCommon.minus(locationList.toArray(), returnList.toArray());
        StringBuilder str = new StringBuilder();
        for (String returnNo : returnList) {
            str.append(returnNo).append("\n");
        }
        if (StringUtils.isNotEmpty(str.toString())) {
            msg.addMessage(str.toString());
        }
        msg.setData(Arrays.asList(minusNos));
        return msg;
    }

    /**
     * 保存生成的库位信息
     */
    @Transactional
    public ResultMessage confirm(List<BanQinCdWhLoc> entityList) {
        ResultMessage msg;
        // 待生成库位
        List<String> listCode = Lists.newArrayList();
        String orgId = CollectionUtil.isNotEmpty(entityList) ? entityList.get(0).getOrgId() : "";
        for (BanQinCdWhLoc loc : entityList) {
            listCode.add(loc.getLocCode());
        }
        // 不重复的库位
        msg = validateLocation(listCode, orgId);
        listCode = (List<String>) msg.getData();
        for (BanQinCdWhLoc entity : entityList) {
            if (listCode.contains(entity.getLocCode())) {
                BanQinCdWhLoc example = new BanQinCdWhLoc();
                BeanUtils.copyProperties(entity, example);
                example.setId(IdGen.uuid());
                example.setIsNewRecord(true);
                this.save(example);
            }
        }
        msg.setSuccess(true);
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    public BanQinCdWhLoc getByCode(String locCode, String orgId) {
        return mapper.getByCode(locCode, orgId);
    }

    @Transactional
    public void remove(String locCode, String orgId) {
        mapper.remove(locCode, orgId);
    }


    @Transactional
    public void importFile(List<BanQinCdWhLoc> importList, String orgId) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            BanQinCdWhLoc data = importList.get(i);
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
            BanQinCdWhZone cdWhZone = cdWhZoneService.getByCode(data.getZoneCode(), orgId);
            if (cdWhZone == null) {
                throw new GlobalException("第" + (i + 1) + "行，库区不存在！");
            }
            BanQinCdWhLoc cdWhLoc = getByCode(data.getLocCode(), orgId);
            if (cdWhLoc != null) {
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
                data.setIsLoseId("00");
            }
            data.setIsEnable(Global.Y);
            data.setZoneName(cdWhZone.getZoneName());
            data.setOrgId(orgId);
            this.save(data);
        }
    }
}