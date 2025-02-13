package com.yunyou.modules.oms.basic.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmItem;
import com.yunyou.modules.oms.basic.entity.extend.OmItemEntity;
import com.yunyou.modules.oms.basic.service.OmItemService;
import com.yunyou.modules.oms.common.OmsException;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * 商品信息Controller
 *
 * @author WMJ
 * @version 2019-04-15
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omItem")
public class OmItemController extends BaseController {
    @Autowired
    private OmItemService omItemService;

    @ModelAttribute
    public OmItemEntity get(@RequestParam(required = false) String id) {
        OmItemEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omItemService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmItemEntity();
        }
        return entity;
    }

    /**
     * 商品信息列表页面
     */
    @RequiresPermissions("basic:omItem:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omItemList";
    }

    /**
     * 商品信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omItem:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmItemEntity omItem, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omItemService.findPage(new Page<OmItemEntity>(request, response), omItem));
    }

    /**
     * 查看，增加，编辑商品信息表单页面
     */
    @RequiresPermissions(value = {"basic:omItem:view", "basic:omItem:add", "basic:omItem:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmItemEntity omItemEntity, Model model) {
        model.addAttribute("omItemEntity", omItemEntity);
        return "modules/oms/basic/omItemForm";
    }

    /**
     * 保存商品信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omItem:add", "basic:omItem:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmItem omItem, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omItem)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omItemService.save(omItem);
            j.setSuccess(true);
            j.setMsg("保存商品信息成功");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("商品已存在");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除商品信息
     */
    @ResponseBody
    @RequiresPermissions("basic:omItem:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            omItemService.delete(omItemService.get(id));
        }
        j.setMsg("删除商品信息成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omItem:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmItemEntity omItem, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "商品信息.xlsx";
            Page<OmItemEntity> page = omItemService.findPage(new Page<OmItemEntity>(request, response, -1), omItem);
            new ExportExcel(null, OmItem.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出商品信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:omItem:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmItem> list = ei.getDataList(OmItem.class);
            for (OmItem omItem : list) {
                try {
                    omItemService.save(omItem);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条商品信息记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条商品信息记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入商品信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omItem/?repage";
    }

    /**
     * 下载导入商品信息数据模板
     */
    @RequiresPermissions("basic:omItem:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "商品信息数据导入模板.xlsx";
            List<OmItem> list = Lists.newArrayList();
            new ExportExcel("商品信息数据", OmItem.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omItem/?repage";
    }

    /**
     * 商品信息(弹出框)
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(OmItemEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omItemService.findGrid(new Page<>(request, response), entity));
    }

    /**
     * 订单商品信息(弹出框)
     */
    @ResponseBody
    @RequestMapping(value = "orderSkuGrid")
    public Map<String, Object> orderSkuGrid(OmItemEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omItemService.findSkuGrid(new Page<>(request, response), entity));
    }

}