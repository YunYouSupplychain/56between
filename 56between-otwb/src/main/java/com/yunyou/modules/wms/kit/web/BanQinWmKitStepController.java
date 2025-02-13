package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitStep;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitStepEntity;
import com.yunyou.modules.wms.kit.service.BanQinWmKitStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 加工工序Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinWmKitStep")
public class BanQinWmKitStepController extends BaseController {
    @Autowired
    private BanQinWmKitStepService banQinWmKitStepService;

    @ModelAttribute
    public BanQinWmKitStep get(@RequestParam(required = false) String id) {
        BanQinWmKitStep entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmKitStepService.get(id);
        }
        if (entity == null) {
            entity = new BanQinWmKitStep();
        }
        return entity;
    }

    /**
     * 加工工序列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmKitStep banQinWmKitStep, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmKitStep> page = banQinWmKitStepService.findPage(new Page<BanQinWmKitStep>(request, response), banQinWmKitStep);
        return getBootstrapData(page);
    }

    /**
     * 保存加工工序
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody List<BanQinWmKitStepEntity> list, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            banQinWmKitStepService.saveEntity(list);
            j.setSuccess(true);
            j.setMsg("保存成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}