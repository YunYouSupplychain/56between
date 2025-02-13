package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmItemPrice;
import com.yunyou.modules.oms.basic.entity.OmItemPriceEntity;
import com.yunyou.modules.oms.basic.mapper.OmItemPriceMapper;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：商品价格Service
 *
 * @auther: Jianhua on 2019/5/20
 */
@Service
@Transactional(readOnly = true)
public class OmItemPriceService extends CrudService<OmItemPriceMapper, OmItemPrice> {

    public OmItemPrice get(String id) {
        return super.get(id);
    }

    public OmItemPriceEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public OmItemPriceEntity getEntity(String customerNo, String skuCode, String channel, String priceType, String orgId, Date effectiveTime, Date expirationTime) {
        OmItemPrice omItemPrice = new OmItemPrice();
        omItemPrice.setCustomerNo(customerNo);
        omItemPrice.setSkuCode(skuCode);
        omItemPrice.setChannel(channel);
        omItemPrice.setPriceType(priceType);
        omItemPrice.setOrgId(orgId);
        omItemPrice.setEffectiveTime(effectiveTime);
        omItemPrice.setExpirationTime(expirationTime);
        return mapper.findEntity(omItemPrice);
    }

    public List<OmItemPrice> findList(OmItemPrice omItemPrice) {
        return super.findList(omItemPrice);
    }

    public Page<OmItemPrice> findPage(Page<OmItemPrice> page, OmItemPrice omItemPrice) {
        return super.findPage(page, omItemPrice);
    }

    public Page<OmItemPriceEntity> findPage(Page page, OmItemPriceEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void beforeSaveCheck(OmItemPrice entity) {
        if (entity.getEffectiveTime() == null || entity.getExpirationTime() == null) {
            throw new OmsException("生效时间和失效时间不能为空！");
        }
        if (entity.getEffectiveTime().after(entity.getExpirationTime())) {
            throw new OmsException("生效时间不能在失效时间之后！");
        }
        // 未输入客户时，该条价格针对所有客户。。。为了方便校验有效期，默认设置为“无”
        if (StringUtils.isBlank(entity.getCustomerNo())) {
            entity.setCustomerNo(OmsConstants.OMS_DEFAULT_PRICE_CUSTOMER);
        }
        List<OmItemPriceEntity> validityTermData = findValidityTermData(entity);
        if (StringUtils.isNotBlank(entity.getId())) {
            validityTermData = validityTermData.stream().filter(item -> !item.getId().equals(entity.getId())).collect(Collectors.toList());
        }
        if (validityTermData != null && validityTermData.size() > 0) {
            throw new OmsException("客户【" + entity.getCustomerNo() + "】商品【" + entity.getSkuCode() + "】渠道【" + entity.getChannel() + "】价格类型【" + entity.getPriceType() + "】存在有效期重叠的数据！");
        }
        if (StringUtils.isBlank(entity.getChannel())) {
            throw new OmsException("渠道不能为空");
        }
        if (StringUtils.isBlank(entity.getPriceType())) {
            throw new OmsException("价格类型不能为空");
        }
        if ("99".equals(entity.getAuditStatus())) {
            throw new OmsException("已经审核，无法修改");
        }
        save(entity);
    }

    @Override
    @Transactional
    public void save(OmItemPrice entity) {
        super.save(entity);
    }

    public List<OmItemPriceEntity> findValidityTermData(OmItemPrice entity) {
        return mapper.findValidityTermData(entity);
    }

    @Transactional
    public void delete(OmItemPrice omItemPrice) {
        super.delete(omItemPrice);
    }

    public Page<OmItemPriceEntity> popData(Page page, OmItemPriceEntity entity) {
        dataRuleFilter(entity);
        entity.setNoSkuList(mapper.popSkuData(entity));
        entity.setPage(page);
        page.setList(mapper.popDataAll(entity));
        return page;
    }

    public List<OmItemPriceEntity> findPopData(OmItemPriceEntity entity) {
        List<OmItemPriceEntity> list = mapper.popData(entity);
        if (CollectionUtil.isEmpty(list)) {
            entity.setCustomerNo(OmsConstants.OMS_DEFAULT_PRICE_CUSTOMER);
            list = mapper.popData(entity);
        }
        return list;
    }

    /**
     * 描述：审核
     * <p>
     * create by Jianhua on 2019/7/29
     */
    @Transactional
    public void audit(String id) {
        OmItemPrice omItemPrice = get(id);
        if (omItemPrice == null) {
            logger.info("ID=[" + id + "]的商品价格记录不存在");
            return;
        }
        if ("99".equals(omItemPrice.getAuditStatus())) {
            logger.info("ID=[" + id + "]的商品价格记录已审核");
            return;
        }
        User user = UserUtils.getUser();
        Date date = new Date();

        omItemPrice.setAuditStatus("99");
        omItemPrice.setAuditor(user.getName());
        omItemPrice.setAuditDate(date);
        omItemPrice.setUpdateBy(user);
        omItemPrice.setUpdateDate(date);
        this.save(omItemPrice);
    }

    /**
     * 描述：取消审核
     * <p>
     * create by Jianhua on 2019/7/29
     */
    @Transactional
    public void cancelAudit(String id) {
        OmItemPrice omItemPrice = get(id);
        if (omItemPrice == null) {
            logger.info("ID=[" + id + "]的商品价格记录不存在");
            return;
        }
        if ("00".equals(omItemPrice.getAuditStatus())) {
            logger.info("ID=[" + id + "]的商品价格记录未审核，无法取消审核");
            return;
        }
        User user = UserUtils.getUser();
        Date date = new Date();

        omItemPrice.setAuditStatus("00");
        omItemPrice.setAuditor(null);
        omItemPrice.setAuditDate(null);
        omItemPrice.setUpdateBy(user);
        omItemPrice.setUpdateDate(date);
        this.save(omItemPrice);
    }
}
