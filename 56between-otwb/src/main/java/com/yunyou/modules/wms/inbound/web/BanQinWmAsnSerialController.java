package com.yunyou.modules.wms.inbound.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerial;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerialEntity;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnSerialService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 入库序列Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inbound/banQinWmAsnSerial")
public class BanQinWmAsnSerialController extends BaseController {
    @Autowired
    private BanQinWmAsnSerialService banQinWmAsnSerialService;

    /**
     * 入库序列号扫描页面
     */
    @RequiresPermissions("inbound:banQinWmAsnSerial:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inbound/banQinWmAsnSerialList";
    }

    /**
     * 入库序列列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAsnSerialEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmAsnSerialEntity> page = banQinWmAsnSerialService.findPage(new Page<BanQinWmAsnSerialEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 保存入库序列
     */
    @RequiresPermissions(value = "inbound:banQinWmAsnSerial:edit")
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(@RequestBody BanQinWmAsnSerialEntity entity, Model model, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnSerialService.saveEntity(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除入库序列
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnSerial:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnSerialService.removeSerial(ids.split(","));
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
    @RequiresPermissions("inbound:banQinWmAsnSerial:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmAsnSerial banQinWmAsnSerial, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "入库序列" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmAsnSerial> page = banQinWmAsnSerialService.findPage(new Page<BanQinWmAsnSerial>(request, response, -1), banQinWmAsnSerial);
            new ExportExcel("入库序列", BanQinWmAsnSerial.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出入库序列记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnSerial:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public AjaxJson importFile(MultipartFile file, String orgId, String ownerCode, String asnNo, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 2, 0);
            List<BanQinWmAsnSerial> list = ei.getDataList(BanQinWmAsnSerial.class);
            ResultMessage msg = banQinWmAsnSerialService.importSerial(list, orgId, ownerCode, asnNo);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入入库序列数据模板
     */
    @RequiresPermissions("inbound:banQinWmAsnSerial:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "入库序列数据导入模板.xlsx";
            List<BanQinWmAsnSerial> list = Lists.newArrayList();
            new ExportExcel("入库序列数据", BanQinWmAsnSerial.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inbound/banQinWmAsnSerial/?repage";
    }

}