package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysOmsPackage;
import com.yunyou.modules.sys.common.service.SysOmsPackageService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 包装Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/oms/package")
public class SysOmsPackageController extends BaseController {
    @Autowired
    private SysOmsPackageService sysOmsPackageService;

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysOmsPackage entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysOmsPackageService.findPage(new Page<>(request, response), entity));
    }

}