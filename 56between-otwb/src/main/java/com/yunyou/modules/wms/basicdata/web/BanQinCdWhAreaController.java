package com.yunyou.modules.wms.basicdata.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhArea;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhAreaService;
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
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 区域Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhArea")
public class BanQinCdWhAreaController extends BaseController {

    @Autowired
    private BanQinCdWhAreaService banQinCdWhAreaService;

    @ModelAttribute
    public BanQinCdWhArea get(@RequestParam(required = false) String id) {
        BanQinCdWhArea entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhAreaService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhArea();
        }
        return entity;
    }

    /**
     * 区域列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhArea:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhAreaList";
    }

    /**
     * 区域列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhArea:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhArea banQinCdWhArea, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhArea> page = banQinCdWhAreaService.findPage(new Page<BanQinCdWhArea>(request, response), banQinCdWhArea);
        return getBootstrapData(page);
    }

    /**
     * 区域弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhArea banQinCdWhArea, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhArea> page = banQinCdWhAreaService.findPage(new Page<BanQinCdWhArea>(request, response), banQinCdWhArea);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑区域表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhArea:view", "basicdata:banQinCdWhArea:add", "basicdata:banQinCdWhArea:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhArea banQinCdWhArea, Model model) {
        model.addAttribute("banQinCdWhArea", banQinCdWhArea);
        if (StringUtils.isBlank(banQinCdWhArea.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdWhAreaForm";
    }

    /**
     * 保存区域
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdWhArea:add", "basicdata:banQinCdWhArea:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhArea banQinCdWhArea, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhArea)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinCdWhAreaService.save(banQinCdWhArea);
            j.setMsg("保存成功！");
        } catch (DuplicateKeyException e)  {
            j.setSuccess(false);
            j.setMsg("编码已存在！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        
        return j;
    }

    /**
     * 删除区域
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhArea:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BanQinCdWhArea banQinCdWhArea, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        banQinCdWhAreaService.delete(banQinCdWhArea);
        j.setMsg("删除区域成功");
        return j;
    }

    /**
     * 批量删除区域
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhArea:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdWhAreaService.delete(banQinCdWhAreaService.get(id));
        }
        j.setMsg("删除区域成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhArea:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhArea banQinCdWhArea, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "区域" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdWhArea> page = banQinCdWhAreaService.findPage(new Page<BanQinCdWhArea>(request, response, -1), banQinCdWhArea);
            new ExportExcel("区域", BanQinCdWhArea.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出区域记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdWhArea:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdWhArea> list = ei.getDataList(BanQinCdWhArea.class);
            for (BanQinCdWhArea banQinCdWhArea : list) {
                try {
                    banQinCdWhAreaService.save(banQinCdWhArea);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条区域记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条区域记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入区域失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhArea/?repage";
    }

    /**
     * 下载导入区域数据模板
     */
    @RequiresPermissions("basicdata:banQinCdWhArea:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "区域数据导入模板.xlsx";
            List<BanQinCdWhArea> list = Lists.newArrayList();
            new ExportExcel("区域数据", BanQinCdWhArea.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhArea/?repage";
    }

}