package com.yunyou.modules.bms.basic.web;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsSettleObject;
import com.yunyou.modules.bms.basic.service.BmsSettleObjectService;
import com.yunyou.modules.bms.common.BmsException;
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
 * 结算对象Controller
 *
 * @author zqs
 * @version 2018-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsSettleObject")
public class BmsSettleObjectController extends BaseController {
    @Autowired
    private BmsSettleObjectService bmsSettleObjectService;
    @Autowired
    private BmsContractService bmsContractService;

    @ModelAttribute
    public BmsSettleObject get(@RequestParam(required = false) String id) {
        BmsSettleObject entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsSettleObjectService.get(id);
        }
        if (entity == null) {
            entity = new BmsSettleObject();
        }
        return entity;
    }

    /**
     * 结算对象列表页面
     */
    @RequiresPermissions("bms:settleObject:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsSettleObjectList";
    }

    /**
     * 结算对象列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:settleObject:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsSettleObject bmsSettleObject, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsSettleObjectService.findPage(new Page<>(request, response), bmsSettleObject));
    }

    /**
     * 结算对象列表数据  按照结算对象类型
     */
    @ResponseBody
    @RequestMapping(value = "dataByType")
    public Map<String, Object> dataByType(BmsSettleObject bmsSettleObject, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsSettleObjectService.findPage(new Page<>(request, response), bmsSettleObject));
    }

    /**
     * 查看，增加，编辑结算对象表单页面
     */
    @RequiresPermissions(value = {"bms:settleObject:view", "bms:settleObject:add", "bms:settleObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsSettleObject bmsSettleObject, Model model) {
        model.addAttribute("bmsSettleObject", bmsSettleObject);
        return "modules/bms/basic/bmsSettleObjectForm";
    }

    /**
     * 保存结算对象
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:settleObject:add", "bms:settleObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsSettleObject bmsSettleObject, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsSettleObject)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsSettleObjectService.save(bmsSettleObject);
            j.setSuccess(true);
            j.setMsg("保存结算对象成功");
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除结算对象
     */
    @ResponseBody
    @RequiresPermissions("bms:settleObject:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder msg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BmsSettleObject bmsSettleObject = bmsSettleObjectService.get(id);
            BmsContract qContract = new BmsContract();
            qContract.setSettleObjectCode(bmsSettleObject.getSettleObjectCode());
            qContract.setOrgId(bmsSettleObject.getOrgId());
            List<BmsContract> list = bmsContractService.findList(qContract);
            if (CollectionUtil.isNotEmpty(list)) {
                msg.append(bmsSettleObject.getSettleObjectCode()).append("存在合同，无法删除<br>");
                continue;
            }
            bmsSettleObjectService.delete(bmsSettleObject);
        }
        j.setSuccess(StringUtils.isBlank(msg));
        j.setMsg(msg.toString());
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:settleObject:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BmsSettleObject bmsSettleObject, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsSettleObject> page = bmsSettleObjectService.findPage(new Page<>(request, response, -1), bmsSettleObject);
            new ExportExcel("结算对象", BmsSettleObject.class).setDataList(page.getList()).write(response, "结算对象.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出结算对象记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("bms:settleObject:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsSettleObject> list = ei.getDataList(BmsSettleObject.class);
            for (BmsSettleObject bmsSettleObject : list) {
                try {
                    bmsSettleObjectService.save(bmsSettleObject);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条结算对象记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条结算对象记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入结算对象失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsSettleObject/?repage";
    }

    /**
     * 下载导入结算对象数据模板
     */
    @RequiresPermissions("bms:settleObject:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel("结算对象数据", BmsSettleObject.class, 1).setDataList(Lists.newArrayList()).write(response, "结算对象数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsSettleObject/?repage";
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsSettleObject bmsSettleObject, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsSettleObjectService.findGrid(new Page<>(request, response), bmsSettleObject));
    }

}