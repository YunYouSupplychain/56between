package com.yunyou.modules.tms.order.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.order.action.TmLabelLogAction;
import com.yunyou.modules.tms.order.entity.extend.TmLabelLogEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 标签履历Controller
 *
 * @author 刘剑华
 * @since 2023/11/16 14:05
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmLabelLog")
public class TmLabelLogController extends BaseController {
    @Autowired
    private TmLabelLogAction action;

    /**
     * 标签履历列表页面
     */
    @RequiresPermissions("tms:order:tmLabelLog:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("entity", new TmLabelLogEntity());
        return "modules/tms/order/tmLabelLogList";
    }

    /**
     * 标签履历列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmLabelLog:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmLabelLogEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(action.findPage(new Page<>(request, response), entity));
    }

}
