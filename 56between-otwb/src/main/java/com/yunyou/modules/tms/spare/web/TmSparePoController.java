package com.yunyou.modules.tms.spare.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.spare.action.TmSparePoAction;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoScanInfoEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/spare/po")
public class TmSparePoController extends BaseController {
    @Autowired
    private TmSparePoAction tmSparePoAction;

    @ModelAttribute
    public TmSparePoEntity get(String id) {
        TmSparePoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmSparePoAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmSparePoEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:spare:po:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/spare/tmSparePoList";
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:po:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmSparePoEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmSparePoAction.findPage(new Page<>(request, response), qEntity));
    }

    @RequiresPermissions(value = {"tms:spare:po:add", "tms:spare:po:edit", "tms:spare:po:view"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmSparePoEntity entity, Model model) {
        model.addAttribute("tmSparePoEntity", entity);
        return "modules/tms/spare/tmSparePoForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:spare:po:add", "tms:spare:po:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmSparePoEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSparePoAction.saveHeader(entity);
        if (msg.isSuccess()) {
            j.put("entity", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:po:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSparePoAction.remove(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:po:list")
    @RequestMapping(value = "/detail/data")
    public List<TmSparePoDetailEntity> detail(TmSparePoDetailEntity qEntity) {
        return tmSparePoAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:spare:po:detail:add", "tms:spare:po:detail:edit"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson saveDetail(TmSparePoEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSparePoAction.saveDetail(entity.getTmSparePoDetailList());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:po:detail:del")
    @RequestMapping(value = "/detail/deleteAll")
    public AjaxJson deleteDetail(String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSparePoAction.removeDetail(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:po:list")
    @RequestMapping(value = "/scan/data")
    public Map<String, Object> scanInfo(TmSparePoScanInfoEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmSparePoAction.findPage(new Page<>(request, response), qEntity));
    }
}
