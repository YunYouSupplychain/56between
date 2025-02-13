package com.yunyou.modules.wms.inventory.service;

import com.yunyou.modules.wms.report.entity.TraceLabel;
import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvQuery;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmInvExportEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmInvImportEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 库存查询Service
 *
 * @author WMJ
 * @version 2019/02/28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvQueryService extends CrudService<BanQinWmInvQueryMapper, BanQinWmInvQuery> {
    @Autowired
    private BanQinInventoryService inventoryService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinCdWhLocService cdWhLocService;

    //按货主查询
    public Page<BanQinWmInvQuery> findPageByOwner(Page<BanQinWmInvQuery> page, BanQinWmInvQuery wmInvQuery) {
        wmInvQuery.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", wmInvQuery.getOrgId()));
        dataRuleFilter(wmInvQuery);
        wmInvQuery.setPage(page);
        page.setList(mapper.findByOwner(wmInvQuery));
        return page;
    }

    //按商品查询
    public Page<BanQinWmInvQuery> findPageBySku(Page<BanQinWmInvQuery> page, BanQinWmInvQuery wmInvQuery) {
        wmInvQuery.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", wmInvQuery.getOrgId()));
        dataRuleFilter(wmInvQuery);
        wmInvQuery.setPage(page);
        page.setList(mapper.findBySku(wmInvQuery));
        return page;
    }

    //按批次查询
    public Page<BanQinWmInvQuery> findPageByLot(Page<BanQinWmInvQuery> page, BanQinWmInvQuery wmInvQuery) {
        wmInvQuery.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", wmInvQuery.getOrgId()));
        dataRuleFilter(wmInvQuery);
        wmInvQuery.setPage(page);
        page.setList(mapper.findByLot(wmInvQuery));
        return page;
    }

    //按库位查询
    public Page<BanQinWmInvQuery> findPageByLoc(Page<BanQinWmInvQuery> page, BanQinWmInvQuery wmInvQuery) {
        wmInvQuery.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", wmInvQuery.getOrgId()));
        dataRuleFilter(wmInvQuery);
        wmInvQuery.setPage(page);
        page.setList(mapper.findByLoc(wmInvQuery));
        return page;
    }

    //按商品/库位查询
    public Page<BanQinWmInvQuery> findPageBySkuAndLoc(Page<BanQinWmInvQuery> page, BanQinWmInvQuery wmInvQuery) {
        wmInvQuery.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", wmInvQuery.getOrgId()));
        dataRuleFilter(wmInvQuery);
        wmInvQuery.setPage(page);
        page.setList(mapper.findBySkuAndLoc(wmInvQuery));
        return page;
    }

    //按批次/库位/跟踪号查询
    public Page<BanQinWmInvQuery> findPageByTraceId(Page<BanQinWmInvQuery> page, BanQinWmInvQuery wmInvQuery) {
        wmInvQuery.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", wmInvQuery.getOrgId()));
        dataRuleFilter(wmInvQuery);
        wmInvQuery.setPage(page);
        page.setList(mapper.findByTraceId(wmInvQuery));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<BanQinWmInvExportEntity> findExportInfo(Page page, BanQinWmInvExportEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("will.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findExportInfo(entity));
        return page;
    }

    @Transactional
    public ResultMessage importInventory(List<BanQinWmInvImportEntity> list, String orgId) {
        ResultMessage msg = new ResultMessage();
        inventoryService.checkInitInventory(true, orgId);
        List<String> ownerSkuList = Lists.newArrayList();
        List<String> ownerCodeList = Lists.newArrayList();
        List<String> locList = Lists.newArrayList();
        List<BanQinInventoryEntity> detailList = Lists.newArrayList();
        // 错误提示信息
        StringBuffer errorMsg = new StringBuffer();
        // 赋值校验非空
        for (int i = 0, size = list.size(); i < size; i++) {
            BanQinWmInvQuery entity = list.get(i);
            entity.setOrgId(orgId);
            if (StringUtils.isEmpty(entity.getOwnerCode()) && StringUtils.isEmpty(entity.getSkuCode())
                && StringUtils.isEmpty(entity.getLocCode()) && StringUtils.isEmpty(entity.getTraceId())
                && null == entity.getQty()) {
                continue;
            }
            String loc = entity.getLocCode();
            // 收集库位编码判断是否存在
            if (StringUtils.isNotEmpty(loc) && !locList.contains(loc)) {
                locList.add(loc);
            }
            String ownerSku = entity.getOwnerCode() + '@' + entity.getSkuCode();
            // 收集商品用于判断商品是否存在
            if (StringUtils.isNotEmpty(entity.getOwnerCode()) && !ownerCodeList.contains(entity.getOwnerCode())) {
                ownerCodeList.add(entity.getOwnerCode());
            }
            if (StringUtils.isNotEmpty(ownerSku) && !ownerSkuList.contains(ownerSku)) {
                if (StringUtils.isNotEmpty(entity.getOwnerCode()) && StringUtils.isNotEmpty(entity.getSkuCode())) {
                    ownerSkuList.add(ownerSku);
                }
            }
            ResultMessage rowMsg = parseRow(entity);
            detailList.add((BanQinInventoryEntity) rowMsg.getData());
            if (StringUtils.isNotEmpty(rowMsg.getMessage())) {
                errorMsg.append("第").append(i + 2).append("行:").append(rowMsg.getMessage()).append("<br>");
            }
        }
        // 判断商品编码是否存在数据库
        validateSkuCode(ownerSkuList, orgId, errorMsg);
        // 判断货主是否存在数据库
        validateOwnerCode(ownerCodeList, orgId, errorMsg);
        // 判断库位编码是否存在数据库
        validateLocCode(locList, orgId, errorMsg);
        // 校验失败
        if (StringUtils.isNotEmpty(errorMsg.toString())) {
            msg.setMessage(errorMsg.toString());
            msg.setSuccess(false);
            return msg;
        } else {
            for (BanQinInventoryEntity detail : detailList) {
                inventoryService.initInventory(detail);
            }
        }
        msg.setSuccess(true);
        msg.setMessage("操作成功");
        return msg;
    }

    @Transactional
    public ResultMessage parseRow(BanQinWmInvQuery entity) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        if (StringUtils.isEmpty(entity.getOwnerCode())) {
            errorMsg.append("货主编码不能为空!");
        }
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            errorMsg.append("商品编码不能为空!");
        }
        if (StringUtils.isEmpty(entity.getLocCode())) {
            errorMsg.append("库位不能为空!");
        }
        BanQinCdWhSku skuModel = cdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        BanQinInventoryEntity item = new BanQinInventoryEntity();
        item.setOwnerCode(entity.getOwnerCode());
        item.setSkuCode(entity.getSkuCode());
        item.setLocCode(entity.getLocCode());
        item.setOrgId(entity.getOrgId());
        // 跟踪号
        if (StringUtils.isBlank(entity.getTraceId())) {
            item.setTraceId("*");
        } else {
            item.setTraceId(entity.getTraceId());
        }
        item.setUom(WmsConstants.UOM_EA);
        Double qty = "".equals(NVL(entity.getQty())) ? 0D : entity.getQty();
        if (qty > 0) {
            item.setQtyUom(qty);
            item.setQtyEaOp(qty);
        } else {
            errorMsg.append("数量必须大于0!");
        }
        // 批次号查找
        ResultMessage newMsg = getWmInvLotNum(entity.getLotAtt01(), entity.getLotAtt02(), entity.getLotAtt03(), entity.getLotAtt04(),
            entity.getLotAtt05(), entity.getLotAtt06(), entity.getLotAtt07(), entity.getLotAtt08(),
            entity.getLotAtt09(), entity.getLotAtt10(), entity.getLotAtt11(), entity.getLotAtt12(),
            entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        if (newMsg.isSuccess()) {
            item.setLotNum(newMsg.getData().toString());// 批次号
        } else {
            errorMsg.append(newMsg.getMessage());
        }
        if (skuModel == null) {
            errorMsg.append("商品不存在!");
        } else {
            item.setPackCode(skuModel.getPackCode());
        }
        msg.setData(item);
        msg.addMessage(errorMsg.toString());
        return msg;
    }

    @Transactional
    public ResultMessage getWmInvLotNum(Object lotAtt01, Object lotAtt02, Object lotAtt03, Object lotAtt04,
                                        Object lotAtt05, Object lotAtt06, Object lotAtt07, Object lotAtt08,
                                        Object lotAtt09, Object lotAtt10, Object lotAtt11, Object lotAtt12,
                                        String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmInvLotAtt model = new BanQinWmInvLotAtt();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Pattern chinaP = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setOrgId(orgId);
        try {
            Matcher chinaM = chinaP.matcher(NVL(lotAtt01));
            boolean chinaKey = chinaM.matches();
            if (chinaKey) {
                Date time1 = format.parse(NVL(lotAtt01));
                model.setLotAtt01(time1);
            } else {
                if (!"".equals(NVL(lotAtt01))) {
                    msg.addMessage("批次属性1格式错误!");
                }
            }
            chinaM = chinaP.matcher(NVL(lotAtt02));
            chinaKey = chinaM.matches();
            if (chinaKey) {
                Date time2 = format.parse(NVL(lotAtt02));
                model.setLotAtt02(time2);
            } else {
                if (!"".equals(NVL(lotAtt02))) {
                    msg.addMessage("批次属性2格式错误!");
                }
            }
            chinaM = chinaP.matcher(NVL(lotAtt03));
            chinaKey = chinaM.matches();
            if (chinaKey) {
                Date time3 = format.parse(NVL(lotAtt03));
                model.setLotAtt03(time3);
            } else {
                if (!"".equals(NVL(lotAtt03))) {
                    msg.addMessage("批次属性3格式错误!");
                }
            }
            if (!"".equals(NVL(lotAtt01)) && !"".equals(NVL(lotAtt02))) {
                Date time1 = format.parse(NVL(lotAtt01));
                Date time2 = format.parse(NVL(lotAtt02));
                if (time1.getTime() > time2.getTime()) {
                    msg.addMessage("生产日期不能大于失效日期!");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.setLotAtt04(NVL(lotAtt04));
        model.setLotAtt05(NVL(lotAtt05));
        model.setLotAtt06(NVL(lotAtt06));
        model.setLotAtt07(NVL(lotAtt07));
        model.setLotAtt08(NVL(lotAtt08));
        model.setLotAtt09(NVL(lotAtt09));
        model.setLotAtt10(NVL(lotAtt10));
        model.setLotAtt11(NVL(lotAtt11));
        model.setLotAtt12(NVL(lotAtt12));
        String lotNum = "";
        if (msg.getMessage() != null && msg.getMessage().length() > 2) {
            msg.setSuccess(false);
        } else {
            try {
                lotNum = inventoryService.createInvLotNum(model);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
            msg.setSuccess(true);
        }
        msg.setData(lotNum);
        return msg;
    }

    private String NVL(Object value) {
        return null == value ? "" : value.toString().trim();
    }

    /**
     * 判断货主是否存在数据中
     */
    private void validateOwnerCode(List<String> ownerCodeList, String orgId, StringBuffer errMsg) {
        if (CollectionUtil.isNotEmpty(ownerCodeList)) {
            List<BanQinEbCustomer> existCustomer = ebCustomerService.getExistCustomer(ownerCodeList, orgId);
            List<String> collect = existCustomer.stream().map(BanQinEbCustomer::getEbcuCustomerNo).collect(Collectors.toList());
            List<String> result = ownerCodeList.stream().filter(t -> !collect.contains(t)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(result)) {
                errMsg.append("货主编码：");
                for (String ownerCode : result) {
                    errMsg.append("[").append(ownerCode).append("],");
                }
                errMsg.deleteCharAt(errMsg.length() - 1);
                errMsg.append("不存在<br>");
            }
        }
    }

    /**
     * 判断库位是否存在数据中
     */
    private void validateLocCode(List<String> locCodeList, String orgId, StringBuffer errMsg) {
        if (CollectionUtil.isNotEmpty(locCodeList)) {
            List<BanQinCdWhLoc> existLoc = cdWhLocService.getExistLoc(locCodeList, orgId);
            List<String> collect = existLoc.stream().map(BanQinCdWhLoc::getLocCode).collect(Collectors.toList());
            List<String> result = locCodeList.stream().filter(t -> !collect.contains(t)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(result)) {
                errMsg.append("库位编码：");
                for (String locCode : result) {
                    errMsg.append("[").append(locCode).append("],");
                }
                errMsg.deleteCharAt(errMsg.length() - 1);
                errMsg.append("不存在<br>");
            }
        }
    }

    /**
     * 判断商品是否存在数据中
     */
    private void validateSkuCode(List<String> ownerSkuList, String orgId, StringBuffer errMsg) {
        if (CollectionUtil.isNotEmpty(ownerSkuList)) {
            List<BanQinCdWhSku> existLoc = cdWhSkuService.getExistSku(convertToString(ownerSkuList), orgId);
            List<String> collect = existLoc.stream().map(c -> c.getOwnerCode() + "@" + c.getSkuCode()).collect(Collectors.toList());
            List<String> result = ownerSkuList.stream().filter(t -> !collect.contains(t)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(result)) {
                errMsg.append("商品编码：");
                for (String skuCode : result) {
                    errMsg.append("[").append(skuCode.split("@")[1]).append("],");
                }
                errMsg.deleteCharAt(errMsg.length() - 1);
                errMsg.append("不存在<br>");
            }
        }
    }

    private String convertToString(List<String> ownerSkuList) {
        StringBuilder builder = new StringBuilder("(");
        for (String ownerSku : ownerSkuList) {
            String[] split = ownerSku.split("@");
            builder.append("('").append(split[0]).append("','").append(split[1]).append("'),");
        }
        builder.deleteCharAt(builder.length() - 1).append(")");

        return builder.toString();
    }

    public List<TraceLabel> getTraceLabel(List<String> ids) {
        return mapper.getTraceLabel(ids);
    }
}