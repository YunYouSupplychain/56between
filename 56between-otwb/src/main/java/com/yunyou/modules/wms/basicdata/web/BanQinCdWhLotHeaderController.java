package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLotHeaderService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * 批次属性Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhLotHeader")
public class BanQinCdWhLotHeaderController extends BaseController {

    @Autowired
    private BanQinCdWhLotHeaderService banQinCdWhLotHeaderService;

    @ModelAttribute
    public BanQinCdWhLotHeader get(@RequestParam(required = false) String id) {
        BanQinCdWhLotHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhLotHeaderService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhLotHeader();
        }
        return entity;
    }

    /**
     * 批次属性列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhLotHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhLotHeaderList";
    }

    /**
     * 批次属性列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhLotHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhLotHeader banQinCdWhLotHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhLotHeader> page = banQinCdWhLotHeaderService.findPage(new Page<BanQinCdWhLotHeader>(request, response), banQinCdWhLotHeader);
        return getBootstrapData(page);
    }

    /**
     * 批次属性列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhLotHeader banQinCdWhLotHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhLotHeader> page = banQinCdWhLotHeaderService.findPage(new Page<BanQinCdWhLotHeader>(request, response), banQinCdWhLotHeader);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑批次属性表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhLotHeader:view", "basicdata:banQinCdWhLotHeader:add", "basicdata:banQinCdWhLotHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhLotHeader banQinCdWhLotHeader, Model model) {
        model.addAttribute("banQinCdWhLotHeader", banQinCdWhLotHeader);
        if (StringUtils.isBlank(banQinCdWhLotHeader.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdWhLotHeaderForm";
    }

    /**
     * 保存批次属性
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhLotHeader:add", "basicdata:banQinCdWhLotHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinCdWhLotHeader banQinCdWhLotHeader, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhLotHeader)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            banQinCdWhLotHeaderService.save(banQinCdWhLotHeader);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("批次属性编码重复！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除批次属性
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhLotHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdWhLotHeaderService.delete(new BanQinCdWhLotHeader(id));
        }
        j.setMsg("删除批次属性成功");
        return j;
    }


}