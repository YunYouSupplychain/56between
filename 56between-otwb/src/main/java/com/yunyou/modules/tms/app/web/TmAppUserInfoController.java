package com.yunyou.modules.tms.app.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.extend.OfficeEntity;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.app.entity.TmAppUserInfo;
import com.yunyou.modules.tms.app.entity.extend.TmAppUserInfoEntity;
import com.yunyou.modules.tms.app.service.TmAppUserInfoService;
import com.yunyou.modules.tms.basic.entity.extend.TmDriverEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * app用户信息Controller
 *
 * @author zyf
 * @version 2020-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/app/tmAppUserInfo")
public class TmAppUserInfoController extends BaseController {

    @Autowired
    private TmAppUserInfoService tmAppUserInfoService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public TmAppUserInfoEntity get(@RequestParam(required = false) String id) {
        TmAppUserInfoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmAppUserInfoService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmAppUserInfoEntity();
        }
        return entity;
    }

    /**
     * app用户信息列表页面
     */
    @RequiresPermissions("tms:app:tmAppUserInfo:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/app/tmAppUserInfoList";
    }

    /**
     * app用户信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:app:tmAppUserInfo:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmAppUserInfo tmAppUserInfo, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmAppUserInfoService.findPage(new Page<TmAppUserInfoEntity>(request, response), tmAppUserInfo));
    }

    /**
     * 查看，增加，编辑app用户信息表单页面
     */
    @RequiresPermissions(value = {"tms:app:tmAppUserInfo:view", "tms:app:tmAppUserInfo:add", "tms:app:tmAppUserInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmAppUserInfoEntity tmAppUserInfoEntity, Model model) {
        model.addAttribute("tmAppUserInfoEntity", tmAppUserInfoEntity);
        return "modules/tms/app/tmAppUserInfoForm";
    }

    /**
     * 保存app用户信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"tms:app:tmAppUserInfo:add", "tms:app:tmAppUserInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmAppUserInfo tmAppUserInfo, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmAppUserInfo)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmAppUserInfoService.saveValidator(tmAppUserInfo);
            tmAppUserInfoService.save(tmAppUserInfo);
            j.put("entity", tmAppUserInfo);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除app用户信息
     */
    @ResponseBody
    @RequiresPermissions("tms:app:tmAppUserInfo:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmAppUserInfo tmAppUserInfo) {
        AjaxJson j = new AjaxJson();
        try {
            tmAppUserInfoService.delete(tmAppUserInfo);
        } catch (Exception e) {
            logger.error("删除app用户信息id=[" + tmAppUserInfo.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除app用户信息
     */
    @ResponseBody
    @RequiresPermissions("tms:app:tmAppUserInfo:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmAppUserInfo tmAppUserInfo = tmAppUserInfoService.get(id);
            try {
                tmAppUserInfoService.delete(tmAppUserInfo);
            } catch (Exception e) {
                logger.error("删除app用户信息id=[" + id + "]", e);
                errMsg.append("<br>app用户信息[").append(tmAppUserInfo.getTransportObjCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg);
        }
        return j;
    }

    /**
     * 审核/取消审核
     */
    @ResponseBody
    @RequiresPermissions(value = {"tms:app:tmAppUserInfo:audit", "tms:app:tmAppUserInfo:unAudit"}, logical = Logical.OR)
    @RequestMapping(value = "audit")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmAppUserInfo tmAppUserInfo = tmAppUserInfoService.get(id);
            try {
                tmAppUserInfo.setStatus("0".equals(flag) ? "10" : "00");
                tmAppUserInfo.setUpdateBy(UserUtils.getUser());
                tmAppUserInfo.setUpdateDate(new Date());
                tmAppUserInfoService.save(tmAppUserInfo);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("app用户信息[").append(tmAppUserInfo.getLoginName()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("审核/取消审核app用户信息id=[" + id + "]", e);
                errMsg.append("<br>").append("app用户信息[").append(tmAppUserInfo.getLoginName()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg);
        }
        return j;
    }

    /**
     * 业务对象信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "transportObjGrid")
    public Map<String, Object> transportObjGrid(TmTransportObjEntity tmTransportObjEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmAppUserInfoService.findTransportObjGrid(new Page<TmTransportObjEntity>(request, response), tmTransportObjEntity));
    }

    /**
     * 运输人员信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "driverGrid")
    public Map<String, Object> driverGrid(TmDriverEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmAppUserInfoService.findDriverGrid(new Page<TmDriverEntity>(request, response), entity));
    }

    @ResponseBody
    @RequestMapping(value = "office/grid")
    public Map<String, Object> officeGrid(Office entity, HttpServletRequest request, HttpServletResponse response) {
        Page<OfficeEntity> page = new Page<>();

        Page<Office> data = officeService.findCompanyData(new Page<>(request, response), entity);
        List<OfficeEntity> list = data.getList().stream().map(o -> {
            String baseOrgId = UserUtils.getOrgCenter(o.getId()).getId();

            OfficeEntity office = new OfficeEntity();
            BeanUtils.copyProperties(o, office);
            office.setBaseOrgId(StringUtils.isBlank(baseOrgId) ? "*" : baseOrgId);
            return office;
        }).collect(Collectors.toList());
        page.setList(list);
        page.setPageNo(data.getPageNo());
        page.setPageSize(data.getPageSize());
        page.setCount(data.getCount());
        page.setOrderBy(data.getOrderBy());
        page.setMessage(data.getMessage());
        page.setFuncName(data.getFuncName());
        page.setFuncParam(data.getFuncParam());
        return getBootstrapData(page);
    }

}