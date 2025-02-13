package com.yunyou.modules.wms.kit.web;

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
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomHeader;
import com.yunyou.modules.wms.kit.entity.extend.BanQinCdWhBomEntity;
import com.yunyou.modules.wms.kit.service.BanQinCdWhBomHeaderService;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 组合件Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinCdWhBomHeader")
public class BanQinCdWhBomHeaderController extends BaseController {
    @Autowired
    private BanQinCdWhBomHeaderService banQinCdWhBomHeaderService;
    @Autowired
    private BanQinKitService banQinKitService;

    @ModelAttribute
    public BanQinCdWhBomEntity get(@RequestParam(required = false) String id) {
        BanQinCdWhBomEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhBomHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhBomEntity();
        }
        return entity;
    }

    /**
     * 组合件列表页面
     */
    @RequiresPermissions("kit:banQinCdWhBomHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/kit/banQinCdWhBomHeaderList";
    }

    /**
     * 组合件列表数据
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinCdWhBomHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhBomEntity banQinCdWhBomEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhBomEntity> page = banQinCdWhBomHeaderService.findPage(new Page<BanQinCdWhBomEntity>(request, response), banQinCdWhBomEntity);
        return getBootstrapData(page);
    }

    /**
     * 组合件grid数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhBomEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhBomEntity> page = banQinCdWhBomHeaderService.findGrid(new Page<BanQinCdWhBomEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑组合件表单页面
     */
    @RequiresPermissions(value = {"kit:banQinCdWhBomHeader:view", "kit:banQinCdWhBomHeader:add", "kit:banQinCdWhBomHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhBomEntity banQinCdWhBomEntity, Model model) {
        model.addAttribute("banQinCdWhBomEntity", banQinCdWhBomEntity);
        return "modules/wms/kit/banQinCdWhBomHeaderForm";
    }

    /**
     * 保存组合件
     */
    @ResponseBody
    @RequiresPermissions(value = {"kit:banQinCdWhBomHeader:add", "kit:banQinCdWhBomHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhBomEntity banQinCdWhBomEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhBomEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            ResultMessage msg = banQinKitService.saveBomHeaderEntity(banQinCdWhBomEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());

            LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
            rsMap.put("entity", banQinCdWhBomEntity);
            j.setBody(rsMap);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除组合件
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinCdWhBomHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            banQinKitService.removeBomEntity(ids.split(","));
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
    @RequiresPermissions("kit:banQinCdWhBomHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhBomEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "组合件" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdWhBomEntity> page = banQinCdWhBomHeaderService.findPage(new Page<BanQinCdWhBomEntity>(request, response, -1), entity);
            new ExportExcel("组合件", BanQinCdWhBomHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出组合件记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("kit:banQinCdWhBomHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdWhBomHeader> list = ei.getDataList(BanQinCdWhBomHeader.class);
            for (BanQinCdWhBomHeader banQinCdWhBomHeader : list) {
                try {
                    banQinCdWhBomHeaderService.save(banQinCdWhBomHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条组合件记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条组合件记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入组合件失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/kit/banQinCdWhBomHeader/?repage";
    }

    /**
     * 下载导入组合件数据模板
     */
    @RequiresPermissions("kit:banQinCdWhBomHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "组合件数据导入模板.xlsx";
            List<BanQinCdWhBomHeader> list = Lists.newArrayList();
            new ExportExcel("组合件数据", BanQinCdWhBomHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/kit/banQinCdWhBomHeader/?repage";
    }

}