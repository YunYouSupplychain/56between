package com.yunyou.modules.wms.task.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.report.entity.PutawayTaskLabel;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.mapper.BanQinWmTaskPaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 上架任务Service
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTaskPaService extends CrudService<BanQinWmTaskPaMapper, BanQinWmTaskPa> {
    @Autowired
    private SynchronizedNoService noService;

    public BanQinWmTaskPaEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmTaskPaEntity> findPage(Page page, BanQinWmTaskPaEntity banQinWmTaskPaEntity) {
        banQinWmTaskPaEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmTaskPaEntity.getOrgId()));
        dataRuleFilter(banQinWmTaskPaEntity);
        banQinWmTaskPaEntity.setPage(page);
        List<BanQinWmTaskPaEntity> list = mapper.findPage(banQinWmTaskPaEntity);
        page.setList(list);
        for (BanQinWmTaskPaEntity entity : list) {
            entity.setCurrentPaQtyEa(entity.getQtyPaEa());
        }
        return page;
    }

    public Page<BanQinWmTaskPaEntity> findGrid(Page page, BanQinWmTaskPaEntity banQinWmTaskPaEntity) {
        banQinWmTaskPaEntity.setPage(page);
        List<BanQinWmTaskPaEntity> list = mapper.findGrid(banQinWmTaskPaEntity);
        page.setList(list);
        for (BanQinWmTaskPaEntity entity : list) {
            entity.setCurrentPaQtyEa(entity.getQtyPaEa());
        }
        return page;
    }

    /**
     * 根据上架任务ID和上架任务行号查询上架任务
     *
     * @param paId
     * @param lineNo
     * @param orgId
     * @return
     */
    public BanQinWmTaskPaEntity getByPaIdAndLineNo(String paId, String lineNo, String orgId) {
        BanQinWmTaskPaEntity model = new BanQinWmTaskPaEntity();
        model.setPaId(paId);
        model.setLineNo(lineNo);
        model.setOrgId(orgId);
        List<BanQinWmTaskPaEntity> list = mapper.findPage(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            return list.get(0);
        }
        return new BanQinWmTaskPaEntity();
    }

    /**
     * 根据上架任务ID和上架任务行号查询上架任务Entity
     *
     * @param paId
     * @param lineNo
     * @param orgId
     * @return
     */
    public BanQinWmTaskPaEntity getEntityByPaIdAndLineNo(String paId, String lineNo, String orgId) {
        return this.getByPaIdAndLineNo(paId, lineNo, orgId);
    }

    /**
     * 汇总上架ID的上架数
     *
     * @param paId
     * @param orgId
     * @return
     */
    public Double getQtyPaByPaId(String paId, String orgId) {
        Double paEa = 0D;

        BanQinWmTaskPa model = new BanQinWmTaskPa();
        model.setPaId(paId);
        model.setOrgId(orgId);
        List<BanQinWmTaskPa> list = mapper.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            for (BanQinWmTaskPa e : list) {
                paEa = paEa + e.getQtyPaEa();
            }
        }
        return paEa;
    }

    /**
     * 通过任务ID，获得最新任务序号
     *
     * @param paId
     * @param orgId
     * @return
     */
    public String getNewPaLineNo(String paId, String orgId) {
        Integer lineNo = mapper.getMaxLineNo(paId, orgId);
        if (lineNo == null) {
            lineNo = 0;
        }
        return String.format("%02d", lineNo + 1);
    }

    /**
     * 描述： 生成上架任务记录
     *
     * @param taskPaEntity
     * @param planToLoc    计划上架库位
     * @param qtyPa        实际需要生成的上架数
     * @param uom
     * @param qtyPaUom     单位数
     */
    @Transactional
    public BanQinWmTaskPa saveTaskPa(BanQinWmTaskPaEntity taskPaEntity, String planToLoc, Double qtyPa, String uom, Double qtyPaUom) {
        String paId = "";
        BanQinWmTaskPa wmTaskPaModel = new BanQinWmTaskPa();
        // 如果收货明细已存在上架Id，不新增上架ID
        if (StringUtils.isNotEmpty(taskPaEntity.getPaIdRcv())) {
            paId = taskPaEntity.getPaIdRcv();
        } else {
            paId = noService.getDocumentNo(GenNoType.WM_PA_ID.name());
        }
        wmTaskPaModel.setPaId(paId);
        wmTaskPaModel.setLineNo(this.getNewPaLineNo(paId, taskPaEntity.getOrgId()));
        wmTaskPaModel.setOrderNo(taskPaEntity.getOrderNo());
        wmTaskPaModel.setOrderType(taskPaEntity.getOrderType());
        wmTaskPaModel.setStatus(WmsCodeMaster.TSK_NEW.getCode());
        wmTaskPaModel.setOwnerCode(taskPaEntity.getOwnerCode());
        wmTaskPaModel.setSkuCode(taskPaEntity.getSkuCode());
        wmTaskPaModel.setUom(uom);
        wmTaskPaModel.setPackCode(taskPaEntity.getPackCode());
        wmTaskPaModel.setLotNum(taskPaEntity.getLotNum());
        wmTaskPaModel.setPrintNum(0);
        // 上架规则
        wmTaskPaModel.setPaRule(taskPaEntity.getNewPaRule());
        wmTaskPaModel.setReserveCode(taskPaEntity.getNewReserveCode());
        // 源
        wmTaskPaModel.setFmLoc(taskPaEntity.getFromLoc());
        wmTaskPaModel.setFmId(taskPaEntity.getFromId());
        // 目标
        wmTaskPaModel.setToId(taskPaEntity.getFromId());
        wmTaskPaModel.setToLoc(planToLoc);
        // 推荐库位
        wmTaskPaModel.setSuggestLoc(planToLoc);
        // 计划上架数
        wmTaskPaModel.setQtyPaEa(qtyPa);
        wmTaskPaModel.setQtyPaUom(qtyPaUom);
        // 托盘Id
        wmTaskPaModel.setTraceId(taskPaEntity.getTraceId());
        wmTaskPaModel.setOrgId(taskPaEntity.getOrgId());
        this.save(wmTaskPaModel);
        return wmTaskPaModel;
    }

    /**
     * 描述： 供删除方法查找WmTaskPaModel对象，根据库位编码作为FmLoc或SuggestLoc或ToLoc字段查找条件
     *
     * @param locCode 库位编码
     * @param orgId
     */
    public ResultMessage getByLocCode(String locCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmTaskPa newModel = new BanQinWmTaskPa();
        // 设置查询对象的值
        newModel.setFmLoc(locCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        List<BanQinWmTaskPa> list = mapper.findList(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            msg.setSuccess(false);
            msg.setData(list.get(0));
            return msg;
        }

        // 通过locCode判断库位是否被调用
        BanQinWmTaskPa model = new BanQinWmTaskPa();
        // 设置查询对象的值
        model.setSuggestLoc(locCode);
        model.setOrgId(orgId);
        // 查询出调用此库位的对象
        list = mapper.findList(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            msg.setSuccess(false);
            msg.setData(list.get(0));
            return msg;
        }

        // 通过locCode判断库位是否被调用
        BanQinWmTaskPa example = new BanQinWmTaskPa();
        // 设置查询对象的值
        example.setToLoc(locCode);
        example.setOrgId(orgId);
        // 查询出调用此库位的对象
        list = mapper.findList(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            msg.setSuccess(false);
            msg.setData(list.get(0));
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 根据货主和商品获取WmTaskPaModel
     *
     * @param ownerCode
     * @param skuCode
     * @param orgId
     */
    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmTaskPa newModel = new BanQinWmTaskPa();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此商品的对象
        List<BanQinWmTaskPa> list = mapper.findList(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            msg.setSuccess(false);
            msg.setData(list.get(0));
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    public List<PutawayTaskLabel> getPaTaskReport(List<String> ids) {
        return mapper.getPaTaskReport(ids);
    }

    public List<BanQinWmTaskPaEntity> rfPaGetPATaskByTaskNoQuery(String paId, String orderNo, String zoneCode, String orgId) {
        return mapper.rfPaGetPATaskByTaskNoQuery(paId, orderNo, zoneCode, orgId);
    }

    /**
     * 描述：置空任务ID
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void updatePaIdByKitRemoveTaskPa(String paId, String orgId) {
        mapper.updatePaIdByKitRemoveTaskPa(paId, orgId);
    }

    public List<BanQinWmTaskPaEntity> paCountQuery(BanQinWmTaskPaEntity entity) {
        return mapper.paCountQuery(entity);
    }
}