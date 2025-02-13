package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomStep;
import com.yunyou.modules.wms.kit.service.BanQinCdWhBomStepService;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 组合件加工工序Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinCdWhBomStep")
public class BanQinCdWhBomStepController extends BaseController {
    @Autowired
    private BanQinCdWhBomStepService banQinCdWhBomStepService;
    @Autowired
    private BanQinKitService banQinKitService;

    /**
     * wms列表数据
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinCdWhBomHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhBomStep banQinCdWhBomStep, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhBomStep> page = banQinCdWhBomStepService.findPage(new Page<BanQinCdWhBomStep>(request, response), banQinCdWhBomStep);
        return getBootstrapData(page);
    }

    /**
     * 保存wms
     */
    @ResponseBody
    @RequiresPermissions(value = {"kit:banQinCdWhBomHeader:add", "kit:banQinCdWhBomHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhBomStep banQinCdWhBomStep, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhBomStep)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            ResultMessage msg = banQinKitService.saveBomStep(banQinCdWhBomStep);
            j.setSuccess(true);
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除wms
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinCdWhBomHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.removeBomStep(ids.split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}