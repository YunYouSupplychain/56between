package com.yunyou.modules.wms.kit.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinCdWhBomDetailEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinCdWhBomDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组合件明细Service
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhBomDetailService extends CrudService<BanQinCdWhBomDetailMapper, BanQinCdWhBomDetail> {

    public BanQinCdWhBomDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinCdWhBomDetail> findList(BanQinCdWhBomDetail banQinCdWhBomDetail) {
        return super.findList(banQinCdWhBomDetail);
    }

    public Page<BanQinCdWhBomDetailEntity> findPage(Page page, BanQinCdWhBomDetail entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinCdWhBomDetail banQinCdWhBomDetail) {
        super.save(banQinCdWhBomDetail);
    }

    @Transactional
    public void delete(BanQinCdWhBomDetail banQinCdWhBomDetail) {
        super.delete(banQinCdWhBomDetail);
    }

    public void checkSaveBefore(BanQinCdWhBomDetail cdWhBomDetail) {
        if (StringUtils.isBlank(cdWhBomDetail.getSubSkuCode())) {
            throw new WarehouseException("子件不能为空");
        }
        if (StringUtils.isBlank(cdWhBomDetail.getPackCode())) {
            throw new WarehouseException("包装规格不能为空");
        }
        if (StringUtils.isBlank(cdWhBomDetail.getUom())) {
            throw new WarehouseException("包装单位不能为空");
        }
        if (StringUtils.isBlank(cdWhBomDetail.getSubSkuType())) {
            throw new WarehouseException("子件类型不能为空");
        }
        if (cdWhBomDetail.getQty() == null) {
            throw new WarehouseException("数量不能为空");
        }
        if (WmsCodeMaster.KIT_TYPE_COMBINE.getCode().equals(cdWhBomDetail.getKitType())) {
            if (cdWhBomDetail.getParentSkuCode().equals(cdWhBomDetail.getSubSkuCode())) {
                throw new WarehouseException("加工类型是组合加工，子件与父件不能一致");
            }
        }
        if (WmsCodeMaster.KIT_TYPE_ACCESSORY.getCode().equals(cdWhBomDetail.getKitType())
                || WmsCodeMaster.KIT_TYPE_VA.getCode().equals(cdWhBomDetail.getKitType())) {
            if (!cdWhBomDetail.getParentSkuCode().equals(cdWhBomDetail.getSubSkuCode())
                    && WmsCodeMaster.SUB_SKU_TYPE_COMMON.getCode().equals(cdWhBomDetail.getSubSkuType())) {
                throw new WarehouseException("加工类型是辅料加工或增值加工，普通子件与父件必须一致");
            }
            if (cdWhBomDetail.getParentSkuCode().equals(cdWhBomDetail.getSubSkuCode())
                    && WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode().equals(cdWhBomDetail.getSubSkuType())) {
                throw new WarehouseException("加工类型是辅料加工或增值加工，辅料子件与父件不能一致");
            }
        }
        if (cdWhBomDetail.getParentSkuCode().equals(cdWhBomDetail.getSubSkuCode())) {
            List<BanQinCdWhBomDetail> list = this.getByOwnerAndParentSkuAndKitTypeAndSubSku(cdWhBomDetail.getOwnerCode(), cdWhBomDetail.getParentSkuCode(), cdWhBomDetail.getKitType(), cdWhBomDetail.getSubSkuCode(), cdWhBomDetail.getOrgId());
            for (BanQinCdWhBomDetail detail : list) {
                if (!detail.getId().equals(cdWhBomDetail.getId())) {
                    throw new WarehouseException("子件重复，不能操作");
                }
            }
        }
        /*List<BanQinCdWhBomDetail> checkModels = getByOwnerAndParentSkuAndKitTypeAndSubSku(cdWhBomDetail.getOwnerCode(), cdWhBomDetail.getParentSkuCode(), cdWhBomDetail.getKitType(), cdWhBomDetail.getParentSkuCode(), cdWhBomDetail.getOrgId());
        if (checkModels.size() == 1) {
            if (!cdWhBomDetail.getParentSkuCode().equals(cdWhBomDetail.getSubSkuCode()) && !WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode().equals(cdWhBomDetail.getSubSkuType())) {
                throw new WarehouseException("子件类型不是辅料，不能操作");
            } else if (cdWhBomDetail.getParentSkuCode().equals(cdWhBomDetail.getSubSkuCode()) && !WmsCodeMaster.SUB_SKU_TYPE_COMMON.getCode().equals(cdWhBomDetail.getSubSkuType())) {
                throw new WarehouseException("子件类型不是普通，不能操作");
            }
        }
        List<String> parentSkuList = getParentSkuList(cdWhBomDetail.getOwnerCode(), cdWhBomDetail.getParentSkuCode(), cdWhBomDetail.getOrgId());
        if (parentSkuList.contains(cdWhBomDetail.getSubSkuCode())) {
            throw new WarehouseException("子件是当前组合件的父件，不能操作");
        }*/
    }

    public BanQinCdWhBomDetailEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：获取新的明细行号
     * <p>
     * create by Jianhua on 2019/8/20
     */
    public String getNewLineNo(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        Integer maxLineNo = mapper.getMaxLineNo(ownerCode, parentSkuCode, kitType, orgId);
        return String.format("%02d", (maxLineNo == null ? 0 : maxLineNo) + 1);
    }

    /**
     * 描述：校验是否被引用
     * <p>
     * create by Jianhua on 2019/8/19
     */
    public void checkIsReferenced(BanQinCdWhBomDetail banQinCdWhBomDetail) {
        Long l = mapper.checkIsReferenced(banQinCdWhBomDetail);
        if (l != null && l > 0) {
            throw new WarehouseException("商品" + banQinCdWhBomDetail.getSubSkuCode() + "子件类型" + banQinCdWhBomDetail.getSubSkuType() + "被引用，不能操作");
        }
    }

    /**
     * 描述：子件弹出窗(过滤当前组合件的所有父节点)
     * <p>
     * create by Jianhua on 2019/8/20
     */
    public Page<BanQinCdWhBomDetailEntity> findSubSkuList(Page page, BanQinCdWhBomDetailEntity entity) {
        // 当前组合件的所有父件
        List<String> parentSkuList = getParentSkuList(entity.getOwnerCode(), entity.getParentSkuCode(), entity.getOrgId());
//        parentSkuList.add(entity.getParentSkuCode());
        entity.setParentSkuCodeList(parentSkuList);

        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findSubSkuList(entity));
        return page;
    }

    /**
     * 描述：查找当子件的所有父件
     * <p>
     * create by Jianhua on 2019/8/20
     */
    public List<String> getParentSkuList(String ownerCode, String subSkuCode, String orgId) {
        List<String> rsList = Lists.newArrayList();

        // 查找当前子件的父件
        BanQinCdWhBomDetail cdWhBomDetail = new BanQinCdWhBomDetail();
        cdWhBomDetail.setOwnerCode(ownerCode);
        cdWhBomDetail.setSubSkuCode(subSkuCode);
        cdWhBomDetail.setOrgId(orgId);
        List<BanQinCdWhBomDetail> parentSkuList = mapper.getParentSku(cdWhBomDetail);
        for (BanQinCdWhBomDetail detail : parentSkuList) {
            if (detail.getParentSkuCode().equals(detail.getSubSkuCode())) {
                continue;
            }
            rsList.add(detail.getParentSkuCode());

            List<String> list = getParentSkuList(detail.getOwnerCode(), detail.getParentSkuCode(), detail.getOrgId());
            if (CollectionUtil.isNotEmpty(list)) {
                rsList.addAll(list);
            }
        }
        return rsList;
    }

    /**
     * 描述：根据货主编码、组合件、加工类型、子件编码 获取BOM明细
     * <p>
     * create by Jianhua on 2019/8/20
     */
    public List<BanQinCdWhBomDetail> getByOwnerAndParentSkuAndKitTypeAndSubSku(String ownerCode, String parentSkuCode, String kitType, String subSkuCode, String orgId) {
        BanQinCdWhBomDetail model = new BanQinCdWhBomDetail();
        model.setOwnerCode(ownerCode);
        model.setParentSkuCode(parentSkuCode);
        model.setKitType(kitType);
        model.setSubSkuCode(subSkuCode);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据货主、组合件编码、加工类型删除子件记录
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void deleteByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        mapper.deleteByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
    }

    /**
     * 描述：根据货主编码、组合件编码、加工类型、行号 获取BOM明细model
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinCdWhBomDetail getByOwnerAndParentSkuAndKitTypeAndLineNo(String ownerCode, String parentSkuCode, String kitType, String lineNo, String orgId) {
        BanQinCdWhBomDetail model = new BanQinCdWhBomDetail();
        model.setOwnerCode(ownerCode);
        model.setParentSkuCode(parentSkuCode);
        model.setKitType(kitType);
        model.setLineNo(lineNo);
        model.setOrgId(orgId);
        List<BanQinCdWhBomDetail> list = this.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：根据货主编码、组合件编码、加工类型 获取BOM明细model
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public List<BanQinCdWhBomDetail> getByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        BanQinCdWhBomDetail model = new BanQinCdWhBomDetail();
        model.setOwnerCode(ownerCode);
        model.setParentSkuCode(parentSkuCode);
        model.setKitType(kitType);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据货主编码、组合件编码、加工类型、行号 获取BOM明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinCdWhBomDetailEntity getEntityByOwnerAndParentSkuAndKitTypeAndLineNo(String ownerCode, String parentSkuCode, String kitType, String lineNo, String orgId) {
        return mapper.getEntityByOwnerAndParentSkuAndKitTypeAndLineNo(ownerCode, parentSkuCode, kitType, lineNo, orgId);
    }

    /**
     * 描述：根据货主编码、组合件编码、加工类型获取BOM明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public List<BanQinCdWhBomDetailEntity> getEntityByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        return mapper.getEntityByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
    }

    /**
     * 描述：保存BOM单 明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public BanQinCdWhBomDetailEntity saveBomDetail(BanQinCdWhBomDetailEntity entity) throws WarehouseException {
        BanQinCdWhBomDetail model = new BanQinCdWhBomDetail();
        BeanUtils.copyProperties(entity, model);
        if (StringUtils.isBlank(entity.getSubSkuCode())) {
            throw new WarehouseException("子件不能为空");
        }
        if (StringUtils.isBlank(entity.getPackCode())) {
            throw new WarehouseException("包装规格不能为空");
        }
        if (StringUtils.isBlank(entity.getUom())) {
            throw new WarehouseException("包装单位不能为空");
        }
        if (StringUtils.isBlank(entity.getSubSkuType())) {
            throw new WarehouseException("子件类型不能为空");
        }
        if (entity.getQty() == null) {
            throw new WarehouseException("数量不能为空");
        }
        if (WmsCodeMaster.KIT_TYPE_COMBINE.getCode().equals(entity.getKitType())) {
            if (entity.getParentSkuCode().equals(entity.getSubSkuCode())) {
                throw new WarehouseException("加工类型是组合加工，子件与父件不能一致");
            }
        }
        if (WmsCodeMaster.KIT_TYPE_ACCESSORY.getCode().equals(entity.getKitType())
                || WmsCodeMaster.KIT_TYPE_VA.getCode().equals(entity.getKitType())) {
            if (!entity.getParentSkuCode().equals(entity.getSubSkuCode())
                    && WmsCodeMaster.SUB_SKU_TYPE_COMMON.getCode().equals(entity.getSubSkuType())) {
                throw new WarehouseException("加工类型是辅料加工或增值加工，普通子件与父件必须一致");
            }
            if (entity.getParentSkuCode().equals(entity.getSubSkuCode())
                    && WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode().equals(entity.getSubSkuType())) {
                throw new WarehouseException("加工类型是辅料加工或增值加工，辅料子件与父件不能一致");
            }
        }
        // 如果父件与子件相同(辅料加工、增值加工)，校验子件编码是否重复
        if (entity.getParentSkuCode().equals(entity.getSubSkuCode())) {
            List<BanQinCdWhBomDetail> list = this.getByOwnerAndParentSkuAndKitTypeAndSubSku(entity.getOwnerCode(), entity.getParentSkuCode(), entity.getKitType(), entity.getSubSkuCode(), entity.getOrgId());
            if (list.size() > 0) {
                throw new WarehouseException("子件重复，不能操作");
            }
        }
        // 如果存在父件与子件相同的记录，校验除相同编码的子件是普通类型外，其他必须全是辅料类型
        List<BanQinCdWhBomDetail> checkModels = getByOwnerAndParentSkuAndKitTypeAndSubSku(entity.getOwnerCode(), entity.getParentSkuCode(), entity.getKitType(), entity.getParentSkuCode(), entity.getOrgId());
        if (checkModels.size() == 1) {
            // 存在相同的子件
            // 非父件记录必须是辅料类型
            if (!entity.getParentSkuCode().equals(entity.getSubSkuCode()) && !entity.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode())) {
                // 子件类型不是辅料，不能操作
                throw new WarehouseException("子件类型不是辅料，不能操作");
            }
            // 非父件记录必须是普通类型
            else if (entity.getParentSkuCode().equals(entity.getSubSkuCode()) && !entity.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_COMMON.getCode())) {
                // 子件类型不是普通，不能操作
                throw new WarehouseException("子件类型不是普通，不能操作");
            }

        }
        /*// 校验子件不能是他的父件的父件
        CdWhBomDetailParentSkuByTreeQueryCondition condition = new CdWhBomDetailParentSkuByTreeQueryCondition();
        condition.setOwnerCode(entity.getOwnerCode());
        condition.setParentSkuCode(entity.getParentSkuCode());
        condition.setOrgId(SessionContext.getUser().getOrgId());
        condition.setProjectId(SessionContext.getUser().getProjectId());
        List<CdWhBomDetailParentSkuByTreeQueryItem> items = this.dao.query(condition, CdWhBomDetailParentSkuByTreeQueryItem.class);
        if (items.size() > 0) {
            for (CdWhBomDetailParentSkuByTreeQueryItem item : items) {
                // 如果子件是父件的父节点，不可保存
                if (item.getParentSkuCode().equals(entity.getSubSkuCode())) {
                    // 子件是当前组合件的父件，不能操作
                    throw new WarehouseException(MsgUtils.getMessage("msg.wms.info.subParent"));
                }

            }
        }*/
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
        }
        this.save(model);
        return getEntityByOwnerAndParentSkuAndKitTypeAndLineNo(model.getOwnerCode(), model.getParentSkuCode(), model.getKitType(), model.getLineNo(), model.getOrgId());
    }

    /**
     * 描述：根据货主、组合件编码、加工类型、行号删除子件记录
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public void removeByOwnerAndParentSkuAndKitTypeAndLineNo(String ownerCode, String parentSkuCode, String kitType, String lineNo, String orgId) throws WarehouseException {
        BanQinCdWhBomDetail model = getByOwnerAndParentSkuAndKitTypeAndLineNo(ownerCode, parentSkuCode, kitType, lineNo, orgId);
        if (model != null) {
            checkIsReferenced(model);
            this.delete(model);
        }
    }

}