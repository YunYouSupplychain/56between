package com.yunyou.modules.wms.kit.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinWmTaskKitMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 加工任务Service
 *
 * @author Jianhua Liu
 * @version 2019-08-21
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTaskKitService extends CrudService<BanQinWmTaskKitMapper, BanQinWmTaskKit> {

    public BanQinWmTaskKit get(String id) {
        return super.get(id);
    }

    public List<BanQinWmTaskKit> findList(BanQinWmTaskKit banQinWmTaskKit) {
        return super.findList(banQinWmTaskKit);
    }

    public Page<BanQinWmTaskKitEntity> findPage(Page page, BanQinWmTaskKitEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmTaskKit banQinWmTaskKit) {
        super.save(banQinWmTaskKit);
    }

    @Transactional
    public void delete(BanQinWmTaskKit banQinWmTaskKit) {
        super.delete(banQinWmTaskKit);
    }

    /**
     * 描述：根据kitTaskId获取加工任务model
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmTaskKit getByKitTaskId(String kitTaskId, String orgId) {
        BanQinWmTaskKit model = new BanQinWmTaskKit();
        model.setKitTaskId(kitTaskId);
        model.setOrgId(orgId);
        List<BanQinWmTaskKit> list = this.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：根据订单号、加工行号获取完全加工的子件任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmTaskKit> getByKitNoAndKitLineNo(String kitNo, String kitLineNo, String orgId) {
        BanQinWmTaskKit model = new BanQinWmTaskKit();
        model.setKitNo(kitNo);
        model.setKitLineNo(kitLineNo);
        model.setStatus(WmsCodeMaster.SUB_KIT_FULL_KIT.getCode());
        model.setOrgId(orgId);
        return this.findList(model);
    }

    public BanQinWmTaskKitEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：根据订单号、加工行号获取完全加工的子件任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmTaskKitEntity> getEntityByKitNoAndKitLineNo(String kitNo, String kitLineNo, String orgId) {
        return mapper.getEntityByKitNoAndKitLineNo(kitNo, kitLineNo, orgId);
    }

    /**
     * 描述：根据kitNo获取加工任务entities
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmTaskKitEntity> getEntityByKitNo(String kitNo, String orgId) {
        return mapper.getEntityByKitNo(kitNo, orgId);
    }

    /**
     * 描述：根据kitTaskId 加工任务ID获取加工任务entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmTaskKitEntity getEntityByKitTaskId(String kitTaskId, String orgId) {
        return mapper.getEntityByKitTaskId(kitTaskId, orgId);
    }

    /**
     * 描述：获取加工任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmTaskKitEntity> getEntityByProcess(String processByCode, String kitNo, List<Object> noList, String status, String orgId) {
        List<BanQinWmTaskKitEntity> resultEntities = Lists.newArrayList();
        int size = noList.size();
        if (processByCode.equals(ProcessByCode.BY_KIT_SUB.getCode())) {
            for (int i = 0; i < size; i = i + 999) {
                Object[] subLineNos;
                if (size >= i + 999) {
                    subLineNos = noList.subList(i, i + 999).toArray();
                } else {
                    subLineNos = noList.subList(i, size).toArray();
                }
                List<BanQinWmTaskKitEntity> entities = mapper.getEntityByKitNoAndSubLineNoAndStatus(kitNo, subLineNos, status, orgId);
                if (CollectionUtil.isNotEmpty(entities)) {
                    resultEntities.addAll(entities);
                }
            }
        } else if (processByCode.equals(ProcessByCode.BY_TASK_KIT.getCode())) {
            for (int i = 0; i < size; i = i + 999) {
                Object[] kitTaskIds;
                if (size >= i + 999) {
                    kitTaskIds = noList.subList(i, i + 999).toArray();
                } else {
                    kitTaskIds = noList.subList(i, size).toArray();
                }
                List<BanQinWmTaskKitEntity> entities = mapper.getEntityByKitNoAndTaskIdAndStatus(kitNo, kitTaskIds, status, orgId);
                if (CollectionUtil.isNotEmpty(entities)) {
                    resultEntities.addAll(entities);
                }
            }
        }
        return resultEntities;
    }

    /**
     * 描述：根据订单号、父件行号、拣货状态 获取完全拣货的子件任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmTaskKitEntity> getEntityByParentLineNoAndStatus(String kitNo, String parentLineNo, String status, String orgId) {
        return mapper.getEntityByParentLineNoAndStatus(kitNo, parentLineNo, status, orgId);
    }
}