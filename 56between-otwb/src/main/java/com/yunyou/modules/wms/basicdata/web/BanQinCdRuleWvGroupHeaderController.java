package com.yunyou.modules.wms.basicdata.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvGroupHeaderService;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
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
 * 波次规则组Controller
 *
 * @author WMJ
 * @version 2020-02-09
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleWvGroupHeader")
public class BanQinCdRuleWvGroupHeaderController extends BaseController {
    @Autowired
    private BanQinCdRuleWvGroupHeaderService banQinCdRuleWvGroupHeaderService;
    @Autowired
    private BanQinOutboundSoService banQinOutboundSoService;

    @ModelAttribute
    public BanQinCdRuleWvGroupHeader get(@RequestParam(required = false) String id) {
        BanQinCdRuleWvGroupHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdRuleWvGroupHeaderService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdRuleWvGroupHeader();
        }
        return entity;
    }

    /**
     * 波次规则组列表页面
     */
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdRuleWvGroupHeaderList";
    }

    /**
     * 波次规则组列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleWvGroupHeader> page = banQinCdRuleWvGroupHeaderService.findPage(new Page<BanQinCdRuleWvGroupHeader>(request, response), banQinCdRuleWvGroupHeader);
        return getBootstrapData(page);
    }

    /**
     * 波次规则组列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleWvGroupHeader> page = banQinCdRuleWvGroupHeaderService.findPage(new Page<BanQinCdRuleWvGroupHeader>(request, response), banQinCdRuleWvGroupHeader);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑波次规则组表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdRuleWvGroupHeader:view", "basicdata:banQinCdRuleWvGroupHeader:add", "basicdata:banQinCdRuleWvGroupHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader, Model model) {
        model.addAttribute("cdRuleWvGroupHeader", banQinCdRuleWvGroupHeader);
        if (StringUtils.isBlank(banQinCdRuleWvGroupHeader.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdRuleWvGroupHeaderForm";
    }

    /**
     * 保存波次规则组
     */
    @RequiresPermissions(value = {"basicdata:banQinCdRuleWvGroupHeader:add", "basicdata:banQinCdRuleWvGroupHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdRuleWvGroupHeader)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        try {
            banQinCdRuleWvGroupHeaderService.save(banQinCdRuleWvGroupHeader);
            j.put("entity", banQinCdRuleWvGroupHeader);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("规则组编码已存在!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除波次规则组
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        try {
            banQinCdRuleWvGroupHeaderService.deleteEntity(idArray);
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
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "波次规则组" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdRuleWvGroupHeader> page = banQinCdRuleWvGroupHeaderService.findPage(new Page<BanQinCdRuleWvGroupHeader>(request, response, -1), banQinCdRuleWvGroupHeader);
            new ExportExcel("波次规则组", BanQinCdRuleWvGroupHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出波次规则组记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdRuleWvGroupHeader> list = ei.getDataList(BanQinCdRuleWvGroupHeader.class);
            for (BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader : list) {
                try {
                    banQinCdRuleWvGroupHeaderService.save(banQinCdRuleWvGroupHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条波次规则组记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条波次规则组记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入波次规则组失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/cdRuleWvGroupHeader/?repage";
    }

    /**
     * 下载导入波次规则组数据模板
     */
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "波次规则组数据导入模板.xlsx";
            List<BanQinCdRuleWvGroupHeader> list = Lists.newArrayList();
            new ExportExcel("波次规则组数据", BanQinCdRuleWvGroupHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/cdRuleWvGroupHeader/?repage";
    }

    /**
     * 生成波次
     *
     * @return
     */
    @RequestMapping(value = "createWave")
    @ResponseBody
    public AjaxJson createWave(String id) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundSoService.createWaveByGroup(id);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

}