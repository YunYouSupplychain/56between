package com.yunyou.modules.monitor.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.monitor.entity.ScheduleJob;
import com.yunyou.modules.monitor.service.ScheduleJobService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 定时任务Controller
 *
 * @author lgf
 * @version 2017-02-04
 */
@Controller
@RequestMapping(value = "${adminPath}/monitor/scheduleJob")
public class ScheduleJobController extends BaseController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    @ModelAttribute
    public ScheduleJob get(@RequestParam(required = false) String id) {
        ScheduleJob entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = scheduleJobService.get(id);
        }
        if (entity == null) {
            entity = new ScheduleJob();
        }
        return entity;
    }

    /**
     * 定时任务列表页面
     */
    @RequiresPermissions("monitor:scheduleJob:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/monitor/task/scheduleJobList";
    }

    /**
     * 定时任务列表数据
     */
    @ResponseBody
    @RequiresPermissions("monitor:scheduleJob:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(ScheduleJob scheduleJob, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(scheduleJobService.findPage(new Page<>(request, response), scheduleJob));
    }

    /**
     * 查看，增加，编辑定时任务表单页面
     */
    @RequiresPermissions(value = {"monitor:scheduleJob:view", "monitor:scheduleJob:add", "monitor:scheduleJob:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(ScheduleJob scheduleJob, Model model) {
        model.addAttribute("scheduleJob", scheduleJob);
        return "modules/monitor/task/scheduleJobForm";
    }

    /**
     * 保存定时任务
     */
    @ResponseBody
    @RequiresPermissions(value = {"monitor:scheduleJob:add", "monitor:scheduleJob:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(ScheduleJob scheduleJob, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, scheduleJob)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            scheduleJobService.save(scheduleJob);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("关系已存在");
        }
        return j;
    }

    /**
     * 删除定时任务
     */
    @ResponseBody
    @RequiresPermissions("monitor:scheduleJob:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(ScheduleJob scheduleJob) {
        AjaxJson j = new AjaxJson();
        scheduleJobService.delete(scheduleJob);
        j.setMsg("删除定时任务成功");
        return j;
    }

    /**
     * 批量删除定时任务
     */
    @ResponseBody
    @RequiresPermissions("monitor:scheduleJob:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            scheduleJobService.delete(scheduleJobService.get(id));
        }
        j.setMsg("删除定时任务成功");
        return j;
    }


    /**
     * 暂停任务
     */
    @RequiresPermissions("monitor:scheduleJob:stop")
    @RequestMapping(value = "stop")
    @ResponseBody
    public AjaxJson stop(ScheduleJob scheduleJob) {
        AjaxJson j = new AjaxJson();
        scheduleJobService.stopJob(scheduleJob);
        j.setSuccess(true);
        j.setMsg("暂停成功!");
        return j;
    }

    /**
     * 立即运行一次
     */
    @RequiresPermissions("monitor:scheduleJob:startNow")
    @RequestMapping("startNow")
    @ResponseBody
    public AjaxJson startNow(ScheduleJob scheduleJob) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setMsg("运行成功");
        scheduleJobService.startNowJob(scheduleJob);
        return j;
    }

    /**
     * 恢复
     */
    @RequiresPermissions("monitor:scheduleJob:resume")
    @RequestMapping("resume")
    @ResponseBody
    public AjaxJson resume(ScheduleJob scheduleJob) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setMsg("恢复成功");
        scheduleJobService.restartJob(scheduleJob);
        scheduleJobService.startNowJob(scheduleJob);
        return j;
    }

    /**
     * 验证类任务类是否存在
     */
    @RequestMapping("existsClass")
    @ResponseBody
    public boolean existsClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e1) {
            return false;
        }
    }

}