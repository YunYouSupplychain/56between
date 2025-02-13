package com.yunyou.modules.wms.basicdata.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuEntity;
import com.yunyou.modules.wms.basicdata.entity.extend.BanQinCdWhSkuExportEntity;
import com.yunyou.modules.wms.basicdata.entity.extend.BanQinCdWhSkuImportEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
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
 * 商品Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhSku")
public class BanQinCdWhSkuController extends BaseController {
    @Autowired
    private BanQinCdWhSkuService banQinCdWhSkuService;

    @ModelAttribute
    public BanQinCdWhSkuEntity get(@RequestParam(required = false) String id) {
        BanQinCdWhSkuEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhSkuService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhSkuEntity();
        }
        return entity;
    }

    /**
     * 商品列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhSku:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhSkuList";
    }

    /**
     * 商品列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSku:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhSkuEntity banQinCdWhSkuEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhSkuService.findPage(new Page<BanQinCdWhSkuEntity>(request, response), banQinCdWhSkuEntity));
    }

    /**
     * 商品弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhSkuEntity banQinCdWhSkuEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhSkuService.findPage(new Page<BanQinCdWhSkuEntity>(request, response), banQinCdWhSkuEntity));
    }

    /**
     * 查看，增加，编辑商品表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhSku:view", "basicdata:banQinCdWhSku:add", "basicdata:banQinCdWhSku:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhSkuEntity banQinCdWhSkuEntity, Model model) {
        model.addAttribute("banQinCdWhSkuEntity", banQinCdWhSkuEntity);
        return "modules/wms/basicdata/banQinCdWhSkuForm";
    }

    /**
     * 保存商品
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdWhSku:add", "basicdata:banQinCdWhSku:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhSkuEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinCdWhSkuService.saveEntity(entity);
            j.put("entity", entity);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("商品编码重复！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除商品
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSku:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinCdWhSkuService.removeSkuEntity(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSku:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhSkuEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinCdWhSkuEntity> page = banQinCdWhSkuService.findPage(new Page<BanQinCdWhSkuEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinCdWhSkuExportEntity.class).setDataList(page.getList()).write(response, "商品.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出商品记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdWhSku:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFile(MultipartFile file, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdWhSkuImportEntity> list = ei.getDataList(BanQinCdWhSkuImportEntity.class);
            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(v -> v.setOrgId(orgId));
                ResultMessage msg = banQinCdWhSkuService.importSku(list);
                j.setSuccess(msg.isSuccess());
                j.setMsg(msg.getMessage());
            } else {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
            }
        } catch (Exception e) {
            logger.error("商品导入异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入商品数据模板
     */
    @RequiresPermissions("basicdata:banQinCdWhSku:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "商品数据导入模板.xlsx";
            List<BanQinCdWhSkuImportEntity> list = Lists.newArrayList();
            new ExportExcel("", BanQinCdWhSkuImportEntity.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhSku/?repage";
    }

}