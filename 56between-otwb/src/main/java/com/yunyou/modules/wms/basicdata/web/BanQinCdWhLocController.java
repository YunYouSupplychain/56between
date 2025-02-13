package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLocEntity;
import com.yunyou.modules.wms.basicdata.entity.extend.BanQinCdWhLocExportEntity;
import com.yunyou.modules.wms.basicdata.entity.extend.BanQinPrintLabelCode;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
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
 * 库位Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhLoc")
public class BanQinCdWhLocController extends BaseController {
    @Autowired
    private BanQinCdWhLocService banQinCdWhLocService;

    @ModelAttribute
    public BanQinCdWhLoc get(@RequestParam(required = false) String id) {
        BanQinCdWhLoc entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhLocService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhLoc();
        }
        return entity;
    }

    /**
     * 库位列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhLoc:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhLocList";
    }

    /**
     * 库位列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhLoc:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhLoc banQinCdWhLoc, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhLocService.findPage(new Page<>(request, response), banQinCdWhLoc));
    }

    /**
     * 库位弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhLoc banQinCdWhLoc, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhLocService.findPage(new Page<>(request, response), banQinCdWhLoc));
    }

    /**
     * 查看，增加，编辑库位表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhLoc:view", "basicdata:banQinCdWhLoc:add", "basicdata:banQinCdWhLoc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhLoc banQinCdWhLoc, Model model) {
        model.addAttribute("banQinCdWhLoc", banQinCdWhLoc);
        //如果ID是空为添加
        if (StringUtils.isBlank(banQinCdWhLoc.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdWhLocForm";
    }

    /**
     * 保存库位
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdWhLoc:add", "basicdata:banQinCdWhLoc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhLoc banQinCdWhLoc, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhLoc)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinCdWhLocService.save(banQinCdWhLoc);
            j.setMsg("保存库位成功！");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("库位编码重复！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除库位
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhLoc:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BanQinCdWhLoc banQinCdWhLoc) {
        AjaxJson j = new AjaxJson();
        banQinCdWhLocService.delete(banQinCdWhLoc);
        j.setMsg("删除库位成功");
        return j;
    }

    /**
     * 批量删除库位
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhLoc:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            banQinCdWhLocService.delete(banQinCdWhLocService.get(id));
        }
        j.setMsg("删除库位成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhLoc:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhLoc banQinCdWhLoc, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinCdWhLoc> page = banQinCdWhLocService.findPage(new Page<>(request, response, -1), banQinCdWhLoc);
            new ExportExcel("", BanQinCdWhLocExportEntity.class).setDataList(page.getList()).write(response, "库位.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库位记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdWhLoc:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            banQinCdWhLocService.importFile(new ImportExcel(file, 1, 0).getDataList(BanQinCdWhLoc.class), orgId);
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入库位失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhLoc/?repage";
    }

    /**
     * 下载导入库位数据模板
     */
    @RequiresPermissions("basicdata:banQinCdWhLoc:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BanQinCdWhLoc.class, 2).setDataList(Lists.newArrayList()).write(response, "库位模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhLoc/?repage";
    }

    /**
     * 生成库位页面
     */
    @RequestMapping(value = "createLocForm")
    public String createLocForm(BanQinCdWhLoc banQinCdWhLoc, Model model) {
        model.addAttribute("banQinCdWhLoc", banQinCdWhLoc);
        return "modules/wms/basicdata/banQinCdWhLocCreateForm";
    }

    /**
     * 生成库位
     */
    @ResponseBody
    @RequestMapping(value = "generateLoc")
    @RequiresPermissions("basicdata:banQinCdWhLoc:createLoc")
    public AjaxJson generateLoc(@RequestBody BanQinCdWhLocEntity banQinCdWhLocEntity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinCdWhLocService.locGeneration(banQinCdWhLocEntity.getLocCode(), banQinCdWhLocEntity.getLocList(), banQinCdWhLocEntity.getSumLength());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.put("list", msg.getData());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 保存生成的库位信息
     */
    @ResponseBody
    @RequestMapping(value = "confirm")
    public AjaxJson confirm(@RequestBody List<BanQinCdWhLoc> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinCdWhLocService.confirm(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印标签(条码)
     */
    @RequestMapping(value = "/printBarcode")
    @RequiresPermissions("basicdata:banQinCdWhLoc:printBarcode")
    public String printBarcode(Model model, String locCodes) {
        List<BanQinPrintLabelCode> result = Arrays.stream(locCodes.split(",")).map(BanQinPrintLabelCode::new).collect(Collectors.toList());

        model.addAttribute("url", "classpath:/jasper/locBarcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

    /**
     * 打印标签(二维码)
     */
    @RequestMapping(value = "/printQrcode")
    @RequiresPermissions("basicdata:banQinCdWhLoc:printQrcode")
    public String printQrcode(Model model, String locCodes) {
        List<BanQinPrintLabelCode> result = Arrays.stream(locCodes.split(",")).map(BanQinPrintLabelCode::new).collect(Collectors.toList());

        model.addAttribute("url", "classpath:/jasper/locQrcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}