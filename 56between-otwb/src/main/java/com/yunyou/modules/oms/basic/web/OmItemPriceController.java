package com.yunyou.modules.oms.basic.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmItemPrice;
import com.yunyou.modules.oms.basic.entity.OmItemPriceEntity;
import com.yunyou.modules.oms.basic.service.OmItemPriceService;
import com.yunyou.modules.oms.common.OmsException;
import com.google.common.collect.Lists;
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
 * 商品价格Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-20
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omItemPrice")
public class OmItemPriceController extends BaseController {
    @Autowired
    private OmItemPriceService omItemPriceService;

    @ModelAttribute
    public OmItemPriceEntity get(@RequestParam(required = false) String id) {
        OmItemPriceEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omItemPriceService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmItemPriceEntity();
        }
        return entity;
    }

    /**
     * 商品价格列表页面
     */
    @RequiresPermissions("basic:omItemPrice:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omItemPriceList";
    }

    /**
     * 商品价格列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omItemPrice:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmItemPriceEntity omItemPriceEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omItemPriceService.findPage(new Page<OmItemPriceEntity>(request, response), omItemPriceEntity));
    }

    /**
     * 查看，增加，编辑商品价格表单页面
     */
    @RequiresPermissions(value = {"basic:omItemPrice:view", "basic:omItemPrice:add", "basic:omItemPrice:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmItemPriceEntity omItemPriceEntity, Model model) {
        model.addAttribute("omItemPrice", omItemPriceEntity);
        return "modules/oms/basic/omItemPriceForm";
    }

    /**
     * 保存商品价格
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omItemPrice:add", "basic:omItemPrice:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmItemPrice omItemPrice, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omItemPrice)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omItemPriceService.beforeSaveCheck(omItemPrice);
            j.setSuccess(true);
            j.setMsg("保存商品价格成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除商品价格
     */
    @ResponseBody
    @RequiresPermissions("basic:omItemPrice:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            omItemPriceService.delete(omItemPriceService.get(id));
        }
        j.setMsg("删除商品价格成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omItemPrice:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmItemPrice omItemPrice, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "商品价格.xlsx";
            Page<OmItemPrice> page = omItemPriceService.findPage(new Page<>(request, response, -1), omItemPrice);
            new ExportExcel("商品价格", OmItemPrice.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出商品价格记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:omItemPrice:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmItemPrice> list = ei.getDataList(OmItemPrice.class);
            for (OmItemPrice omItemPrice : list) {
                try {
                    omItemPriceService.beforeSaveCheck(omItemPrice);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条商品价格记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条商品价格记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入商品价格失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omItemPrice/?repage";
    }

    /**
     * 下载导入商品价格数据模板
     */
    @RequiresPermissions("basic:omItemPrice:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "商品价格导入模板.xlsx";
            List<OmItemPrice> list = Lists.newArrayList();
            new ExportExcel(null, OmItemPrice.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omItemPrice/?repage";
    }

    /**
     * 商品价格列表数据（弹出框）
     */
    @ResponseBody
    @RequestMapping(value = "popData")
    public Map<String, Object> popData(OmItemPriceEntity omItemPriceEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omItemPriceService.popData(new Page<OmItemPriceEntity>(request, response), omItemPriceEntity));
    }

    /**
     * 描述：审核
     * <p>
     * create by Jianhua on 2019/7/29
     */
    @ResponseBody
    @RequiresPermissions("basic:omItemPrice:audit")
    @RequestMapping("audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();

        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                omItemPriceService.audit(id);
            }
        } catch (OmsException o) {
            j.setSuccess(false);
            j.setMsg(o.getMessage());
        }
        return j;
    }

    /**
     * 描述：取消审核
     * <p>
     * create by Jianhua on 2019/7/29
     */
    @ResponseBody
    @RequiresPermissions("basic:omItemPrice:cancelAudit")
    @RequestMapping("cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();

        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                omItemPriceService.cancelAudit(id);
            }
        } catch (OmsException o) {
            j.setSuccess(false);
            j.setMsg(o.getMessage());
        }
        return j;
    }

}