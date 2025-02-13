package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import com.yunyou.modules.wms.kit.service.BanQinWmTaskKitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * 加工任务Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-21
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinWmTaskKit")
public class BanQinWmTaskKitController extends BaseController {
    @Autowired
    private BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    private BanQinKitService banQinKitService;

    /**
     * 加工任务列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmTaskKitEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmTaskKitEntity> page = banQinWmTaskKitService.findPage(new Page<BanQinWmTaskKitEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 手工分配保存
     */
    @ResponseBody
    @RequiresPermissions(value = "kit:banQinWmTaskKit:manualAlloc")
    @RequestMapping(value = "manualAlloc")
    public AjaxJson manualAlloc(BanQinWmTaskKitEntity banQinWmTaskKitEntity, RedirectAttributes redirectAttributes)  {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.manualAlloc(banQinWmTaskKitEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拣货确认
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmTaskKit:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(@RequestBody List<BanQinWmTaskKitEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.pickingByTask(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消分配
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmTaskKit:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(@RequestBody List<BanQinWmTaskKitEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.cancelAllocByTask(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消拣货
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmTaskKit:cancelPick")
    @RequestMapping(value = "cancelPick")
    public AjaxJson cancelPick(@RequestBody List<BanQinWmTaskKitEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.cancelPickingByTask(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}