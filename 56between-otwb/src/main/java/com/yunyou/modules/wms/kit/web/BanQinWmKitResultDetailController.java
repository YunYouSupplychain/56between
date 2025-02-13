package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import com.yunyou.modules.wms.kit.service.BanQinWmKitResultDetailService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelAllocEntity;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 加工单结果明细Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinWmKitResultDetail")
public class BanQinWmKitResultDetailController extends BaseController {
    @Autowired
    private BanQinWmKitResultDetailService banQinWmKitResultDetailService;
    @Autowired
    private BanQinKitService banQinKitService;

    /**
     * 加工单结果明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmKitResultDetailEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmKitResultDetailEntity> page = banQinWmKitResultDetailService.findPage(new Page<BanQinWmKitResultDetailEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 加工确认
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitResultDetail:kitConfirm")
    @RequestMapping(value = "kitConfirm")
    public AjaxJson kitConfirm(@RequestBody List<BanQinWmKitResultDetailEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.kitConfirm(list, null, WmsCodeMaster.SUB_KIT_RT.getCode());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消加工
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitResultDetail:cancelKit")
    @RequestMapping(value = "cancelKit")
    public AjaxJson cancelKit(@RequestBody List<BanQinWmKitResultDetailEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.kitCancelConfirm(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成上架任务
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitResultDetail:createTask")
    @RequestMapping(value = "createTask")
    public AjaxJson createTask(@RequestBody List<BanQinWmKitResultDetailEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.createTaskPa(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除上架任务
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitResultDetail:removePaTask")
    @RequestMapping(value = "removePaTask")
    public AjaxJson removePaTask(@RequestBody List<BanQinWmTaskPaEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.kitBatchRemoveTaskPa(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 上架确认
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitResultDetail:paConfirm")
    @RequestMapping(value = "paConfirm")
    public AjaxJson paConfirm(@RequestBody List<BanQinWmTaskPaEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.kitBatchPutaway(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 重新生成上架任务
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitResultDetail:reCreateTask")
    @RequestMapping(value = "reCreateTask")
    public AjaxJson reCreateTask(@RequestBody List<BanQinWmDelAllocEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.kitBatchCreateTaskPaByDelAlloc(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}