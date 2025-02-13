package com.yunyou.modules.bms.basic.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSkuPriceEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsContractSkuPriceImport;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.yunyou.modules.bms.basic.service.BmsContractSkuPriceService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 合同商品价格Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsContractSkuPrice")
public class BmsContractSkuPriceController extends BaseController {
    @Autowired
    private BmsContractSkuPriceService bmsContractSkuPriceService;
    @Autowired
    private BmsContractService bmsContractService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public BmsContractSkuPriceEntity get(@RequestParam(required = false) String id) {
        BmsContractSkuPriceEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsContractSkuPriceService.getEntity(id);
        }
        if (entity == null) {
            entity = new BmsContractSkuPriceEntity();
        }
        return entity;
    }

    /**
     * 合同商品价格列表页面
     */
    @RequiresPermissions("bms:bmsContractSkuPrice:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsContractSkuUnitPriceList";
    }

    /**
     * 合同商品价格列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContractSkuPrice:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsContractSkuPriceEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsContractSkuPriceService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑合同商品价格表单页面
     */
    @RequiresPermissions(value = {"bms:bmsContractSkuPrice:view", "bms:bmsContractSkuPrice:add", "bms:bmsContractSkuPrice:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsContractSkuPriceEntity entity, Model model) {
        model.addAttribute("bmsContractSkuPriceEntity", entity);
        return "modules/bms/basic/bmsContractSkuUnitPriceForm";
    }

    /**
     * 保存合同商品价格
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContractSkuPrice:add", "bms:bmsContractSkuPrice:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsContractSkuPrice bmsContractSkuPrice, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsContractSkuPrice)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsContractSkuPriceService.save(bmsContractSkuPrice);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除合同商品价格
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContractSkuPrice:del", "bms:bmsContract:del"}, logical = Logical.OR)
    @RequestMapping(value = "delete")
    public AjaxJson delete(BmsContractSkuPrice bmsContractSkuPrice) {
        AjaxJson j = new AjaxJson();
        bmsContractSkuPriceService.delete(bmsContractSkuPrice);
        j.setMsg("删除合同商品价格成功");
        return j;
    }

    /**
     * 批量删除合同商品价格
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContractSkuPrice:del", "bms:bmsContract:del"}, logical = Logical.OR)
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsContractSkuPriceService.delete(bmsContractSkuPriceService.get(id));
        }
        j.setMsg("删除合同商品价格成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsContractSkuPrice:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsContractSkuPrice bmsContractSkuPrice, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsContractSkuPrice> page = bmsContractSkuPriceService.findPage(new Page<>(request, response, -1), bmsContractSkuPrice);
            new ExportExcel(null, BmsContractSkuPrice.class).setDataList(page.getList()).write(response, "合同商品价格.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出合同商品价格记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("bms:bmsContractSkuPrice:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int lineNo = 0;
            StringBuilder failureMsg = new StringBuilder();
            Map<String, BmsContractSkuPrice> skuPriceMap = Maps.newHashMap();

            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsContractSkuPriceImport> list = ei.getDataList(BmsContractSkuPriceImport.class);
            for (BmsContractSkuPriceImport dataImport : list) {
                lineNo++;
                ResultMessage message = this.checkImportData(dataImport);
                if (message.isSuccess()) {
                    String s = dataImport.getSysContractNo() + dataImport.getSkuClass() + dataImport.getSkuCode() + dataImport.getOrgCode();
                    skuPriceMap.put(s, (BmsContractSkuPrice) message.getData());
                } else {
                    failureMsg.append("第").append(lineNo).append("行").append(message.getMessage()).append(";");
                }
            }
            if (StringUtils.isBlank(failureMsg)) {
                skuPriceMap.values().forEach(bmsContractSkuPrice -> bmsContractSkuPriceService.save(bmsContractSkuPrice));
                addMessage(redirectAttributes, "已成功导入" + skuPriceMap.values().size() + "条合同商品价格记录");
            } else {
                addMessage(redirectAttributes, failureMsg.toString());
            }
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入合同商品价格失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsContractSkuPrice?repage";
    }

    private ResultMessage checkImportData(BmsContractSkuPriceImport dataImport) {
        StringBuilder msg = new StringBuilder();
        BmsContractSkuPrice skuPrice = new BmsContractSkuPrice();

        if (StringUtils.isBlank(dataImport.getOrgCode())) {
            msg.append("机构代码不能为空");
        }
        Office office = officeService.getByCode(dataImport.getOrgCode());
        if (office == null) {
            msg.append(",机构[").append(dataImport.getOrgCode()).append("]不存在");
        } else {
            skuPrice.setOrgId(office.getId());
        }
        if (StringUtils.isBlank(dataImport.getSysContractNo())) {
            msg.append(",系统合同编码不能为空");
        } else if (office != null) {
            BmsContract bmsContract = bmsContractService.getByContract(dataImport.getSysContractNo(), office.getId());
            if (bmsContract == null) {
                msg.append(",系统合同编号[").append(dataImport.getSysContractNo()).append("]不存在");
            } else {
                skuPrice.setSysContractNo(dataImport.getSysContractNo());
            }
        }
        if (dataImport.getPrice() == null && dataImport.getTaxPrice() == null) {
            msg.append(",未税价格与含税价格至少有一项不能为空");
        }
        skuPrice.setSkuClass(dataImport.getSkuClass());
        skuPrice.setSkuCode(dataImport.getSkuCode());
        skuPrice.setSkuName(dataImport.getSkuName());
        skuPrice.setPrice(dataImport.getPrice());
        skuPrice.setTaxPrice(dataImport.getTaxPrice());

        BmsContractSkuPrice bmsContractSkuPrice = bmsContractSkuPriceService.getContractPrice(skuPrice.getSysContractNo(), skuPrice.getSkuClass(), skuPrice.getSkuCode(), skuPrice.getOrgId());
        if (bmsContractSkuPrice != null) {
            if (StringUtils.isNotBlank(skuPrice.getSkuCode()) && StringUtils.isNotBlank(bmsContractSkuPrice.getSkuCode())
                    && skuPrice.getSkuCode().equals(bmsContractSkuPrice.getSkuCode())) {
                bmsContractSkuPrice.setSkuClass(skuPrice.getSkuClass());
                bmsContractSkuPrice.setPrice(skuPrice.getPrice());
                bmsContractSkuPrice.setTaxPrice(skuPrice.getTaxPrice());
                skuPrice = bmsContractSkuPrice;
            } else if (StringUtils.isBlank(skuPrice.getSkuCode()) && StringUtils.isBlank(bmsContractSkuPrice.getSkuCode())
                    && StringUtils.isNotBlank(skuPrice.getSkuClass()) && StringUtils.isNotBlank(bmsContractSkuPrice.getSkuClass())
                    && skuPrice.getSkuClass().equals(bmsContractSkuPrice.getSkuClass())) {
                bmsContractSkuPrice.setPrice(skuPrice.getPrice());
                bmsContractSkuPrice.setTaxPrice(skuPrice.getTaxPrice());
                skuPrice = bmsContractSkuPrice;
            } else if (StringUtils.isBlank(skuPrice.getSkuCode()) && StringUtils.isBlank(bmsContractSkuPrice.getSkuCode())
                    && StringUtils.isBlank(skuPrice.getSkuClass()) && StringUtils.isBlank(bmsContractSkuPrice.getSkuClass())) {
                bmsContractSkuPrice.setPrice(skuPrice.getPrice());
                bmsContractSkuPrice.setTaxPrice(skuPrice.getTaxPrice());
                skuPrice = bmsContractSkuPrice;
            }
        }

        ResultMessage message = new ResultMessage();
        message.setSuccess(StringUtils.isBlank(msg));
        message.setMessage(msg.toString());
        message.setData(skuPrice);
        return message;
    }

    /**
     * 下载导入合同商品价格数据模板
     */
    @RequiresPermissions("bms:bmsContractSkuPrice:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel("", BmsContractSkuPriceImport.class, 1).setDataList(Lists.newArrayList()).write(response, "商品价格数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsContractSkuPrice/?repage";
    }

    @RequiresPermissions(value = {"bms:bmsContractSkuPrice:view", "bms:bmsContractSkuPrice:add", "bms:bmsContractSkuPrice:edit", "bms:bmsContract:view", "bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "skuPriceForm")
    public String skuPriceForm(@RequestParam String contractId, Model model) {
        BmsContractEntity entity = bmsContractService.getEntity(contractId);
        model.addAttribute("bmsContractEntity", entity);
        return "modules/bms/basic/bmsContractSkuPriceForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsContractSkuPrice:add", "bms:bmsContractSkuPrice:edit", "bms:bmsContract:add", "bms:bmsContract:edit"}, logical = Logical.OR)
    @RequestMapping(value = "batchSave")
    public AjaxJson batchSave(BmsContractEntity entity) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            for (BmsContractSkuPrice bmsContractSkuPrice : entity.getSkuPriceList()) {
                if (bmsContractSkuPrice.getId() == null) {
                    continue;
                }
                bmsContractSkuPriceService.save(bmsContractSkuPrice);
            }
            j.setSuccess(true);
            j.setMsg("保存合同商品价格成功");
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}