package com.yunyou.modules.wms.weigh.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfo;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfoEntity;
import com.yunyou.modules.wms.weigh.service.BanQinWeighMachineInfoService;
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
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 称重设备表Controller
 *
 * @author zyf
 * @version 2019-09-21
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/weigh/banQinWeighMachineInfo")
public class BanQinWeighMachineInfoController extends BaseController {

    @Autowired
    private BanQinWeighMachineInfoService banQinWeighMachineInfoService;

    @ModelAttribute
    public BanQinWeighMachineInfoEntity get(@RequestParam(required = false) String id) {
        BanQinWeighMachineInfoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWeighMachineInfoService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWeighMachineInfoEntity();
        }
        return entity;
    }

    /**
     * 称重设备表列表页面
     */
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/weigh/banQinWeighMachineInfoList";
    }

    /**
     * 称重设备表列表数据
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWeighMachineInfoEntity banQinWeighMachineInfoEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWeighMachineInfoEntity> page = banQinWeighMachineInfoService.findPage(new Page<BanQinWeighMachineInfoEntity>(request, response), banQinWeighMachineInfoEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑称重设备表表单页面
     */
    @RequiresPermissions(value = {"wms:weigh:banQinWeighMachineInfo:view", "wms:weigh:banQinWeighMachineInfo:add", "wms:weigh:banQinWeighMachineInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWeighMachineInfoEntity banQinWeighMachineInfoEntity, Model model) {
        model.addAttribute("banQinWeighMachineInfoEntity", banQinWeighMachineInfoEntity);
        return "modules/wms/weigh/banQinWeighMachineInfoForm";
    }

    /**
     * 保存称重设备表
     */
    @ResponseBody
    @RequiresPermissions(value = {"wms:weigh:banQinWeighMachineInfo:add", "wms:weigh:banQinWeighMachineInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWeighMachineInfoEntity banQinWeighMachineInfoEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWeighMachineInfoEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        banQinWeighMachineInfoService.save(banQinWeighMachineInfoEntity);//新建或者编辑保存
        j.setSuccess(true);
        j.setMsg("保存称重设备表成功");
        return j;
    }

    /**
     * 删除称重设备表
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BanQinWeighMachineInfoEntity banQinWeighMachineInfoEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        banQinWeighMachineInfoService.delete(banQinWeighMachineInfoEntity);
        j.setMsg("删除称重设备表成功");
        return j;
    }

    /**
     * 批量删除称重设备表
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinWeighMachineInfoService.delete(banQinWeighMachineInfoService.get(id));
        }
        j.setMsg("删除称重设备表成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWeighMachineInfo banQinWeighMachineInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "称重设备表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWeighMachineInfo> page = banQinWeighMachineInfoService.findPage(new Page<BanQinWeighMachineInfo>(request, response, -1), banQinWeighMachineInfo);
            new ExportExcel("称重设备表", BanQinWeighMachineInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出称重设备表记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWeighMachineInfo> list = ei.getDataList(BanQinWeighMachineInfo.class);
            for (BanQinWeighMachineInfo banQinWeighMachineInfo : list) {
                try {
                    banQinWeighMachineInfoService.save(banQinWeighMachineInfo);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条称重设备表记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条称重设备表记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入称重设备表失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/weigh/banQinWeighMachineInfo/?repage";
    }

    /**
     * 下载导入称重设备表数据模板
     */
    @RequiresPermissions("wms:weigh:banQinWeighMachineInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "称重设备表数据导入模板.xlsx";
            List<BanQinWeighMachineInfo> list = Lists.newArrayList();
            new ExportExcel("称重设备表数据", BanQinWeighMachineInfo.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/weigh/banQinWeighMachineInfo/?repage";
    }

}