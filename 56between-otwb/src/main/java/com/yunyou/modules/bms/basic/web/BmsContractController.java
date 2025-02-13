package com.yunyou.modules.bms.basic.web;

import com.alibaba.fastjson.JSON;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.entity.BmsContractCostItem;
import com.yunyou.modules.bms.basic.entity.BmsContractStoragePrice;
import com.yunyou.modules.bms.basic.entity.BmsContractStorageSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractCostItemEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractEntity;
import com.yunyou.modules.bms.basic.service.BmsContractCostItemService;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.yunyou.modules.bms.basic.service.BmsContractStoragePriceService;
import com.yunyou.modules.bms.basic.service.BmsContractStorageSteppedPriceService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.service.calc.BmsCalculatorService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 描述：合同Controller
 *
 * @author liujianhua
 * @version 2019-11-14
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsContract")
public class BmsContractController extends BaseController {
    @Autowired
    private BmsContractService bmsContractService;
    @Autowired
    private BmsContractCostItemService bmsContractCostItemService;
    @Autowired
    private BmsContractStoragePriceService bmsContractStoragePriceService;
    @Autowired
    private BmsContractStorageSteppedPriceService bmsContractStorageSteppedPriceService;
    @Autowired
    private BmsCalculatorService bmsCalculatorService;

    /**
     * 列表页面
     */
    @RequiresPermissions("bms:bmsContract:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsContractEntity", new BmsContractEntity());
        return "modules/bms/basic/bmsContractList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsContract bmsContract, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsContractService.findPage(new Page<>(request, response), bmsContract));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"bms:bmsContract:view", "bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsContractEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsContractService.getEntity(entity.getId());
        }
        model.addAttribute("bmsContractEntity", entity);
        return "modules/bms/basic/bmsContractForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsContract bmsContract, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsContract)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsContractService.save(bmsContract);
            j.put("id", bmsContract.getId());
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                bmsContractService.delete(bmsContractService.getEntity(id));
            }
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除合同[ids=" + ids + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生效/失效
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:valid")
    @RequestMapping(value = "valid")
    public AjaxJson valid(String ids) {
        AjaxJson j = new AjaxJson();

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BmsContract bmsContract = bmsContractService.get(id);
            bmsContract.setContractStatus(BmsConstants.CONTRACT_VALID);
            bmsContractService.save(bmsContract);
        }
        return j;
    }

    /**
     * 生效/失效
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:invalid")
    @RequestMapping(value = "invalid")
    public AjaxJson invalid(String ids) {
        AjaxJson j = new AjaxJson();

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BmsContract bmsContract = bmsContractService.get(id);
            bmsContract.setContractStatus(BmsConstants.CONTRACT_INVALID);
            bmsContractService.save(bmsContract);
        }
        return j;
    }

    /**
     * 描述：复制
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:copy")
    @RequestMapping("copy")
    public AjaxJson copy(String id, String settleObjectCode, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            bmsContractService.copy(id, settleObjectCode, orgId);
        } catch (Exception e) {
            logger.error("【BMS复制】", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsContract bmsContract, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsContractService.findPage(new Page<>(request, response), bmsContract));
    }


    /**
     * 费用项目列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:list")
    @RequestMapping(value = "costItemData")
    public Map<String, Object> costItemData(BmsContractCostItem bmsContractCostItem) {
        Page<BmsContractCostItemEntity> page = new Page<>();
        page.setList(bmsContractCostItemService.findByContract(bmsContractCostItem.getSysContractNo(), bmsContractCostItem.getOrgId()));
        return getBootstrapData(page);
    }

    /**
     * 描述：明细页面
     */
    @RequiresPermissions(value = "bms:bmsContract:view")
    @RequestMapping(value = "costItemForm")
    public String costItemForm(BmsContractCostItemEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsContractCostItemService.getEntity(entity.getId());
        }
        model.addAttribute("bmsContractCostItemEntity", entity);
        return "modules/bms/basic/bmsContractCostItemForm";
    }

    /**
     * 描述：保存明细
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "saveCostItem")
    public AjaxJson saveCostItem(BmsContractCostItemEntity entity) {
        AjaxJson j = new AjaxJson();

        try {
            bmsContractCostItemService.saveEntity(entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存合同明细异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：删除明细
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "deleteCostItem")
    public AjaxJson deleteCostItem(String ids) {
        AjaxJson j = new AjaxJson();

        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                bmsContractCostItemService.delete(bmsContractCostItemService.getEntity(id));
            }
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除明细[ids=" + ids + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }


    /**
     * 描述：仓储价格页面
     */
    @RequiresPermissions(value = "bms:bmsContract:view")
    @RequestMapping(value = "storagePrice/list")
    public String storagePriceList(String costItemId, Model model) {
        model.addAttribute("bmsContractCostItemEntity", bmsContractCostItemService.getEntity(costItemId));
        return "modules/bms/basic/bmsContractStoragePriceList";
    }

    /**
     * 描述：仓储价格数据
     */
    @ResponseBody
    @RequiresPermissions(value = "bms:bmsContract:view")
    @RequestMapping(value = "storagePrice/data")
    public Map<String, Object> storagePriceData(BmsContractStoragePrice bmsContractStoragePrice, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsContractStoragePriceService.findPage(new Page<>(request, response, -1), bmsContractStoragePrice));
    }

    /**
     * 描述：仓储阶梯价格数据
     */
    @ResponseBody
    @RequiresPermissions(value = "bms:bmsContract:view")
    @RequestMapping(value = "storagePrice/stepPrice/data")
    public Map<String, Object> storageStepPriceData(BmsContractStorageSteppedPrice bmsContractStorageSteppedPrice, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsContractStorageSteppedPriceService.findPage(new Page<>(request, response, -1), bmsContractStorageSteppedPrice));
    }

    /**
     * 描述：仓储价格页面
     */
    @RequiresPermissions(value = "bms:bmsContract:view")
    @RequestMapping(value = "storagePrice/form")
    public String storagePriceForm(BmsContractStoragePrice bmsContractStoragePrice, Model model) {
        if (StringUtils.isNotBlank(bmsContractStoragePrice.getId())) {
            bmsContractStoragePrice = bmsContractStoragePriceService.get(bmsContractStoragePrice.getId());
            if (bmsContractStoragePrice != null) {
                bmsContractStoragePrice.setSteppedPrices(bmsContractStorageSteppedPriceService.findByFkId(bmsContractStoragePrice.getId()));
            }
        }
        model.addAttribute("bmsContractStoragePrice", bmsContractStoragePrice);
        return "modules/bms/basic/bmsContractStoragePriceForm";
    }

    /**
     * 描述：明细关联仓储价格
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "storagePrice/save")
    public AjaxJson associateStoragePrice(BmsContractStoragePrice bmsContractStoragePrice) {
        AjaxJson j = new AjaxJson();
        try {
            bmsContractStoragePriceService.save(bmsContractStoragePrice);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("明细关联服务价格[" + JSON.toJSONString(bmsContractStoragePrice) + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：删除仓储价格
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:del")
    @RequestMapping(value = "deleteStoragePrice")
    public AjaxJson deleteStoragePrice(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                bmsContractStoragePriceService.delete(new BmsContractStoragePrice(id));
            }
        } catch (BmsException e) {
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
     * 描述：删除仓储阶梯价格
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:del")
    @RequestMapping(value = "storageStepPrice/delete")
    public AjaxJson deleteStorageStepPrice(String stepPriceId) {
        AjaxJson j = new AjaxJson();
        try {
            bmsContractStorageSteppedPriceService.delete(new BmsContractStorageSteppedPrice(stepPriceId));
        } catch (BmsException e) {
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
     * 描述：运输价格页面
     */
    @RequiresPermissions(value = "bms:bmsContract:view")
    @RequestMapping(value = "transportPriceList")
    public String transportPriceForm(String costItemId, Model model) {
        model.addAttribute("bmsContractCostItemEntity", bmsContractCostItemService.getEntity(costItemId));
        return "modules/bms/basic/bmsContractTransportPriceList";
    }

    /**
     * 描述：明细关联运输体系
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "associateTransportGroup")
    public AjaxJson associateTransportGroup(BmsContractCostItemEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            BmsContractCostItem bmsContractCostItem = bmsContractCostItemService.get(entity.getId());
            if (bmsContractCostItem != null) {
                bmsContractCostItem.setTransportGroupCode(entity.getTransportGroupCode());
                bmsContractCostItemService.save(bmsContractCostItem);
            }
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("明细关联运输体系[id=" + entity.getId() + ", transportGroupCode=" + entity.getTransportGroupCode() + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }


    /**
     * 描述：费用计算
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:calc")
    @RequestMapping(value = "calc")
    public AjaxJson calc(@RequestParam String ids, @RequestParam Date fmDate, @RequestParam Date toDate) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = bmsCalculatorService.batchCalcByContact(Arrays.asList(ids.split(",")), fmDate, toDate);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 描述：费用计算
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContract:batchCalc")
    @RequestMapping(value = "batchCalc", method = RequestMethod.POST)
    public AjaxJson batchCalc(@RequestBody BmsContractEntity entity) {
        AjaxJson j = new AjaxJson();

        List<String> idList = bmsContractService.findPage(new Page<>(), entity).getList().stream().map(BmsContract::getId).collect(Collectors.toList());
        ResultMessage msg = bmsCalculatorService.batchCalcByContact(idList, entity.getFmDate(), entity.getToDate());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }
}