package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsCustomer;
import com.yunyou.modules.sys.common.service.SysWmsCustomerService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 客户Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/customer")
public class SysWmsCustomerController extends BaseController {
    @Autowired
    private SysWmsCustomerService sysWmsCustomerService;

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysWmsCustomer entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsCustomerService.findGrid(new Page<>(request, response), entity));
    }

}