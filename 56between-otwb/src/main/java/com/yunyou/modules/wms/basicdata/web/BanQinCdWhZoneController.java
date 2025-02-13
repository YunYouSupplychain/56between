package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhZone;
import com.yunyou.modules.wms.basicdata.entity.extend.BanQinPrintLabelCode;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhZoneService;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库区Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhZone")
public class BanQinCdWhZoneController extends BaseController {
    @Autowired
    private BanQinCdWhZoneService banQinCdWhZoneService;

    @ModelAttribute
    public BanQinCdWhZone get(@RequestParam(required = false) String id) {
        BanQinCdWhZone entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhZoneService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhZone();
        }
        return entity;
    }

    /**
     * 库区列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhZone:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhZoneList";
    }

    /**
     * 库区列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhZone:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhZone banQinCdWhZone, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhZoneService.findPage(new Page<>(request, response), banQinCdWhZone));
    }

    /**
     * 库区弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhZone banQinCdWhZone, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhZoneService.findPage(new Page<>(request, response), banQinCdWhZone));
    }

    /**
     * 查看，增加，编辑库区表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhZone:view", "basicdata:banQinCdWhZone:add", "basicdata:banQinCdWhZone:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhZone banQinCdWhZone, Model model) {
        model.addAttribute("banQinCdWhZone", banQinCdWhZone);
        // 如果ID是空为添加
        if (StringUtils.isBlank(banQinCdWhZone.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdWhZoneForm";
    }

    /**
     * 保存库区
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdWhZone:add", "basicdata:banQinCdWhZone:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhZone banQinCdWhZone, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhZone)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinCdWhZoneService.save(banQinCdWhZone);
            j.setMsg("保存库区成功！");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("库区编码已存在！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除库区
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhZone:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BanQinCdWhZone banQinCdWhZone) {
        AjaxJson j = new AjaxJson();
        banQinCdWhZoneService.delete(banQinCdWhZone);
        j.setMsg("删除库区成功");
        return j;
    }

    /**
     * 批量删除库区
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhZone:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            banQinCdWhZoneService.delete(banQinCdWhZoneService.get(id));
        }
        j.setMsg("删除库区成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhZone:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BanQinCdWhZone banQinCdWhZone, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinCdWhZone> page = banQinCdWhZoneService.findPage(new Page<>(request, response, -1), banQinCdWhZone);
            new ExportExcel("库区", BanQinCdWhZone.class).setDataList(page.getList()).write(response, "库区.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库区记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdWhZone:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            banQinCdWhZoneService.importFile(new ImportExcel(file, 1, 0).getDataList(BanQinCdWhZone.class), orgId);
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入库区失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhZone/?repage";
    }

    /**
     * 下载导入库区数据模板
     */
    @RequiresPermissions("basicdata:banQinCdWhZone:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BanQinCdWhZone.class, 2).setDataList(Lists.newArrayList()).write(response, "库区导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhZone/?repage";
    }

    /**
     * 打印标签(条码)
     */
    @RequestMapping(value = "/printBarcode")
    @RequiresPermissions("basicdata:banQinCdWhZone:printBarcode")
    public String printBarcode(Model model, String zoneCodes) {
        List<BanQinPrintLabelCode> result = Arrays.stream(zoneCodes.split(",")).map(BanQinPrintLabelCode::new).collect(Collectors.toList());

        model.addAttribute("url", "classpath:/jasper/zoneBarcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

    /**
     * 打印标签(二维码)
     */
    @RequestMapping(value = "/printQrcode")
    @RequiresPermissions("basicdata:banQinCdWhZone:printBarcode")
    public String printQrcode(Model model, String zoneCodes) {
        List<BanQinPrintLabelCode> result = Arrays.stream(zoneCodes.split(",")).map(BanQinPrintLabelCode::new).collect(Collectors.toList());

        model.addAttribute("url", "classpath:/jasper/zoneQrcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}