package com.yunyou.modules.oms.order.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionEntity;
import com.yunyou.modules.oms.order.manager.OmRequisitionManager;
import com.yunyou.modules.oms.order.manager.OmTaskGenManager;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/oms/order/omRequisition")
public class OmRequisitionController extends BaseController {
    @Autowired
    private OmRequisitionManager omRequisitionManager;
    @Autowired
    private OmTaskGenManager omTaskGenManager;

    @ModelAttribute
    public OmRequisitionEntity get(@RequestParam(required = false) String id) {
        OmRequisitionEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omRequisitionManager.getEntity(id);
        }
        if (entity == null) {
            entity = new OmRequisitionEntity();
        }
        return entity;
    }

    @RequiresPermissions("oms:order:requisition:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/order/omRequisitionList";
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmRequisitionEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omRequisitionManager.findPage(new Page<OmRequisitionEntity>(request, response), qEntity));
    }

    @RequiresPermissions(value = {"oms:order:requisition:view", "oms:order:requisition:add", "oms:order:requisition:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmRequisitionEntity entity, Model model) {
        model.addAttribute("omRequisitionEntity", entity);
        return "modules/oms/order/omRequisitionForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"oms:order:requisition:add", "oms:order:requisition:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmRequisitionEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            OmRequisitionEntity omRequisitionEntity = omRequisitionManager.save(entity);
            j.put("entity", omRequisitionEntity);
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("订单已存在");
        } catch (RuntimeException e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg("操作异常");
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                omRequisitionManager.remove(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions(value = {"oms:order:requisition:detail:add", "oms:order:requisition:detail:edit"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson saveDetail(OmRequisitionEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            OmRequisitionEntity omRequisitionEntity = omRequisitionManager.saveDetail(entity.getId(), entity.getOmRequisitionDetailList());
            j.put("entity", omRequisitionEntity);
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("行号已存在");
        } catch (RuntimeException e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg("操作异常");
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:detail:del")
    @RequestMapping(value = "/detail/deleteAll")
    public AjaxJson removeDetail(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }
        try {
            omRequisitionManager.removeDetail(ids.split(","));
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (RuntimeException e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg("操作异常");
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:task:gen")
    @RequestMapping(value = "/detail/createTask", method = RequestMethod.POST)
    public AjaxJson createTask(@RequestBody OmRequisitionEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            omTaskGenManager.genTask(entity);
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }  catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg("操作异常");
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:audit")
    @RequestMapping(value = "audit")
    public AjaxJson audit(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                omRequisitionManager.audit(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:audit:cancel")
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                omRequisitionManager.cancelAudit(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:task:gen")
    @RequestMapping(value = "createTask", method = RequestMethod.POST)
    public AjaxJson genTask(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                omTaskGenManager.genTask(omRequisitionManager.getEntity(id));
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br/>");
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br/>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:close")
    @RequestMapping("close")
    public AjaxJson close(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                omRequisitionManager.close(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("oms:order:requisition:cancel")
    @RequestMapping("cancel")
    public AjaxJson cancel(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                omRequisitionManager.cancel(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }
}