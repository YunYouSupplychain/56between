package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.basic.entity.TmElectronicFence;
import com.yunyou.modules.tms.basic.service.TmElectronicFenceService;
import com.yunyou.modules.tms.common.map.MapConstants;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * 电子围栏Controller
 */
@Controller
@RequestMapping("${adminPath}/tms/basic/tmElectronicFence")
public class TmElectronicFenceController extends BaseController {
    @Autowired
    private TmElectronicFenceService tmElectronicFenceService;

    @RequiresPermissions("tms:basic:electronicFence:list")
    @RequestMapping(value = {"", "list"})
    public String list(Model model) {
        model.addAttribute("tmElectronicFence", new TmElectronicFence());
        return "modules/tms/basic/tmElectronicFenceList";
    }

    @ResponseBody
    @RequiresPermissions("tms:basic:electronicFence:list")
    @RequestMapping("data")
    public Map<String, Object> data(TmElectronicFence entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmElectronicFenceService.findPage(new Page<>(request, response), entity));
    }

    @RequiresPermissions(value = {"tms:basic:electronicFence:view", "tms:basic:electronicFence:add", "tms:basic:electronicFence:edit"}, logical = Logical.OR)
    @RequestMapping("form")
    public String form(TmElectronicFence entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = tmElectronicFenceService.get(entity.getId());
            entity.setPointList(tmElectronicFenceService.findPoints(entity.getFenceCode(), entity.getOrgId()));
        }
        model.addAttribute("tmElectronicFence", entity);
        model.addAttribute("ak", SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_CLIENT_AK));
        if (MapConstants.B_MAP.equals(MapConstants.USE_MAP)) {
            return "modules/tms/basic/tmElectronicFenceForBMapForm";
        } else {
            return "modules/tms/basic/tmElectronicFenceForGMapForm";
        }
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:basic:electronicFence:add", "tms:basic:electronicFence:edit"}, logical = Logical.OR)
    @RequestMapping("save")
    public AjaxJson save(@Valid TmElectronicFence entity) {
        AjaxJson j = new AjaxJson();
        try {
            tmElectronicFenceService.save(entity);
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isInfoEnabled()) {
                logger.info("", e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:basic:electronicFence:del")
    @RequestMapping("deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                tmElectronicFenceService.delete(tmElectronicFenceService.get(id));
            }
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isInfoEnabled()) {
                logger.info("", e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping("grid")
    public Map<String, Object> grid(TmElectronicFence entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmElectronicFenceService.findGrid(new Page<>(request, response), entity));
    }
}
