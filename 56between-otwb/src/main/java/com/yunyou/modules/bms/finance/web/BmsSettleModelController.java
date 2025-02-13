package com.yunyou.modules.bms.finance.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.entity.BmsSettleModel;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelEntity;
import com.yunyou.modules.bms.finance.service.calc.BmsCalculatorService;
import com.yunyou.modules.bms.finance.service.BmsSettleModelDetailService;
import com.yunyou.modules.bms.finance.service.BmsSettleModelService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 结算模型Controller
 *
 * @author Jianhua Liu
 * @version 2019-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/finance/bmsSettleModel")
public class BmsSettleModelController extends BaseController {
    @Autowired
    private BmsSettleModelService bmsSettleModelService;
    @Autowired
    private BmsSettleModelDetailService bmsSettleModelDetailService;
    @Autowired
    private BmsCalculatorService bmsCalculatorService;

    @ModelAttribute
    public BmsSettleModelEntity get(@RequestParam(required = false) String id) {
        BmsSettleModelEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsSettleModelService.getEntity(id);
        }
        if (entity == null) {
            entity = new BmsSettleModelEntity();
        }
        return entity;
    }

    /**
     * 结算模型列表页面
     */
    @RequiresPermissions("bms:finance:bmsSettleModel:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/finance/bmsSettleModelList";
    }

    /**
     * 结算模型列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsSettleModel:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsSettleModelEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsSettleModelService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑结算模型表单页面
     */
    @RequiresPermissions(value = {"bms:finance:bmsSettleModel:view", "bms:finance:bmsSettleModel:add", "bms:finance:bmsSettleModel:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsSettleModelEntity entity, Model model) {
        model.addAttribute("bmsSettleModelEntity", entity);
        return "modules/bms/finance/bmsSettleModelForm";
    }

    /**
     * 保存结算模型
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:finance:bmsSettleModel:add", "bms:finance:bmsSettleModel:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsSettleModel entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsSettleModelService.save(entity);
            j.put("entity", entity);
        } catch (DuplicateKeyException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(null, e);
            }
            j.setSuccess(false);
            j.setMsg("数据已存在");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除结算模型
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsSettleModel:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsSettleModelService.delete(bmsSettleModelService.get(id));
        }
        j.setMsg("删除结算模型成功");
        return j;
    }

    /**
     * 描述：查询结算模型明细
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsSettleModel:list")
    @RequestMapping(value = "findDetail")
    public Map<String, Object> findDetail(BmsSettleModelDetailEntity entity) {
        Page<BmsSettleModelDetailEntity> page = new Page<>();
        page.setList(bmsSettleModelDetailService.findEntityBySettleModelCode(entity.getSettleModelCode(), entity.getOrgId()));
        return getBootstrapData(page);
    }

    /**
     * 描述：选择合同科目页面
     */
    @RequiresPermissions(value = {"bms:finance:bmsSettleModel:view", "bms:finance:bmsSettleModel:add", "bms:finance:bmsSettleModel:edit"}, logical = Logical.OR)
    @RequestMapping("selectContractSubject")
    public String selectContractSubject(BmsSettleModelEntity entity, Model model) {
        entity.setDetailList(bmsSettleModelService.findContractSubject(entity));
        model.addAttribute("bmsSettleModelEntity", entity);
        return "modules/bms/finance/bmsSelectContractSubjectForm";
    }

    /**
     * 描述：添加模型明细
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:finance:bmsSettleModel:add", "bms:finance:bmsSettleModel:edit"}, logical = Logical.OR)
    @RequestMapping("addDetail")
    public AjaxJson addDetail(@RequestBody BmsSettleModelEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            bmsSettleModelDetailService.addDetail(entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：删除模型明细
     */
    @ResponseBody
    @RequiresPermissions(value = "bms:finance:bmsSettleModel:del")
    @RequestMapping("deleteDetail")
    public AjaxJson deleteDetail(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder message = new StringBuilder("操作成功");
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            try {
                bmsSettleModelDetailService.delete(bmsSettleModelDetailService.get(idArray[i]));
            } catch (BmsException e) {
                message.append("\r\n第").append(i + 1).append("行删除失败，").append(e.getMessage());
            } catch (Exception e) {
                logger.error("删除运输价格[id=" + idArray[i] + "]", e);
                message.append("\r\n第").append(i + 1).append("行删除失败，").append(e.getMessage());
            }
        }
        j.setMsg(message.toString());
        return j;
    }

    /**
     * 描述：根据模型明细ID查询明细（扩展实体）
     */
    @ResponseBody
    @RequestMapping("getDetailEntity")
    public BmsSettleModelDetailEntity getDetailEntity(String modelDetailId) {
        return bmsSettleModelDetailService.getEntity(modelDetailId);
    }

    /**
     * 描述：保存模型明细参数
     */
    @ResponseBody
    @RequestMapping("saveDetailParameter")
    public AjaxJson saveDetailParameter(BmsSettleModelDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            bmsSettleModelDetailService.saveParameter(entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：模型复制
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsSettleModel:copy")
    @RequestMapping("copy")
    public AjaxJson copy(String settleModelCode, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            bmsSettleModelService.copy(settleModelCode, orgId);
        } catch (Exception e) {
            logger.error("【BMS结算模型复制】", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：费用计算
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsSettleModel:calc")
    @RequestMapping(value = "calc")
    public AjaxJson calc(@RequestParam String ids, @RequestParam Date fmDate, @RequestParam Date toDate) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = bmsCalculatorService.batchCalcByModel(Arrays.asList(ids.split(",")), fmDate, toDate);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 描述：费用计算
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsSettleModel:batchCalc")
    @RequestMapping(value = "batchCalc", method = RequestMethod.POST)
    public AjaxJson batchCalc(@RequestBody BmsSettleModelEntity entity) {
        AjaxJson j = new AjaxJson();
        Page<BmsSettleModel> page = bmsSettleModelService.findPage(new Page<>(), entity);
        List<BmsSettleModel> list = page.getList();

        ResultMessage msg = bmsCalculatorService.batchCalcByModel(list.stream().map(BmsSettleModel::getId).collect(Collectors.toList()), entity.getFmDate(), entity.getToDate());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }
}