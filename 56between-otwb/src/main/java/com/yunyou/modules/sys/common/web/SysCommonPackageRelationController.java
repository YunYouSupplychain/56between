package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysCommonPackage;
import com.yunyou.modules.sys.common.entity.SysCommonPackageRelation;
import com.yunyou.modules.sys.common.service.SysCommonPackageRelationService;
import com.yunyou.modules.sys.common.service.SysCommonPackageService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 包装明细Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/packageRelation")
public class SysCommonPackageRelationController extends BaseController {
    @Autowired
    private SysCommonPackageService sysCommonPackageService;
    @Autowired
    private SysCommonPackageRelationService sysCommonPackageRelationService;

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<SysCommonPackageRelation> data(SysCommonPackageRelation entity) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return sysCommonPackageRelationService.findByPackage(entity.getCdprCdpaPmCode());
    }

    /**
     * 明细初始化
     */
    @ResponseBody
    @RequestMapping(value = "initialList")
    public List<SysCommonPackageRelation> initialList() {
        return sysCommonPackageRelationService.initialList();
    }

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonPackageRelation entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        SysCommonPackage sysCommonPackage = sysCommonPackageService.getByCode(entity.getPackCode(), entity.getDataSet());
        entity.setCdprCdpaPmCode(sysCommonPackage == null ? "#" : sysCommonPackage.getPmCode());
        return getBootstrapData(sysCommonPackageRelationService.findPage(new Page<>(request, response), entity));
    }
}