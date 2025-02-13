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
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitEntity;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import com.yunyou.modules.wms.kit.service.BanQinWmKitHeaderService;
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
 * 加工单Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinWmKitHeader")
public class BanQinWmKitHeaderController extends BaseController {
    @Autowired
    private BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    private BanQinKitService banQinKitService;

    @ModelAttribute
    public BanQinWmKitEntity get(@RequestParam(required = false) String id) {
        BanQinWmKitEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmKitHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmKitEntity();
        }
        return entity;
    }

    /**
     * 加工单列表页面
     */
    @RequiresPermissions("kit:banQinWmKitHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/kit/banQinWmKitHeaderList";
    }

    /**
     * 加工单列表数据
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmKitHeader banQinWmKitHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmKitHeader> page = banQinWmKitHeaderService.findPage(new Page<BanQinWmKitHeader>(request, response), banQinWmKitHeader);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑加工单表单页面
     */
    @RequiresPermissions(value = {"kit:banQinWmKitHeader:view", "kit:banQinWmKitHeader:add", "kit:banQinWmKitHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmKitEntity entity, Model model) {
        model.addAttribute("banQinWmKitEntity", entity);
        return "modules/wms/kit/banQinWmKitHeaderForm";
    }

    /**
     * 保存加工单
     */
    @ResponseBody
    @RequiresPermissions(value = {"kit:banQinWmKitHeader:add", "kit:banQinWmKitHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmKitEntity entity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            BanQinWmKitEntity wmKitEntity = banQinWmKitHeaderService.saveEntity(entity);//新建或者编辑保存
            LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
            rsMap.put("entity", wmKitEntity);

            j.setSuccess(true);
            j.setBody(rsMap);
            j.setMsg("保存加工单成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除加工单
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] split = ids.split(",");
        try {
            ResultMessage msg = banQinWmKitHeaderService.removeEntity(split);
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
    @RequiresPermissions("kit:banQinWmKitHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmKitHeader banQinWmKitHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "加工单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmKitHeader> page = banQinWmKitHeaderService.findPage(new Page<BanQinWmKitHeader>(request, response, -1), banQinWmKitHeader);
            new ExportExcel("加工单", BanQinWmKitHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出加工单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("kit:banQinWmKitHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmKitHeader> list = ei.getDataList(BanQinWmKitHeader.class);
            for (BanQinWmKitHeader banQinWmKitHeader : list) {
                try {
                    banQinWmKitHeaderService.save(banQinWmKitHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条加工单记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条加工单记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入加工单失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/kit/banQinWmKitHeader/?repage";
    }

    /**
     * 下载导入加工单数据模板
     */
    @RequiresPermissions("kit:banQinWmKitHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "加工单数据导入模板.xlsx";
            List<BanQinWmKitHeader> list = Lists.newArrayList();
            new ExportExcel("加工单数据", BanQinWmKitHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/kit/banQinWmKitHeader/?repage";
    }

    /**
     * 描述：复制
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:duplicate")
    @RequestMapping(value = "duplicate")
    public AjaxJson duplicate(String id) {
        AjaxJson j = new AjaxJson();

        try {
            banQinKitService.duplicateKitEntity(banQinWmKitHeaderService.getEntity(id));
            j.setMsg("复制成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：审核
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idAddr = ids.split(",");
        for (String id : idAddr) {
            try {
                BanQinWmKitHeader wmKitHeader = banQinWmKitHeaderService.get(id);
                banQinKitService.audit(wmKitHeader.getKitNo(), wmKitHeader.getOrgId());
            } catch (WarehouseException e) {
                j.setSuccess(false);
                j.setMsg(e.getMessage());
            }
        }
        if (StringUtils.isBlank(j.getMsg())) {
            j.setMsg("审核成功");
        }
        return j;
    }

    /**
     * 描述：取消审核
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:cancelAudit")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idAddr = ids.split(",");
        for (String id : idAddr) {
            try {
                BanQinWmKitHeader wmKitHeader = banQinWmKitHeaderService.get(id);
                banQinKitService.cancelAudit(wmKitHeader.getKitNo(), wmKitHeader.getOrgId());
            } catch (WarehouseException e) {
                j.setSuccess(false);
                j.setMsg(e.getMessage());
            }
        }
        if (StringUtils.isBlank(j.getMsg())) {
            j.setMsg("取消审核成功");
        }
        return j;
    }

    /**
     * 描述：关闭订单
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:close")
    @RequestMapping(value = "close")
    public AjaxJson closeOrder(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idAddr = ids.split(",");
        for (String id : idAddr) {
            try {
                BanQinWmKitHeader wmKitHeader = banQinWmKitHeaderService.get(id);
                banQinKitService.close(wmKitHeader.getKitNo(), wmKitHeader.getOrgId());
            } catch (WarehouseException e) {
                j.setSuccess(false);
                j.setMsg(e.getMessage());
            }
        }
        if (StringUtils.isBlank(j.getMsg())) {
            j.setMsg("关闭订单成功");
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:cancel")
    @RequestMapping(value = "cancel")
    public AjaxJson cancelOrder(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idAddr = ids.split(",");
        for (String id : idAddr) {
            try {
                BanQinWmKitHeader wmKitHeader = banQinWmKitHeaderService.get(id);
                banQinKitService.cancelKit(wmKitHeader.getKitNo(), wmKitHeader.getOrgId());
            } catch (WarehouseException e) {
                j.setSuccess(false);
                j.setMsg(e.getMessage());
            }
        }
        if (StringUtils.isBlank(j.getMsg())) {
            j.setMsg("取消订单成功");
        }
        return j;
    }

}