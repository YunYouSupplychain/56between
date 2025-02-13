package com.yunyou.modules.oms.order.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmBusinessOrderTypeRelation;
import com.yunyou.modules.oms.basic.service.OmBusinessOrderTypeRelationService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.OmChainDetail;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.entity.OmChainHeaderEntity;
import com.yunyou.modules.oms.order.manager.OmTaskGenManager;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应链订单生成作业任务Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/order/omChainCreateTask")
public class OmChainCreateTaskController extends BaseController {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;
    @Autowired
    private OmTaskGenManager omTaskGenManager;

    /**
     * 作业任务生成调整页面
     */
    @RequiresPermissions(value = {"order:omChainHeader:createTask", "order:omChainB2CECommerceOrder:createTask", "order:omChainB2CGroupPurchaseOrder:createTask",
            "order:omChainContractLogisticsOrder:createTask", "order:omChainSupDistributionOrder:createTask", "order:omChainCusReturnOrder:createTask",
            "order:omChainDCOrder:createTask", "order:omChainLTLLineHaulOrder:createTask", "order:omChainFABDistributionOrder:createTask",
            "order:omChainSupReturnOrder:createTask"}, logical = Logical.OR)
    @RequestMapping(value = "createTaskForm")
    public String createTaskForm(String id, Model model) {
        OmChainHeaderEntity entity = omChainHeaderService.getEntity(id);
        List<OmBusinessOrderTypeRelation> typeList = omBusinessOrderTypeRelationService.getByBusinessOrderType(entity.getBusinessOrderType(), entity.getOrgId());
        if (typeList.stream().anyMatch(o -> OmsConstants.OMS_TASK_TYPE_01.equals(o.getOrderType()))) {
            entity.setIsHaveSo("Y");
        }
        entity.setOmChainDetailList(entity.getOmChainDetailList().stream().filter(o -> o.getPlanTaskQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList()));
        model.addAttribute("omChainHeaderEntity", entity);
        return "modules/oms/order/omChainCreateTaskForm";
    }

    /**
     * 描述：重新获取库存可用数量
     *
     * @author zyf on 2019/6/5
     */
    @ResponseBody
    @RequiresPermissions(value = {"order:omChainHeader:createTask", "order:omChainB2CECommerceOrder:createTask", "order:omChainB2CGroupPurchaseOrder:createTask",
            "order:omChainContractLogisticsOrder:createTask", "order:omChainSupDistributionOrder:createTask", "order:omChainCusReturnOrder:createTask",
            "order:omChainDCOrder:createTask", "order:omChainLTLLineHaulOrder:createTask", "order:omChainFABDistributionOrder:createTask",
            "order:omChainSupReturnOrder:createTask"}, logical = Logical.OR)
    @RequestMapping(value = "resetAvailableQty", method = RequestMethod.POST)
    public AjaxJson resetAvailableQty(OmChainHeader omChainHeader) {
        AjaxJson j = new AjaxJson();

        try {
            // 剔除空数据行
            List<OmChainDetail> omChainDetails = omChainHeader.getOmChainDetailList().stream().filter(o -> StringUtils.isNotBlank(o.getId())).collect(Collectors.toList());
            omChainHeader.setOmChainDetailList(omChainDetails);
            for (OmChainDetail omChainDetail : omChainHeader.getOmChainDetailList()) {
                double availableQty = omSaleInventoryService.getAvailableQty(omChainHeader.getOwner(), omChainDetail.getSkuCode(), omChainHeader.getWarehouse());
                omChainDetail.setAvailableQty(BigDecimal.valueOf(availableQty));
            }

            j.put("entity", omChainHeader);
            j.setSuccess(true);
            j.setMsg("获取库存可用数量成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：生成任务
     *
     * @author Jianhua on 2019/5/6
     */
    @ResponseBody
    @RequiresPermissions(value = {"order:omChainHeader:createTask", "order:omChainB2CECommerceOrder:createTask", "order:omChainB2CGroupPurchaseOrder:createTask",
            "order:omChainContractLogisticsOrder:createTask", "order:omChainSupDistributionOrder:createTask", "order:omChainCusReturnOrder:createTask",
            "order:omChainDCOrder:createTask", "order:omChainLTLLineHaulOrder:createTask", "order:omChainFABDistributionOrder:createTask",
            "order:omChainSupReturnOrder:createTask"}, logical = Logical.OR)
    @RequestMapping(value = "createTask", method = RequestMethod.POST)
    public AjaxJson createTask(OmChainHeaderEntity entity) {
        AjaxJson j = new AjaxJson();

        try {
            OmChainHeader omChainHeader = omChainHeaderService.get(entity.getId());
            omChainHeader.setWarehouse(entity.getWarehouse());
            // 剔除空数据行和计划任务数量为0的数据行
            omChainHeader.setOmChainDetailList(entity.getOmChainDetailList().stream().filter(o -> StringUtils.isNotBlank(o.getId()) && o.getPlanTaskQty().doubleValue() != 0D).collect(Collectors.toList()));
            omTaskGenManager.genTask(omChainHeader);

            j.setSuccess(true);
            j.put("omChainDetailList", omChainHeaderService.findDetails(omChainHeader).stream().filter(o -> o.getPlanTaskQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList()));
            j.setMsg("生成任务成功");
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}
