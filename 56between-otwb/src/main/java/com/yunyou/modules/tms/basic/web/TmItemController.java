package com.yunyou.modules.tms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmItem;
import com.yunyou.modules.tms.basic.entity.extend.TmItemEntity;
import com.yunyou.modules.tms.basic.service.TmItemService;
import com.yunyou.modules.tms.common.TmsException;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmItem")
public class TmItemController extends BaseController {

    @Autowired
    private TmItemService tmItemService;

    @ModelAttribute
    public TmItemEntity get(@RequestParam(required = false) String id) {
        TmItemEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmItemService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmItemEntity();
        }
        return entity;
    }

    /**
     * 商品信息列表页面
     */
    @RequiresPermissions("basic:tmItem:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmItemList";
    }

    /**
     * 商品信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItem:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmItem tmItem, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmItemEntity> page = tmItemService.findPage(new Page<TmItemEntity>(request, response), tmItem);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑商品信息表单页面
     */
    @RequiresPermissions(value = {"basic:tmItem:view", "basic:tmItem:add", "basic:tmItem:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmItemEntity tmItemEntity, Model model) {
        model.addAttribute("tmItemEntity", tmItemEntity);
        return "modules/tms/basic/tmItemForm";
    }

    /**
     * 保存商品信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmItem:add", "basic:tmItem:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmItem tmItem, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmItem)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmItemService.saveValidator(tmItem);
            tmItemService.save(tmItem);
            j.put("entity", tmItem);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除商品信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItem:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmItem tmItem, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            tmItemService.delete(tmItem);
        } catch (Exception e) {
            logger.error("删除商品id=[" + tmItem.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除商品信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItem:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmItem tmItem = tmItemService.get(id);
            try {
                tmItemService.delete(tmItem);
            } catch (Exception e) {
                logger.error("删除商品id=[" + id + "]", e);
                errMsg.append("<br>").append("商品[").append(tmItem.getSkuCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItem:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmItem tmItem, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "商品信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmItemEntity> page = tmItemService.findPage(new Page<TmItemEntity>(request, response, -1), tmItem);
            new ExportExcel("商品信息", TmItem.class).setDataList(page.getList()).write(response, fileName).dispose();
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
    @RequiresPermissions("basic:tmItem:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmItem> list = ei.getDataList(TmItem.class);
            for (TmItem tmItem : list) {
                try {
                    tmItemService.save(tmItem);
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
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmItem/?repage";
    }

    /**
     * 下载导入商品信息数据模板
     */
    @RequiresPermissions("basic:tmItem:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "商品信息数据导入模板.xlsx";
            List<TmItem> list = Lists.newArrayList();
            new ExportExcel("商品信息数据", TmItem.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmItem/?repage";
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmItem:enable", "basic:tmItem:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmItem tmItem = tmItemService.get(id);
            try {
                tmItem.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmItem.setUpdateBy(UserUtils.getUser());
                tmItem.setUpdateDate(new Date());
                tmItemService.save(tmItem);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("商品[").append(tmItem.getSkuCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用商品id=[" + id + "]", e);
                errMsg.append("<br>").append("商品[").append(tmItem.getSkuCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 商品信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmItemEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmItemEntity> page = tmItemService.findGrid(new Page<TmItemEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}