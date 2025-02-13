package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmTransportOrderTrackAction;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/order/transport/track")
public class TmTransportOrderTrackController extends BaseController {
    @Autowired
    private TmTransportOrderTrackAction tmTransportOrderTrackAction;

    @ModelAttribute
    public TmTransportOrderTrack get(@RequestParam(required = false) String id) {
        TmTransportOrderTrack entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportOrderTrackAction.get(id);
        }
        if (entity == null) {
            entity = new TmTransportOrderTrack();
        }
        return entity;
    }

    @RequiresPermissions("tms:order:transport:track:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmTransportOrderTrackList";
    }

    @RequiresPermissions(value = {"tms:order:transport:track:view", "tms:order:transport:track:add", "tms:order:transport:track:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmTransportOrderTrack entity, Model model) {
        model.addAttribute("tmTransportOrderTrack", entity);
        return "modules/tms/order/tmTransportOrderTrackForm";
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:track:list")
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> findPage(TmTransportOrderTrack qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderTrackAction.findPage(new Page<TmTransportOrderTrack>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:track:data")
    @RequestMapping(value = "data", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TmTransportOrderTrack> findList(TmTransportOrderTrack qEntity) {
        return tmTransportOrderTrackAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:transport:track:add", "tms:order:transport:track:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public AjaxJson save(TmTransportOrderTrack entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderTrackAction.save(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:transport:track:del")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public AjaxJson delete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderTrackAction.removeAll(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("tms:order:transport:track:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public AjaxJson importFile(@RequestParam(value = "orgId") String orgId, @RequestParam(value = "file") MultipartFile file) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 2, 0);
            List<TmTransportOrderTrack> list = ei.getDataList(TmTransportOrderTrack.class);
            for (TmTransportOrderTrack tmTransportOrderTrack : list) {
                if (StringUtils.isBlank(tmTransportOrderTrack.getOpPerson()) && StringUtils.isBlank(tmTransportOrderTrack.getOperation()) && StringUtils.isBlank(tmTransportOrderTrack.getOpNode())) {
                    continue;
                }
                try {
                    tmTransportOrderTrack.setOrgId(orgId);
                    tmTransportOrderTrackAction.save(tmTransportOrderTrack);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                    ex.printStackTrace();
                }
            }
            if (failureNum > 0) {
                j.setMsg("失败 " + failureNum + " 条路由节点信息");
            }
            j.setSuccess(true);
            j.setMsg(j.getMsg() + "，成功导入 " + successNum + " 条路由节点信息");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入路由节点信息失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入订单跟踪信息登记数据模板
     */
    @RequiresPermissions("tms:order:transport:track:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "物流跟踪信息导入模板.xlsx";
            List<TmTransportOrderTrack> list = Lists.newArrayList();
            new ExportExcel("物流跟踪信息数据", TmTransportOrderTrack.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/order/transport/track/?repage";
    }

    @RequiresPermissions(value = {"tms:order:transport:track:view", "tms:order:transport:track:add", "tms:order:transport:track:edit"}, logical = Logical.OR)
    @RequestMapping(value = "formByTransport")
    public String form(String transportNo, String customerNo, String wayBillNo, String orgId, Model model) {
        TmTransportOrderTrack entity = new TmTransportOrderTrack();
        entity.setTransportNo(transportNo);
        entity.setCustomerNo(customerNo);
        entity.setLabelNo(wayBillNo);
        entity.setOrgId(orgId);
        model.addAttribute("tmTransportOrderTrack", entity);
        return "modules/tms/order/tmTransportOrderTrackForm";
    }
}
