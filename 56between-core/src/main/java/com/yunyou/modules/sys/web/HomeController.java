package com.yunyou.modules.sys.web;

import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页Controller
 *
 * @author liujianhua
 * @version 2022.8.10
 */
@Controller
@RequestMapping(value = "${adminPath}/home")
public class HomeController extends BaseController {
    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "")
    public String home() {
        return "modules/sys/login/sysHome";
    }

}
