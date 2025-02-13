package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdTrackingInfo;
import com.yunyou.modules.wms.basicdata.service.BanQinCdTrackingInfoService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 快递接口信息Controller
 *
 * @author WMJ
 * @version 2020-05-06
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdTrackingInfo")
public class BanQinCdTrackingInfoController extends BaseController {
    @Autowired
    private BanQinCdTrackingInfoService banQinCdTrackingInfoService;

    @ModelAttribute
    public BanQinCdTrackingInfo get(@RequestParam(required = false) String id) {
        BanQinCdTrackingInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdTrackingInfoService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdTrackingInfo();
        }
        return entity;
    }

    /**
     * 快递接口信息列表页面
     */
    @RequiresPermissions("basicdata:banQinCdTrackingInfo:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdTrackingInfoList";
    }

    /**
     * 快递接口信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdTrackingInfo:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdTrackingInfo cdTrackingInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdTrackingInfo> page = banQinCdTrackingInfoService.findPage(new Page<BanQinCdTrackingInfo>(request, response), cdTrackingInfo);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdTrackingInfo cdTrackingInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdTrackingInfo> page = banQinCdTrackingInfoService.findPage(new Page<BanQinCdTrackingInfo>(request, response), cdTrackingInfo);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑快递接口信息表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdTrackingInfo:view", "basicdata:banQinCdTrackingInfo:add", "basicdata:banQinCdTrackingInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdTrackingInfo cdTrackingInfo, Model model) {
        model.addAttribute("banQinCdTrackingInfo", cdTrackingInfo);
        if (StringUtils.isBlank(cdTrackingInfo.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdTrackingInfoForm";
    }

    /**
     * 保存快递接口信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdTrackingInfo:add", "basicdata:banQinCdTrackingInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdTrackingInfo cdTrackingInfo, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, cdTrackingInfo)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            cdTrackingInfo.setParams(StringEscapeUtils.unescapeHtml3(cdTrackingInfo.getParams()));
            banQinCdTrackingInfoService.save(cdTrackingInfo);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除快递接口信息
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdTrackingInfo:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdTrackingInfoService.delete(banQinCdTrackingInfoService.get(id));
        }
        j.setMsg("删除成功！");
        return j;
    }

}