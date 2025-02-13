package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjEntity;
import com.yunyou.modules.sys.common.service.SysTmsTransportObjService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 业务对象信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/transportObj")
public class SysTmsTransportObjController extends BaseController {
    @Autowired
    private SysTmsTransportObjService sysTmsTransportObjService;

    /**
     * Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysTmsTransportObjEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportObjService.findGrid(new Page<SysTmsTransportObjEntity>(request, response), entity));
    }

    /**
     * Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "settleGrid")
    public Map<String, Object> settleGrid(SysTmsTransportObjEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportObjService.findSettleGrid(new Page<SysTmsTransportObjEntity>(request, response), entity));
    }

}