package com.yunyou.modules.sys.common.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysCommonSku;
import com.yunyou.modules.sys.common.entity.SysCommonSkuSupplier;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuImportEntity;
import com.yunyou.modules.sys.common.service.SysCommonSkuBarcodeService;
import com.yunyou.modules.sys.common.service.SysCommonSkuService;
import com.yunyou.modules.sys.common.service.SysCommonSkuSupplierService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/sku")
public class SysCommonSkuController extends BaseController {
    @Autowired
    private SysCommonSkuService sysCommonSkuService;
    @Autowired
    private SysCommonSkuSupplierService sysCommonSkuSupplierService;
    @Autowired
    private SysCommonSkuBarcodeService sysCommonSkuBarcodeService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:sku:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysCommonSku", new SysCommonSku());
        return "modules/sys/common/sysCommonSkuList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:sku:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysCommonSku entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonSkuService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:sku:view", "sys:common:sku:add", "sys:common:sku:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysCommonSku sysCommonSku, Model model) {
        if (StringUtils.isNotBlank(sysCommonSku.getId())) {
            sysCommonSku = sysCommonSkuService.get(sysCommonSku.getId());
        }
        model.addAttribute("sysCommonSku", sysCommonSku);
        return "modules/sys/common/sysCommonSkuForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:sku:add", "sys:common:sku:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysCommonSku sysCommonSku, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysCommonSku)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysCommonSkuService.save(sysCommonSku);
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
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
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:sku:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonSkuService.delete(sysCommonSkuService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:sku:del")
    @RequestMapping(value = "supplier/deleteAll")
    public AjaxJson deleteSupplier(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonSkuSupplierService.remove(new SysCommonSkuSupplier(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("sys:common:sku:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFile(MultipartFile file) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysCommonSkuImportEntity> list = ei.getDataList(SysCommonSkuImportEntity.class);
            if (CollectionUtil.isNotEmpty(list)) {
                ResultMessage msg = sysCommonSkuService.importSku(list);
                j.setSuccess(msg.isSuccess());
                j.setMsg(msg.getMessage());
            } else {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
            }
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
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
     * 下载导入商品数据模板
     */
    @RequiresPermissions("sys:common:sku:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel("", SysCommonSkuImportEntity.class, 2).setDataList(Lists.newArrayList()).write(response, "商品数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/sys/common/sku/?repage";
    }

    @ResponseBody
    @RequiresPermissions("sys:common:sku:del")
    @RequestMapping(value = "barcode/deleteAll")
    public AjaxJson deleteBarcode(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonSkuBarcodeService.remove(sysCommonSkuBarcodeService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:sku:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            SysCommonSku entity = new SysCommonSku();
            entity.setIds(Arrays.asList(ids.split(",")));
            syncPlatformDataCommonAction.syncSku(sysCommonSkuService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:sku:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysCommonSku entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            syncPlatformDataCommonAction.syncSku(sysCommonSkuService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonSku entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonSkuService.findGrid(new Page<>(request, response), entity));
    }
}