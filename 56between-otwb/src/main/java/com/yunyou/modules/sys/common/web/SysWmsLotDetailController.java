package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsLotDetail;
import com.yunyou.modules.sys.common.service.SysWmsLotDetailService;
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
 * 批次属性明细Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/lotDetail")
public class SysWmsLotDetailController extends BaseController {
    @Autowired
    private SysWmsLotDetailService sysWmsLotDetailService;

    /**
     * 明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<SysWmsLotDetail> data(SysWmsLotDetail entity) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return sysWmsLotDetailService.findList(entity);
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysWmsLotDetail entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsLotDetailService.findGrid(new Page<>(request, response), entity));
    }

    /**
     * 明细初始化
     */
    @ResponseBody
    @RequestMapping(value = "initialList")
    public List<SysWmsLotDetail> initialList() {
        return sysWmsLotDetailService.initialList();
    }

}