package com.yunyou.modules.sys.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Log;
import com.yunyou.modules.sys.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志Controller
 *
 * @author yunyou
 * @version 2016-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {
    @Autowired
    private LogService logService;

    @RequiresPermissions("sys:log:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/log/logList";
    }

    @ResponseBody
    @RequiresPermissions("sys:log:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Log log, HttpServletRequest request, HttpServletResponse response) {
        return super.getBootstrapData(logService.findPage(new Page<>(request, response), log));
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:log:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            logService.delete(logService.get(id));
        }
        j.setSuccess(true);
        j.setMsg("删除日志成功！");
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:log:del")
    @RequestMapping(value = "empty")
    public AjaxJson empty() {
        AjaxJson j = new AjaxJson();
        logService.empty();
        j.setSuccess(true);
        j.setMsg("清空日志成功!");
        return j;
    }
}
