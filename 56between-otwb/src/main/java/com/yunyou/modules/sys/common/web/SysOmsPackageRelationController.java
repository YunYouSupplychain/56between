package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysOmsPackage;
import com.yunyou.modules.sys.common.entity.SysOmsPackageRelation;
import com.yunyou.modules.sys.common.service.SysOmsPackageRelationService;
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
 * 包装明细Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/oms/packageRelation")
public class SysOmsPackageRelationController extends BaseController {
    @Autowired
    private SysOmsPackageRelationService sysOmsPackageRelationService;
    @Autowired
    private SysOmsPackageService sysOmsPackageService;

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysOmsPackageRelation entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        SysOmsPackage sysOmsPackage = sysOmsPackageService.findByPackCode(entity.getPackCode(), entity.getDataSet());
        entity.setCdprCdpaPmCode(null != sysOmsPackage ? sysOmsPackage.getPmCode() : "#");
        return getBootstrapData(sysOmsPackageRelationService.findPage(new Page<>(request, response), entity));
    }

}